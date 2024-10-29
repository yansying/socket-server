package com.xzr.socket.socketcontext;



import com.xzr.socket.ano.AuthenticationFailure;
import com.xzr.socket.ano.SocketUrl;
import com.xzr.socket.authen.SocketAuthenticationFilter;
import com.xzr.socket.config.SocketConfig;
import com.xzr.socket.key.DefSocketCacheKeyGenerationStrategy;
import com.xzr.socket.key.SocketCacheKeyGenerationStrategy;
import com.xzr.socket.message.ResponseMessageBean;
import com.xzr.socket.message.SocketMessageInfo;
import com.xzr.socket.message.SocketMessageInterceptor;
import com.xzr.socket.properties.SocketConfigProperties;
import com.xzr.socket.scanner.SocketHandler;
import com.xzr.socket.utils.AnnotationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/**
 * @author xzr
 */
@Component
@RequiredArgsConstructor
@Slf4j

public class SocketServer {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final Validator validator;
    private final SocketConfigProperties socketConfigProperties;
    private final SocketMessageInterceptor socketMessageInterceptor;
    private final DefSocketCacheKeyGenerationStrategy socketCacheKeyGenerationStrategy;
    private final SocketHandler socketHandler;
    private final SocketAuthenticationFilter socketAuthenticationFilter;
    private final SocketConfig socketConfig;


    // 项目启动时自动调用
    @PostConstruct
    public void startServerOnStartup() {
        init();
    }

    /**
     * 用于存储当前认证后客户端（线程安全）
     */
    @Getter
    private final ConcurrentMap<String, SocketMetBean> socketMetBeanCountMap = new ConcurrentHashMap<>();

    /**
     * 热更新Socket服务端口所需属性
     */
    private volatile boolean running = false;
    private ServerSocket serverSocket;


    // 启动 Socket 服务
    public void init() {
        // 如果服务已经在运行，直接返回
        if (running) {
            log.warn("Socket 服务已经在运行，无法重复启动！");
            return;
        }

        int port = socketConfigProperties.getPort();
        try {
            serverSocket = new ServerSocket(port);
            int localPort = serverSocket.getLocalPort();
            log.info("服务端已启动，监听端口: {}", localPort);
            running = true;

            // 等待客户端连接
            new Thread(() -> {
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        log.info("客户端已连接: {}:{}", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
                        threadPoolTaskExecutor.execute(new ClientHandler(clientSocket));
                    } catch (IOException e) {
                        if (running) {
                            log.error("客户端异常断开: {}", e.getMessage());
                        } else {
                            log.info("服务端已关闭，停止接收连接！");
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            log.error("Socket 启动服务端时出错: {}", e.getMessage());
            throw new RuntimeException("Socket 启动服务端时出错: " + e.getMessage());
        }
    }

    // 停止 Socket 服务
    public void stopServer() {
        if (!running) {
            log.warn("服务端已关闭，无法重复关闭！");
            return;
        }

        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                log.info("服务端已关闭成功");
            }
        } catch (IOException e) {
            log.error("关闭ServerSocket时发生异常: {}", e.getMessage());
            throw new RuntimeException("关闭ServerSocket时发生异常: " + e.getMessage());
        }
    }

    // 项目关闭时自动调用，确保服务正确关闭
    @PreDestroy
    public void onShutdown() {
        stopServer();
    }


    /**
     * Socket客户端处理程序
     */
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    PrintWriter writer = new PrintWriter(outputStream, true)
            ) {
                String clientMessage;
                while ((clientMessage = reader.readLine()) != null) {
                    log.info("接收到（{}）的消息: {}", clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort(), clientMessage);
                    // 消息解析
                    Object messageBean  = socketMessageInterceptor.deserialize(clientMessage);

                    // 验证消息合法性
                    if (validateAndProcessResponse(messageBean,writer)) continue;

                    String socketUrl = AnnotationUtils.findSocketUrl1(messageBean);
                    if (Objects.isNull(socketUrl)) {
                        writer.println( (String) socketMessageInterceptor.serialize(messageBean));
                        continue;
                    }

                    try {

                        DefaultSocketAuthentication defaultSocketAuthentication = new DefaultSocketAuthentication(
                                clientSocket, messageBean,
                                reader, writer
                        );
                        String cacheKey = socketCacheKeyGenerationStrategy.generateKey(defaultSocketAuthentication);
                        defaultSocketAuthentication.initSocketKeyAndCertificationResults(cacheKey);
                        SocketContextHolder.getSocketContext().setSocketAuthentication(defaultSocketAuthentication);

                        // 认证处理
                        if (!socketAuthenticationFilter.authenticate(socketUrl)) {
                            LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages = socketConfig.getSocketMessages();
                            for (Class<?> aClass : socketMessages.keySet()) {
                                if (aClass.isInstance(messageBean)) {
                                    // 在这里添加您的逻辑
                                    SocketMessageInfo socketMessageInfo = socketMessages.get(aClass);
                                    String authenticationFailureMethodName = socketMessageInfo.getAuthenticationFailureMethodName();
                                    if (authenticationFailureMethodName != null)  {
                                        Method declaredMethod = messageBean.getClass().getDeclaredMethod(authenticationFailureMethodName, messageBean.getClass());
                                        Object authenticationFailureMessage = declaredMethod.invoke(messageBean, messageBean);
                                        Object serialize = socketMessageInterceptor.serialize(authenticationFailureMessage);
                                        writer.println((String) serialize);
                                        break;
                                    }

                                }
                            }
                            continue;
//                            Method[] declaredMethods = messageBean.getClass().getDeclaredMethods();
//                            Object authenticationFailureMessage = null;
//                            for (Method declaredMethod : declaredMethods) {
//                                if (declaredMethod.isAnnotationPresent(AuthenticationFailure.class)) {
//                                    authenticationFailureMessage  = declaredMethod.invoke(messageBean, messageBean);
//                                    break;
//                                }
//                            }
//                            Object serialize = socketMessageInterceptor.serialize(authenticationFailureMessage);
//                            writer.println((String) serialize);
//                            continue;
                        }


                        //  使用 SocketHandler 调用对应的处理方法
                        Object returnValue = socketHandler.handleRequest(socketUrl, messageBean);
                        Object serialize = socketMessageInterceptor.serialize(returnValue);

                        writer.println((String) serialize);
                        log.info("处理客户端请求: {}", returnValue);
                        log.info("处理客户端请求 序列化后: {}", serialize);
                    } finally {
                        SocketContextHolder.clearSocketContext();

                    }
                }
            } catch (IOException e) {
                log.error("处理客户端连接时出错: {}", e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        @SafeVarargs
        public final <T> Set<ConstraintViolation<T>> validateAndProcess(T... params) {
            Set<ConstraintViolation<T>> violations = new HashSet<>();
            for (T param : params) {
                violations = validator.validate(param);
                if (!violations.isEmpty()) {
                    log.error("参数校验失败: {}", violations);

                }
            }
            return violations;
        }

        private boolean validateAndProcessResponse(Object deserializeData, PrintWriter writer) {
            Set<ConstraintViolation<Object>> constraintViolations = validateAndProcess(deserializeData);
            if (constraintViolations == null || constraintViolations.isEmpty()) {
                return false;
            } else {
                Map<String, String> errors = extractValidationErrors(constraintViolations);
                writer.println( (String) socketMessageInterceptor.serialize(errors));
                return true; // 验证失败
            }
        }

        private <T> Map<String, String> extractValidationErrors(Set<ConstraintViolation<T>> ex) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<?> violation : ex) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }


}

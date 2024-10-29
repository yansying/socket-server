package com.xzr.socket.scanner;

import com.xzr.socket.ano.*;
import com.xzr.socket.config.SocketConfig;
import com.xzr.socket.socketcontext.SocketContextHolder;
import com.xzr.socket.socketcontext.SocketContextHolderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Component
public class SocketHandler {
    private final SocketConfig socketConfig;

    public SocketHandler(SocketConfig socketConfig) {
        this.socketConfig = socketConfig;
    }
    public Object handleRequest(String socketUrl, Object messageBean) throws Exception {
        // 获取映射方法
        SocketHandlerMethod handlerMethod = socketConfig.getHandler(socketUrl);

        if (handlerMethod != null) {
            // 直接调用Spring管理的Bean和方法
            Method method = handlerMethod.getMethod();
            Object controllerInstance = handlerMethod.getControllerInstance();
            Parameter[] parameters = handlerMethod.getParameters();
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(SocketRequestData.class)){
                    SocketRequestData annotation = parameters[i].getAnnotation(SocketRequestData.class);
                    String value = annotation.value();
                    if (value.isEmpty()){
                        args[i] = messageBean;
                    }else {
                        Object currentObject = getCurrentObject(messageBean, value);
                        args[i] = currentObject;
                    }
                }else if (parameters[i].isAnnotationPresent(SocketContextReader.class)){
                    args[i] =  SocketContextHolder.getSocketContext().getSocketAuthentication().getReader();
                }else if (parameters[i].isAnnotationPresent(SocketContextWriter.class)){
                    args[i] =  SocketContextHolder.getSocketContext().getSocketAuthentication().getWriter();
                }else if (parameters[i].isAnnotationPresent(SocketContextKey.class)){
                    args[i] =  SocketContextHolder.getSocketContext().getSocketAuthentication().getSessionId();
                }else if (parameters[i].isAnnotationPresent(SocketContextSocket.class)){
                    args[i] =  SocketContextHolder.getSocketContext().getSocketAuthentication().getSocket();
                }
                else {
                    args[i] = null;
                }
            }

            Object invoke = method.invoke(controllerInstance,args);// 直接调用Spring Bean中的方法
            return invoke;
        } else {
            log.warn("未找到对应的SocketMapping处理方法: {}", socketUrl);
            return null;
        }
    }

    private static Object getCurrentObject(Object messageBean, String value) throws NoSuchFieldException, IllegalAccessException {
        String[] split ;
        try {
           split  = value.split("\\.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Object currentObject = messageBean;
        for (String fieldName : split) {
            Field declaredField = currentObject.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            currentObject = declaredField.get(currentObject);

        }
        return currentObject;
    }

}

package com.xzr.socket.scanner;


import com.xzr.socket.ano.SocketController;
import com.xzr.socket.ano.SocketMapping;
import com.xzr.socket.ano.SocketRequestData;
import com.xzr.socket.config.SocketConfig;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Component
public class SocketControllerScanner {
    private final SocketConfig socketConfig;
    private final ApplicationContext applicationContext;

    public SocketControllerScanner(SocketConfig socketConfig, ApplicationContext applicationContext) {
        this.socketConfig = socketConfig;
        this.applicationContext = applicationContext;
    }
    @PostConstruct
    public void scanSocketControllers() {
        // 从Spring容器中获取所有标注了 @SocketController 的 Bean
        Map<String, Object> socketControllers = applicationContext.getBeansWithAnnotation(SocketController.class);

        for (Object controller : socketControllers.values()) {
            // 获取原始类，而不是代理类
            Class<?> targetClass = AopUtils.getTargetClass(controller);

            Method[] methods = targetClass.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(SocketMapping.class)) {
                    SocketMapping socketMapping = method.getAnnotation(SocketMapping.class);
                    String mappingUrl = socketMapping.value();

                    //获取方法上的参数与对应的注解
                    Parameter[] parameters = method.getParameters();



                    // 注册到Socket请求映射表，controller是Spring管理的Bean，不需要反射
                    socketConfig.registerMapping(mappingUrl, new SocketHandlerMethod(controller, method,parameters));
                }
            }
        }
    }

}

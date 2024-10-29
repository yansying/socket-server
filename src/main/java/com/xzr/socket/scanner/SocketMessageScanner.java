package com.xzr.socket.scanner;


import com.xzr.socket.ano.SocketMessage;
import com.xzr.socket.config.SocketConfig;
import com.xzr.socket.message.SocketMessageInfo;
import com.xzr.socket.utils.AnnotationUtils;
import com.xzr.socket.utils.SocketUrlFinder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于扫描SocketMessage的消息扫描器
 */

@Component
public class SocketMessageScanner {

    private final SocketConfig socketConfig;

    public SocketMessageScanner(SocketConfig socketConfig) {
        this.socketConfig = socketConfig;
    }

    @PostConstruct
    public void scanSocketMessages() throws ClassNotFoundException {
        // 创建扫描器
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);

        // 添加过滤器，查找带有 @SocketMessage 注解的类
        scanner.addIncludeFilter(new AnnotationTypeFilter(SocketMessage.class));

        // 扫描指定包中的类
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents("com.xzr.socket.domain");

        LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages = new LinkedHashMap<>();

        for (BeanDefinition beanDefinition : beanDefinitions) {
            // 获取类对象
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

            // 获取类上的 @SocketMessage 注解
            SocketMessage socketMessageAnnotation = clazz.getAnnotation(SocketMessage.class);

            if (socketMessageAnnotation != null) {
                // 将类和注解信息存储到 SocketConfig 中
                SocketMessageInfo messageInfo = new SocketMessageInfo(
                        socketMessageAnnotation.priority(),
                        socketMessageAnnotation.serializeType()
                );
                // 查找 @SocketUrl 注解字段
//                String socketUrlInFieldNames = AnnotationUtils.findSocketUrlInFieldNames(clazz);
                List<String> socketUrlPaths = SocketUrlFinder.findSocketUrlPaths(clazz);
                String authenticationFailureMethodName = SocketUrlFinder.findAuthenticationFailureMethodName(clazz);
                System.out.println(socketUrlPaths);
//                messageInfo.setSocketUrlInFieldName(socketUrlInFieldNames);
                messageInfo.setSocketUrlPaths(socketUrlPaths);
                messageInfo.setAuthenticationFailureMethodName(authenticationFailureMethodName);
                socketMessages.put( clazz, messageInfo);
            }
        }

        //将socketMessages排序，逻辑为根据value的  priority 属性，1最大
        socketConfig.setSocketMessages(sortSocketMessages(socketMessages));



    }
    public static LinkedHashMap<Class<?>, SocketMessageInfo> sortSocketMessages(LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages) {
        return socketMessages.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getPriority(), Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

}

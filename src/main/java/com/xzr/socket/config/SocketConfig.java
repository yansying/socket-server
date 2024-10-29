package com.xzr.socket.config;


import com.xzr.socket.message.SocketMessageInfo;
import com.xzr.socket.scanner.SocketHandlerMethod;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class SocketConfig {

    // 需要放行的url
    private List<String> allowUrls;


    // 存储扫描到的Bean类及其注解信息
    private LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages = new LinkedHashMap<>();
    private final Map<String, SocketHandlerMethod> handlerMappings = new HashMap<>();

    public void registerMapping(String url, SocketHandlerMethod handlerMethod) {
        handlerMappings.put(url, handlerMethod);
    }

    public SocketHandlerMethod getHandler(String url) {
        return handlerMappings.get(url);
    }

}

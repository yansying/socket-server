package com.xzr.socket.authen;

import com.xzr.socket.config.SocketConfig;
import com.xzr.socket.socketcontext.SocketContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 自定义的认证逻辑
 */
@Component
@RequiredArgsConstructor
public class CustomSocketAuthenticationFilter implements SocketAuthenticationFilter {
    private final SocketConfig socketConfig;

    @Override
    public boolean authenticate(String requestUrl) {
        if (socketConfig.getAllowUrls().contains(requestUrl)) {
            return true;
        }else {
            Boolean certificationResults = SocketContextHolder.getSocketContext().getSocketAuthentication().getCertificationResults();
            return certificationResults != null && certificationResults;
        }

    }
}

package com.xzr.socket.key;

import com.xzr.socket.socketcontext.SocketAuthentication;
import com.xzr.socket.utils.SocketUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 默认IP端口
 */
@Component
public class DefSocketCacheKeyGenerationStrategy implements SocketCacheKeyGenerationStrategy{
    @Override
    public String generateKey(SocketAuthentication socketAuthentication) {
            return SocketUtil.getSocketIPPort(socketAuthentication.getSocket());
    }
}

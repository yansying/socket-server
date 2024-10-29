package com.xzr.socket.key;


import com.xzr.socket.socketcontext.SocketAuthentication;
import com.xzr.socket.utils.SocketUtil;

/**
 * @author xzr
 */

public interface SocketCacheKeyGenerationStrategy {

    /**
     * 生成key
     * @param socketAuthentication
     * @return
     */
     String generateKey(SocketAuthentication socketAuthentication);
}

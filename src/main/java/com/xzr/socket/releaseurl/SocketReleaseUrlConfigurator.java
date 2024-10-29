package com.xzr.socket.releaseurl;

import java.util.List;

/**
 * 释放 url 配置器
 */
public interface SocketReleaseUrlConfigurator {
    /**
     *
     * @return 要释放 url 集合
     */
    List<String> releaseUrl();
}

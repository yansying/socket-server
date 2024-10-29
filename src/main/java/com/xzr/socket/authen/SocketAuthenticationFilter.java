package com.xzr.socket.authen;



public interface SocketAuthenticationFilter {
    /**
     * 验证消息的合法性
     *
     * @param requestUrl 请求url
     * @return 是否认证通过
     */
    boolean authenticate(String requestUrl);
}

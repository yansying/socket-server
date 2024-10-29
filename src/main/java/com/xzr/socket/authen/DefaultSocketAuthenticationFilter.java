package com.xzr.socket.authen;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 默认身份验证过滤器
 */
@Component
@ConditionalOnMissingBean(SocketAuthenticationFilter.class)
public class DefaultSocketAuthenticationFilter implements SocketAuthenticationFilter {

    @Override
    public boolean authenticate(String requestUrl) {
        // 默认的认证逻辑
        // 可以通过 messageBean 获取需要认证的信息，验证其合法性
        // 返回 true 表示认证通过，false 表示失败
        return true; // 默认认证通过
    }

}

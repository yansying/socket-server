package com.xzr.socket.releaseurl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 默认释放url
 */
@Component
@ConditionalOnMissingBean(SocketReleaseUrlConfigurator.class)
public class DefaultSocketReleaseUrlConfigurator implements SocketReleaseUrlConfigurator{
    @Override
    public List<String> releaseUrl() {
        return Collections.singletonList("/login");
    }
}

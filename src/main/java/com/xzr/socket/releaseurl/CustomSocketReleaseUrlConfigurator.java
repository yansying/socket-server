package com.xzr.socket.releaseurl;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 自定义释放url
 */
@Component
public class CustomSocketReleaseUrlConfigurator  implements SocketReleaseUrlConfigurator{
    @Override
    public List<String> releaseUrl() {
        return Collections.singletonList("1001");
    }
}

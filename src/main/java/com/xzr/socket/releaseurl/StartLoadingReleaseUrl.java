package com.xzr.socket.releaseurl;

import com.xzr.socket.config.SocketConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
public class StartLoadingReleaseUrl {
    private final SocketReleaseUrlConfigurator socketReleaseUrlConfigurator;
    private final SocketConfig socketConfig;
    public StartLoadingReleaseUrl(SocketReleaseUrlConfigurator socketReleaseUrlConfigurator, SocketConfig socketConfig) {
        this.socketReleaseUrlConfigurator = socketReleaseUrlConfigurator;
        this.socketConfig = socketConfig;
    }

    @PostConstruct
    public void start() {
        List<String> strings = socketReleaseUrlConfigurator.releaseUrl();
        socketConfig.setAllowUrls(strings);
    }
}

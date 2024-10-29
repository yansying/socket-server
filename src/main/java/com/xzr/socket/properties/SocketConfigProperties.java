package com.xzr.socket.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "socket")
@Data
public class SocketConfigProperties {

    /**
     * socket缓存key生成策略
     */
    private String keyGenerationStrategy;
    private int port;
    //心跳检测间隔时间 单位分钟
    private int heartbeatDetectionInterval;
    // 心跳超时时间 单位分钟
    private int heartbeatTimeout;
}

package com.xzr.socket.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: xzr
 * SocketMessage 的结构
 *
 */
@Getter
@Setter
public class SocketMessageInfo {
    private final int priority;
    private final SerializeType serializeType;
    private String socketUrlInFieldName;
    private List<String> socketUrlPaths;
    private String authenticationFailureMethodName;


    public SocketMessageInfo(int priority, SerializeType serializeType) {
        this.priority = priority;
        this.serializeType = serializeType;
    }

}

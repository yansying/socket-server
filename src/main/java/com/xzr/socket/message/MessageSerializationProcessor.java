package com.xzr.socket.message;

/**
 * 消息序列化处理器
 */
@FunctionalInterface
public interface MessageSerializationProcessor {
    Object process(SocketMessageInfo value);
}

package com.xzr.socket.message;



/**
 * 消息反序列化处理器
 */
@FunctionalInterface
public interface MessageDeserializationProcessor {
    Object process(Class<? > key, SocketMessageInfo value);
//    CommunicationParent process(Class<? extends CommunicationParent> key, SocketMessageInfo value);
}

package com.xzr.socket.message;


import com.xzr.socket.config.SocketConfig;
import com.xzr.socket.utils.SerializerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Socket消息拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketMessageInterceptor {

    private final SocketConfig socketConfig;

    /**
     * 处理反序列化
     * @param message 消息
     * @return 标记@SocketMessage的JavaBean
     */
    public Object deserialize(Object message) {
        return SocketMessageIterator.iterateAndProcess(socketConfig.getSocketMessages(), (key, value) ->
                SerializerUtil.deserializeFrom(message, key, value.getSerializeType())

        );
    }
   /* public CommunicationParent deserialize(Object message) {
        return SocketMessageIterator.iterateAndProcess(socketConfig.getSocketMessages(), (key, value) ->
             SerializerUtil.deserializeFrom(message, key, value.getSerializeType())

        );
    }*/

    /**
     * 处理序列化
     * @param data JavaBean
     * @return 序列化数据
     */
    public Object serialize(Object data) {
        return SocketMessageIterator.iterateAndProcess(socketConfig.getSocketMessages(), ( value) ->
                SerializerUtil.serialize(data, value.getSerializeType())

        );

    }



}

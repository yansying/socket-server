package com.xzr.socket.message;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *Socket消息迭代器
 */
@Slf4j
public class SocketMessageIterator {

    /**
     * 反序列化迭代
     * @param socketMessages  标记@SocketMessage集合
     * @param processor 消息处理器
     * @return 反序列化后的JavaBean
     */
    public static Object iterateAndProcess(LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages,
                                                        MessageDeserializationProcessor processor) {
        for (Map.Entry<Class<?>, SocketMessageInfo> entry : socketMessages.entrySet()) {
            Class<?> key = entry.getKey();
            SocketMessageInfo value = entry.getValue();
            try {
                Object process = processor.process(key, value);
                if (Objects.nonNull(process)) return process;
            } catch (Exception e) {
                log.info("反序列化失败:{}", e.getMessage());
                throw new RuntimeException("反序列化失败" + e.getMessage());
            }
        }
        return null;
    }
   /* public static CommunicationParent iterateAndProcess(LinkedHashMap<Class<? extends CommunicationParent>, SocketMessageInfo> socketMessages,
                                                        MessageDeserializationProcessor processor) {
        for (Map.Entry<Class<? extends CommunicationParent>, SocketMessageInfo> entry : socketMessages.entrySet()) {
            Class<? extends CommunicationParent> key = entry.getKey();
            SocketMessageInfo value = entry.getValue();
            try {
                CommunicationParent process = processor.process(key, value);
                if (Objects.nonNull(process)) return process;
            } catch (Exception e) {
                log.info("反序列化失败:{}", e.getMessage());
                throw new RuntimeException("反序列化失败" + e.getMessage());
            }
        }
        return null;
    }*/

    /**
     * 序列化迭代
     * @param socketMessages 标记@SocketMessage集合
     * @param processor      消息处理器
     * @return 序列化后的数据
     */
    public static Object iterateAndProcess(LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages,
                                           MessageSerializationProcessor processor) {
        for (Map.Entry<Class<?>, SocketMessageInfo> entry : socketMessages.entrySet()) {
            SocketMessageInfo value = entry.getValue();
            try {
                Object process = processor.process( value);
                if (Objects.nonNull(process)) return process;
            } catch (Exception e) {
                log.info("序列化失败:{}", e.getMessage());
                throw new RuntimeException("序列化失败" + e.getMessage());
            }
        }
        return null;
    }
 /*   public static Object iterateAndProcess(LinkedHashMap<Class<? extends CommunicationParent>, SocketMessageInfo> socketMessages,
                                                        MessageSerializationProcessor processor) {
        for (Map.Entry<Class<? extends CommunicationParent>, SocketMessageInfo> entry : socketMessages.entrySet()) {
            SocketMessageInfo value = entry.getValue();
            try {
                Object process = processor.process( value);
                if (Objects.nonNull(process)) return process;
            } catch (Exception e) {
                log.info("序列化失败:{}", e.getMessage());
                throw new RuntimeException("序列化失败" + e.getMessage());
            }
        }
        return null;
    }*/
}

package com.xzr.socket.utils;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzr.socket.message.SerializeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SerializerUtil {

    private static final ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
    private static final Logger log = LoggerFactory.getLogger(SerializerUtil.class);


    // 将对象序列化为指定类型的字符串
    public static Object serialize(Object data, SerializeType serializeType)  {
        switch (serializeType) {
            case JSON:
                try {
                    return objectMapper.writeValueAsString(data);
                } catch (JsonProcessingException e) {
                    log.info("JSON序列化失败", e);
                    throw new RuntimeException(e);
                }
//                JSONConfig jsonConfig = new JSONConfig();
//                jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
//                // 将对象序列化为JSON字符串
//                return JSONUtil.toJsonStr(data, jsonConfig);
            case PROTOBUF:
                // todo
                return null;
            case KRYO:
                // todo
                return null;
            // 你可以添加其他序列化方式
            default:
                return null;
        }
    }

    // 将指定类型的字符串反序列化为对象
    public static Object deserializeFrom(Object message, Class<?> key, SerializeType serializeType)  {
        switch (serializeType) {
            case JSON:
                try {
                    return objectMapper.readValue((String) message, key);
                } catch (JsonProcessingException e) {
                    log.error("反序列化失败", e);
                    throw new RuntimeException(e);
                }
                // 将JSON字符串反序列化为对象
//                return JSONUtil.toBean((String) message,key);
            case PROTOBUF:
                // todo
                return null;
            case KRYO:
                // todo
                return null;
            // 你可以添加其他序列化方式
            default:
                return null;
        }
    }
  /*  public static CommunicationParent deserializeFrom(Object message, Class<? extends CommunicationParent> key, SerializeType serializeType) {
        switch (serializeType) {
                case JSON:
                    // 将JSON字符串反序列化为对象
                    return JSONUtil.toBean((String) message,key);
                case PROTOBUF:
                    // todo
                    return null;
                case KRYO:
                    // todo
                    return null;
                // 你可以添加其他序列化方式
                default:
                    return null;
            }
    }*/
}

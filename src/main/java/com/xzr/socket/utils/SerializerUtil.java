package com.xzr.socket.utils;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.xzr.socket.message.SerializeType;


public class SerializerUtil {



    // 将对象序列化为指定类型的字符串
    public static Object serialize(Object data, SerializeType serializeType) {
        switch (serializeType) {
            case JSON:
                JSONConfig jsonConfig = new JSONConfig();
                jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
                // 将对象序列化为JSON字符串
                return JSONUtil.toJsonStr(data, jsonConfig);
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
    public static Object deserializeFrom(Object message, Class<?> key, SerializeType serializeType) {
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

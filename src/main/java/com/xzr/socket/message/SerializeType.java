package com.xzr.socket.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xzr
 * 序列化类型
 */
@Getter
@AllArgsConstructor
public enum SerializeType {
    JSON(1),
    PROTOBUF(2),
    KRYO(3)
    ;

    private final int code;

}

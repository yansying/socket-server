package com.xzr.socket.ano;


import com.xzr.socket.message.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author xzr
 * 标注在JavaBean上，表示socket通信时的协议，可指定bean的序列化与反序列化
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketMessage {
    // JavaBean解析的优先级，1为最高
    int priority() default 1;

    // 序列化/反序列化方式
    SerializeType serializeType() default SerializeType.JSON;
}

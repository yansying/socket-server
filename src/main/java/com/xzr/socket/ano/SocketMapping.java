package com.xzr.socket.ano;

import java.lang.annotation.*;

/**
 * 标注在方法上，类似与@PostMapping，根据标注SocketUrl的属性定位
 */
@Target(ElementType.METHOD) // 该注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 该注解在运行时保留
@Documented // 该注解将被包含在javadoc中
public @interface SocketMapping {
    String value();
}



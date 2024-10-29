package com.xzr.socket.ano;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在类上类似于 @RestController
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component // 添加@Component，让Spring自动管理
public @interface SocketController {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";



}

package com.xzr.socket.ano;
import java.lang.annotation.*;
@Target(ElementType.METHOD) // 该注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 该注解在运行时保留
@Documented
public @interface AuthenticationFailure {
}

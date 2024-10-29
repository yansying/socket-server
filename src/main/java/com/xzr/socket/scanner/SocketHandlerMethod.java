package com.xzr.socket.scanner;

import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Getter
public class SocketHandlerMethod {
    private final Object controllerInstance;
    private final Method method;
    private final Parameter[] parameters;

    public SocketHandlerMethod(Object controllerInstance, Method method,Parameter[] parameters) {
        this.controllerInstance = controllerInstance;
        this.method = method;
        this.parameters = parameters;
    }

}

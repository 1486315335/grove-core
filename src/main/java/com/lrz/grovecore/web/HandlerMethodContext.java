package com.lrz.grovecore.web;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;

/**
 * controller 请求方法上下文
 */
public class HandlerMethodContext {

    static ThreadLocal<HandlerMethod> handlerMethodThreadLocal = new NamedThreadLocal<>("请求处理Handler上下文");

    public static void set(HandlerMethod handlerMethod) {
        handlerMethodThreadLocal.set(handlerMethod);
    }

    public static void remove() {
        handlerMethodThreadLocal.remove();
    }

    public static HandlerMethod get() {
        return handlerMethodThreadLocal.get();
    }
}
package com.lrz.grovecore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前 HTTP 请求分派线程内的方法缓存，需通过 Spring 代理调用 public 方法。
 * 不支持 static/final 方法或同一对象内部的直接调用。
 * 缓存非 null 的成功结果；异常和 null 结果不缓存。
 * 默认键为“类名#方法名”，适合无副作用的查询方法。
 *
 * @author lirenzhi
 * @since 2022/12/15 4:05 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestCached {

    /**
     * 可选的 MVEL 表达式，例如 "'userById:' + args[0]"；可使用 self 和 args。
     * 为空时使用“类名#方法名”，不区分参数、重载方法和同类的不同实例。
     * 需要区分参数时应显式设置 key；自定义键需包含业务前缀，避免不同方法之间重复。
     */
    String key() default "";
}

package com.lrz.grovecore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法缓存注解，如果一个方法会被循环调用，可加上这个注解
 *
 * @author lirenzhi
 * @since 2022/12/15 4:05 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestCached {

    String key() default  "";
}

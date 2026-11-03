package com.lrz.grovecore.utils;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 对象工具类
 *
 * @author lirenzhi
 * @since 2023/3/31 5:39 PM
 */
public class ObjectUtil extends ObjectUtils {

    /**
     * 判断一个对象是否为空或者empty
     *
     * @param t t
     * @return boolean
     */
    public static <T> boolean isNullOrEmpty(T t) {
        if (t == null) {
            return true;
        }
        if (t.getClass().isArray()) {
            return Array.getLength(t) == 0;
        }
        if (t instanceof Collection) {
            return ((Collection<?>) t).isEmpty();
        }
        if (t instanceof Map) {
            return ((Map<?, ?>) t).isEmpty();
        }
        if (t instanceof Optional) {
            return !((Optional<?>) t).isPresent();
        }
        if (t instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) t;
            for (int i = 0; i < charSequence.length(); i++) {
                if (!Character.isWhitespace(charSequence.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * 判断一个对象是否不为空或者empty
     *
     * @param t t
     * @return boolean
     */
    public static <T> boolean nonNullOrEmpty(T t) {
        return !isNullOrEmpty(t);
    }

    /**
     * 如果supplier.get不为空，则调用consumer
     *
     * @param supplier 供应商
     * @param consumer 消费者
     */
    public static <T> void setIfNonNull(Supplier<T> supplier, Consumer<T> consumer) {
        Optional.ofNullable(supplier.get()).ifPresent(consumer);
    }

    /**
     * 如果t为空，则返回默认值，否则返回本身
     *
     * @param t        t
     * @param defaults 默认值
     * @return {@link T}
     */
    @SafeVarargs
    public static <T> T nvl(T t, T... defaults) {
        if (nonNullOrEmpty(t)) {
            return t;
        }
        if (defaults == null) {
            return null;
        }
        for (T aDefault : defaults) {
            if (nonNullOrEmpty(aDefault)) {
                return aDefault;
            }
        }
        return null;
    }

    /**
     * nvl,优化方法，不会导致后面的表达式被执行
     *
     * @param t         t
     * @param suppliers 供应商
     * @return {@link T}
     */
    @SafeVarargs
    public static <T> T nvl(T t, Supplier<T>... suppliers) {
        if (nonNullOrEmpty(t)) {
            return t;
        }
        if (suppliers == null) {
            return null;
        }
        for (Supplier<T> supplier : suppliers) {
            T t1 = supplier.get();
            if (t1 != null) {
                return t1;
            }
        }
        return null;
    }

    /** 所有的都是空对象 */
    public static boolean isAllNullOrEmpty(Object ... objects) {
        return ListUtil.adaptArray(objects).allSatisfy(ObjectUtil::isNullOrEmpty);
    }

    /** 一个空对象都没有 */
    public static boolean isNoneNullOrEmpty(Object ... objects) {
        return ListUtil.adaptArray(objects).allSatisfy(ObjectUtil::nonNullOrEmpty);
    }

    /** 任意一个为空或者null */
    public static boolean isAnyNullOrEmpty(Object ... objects) {
        return nvl(null, objects) == null;
    }


    /**
     * 如果为空，则取默认值
     */
    public static <T> T ifNullOrEmpty(T value, T defaultValue) {
        return isNullOrEmpty(value) ? defaultValue : value;
    }
}

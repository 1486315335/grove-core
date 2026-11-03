package com.lrz.grovecore.utils;

import com.lrz.grovecore.function.Executable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 断言
 *
 * @author lirenzhi
 * @since 2023/3/30 11:05 AM
 */
public final class Assert {

    /**
     * 断言这个 expression 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression 表达式
     * @param ex         异常
     */
    public static void isTrue(boolean expression, RuntimeException ex) {
        if (!expression) {
            throw ex;
        }
    }

    /**
     * 断言这个 expression 为 false
     * <p>为 false 则抛出异常</p>
     *
     * @param expression 表达式
     * @param ex         异常
     */
    public static void isFalse(boolean expression, RuntimeException ex) {
        isTrue(!expression, ex);
    }

    public static void isNull(Object object, RuntimeException ex) {
        isTrue(object == null, ex);
    }

    public static void nonNull(Object object, RuntimeException ex) {
        isTrue(object != null, ex);
    }

    public static void notEmpty(String str, RuntimeException ex) {
        isTrue(StringUtil.isNotEmpty(str), ex);
    }

    public static void notEmpty(Collection<?> collection, RuntimeException ex) {
        isTrue(ListUtil.isNotEmpty(collection), ex);
    }

    public static void notEmpty(Map<?, ?> map, RuntimeException ex) {
        isTrue(MapUtil.isNotEmpty(map), ex);
    }

    public static void notEmpty(Object[] array, RuntimeException ex) {
        isTrue(ListUtil.adaptArray(array).notEmpty(), ex);
    }

    public static void isEmpty(Map<?, ?> map, RuntimeException ex) {
        isTrue(MapUtil.isEmpty(map), ex);
    }

    public static void nonException(Executable executable, RuntimeException ex) {
        try {
            executable.execute();
        } catch (Exception e) {
            throw ex;
        }
    }

    public static <T> T nonException(Supplier<T> supplier, RuntimeException ex) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw ex;
        }
    }

    public static <T> void nonException(T t, Consumer<T> consumer, RuntimeException ex) {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw ex;
        }
    }

    public static <T, R> R nonException(T t, Function<T, R> function, RuntimeException ex) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw ex;
        }
    }
}

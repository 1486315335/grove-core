package com.lrz.grovecore.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类工具
 *
 * @author lirenzhi
 * @since 2023/8/15 11:12 AM
 */
public final class ClassUtil {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 对指定的类进行初始化
     *
     * @param clazz clazz
     * @return {@link T}
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> newInstance(List<Class<? extends T>> classList) {
        List<T> list = ListUtil.empty();
        for (Class<? extends T> aClass : classList) {
            list.add(newInstance(aClass));
        }
        return list;
    }

    /**
     * 递归获取字段定义
     *
     * @param clazz        clazz
     * @param propertyName 属性名
     * @return {@link Field}
     */
    public static Field getFieldRecursion(Class<?> clazz, String propertyName) {
        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(propertyName);
        } catch (NoSuchFieldException ignored) {
        }
        if (declaredField == null && clazz.getSuperclass() != null) {
            declaredField = getFieldRecursion(clazz.getDeclaringClass(), propertyName);
        }
        return declaredField;
    }

    public static <T> List<Map<String, Object>> beanToListMap(List<T> list, Class<T> clazz) {
        return ListUtil.collect(list, x -> beanToMap(x, clazz));
    }

    public static <T> Map<String, Object> beanToMap(T x, Class<T> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String name = declaredField.getName();
            Object value = null;
            try {
                value = declaredField.get(x);
            } catch (IllegalAccessException ignored) {
            }
            map.put(name, value);
        }
        return map;
    }
}

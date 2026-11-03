package com.lrz.grovecore.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.List;

/**
 * @author lirenzhi
 * @since 2023/1/29 4:23 下午
 */
@SuppressWarnings("ALL")
public class ListenerRegister {
    private static Multimap<Class<? extends Listener>, Listener> registerMap;

    static {
        registerMap = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    /**
     * 注册相应的监听器
     *
     * @param listener 监听器
     */
    public static void register(Listener listener) {
        if (registerMap.containsValue(listener)) {
            return;
        }
        Class<? extends Listener> clazz = listener.getClass();
        for (Class<?> interfaceClass : clazz.getInterfaces()) {
            if (Listener.class.isAssignableFrom(interfaceClass))
                register((Class<? extends Listener>) interfaceClass, listener);
        }
    }

    private static void register(Class<? extends Listener> interfaceClass, Listener listener) {
        if (interfaceClass == Listener.class) return;

        registerMap.put(interfaceClass, listener);

        for (Class<?> extendsInterfaceClass : interfaceClass.getInterfaces()) {
            register((Class<? extends Listener>) extendsInterfaceClass, listener);
        }
    }

    /**
     * 获取注册指定接口的所有监听器列表
     *
     * @param interfaceClass 接口类
     * @return {@link List}<{@link T}>
     */
    public static <T extends Listener> List<T> registeredListener(Class<T> interfaceClass) {
        return Lists.newArrayList((List<T>) registerMap.get(interfaceClass));
    }
}

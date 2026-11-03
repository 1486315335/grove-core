package com.lrz.grovecore.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程的缓存清理
 *
 * @author lirenzhi
 * @since 2022/12/15 4:41 下午
 */
public class RequestCacheContext {

    static ThreadLocal<ConcurrentHashMap<String, Object>> threadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public static Object get(String key) {
        return threadLocal.get().get(key);
    }

    public static void set(String key, Object obj) {
        threadLocal.get().put(key, obj);
    }

    public static void remove() {
        threadLocal.get().clear();
    }
}

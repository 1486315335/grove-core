package com.lrz.grovecore.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前请求分派线程的缓存，由 RequestCacheFilter 创建和清理。
 * 不向异步任务传播，也不跨异步分派复用。
 *
 * @author lirenzhi
 * @since 2022/12/15 4:41 下午
 */
public class RequestCacheContext {

    private static final ThreadLocal<ConcurrentHashMap<String, Object>> threadLocal = new ThreadLocal<>();

    public static void begin() {
        threadLocal.set(new ConcurrentHashMap<>());
    }

    public static boolean isActive() {
        return threadLocal.get() != null;
    }

    public static Object get(String key) {
        ConcurrentHashMap<String, Object> cache = threadLocal.get();
        return cache == null ? null : cache.get(key);
    }

    public static void set(String key, Object obj) {
        ConcurrentHashMap<String, Object> cache = threadLocal.get();
        if (cache != null) {
            cache.put(key, obj);
        }
    }

    public static void remove() {
        threadLocal.remove();
    }
}

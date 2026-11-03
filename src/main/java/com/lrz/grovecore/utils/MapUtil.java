package com.lrz.grovecore.utils;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Map工具类
 *
 * @author lirenzhi
 * @since 2023/02/22
 */
public final class MapUtil {

    public static <K, V> MutableMap<K, V> empty() {
        return Maps.mutable.empty();
    }

    public static <K, V> MutableMap<K, V> adaptIterable(Map<K, V> map) {
        if (map.isEmpty()) return empty();
        return Maps.mutable.ofMap(map);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || adaptIterable(map).isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> boolean containsAnyKey(Map<K, V> map, Iterable<K> keys) {
        return ListUtil.adaptIterable(keys).anySatisfy(map::containsKey);
    }

    public static <K, V> List<V> getKeys(Map<K, V> map, Iterable<K> keys) {
        keys = ListUtil.adaptIterable(keys).distinct();
        if (ListUtil.isEmpty(keys)) return ListUtil.empty();

        return ListUtil.collect(keys, map::get);
    }

    public static <K, V> Map<K, V> of(K k, V v) {
        HashMap<K, V> kvHashMap = new HashMap<>();
        kvHashMap.put(k, v);
        return kvHashMap;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1) {
        Map<K, V> of = of(k, v);
        of.put(k1, v1);
        return of;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1, K k2, V v2) {
        Map<K, V> of = of(k, v, k1, v1);
        of.put(k2, v2);
        return of;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> of = of(k, v, k1, v1, k2, v2);
        of.put(k3, v3);
        return of;
    }

    public static <K, V> LinkedHashMap<K, V> toLinkedHashMap(Map<K, V> map) {
        return new LinkedHashMap<>(map);
    }
}

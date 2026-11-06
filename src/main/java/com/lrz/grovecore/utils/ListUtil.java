package com.lrz.grovecore.utils;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.lrz.grovecore.envm.ErrorCodeEnum;
import com.lrz.grovecore.exception.SplitParametersException;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 集合工具类
 *
 * @author lirenzhi
 * @since 2022/12/13 4:37 下午
 */
public final class ListUtil {

    public static <T> MutableList<T> empty() {
        return Lists.mutable.empty();
    }

    /**
     * 对List进行包装，此方法空安全，为null时返回空集合
     *
     * @param list 列表
     * @return {@link MutableList}<{@link E}>
     */
    public static <E> MutableList<E> adaptIterable(Iterable<E> list) {
        if (list == null) return empty();
        return Lists.mutable.ofAll(list);
    }

    public static boolean isEmpty(Iterable<?> list) {
        return list == null || adaptIterable(list).isEmpty();
    }

    public static boolean isNotEmpty(Iterable<?> list) {
        return !isEmpty(list);
    }

    /**
     * 分割列表
     * <p>
     * Arrays.asList(1,2,3,4,5,6,7) , 2  ---->  [[1, 2], [3, 4], [5, 6], [7]]
     *
     * @param source 源
     * @param range  范围
     * @return {@link List}<{@link List}<{@link T}>>
     */
    public static <T> List<List<T>> splitList(List<T> source, int range) {
        if (range <= 0) throw new SplitParametersException(ErrorCodeEnum.SPLITTING_PARAMETER_EXCEPTION.getMessage());
        if (null == source || source.isEmpty()) return Collections.emptyList();

        List<List<T>> result = new ArrayList<>();
        if (source.size() > range) {
            int remainder = source.size() % range;
            int times = source.size() / range;
            int offset = 0;
            for (int i = 0; i < times; i++) {
                result.add(source.subList(offset, offset += range));
            }
            if (remainder > 0) {
                result.add(source.subList(offset, offset + remainder));
            }
        } else {
            result.add(source);
        }
        return result;
    }

    /**
     * 从列表中查找一个对象，若符合条件者有多个，返回第一个
     *
     * @param list      列表
     * @param predicate 谓词
     * @return {@link Optional}<{@link T}>
     */
    public static <T> Optional<T> findFirst(List<T> list, Predicate<T> predicate) {
        MutableList<T> mutableList = adaptIterable(list).select(predicate::test);
        if (!ObjectUtils.isEmpty(mutableList)) {
            return Optional.of(mutableList.get(0));
        }
        return Optional.empty();
    }

    /**
     * 从列表中查找符合条件列表，返回
     *
     * @param list      列表
     * @param predicate 谓词
     * @return {@link MutableList}<{@link T}>
     */
    public static <T> MutableList<T> select(List<T> list, Predicate<T> predicate) {
        return adaptIterable(list).select(predicate::test);
    }

    public static <T> boolean equals(List<T> expected, List<T> actual) {
        if (expected == actual) return true;
        if (expected == null || actual == null) return false;
        if (expected.size() != actual.size()) return false;
        List<T> tempList = adaptIterable(expected);
        tempList.removeAll(actual);
        return tempList.isEmpty();
    }

    /**
     * 从List中收集对象
     *
     * @param list     列表
     * @param function 函数
     * @return {@link MutableList}<{@link R}>
     */
    public static <T, R> MutableList<R> collect(Iterable<T> list, Function<T, R> function) {
        return adaptIterable(list).collect(function::apply);
    }

    /**
     * 将集合中的某个字段抽取出来，指定分隔符拼接成字符串
     *
     * @param list      列表
     * @param function  函数
     * @param separator 分隔符
     * @return {@link String}
     */
    public static <T> String makeString(Iterable<T> list, Function<T, String> function, boolean rejectNull, String... separator) {
        if (ObjectUtils.isEmpty(list)) return null;

        String defaultSeparator = ",";

        if (separator != null && separator.length > 0) {
            defaultSeparator = separator[0];
        }
        return collect(list, function).reject(x -> rejectNull && Objects.isNull(x)).makeString(defaultSeparator);
    }

    public static String makeString(Object[] items, String separator, boolean rejectNull) {
        return adaptItems(items).reject(x -> rejectNull && Objects.isNull(x)).makeString(separator);
    }

    /**
     * 拼接字符串
     *
     * @param list      集合
     * @param separator 分隔符
     * @return {@link String}
     */
    public static <T> String makeString(Iterable<T> list, String separator, boolean rejectNull) {
        return adaptIterable(list).reject(x -> rejectNull && Objects.isNull(x)).makeString(separator);
    }

    /**
     * 拼接字符串
     *
     * @param list 列表
     * @return {@link String}
     */
    public static <T> String makeString(Iterable<T> list, boolean rejectNull) {
        return adaptIterable(list).reject(x -> rejectNull && Objects.isNull(x)).makeString("");
    }

    /**
     * 拼接字符串
     * "[1, 2, 4, null, 5]" 会有中括号
     *
     * @param separator 分隔符
     * @param list      列表
     * @return {@link String}
     */
    public static String makeString(String separator, boolean rejectNull, Object... list) {
        return adaptArray(list).reject(x -> rejectNull && Objects.isNull(x)).makeString(separator);
    }

    public static <T> List<T> subList(List<T> list, int size) {
        if (list.size() >= size) {
            return list.subList(0, size);
        }
        return list;
    }

    /**
     * 物理分页
     * 慎用，按理分页应该是用sql分页
     *
     * @param list     列表
     * @param pageNum  页面索引
     * @param pageSize 页面大小
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> page(List<T> list, Long pageNum, Long pageSize) {
        int start = Math.toIntExact((pageNum - 1) * pageSize);
        int end = Math.toIntExact(start + pageSize);
        return subList(list, start, end);
    }

    public static <T> List<T> subList(List<T> list, int start, int end) {
        MutableList<T> es = adaptIterable(list);
        if (start > es.size()) {
            return empty();
        }
        end = Math.min(end, es.size());
        return es.subList(start, end);
    }

    /**
     * key会去重，null值会报错
     *
     * @param list     列表
     * @param function 函数
     * @return {@link MutableMap}<{@link V}, {@link T}>
     */
    public static <T, V> MutableMap<V, T> toMap(Iterable<T> list, Function<T, V> function) {
        return toMap(list, function, Function.identity());
    }

    public static <T, K, V> MutableMap<K, V> toMap(Iterable<T> list, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        return adaptIterable(list).toMap(keyFunction::apply, valueFunction::apply);
    }

    /**
     * 返回一个key排序的Map
     *
     * @param list          列表
     * @param keyFunction   关键功能
     * @param valueFunction 值函数
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public static <T, K extends Comparable<K>, V> LinkedHashMap<K, V> toLinkedMap(Collection<T> list, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        TreeMap<K, V> treeMap = new TreeMap<>();
        forEach(list, x -> treeMap.put(keyFunction.apply(x), valueFunction.apply(x)));
        return new LinkedHashMap<>(treeMap);
    }

    /**
     * List分组
     * <p>
     * List ---> Map<K, List>
     *
     * @param list     列表
     * @param function 函数
     * @return {@link Map}<{@link K}, {@link List}<{@link V}>>
     */
    public static <K extends Comparable<K>, V> Map<K, List<V>> toMapList(Collection<V> list, Function<V, K> function) {
        Map<K, List<V>> map = new HashMap<>();
        adaptIterable(list).groupBy(function::apply).forEachKeyMutableList((a, b) -> {
            map.put(a, adaptIterable(b));
        });
        return map;
    }

    /**
     * List分组, 可自定义value映射
     *
     * @param list          列表
     * @param keyFunction   键功能
     * @param valueFunction 值函数
     * @return {@link Map}<{@link K}, {@link List}<{@link X}>>
     */
    public static <K extends Comparable<K>, V, X> Map<K, List<X>> toMapList(Collection<V> list, Function<V, K> keyFunction, Function<V, X> valueFunction) {
        Map<K, List<X>> map = new HashMap<>();
        adaptIterable(list).groupBy(keyFunction::apply).forEachKeyMutableList((a, b) -> {
            map.put(a, collect(b, valueFunction));
        });
        return map;
    }

    /**
     * alias for toMapList
     *
     * @param list     列表
     * @param function 函数
     * @return {@link Map}<{@link K}, {@link List}<{@link T}>>
     */
    public static <T, K extends Comparable<K>> Map<K, List<T>> groupBy(Collection<T> list, Function<T, K> function) {
        return toMapList(list, function);
    }

    @SafeVarargs
    public static <T> MutableList<T> of(T... items) {
        return adaptItems(items);
    }

    public static <T> MutableList<T> adaptEnumeration(Enumeration<T> enumeration) {
        MutableList<T> mutableList = empty();
        forEach(enumeration, mutableList::add);
        return mutableList;
    }

    /**
     * 展开Map中的entry为一个字符串对象
     *
     * @param map       地图
     * @param separator 分隔符
     * @return {@link List}<{@link String}>
     */
    public static List<String> flatMapToList(Map<String, Object> map, String separator) {
        return adaptIterable(map.keySet()).sortThis()
                .collect(x -> {
                    Object value = map.get(x);
                    if (value == null) {
                        value = "";
                    }
                    return String.join(separator, x, value.toString());
                });
    }

    public static <T> List<T> distinct(List<T> list) {
        return adaptIterable(list).distinct();
    }

    @SafeVarargs
    public static <T> List<T> merge(List<T> list, T... items) {
        if (list.isEmpty()) return empty();
        list.addAll(adaptArray(items));
        return list;
    }

    public static boolean containsIgnoreCase(Iterable<String> list, String item) {
        MutableList<String> newList = adaptIterable(list);
        if (newList == null) return false;
        if (newList.contains(item)) return true;
        return newList.anySatisfy(x -> x.equalsIgnoreCase(item));
    }

    /**
     * 分割并处理
     *
     * @param list     列表
     * @param size     大小
     * @param consumer 消费者
     */
    public static <T> void splitAndProcess(List<T> list, int size, Consumer<List<T>> consumer) {
        if (list.size() == size) {
            consumer.accept(list);
            return;
        }

        int fromIndex = 0;
        int toIndex = size;

        while (fromIndex < list.size()) {
            List<T> ts = list.subList(fromIndex, toIndex);
            consumer.accept(ts);
            fromIndex = toIndex;
            toIndex += size;
            toIndex = Math.min(toIndex, list.size());
        }
    }

    public static <T> boolean containsAny(List<T> list, List<T> containsList) {
        list = adaptIterable(list);
        if (ObjectUtils.isEmpty(containsList)) return false;

        return adaptIterable(containsList).anySatisfy(list::contains);
    }

    public static <T> MutableList<T> parseString(String parseString, String separator, Function<String, T> function) {
        if (ObjectUtils.isEmpty(parseString)) return empty();

        return adaptArray(parseString.split(separator)).collect(x -> {
            try {
                return function.apply(x);
            } catch (Exception e) {
                return null;
            }
        }).reject(Objects::isNull);
    }

    /**
     * 将 List<{@link List}<{@link T}>> 结构展开成 List<{@link T}>
     *
     * @param lists 列表
     * @return {@link MutableList}<{@link T}>
     */
    public static <T> MutableList<T> flatMap(List<List<T>> lists) {
        MutableList<T> value = empty();
        adaptIterable(lists).select(ListUtil::isNotEmpty).forEach(value::addAll);
        return value;
    }

    /**
     * 查找两个List中不同项
     * <p>
     * 相同的会去掉，不同的会分成左右两个Map
     *
     * @param left        左
     * @param right       正确
     * @param keyFunction 关键功能
     * @return {@link MapDifference}<{@link K}, {@link X}>
     */
    public static <X, K> MapDifference<K, X> difference(Iterable<X> left, Iterable<X> right, Function<X, K> keyFunction) {
        Map<K, X> leftMap = toMap(left, keyFunction);
        Map<K, X> rightMap = toMap(right, keyFunction);

        return Maps.difference(leftMap, rightMap);
    }

    public static <T> void forEach(Collection<T> list, Consumer<T> consumer) {
        adaptIterable(list).forEach(consumer::accept);
    }

    public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
        while (enumeration.hasMoreElements()) {
            T nextElement = enumeration.nextElement();
            consumer.accept(nextElement);
        }
    }

    public static <T> T safeGet(List<T> groupColumns, int i) {
        try {
            return adaptIterable(groupColumns).get(i);
        } catch (Exception ex) {
            return null;
        }
    }

    public static <T> T get(List<T> list, Predicate<T> predicate) {
        return findFirst(list, predicate).orElse(null);
    }

    @SafeVarargs
    public static <T> MutableList<T> adaptItems(T... items) {
        return adaptArray(items);
    }

    public static <T> MutableList<T> adaptArray(T[] array) {
        if (ObjectUtils.isEmpty(array)) return empty();

        return Lists.mutable.of(array);
    }
}

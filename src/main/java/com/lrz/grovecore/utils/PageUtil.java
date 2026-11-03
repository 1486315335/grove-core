package com.lrz.grovecore.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 分页工具类
 *
 * @author lirenzhi
 * @since 2023/5/31 2:53 PM
 */
public final class PageUtil {

    /**
     * 判断页是否为空页
     *
     * @param page 页面
     * @return boolean
     */
    public static boolean isEmpty(Page<?> page) {
        return page == null || ListUtil.isEmpty(page.getRecords());
    }

    /**
     * 消费页里的数据
     *
     * @param page     页
     * @param consumer 消费者
     */
    public static <T> void consumer(Page<T> page, Consumer<T> consumer) {
        if (isEmpty(page)) {
            return;
        }
        page.getRecords().forEach(consumer);
    }

    /**
     * 下一页
     *
     * @param page 页
     */
    public static void nextPage(Page<?> page) {
        long current = page.getCurrent();
        page.setCurrent(current + 1);
    }

    /**
     * 对页数据进行收集
     *
     * @param page     页面
     * @param function 函数
     * @return {@link List}<{@link R}>
     */
    public static <T, R> List<R> collect(Page<T> page, Function<T, R> function) {
        return ListUtil.collect(page.getRecords(), function);
    }

    /**
     * 对数据页的数据进行转换
     *
     * @param page     页
     * @param function 函数
     * @return {@link Page}<{@link R}>
     */
    public static <T, R> Page<R> transform(Page<T> page, Function<T, R> function) {
        Page<R> rPage = Page.of(page.getCurrent(), page.getSize(), page.getTotal());
        return rPage.setRecords(ListUtil.collect(page.getRecords(), function));
    }

    public static <T, R> Page<R> transform(Page<T> page, Function<T, R> function, boolean rejectNullObject) {
        Page<R> transform = transform(page, function);
        if (rejectNullObject) {
            transform.getRecords().removeIf(Objects::isNull);
        }
        return transform;
    }

    public static <T, R> Page<R> of(Page<T> page, List<R> records) {
        Page<R> of = Page.of(page.getCurrent(), page.getSize(), ListUtil.adaptIterable(records).size());
        of.setRecords(records);
        return of;
    }

    /**
     * 创建一个不包含 total 属性的 Page 对象
     *
     * @param current  当前页
     * @param pageSize 页面大小
     * @param records  数据
     * @return {@link Page}<{@link T}>
     */
    public static <T> Page<T> of(long current, long pageSize, List<T> records) {
        Page<T> page = Page.of(current, pageSize);
        page.setRecords(records);
        return page;
    }

    public static <T> Page<T> of(long current, long size, long total, List<T> records) {
        Page<T> page = of(current, size, records);
        page.setTotal(total);
        return page;
    }

    /**
     * 默认分页对象，页码=1，分页大小=Int.max，相当于不分页
     *
     * @return {@link Page}<{@link T}>
     */
    public static <T> Page<T> nonePage() {
        return Page.of(1, Integer.MAX_VALUE, false);
    }
}

package com.lrz.grovecore.utils;

import com.lrz.grovecore.core.Entity;
import com.lrz.grovecore.core.IdAutoEntity;
import org.eclipse.collections.api.list.MutableList;

import java.util.List;
import java.util.function.Function;


/**
 * 实体类工具类
 *
 * @author lirenzhi
 * @since 2022/12/14 1:22 下午
 */
public final class EntityUtil {

    /**
     * 从实体类中抽取所有ID
     *
     * @param entities 实体
     * @return {@link MutableList}<{@link Long}>
     */
    public static <E extends IdAutoEntity> MutableList<Long> ids(List<E> entities) {
        return ids(entities, ListUtil.empty());
    }

    /**
     * 从实体类中抽取所有ID
     *
     * @param entities 实体
     * @param appendId 添加id
     * @return {@link MutableList}<{@link Long}>
     */
    public static <E extends IdAutoEntity> MutableList<Long> ids(List<E> entities, Long appendId) {
        return ids(entities, ListUtil.adaptItems(appendId));
    }

    /**
     * 从实体类中抽取所有ID
     *
     * @param entities  实体集合
     * @param appendIds 需要额外添加的ids
     * @return {@link MutableList}<{@link Long}>
     */
    public static <E extends IdAutoEntity> MutableList<Long> ids(List<E> entities, List<Long> appendIds) {
        MutableList<Long> ids = ListUtil.collect(entities, IdAutoEntity::getId);
        ids.addAll(appendIds);
        return ids.distinct();
    }

    /**
     * 从实体类中抽取某个字段
     *
     * @param entities 实体
     * @param function 函数
     * @return {@link MutableList}<{@link K}>
     */
    public static <E extends Entity, K> MutableList<K> fields(List<E> entities, Function<E, K> function) {
        return fields(entities, function, ListUtil.empty());
    }

    /**
     * 从实体类中抽取某个字段，并拼接一个字段
     *
     * @param entities 实体
     * @param function 函数
     * @param appends  附加
     * @return {@link MutableList}<{@link K}>
     */
    public static <E extends Entity, K> MutableList<K> fields(List<E> entities, Function<E, K> function, K appends) {
        return fields(entities, function, ListUtil.adaptItems(appends));
    }

    /**
     * 从实体类中抽取某个字段，并拼接多个字段
     *
     * @param entities 实体
     * @param function 函数
     * @param appends  附加
     * @return {@link MutableList}<{@link K}>
     */
    public static <E extends Entity, K> MutableList<K> fields(List<E> entities, Function<E, K> function, List<K> appends) {
        MutableList<K> mutableList = ListUtil.collect(entities, function);
        mutableList.addAll(ListUtil.adaptIterable(appends));//防止为kong，再转一次
        return mutableList.distinct();
    }
}

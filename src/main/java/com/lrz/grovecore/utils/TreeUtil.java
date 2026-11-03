package com.lrz.grovecore.utils;

import com.lrz.grovecore.annotation.RequestCached;
import com.lrz.grovecore.common.ValueHolder;
import com.lrz.grovecore.tree.Tree;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 树工具类
 *
 * @author lirenzhi
 * @since 2023/2/2 4:28 下午
 */
public final class TreeUtil {

    /**
     * 遍历树
     *
     * @param tree     树
     * @param consumer 消费者
     */
    public static <E extends Tree<E>> void walk(E tree, Consumer<E> consumer) {
        consumer.accept(tree);
        List<E> children = tree.getChildren();
        if (ListUtil.isNotEmpty(children)) {
            children.forEach(x -> walk(x, consumer));
        }
    }

    /**
     * 压缩树层级
     *
     * @param tree  树
     * @param level 层级
     */
    public static <E extends Tree<E>> void zip(E tree, int level) {
        walk(tree, x -> {
            if (x.getLevel() == level) {
                x.setChildren(null);
            }
        });
    }

    /**
     * 构建树
     *
     * @param root  根
     * @param nodes 节点
     * @return {@link E}
     */
    public static <E extends Tree<E>> E build(E root, List<E> nodes) {
        return build(root, nodes, Integer.MAX_VALUE); //实际上表示无限制
    }

    public static <E extends Tree<E>> E build(E root, List<E> nodes, Integer levelLimit) {
        return build(root, nodes, levelLimit, null);
    }

    public static <E extends Tree<E>> E build(E root, List<E> nodes, Integer levelLimit, Comparator<Tree<E>> comparator) {
        if (comparator == null) {
            comparator = Comparator.comparing(Tree::getPosition);
        }

        root.setLevel(1);
        if (ListUtil.isEmpty(nodes)) return root;

        MutableListMultimap<Long, E> parentId_x_map = ListUtil.adaptIterable(nodes).groupBy(Tree::getParentId);
        if (parentId_x_map.containsKey(root.getId())) {
            MutableList<E> children = parentId_x_map.get(root.getId());
            children = ListUtil.adaptIterable(children);//这里转换一次，是因为 MutableListMultimap 它的get方法，返回是一个不可修改集合，不能做排序

            children.forEach(x -> x.setLevel(2));//在节点上设置树的深度
            children.sortThis(comparator);//排序子节点

            root.setChildrenCount(children.size());

            if (Objects.equals(root.getLevel(), levelLimit)) {
                return root;
            }

            root.setChildren(children);

            recursionBuildChildren(children, parentId_x_map, 3, comparator);
        }
        return root;
    }

    private static <E extends Tree<E>> void recursionBuildChildren(List<E> children, MutableListMultimap<Long, E> parentId_x_map, Integer levelLimit, Comparator<Tree<E>> comparator) {
        if (ListUtil.isEmpty(children)) {
            return;
        }
        children.forEach(item -> {
            if (parentId_x_map.containsKey(item.getId())) {
                List<E> children1 = parentId_x_map.get(item.getId());
                children1 = ListUtil.adaptIterable(children1);
                children1.forEach(x -> x.setLevel(levelLimit));
                children1.sort(comparator);

                item.setChildrenCount(ListUtil.adaptIterable(children1).size());

                if (item.getLevel() >= levelLimit) {
                    return;
                }

                item.setChildren(children1);
                recursionBuildChildren(children1, parentId_x_map, levelLimit + 1, comparator);
            }
        });
    }

    /**
     * 获取树中所有节点
     *
     * @param tree 树
     * @return {@link List}<{@link E}>
     */
    @RequestCached
    public static <E extends Tree<E>> List<E> nodes(E tree) {
        List<E> list = ListUtil.empty();
        walk(tree, list::add);
        return list;
    }

    /**
     * 重置元数据
     *
     * @param tree 树
     */
    public static <E extends Tree<E>> void resetMeta(E tree) {
        walk(tree, x -> x.setChildrenCount(x.getChildren().size()));
    }

    /**
     * 对树按条件进行剪枝
     *
     * @param tree      树
     * @param predicate 条件
     */
    public static <E extends Tree<E>> void prune(E tree, Predicate<E> predicate) {
        prune(tree, predicate, true);
    }

    public static <E extends Tree<E>> void prune(E tree, Predicate<E> predicate, boolean resetMetadata) {
        List<E> children = tree.getChildren();
        if (ListUtil.isEmpty(children)) return;

        children.removeIf(predicate);

        children.forEach(item -> doPrune(item, predicate));

        if (resetMetadata) {
            resetMeta(tree);
        }
    }

    private static <E extends Tree<E>> void doPrune(E tree, Predicate<E> predicate) {
        List<E> children = tree.getChildren();
        if (ListUtil.isNotEmpty(children)) {
            children.removeIf(predicate);
            children.forEach(item -> doPrune(item, predicate));
        }
    }

    /**
     * A树->B树
     *
     * @param tree     树
     * @param function 函数
     * @return {@link T}
     */
    public static <E extends Tree<E>, T extends Tree<T>> T transform(E tree, Function<E, T> function) {
        T t = function.apply(tree);

        List<E> children = tree.getChildren();

        if (ListUtil.isEmpty(children)) return t;

        List<T> transformChildren = ListUtil.empty();

        children.forEach(x -> transformChildren.add(transform(x, function)));

        t.setChildren(transformChildren);

        return t;
    }

    /**
     * 获取指定父级的子集
     *
     * @param tree     树
     * @param parentId 父id
     * @param function 函数
     * @return {@link List}<{@link R}>
     */
    public static <E extends Tree<E>, R> List<R> children(E tree, Long parentId, Function<E, R> function) {
        List<E> result = ListUtil.empty();

        ValueHolder<Integer> visitedLevel = new ValueHolder<>(0);

        List<Long> parentIds = ListUtil.empty();

        walk(tree, x -> {
            Long currentParentId = x.getParentId();
            if (Objects.equals(currentParentId, parentId)) {
                result.add(x);
                parentIds.add(x.getId());
                visitedLevel.setValue(x.getLevel());
            }
        });

        if (ListUtil.isNotEmpty(parentIds)) doChildren(tree, visitedLevel, parentIds, result);

        return ListUtil.collect(result, function);
    }

    /**
     * 遍历过的层级不会再遍历，理论上不会有性能问题
     *
     * @param tree         树
     * @param visitedLevel 访问级别
     * @param parentIds    父id
     * @param result       结果
     */
    private static <E extends Tree<E>> void doChildren(E tree, ValueHolder<Integer> visitedLevel, List<Long> parentIds, List<E> result) {
        if (ListUtil.isEmpty(parentIds)) return;

        ValueHolder<Integer> newValueHolder = new ValueHolder<>(visitedLevel.getValue());
        List<Long> newParentIds = ListUtil.empty();

        walk(tree, e -> {
            if (e.getLevel() <= visitedLevel.get()) return;
            Long parentId = e.getParentId();
            if (parentIds.contains(parentId)) {
                newParentIds.add(e.getId());
                result.add(e);
                newValueHolder.setValue(e.getLevel());
            }
        });
        if (ListUtil.isNotEmpty(newParentIds)) doChildren(tree, newValueHolder, newParentIds, result);
    }

    /**
     * 从树上获取某个节点,会同时获取到指定节点的子集
     *
     * @param tree     树
     * @param id       id
     * @param function 函数
     * @return {@link X}
     */
    public static <E extends Tree<E>, X> X getNode(E tree, Long id, Function<E, X> function) {
        E e = ListUtil.toMap(nodes(tree), Tree::getId).get(id);
        return e == null ? null : function.apply(e);
    }

    /**
     * 获取指定节点的所有祖辈节点
     *
     * @param tree     树
     * @param id       id
     * @param function 函数
     * @return {@link List}<{@link R}>
     */
    public static <E extends Tree<E>, R> List<R> parents(E tree, Long id, Function<E, R> function) {
        Map<Long, E> map = ListUtil.toMap(nodes(tree), Tree::getId);

        E e = map.get(id);
        if (e == null || map.get(e.getParentId()) == null) {
            return ListUtil.empty();
        }

        List<R> result = ListUtil.empty();
        E node = map.get(e.getParentId());
        while (node != null) {
            result.add(function.apply(node));
            node = map.get(node.getParentId());
        }
        return result;
    }

    /**
     * 获取指定父级的子集，理论上快一点，但是
     * 经过测试，children方法还是快一点，估计是这个方法工具类用的太多的原因，虽然没有递归
     *
     * @param tree     树
     * @param parentId 父id
     * @param function 函数
     * @return {@link List}<{@link R}>
     */
    public static <E extends Tree<E>, R> List<R> childrenFast(E tree, Long parentId, Function<E, R> function) {
        List<E> nodes = nodes(tree);
        Map<Long, List<E>> parentIdMaps = ListUtil.groupBy(nodes, Tree::getParentId);

        List<R> result = ListUtil.empty();
        List<Long> parentIds = ListUtil.of(parentId);

        while (MapUtil.containsAnyKey(parentIdMaps, parentIds)) {
            List<List<E>> values = MapUtil.getKeys(parentIdMaps, parentIds);
            List<E> children = ListUtil.flatMap(values);

            if (ListUtil.isEmpty(children)) {
                break;
            }

            MutableList<R> childrenValue = ListUtil.collect(children, function);
            result.addAll(childrenValue);
            parentIds.clear();
            parentIds.addAll(ListUtil.collect(children, Tree::getId).distinct());
        }
        return result;
    }
}

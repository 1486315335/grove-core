package com.lrz.grovecore.tree;

import com.lrz.grovecore.utils.JsonUtil;
import com.lrz.grovecore.utils.ListUtil;
import com.lrz.grovecore.utils.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 树基本信息，部分值会在树构建的过程中被创建
 *
 * @author lirenzhi
 * @since 2023/2/2 4:14 下午
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractTree<E extends Tree<E>> implements Tree<E> {

    protected Long id; //节点ID
    protected Long parentId; //父节点ID
    protected String name; //节点名称
    private boolean hidden; //是否隐藏

    protected Long position;  //节点排序
    protected Boolean enable; //节点是否可用？表示选择
    private Boolean checked; //表示选择

    protected Integer level; //当前所在树层级，构建过程中赋值
    protected Integer childrenCount = 0; //子节点数量，构建过程中赋值
    protected List<E> children = ListUtil.empty(); //子节点列表，构建过程中赋值

    protected Object refObject;
    protected String treeId = StringUtil.UUID(); //因为需要整合不同的实体类型，会有id相同的场景(如组织用户树等），需要提供一个内置的唯一ID

    public <T extends AbstractTree<T>> T to(T t) {
        BeanUtils.copyProperties(this, t);
        return t;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public boolean isEmpty() {
        return ListUtil.isEmpty(children);
    }
}

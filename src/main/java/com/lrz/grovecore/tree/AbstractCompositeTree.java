package com.lrz.grovecore.tree;

import com.lrz.grovecore.utils.ListUtil;
import com.lrz.grovecore.utils.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象复合树
 *
 * @author lirenzhi
 * @since 2023/6/19 3:45 PM
 */
@Getter
@Setter
public abstract class AbstractCompositeTree<E extends Tree<E>, D> extends AbstractTree<E> {
    public void build(E parentNode, D d, String keyword) {
        processValueOf(parentNode, d, keyword);

        this.setParentId(parentNode.getId());
        this.setLevel(parentNode.getLevel() + 1);
        this.setChildren(ListUtil.empty());
        this.setChildrenCount(0);
        this.setChecked(ObjectUtil.nonNullOrEmpty(keyword) && ObjectUtil.ifNullOrEmpty(getName(), "").contains(keyword));
    }

    protected abstract void processValueOf(E parentNode, D d, String keyword);
}

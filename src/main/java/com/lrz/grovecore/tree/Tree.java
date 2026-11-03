package com.lrz.grovecore.tree;

import java.util.List;

/**
 * 树节点接口
 *
 * @author lirenzhi
 * @since 2023/2/2 4:12 下午
 */
public interface Tree<E extends Tree<E>> {
    /** 节点 ID */
    Long getId();
    void setId(Long id);

    String getName();
    void setName(String name);

    /** 父节点 ID */
    Long getParentId();
    void setParentId(Long parentId);

    /** 子节点 */
    List<E> getChildren();
    void setChildren(List<E> children);

    /** 层级 */
    Integer getLevel();
    void setLevel(Integer level);

    /** 子节点数量 */
    void setChildrenCount(Integer childrenCount);
    Integer getChildrenCount();

    /** 是否可用 */
    Boolean getEnable();
    void setEnable(Boolean enable);

    Boolean getChecked();
    void setChecked(Boolean checked);

    /** 排序号 */
    Long getPosition();
    void setPosition(Long position);

    void setRefObject(Object object);
    Object getRefObject();
}

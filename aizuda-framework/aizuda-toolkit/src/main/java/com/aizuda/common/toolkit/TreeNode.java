package com.aizuda.common.toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * 父子树建树 树节点
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author gwml
 * @since 1.1.0
 */
public abstract class TreeNode<I, V extends TreeNode> {

    private List<V> children = new ArrayList<>();

    public abstract I nodeId();

    public abstract I nodePid();

    public List<V> getChildren() {
        return children;
    }

    public void setChildren(List<V> children) {
        this.children = children;
    }
}

package com.aizuda.common.toolkit;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 父子树建树工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author gwml
 * @since 1.1.0
 */
public class TreeUtils {

    /**
     * 构建树结构
     */
    public static <I, V extends TreeNode<I, V>> List<V> buildTree(List<V> treeNodes, I currentRootId, I rootId) {
        return buildTree(treeNodes, defaultPredicate(currentRootId, rootId));
    }

    /**
     * 默认根节点断言
     */
    public static <I, V extends TreeNode<I, V>> Predicate<V> defaultPredicate(I currentRootId, I rootId) {
        return x -> {
            if (!Objects.equals(currentRootId, rootId)) {
                return Objects.equals(currentRootId, x.nodeId());
            }
            return Objects.equals(x.nodePid(), rootId);
        };
    }

    /**
     * 自定义根节点断言
     */
    public static <I, V extends TreeNode<I, V>> List<V> buildTree(List<V> treeNodes, Predicate<V> rootPredicate) {
        return buildTree(treeNodes, rootPredicate, null);
    }

    /**
     * 构建树结构并排序
     */
    public static <I, V extends TreeNode<I, V>> List<V> buildTree(List<V> treeNodes, I currentRootId, I rootId, Comparator<? super V> comparator) {
        return buildTree(treeNodes, defaultPredicate(currentRootId, rootId), comparator);
    }

    /**
     * 自定义根节点断言建树并排序
     */
    public static <I, V extends TreeNode<I, V>> List<V> buildTree(List<V> treeNodes, Predicate<V> rootPredicate, Comparator<? super V> comparator) {
        Map<I, List<V>> group = treeNodes.stream().collect(Collectors.groupingBy(x -> x.nodePid()));
        List<V> result = new ArrayList<>();
        treeNodes.forEach(x -> {
            if (group.containsKey(x.nodeId())) {
                List<V> ts = group.get(x.nodeId());
                if (Objects.nonNull(comparator)) {
                    ts.sort(comparator);
                }
                x.getChildren().addAll(ts);
            }
            if (rootPredicate.test(x)) {
                result.add(x);
            }
        });
        if (Objects.nonNull(comparator)) {
            result.sort(comparator);
        }
        return result;
    }
}

package com.aizuda.common.toolkit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TreeUtilTest {

    static final int ROOT_ID = 0;

    public static void main(String[] args) {
        List<TreeNodeC> list = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            TreeNodeC node = TreeNodeC.builder().id(i).pid(ROOT_ID).build();
            list.add(node);
            for (int j =node.getId() * 100 + 10; j >  node.getId() * 100; j--) {
                list.add(TreeNodeC.builder().id(j).pid(node.getId()).build());
            }
        }

        List<TreeNodeC> treeNodeCS = TreeUtils.buildTree(list, ROOT_ID, ROOT_ID);
        System.out.println(treeNodeCS);
        List<TreeNodeC> treeNodeCS1 = TreeUtils.buildTree(list, 1, ROOT_ID);
        System.out.println(treeNodeCS1);
        List<TreeNodeC> treeNodeCS2 = TreeUtils.buildTree(list, ROOT_ID, ROOT_ID, Comparator.comparing(TreeNodeC::getId));
        System.out.println(treeNodeCS2);
    }

    @Getter
    @Setter
    @Builder
    static class TreeNodeC extends TreeNode<Integer, TreeNodeC> {

        private Integer id;

        private Integer pid;

        @Override
        public Integer nodeId() {
            return id;
        }

        @Override
        public Integer nodePid() {
            return pid;
        }

        public String toString() {
            List<String> childStr = new ArrayList<>();
            if (!this.getChildren().isEmpty()) {
                this.getChildren().forEach(x -> childStr.add(x.toString()));
            }
            String str = String.join(",", childStr);
            return "{\"id\":" + this.id + ",\"pid\":" + this.pid + ",\"child\":[" + str + "]}";
        }
    }
}

package com.quinn.interview.tree;

import org.junit.Test;

import java.util.*;

/**
 * 二叉树树遍历
 */
public class TreeScanTest {

    /**
     * 创建树
     * A
     * / \
     * B   C
     * /   / \
     * D   E   F
     * / \
     * G   H
     */
    public Node buildTree() {
        Node root = new Node('A');
        root.left = new Node('B');
        root.left.left = new Node('D');

        root.right = new Node('C');

        root.right.left = new Node('E');
        root.right.right = new Node('F');

        root.right.right.left = new Node('G');
        root.right.right.right = new Node('H');
        return root;
    }

    @Test
    public void test() {
        Node node = buildTree();
        prevScan(node);
        System.out.println();
        midScan(node);
        System.out.println();
        recoveryLayer("ABDCEFGH", "DBAECGFH");
        postScan(node);
        System.out.println();
        layerScan(node);
        lookFromRight(node);
    }

    /**
     * 前序遍历
     * 父节点-左子树-右子树
     * 根节点在最前面，然后根着一段是左子数；再根着一段右子树：每一颗子树也是这样
     */
    public void prevScan(Node root) {
        if (root != null) {
            System.out.print(root.value);
            prevScan(root.left);
            prevScan(root.right);
        }
    }

    /**
     * 中遍历
     * 左子树 - 父节点 - 右子树
     * 根节点在中间，然后左边是左子树，右边是右子树；每一颗子树也是这样
     */
    public void midScan(Node root) {
        if (root != null) {
            midScan(root.left);
            System.out.print(root.value);
            midScan(root.right);
        }
    }

    /**
     * 后序遍历
     * 左子数 - 右子树 - 父节点
     * 根节点在最后面，紧跟在这之前的是右子数，然后跟着左子数
     */
    public void postScan(Node root) {
        if (root != null) {
            postScan(root.left);
            postScan(root.right);
            System.out.print(root.value);
        }
    }

    /**
     * 层次遍历(自上而下)
     * <p>
     * 第一层-第二层……
     */
    public void layerScan(Node root) {
        Stack<List<Node>> nodeList = new Stack<>();
        nodeList.push(Collections.singletonList(root));
        if (root != null) {
            layerScan(nodeList);
        }
    }

    /**
     * 遍历层序
     *
     * @param stack 栈
     */
    public void layerScan(Stack<List<Node>> stack) {
        List<Node> pop = stack.pop();
        if (pop != null && !pop.isEmpty()) {
            List<Node> children = new ArrayList<>();
            for (Node node : pop) {
                System.out.print(node.value);
                if (node.left != null) {
                    children.add(node.left);
                }
                if (node.right != null) {
                    children.add(node.right);
                }
            }
            System.out.println();
            stack.push(children);
            layerScan(stack);
        }
    }

    /**
     * 右侧视图
     */
    public void lookFromRight(Node node) {
        LinkedList<List<Node>> layer = orgAsLayer(node);
        for (List<Node> list : layer) {
            System.out.println(list.get(list.size() - 1).value);
        }
    }

    /**
     * 层序视图
     */
    public LinkedList<List<Node>> orgAsLayer(Node root) {
        List<Node> list = new ArrayList<>();
        list.add(root);
        LinkedList<List<Node>> queue = new LinkedList<>();
        queue.offer(list);
        orgAsLayer(queue);
        return queue;
    }

    public void orgAsLayer(LinkedList<List<Node>> queue) {
        List<Node> peek = queue.peekLast();
        List<Node> next = new ArrayList<>();
        for (Node n : peek) {
            if (n.left != null) {
                next.add(n.left);
            }
            if (n.right != null) {
                next.add(n.right);
            }
        }

        if (!next.isEmpty()) {
            queue.add(next);
            orgAsLayer(queue);
        }
    }

    /**
     * 所以已知中序 + 前面序 => 层序
     * 先根据前序，得出根节点；
     * 然后使用根节点把中序分为两个序列
     * 然后将前序中的根节点去掉，分为两个序列，每个序列又是递归逻辑
     */
    public void recoveryLayer(String pre, String mid) {
        char[] pa = pre.toCharArray();
        char[] ma = mid.toCharArray();
        Map<Integer, List> data = new HashMap<>();
        recoveryLayer(pa, ma, 0, pa.length, 0, data, 1);
        System.out.println(data);
    }

    /**
     * "ABDCEFGH", "BDACEFGH"
     *
     * @param pa    前序数组
     * @param ma    中序数组
     * @param start 本次递归中序起始位（循环和前序第一位比较，找到相同的地方做断点）
     * @param end   本次递归中序结束位（表示递归分界点）
     * @param ps    记录本次递归范围前序序列起始位置
     * @param map   放置层数对应节点列表
     * @param lay   层数
     */
    public void recoveryLayer(char[] pa, char[] ma, int start, int end, int ps, Map<Integer, List> map, int lay) {
        for (int i = start; i < end; i++) {
            if (pa[ps] == ma[i]) {
                List o = map.get(lay);
                if (o == null) {
                    o = new ArrayList();
                    map.put(lay, o);
                }
                o.add(ma[i]);

                recoveryLayer(pa, ma, start, i, ps + 1, map, ++lay);
                recoveryLayer(pa, ma, i + 1, end, ps + i - start + 1, map, lay);

                break;
            }
        }
    }

    public static class Node {

        public Node(char value) {
            this.value = value;
        }

        private char value;

        private Node left;

        private Node right;

    }

}

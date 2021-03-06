package com.quinn.interview.tree;

import java.util.Scanner;

public class SeqTest {

    private static final char MAX_CHAR = 'z';

    private static final char MIN_CHAR = 'A';

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        loop:
        while (in.hasNext()) {
            String line = in.nextLine();
            Integer seq = Integer.valueOf(in.nextLine());

            Node head = new Node((char) (MIN_CHAR - 1), -1);

            char[] chars = line.toCharArray();
            int index = 0;
            for (char c : chars) {
                if (c > MAX_CHAR || c < MIN_CHAR) {
                    System.out.println("字符串超出范围");
                    continue loop;
                }
                head.append(new Node(c, index++), head);
            }

            Node res = head;
            for (int i = 0; i < seq; i++) {
                if (res.next == null) {
                    break;
                }
                res = res.next;
            }
            System.out.println(res.index);
        }
    }

    public static class Node {

        public Node(char value, int index) {
            this.value = value;
            this.index = index;
        }

        private char value;

        private int index;

        private Node next;

        public void append(Node node, Node prev) {
            if (node.value > this.value) {
                if (this.next == null) {
                    this.next = node;
                } else {
                    this.next.append(node, this);
                }
            } else if (node.value == this.value) {
                node.index = this.index;
                node.next = this.next;
                this.next = node;
            } else {
                node.next = this;
                prev.next = node;
            }
        }

    }
}

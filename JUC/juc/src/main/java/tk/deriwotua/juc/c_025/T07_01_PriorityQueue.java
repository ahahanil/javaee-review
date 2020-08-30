package tk.deriwotua.juc.c_025;

import java.util.PriorityQueue;

/**
 * PriorityQueue 基于二叉树排序队列
 */
public class T07_01_PriorityQueue {
    public static void main(String[] args) {
        PriorityQueue<String> q = new PriorityQueue<>();

        q.add("c");
        q.add("e");
        q.add("a");
        q.add("d");
        q.add("z");

        /**
         * 按照自然排序
         */
        for (int i = 0; i < 5; i++) {
            System.out.println(q.poll());
        }

    }
}

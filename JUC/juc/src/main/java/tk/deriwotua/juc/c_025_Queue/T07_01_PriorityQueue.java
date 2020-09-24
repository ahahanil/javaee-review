package tk.deriwotua.juc.c_025_Queue;

import java.util.PriorityQueue;

/**
 * PriorityQueue 优先队列基于二叉树排序队列
 *  优先队列的作用是能保证每次取出的元素都是队列中权值最小的（Java的优先队列每次取最小元素，C++的优先队列每次取最大元素）。
 *  这里牵涉到了大小关系，元素大小的评判可以通过元素本身的自然顺序（natural ordering），也可以通过构造时传入的比较器（Comparator，类似于C++的仿函数）
 *
 *  PriorityQueue的底层数组实现小顶堆(任意一个非叶子节点的权值，都不大于其左右子节点的权值)
 *
 *  add(E e)和offer(E e)插入元素时先判断数组是否需要扩容
 *  然后调整找到符合小顶堆(任意一个非叶子节点的权值，都不大于其左右子节点的权值)位置插入
 *  element()、peek()、remove()、poll() 都是取出最小权重元素(即队首数组下标为0的元素)
 *      element()、peek() 取出不删除
 *      remove()、poll() 取出并删除(出队列) 需要调整使其符合符合小顶堆(任意一个非叶子节点的权值，都不大于其左右子节点的权值)
 *  ![PriorityQueue优先队列](../assets/PriorityQueue优先队列.png)
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

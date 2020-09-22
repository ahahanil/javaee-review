package tk.deriwotua.juc.c_023_01_Containers;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 容器
 *      Collection
 *          List
 *              CopyOnWriteList 写时复制容器
 *                  数组实现 并发安全
 *                  读写分离：读时共享、写时复制(原本的array)更新(且为独占式的加锁) JDK8使用的是ReentrantLock锁 JDK11使用的是synchronized同步代码块
 *                  添加元素时先加锁 然后数组元素拷贝到新数组(长度为旧数组+1) 最后新数组末尾元素赋值再替换掉旧数组
 *                  修改、删除都是写操作加锁
 *                  读取时存在弱一致性问题
 *                      所有的写操作都会产生新数组然后操作后替换旧数组
 *                      但是读操作都是读取旧数组，存在某个写线程变更了元素但新数组还未替换旧数组导致此时读的还是旧数据
 *              Vector
 *                  Stack
 *              ArrayList
 *                  非线程安全类变长的集合类，基于定长数组实现(O(1) 复杂度下完成随机查找操作)
 *                  允许空值和重复元素
 *                  添加时元素数量大于其底层数组容量时，其会通过扩容机制重新生成一个更大的数组
 *                      然后把旧数组数据拷贝到新数组里
 *                  指定 index 位置插入时会先校验下标是否合法然后判断是否需要扩容
 *                      然后把[index, size-index]区间元素一个一个拷贝方式向后移一位 O(N)
 *                      最后新元素插入至 index 处
 *                  删除元素是把该元素下标后面的元素一个一个拷贝向前移一位，最后把size值下标元素置null
 *
 *                  删除、插入时间复杂度都是O(n)
 *                  [参考](https://juejin.im/post/6844903904346374158)
 *                  ![JDK8-ArrayList添加元素](../assets/JDK8-ArrayList添加元素.png)
 *              LinkedList
 *          Set
 *              HashSet
 *                  LinkedHashSet
 *              SortedSet
 *                  TreeSet
 *              EnumSet
 *              CopyOnWriteArraySet
 *              ConcurrentSkipListSet
 *          Queue
 *              Deque   双端队列
 *                  ArrayDeque
 *                  BlockingDeque
 *                      LinkedBlockingDeque
 *              BlockingQueue
 *                  ArrayBlockingQueue
 *                  PriorityBlockingQueue
 *                  LinkedBlockingQueue
 *                  TransferQueue
 *                      LinkedTransferQueue
 *                  SynchronousQueue
 *              PriorityQueue
 *              ConcurrentLinkedQueue
 *              DelayQueue
 *      Map
 *          HashMap
 *              LinkedHashMap
 *          TreeMap
 *          WeakHashMap
 *          IdentifyHashMap
 *          ConcurrentHashMap
 *          ConcurrentSkipListMap
 * 为什么有List、Set后又搞了Queue
 *      Queue 针对高并发场景提供了一些线程友好的API
 *          offer peek poll
 *      BlockingQueue 又提供阻塞API
 *          put take 自然实现任务队列和生产者消费者模型
 */
public class HelloQueue {
    public static void main(String[] args) {
        Queue<Integer> q = new ArrayBlockingQueue<>(2);
        q.add(0);
        q.add(1);
        q.add(2);
        q.add(3);
        System.out.println(q);
    }
}

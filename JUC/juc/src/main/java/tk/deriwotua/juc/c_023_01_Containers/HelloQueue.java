package tk.deriwotua.juc.c_023_01_Containers;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 容器
 *      Collection
 *          List
 *              CopyOnWriteList
 *              Vector
 *                  Stack
 *              ArrayList
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

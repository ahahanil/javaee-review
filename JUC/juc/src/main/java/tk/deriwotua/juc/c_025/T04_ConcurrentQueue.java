package tk.deriwotua.juc.c_025;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue 相当于 BlockingQueue是线程安全的操作
 *
 * ConcurrentLinkedQueue 线程安全队列模拟售票
 * 	基于CAS保障线程安全 且 非阻塞 的 无界 队列
 * 		不允许存在null元素
 * 多线程环境下多考虑 Queue和Set
 *
 * ConcurrentLinkedQueue
 * 	  static final class Node<E> {
 *           volatile E item; // 数据域
 *           volatile Node<E> next; // next 指针
 *    }
 *    重要字段
 *    	private transient volatile Node<E> head; 头节点指针
 * 		private transient volatile Node<E> tail; 尾节点指针
 *
 * 		JDK9 引入java.lang.invoke.VarHandle 可通过指针直接操作指针指向的内存空间而且是原子性操作直接操作二进制码
 * 			字段定义使用 VarHandle 修饰
 * 				private static final VarHandle HEAD; 头节点指针
 *     			private static final VarHandle TAIL; 尾节点指针
 *     			static final VarHandle ITEM;
 *     			static final VarHandle NEXT;
 *    引入 VarHandle 指针后就不在使用 Unsafe CAS相关方法操作直接使用 java.lang.invoke.VarHandle#compareAndSet()相关CAS方法
 * ConcurrentLinkedQueue队列存在头尾指针所以一般
 * 		offer 线程和 poll 线程两者并无“交集”
 * 			poll 出队列删除头节点通常是要快于 offer 入队列队尾添加，队头删的速度要快于队尾添加节点的速度，结果就是队列长度会越来越短
 */
public class T04_ConcurrentQueue {
	public static void main(String[] args) {
		Queue<String> strs = new ConcurrentLinkedQueue<>();
		
		for(int i=0; i<10; i++) {
			/**
			 * 在容量范围类尝试插入成功返回true(相比add队列满时插入抛异常)
			 */
			strs.offer("a" + i);  //add
		}
		
		System.out.println(strs);
		
		System.out.println(strs.size());
		/**
		 * 取出头元素(删除操作)
		 */
		System.out.println(strs.poll());
		System.out.println(strs.size());
		/**
		 * 取头元素(不删除)
		 */
		System.out.println(strs.peek());
		System.out.println(strs.size());
		
		//双端队列Deque
	}
}

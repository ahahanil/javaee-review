/**
 * 有N张火车票，每张票都有一个编号
 * 同时有10个窗口对外售票
 * 请写一个模拟程序
 * 
 * 分析下面的程序可能会产生哪些问题？
 * 重复销售？超量销售？
 * 
 * 使用Vector或者Collections.synchronizedXXX
 * 分析一下，这样能解决问题吗？
 * 
 * 就算操作A和B都是同步的，但A和B组成的复合操作也未必是同步的，仍然需要自己进行同步
 * 就像这个程序，判断size和进行remove必须是一整个的原子操作
 * 
 * 使用ConcurrentQueue提高并发性
 * 
 * @author 马士兵
 */
package tk.deriwotua.juc.c_024_FromVectorToQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue 线程安全队列模拟售票
 * 	基于CAS保障线程安全 且 非阻塞 的 无界 队列
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
public class TicketSeller4 {
	static Queue<String> tickets = new ConcurrentLinkedQueue<>();

	static {
		for(int i=0; i<1000; i++) tickets.add("票 编号：" + i);
	}
	
	public static void main(String[] args) {
		
		for(int i=0; i<10; i++) {
			new Thread(()->{
				while(true) {
					/**
					 * 从队列里取出头值(出队列)
					 */
					String s = tickets.poll();
					// 售完后为null
					if(s == null) break;
					else System.out.println("销售了--" + s);
				}
			}).start();
		}
	}
}

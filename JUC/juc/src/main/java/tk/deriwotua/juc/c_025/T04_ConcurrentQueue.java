package tk.deriwotua.juc.c_025;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue 相当于 BlockingQueue是线程安全的操作
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

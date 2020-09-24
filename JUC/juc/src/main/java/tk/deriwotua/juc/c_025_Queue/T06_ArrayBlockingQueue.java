package tk.deriwotua.juc.c_025_Queue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ArrayBlockingQueue 有界阻塞队列
 * 	队列满后put()再添加则阻塞等待直到有空间插入
 *         offer()再添加返回false 添加失败
 *  队列空后take()再取则阻塞等待直到能取出数据
 *         poll()返回null
 */
public class T06_ArrayBlockingQueue {

	static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10);

	static Random r = new Random();

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			strs.put("a" + i);
		}

		/**
		 * 队列插入数据时
		 * 如果队列满了就会阻塞等待
		 */
		//strs.put("aaa");
		/**
		 * 队列插入数据时
		 * 如果队列满了就会抛异常
		 */
		//strs.add("aaa");
		/**
		 * 队列插入数据时
		 * 如果队列满了就立即会返回false
		 */
		//strs.offer("aaa");
		/**
		 * 队列插入数据时
		 * 如果队列满了就等1s再尝试插入还不成功会返回false
		 */
		strs.offer("aaa", 1, TimeUnit.SECONDS);
		
		System.out.println(strs);
	}
}

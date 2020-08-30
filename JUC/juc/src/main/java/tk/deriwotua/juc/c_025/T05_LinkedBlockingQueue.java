package tk.deriwotua.juc.c_025;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * LinkedBlockingQueue 无界阻塞队列
 * 	在 Queue 基础上实现了阻塞式put()、take()方法
 * 	链表实现的阻塞队列
 * 	队列满后再添加则阻塞等待直到有空间插入
 * 	队列空后再取则阻塞等待直到能取出数据
 * 	因此 LinkedBlockingQueue 天然友好生产者&消费者模型
 * 		底层基于 LockSupport#park()阻塞、LockSupport#unpark()唤醒
 */
public class T05_LinkedBlockingQueue {

	static BlockingQueue<String> strs = new LinkedBlockingQueue<>();

	static Random r = new Random();

	public static void main(String[] args) {
		/**
		 * 生产者
		 */
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				try {
					/**
					 * 往队列里插入数据
					 * 如果队列满了则阻塞等待直到有空间插入
					 */
					strs.put("a" + i);
					TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "p1").start();

		/**
		 * 消费者
		 */
		for (int i = 0; i < 5; i++) {
			new Thread(() -> {
				for (;;) {
					try {
						/**
						 * 从队列里取出头元素(删除操作)
						 * 如果队列空了则阻塞等待能取出数据为止
						 */
						System.out.println(Thread.currentThread().getName() + " take -" + strs.take()); //如果空了，就会等待
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "c" + i).start();

		}
	}
}

package tk.deriwotua.juc.c_025;

import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue 线程间多任务传递
 * 	在数据插入队列后自动阻塞直到被取出消费
 * 	常用于处理某个逻辑后需要得到一个结果后才能继续执行下面逻辑
 */
public class T09_TransferQueue {
	public static void main(String[] args) throws InterruptedException {
		LinkedTransferQueue<String> strs = new LinkedTransferQueue<>();
		
		new Thread(() -> {
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		/**
		 * 与put区别在于往队列里插入后阻塞等待被取走
		 */
		strs.transfer("aaa");

		/**
		 * 往队列里插入
		 */
		//strs.put("aaa");

		/*new Thread(() -> {
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/


	}
}

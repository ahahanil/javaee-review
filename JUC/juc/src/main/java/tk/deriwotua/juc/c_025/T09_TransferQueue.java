package tk.deriwotua.juc.c_025;

import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue 线程间多任务传递
 * 	生产者会一直阻塞直到所添加到队列的元素被某一个消费者所消费
 * 	阻塞就是发生在元素从一个线程 transfer 到另一个线程的过程中，它有效地实现了元素在线程之间的传递
 * 	LinkedTransferQueue 实际上是 ConcurrentLinkedQueue、SynchronousQueue（公平模式）和 LinkedBlockingQueue 的超集
 *
 *
 *  transfer(E e)：若当前存在一个正在等待获取的消费者线程，即立刻移交之；
 *  	否则，会插入当前元素e到队列尾部，并且等待进入阻塞状态，到有消费者线程取走该元素。
 *  tryTransfer(E e)：若当前存在一个正在等待获取的消费者线程（使用take()或者poll()函数），使用该方法会即刻转移/传输对象元素e；若不存在，则返回false，并且不进入队列。这是一个不阻塞的操作。
 *  tryTransfer(E e, long timeout, TimeUnit unit)：若当前存在一个正在等待获取的消费者线程，会立即传输给它;
 * 		否则将插入元素e到队列尾部，并且等待被消费者线程获取消费掉；若在指定的时间内元素e无法被消费者线程获取，则返回false，同时该元素被移除。
 *  hasWaitingConsumer()：判断是否存在消费者线程。
 *  getWaitingConsumerCount()：获取所有等待获取元素的消费线程数量。
 *
 * 	底层使用LockSupport#park() LockSupport#unpark()
 *
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
		 * 阻塞式的将数据从一个线程传递到另一个线程
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

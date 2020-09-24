package tk.deriwotua.juc.c_025_Queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue 无界、不存储元素的阻塞队列或栈(双栈双队列算法无空间的阻塞队列)
 * 	在某次put添加元素后必须等待其他线程take取走后才能继续添加反之亦然
 * 	可以认为是缓存值为1的阻塞队列
 * 	 isEmpty()方法永远返回是true
 * 	 remainingCapacity() 方法永远返回是0
 * 	 remove()和removeAll() 方法永远返回是false
 * 	 iterator()方法永远返回空
 * 	 peek()方法永远返回null
 *
 * 	 在公平模式下
 * 	 	采用公平锁，并配合一个FIFO双队列来阻塞多余的生产者和消费者，从而体系整体的公平策略
 * 	 默认非公平模式
 * 	 	采用非公平锁，同时配合一个LIFO双栈来管理多余的生产者和消费者 可能有某些生产者或者是消费者的数据永远都得不到处理
 *
 * 	 put() 往queue放进去一个element以后就一直wait直到有其他thread进来把这个element取走。
 * 	 offer() 往queue里放一个element后立即返回，如果碰巧这个element被另一个thread取走了，offer方法返回true，认为offer成功；否则返回false。
 * 	 offer(2000, TimeUnit.SECONDS) 往queue里放一个element但是等待指定的时间后才返回，返回的逻辑和offer()方法一样。
 * 	 take() 取出并且remove掉queue里的element（认为是在queue里的。。。），取不到东西会一直等。
 * 	 poll() 取出并且remove掉queue里的element（认为是在queue里的。。。），只有到碰巧另外一个线程正在往queue里offer数据或者put数据的时候，该方法才会取到东西。否则立即返回null。
 * 	 poll(2000, TimeUnit.SECONDS) 等待指定的时间然后取出并且remove掉queue里的element,其实就是再等其他的thread来往里塞。
 *
 *  非常适合做交换工作，生产者的线程和消费者的线程同步以传递某些信息、事件或者任务
 *
 */
public class T08_SynchronousQueue { //����Ϊ0
	public static void main(String[] args) throws InterruptedException {
		/**
		 * Ҳ����������
		 */
		BlockingQueue<String> strs = new SynchronousQueue<>();
		
		new Thread(()->{
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		/**
		 * �����ȴ�����������
		 */
		strs.put("aaa");
		//strs.put("bbb");
		//strs.add("aaa");
		System.out.println(strs.size());
	}
}

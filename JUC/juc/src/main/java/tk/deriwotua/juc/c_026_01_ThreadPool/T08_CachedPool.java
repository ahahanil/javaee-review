package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CachedThreadPool 弹性线程池(来的任务必须立即执行没有空闲的线程去执行创建新线程去执行)
 * 	源码
 * 		public static ExecutorService newCachedThreadPool() {
 *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *                                       60L, TimeUnit.SECONDS,
 *                                       new SynchronousQueue<Runnable>());
 *     }
 *   CachedThreadPool线程池初始线程池中无线程但是线程池容量Integer.MAX_VALUE
 *   	当有任务时如果线程池中没有线程创建一个线程去执行任务，执行完后如果没有新任务执行等待60s后归还系统
 *   	当新任务执行时如果线程池中线程都忙着再新起线程来执行
 *   	SynchronousQueue 队列特点是插入的元素必须被取出后才能继续插入否则阻塞等待取走
 *   	因此 CachedThreadPool 队列 相当于仅仅转手
 */
public class T08_CachedPool {
	public static void main(String[] args) throws InterruptedException {
		/**
		 * 添加任务立即执行不存在空闲线程创建新线程
		 */
		ExecutorService service = Executors.newCachedThreadPool();
		System.out.println(service);
		for (int i = 0; i < 2; i++) {
			service.execute(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			});
		}
		System.out.println(service);
		TimeUnit.SECONDS.sleep(80);
		System.out.println(service);
	}
}

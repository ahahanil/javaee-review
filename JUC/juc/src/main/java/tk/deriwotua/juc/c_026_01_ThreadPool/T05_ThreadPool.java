/**
 * 线程池的概念
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Java 提供两种线程池
 * 	ThreadPoolExecutor
 * 		普通线程池
 * 	ForkJoinPool
 * 		分解汇总(任务)线程池
 * 		用很少的线程可以执行很多的任务(子任务)
 * 		CPU密集型
 */
public class T05_ThreadPool {
	public static void main(String[] args) throws InterruptedException {
		/**
		 *
		 * 		new ThreadPoolExecutor(5, 5,
		 *                             0L, TimeUnit.MILLISECONDS,
		 *                             new LinkedBlockingQueue<Runnable>()); // 无界阻塞队列
		 */
		ExecutorService service = Executors.newFixedThreadPool(5); //execute submit
		for (int i = 0; i < 6; i++) {
			/**
			 * 执行任务
			 */
			service.execute(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			});
		}
		System.out.println(service); // Running
		
		service.shutdown();
		System.out.println(service.isTerminated());
		System.out.println(service.isShutdown());
		System.out.println(service);  // Shutting down
		
		TimeUnit.SECONDS.sleep(5);
		System.out.println(service.isTerminated());
		System.out.println(service.isShutdown());
		System.out.println(service);
	}
}

package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledThreadPool 定时任务线程池(类似quartz定时器)
 * 	源码
 * 		new ThreadPoolExecutor(corePoolSize,
 *                               Integer.MAX_VALUE,
 *                               0,
 *                               NANOSECONDS,
 *                               new DelayedWorkQueue());
 *     ScheduledThreadPool 自定义核心线程数 Integer.MAX_VALUE容量的线程池
 *     		DelayedWorkQueue 间隔时间后执行的队列
 */
public class T10_ScheduledPool {
	public static void main(String[] args) {
		/**
		 * 4
		 */
		ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
		/**
		 * 多长时间执行一次
		 */
		service.scheduleAtFixedRate(()->{
			try {
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		},
		// 间隔500毫秒执行一次
		0, 500, TimeUnit.MILLISECONDS);
	}
	/**
	 * 假如提供一个闹钟服务，订阅服务的人特别多，10亿人，怎么优化？
	 */
}

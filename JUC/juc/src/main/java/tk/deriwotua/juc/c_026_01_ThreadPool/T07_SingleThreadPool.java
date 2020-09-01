package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SingleThreadExecutor 单线程线程池
 * 	为什么要有单线程线程池
 * 		任务队列
 * 		线程生命周期管理
 */
public class T07_SingleThreadPool {
	public static void main(String[] args) {
		/**
		 * 通过Executor线程池工厂类创建单线程线程池
		 * 	查看源码可以发现
		 * 		public static ExecutorService newSingleThreadExecutor() {
		 *         return new FinalizableDelegatedExecutorService
		 *             (new ThreadPoolExecutor(1, 1,
		 *                                     0L, TimeUnit.MILLISECONDS,
		 *                                     new LinkedBlockingQueue<Runnable>()));
		 *     }
		 *     SingleThreadExecutor 其实就是指定了最大1个线程永久存活(0s) 的 ThreadPoolExecutor
		 *     然后委托给 FinalizableDelegatedExecutorService 管理
		 *     需要注意这里指定的阻塞队列使用的默认容量 Integer.Max
		 *     		如果真的存在这么多的线程其实CPU就只光顾着进行线程间切换了
		 *     	Java手册中不建议使用JDK自带的API创建这种线程池
		 *     		容量默认任意OOM
		 *     		线程未指定名称不利于回溯
		 */
		ExecutorService service = Executors.newSingleThreadExecutor();
		for(int i=0; i<5; i++) {
			final int j = i;
			service.execute(()->{
				
				System.out.println(j + " " + Thread.currentThread().getName());
			});
		}
			
	}
}

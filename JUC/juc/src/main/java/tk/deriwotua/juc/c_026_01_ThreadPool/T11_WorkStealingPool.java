package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * WorkStealingPool
 * 	与 ThreadPoolExecutor 区别在于
 * 		ThreadPoolExecutor 维护了一个线程集合和一个队列 然后每个线程从公共队列里获取任务
 * 		而 WorkStealingPool 每个线程维护了一个队列 线程从自己的队列里获取任务
 * 		当 WorkStealingPool 某个线程队列里任务执行完后会从其他线程队列里获取任务
 * 	源码
 * 		public static ExecutorService newWorkStealingPool() {
 *         return new ForkJoinPool
 *             (Runtime.getRuntime().availableProcessors(),
 *              ForkJoinPool.defaultForkJoinWorkerThreadFactory,
 *              null, true);
 *     }
 */
public class T11_WorkStealingPool {
	public static void main(String[] args) throws IOException {
		ExecutorService service = Executors.newWorkStealingPool();
		System.out.println(Runtime.getRuntime().availableProcessors());

		service.execute(new R(1000));
		service.execute(new R(2000));
		service.execute(new R(2000));
		service.execute(new R(2000)); //daemon
		service.execute(new R(2000));
		
		//由于产生的是精灵线程（守护线程、后台线程），主线程不阻塞的话，看不到输出
		System.in.read(); 
	}

	static class R implements Runnable {

		int time;

		R(int t) {
			this.time = t;
		}

		@Override
		public void run() {
			try {
				TimeUnit.MILLISECONDS.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(time  + " " + Thread.currentThread().getName());
		}
	}
}
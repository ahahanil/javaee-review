package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FixedThreadPool 自定义核心线程数线程池
 * 	源码
 * 	    public static ExecutorService newFixedThreadPool(int nThreads) {
 *         return new ThreadPoolExecutor(nThreads, nThreads,
 *                                       0L, TimeUnit.MILLISECONDS,
 *                                       new LinkedBlockingQueue<Runnable>());
 *     }
 *  线程池的大小如何设定？
 *  	Java并发编程实战书中提供了一些优化建议。如果线程池中的线程数量过多最终它们会竞争
 *  	CPU和内存资源会浪费大量时间在切换上下文上但线程过少CPU的核心可能无法充分利用
 *  	建议的做法是 N(threads) = N(CPU) * U(CPU) * (1 + W/C) 进行估算
 *  			N(CPU) CPU核心数 可通过 Runtime.getRuntime().availableProcessors()获取到
 *  			U(CPU) 期望的CPU利用率
 *  			W/C 等待时间与计算时间比值
 *		估算后还没完还要进行压测验证是否达标不满意再调
 *
 * CachedThreadPool VS 	FixedThreadPool
 * 	当任务来的忽高忽低为了确保不会堆积使用CachedThreadPool
 * 	当任务来的比较平稳使用FixedThreadPool
 * concurrent并发 VS parallel并行
 * 	并发指任务提交
 * 	并行指任务执行
 * 	并行是并发的子集
 * 	FixedThreadPool 线程池可并行处理
 *
 * 获取指定区间的质数
 */
public class T09_FixedThreadPool {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/**
		 * 获取指定区间的质数方式一
		 */
		long start = System.currentTimeMillis();
		// 获取[1,200000]间的质数
		getPrime(1, 200000);
		long end = System.currentTimeMillis();
		System.out.println(end - start);

		/**
		 * 获取指定区间的质数方式二
		 * 	任务切分不同区间多线程并行处理
		 */
		final int cpuCoreNum = 4;
		ExecutorService service = Executors.newFixedThreadPool(cpuCoreNum);
		// 任务切分
		MyTask t1 = new MyTask(1, 80000); //1-5 5-10 10-15 15-20
		MyTask t2 = new MyTask(80001, 130000);
		MyTask t3 = new MyTask(130001, 170000);
		MyTask t4 = new MyTask(170001, 200000);
		
		Future<List<Integer>> f1 = service.submit(t1);
		Future<List<Integer>> f2 = service.submit(t2);
		Future<List<Integer>> f3 = service.submit(t3);
		Future<List<Integer>> f4 = service.submit(t4);
		
		start = System.currentTimeMillis();
		f1.get();
		f2.get();
		f3.get();
		f4.get();
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	/**
	 * 获取指定区间质数任务
	 */
	static class MyTask implements Callable<List<Integer>> {
		int startPos, endPos;
		
		MyTask(int s, int e) {
			this.startPos = s;
			this.endPos = e;
		}
		
		@Override
		public List<Integer> call() throws Exception {
			List<Integer> r = getPrime(startPos, endPos);
			return r;
		}
		
	}

	/**
	 * 质数
	 * @param num
	 * @return
	 */
	static boolean isPrime(int num) {
		for(int i=2; i<=num/2; i++) {
			if(num % i == 0) return false;
		}
		return true;
	}

	/**
	 * 获取指定区间质数
	 * @param start
	 * @param end
	 * @return
	 */
	static List<Integer> getPrime(int start, int end) {
		List<Integer> results = new ArrayList<>();
		for(int i=start; i<=end; i++) {
			if(isPrime(i)) results.add(i);
		}
		
		return results;
	}
}

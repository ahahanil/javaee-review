package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ParallelStreamAPI 并行流式API
 * 	底层 ForkJoinPool实现的
 * 	内部会把任务切分层子任务执行
 * 	当处理时线程间不需要同步使用并行流式处理能提高效率(相比lambda表达式能提升三四倍效率)
 */
public class T13_ParallelStreamAPI {
	public static void main(String[] args) {
		/**
		 * 集合里添加一万个随机数
		 */
		List<Integer> nums = new ArrayList<>();
		Random r = new Random();
		for(int i=0; i<10000; i++) nums.add(1000000 + r.nextInt(1000000));
		
		//System.out.println(nums);
		
		long start = System.currentTimeMillis();
		/**
		 * lambda 方式计算是否是质数
		 */
		nums.forEach(v->isPrime(v));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
		start = System.currentTimeMillis();
		/**
		 * 使用parallel stream api
		 * 并行流方式计算是否是质数
		 * 	并行流即内部会把任务切分层子任务执行
		 */
		nums.parallelStream().forEach(T13_ParallelStreamAPI::isPrime);
		end = System.currentTimeMillis();
		
		System.out.println(end - start);
		/**
		 * 最终输出的结果会发现
		 * 	parallelStream 效率是 lambda方式的三四倍
		 */
	}

	/**
	 * 是否是质数
	 * @param num
	 * @return
	 */
	static boolean isPrime(int num) {
		for(int i=2; i<=num/2; i++) {
			if(num % i == 0) return false;
		}
		return true;
	}
}

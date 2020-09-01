package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinPool 大任务切分小任务(结果可汇总)线程池
 */
public class T12_ForkJoinPool {
	/**
	 * 容量为一百万数组里存放随机数
	 */
	static int[] nums = new int[1000000];
	static Random r = new Random();
	/**
	 * 最大分片 每5万个为一组
	 * 区间超过5万时继续分片
	 */
	static final int MAX_NUM = 50000;
	
	static {
		for(int i=0; i<nums.length; i++) {
			// 存放一个随机数
			nums[i] = r.nextInt(100);
		}
		/**
		 * 这一百万个随机数要求和时单线程可以通过 Arrays.stream().sum() 流实现
		 */
		System.out.println("---" + Arrays.stream(nums).sum()); //stream api
	}

	/**
	 * ForkJoinPool处理的任务是需要能进行分叉的ForkJoinTask任务
	 * 	RecursiveAction 递归任务不带返回值的继承ForkJoinTask分叉任务
	 */
	static class AddTask extends RecursiveAction {
		/**
		 * 数组分片起始位置
		 */
		int start, end;

		AddTask(int s, int e) {
			start = s;
			end = e;
		}

		/**
		 * 任务是无返回值的
		 */
		@Override
		protected void compute() {
			/**
			 * 区间数小于分片数时进行计算
			 */
			if(end-start <= MAX_NUM) {
				long sum = 0L;
				for(int i=start; i<end; i++) sum += nums[i];
				System.out.println("from:" + start + " to:" + end + " = " + sum);
			}
			/**
			 * 否则继续进行分片
			 */
			else {
				// 分成两个子任务
				int middle = start + (end-start)/2;

				AddTask subTask1 = new AddTask(start, middle);
				AddTask subTask2 = new AddTask(middle, end);
				/**
				 * 子任务继续分叉
				 */
				subTask1.fork();
				subTask2.fork();
			}
		}
	}

	/**
	 * RecursiveTask 带返回值的继承ForkJoinTask分叉任务
	 */
	static class AddTaskRet extends RecursiveTask<Long> {
		
		private static final long serialVersionUID = 1L;
		int start, end;
		
		AddTaskRet(int s, int e) {
			start = s;
			end = e;
		}

		/**
		 * 带返回值的分叉任务
		 * @return
		 */
		@Override
		protected Long compute() {
			/**
			 * 区间数小于分片数时进行计算
			 */
			if(end-start <= MAX_NUM) {
				long sum = 0L;
				for(int i=start; i<end; i++) sum += nums[i];
				return sum;
			}
			/**
			 * 否则继续进行分片
			 */
			int middle = start + (end-start)/2;
			
			AddTaskRet subTask1 = new AddTaskRet(start, middle);
			AddTaskRet subTask2 = new AddTaskRet(middle, end);
			subTask1.fork();
			subTask2.fork();
			// 结果汇总
			return subTask1.join() + subTask2.join();
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		/**
		 * 无返回值的RecursiveAction分叉任务
		 */
		ForkJoinPool fjp1 = new ForkJoinPool();
		// 任务分片
		AddTask task = new AddTask(0, nums.length);
		// 执行任务
		fjp1.execute(task);
		// 后台线程需要阻塞等待结果防止退出
		//System.in.read();

		/**
		 * 带返回值的分叉任务
		 * 	结果最终汇总所以不需要阻塞等待结果
		 */
		ForkJoinPool fjp2 = new ForkJoinPool();
		AddTaskRet taskRet = new AddTaskRet(0, nums.length);
		// 执行任务
		fjp2.execute(taskRet);
		// 获取汇总值
		long result = taskRet.join();
		System.out.println(result);

	}
}

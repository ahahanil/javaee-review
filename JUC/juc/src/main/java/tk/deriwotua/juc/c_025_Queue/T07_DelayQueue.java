package tk.deriwotua.juc.c_025_Queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue 时间上延迟排序队列(无界延时阻塞队列)
 * 	基于 PriorityQueue 实现时间排序常用于按时间任务调度
 * 	队列中存放的元素必须是实现 java.util.concurrent.Delayed 接口
 * 		且必须有一个变量声明推迟多长时间
 *  延迟结合 PriorityQueue 优先队列
 *  	延迟时间作为权重
 *  	即延迟小的先出队列
 *  take() 时先取出队首元素为空,阻塞请求
 *  	不为空,获得这个元素的delay时间值
 *  	为0的话,说明该元素已经到了可以使用的时间,调用poll方法弹出该元素,跳出方法
 *  	等
 */
public class T07_DelayQueue {
	/**
	 * DelayQueue 属于阻塞队列
	 */
	static BlockingQueue<MyTask> tasks = new DelayQueue<>();

	static Random r = new Random();

	/**
	 * DelayQueue 存放的任务必须实现Delayed接口
	 * 	且必须有一个变量声明推迟多长时间
	 */
	static class MyTask implements Delayed {
		String name;
		/**
		 * 推迟多长时间
		 */
		long runningTime;
		
		MyTask(String name, long rt) {
			this.name = name;
			this.runningTime = rt;
		}

		/**
		 * 时间优先所以存在比较那个任务时间最短即按时间长短排序
		 * @param o
		 * @return
		 */
		@Override
		public int compareTo(Delayed o) {
			if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS))
				return -1;
			else if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) 
				return 1;
			else 
				return 0;
		}

		/**
		 * 以给定的时间单位返回与此对象关联的剩余延迟
		 * @param unit
		 * @return
		 */
		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
		
		@Override
		public String toString() {
			return name + " " + runningTime;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		long now = System.currentTimeMillis();
		// 当前时间之后1s运行
		MyTask t1 = new MyTask("t1", now + 1000);
		// 当前时间之后2s运行
		MyTask t2 = new MyTask("t2", now + 2000);
		MyTask t3 = new MyTask("t3", now + 1500);
		MyTask t4 = new MyTask("t4", now + 2500);
		MyTask t5 = new MyTask("t5", now + 500);

		/**
		 * 插入队列
		 */
		tasks.put(t1);
		tasks.put(t2);
		tasks.put(t3);
		tasks.put(t4);
		tasks.put(t5);
		
		System.out.println(tasks);

		/**
		 * 一般队列先进先出但是这里使用的是基于延迟排序的队列
		 * 所以延迟小的先出队列 依次出队列
		 * 	t5 t1 t3 t2 t4
		 */
		for(int i=0; i<5; i++) {
			System.out.println(tasks.take());
		}
	}
}

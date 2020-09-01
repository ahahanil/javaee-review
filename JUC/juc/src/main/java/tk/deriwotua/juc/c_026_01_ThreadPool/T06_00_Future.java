/**
 * 认识future
 * 异步
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.*;

/**
 * Future 任务未来时间执行后生产的结果
 *
 */
public class T06_00_Future {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService service = Executors.newFixedThreadPool(5);
		/**
		 * 提交任务
		 * 	submit()提交后是有返回值的所以提交 Runnable任务是不行的
		 * 	需要是 Callable任务
		 */
		Future<Integer> f = service.submit(() -> {
			TimeUnit.MILLISECONDS.sleep(500);
			return 1;
		});
		System.out.println(f.get());
		System.out.println(f.isDone());

		/**
		 * 相比较 Future 更常用的是 FutureTask
		 * FutureTask
		 * 	Future + Task 相当于既是一个任务类又任务类执行结果也放在自己对象里
		 *	FutureTask 实现RunnableFuture接口而RunnableFuture接口既继承了Runnable接口又继承了Future
		 *  既是任务又是执行后结果
		 *
		 * 因此定义 FutureTask 指定任务后可返回任务执行FutureTask结果
		 */
		FutureTask<Integer> task = new FutureTask<>(()->{
			TimeUnit.MILLISECONDS.sleep(500);
			return 1000;
		}); //new Callable () { Integer call();}

		/**
		 * FutureTask 实现了 Runnable接口任务类可以通过 Thread#start()执行
		 */
		new Thread(task).start();
		/**
		 * 阻塞式等待执行生产结果
		 */
		System.out.println(task.get()); //阻塞
	}
}

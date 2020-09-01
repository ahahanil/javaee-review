/**
 * 认识Executor
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.Executor;

/**
 * ThreadPoolExecutor 顶层接口 Executor（执行者）
 * 	ThreadPoolExecutor -> ExecutorService -> Executor
 */
public class T01_MyExecutor implements Executor{

	public static void main(String[] args) {

		new T01_MyExecutor().execute(()->System.out.println("hello executor"));
	}

	/**
	 * Executor 执行方法
	 * @param command
	 */
	@Override
	public void execute(Runnable command) {
		//new Thread(command).run();
		command.run();
	}
}
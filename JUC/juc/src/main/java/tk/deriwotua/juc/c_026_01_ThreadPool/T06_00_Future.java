/**
 * ��ʶfuture
 * �첽
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.*;

/**
 * Future ����δ��ʱ��ִ�к������Ľ��
 *
 */
public class T06_00_Future {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService service = Executors.newFixedThreadPool(5);
		/**
		 * �ύ����
		 * 	submit()�ύ�����з���ֵ�������ύ Runnable�����ǲ��е�
		 * 	��Ҫ�� Callable����
		 */
		Future<Integer> f = service.submit(() -> {
			TimeUnit.MILLISECONDS.sleep(500);
			return 1;
		});
		System.out.println(f.get());
		System.out.println(f.isDone());

		/**
		 * ��Ƚ� Future �����õ��� FutureTask
		 * FutureTask
		 * 	Future + Task �൱�ڼ���һ����������������ִ�н��Ҳ�����Լ�������
		 *	FutureTask ʵ��RunnableFuture�ӿڶ�RunnableFuture�ӿڼȼ̳���Runnable�ӿ��ּ̳���Future
		 *  ������������ִ�к���
		 *
		 * ��˶��� FutureTask ָ�������ɷ�������ִ��FutureTask���
		 */
		FutureTask<Integer> task = new FutureTask<>(()->{
			TimeUnit.MILLISECONDS.sleep(500);
			return 1000;
		}); //new Callable () { Integer call();}

		/**
		 * FutureTask ʵ���� Runnable�ӿ����������ͨ�� Thread#start()ִ��
		 */
		new Thread(task).start();
		/**
		 * ����ʽ�ȴ�ִ���������
		 */
		System.out.println(task.get()); //����
	}
}

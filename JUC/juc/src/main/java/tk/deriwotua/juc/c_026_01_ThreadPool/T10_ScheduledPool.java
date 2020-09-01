package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledThreadPool ��ʱ�����̳߳�(����quartz��ʱ��)
 * 	Դ��
 * 		new ThreadPoolExecutor(corePoolSize,
 *                               Integer.MAX_VALUE,
 *                               0,
 *                               NANOSECONDS,
 *                               new DelayedWorkQueue());
 *     ScheduledThreadPool �Զ�������߳��� Integer.MAX_VALUE�������̳߳�
 *     		DelayedWorkQueue ���ʱ���ִ�еĶ���
 */
public class T10_ScheduledPool {
	public static void main(String[] args) {
		/**
		 * 4
		 */
		ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
		/**
		 * �೤ʱ��ִ��һ��
		 */
		service.scheduleAtFixedRate(()->{
			try {
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		},
		// ���500����ִ��һ��
		0, 500, TimeUnit.MILLISECONDS);
	}
	/**
	 * �����ṩһ�����ӷ��񣬶��ķ�������ر�࣬10���ˣ���ô�Ż���
	 */
}

package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SingleThreadExecutor ���߳��̳߳�
 * 	ΪʲôҪ�е��߳��̳߳�
 * 		�������
 * 		�߳��������ڹ���
 */
public class T07_SingleThreadPool {
	public static void main(String[] args) {
		/**
		 * ͨ��Executor�̳߳ع����ഴ�����߳��̳߳�
		 * 	�鿴Դ����Է���
		 * 		public static ExecutorService newSingleThreadExecutor() {
		 *         return new FinalizableDelegatedExecutorService
		 *             (new ThreadPoolExecutor(1, 1,
		 *                                     0L, TimeUnit.MILLISECONDS,
		 *                                     new LinkedBlockingQueue<Runnable>()));
		 *     }
		 *     SingleThreadExecutor ��ʵ����ָ�������1���߳����ô��(0s) �� ThreadPoolExecutor
		 *     Ȼ��ί�и� FinalizableDelegatedExecutorService ����
		 *     ��Ҫע������ָ������������ʹ�õ�Ĭ������ Integer.Max
		 *     		�����Ĵ�����ô����߳���ʵCPU��ֻ����Ž����̼߳��л���
		 *     	Java�ֲ��в�����ʹ��JDK�Դ���API���������̳߳�
		 *     		����Ĭ������OOM
		 *     		�߳�δָ�����Ʋ����ڻ���
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

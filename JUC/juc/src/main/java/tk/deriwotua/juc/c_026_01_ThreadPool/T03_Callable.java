/**
 * ��ʶCallable����Runnable��������չ
 * ��Callable�ĵ��ã������з���ֵ
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.*;

/**
 * Callable �ӿڶ�Runnable��������չ
 *  Callable#call() �൱�� Runnable#run()
 *  ��Callable#call()�з���ֵ
 */
public class T03_Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * ����ֵString���� Callable����
         */
        Callable<String> c = new Callable() {
            @Override
            public String call() throws Exception {
                return "Hello Callable";
            }
        };

        ExecutorService service = Executors.newCachedThreadPool();
        /**
         * �����ύ���̳߳�(�첽)
         */
        Future<String> future = service.submit(c);
        /**
         * java.util.concurrent.Future#get() ����ʽ�ȴ��첽����ֱ���н������
         */
        System.out.println(future.get());

        /**
         * �������� ����һ������ֵ��String���͵�Callable�����ύ���̳߳��첽ִ��
         * ���ڲ�֪���̳߳�ʲôʱ��ִ�в������ʹ��java.util.concurrent.Future#get() ����ʽ�ȴ��첽����ֱ���н������
         * ֱ���н����������� Future<String> ��
         */

        // �ر��̳߳�
        service.shutdown();
    }

}

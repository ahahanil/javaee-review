/**
 * ��ʶExecutorService,�Ķ�API�ĵ�
 * ��ʶsubmit��������չ��execute����������һ������ֵ
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecutorService �������� Executor ����ִ����������
 *  ��չ��execute����
 *      submit() �ύ����ʲôʱ��ִ�����̳߳ؾ���(�൱���첽)
 *      Ȼ�����һ�����
 */
public class T02_ExecutorService  {
    public static void main(String[] args) {
        ExecutorService e = Executors.newCachedThreadPool();
    }
}

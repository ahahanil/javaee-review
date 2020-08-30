/**
 * ThreadLocal�ֲ߳̾�����
 *
 * ThreadLocal��ʹ�ÿռ任ʱ�䣬synchronized��ʹ��ʱ�任�ռ�
 * ������hibernate��session�ʹ�����ThreadLocal�У�����synchronized��ʹ��
 *
 * ��������ĳ������ThreadLocal
 * 
 * @author ��ʿ��
 */
package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal
 * 	java.lang.ThreadLocal#set(java.lang.Object)����ֵʱ�ȳ��Ի�ȡ��ǰ�̵߳� ThreadLocalMap ����
 * 		��������ڴ��� ThreadLocalMap �ŵ��߳���
 * 		������� �Ե�ǰThreadLocal��Ϊkey��ֵ����Map��
 * Thread����Ĭ��������һ��������
 * 	ThreadLocal.ThreadLocalMap threadLocals = null;
 * Spring������ʽ����ͨ�� ThreadLocal �ɱ�֤ͬһ�������ڶ����������ͬһ��������
 */
public class _02ThreadLocal {
	/**
	 * �����̼߳乲��
	 */
	//volatile static Person p = new Person();
	/**
	 * ���ĳЩ������Ҫ�̼߳��һ���໥����
	 */
	static ThreadLocal<Person> tl = new ThreadLocal<>();
	
	public static void main(String[] args) {
				
		new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(tl.get());
		}).start();
		
		new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tl.set(new Person());
		}).start();
	}
	
	static class Person {
		String name = "zhangsan";
	}
}



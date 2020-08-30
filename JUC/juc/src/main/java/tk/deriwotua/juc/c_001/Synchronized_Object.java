/**
 * synchronized�ؼ���
 * ��ĳ���������
 * @author mashibing
 */

package tk.deriwotua.juc.c_001;

public class Synchronized_Object {
	
	private int count = 10;
	private Object o = new Object();
	
	public void m() {
		synchronized(o) { //�κ��߳�Ҫִ������Ĵ��룬�������õ�o����
			count--;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
		}
	}
	
}


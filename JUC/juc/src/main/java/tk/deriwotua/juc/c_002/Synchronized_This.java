/**
 * synchronized�ؼ���
 * ��ĳ���������
 * @author mashibing
 */

package tk.deriwotua.juc.c_002;

public class Synchronized_This {
	
	private int count = 10;
	
	public void m() {
		synchronized(this) { //�κ��߳�Ҫִ������Ĵ��룬�������õ�this����
			count--;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
		}
	}
	
}


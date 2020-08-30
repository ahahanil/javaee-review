/**
 * synchronized�ؼ���
 * ��ĳ���������
 * @author mashibing
 */

package tk.deriwotua.juc.c_003;

public class Synchronized_Function {

	private int count = 10;
	
	public synchronized void m() { //��ͬ���ڷ����Ĵ���ִ��ʱҪsynchronized(this)
		count--;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}


}

/**
 * дʱ�������� copy on write
 * ���̻߳����£�дʱЧ�ʵͣ���ʱЧ�ʸ�
 * �ʺ�д�ٶ���Ļ���
 * 
 * 
 * 
 * @author ��ʿ��
 */
package tk.deriwotua.juc.c_025;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList дʱ��������
 * 	�������ж�����ʱ�ǲ�������
 * 		������ԭ���������������ھ���������������һ�µ�
 * 	������java.util.concurrent.CopyOnWriteArrayList#add(java.lang.Object)д����ʱ����
 * 		д����ʱ�Ȱ���������(����ʱ������һ)Ȼ����β������Ҫ���ӵ�Ԫ��
 * 		Ȼ���ٰ�ָ�������ָ��ָ���Ƶ����
 *
 */
public class T02_CopyOnWriteList {
	public static void main(String[] args) {
		List<String> lists = 
				//new ArrayList<>(); //�������������⣡
				//new Vector();
				new CopyOnWriteArrayList<>();
		Random r = new Random();
		Thread[] ths = new Thread[100];
		/**
		 * ÿ���߳���lists���1000��Ԫ��
		 */
		for(int i=0; i<ths.length; i++) {
			Runnable task = new Runnable() {
	
				@Override
				public void run() {
					for(int i=0; i<1000; i++) lists.add("a" + r.nextInt(10000));
				}
				
			};
			ths[i] = new Thread(task);
		}
		
		
		runAndComputeTime(ths);
		
		System.out.println(lists.size());
	}
	
	static void runAndComputeTime(Thread[] ths) {
		long s1 = System.currentTimeMillis();
		Arrays.asList(ths).forEach(t->t.start());
		Arrays.asList(ths).forEach(t->{
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		long s2 = System.currentTimeMillis();
		System.out.println(s2 - s1);
		
	}
}

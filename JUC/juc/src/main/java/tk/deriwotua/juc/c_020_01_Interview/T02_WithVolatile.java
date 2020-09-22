/**
 * �����������⣺���Ա�����
 * ʵ��һ���������ṩ����������add��size
 * д�����̣߳��߳�1���10��Ԫ�ص������У��߳�2ʵ�ּ��Ԫ�صĸ�������������5��ʱ���߳�2������ʾ������
 * 
 * ��lists���volatile֮��t2�ܹ��ӵ�֪ͨ�����ǣ�t2�̵߳���ѭ�����˷�cpu�����������ѭ����
 * ���ң������if �� break֮�䱻����̴߳�ϣ��õ��Ľ��Ҳ����ȷ��
 * ����ô���أ�
 * @author mashibing
 */
package tk.deriwotua.juc.c_020_01_Interview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class T02_WithVolatile {

	/**
	 * ���volatile��ʹt2�ܹ��õ�֪ͨ
	 * 	���� ���Ǵ��� add �� list����sizeǰ c.size()��ȡ�˴�С��ʱ�����ǲ�׼��
	 * ����һ���ǳ�������� list ������ ����add()�����ı���� list ָ��ռ����ݶ� listָ�벢û�б仯
	 * 	��������� volatile ��listָ����ʱ��֪ͨ�������߳����ڻ�����Ч�������ȡ ����ʱ����listָ��û�б䶯
	 * 	��Ϊʲô���� volatile ������ȷ��
	 * 		�������� t1 TimeUnit.SECONDS.sleep(1); ��� ȥ��������ֳ��� volatile �޶������ǽ����Ч ����1s���ɺ϶���
	 * ���ۣ�	û�а���ʱ��Ҫ�� volatile ��� volatile ��Ҫ���������������϶����ڻ���������
	 * 		���ö����ڲ����Ա�� volatile �����߳�Ҳ�ǹ۲첻����
	 */
	//volatile List lists = new LinkedList();
	/**
	 * java.util.Collections.SynchronizedList ͬ������ �ڲ���������ͨ�� synchronized ����
	 * ��֤ list add()��size()���ͬ��
	 */
	volatile List lists = Collections.synchronizedList(new LinkedList<>());

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}

	public static void main(String[] args) {

		T02_WithVolatile c = new T02_WithVolatile();
		new Thread(() -> {
			for(int i=0; i<10; i++) {
				c.add(new Object());
				System.out.println("add " + i);
				
				/*try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}, "t1").start();
		
		new Thread(() -> {
			while(true) {
				if(c.size() == 5) {
					break;
				}
			}
			System.out.println("t2 ����");
		}, "t2").start();
	}
}

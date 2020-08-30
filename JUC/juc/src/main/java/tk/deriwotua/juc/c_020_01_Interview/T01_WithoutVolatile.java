/**
 * �����������⣺���Ա�����
 * ʵ��һ���������ṩ����������add��size
 * д�����̣߳��߳�1���10��Ԫ�ص������У��߳�2ʵ�ּ��Ԫ�صĸ�������������5��ʱ���߳�2������ʾ������
 * 
 * �����������������������������
 * @author mashibing
 */
package tk.deriwotua.juc.c_020_01_Interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class T01_WithoutVolatile {

	List lists = new ArrayList();

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}
	
	public static void main(String[] args) {
		T01_WithoutVolatile c = new T01_WithoutVolatile();

		new Thread(() -> {
			for(int i=0; i<10; i++) {
				c.add(new Object());
				System.out.println("add " + i);
				
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t1").start();
		
		new Thread(() -> {
			while(true) {
				/**
				 * ArrayList ��ͬ����
				 * ���� add ��list�����¼���size��С
				 * 	������ add �� list����sizeǰ c.size()��ȡ�˴�С��ʱ�����ǲ�׼��
				 * ����̼߳����ݲ���ͬ��
				 * 	t1 �߳�add �� t2 �߳��ǲ��ɼ��Ĳ��������ϴ������л�ȡ��
				 * ��ʱ���ܻ��뵽list��� Volatile�޶� ִ�п��ܽ����ȷ���������ܽ��
				 * 	 add �� list����sizeǰ c.size()��ȡ�˴�С��ʱ�����ǲ�׼��
				 */
				if(c.size() == 5) {
					/**
					 * break �̼߳��޷�ͨ�� break��������t1�߳�ֹͣ
					 */
					break;
				}
			}
			System.out.println("t2 ����");
		}, "t2").start();
	}
}

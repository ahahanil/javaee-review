/**
 * 曾经的面试题：（淘宝？）
 * 实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 * 
 * 分析下面这个程序，能完成这个功能吗？
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
				 * ArrayList 非同步的
				 * 数据 add 到list后会更新集合size大小
				 * 	问题是 add 后 list更新size前 c.size()读取了大小此时数据是不准的
				 * 其二线程间数据不会同步
				 * 	t1 线程add 后 t2 线程是不可见的并不会马上从主存中获取到
				 * 此时可能会想到list添加 Volatile限定 执行可能结果正确但还不不能解决
				 * 	 add 后 list更新size前 c.size()读取了大小此时数据是不准的
				 */
				if(c.size() == 5) {
					/**
					 * break 线程间无法通信 break并不会让t1线程停止
					 */
					break;
				}
			}
			System.out.println("t2 结束");
		}, "t2").start();
	}
}

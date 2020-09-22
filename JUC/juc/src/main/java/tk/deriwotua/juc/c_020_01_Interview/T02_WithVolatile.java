/**
 * 曾经的面试题：（淘宝？）
 * 实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 * 
 * 给lists添加volatile之后，t2能够接到通知，但是，t2线程的死循环很浪费cpu，如果不用死循环，
 * 而且，如果在if 和 break之间被别的线程打断，得到的结果也不精确，
 * 该怎么做呢？
 * @author mashibing
 */
package tk.deriwotua.juc.c_020_01_Interview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class T02_WithVolatile {

	/**
	 * 添加volatile，使t2能够得到通知
	 * 	但是 还是存在 add 后 list更新size前 c.size()读取了大小此时数据是不准的
	 * 还有一个非常奇怪问题 list 是引用 调用add()方法改变的是 list 指向空间内容而 list指针并没有变化
	 * 	按道理加了 volatile 当list指向变更时会通知其他的线程其内缓冲无效从主存读取 问题时这里list指向并没有变动
	 * 	那为什么加了 volatile 后结果正确？
	 * 		问题在于 t1 TimeUnit.SECONDS.sleep(1); 这句 去掉后就体现出来 volatile 限定后本质是结果无效 休眠1s后巧合对了
	 * 结论：	没有把握时不要用 volatile 其二 volatile 不要修饰在引用类型上多用在基本类型上
	 * 		引用对象内部属性变更 volatile 其他线程也是观察不到的
	 */
	//volatile List lists = new LinkedList();
	/**
	 * java.util.Collections.SynchronizedList 同步容器 内部方法都是通过 synchronized 加锁
	 * 保证 list add()或size()间的同步
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
			System.out.println("t2 结束");
		}, "t2").start();
	}
}

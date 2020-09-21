/**
 * 解决同样的问题的更高效的方法，使用AtomXXX类
 * AtomXXX类本身方法都是原子性的，但不能保证多个方法连续调用是原子性的
 * @author mashibing
 */
package tk.deriwotua.juc.c_018_00_AtomicXXX;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class T01_AtomicInteger {
	/*volatile*/ //int count1 = 0;

	/**
	 * 原子类
	 * 访问共享资源时根据需不需要加锁可分为乐观锁、悲观锁
	 * 	悲观锁认为在访问共享资源时会有其它线程去修改共享资源所以需要加锁存在
	 * 	乐观锁认为在单线程访问共享资源时不会加锁，只是在修改共享资源前会判断共享资源是否发生了变更
	 * 		底层依赖CAS比较替换算法 和 自旋
	 */
	AtomicInteger count = new AtomicInteger(0); 

	/*synchronized*/ void m() { 
		for (int i = 0; i < 10000; i++)
			//if count1.get() < 1000
			count.incrementAndGet(); //count1++
	}

	public static void main(String[] args) {
		T01_AtomicInteger t = new T01_AtomicInteger();

		List<Thread> threads = new ArrayList<Thread>();

		for (int i = 0; i < 10; i++) {
			threads.add(new Thread(t::m, "thread-" + i));
		}

		threads.forEach((o) -> o.start());

		threads.forEach((o) -> {
			try {
				o.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		System.out.println(t.count);
	}

}

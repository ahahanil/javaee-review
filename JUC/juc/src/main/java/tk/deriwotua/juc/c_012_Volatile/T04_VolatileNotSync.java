/**
 * volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 * 运行下面的程序，并分析结果
 * @author mashibing
 */
package tk.deriwotua.juc.c_012_Volatile;

import java.util.ArrayList;
import java.util.List;

/**
 * 最终值一定小于 100000
 * 	count++ 非原子性操作 需要加锁
 * 	volatile 保证可见性但不能替代 synchronized
 */
public class T04_VolatileNotSync {
	volatile int count = 0;

	/**
	 * count++ 非原子性操作
	 */
	void m() {
		for(int i=0; i<10000; i++) count++;
	}
	
	public static void main(String[] args) {
		T04_VolatileNotSync t = new T04_VolatileNotSync();
		
		List<Thread> threads = new ArrayList<Thread>();
		
		for(int i=0; i<10; i++) {
			threads.add(new Thread(t::m, "thread-"+i));
		}
		
		threads.forEach((o)->o.start());
		
		threads.forEach((o)->{
			try {
				o.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		System.out.println(t.count);
	}
	
}



/**
 * 有N张火车票，每张票都有一个编号
 * 同时有10个窗口对外售票
 * 请写一个模拟程序
 * 
 * 分析下面的程序可能会产生哪些问题？
 *  
 * 使用Vector或者Collections.synchronizedXXX
 * 分析一下，这样能解决问题吗？
 * 
 * @author 马士兵
 */
package tk.deriwotua.juc.c_024_FromVectorToQueue;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Vector 自带锁list
 * 	还是存在问题 Vector#size()、Vector#Remove()单独本身是线程安全的
 * 	问题是 Vector#size()、Vector#Remove()期间如果存在逻辑是非线程安全
 */
public class TicketSeller2 {
	static Vector<String> tickets = new Vector<>();

	static {
		for(int i=0; i<1000; i++) tickets.add("票 编号：" + i);
	}
	
	public static void main(String[] args) {
		
		for(int i=0; i<10; i++) {
			new Thread(()->{
				while(tickets.size() > 0) {
					/**
					 * 这里是非原子的 存在线程安全问题
					 */
					try {
						TimeUnit.MILLISECONDS.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println("销售了--" + tickets.remove(0));
				}
			}).start();
		}
	}
}

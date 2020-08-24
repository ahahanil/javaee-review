package tk.deriwotua.thread.communication.farmerchild;

/**
 * 农夫采摘水果放到筐里，小孩从筐里拿水果吃，农夫是一个线程，小孩是一个线程，水果筐放满了，农夫停；水果筐空了，小孩停。
 */
public class TestFarmerChild {
	public static void main(String[] args) {
		new Farmer().start();
		new Child().start();
	}
}

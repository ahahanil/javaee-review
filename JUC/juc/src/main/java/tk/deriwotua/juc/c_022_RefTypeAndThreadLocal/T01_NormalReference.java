package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.io.IOException;

/**
 * 普通引用(即强引用)
 *  只要存在变量引用垃圾回收器就不会回收
 */
public class T01_NormalReference {
    public static void main(String[] args) throws IOException {
        M m = new M();
        m = null;
        System.gc(); //DisableExplicitGC

        // 阻塞当前线程防止退出便于观察
        System.in.read();
    }
}

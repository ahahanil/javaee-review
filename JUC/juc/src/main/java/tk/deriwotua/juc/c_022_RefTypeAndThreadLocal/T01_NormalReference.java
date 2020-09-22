package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.io.IOException;

/**
 * 普通引用(即强引用)
 *  JVM垃圾回收器 GC 可达性分析结果为可达，表示引用类型仍然被引用着，这类对象始终不会被垃圾回收器回收，即使JVM发生OOM也不会回收。
 *  而如果 GC 的可达性分析结果为不可达，那么在GC时会被回收
 *
 *  即只要存在变量引用垃圾回收器就不会回收
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

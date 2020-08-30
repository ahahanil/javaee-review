package tk.deriwotua.juc.c_021_03_VarHandle;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * JDK 9 新增API
 * 意义在于可通过指针直接操作指针指向的内存空间而且是原子性操作直接操作二进制码
 * JDK 9 之前这样操作要通过反射完成那性能不能与此相比
 *
 * 在 JDK9新API下 AQS 内部 java.util.concurrent.locks.AbstractQueuedSynchronizer#addWaiter(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node)
 * 就有使用 VarHandle 直接操作AQS内部链表节点
 */
public class T01_HelloVarHandle {

    int x = 8;

    /**
     * 一个指针变量
     */
    private static VarHandle handle;

    static {
        try {
            /**
             * 设置指针变量指向
             *  通过查找 T01_HelloVarHandle.class 中变量名为 x 类型为 int 的变量
             *  即 handle 指向语句 int x = 8; 的值 8 的内存空间
             */
            handle = MethodHandles.lookup().findVarHandle(T01_HelloVarHandle.class, "x", int.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        T01_HelloVarHandle t = new T01_HelloVarHandle();

        /**
         * VarHandle 意义在于可通过指针直接操作指针指向的内存空间
         * C/C++ 用法 内部就是包装的C/C++操作
         */
        //plain read / write
        System.out.println((int)handle.get(t));
        // 原子性操作
        // 直接操作实例化对象t的属性x值 设置为9
        handle.set(t,9);
        System.out.println(t.x);
        // 原子性操作
        handle.compareAndSet(t, 9, 10);
        System.out.println(t.x);
        // 原子性操作
        handle.getAndAdd(t, 10);
        System.out.println(t.x);

    }
}

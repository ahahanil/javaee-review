package tk.deriwotua.jvm;

public class TestJVM {
    public static void main(String[] args) {
        /**
         * System.getProperty(); 获取系统参数
         *  在运行的时候可指定参数
         */
        String str = System.getProperty("str");
        if (str == null) {
            System.out.println("deriwotua");
        } else {
            System.out.println(str);
        }
        /**
         * 设置了-XX:+DisableExplicitGC禁用手动调用gc操作
         *   手动调用gc无效
         */
        System.gc();
    }
}

package tk.deriwotua.dp.D01_singleton;

/**
 * 使用枚举来实现单实例控制会更加简洁，而且无偿地提供了序列化机制，
 * 并由JVM从根本上提供保障，绝对防止多次实例化，是更简洁、高效、安全的实现单例的方式。
 * <p>
 * 单元素的枚举类型已经成为实现Singleton的最佳方法
 * 不仅可以解决线程同步，还可以防止反序列化。
 */
public enum Mgr08 {

    /**
     * 定义一个枚举的元素，它就代表了Mgr08的一个实例。
     */
    INSTANCE;

    public void m() {
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Mgr08.INSTANCE.hashCode());
            }).start();
        }
    }

}

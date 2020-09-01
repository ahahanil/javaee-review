package tk.deriwotua.dp.D02_strategy;

/**
 * 抽象策略类
 *
 * @param <T>
 * @see java.util.Comparator 接口
 */
@FunctionalInterface
public interface Comparator<T> {

    /**
     * 策略方法
     *
     * @param o1
     * @param o2
     * @return
     */
    int compare(T o1, T o2);

    default void m() {
        System.out.println("m");
    }
}

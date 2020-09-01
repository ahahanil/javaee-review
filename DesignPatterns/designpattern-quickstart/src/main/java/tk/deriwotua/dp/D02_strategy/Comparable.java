package tk.deriwotua.dp.D02_strategy;

/**
 * 抽象策略类
 *
 * @param <T>
 * @see java.lang.Comparable
 */
public interface Comparable<T> {
    /**
     * 策略方法
     *
     * @param o
     * @return
     */
    int compareTo(T o);
}

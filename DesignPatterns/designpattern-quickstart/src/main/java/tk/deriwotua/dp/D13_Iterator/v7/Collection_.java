package tk.deriwotua.dp.D13_Iterator.v7;

/**
 * 泛型容器
 * @param <E>
 */
public interface Collection_<E> {
    void add(E o);
    int size();

    Iterator_ iterator();
}

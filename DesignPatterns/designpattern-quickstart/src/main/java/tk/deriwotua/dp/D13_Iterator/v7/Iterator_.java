package tk.deriwotua.dp.D13_Iterator.v7;

/**
 * 泛型迭代器
 * @param <E>
 */
public interface Iterator_<E> { //Element //Type //K //Value V Tank
    boolean hasNext();

    E next(); //Tank next() Iterator_<Tank> it = ... Tank t = it.next();
}

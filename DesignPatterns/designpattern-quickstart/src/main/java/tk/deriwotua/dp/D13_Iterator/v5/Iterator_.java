package tk.deriwotua.dp.D13_Iterator.v5;

/**
 * 迭代器模式
 *  不关注各容器内部数据结构
 *  容器自己去实现自己的迭代方式
 */
public interface Iterator_ {
    /**
     * 是否还存在下一个元素
     * @return
     */
    boolean hasNext();

    /**
     * 下一个元素
     * @return
     */
    Object next();
}

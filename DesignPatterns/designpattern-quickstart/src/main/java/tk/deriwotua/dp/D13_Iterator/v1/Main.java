package tk.deriwotua.dp.D13_Iterator.v1;

/**
 * 构建一个容器，可以添加对象
 *  底层基于数组
 *
 *  物理存储分为连续存储和非连续存储
 *      连续 -> 数组
 *      非连续通过指针指向下一块区域 -> 链表
 *          数据结构中很多都是逻辑结构存储
 */

public class Main {
    public static void main(String[] args) {
        ArrayList_ list = new ArrayList_();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s" + i));
        }
        System.out.println(list.size());
    }
}

/**
 * 相比数组，这个容器不用考虑边界问题，可以动态扩展
 */
class ArrayList_ {
    /**
     * 底层基于数组
     */
    Object[] objects = new Object[10];
    //objects中下一个空的位置在哪儿,或者说，目前容器中有多少个元素
    private int index = 0;

    public void add(Object o) {
        if (index == objects.length) {
            /**
             * 数组动态扩展
             *  当初始数组长度满了后创建一个更大容量新数组然后把数组数据拷贝进去最后再把要插入的数据插入进去
             */
            Object[] newObjects = new Object[objects.length * 2];
            // 拷贝
            System.arraycopy(objects, 0, newObjects, 0, objects.length);
            objects = newObjects;
        }

        objects[index] = o;
        index++;
    }

    public int size() {
        return index;
    }
}
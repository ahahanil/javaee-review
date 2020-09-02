package tk.deriwotua.dp.D13_Iterator.v4;

/**
 * 如何对容器遍历呢？如何考虑通用性
 *  容器自己实现自己的遍历方法
 */

public class Main {
    public static void main(String[] args) {
        Collection_ list = new ArrayList_();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s" + i));
        }
        System.out.println(list.size());


        ArrayList_ al = (ArrayList_) list;
        for (int i = 0; i < al.size(); i++) {
            //如果用这种遍历方式，就不能实现通用了
        }
    }
}



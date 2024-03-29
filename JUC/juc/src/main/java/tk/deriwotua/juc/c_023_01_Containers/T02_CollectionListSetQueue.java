package tk.deriwotua.juc.c_023_01_Containers;

import java.util.*;

public class T02_CollectionListSetQueue {
    public static void main(String[] args) {
        Collection<Integer> c1 = new ArrayList();
        c1.add(1);
        c1.add(2);
        c1.add(3);
        c1.stream().forEach(System.out::println);

        List<Integer> c2 = new ArrayList<>();
        Set<Integer> c3 = new HashSet<>();
        /**
         * LinkedList 也实现了 Deque 接口
         */
        Queue<Integer> c4 = new LinkedList<>();

    }
}

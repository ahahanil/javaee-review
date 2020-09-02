package tk.deriwotua.dp.D13_Iterator.v2;

/**
 * 用链表来实现一个容器
 */

public class Main {
    public static void main(String[] args) {
        LinkedList_ list = new LinkedList_();
        for(int i=0; i<15; i++) {
            list.add(new String("s" + i));
        }
        System.out.println(list.size());
    }
}


/**
 * 相比数组，这个容器不用考虑边界问题，可以动态扩展
 */
class LinkedList_ {
    Node head = null;
    Node tail = null;
    //目前容器中有多少个元素
    private int size = 0;

    public void add(Object o) {
        Node n = new Node(o);
        n.next = null;

        if(head == null) {
            head = n;
            tail = n;
        }

        tail.next = n;
        tail = n;
        size++;
    }

    /**
     * 节点
     */
    private class Node {
        private Object o;
        /**
         * 下一个节点
         */
        Node next;

        public Node(Object o) {
            this.o = o;
        }
    }

    public int size() {
        return size;
    }
}
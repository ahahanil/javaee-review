package tk.deriwotua.dp.D13_Iterator.v5;


/**
 * 基于数组容器
 */
class ArrayList_ implements Collection_ {
    Object[] objects = new Object[10];
    //objects中下一个空的位置在哪儿,或者说，目前容器中有多少个元素
    private int index = 0;
    public void add(Object o) {
        if(index == objects.length) {
            Object[] newObjects = new Object[objects.length*2];
            System.arraycopy(objects, 0, newObjects, 0, objects.length);
            objects = newObjects;
        }

        objects[index] = o;
        index ++;
    }

    public int size() {
        return index;
    }

    @Override
    public Iterator_ iterator() {
        return new ArrayListIterator();
    }

    /**
     * 实现自己迭代方式
     */
    private class ArrayListIterator implements Iterator_{

        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            if(currentIndex >= index) return false;
            return true;
        }

        @Override
        public Object next() {
            Object o = objects[currentIndex];
            currentIndex ++;
            return o;
        }
    }


}
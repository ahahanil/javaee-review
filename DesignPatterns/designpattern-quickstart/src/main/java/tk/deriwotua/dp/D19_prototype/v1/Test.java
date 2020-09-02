package tk.deriwotua.dp.D19_prototype.v1;

/**
 * 原型模式也叫克隆模式
 * Java自带原型模式实现需要实现Cloneable标记型接口
 *      标记型接口设置接口不需要实现任何方法
 *      重写clone()方法
 *      如果一个类重写了clone()方法但没有实现Cloneable标记型接口调用时就会抛异常
 * 用于确定属性的对象，需要产生很多相同属性对象时
 *  需要区分浅拷贝和深拷贝
 */

public class Test {
    public static void main(String[] args) throws Exception {
        Person p1 = new Person();
        Person p2 = (Person)p1.clone();
        System.out.println(p2.age + " " + p2.score);
        System.out.println(p2.loc);

        /**
         * 浅拷贝 两个对象引用(指向)同一个Location
         */
        System.out.println(p1.loc == p2.loc);
        p1.loc.street = "sh";
        System.out.println(p2.loc);

    }
}

/**
 * 克隆需要实现 Cloneable 接口
 *  如果一个类重写了clone()方法但没有实现Cloneable标记型接口调用时就会抛异常
 */
class Person implements Cloneable {
    int age = 8;
    int score = 100;

    Location loc = new Location("bj", 22);

    /**
     * 重写克隆
     *  Object#clone()是 protected native 修饰的所以需要重写改为public修饰以调用
     *      内部通过C/C++直接把实例化对象的内存空间复制了一份
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class Location {
    String street;
    int roomNo;

    @Override
    public String toString() {
        return "Location{" +
                "street='" + street + '\'' +
                ", roomNo=" + roomNo +
                '}';
    }

    public Location(String street, int roomNo) {
        this.street = street;
        this.roomNo = roomNo;
    }
}

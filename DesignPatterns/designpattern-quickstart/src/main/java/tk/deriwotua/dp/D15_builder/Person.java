package tk.deriwotua.dp.D15_builder;

/**
 * 构建复杂对象时要使用Builder模式
 *
 */
public class Person {

    int id;
    String name;
    int age;
    double weight;
    int score;
    Location loc;

    private Person() {}

    /**
     * Person静态内部类只用来构建Person
     * Person对象有非常多的属性 创建时一个一个调用setXxx()不优雅
     */
    public static class PersonBuilder {
        Person p = new Person();

        /**
         * 构建基础信息
         * @param id
         * @param name
         * @param age
         * @return
         */
        public PersonBuilder basicInfo(int id, String name, int age) {
            p.id = id;
            p.name = name;
            p.age = age;
            return this;
        }

        /**
         * 构建weight
         * @param weight
         * @return
         */
        public PersonBuilder weight(double weight) {
            p.weight = weight;
            return this;
        }

        public PersonBuilder score(int score) {
            p.score = score;
            return this;
        }

        public PersonBuilder loc(String street, String roomNo) {
            p.loc = new Location(street, roomNo);
            return this;
        }

        public Person build() {
            return p;
        }
    }
}

/**
 * 住址
 */
class Location {
    String street;
    String roomNo;

    public Location(String street, String roomNo) {
        this.street = street;
        this.roomNo = roomNo;
    }
}
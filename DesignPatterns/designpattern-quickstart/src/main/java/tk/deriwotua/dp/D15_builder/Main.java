package tk.deriwotua.dp.D15_builder;

/**
 * 建造模式(对象的创建模式)
 *  分离复杂对象的构建和表示
 *
 * 在什么情况下使用建造模式
 * 　　1. 需要生成的产品对象有复杂的内部结构，每一个内部成分本身可以是对象，也可以仅仅是一个对象（即产品对象）的一个组成部分。
 * 　　2. 需要生成的产品对象的属性相互依赖。建造模式可以强制实行一种分步骤进行的建造过程，因此，如果产品对象的一个属性必须在另一个属性被赋值之后才可以被赋值，使用建造模式是一个很好的设计思想。
 * 　　3. 在对象创建过程中会使用到系统中的其他一些对象，这些对象在产品对象的创建过程中不易得到。
 */
public class Main {
    public static void main(String[] args) {
        /**
         * 建造某种指定类型的坦克大战地形
         */
        TerrainBuilder builder = new ComplexTerrainBuilder();
        // 链式编程
        Terrain t = builder.buildFort().buildMine().buildWall().build();
        //new Terrain(Wall w, Fort f, Mine m)
        //Effective Java 构建复杂对象时要使用Builder模式

        /**
         * Person有着非常多的属性使用setXxx()创建会繁琐且不优雅
         *  通过静态内部类构建Person
         */
        Person p = new Person.PersonBuilder()
                .basicInfo(1, "zhangsan", 18)
                //.score(20)
                .weight(200)
                //.loc("bj", "23")
                .build();
    }
}

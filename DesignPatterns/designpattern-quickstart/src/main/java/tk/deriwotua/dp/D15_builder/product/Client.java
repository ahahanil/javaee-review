package tk.deriwotua.dp.D15_builder.product;

/**
 * 客户端
 */
public class Client {
    public static void main(String[]args){
        /**
         * 指定建造器通过导演者角色创建产品对象
         */
        Builder builder = new ConcreteBuilder();
        Director director = new Director(builder);
        // 产品构造方法，负责调用各个零件建造方法
        director.construct();
        // 获取建造的产品
        Product product = builder.retrieveResult();
        System.out.println(product.getPart1());
        System.out.println(product.getPart2());
    }
}
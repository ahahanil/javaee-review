package tk.deriwotua.dp.D07_decorator.coffee;

/**
 * 客户端使用装饰器模式，是不是与java中的io使用方式很像？！
 *  装饰器模式无论怎么装饰都不改变原始属性
 *      再怎么加牛奶加糖咖啡还是咖啡
 */
public class DecoratorDemo {

    static void print(Coffee c) {
        System.out.println("花费了: " + c.getCost());
        System.out.println("配料: " + c.getIngredients());
        System.out.println("============");
    }

    public static void main(String[] args) {
        //原味咖啡
        Coffee c = new SimpleCoffee();
        print(c);

        //增加牛奶的咖啡
        Coffee milkCoffee = new WithMilk(new SimpleCoffee());
        print(milkCoffee);

        //再加一点糖
        Coffee milkWithSugarCoffee = new WithSugar(milkCoffee);
        print(milkWithSugarCoffee);
    }
}
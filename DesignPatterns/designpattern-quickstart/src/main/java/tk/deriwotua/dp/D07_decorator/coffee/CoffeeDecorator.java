package tk.deriwotua.dp.D07_decorator.coffee;

/**
 * 咖啡的"装饰器"，可以给咖啡添加各种"配料"
 * 实现Coffee接口，定义一个Coffee对象的引用，在构造器中进行初始化。
 * 并且将getCost()和getIntegredients()方法转发给被装饰对象。
 */
abstract class CoffeeDecorator implements Coffee {
    protected final Coffee decoratedCoffee;

    /**
     * 在构造方法中，初始化咖啡对象的引用
     */
    public CoffeeDecorator(Coffee coffee) {
        decoratedCoffee = coffee;
    }

    /**
     * 装饰器父类中直接转发"请求"至引用对象
     */
    public double getCost() {
        return decoratedCoffee.getCost();
    }

    public String getIngredients() {
        return decoratedCoffee.getIngredients();
    }
}
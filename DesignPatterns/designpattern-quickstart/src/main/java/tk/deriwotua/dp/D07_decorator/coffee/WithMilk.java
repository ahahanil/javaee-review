package tk.deriwotua.dp.D07_decorator.coffee;

/**
 * 具体的装饰器类，负责往咖啡中“添加”牛奶
 * 注意看getCost()方法和getIngredients()方法，可以在转发请求之前或者之后，增加功能。
 * 相比较代理模式，这里的结构就有所不同，通常代理模式根据运行时的条件来判断是否转发请求。
 */
class WithMilk extends CoffeeDecorator {

    public WithMilk(Coffee coffee) {
        super(coffee);
    }

    @Override
    public double getCost() {
        double additionalCost = 0.5;
        return super.getCost() + additionalCost;
    }

    @Override
    public String getIngredients() {
        String additionalIngredient = "milk";
        return super.getIngredients() + ", " + additionalIngredient;
    }
}
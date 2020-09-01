package tk.deriwotua.dp.D07_decorator.coffee;

/**
 * 假设去买咖啡，首先服务员给冲了一杯原味咖啡，我希望服务员给加些牛奶和白糖混合入原味咖啡中。使用装饰器模式就可以解决这个问题。
 * <p>
 * 咖啡接口，定义了获取花费和配料的接口。
 */
interface Coffee {
    /**
     * 获取价格
     */
    double getCost();

    /**
     * 获取配料
     */
    String getIngredients();
}
package tk.deriwotua.dp.D07_decorator.coffee;

/**
 * 原味咖啡，实现Coffee接口，花费1元，配料中，只有咖啡
 */
class SimpleCoffee implements Coffee {

    @Override
    public double getCost() {
        return 1;
    }

    @Override
    public String getIngredients() {
        return "Coffee";
    }
}
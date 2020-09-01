package tk.deriwotua.dp.D11_flyweight.simpleflyweight;

/**
 * 具体享元
 */
public class ConcreteFlyweight implements Flyweight {
    /**
     * 内蕴状态(值应当在享元对象被创建时赋予)
     * 所有的内蕴状态在对象创建之后，就不会再改变了。
     */
    private Character intrinsicState = null;

    /**
     * 构造函数，内蕴状态作为参数传入
     *
     * @param state
     */
    public ConcreteFlyweight(Character state) {
        this.intrinsicState = state;
    }

    /**
     * 享元对象有外蕴状态的话，所有的外部状态都必须存储在客户端，在使用享元对象时，再由客户端传入享元对象
     * <p>
     * 这里外蕴状态作为参数传入方法中，改变方法的行为，但是并不改变对象的内蕴状态。
     *
     * @param state 外蕴状态
     */
    @Override
    public void operation(String state) {
        /**
         * 外蕴状态并不改变对象的内蕴状态
         */
        System.out.println("Intrinsic State = " + this.intrinsicState);
        /**
         * 外蕴状态作为参数影响方法的行为
         */
        System.out.println("Extrinsic State = " + state);
    }

}
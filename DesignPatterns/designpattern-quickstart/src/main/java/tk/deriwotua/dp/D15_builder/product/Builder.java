package tk.deriwotua.dp.D15_builder.product;

/**
 * 抽象建造者
 * 最终产品Product只有两个零件
 * 产品所包含的零件数目与建造方法的数目相符即有多少零件，就有多少相应的建造方法。
 */
public interface Builder {
    /**
     * 建造Part1方法
     */
    public void buildPart1();

    /**
     * 建造Part2方法
     */
    public void buildPart2();

    /**
     * 返还结构方法
     *
     * @return
     */
    public Product retrieveResult();
}
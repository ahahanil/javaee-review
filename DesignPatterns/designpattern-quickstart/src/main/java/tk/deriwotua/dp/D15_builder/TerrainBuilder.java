package tk.deriwotua.dp.D15_builder;

/**
 * 抽象坦克大战地形建造者
 */
public interface TerrainBuilder {
    /**
     * 建造墙
     *
     * @return 方便链式编程
     */
    TerrainBuilder buildWall();

    /**
     * 建造暗堡
     *
     * @return 方便链式编程
     */
    TerrainBuilder buildFort();

    /**
     * 建造地雷
     *
     * @return 方便链式编程
     */
    TerrainBuilder buildMine();

    /**
     * 构建地形
     *
     * @return
     */
    Terrain build();
}

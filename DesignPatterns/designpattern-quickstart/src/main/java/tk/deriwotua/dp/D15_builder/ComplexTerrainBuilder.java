package tk.deriwotua.dp.D15_builder;

/**
 * 某种具体坦克大战地形建造器
 */
public class ComplexTerrainBuilder implements TerrainBuilder {
    /**
     * 建造某种地形初始化状态
     */
    Terrain terrain = new Terrain();

    /**
     * 建造墙
     *
     * @return
     */
    @Override
    public TerrainBuilder buildWall() {
        terrain.w = new Wall(10, 10, 50, 50);
        return this;
    }

    /**
     * 建造暗堡
     *
     * @return
     */
    @Override
    public TerrainBuilder buildFort() {
        terrain.f = new Fort(10, 10, 50, 50);
        return this;
    }

    /**
     * 建造地雷
     *
     * @return
     */
    @Override
    public TerrainBuilder buildMine() {
        terrain.m = new Mine(10, 10, 50, 50);
        return this;
    }

    /**
     * 返回建造的地形
     *
     * @return
     */
    @Override
    public Terrain build() {
        return terrain;
    }
}

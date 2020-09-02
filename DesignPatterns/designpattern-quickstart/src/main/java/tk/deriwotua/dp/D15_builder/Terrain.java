package tk.deriwotua.dp.D15_builder;

/**
 * 坦克大战地形
 *
 */
public class Terrain {
    /**
     * 墙
     */
    Wall w;
    /**
     * 暗堡
     */
    Fort f;
    /**
     * 地雷
     */
    Mine m;
}

/**
 * 地形上墙
 */
class Wall {
    int x, y, w, h;

    public Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}

/**
 * 地形上的暗堡
 */
class Fort {
    int x, y, w, h;

    public Fort(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

}

/**
 * 地形上地雷
 */
class Mine {
    int x, y, w, h;

    public Mine(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}

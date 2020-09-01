package tk.deriwotua.dp.D04_abstractfactory.game;

/**
 * 抽象工厂类
 *  处理具有相同（或者相似）等级结构中的多个产品族中的产品对象的创建问题
 */
public abstract class AbstractFactory {
    /**
     * 创建食物
     *
     * @return
     */
    abstract Food createFood();

    /**
     * 创建车辆
     *
     * @return
     */
    abstract Vehicle createVehicle();

    /**
     * 创建武器
     *
     * @return
     */
    abstract Weapon createWeapon();
}

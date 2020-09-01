package tk.deriwotua.dp.D04_abstractfactory.game;

/**
 * 每一个产品族都有一个具体工厂，每一个具体工厂负责创建属于这个产品族，但是分属于不同等级(类型)结构的产品。
 */
public class MagicFactory extends AbstractFactory {
    @Override
    Food createFood() {
        return new MushRoom();
    }

    @Override
    Vehicle createVehicle() {
        return new Broom();
    }

    @Override
    Weapon createWeapon() {
        return new MagicStick();
    }
}

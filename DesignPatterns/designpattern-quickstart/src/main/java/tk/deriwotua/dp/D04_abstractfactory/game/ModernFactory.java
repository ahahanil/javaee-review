package tk.deriwotua.dp.D04_abstractfactory.game;

/**
 * 每一个产品族都有一个具体工厂，每一个具体工厂负责创建属于这个产品族，但是分属于不同等级(类型)结构的产品。
 */
public class ModernFactory extends AbstractFactory {
    @Override
    Food createFood() {
        return new Bread();
    }

    @Override
    Vehicle createVehicle() {
        return new Car();
    }

    @Override
    Weapon createWeapon() {
        return new AK47();
    }
}

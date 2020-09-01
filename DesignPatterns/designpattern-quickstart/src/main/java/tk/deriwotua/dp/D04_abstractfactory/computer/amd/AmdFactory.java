package tk.deriwotua.dp.D04_abstractfactory.computer.amd;

import tk.deriwotua.dp.D04_abstractfactory.computer.AbstractFactory;
import tk.deriwotua.dp.D04_abstractfactory.computer.Cpu;
import tk.deriwotua.dp.D04_abstractfactory.computer.Mainboard;

public class AmdFactory implements AbstractFactory {

    @Override
    public Cpu createCpu() {
        return new AmdCpu(938);
    }

    @Override
    public Mainboard createMainboard() {
        return new AmdMainboard(938);
    }

}
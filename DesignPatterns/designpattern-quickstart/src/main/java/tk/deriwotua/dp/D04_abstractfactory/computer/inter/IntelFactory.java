package tk.deriwotua.dp.D04_abstractfactory.computer.inter;

import tk.deriwotua.dp.D04_abstractfactory.computer.AbstractFactory;
import tk.deriwotua.dp.D04_abstractfactory.computer.Cpu;
import tk.deriwotua.dp.D04_abstractfactory.computer.Mainboard;

public class IntelFactory implements AbstractFactory {

    @Override
    public Cpu createCpu() {
        return new IntelCpu(755);
    }

    @Override
    public Mainboard createMainboard() {
        return new IntelMainboard(755);
    }

}
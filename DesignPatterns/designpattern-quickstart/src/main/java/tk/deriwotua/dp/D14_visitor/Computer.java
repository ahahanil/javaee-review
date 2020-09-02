/**
 * 访问者模式
 *  常见于编译器语法分析使用 visitor模式
 */
package tk.deriwotua.dp.D14_visitor;

public class Computer {
    /**
     * 电脑组件
     */
    ComputerPart cpu = new CPU();
    ComputerPart memory = new Memory();
    ComputerPart board = new Board();

    /**
     * 针对不同需求人群根据其需求组件组装
     * @param v
     */
    public void acccept(Visitor v) {
        /**
         * 访问者模式有其自己定义其实现
         */
        this.cpu.accept(v);
        this.memory.accept(v);
        this.board.accept(v);
    }

    public static void main(String[] args) {
        PersonelVisitor p = new PersonelVisitor();
        new Computer().acccept(p);
        System.out.println(p.totalPrice);
    }
}

/**
 * 电脑组件
 */
abstract class ComputerPart {
    abstract void accept(Visitor v);
    //some other operations eg:getName getBrand
    abstract double getPrice();
}

class CPU extends ComputerPart {

    @Override
    void accept(Visitor v) {
        v.visitCpu(this);
    }

    @Override
    double getPrice() {
        return 500;
    }
}

class Memory extends ComputerPart {

    @Override
    void accept(Visitor v) {
        v.visitMemory(this);
    }

    @Override
    double getPrice() {
        return 300;
    }
}

class Board extends ComputerPart {

    @Override
    void accept(Visitor v) {
        v.visitBoard(this);
    }

    @Override
    double getPrice() {
        return 200;
    }
}

/**
 * 电脑结构是固定的
 *  只不过组件可以选购各式各样的
 */
interface Visitor {
    void visitCpu(CPU cpu);
    void visitMemory(Memory memory);
    void visitBoard(Board board);
}

/**
 * 每个人有不同需求
 *  即客户自定义自己所需的组件
 */
class PersonelVisitor implements Visitor {
    double totalPrice = 0.0;

    @Override
    public void visitCpu(CPU cpu) {
        totalPrice += cpu.getPrice()*0.9;
    }

    @Override
    public void visitMemory(Memory memory) {
        totalPrice += memory.getPrice()*0.85;
    }

    @Override
    public void visitBoard(Board board) {
        totalPrice += board.getPrice()*0.95;
    }
}

/**
 * 客户
 */
class CorpVisitor implements Visitor {
    double totalPrice = 0.0;

    @Override
    public void visitCpu(CPU cpu) {
        totalPrice += cpu.getPrice()*0.6;
    }

    @Override
    public void visitMemory(Memory memory) {
        totalPrice += memory.getPrice()*0.75;
    }

    @Override
    public void visitBoard(Board board) {
        totalPrice += board.getPrice()*0.75;
    }
}

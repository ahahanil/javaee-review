package tk.deriwotua.dp.D18_command;

/**
 * 命令模式 封装了一些命令
 */
public abstract class Command {
    /**
     * 每个命令都有其执行过程
     */
    public abstract void doit(); //exec run
    public abstract void undo();
}

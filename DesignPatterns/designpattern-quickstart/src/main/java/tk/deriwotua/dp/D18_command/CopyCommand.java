package tk.deriwotua.dp.D18_command;

/**
 * 拷贝命令
 *  执行拷贝和撤销拷贝操作过程
 */
public class CopyCommand extends Command {
    Content c;
    public CopyCommand(Content c) {
        this.c = c;
    }

    @Override
    public void doit() {
        c.msg = c.msg + c.msg;
    }

    @Override
    public void undo() {
        c.msg = c.msg.substring(0, c.msg.length()/2);
    }
}

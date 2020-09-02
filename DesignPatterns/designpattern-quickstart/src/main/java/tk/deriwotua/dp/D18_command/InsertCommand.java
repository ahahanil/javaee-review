package tk.deriwotua.dp.D18_command;

/**
 * 插入命令
 *  命令固定描述 对Content进行了那些操作(Command接口里的方法)
 */
public class InsertCommand extends Command {
    Content c;
    String strToInsert = "http://www.mashibing.com";
    public InsertCommand(Content c) {
        this.c = c;
    }

    /**
     * 插入操作在内容后面拼接上
     */
    @Override
    public void doit() {
        c.msg = c.msg + strToInsert;
    }

    /**
     * 撤回插入操作把刚刚插入的再删除
     */
    @Override
    public void undo() {
        c.msg = c.msg.substring(0, c.msg.length()-strToInsert.length());
    }
}

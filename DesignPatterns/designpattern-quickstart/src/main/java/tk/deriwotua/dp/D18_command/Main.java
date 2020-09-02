package tk.deriwotua.dp.D18_command;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装命令模式
 */
public class Main {
    public static void main(String[] args) {
        Content c = new Content();

        Command insertCommand = new InsertCommand(c);
        insertCommand.doit();
        insertCommand.undo();

        Command copyCommand = new CopyCommand(c);
        insertCommand.doit();
        insertCommand.undo();

        Command deleteCommand = new DeleteCommand(c);
        deleteCommand.doit();
        deleteCommand.undo();

        /**
         * 执行一个系列命令需要先记录命令行
         */
        List<Command> commands = new ArrayList<>();
        commands.add(new InsertCommand(c));
        commands.add(new CopyCommand(c));
        commands.add(new DeleteCommand(c));

        for(Command comm : commands) {
            comm.doit();
        }

        System.out.println(c.msg);

        for(int i= commands.size()-1; i>=0; i--) {
            commands.get(i).undo();
        }


        System.out.println(c.msg);
    }
}



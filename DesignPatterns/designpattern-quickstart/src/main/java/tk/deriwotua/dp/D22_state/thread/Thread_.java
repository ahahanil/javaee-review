package tk.deriwotua.dp.D22_state.thread;

/**
 * 有限状态机
 *  状态迁移
 */
public class Thread_ {
    ThreadState_ state;

    void move(Action input) {
        state.move(input);
    }

    void run() {
        state.run();
    }


}

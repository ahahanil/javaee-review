package tk.deriwotua.dp.D22_state.v1;

/**
 * 状态模式
 *  状态决定行为
 *  不同状态有不同行为
 *
 * 当增加新的状态时非常不方便
 */

public class MM {
    String name;

    /**
     * 状态
     */
    private enum MMState {HAPPY, SAD}

    MMState state;

    public void smile() {
        //switch case

    }

    public void cry() {
        //switch case
    }

    public void say() {
        //switch case
    }
}

package tk.deriwotua.dp.D09_observer.v9;

import java.util.ArrayList;
import java.util.List;


public class Test {
    public static void main(String[] args) {
        Button b = new Button();
        b.addActionListener(new MyActionListener());
        b.addActionListener(new MyActionListener2());
        b.buttonPressed();
    }
}

/**
 * 具体主题
 */
class Button {

    /**
     * 用来保存注册的观察者对象
     */
    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    /**
     * 通知所有注册的观察者对象
     */
    public void buttonPressed() {
        ActionEvent e = new ActionEvent(System.currentTimeMillis(), this);
        for (int i = 0; i < actionListeners.size(); i++) {
            ActionListener l = actionListeners.get(i);
            l.actionPerformed(e);
        }
    }

    /**
     * 注册观察者对象
     */
    public void addActionListener(ActionListener l) {
        actionListeners.add(l);
    }
}

/**
 * 抽象观察者
 */
interface ActionListener {

    /**
     * 当某个主题事件发生
     */
    public void actionPerformed(ActionEvent e);
}

/**
 * 具体观察者
 */
class MyActionListener implements ActionListener {

    /**
     * 当某个主题事件发生
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println("button pressed!");
    }

}

/**
 * 具体观察者
 */
class MyActionListener2 implements ActionListener {

    /**
     * 当某个主题事件发生
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println("button pressed 2!");
    }

}

/**
 * 事件
 */
class ActionEvent {

    long when;
    /**
     * 事件发生后定位事件源
     */
    Object source;

    public ActionEvent(long when, Object source) {
        super();
        this.when = when;
        this.source = source;
    }

    public long getWhen() {
        return when;
    }

    public Object getSource() {
        return source;
    }

}
package tk.deriwotua.dp.D09_observer.v7;

import java.util.ArrayList;
import java.util.List;

/**
 * 有很多时候，观察者需要根据事件的具体情况来进行处理
 * 大多数时候，处理事件的时候，需要事件源对象
 */

/**
 * 具体主题
 */
class Child {
    private boolean cry = false;

    /**
     * 用来保存注册的观察者对象
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     * 注册观察者对象
     */
    {
        observers.add(new Dad());
        observers.add(new Mum());
        observers.add(new Dog());
    }


    public boolean isCry() {
        return cry;
    }

    /**
     * 通知所有注册的观察者对象
     */
    public void wakeUp() {
        cry = true;

        /**
         * 创建具体事件
         */
        wakeUpEvent event = new wakeUpEvent(System.currentTimeMillis(), "bed", this);

        for(Observer o : observers) {
            o.actionOnWakeUp(event);
        }
    }
}

/**
 * 事件类 fire Event
 */
class wakeUpEvent{
    long timestamp;
    String loc;
    /**
     * 事件发生后需要定位事件源头
     */
    Child source;

    /**
     * 某个事件
     * @param timestamp
     * @param loc
     */
    public wakeUpEvent(long timestamp, String loc, Child source) {
        this.timestamp = timestamp;
        this.loc = loc;
        this.source = source;
    }
}

/**
 * 抽象观察者
 */
interface Observer {
    void actionOnWakeUp(wakeUpEvent event);
}

/**
 * 具体观察者
 */
class Dad implements Observer {
    public void feed() {
        System.out.println("dad feeding...");
    }

    /**
     * 当某个主题事件发生
     */
    @Override
    public void actionOnWakeUp(wakeUpEvent event) {
        feed();
    }
}

/**
 * 具体观察者
 */
class Mum implements Observer {
    public void hug() {
        System.out.println("mum hugging...");
    }

    /**
     * 当某个主题事件发生
     */
    @Override
    public void actionOnWakeUp(wakeUpEvent event) {
        hug();
    }
}

/**
 * 具体观察者
 */
class Dog implements Observer {
    public void wang() {
        System.out.println("dog wang...");
    }

    /**
     * 当某个主题事件发生
     */
    @Override
    public void actionOnWakeUp(wakeUpEvent event) {
        wang();
    }
}

public class Main {
    public static void main(String[] args) {
        Child c = new Child();
        //do sth
        c.wakeUp();
    }
}

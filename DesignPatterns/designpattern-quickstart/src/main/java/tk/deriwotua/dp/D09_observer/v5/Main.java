package tk.deriwotua.dp.D09_observer.v5;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式是对象的行为模式，又叫发布-订阅(Publish/Subscribe)模式、模型-视图(Model/View)模式、源-监听器(Source/Listener)模式或从属者(Dependents)模式。
 * 观察者模式定义了一种一对多的依赖关系，让多个观察者对象同时监听某一个主题对象。
 * 这个主题对象在状态上发生变化时，会通知所有观察者对象，使它们能够自动更新自己。
 *
 * 观察者模式中，又分为推模型和拉模型两种方式。
 *  推模型
 *      主题对象向观察者推送主题的详细信息，不管观察者是否需要，推送的信息通常是主题对象的全部或部分数据。
 *
 *      推模型是假定主题对象知道观察者需要的数据
 *  拉模型
 *      主题对象在通知观察者的时候，只传递少量信息。如果观察者需要更具体的信息，由观察者主动到主题对象中获取，相当于是观察者从主题对象中拉数据。
 *      一般这种模型的实现中，会把主题对象自身通过update()方法传递给观察者，这样在观察者需要获取数据的时候，就可以通过这个引用来获取了。
 *
 *      拉模型是主题对象不知道观察者具体需要什么数据，没有办法的情况下，干脆把自身传递给观察者，让观察者自己去按需要取值。
 *
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

    /**
     * 哭了
     * @return
     */
    public boolean isCry() {
        return cry;
    }

    /**
     * 通知所有注册的观察者对象
     */
    public void wakeUp() {
        cry = true;
        for(Observer o : observers) {
            o.actionOnWakeUp();
        }
    }
}

/**
 * 抽象观察者
 */
interface Observer {
    /**
     * 观察某个主题发生
     */
    void actionOnWakeUp();
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
    public void actionOnWakeUp() {
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
    public void actionOnWakeUp() {
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
    public void actionOnWakeUp() {
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

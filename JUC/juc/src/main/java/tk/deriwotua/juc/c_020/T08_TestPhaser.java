package tk.deriwotua.juc.c_020;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * java.util.concurrent.Phaser 阶段
 * 一个大任务可以分为多个阶段完成，且每个阶段的任务可以多个线程并发执行，
 * 但是必须上一个阶段的任务都完成了才可以执行下一个阶段的任务
 * 使用CyclicBarrier或者CountryDownLatch也可以实现，但是要复杂的多
 *
 * Phaser计数器叫做parties 可通过Phaser的构造函数或者register()方法来注册
 * 通过调用register()方法，可以动态的控制phaser的个数。如果需要取消注册，则可以调用arriveAndDeregister()方法
 *
 * 内部通过 AtomicReference 原子引用泛型类 + CAS操作 + synchronized 实现
 */
public class T08_TestPhaser {
    static Random r = new Random();
    static MarriagePhaser phaser = new MarriagePhaser();

    static void milliSleep(int milli) {
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /**
         * 初始 party 个数
         * 可通过register()和bulkRegister(int parties)方法来动态调整注册任务的数量
         */
        phaser.bulkRegister(5);

        for (int i = 0; i < 5; i++) {
            final int nameIndex = i;
            new Thread(() -> {

                Person p = new Person("person " + nameIndex);
                p.arrive();
                // 当前线程当前阶段执行完毕(标记为arrive)，等待其它线程完成当前阶段(等待这一个Phaser周期结束)
                phaser.arriveAndAwaitAdvance();

                p.eat();
                phaser.arriveAndAwaitAdvance();

                p.leave();
                phaser.arriveAndAwaitAdvance();
            }).start();
        }

    }

    /**
     * 执行多个Phaser周期
     */
    static class MarriagePhaser extends Phaser {
        /**
         * onAdvance() 将会在最后一个arrive()调用的时候被调用
         * 类似CyclicBarrier的barrier到达机制 唤醒上一阶段阻塞线程
         * 重写实现更多执行多个Phaser周期
         * @param phase 每执行一个Phaser周期后 phase 会加一直到Integer.MAX_VALUE 在从0开始
         * @param registeredParties 当前阶段参与人数
         *      registeredParties为0的话，该Phaser将会调用isTerminated方法结束该Phaser
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            /**
             * 定义三个周期(阶段)
             */
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐了！");
                    return false;
                case 1:
                    System.out.println("所有人吃完了！");
                    return false;
                case 2:
                    System.out.println("所有人离开了！");
                    System.out.println("婚礼结束！");
                    return true;
                default:
                    return true;
            }
        }
    }

    static class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }

        public void arrive() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到达现场！\n", name);
        }

        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完!\n", name);
        }

        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开！\n", name);
        }

    }
}



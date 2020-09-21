package tk.deriwotua.juc.c_020;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * java.util.concurrent.Phaser �׶�
 * һ����������Է�Ϊ����׶���ɣ���ÿ���׶ε�������Զ���̲߳���ִ�У�
 * ���Ǳ�����һ���׶ε���������˲ſ���ִ����һ���׶ε�����
 * ʹ��CyclicBarrier����CountryDownLatchҲ����ʵ�֣�����Ҫ���ӵĶ�
 *
 * Phaser����������parties ��ͨ��Phaser�Ĺ��캯������register()������ע��
 * ͨ������register()���������Զ�̬�Ŀ���phaser�ĸ����������Ҫȡ��ע�ᣬ����Ե���arriveAndDeregister()����
 *
 * �ڲ�ͨ�� AtomicReference ԭ�����÷����� + CAS���� + synchronized ʵ��
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
         * ��ʼ party ����
         * ��ͨ��register()��bulkRegister(int parties)��������̬����ע�����������
         */
        phaser.bulkRegister(5);

        for (int i = 0; i < 5; i++) {
            final int nameIndex = i;
            new Thread(() -> {

                Person p = new Person("person " + nameIndex);
                p.arrive();
                // ��ǰ�̵߳�ǰ�׶�ִ�����(���Ϊarrive)���ȴ������߳���ɵ�ǰ�׶�(�ȴ���һ��Phaser���ڽ���)
                phaser.arriveAndAwaitAdvance();

                p.eat();
                phaser.arriveAndAwaitAdvance();

                p.leave();
                phaser.arriveAndAwaitAdvance();
            }).start();
        }

    }

    /**
     * ִ�ж��Phaser����
     */
    static class MarriagePhaser extends Phaser {
        /**
         * onAdvance() ���������һ��arrive()���õ�ʱ�򱻵���
         * ����CyclicBarrier��barrier������� ������һ�׶������߳�
         * ��дʵ�ָ���ִ�ж��Phaser����
         * @param phase ÿִ��һ��Phaser���ں� phase ���һֱ��Integer.MAX_VALUE �ڴ�0��ʼ
         * @param registeredParties ��ǰ�׶β�������
         *      registeredPartiesΪ0�Ļ�����Phaser�������isTerminated����������Phaser
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            /**
             * ������������(�׶�)
             */
            switch (phase) {
                case 0:
                    System.out.println("�����˵����ˣ�");
                    return false;
                case 1:
                    System.out.println("�����˳����ˣ�");
                    return false;
                case 2:
                    System.out.println("�������뿪�ˣ�");
                    System.out.println("���������");
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
            System.out.printf("%s �����ֳ���\n", name);
        }

        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s ����!\n", name);
        }

        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s �뿪��\n", name);
        }

    }
}



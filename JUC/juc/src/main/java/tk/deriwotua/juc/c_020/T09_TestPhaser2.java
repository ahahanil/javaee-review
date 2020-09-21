package tk.deriwotua.juc.c_020;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * ����ÿ�׶β�����
 */
public class T09_TestPhaser2 {
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

        phaser.bulkRegister(7);

        for (int i = 0; i < 5; i++) {

            new Thread(new Person("p" + i)).start();
        }

        new Thread(new Person("����")).start();
        new Thread(new Person("����")).start();

    }

    static class MarriagePhaser extends Phaser {
        /**
         * ÿ���һ�� Phaser �׶� �ᴥ��onAdvance()
         * @param phase ��ǰ�׶� Ĭ��0 ÿ���һ���׶κ��һ
         * @param registeredParties ��ǰ�׶β�������
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            /**
             * �����ĸ��׶� ��Person �ĸ�������Ӧ
             */
            switch (phase) {
                case 0:
                    System.out.println("�����˵����ˣ�" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("�����˳����ˣ�" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("�������뿪�ˣ�" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    /**
                     * �˽׶�ͨ��java.util.concurrent.Phaser#arriveAndDeregister()ȡ���˿��˲���
                     */
                    System.out.println("����������������ﱧ����" + registeredParties);
                    return true;
                default:
                    return true;
            }
        }
    }


    static class Person implements Runnable {
        String name;

        public Person(String name) {
            this.name = name;
        }

        public void arrive() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s �����ֳ���\n", name);
            // ��� arrive �ȴ����������̵߳��� �������ᴥ�� java.util.concurrent.Phaser.onAdvance()����
            phaser.arriveAndAwaitAdvance();
        }

        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s ����!\n", name);
            phaser.arriveAndAwaitAdvance();
        }

        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s �뿪��\n", name);

            phaser.arriveAndAwaitAdvance();
        }

        private void hug() {
            if (name.equals("����") || name.equals("����")) {
                milliSleep(r.nextInt(1000));
                System.out.printf("%s ������\n", name);
                phaser.arriveAndAwaitAdvance();
            } else {
                /**
                 * ȡ�����޹��̵߳�ע��
                 * parties ���һ
                 */
                phaser.arriveAndDeregister();
                //phaser.register()
            }
        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();
        }
    }
}



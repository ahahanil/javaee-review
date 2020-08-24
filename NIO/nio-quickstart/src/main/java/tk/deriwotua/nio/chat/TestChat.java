package tk.deriwotua.nio.chat;

import java.util.Scanner;

/**
 * 启动聊天程序客户端
 */
public class TestChat {
    public static void main(String[] args) throws Exception {
        ChatClient chatClient = new ChatClient();

        new Thread() {
            public void run() {
                /**
                 * 服务端何时会有消息为知 所以死循环
                 */
                while (true) {
                    try {
                        chatClient.receiveMsg();
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            /**
             * 发送数据
             */
            chatClient.sendMsg(msg);
        }

    }
}
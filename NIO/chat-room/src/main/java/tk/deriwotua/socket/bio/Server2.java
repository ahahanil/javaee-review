package tk.deriwotua.socket.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO socket编程 服务端
 */
public class Server2 {

    public static void main(String[] args) {
        final String QUIT = "quit";
        final int DEFAULT_PORT = 8888;

        /**
         * 绑定监听端口
         */
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);){
            System.out.println("启动服务器，监听端口" + DEFAULT_PORT);
            while (true) {
                // 等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("客户端[" + socket.getPort() + "]已连接");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                // 读取客户端发送的消息
                String msg = null;
                while((msg = reader.readLine()) != null) {
                    System.out.println("客户端[" + socket.getPort() + "]:" + msg);
                    // 回复客户端发送的消息
                    writer.write("服务器:" + msg + "\n");
                    // 缓冲区的数据发送出去
                    writer.flush();

                    // 查看客户端是否退出，其实这里的代码不用写，因为客户端的Socket关闭的话，那么readerLine的返回的内容是null，也就不会进入这个循环了
                    if (QUIT.equals(msg)) {
                        System.out.println("客户端[" + socket.getPort() + "]已断开");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
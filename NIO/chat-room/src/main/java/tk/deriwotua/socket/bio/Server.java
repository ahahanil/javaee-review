package tk.deriwotua.socket.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO socket编程 运服务端
 */
public class Server {

    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        /*ServerSocket serverSocket = null;
        try {
            // 绑定监听端口
            serverSocket = new ServerSocket(DEFAULT_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != serverSocket) {
                try {
                    serverSocket.close();
                    System.out.println("关闭 ServerSocket");
                } catch (Exception e) {
                }
            }
        }*/
        /**
         * JDK 7 try-with-resources 语句确保了每个资源在语句结束时关闭不需要finally手动关闭
         * Java 9 又进一步做了改进
         * 绑定监听端口
         */
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT); ) {
            System.out.println("启动服务器，监听端口" + DEFAULT_PORT);
            while (true) {
                // 阻塞等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("客户端[" + socket.getPort() + "]已连接");
                // 装饰器模式
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                // 读取客户端发送的消息，这个时候只能读一行消息(基于行分隔符)，后面改善
                String msg = reader.readLine();
                if (null != msg) {
                    System.out.println("客户端[" + socket.getPort() + "]:" + msg);
                    // 回复客户端发送的消息
                    writer.write("服务器:" + msg + "\n");
                    // 缓冲区的数据发送出去
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package tk.deriwotua.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 聊天程序客户端
 */
public class ChatClient {
    /**
     * 服务器地址
     */
    private final String HOST = "127.0.0.1";
    /**
     * 服务器端口
     */
    private int PORT = 9999;
    /**
     * 网络通道
     */
    private SocketChannel socketChannel;
    /**
     * 聊天用户名
     */
    private String userName;

    public ChatClient() throws IOException {
        //1. 得到一个网络通道
        socketChannel = SocketChannel.open();
        //2. 设置非阻塞方式
        socketChannel.configureBlocking(false);
        //3. 提供服务器端的IP地址和端口号
        InetSocketAddress address = new InetSocketAddress(HOST, PORT);
        //4. 连接服务器端
        if (!socketChannel.connect(address)) {
            while (!socketChannel.finishConnect()) {  //nio作为非阻塞式的优势
                System.out.println("Client:连接服务器端的同时，我还可以干别的一些事情");
            }
        }
        //5. 得到客户端IP地址和端口信息，作为聊天用户名使用
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("---------------Client(" + userName + ") is ready---------------");
    }

    /**
     * 向服务器端发送数据
     * @param msg
     * @throws Exception
     */
    public void sendMsg(String msg) throws Exception {
        if (msg.equalsIgnoreCase("bye")) {
            socketChannel.close();
            return;
        }
        msg = userName + "说：" + msg;
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        // 发送数据
        socketChannel.write(buffer);
    }

    /**
     * 从服务器端接收数据
     * @throws Exception
     */
    public void receiveMsg() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int size = socketChannel.read(buffer);
        if (size > 0) {
            String msg = new String(buffer.array());
            System.out.println(msg.trim());
        }
    }

}

package tk.deriwotua.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 聊天程序服务器端
 */
public class ChatServer {
    /**
     * 监听通道
     */
    private ServerSocketChannel listenerChannel;
    /**
     * 选择器对象
     */
    private Selector selector;
    /**
     * 服务器端口
     */
    private static final int PORT = 9999;

    public ChatServer() {
        try {
            // 1. 得到监听通道
            listenerChannel = ServerSocketChannel.open();
            // 2. 得到选择器监听
            selector = Selector.open();
            // 3. 绑定端口
            listenerChannel.bind(new InetSocketAddress(PORT));
            // 4. 设置为非阻塞模式
            listenerChannel.configureBlocking(false);
            // 5. 将选择器绑定到监听通道并监听accept事件
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("Chat Server is ready.......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 6. 业务逻辑
     *
     * @throws Exception
     */
    public void start() throws Exception {
        try {
            //不停监控
            while (true) {
                /**
                 * 监听是否有已建立IO通道
                 */
                if (selector.select(2000) == 0) {
                    // 0 没有客户端连接
                    System.out.println("Server:没有客户端找我， 我就干别的事情");
                    continue;
                }
                // 取出通道里事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    /**
                     * 连接请求事件
                     *  通道已准备好接受新的 Socket 连接
                     */
                    if (key.isAcceptable()) {
                        // 接受与服务端通道 Socket 建立的连接 返回新连接Socket通道
                        SocketChannel sc = listenerChannel.accept();
                        sc.configureBlocking(false);
                        // 新连接Socket通道绑定到选择器并监听read事件
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println(sc.getRemoteAddress().toString().substring(1) + "上线了...");
                    }
                    /**
                     * 读数据事件
                     */
                    if (key.isReadable()) {
                        readMsg(key);
                    }
                    //一定要把当前key删掉，防止重复处理
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取客户端发来的消息并广播出去
     * @param key  SelectionKey.OP_READ
     * @throws Exception
     */
    public void readMsg(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = channel.read(buffer);
        // 读取到数据
        if (count > 0) {
            String msg = new String(buffer.array());
            // 往控制台打印消息
            printInfo(msg);
            //发广播
            broadCast(channel, msg);
        }
    }

    /**
     * 给所有的客户端发广播
     * @param except  客户端与服务端通道连接建立的且发送了msg数据的Socket通道
     * @param msg 某客户端发送的需要广播出去数据
     * @throws Exception
     */
    public void broadCast(SocketChannel except, String msg) throws Exception {
        for (SelectionKey key : selector.keys()) {
            // 所有与选择器绑定的通道
            Channel targetChannel = key.channel();
            // 排除服务端通道 和 发送msg消息的通道
            if (targetChannel instanceof SocketChannel && targetChannel != except) {
                SocketChannel destChannel = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 广播到其它通道
                destChannel.write(buffer);
                System.out.println("服务器发送了广播...");
            }
        }
    }

    /**
     * 往控制台打印消息
     * @param str
     */
    private void printInfo(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }

    public static void main(String[] args) throws Exception {
        new ChatServer().start();
    }
}

package tk.deriwotua.nio.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 网络服务器端程序
 */
public class NIOServer {
    public static void main(String[] args) throws Exception {
        //1. 得到一个ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2. 得到一个Selector对象
        Selector selector = Selector.open();
        //3. 绑定一个端口号
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //4. 设置非阻塞方式
        serverSocketChannel.configureBlocking(false);
        /**
         * 5. 把ServerSocketChannel对象注册给Selector对象
         *  arg1: selector选择器
         *  arg2：事件
         *      SelectionKey.OP_ACCEPT 连接事件 有新的网络连接可以 `accept`
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6. 干活
        while (true) {
            //6.1 监控客户端 监控所有注册的通道 0 没有连接
            if (selector.select(2000) == 0) {  //nio非阻塞式的优势
                System.out.println("Server:没有客户端搭理我，我就干点别的事");
                continue;
            }
            //6.2 得到SelectionKey,判断通道里的事件 SelectionKey对应客户端连接
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                //客户端连接请求事件
                if (key.isAcceptable()) {
                    System.out.println("OP_ACCEPT");
                    // 接收客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    /**
                     * 通道注册给选择器监控 监听读数据操作 放入缓冲区
                     *  arg1: 选择器
                     *  arg2: 监听事件
                     *  arg1: 客户端发送的附件
                     */
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //读取客户端数据事件
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取客户端附件 这里即上面注册选择器监听读操作传入的缓冲区
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("客户端发来数据：" + new String(buffer.array()));
                }
                /**
                 * 6.3 手动从集合中移除当前key,防止重复处理
                 *  每一个SelectionKey只可能处于发送了某个事件处理后要移除掉
                 */
                keyIterator.remove();
            }
        }
    }
}

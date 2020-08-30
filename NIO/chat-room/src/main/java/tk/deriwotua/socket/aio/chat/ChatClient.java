package tk.deriwotua.socket.aio.chat;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

/**
 * 使用AIO编程模型实现多人聊天室-客户端
 */
public class ChatClient {

    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    /**
     * 客户端异步通道
     */
    private AsynchronousSocketChannel clientChannel;

    /**
     * 编码方式
     */
    private Charset charset = Charset.forName("UTF-8");

    private void start() {
        try {
            // 创建异步通道channel，并发起连接请求
            clientChannel = AsynchronousSocketChannel.open();
            /**
             * 之后通过异步客户端通道向服务端异步发起连接请求
             * 直接返回 Future 未来时间完成任务抽象结果对象
             */
            Future<Void> future = clientChannel.connect(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            // 阻塞式调用，等待客户端连接成功
            future.get();
            // Future#get()执行成功返回后建立了连接继续执行
            System.out.println("已连接到服务器");

            // 子线程处理用户输入事件
            new Thread(new UserInputHandler(this)).start();

            // 主线程不停读取服务器转发过来的其他客户端消息
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER);
            while (true) {
                Future<Integer> readResult = clientChannel.read(buffer);
                // 阻塞式读取数据
                int result = readResult.get();

                if (result <= 0) {
                    // 读取数据大小非负值除非客户端异步通道异常
                    // 关闭异常通道
                    close(clientChannel);
                    System.out.println("与服务器连接异常");
                    System.exit(1);
                } else {
                    // buffer 从写置为读模式
                    buffer.flip();
                    String message = String.valueOf(charset.decode(buffer));
                    // 读取后重置缓冲区指针
                    buffer.clear();
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(clientChannel);
        }
    }

    /**
     * 向服务器发送消息
     *
     * @param message
     * @throws Exception
     */
    public void send(String message) throws Exception {
        if (message.isEmpty()) {
            return;
        }
        ByteBuffer byteBuffer = charset.encode(message);
        Future<Integer> writeResult = clientChannel.write(byteBuffer);
        // 阻塞等待客户端往服务器发送数据完成
        writeResult.get();
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }


    /**
     * 回收资源
     *
     * @param closeable
     */
    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}

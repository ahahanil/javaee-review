package tk.deriwotua.socket.aio.echo;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * 客户端使用 Future 的方式来实现AIO
 *  与服务端使用CompletionHandler#callback()回调方式实现AIO做对比
 */
public class EchoClient {

    final String LOCALHOST = "localhost";

    final int DEFAULT_PORT = 8888;

    /**
     * 客户端异步通道
     */
    AsynchronousSocketChannel clientChannel;

    /**
     * 回收资源
     *
     * @param close
     */
    private void close(Closeable close) {
        if (null != close) {
            try {
                close.close();
                System.out.println("关闭" + close);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 创建客户端异步通道
            clientChannel = AsynchronousSocketChannel.open();
            /**
             * 之后通过异步客户端通道向服务端异步发起连接请求
             * 直接返回 Future 未来时间完成任务抽象结果对象
             */
            Future<Void> future = clientChannel.connect(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            // 阻塞式调用get方法，等待直到连接成功
            future.get();
            // Future#get()执行成功返回后建立了连接继续执行

            // 不停等待用户的输入
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // 阻塞式调用
                String input = consoleReader.readLine();
                byte[] inputBytes = input.getBytes();
                // 构建ByteBuffer
                ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
                // 客户端的Channel往服务器发送数据
                Future<Integer> writeResult = clientChannel.write(buffer);
                // 阻塞等待客户端往服务器发送数据完成
                writeResult.get();

                // 数据发送完毕后 缓冲区变换为读模式
                buffer.flip();
                Future<Integer> readResult = clientChannel.read(buffer);
                // 阻塞获取服务器返回的数据
                readResult.get();

                // 通道数据读取完毕后
                String echo = new String(buffer.array());
                System.out.println(echo);
                // 重置缓冲区指针
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(clientChannel);
        }
    }

    public static void main(String[] args) {
        EchoClient client = new EchoClient();
        client.start();
    }

}

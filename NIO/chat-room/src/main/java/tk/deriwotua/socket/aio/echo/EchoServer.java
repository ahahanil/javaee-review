package tk.deriwotua.socket.aio.echo;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务端使用CompletionHandler#callback()回调方式实现AIO
 *  与客户端使用 Future 的方式来实现AIO做对比
 *  CompletionHandler#callback() 执行在 AsynchronousChannelGroup 线程完成
 */
public class EchoServer {
    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 8888;

    /**
     * 服务端异步通道
     */
    AsynchronousServerSocketChannel serverChannel;

    /**
     * 关闭资源
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
            /**
             * 通过静态open()方法开启 服务端异步通道时底层 会自动创建默认 AsynchronousChannelGroup 类似一个线程池
             * 与 AsynchronousServerSocketChannel 绑定当然也可以指定自定义的 AsynchronousChannelGroup
             * AsynchronousChannelGroup 提供一些异步通道、可以共享系统资源
             *      比如 异步调用后需要通过 CompletionHandler#callback() 处理结果不可能在主线程执行回调
             *          都是通过向 AsynchronousServerSocketChannel 线程池申请子线程完成
             *
             */
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            System.out.println("启动服务器，监听端口：" + DEFAULT_PORT);

            /**
             * 等待并接收新的客户端的连接请求由于 AsynchronousServerSocketChannel#accept()是异步的调用，即等不到真正的结果完成，
             * 也就是说我在调用 AsynchronousServerSocketChannel#accept() 时可能完全没有客户端发送过来连接请求，
             * 即使这样，调用也会立即返回，因为是异步的调用，返回之后，要等到直到有客户端
             * 发来连接请求的时候，定义的 AcceptHandler 里边的回调函数才会被系统调用，
             * 即要保证服务的主线程还在工作，所以需要将 serverChannel.accept(null, new AcceptHandler()); 放到while循环中
             */
            // serverChannel.accept(null, new AcceptHandler());

            while (true) {
                /**
                 * attachment：附加信息，可以是任意对象类型 回调方法传参
                 * AcceptHandler：CompletionHandler的实现，用来处理accept结束时的结果
                 */
                serverChannel.accept(null, new AcceptHandler());
                // 阻塞式调用，避免while循环过于频繁占用系统资源
                System.in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(serverChannel);
        }
    }

    /**
     * AsynchronousServerSocketChannel#accept() 服务端异步接受客户端建立连接后
     * 通过CompletionHandler#callback()回调方式处理后续对结果操作
     * <p>
     * 由AsynchronousChannelGroup中的线程回调
     */
    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        /**
         * 异步调用函数成功返回时调用
         *
         * @param result     与服务端建立连接的异步的客户端通道(泛型指定结果类型)
         * @param attachment 额外的信息或数据(泛型指定附件类型)
         */
        @Override
        public void completed(AsynchronousSocketChannel result, Object attachment) {
            /**
             * 服务端未关闭是持续监听客户端来连接的请求
             *  当连接触发后回调函数里重置服务端异步通道事件
             *  和之前Zookeeper里Watcher监视器注册只单次有效类似需要再次注册
             */
            if (serverChannel.isOpen()) {
                // 底层限制了accept里边回调里调用accept的层级，保证了不会出现 stackOverflow 的错误
                serverChannel.accept(null, this);
            }

            // 处理读写操作
            AsynchronousSocketChannel clientChannel = result;
            // 客户端异步通道未关闭且处于可操作的状态
            if (null != clientChannel && clientChannel.isOpen()) {
                // 异步每步操作都不是实时返回结果
                // 这里异步从客户端通道读数据也是一样，就需要设置读完成后CompletionHandler#callback()
                ClientHandler handler = new ClientHandler(clientChannel);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                /**
                 * 读完成后回调方法传参
                 *  区分客户端异步通道回调所触发读写事件
                 *  及所读取到的数据
                 */
                Map<String, Object> attachmentInfo = new HashMap();
                attachmentInfo.put("type", "read");
                attachmentInfo.put("buffer", buffer);

                /**
                 * 读取客户端发送的消息到buffer中，并交给ClientHandler处理把消息转发回客户端
                 *  读取数据写入 buffer 缓冲区
                 *  把当前事件和读取到的数据缓冲区作为 attachmentInfo 附件参数传递到读完通道数据回调方法里
                 *  异步读取通道数据当读取完毕后回调 handler 里回调方法
                 */
                clientChannel.read(buffer, attachmentInfo, handler);
                // NIO 中 读取时 SocketChannel#read(rBuffer) > 0 确保通道数据都读取到缓冲区 AIO里是否也需要？？？
                // 还有通道里数据字节要是大于缓冲区大小呢？？？
            }
        }

        /**
         * 异步调用失败时触发
         *
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, Object attachment) {
            // 处理异步调用失败异常
        }
    }

    /**
     * AsynchronousServerSocketChannel#accept() 服务端异步接受客户端建立连接后获取到AsynchronousSocketChannel客户端异步通道
     * 而后 AsynchronousSocketChannel#read() 异步读取数据完毕后
     *      亦或 AsynchronousSocketChannel#write() 异步写取数据完毕后
     * 通过CompletionHandler#callback()回调方式处理后续对结果操作
     * <p>
     * 由AsynchronousChannelGroup中的线程回调
     */
    private class ClientHandler implements CompletionHandler<Integer, Object> {

        /**
         * 与客户端建立的异步通道
         */
        private AsynchronousSocketChannel clientChannel;

        public ClientHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        /**
         * 异步执行成功后回调函数
         * @param result    从通道里读取到的数据字节数(泛型指定结果类型)
         * @param attachment 额外的信息或数据(泛型指定附件类型)
         */
        @Override
        public void completed(Integer result, Object attachment) {
            /**
             * 从客户端通道读取到的数据再回写回去
             */
            Map<String, Object> info = (Map<String, Object>) attachment;
            // 判断是读操作还是写操作
            String type = (String) info.get("type");
            // 如果是读操作，读取到数据后，将数据写回客户端
            if ("read".equals(type)) {
                ByteBuffer buffer = (ByteBuffer) info.get("buffer");
                // 将 buffer 从写变为读模式
                buffer.flip();
                info.put("type", "write");
                // 在 NIO 中写时通过 ByteBuffer#hasRemaining() 判读保证缓冲区数据全部写入到通道AIO是否也需要？？？
                // 还有通道里数据字节要是大于缓冲区大小呢？？？
                clientChannel.write(buffer, info, this);
                // 数据写后重置缓冲区指针
                buffer.clear();
            }
            // 如果之前已经把客户端发送过来的数据又重新发回给了客户端，则继续去调用read函数，继续去监听客户端发送过来的数据
            else if ("write".equals(type)) {
                // 又去读客户端发送过来的数据
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                info.put("type", "read");
                info.put("buffer", buffer);
                clientChannel.read(buffer, info, this);
                // NIO 中 读取时 SocketChannel#read(rBuffer) > 0 确保通道数据都读取到缓冲区 AIO里是否也需要？？？
                // 还有通道里数据字节要是大于缓冲区大小呢？？？
            }
        }

        /**
         * 异步回调失败回调
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, Object attachment) {
            // 处理异步调用失败异常
        }
    }

    public static void main(String[] args) {
        EchoServer server = new EchoServer();
        // 服务端启动
        server.start();
    }

}

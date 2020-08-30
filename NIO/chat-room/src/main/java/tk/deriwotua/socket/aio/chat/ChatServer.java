package tk.deriwotua.socket.aio.chat;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用AIO编程模型实现多人聊天室-服务端
 */
public class ChatServer {

    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    /**
     * 使用自定义的资源共享异步通道组
     *  然后把 服务端异步通道加入组里
     */
    private AsynchronousChannelGroup asynchronousChannelGroup;

    /**
     * 服务端通道
     */
    private AsynchronousServerSocketChannel serverSocketChannel;

    /**
     * 在线客户端列表
     */
    private List<ClientHandler> connectedClients;

    /**
     * 编码方式
     */
    private Charset charset = Charset.forName("UTF-8");

    private int port;

    public ChatServer(int port) {
        this.port = port;
        this.connectedClients = new ArrayList<>();
    }


    public void start() {
        try {
            // 创建线程池设置线程池大小
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            /**
             * 创建自定义 AsynchronousChannelGroup 并将线程池加入到资源共享异步通道组中
             */
            asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(executorService);

            // 打开服务端异步通道,并加入自定义 asynchronousChannelGroup 资源共享异步通道组中
            serverSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup);

            // 服务端异步通道绑定本地主机和端口
            serverSocketChannel.bind(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            System.out.println("启动服务器,监听端口" + DEFAULT_PORT);

            // while循环，持续监听客户端的连接请求
            while (true) {
                // 一直调用accept函数,接收要与服务端建立连接的客户端 指定CompletionHandler来处理accept的结果
                serverSocketChannel.accept(null, new AcceptHandler());
                // 阻塞式调用,避免while循环过于频繁占用系统资源
                System.in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(serverSocketChannel);
        }
    }

    /**
     * AsynchronousServerSocketChannel#accept() 服务端异步接受客户端建立连接后
     * 通过CompletionHandler#callback()回调方式处理后续对结果操作
     *      CompletionHandler<V,A>
     *          泛型V 对应事件返回值类型 这里accept()后建立的AsynchronousSocketChannel客户端异步通道
     *          泛型A 回调方法传参 可回调方法里需要用到的任意类型
     * <p>
     * 由AsynchronousChannelGroup中的线程回调
     */
    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        /**
         * 异步调用函数成功返回时调用
         *
         * @param clientChannel 与服务端建立连接的异步的客户端通道(泛型指定结果类型)
         * @param attachment 额外的信息或数据(泛型指定附件类型)
         */
        @Override
        public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
            /**
             * 服务端未关闭是持续监听客户端来连接的请求
             *  当连接触发后回调函数里重置服务端异步通道事件
             *  和之前Zookeeper里Watcher监视器注册只单次有效类似需要再次注册
             */
            if (serverSocketChannel.isOpen()) {
                // 继续等待监听新的客户端的连接请求
                serverSocketChannel.accept(null, this);
            }

            // 处理当前已连接的客户端的数据读写
            // 客户端异步通道未关闭且处于可操作的状态
            if (clientChannel != null && clientChannel.isOpen()) {
                // 为该新连接的用户创建handler,用于读写操作
                ClientHandler clientHandler = new ClientHandler(clientChannel);

                // 数据读写需要通过buffer
                ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER);

                // 将客户端添加到在线列表
                addClient(clientHandler);

                /**
                 * 读取客户端异步通道发送的消息到byteBuffer中，并交给ClientHandler处理
                 *  读取数据写入 byteBuffer 缓冲区
                 *  把当前事件和读取到的数据缓冲区作为 byteBuffer 附件参数传递到读完通道数据回调方法里
                 *  handler 回调处理read函数的异步调用返回结果
                 */
                clientChannel.read(byteBuffer, byteBuffer, clientHandler);
            }
        }

        /**
         * 异步调用失败时触发
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, Object attachment) {
            // 处理异步调用失败异常
            System.out.println("连接失败" + exc);
        }
    }

    /**
     * 处理客户端通道读写事件ClientHandler
     * AsynchronousServerSocketChannel#accept() 服务端异步接受客户端建立连接后获取到AsynchronousSocketChannel客户端异步通道
     * 而后 AsynchronousSocketChannel#read() 异步读取数据完毕后
     *      亦或 AsynchronousSocketChannel#write() 异步写取数据完毕后
     * 通过CompletionHandler#callback()回调方式处理后续对结果操作
     *      CompletionHandler<V,A>
     *          泛型V 对应事件返回值类型 这里read()/write()缓冲区数据字节大小
     *          泛型A 回调方法传参 可回调方法里需要用到的任意类型
     * <p>
     * 由AsynchronousChannelGroup中的线程回调
     */
    private class ClientHandler implements CompletionHandler<Integer, Object> {

        /**
         * 当前处理对应的客户端通道
         */
        private AsynchronousSocketChannel clientChannel;

        private ClientHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        /**
         * 成功的异步回调
         *
         *
         * @param result     表示从read中读取了多少数据
         * @param attachment
         */
        @Override
        public void completed(Integer result, Object attachment) {
            ByteBuffer byteBuffer = (ByteBuffer) attachment;
            /**
             * 由于读写都是绑定的ClientHandler所以这里就需要区分读写
             *  从客户端异步通道里读取到的数据会通过 attachment 传递到这里
             *  而将这个数据转发给其他客户端异步通道(写操作)后触发这些这里回调其实所有步骤就已经完成了
             *      不需要再回调里对这个写操作做额外处理所以可以通过给写操作绑定 ClientHandler 不指定附件方式区分
             *      即 写操作时这里的 attachment 为 null
             */
            if (byteBuffer != null) {
                if (result <= 0) {
                    // 读取数据大小非负值除非客户端异步通道异常
                    // 在线列表移除异常客户端
                    removeClient(this);
                } else {
                    // 客户端异步通道里存在有效数据
                    // 首先将 buffer 从写变为读模式
                    byteBuffer.flip();

                    // 打印消息
                    String fwdMsg = receive(byteBuffer);
                    System.out.println(getClientName(clientChannel) + ":" + fwdMsg);

                    // 转发消息给当前的其他在线客户端
                    forwardMessage(clientChannel, fwdMsg);

                    // 重置buffer指针
                    byteBuffer.clear();

                    /**
                     * 如果客户端发送的是quit退出消息，则把客户移除监听的客户列表
                     * 否则持续从客户端异步读取数据
                     */
                    if (readyToQuit(fwdMsg)) {
                        // 将客户从在线客户列表中去除
                        removeClient(this);
                    } else {
                        // 如果不是则继续等待读取用户输入的信息
                        clientChannel.read(byteBuffer, byteBuffer, this);
                    }
                }
            }
        }

        /**
         * 异步调用失败时触发
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, Object attachment) {
            // 处理异步调用失败异常
            System.out.println("读写失败:" + exc);
        }
    }

    /**
     * 添加一个新的客户端进客户端列表(list集合)
     *
     * @param handler
     */
    private synchronized void addClient(ClientHandler handler) {
        connectedClients.add(handler);
        System.out.println(getClientName(handler.clientChannel) + "已经连接到服务器");
    }

    /**
     * 将该客户(下线)从列表中删除
     *
     * @param clientHandler
     */
    private synchronized void removeClient(ClientHandler clientHandler) {
        connectedClients.remove(clientHandler);
        System.out.println(getClientName(clientHandler.clientChannel) + "已断开连接");
        //关闭该客户对应流
        close(clientHandler.clientChannel);
    }

    /**
     * 服务器其实客户端发送的信息,并将该信息进行utf-8解码
     *
     * @param buffer
     * @return
     */
    private synchronized String receive(ByteBuffer buffer) {
        return String.valueOf(charset.decode(buffer));
    }

    /**
     * 服务端转发该客户端发送的消息到其他客户端异步通道上
     *
     * @param clientChannel
     * @param fwdMsg
     */
    private synchronized void forwardMessage(AsynchronousSocketChannel clientChannel, String fwdMsg) {
        for (ClientHandler handler : connectedClients) {
            // 该信息不用再转发到发送信息的那个客户端那
            if (!handler.clientChannel.equals(clientChannel)) {
                try {
                    // 将要转发的信息写入到缓冲区中
                    ByteBuffer buffer = charset.encode(getClientName(handler.clientChannel) + ":" + fwdMsg);
                    /**
                     * 将相应的信息写入到客户端异步通道中,然后客户端再通过客户端异步通道读取转发的内容
                     *  消息转发后触发 ClientHandler 回调方法回调方法里不需要再额外处理什么即空实现即可
                     *  但是由于 读写都公用 ClientHandler#completed()
                     *      而读操作会传递缓冲区附件
                     *      写操作这里总结空实现即可
                     *  所以ClientHandler#completed()可以通过 附件这个参数是否为 null 区分读写操作
                     */
                    handler.clientChannel.write(buffer, null, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取客户端的端口号并打印出来
     *
     * @param clientChannel
     * @return
     */
    private String getClientName(AsynchronousSocketChannel clientChannel) {
        int clientPort = -1;
        try {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) clientChannel.getRemoteAddress();
            clientPort = inetSocketAddress.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "客户端[" + clientPort + "]:";
    }

    /**
     * 判断是否退出
     *
     * @param msg
     * @return
     */
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
        ChatServer charServer = new ChatServer(8886);
        charServer.start();
    }
}

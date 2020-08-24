package tk.deriwotua.nio.file;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过NIO实现文件IO
 */
public class TestNIO {

    /**
     * 往本地文件中写数据
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        //1. 创建输出流
        FileOutputStream fos = new FileOutputStream("basic.txt");
        //2. 从流中得到一个通道 通道建立在流基础上
        FileChannel fc = fos.getChannel();
        //3. 提供一个缓冲区
        // 设置缓冲区的初始容量
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4. 往缓冲区中存入数据
        String str = "hello,nio";
        // 存储字节数据到缓冲区
        buffer.put(str.getBytes());
        /**
         * 往申请的 buffer 缓冲区写数据时从初始位置开始写写多少字节指针下移多少字节处
         * 写完指针并不会回到到初始位置 但是问题是 缓冲区数据写到通道时是从指针位置开始到缓冲区末尾数据写入通道内
         * 这样就会导致上面hello,nio是无法写入文件中的写入的都是缓冲区没有存放数据的区间空数据
         * 此时就需要通过flip()把指针重置到开始位置
         */
        //5. 翻转缓冲区
        buffer.flip();
        //6. 把缓冲区写到通道中 通道会把数据写到文件里
        fc.write(buffer);
        //7. 关闭 流时通道自动关闭
        fos.close();
    }

    /**
     * 从本地文件中读取数据
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        File file = new File("basic.txt");
        //1. 创建输入流
        FileInputStream fis = new FileInputStream(file);
        //2. 得到一个通道
        FileChannel fc = fis.getChannel();
        //3. 准备一个缓冲区
        // 设置缓冲区的初始容量
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        //4. 从通道里读取数据并存到缓冲区中
        fc.read(buffer);
        // 从缓冲区中取出数据
        System.out.println(new String(buffer.array()));
        //5. 关闭
        fis.close();
    }

    /**
     * 使用NIO实现文件复制
     *      transferFrom 和 transferTo特别适合复制大文件
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        //1. 创建两个流
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("c:\\test\\basic.txt");

        //2. 得到两个通道
        FileChannel sourceFC = fis.getChannel();
        FileChannel destFC = fos.getChannel();

        /**
         * 3. 复制
         *  arg1: 源
         *  arg2: 从头开始读
         *  arg3: 该通道文件的当前大小，以字节为单位
         */
        destFC.transferFrom(sourceFC, 0, sourceFC.size());
        //sourceFC.transferTo(destFC, 0, sourceFC.size());

        //4. 关闭
        fis.close();
        fos.close();
    }

}

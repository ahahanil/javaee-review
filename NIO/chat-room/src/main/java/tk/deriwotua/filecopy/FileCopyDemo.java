package tk.deriwotua.filecopy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝
 */
public class FileCopyDemo {

    /**
     * 不使用用任何缓冲的流进行拷贝文件
     *      一个字节一个字节拷贝
     */
    private static FileCopyRunner noBufferStreamCopy = (source, target) -> {

        try (InputStream fin = new FileInputStream(source);
             OutputStream fout = new FileOutputStream(target);){

            int result;
            while ((result = fin.read()) != -1) {
                fout.write(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 使用缓冲进行拷贝文件
     *      一次性读取缓冲区大小的数据
     */
    private static FileCopyRunner bufferStreamCopy = (source, target) -> {
        try (InputStream fin = new BufferedInputStream(new FileInputStream(source));
             OutputStream fout = new BufferedOutputStream(new FileOutputStream(target));) {

            byte[] buffer = new byte[1024];

            // 最多读取1024个字节，如最后一次，可能只剩余10个字节，result就是buffer读取的字节数
            int result;
            while ((result = fin.read(buffer)) != -1) {
                fout.write(buffer, 0, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 使用 nio 的 buffer 进行拷贝文件
     */
    private static FileCopyRunner nioBufferCopy = (source, target) -> {
        try (FileChannel fin = new FileInputStream(source).getChannel();
             FileChannel fout = new FileOutputStream(target).getChannel();) {
            /**
             * NIO提供各种各样buffer 这里使用byte buffer指定分配大小
             */
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (fin.read(buffer) != -1) {
                // 翻转读模式转换为写的模式
                buffer.flip();
                // 判断buffer中是否还有可以读的元素
                // 即缓冲区当前position指针指向和limit指针指向之间是否存在任何元素
                while (buffer.hasRemaining()) {
                    // 通过循环一次读取完
                    fout.write(buffer);
                }
                // 调整为读模式
                buffer.clear();
            }
            /**
             * 从通道里读取数据其实就是对缓冲区的写操作把数据暂存在缓冲区
             * 往通道里写数据其实就是对缓冲区的读操作把缓冲区数据读取到通道里
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 使用 nio 直接进行拷贝
     */
    private static FileCopyRunner nioTransferCopy = (source, target) -> {
        try (FileChannel fin = new FileInputStream(source).getChannel();
             FileChannel fout = new FileOutputStream(target).getChannel();) {
            // 已传输字节
            long transferred = 0L;
            // 需要传输大小
            long size = fin.size();
            while (transferred != size) {
                /**
                 * 将该通道文件的字节传输到给定的可写字节通道
                 * fin 数据从 指定位置 传输 多大数据 到 fout
                 * transferTo 并不能保证100%完整拷贝数据需要while循环
                 *  如果拷贝的大小没有达到源文件的大小就一直拷贝
                 */
                transferred += fin.transferTo(0, size, fout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 执行5次，之后求时间平均数
     */
    private static final int ROUNDS = 5;

    /**
     * 测试文件拷贝效率，计算平均值
     *
     * @param test
     * @param source
     * @param target
     */
    private static void benchMark(FileCopyRunner test, File source, File target) {
        // 总时间
        long elapsed = 0L;
        for (int i = 0; i < ROUNDS; i++) {
            long startTime = System.currentTimeMillis();
            test.copyFile(source, target);
            elapsed += System.currentTimeMillis() - startTime;
            target.delete();
        }
        System.out.println(test + ":" + elapsed / ROUNDS);
    }

    public static void main(String[] args) {

        // 100k
        File smallFile = new File("风控规则自动化测试报文4.json");
        File smallFileCopy = new File("a.json");

        System.out.println("---Copying small file---");
        benchMark(noBufferStreamCopy, smallFile, smallFileCopy);
        benchMark(bufferStreamCopy, smallFile, smallFileCopy);
        benchMark(nioBufferCopy, smallFile, smallFileCopy);
        benchMark(nioTransferCopy, smallFile, smallFileCopy);

        // 13M，发现没有缓冲的noBufferStreamCopy巨慢，运行就跳过吧
        File bigFile = new File("Java程序的151个建议.pdf");
        File bigFileCopy = new File("b.pdf");

        System.out.println("---Copying bigFile file---");
//        benchMark(noBufferStreamCopy, bigFile, bigFileCopy);
        benchMark(bufferStreamCopy, bigFile, bigFileCopy);
        benchMark(nioBufferCopy, bigFile, bigFileCopy);
        benchMark(nioTransferCopy, bigFile, bigFileCopy);

        // 47M
        File hugeFile = new File("Spring Cloud微服务实战.pdf");
        File hugeFileCopy = new File("Desktop/c.pdf");

        System.out.println("---Copying hugeFile file---");
//        benchMark(noBufferStreamCopy, hugeFile, hugeFileCopy);
        benchMark(bufferStreamCopy, hugeFile, hugeFileCopy);
        benchMark(nioBufferCopy, hugeFile, hugeFileCopy);
        benchMark(nioTransferCopy, hugeFile, hugeFileCopy);
    }

}
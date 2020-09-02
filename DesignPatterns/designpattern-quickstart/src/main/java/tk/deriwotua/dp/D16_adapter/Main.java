package tk.deriwotua.dp.D16_adapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 适配器模式
 *  把一个类的接口变换成客户端所期待的另一种接口，从而使原本因接口不匹配而无法在一起工作的两个类能够在一起工作。
 *
 *  前面装饰器模式有提到过直接文件IO流到带缓冲区的文件流就是使用装饰器模式
 *
 *  在BIO里字节流是无法直接赋值给字符流不同的类型接口，所以提供转换流把字节流转换为字符流这也是适配器模式一种应用
 *
 *  常见的带有Adapter后缀的类反而不是适配器模式不能见名就乱猜
 *      有些接口方法太多了，实现起来要实现N多方法非常不便，但实现接口又要求实现接口所有方法怎么办？常见做法就是使用XxxAdapter抽象类空实现该接口所有方法
 *      然后需要使用到其中某几个方法时继承XxxAdapter只用实现这几个关注的方法就可以的
 *      例如：java.awt.event.WindowAdapter
 * 更详细参考：https://www.cnblogs.com/java-my-life/archive/2012/04/13/2442795.html
 */
public class Main {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("c:/test.text");
        /**
         * InputStreamReader转换流 把输入流转换成Reader
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = br.readLine();
        while (line != null && !line.equals("")) {
            System.out.println(line);
        }
        br.close();
    }
}

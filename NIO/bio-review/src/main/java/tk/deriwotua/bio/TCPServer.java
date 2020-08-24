package tk.deriwotua.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO 服务器端程序
 */
public class TCPServer {
	public static void main(String[] args) throws Exception {
		//1.创建ServerSocket对象
		ServerSocket ss=new ServerSocket(9999); //端口号

		// 服务端一直接收客户端请求
		while (true) {
			//2.监听客户端
			System.out.println("来呀");
			/**
			 * 阻塞 等待接收连接请求
			 */
			Socket s = ss.accept();
			System.out.println("来呀");
			/**
			 * 3.从连接中取出输入流来接收消息
			 * 		也是阻塞 客户端不发送数据一直等待
			 */
			InputStream is = s.getInputStream(); //阻塞
			byte[] b = new byte[10];
			is.read(b);
			String clientIP = s.getInetAddress().getHostAddress();
			System.out.println(clientIP + "说:" + new String(b).trim());
			//4.从连接中取出输出流并回话
			//OutputStream os = s.getOutputStream();
			//os.write("没钱".getBytes());
			//5.关闭
			//s.close();
		}
	}
}


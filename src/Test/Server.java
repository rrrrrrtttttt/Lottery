package Test;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
	public static void start() throws IOException{
		int i = 0;
		ServerSocket s = new ServerSocket(10072);
		while(true)
		{
				Socket s1 = s.accept();
				i++;
				System.out.println("连接数： "+i);
				OutputStream os = s1.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				
				new ServerReader(s1).start();
				new ServerWriter(dos).start();

		}
	}
}

class ServerReader extends Thread {
     private Socket s1;
 
     public ServerReader(Socket s) {
         this.s1 = s;
     }
 
     public void run() {
         String info;
         try {
        	 InputStream is = s1.getInputStream();
        	 DataInputStream dis = new DataInputStream(is);
             while (true) {
                 // 如果对方，即客户端没有说话，那么就会阻塞到这里，
                 // 这里的阻塞并不会影响到其他线程
                 info = dis.readUTF();
                 // 如果状态有阻塞变为非阻塞，那么就打印接受到的信息
                 System.out.println("对方说: " + info);
                 if (info.equals("bye")) {
                     System.out.println("对方下线，程序退出!");
                     System.exit(0);
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }

class ServerWriter extends Thread {
     private DataOutputStream dos;
 
     public ServerWriter(DataOutputStream dos) {
         this.dos = dos;
     }
 
     public void run() {
         // 读取键盘输入流
         InputStreamReader isr = new InputStreamReader(System.in);
         // 封装键盘输入流
         BufferedReader br = new BufferedReader(isr);
         String info;
         try {
             while (true) {
                 info = br.readLine();
                 dos.writeUTF(info);
                 if (info.equals("bye")) {
                     System.out.println("自己下线，程序退出!");
                     System.exit(0);
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }

package test;

import java.net.Socket;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;


public class Client {
	String des="";
	String des1="";
	String ticket="";
	static String ipp="192.168.43.154";
	static String ipv="192.168.43.179";
    public static Package pp=new Package();
	public static void start(){
		
		try {
			Socket s1 = new Socket("172.20.10.2",8989);
			OutputStream os = s1.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
         	pp.log("尝试与AS成功建立连接。\r\n",true);

			new ClientReader(s1).start();
			//s1.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void start1(){
		try {
			Socket s1 = new Socket("172.20.10.3",8888);
			OutputStream os = s1.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
         	pp.log("尝试与TGS建立连接。\r\n",true);
			new ClientReader2(s1).start();
			//s1.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void start2(){
		try {
			Socket s1 = new Socket("172.20.10.8",8888);
			OutputStream os = s1.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			Client_UI ui=new Client_UI(s1);
			ui.a();
         	pp.log("UI界面初始化。\r\n",true);
         	pp.log("尝试与server建立连接。\r\n",true);
			new ClientReader3(s1,ui).start();
			//s1.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static int error(String w)
	{
		int a=0;
		if(w.equals("001"))
		{
			pp.log("用户口令发送失败！\r\n",true);
			a=1;
		}
		else if(w.equals("010"))
		{
			pp.log("客户端身份验证失败，IP地址不正确！\r\n",true);
			a=2;
		}
		else if(w.equals("011"))
		{
			pp.log("数据包超过生存周期！\r\n",true);
			a=3;
		}
		return a;
	}
}


class ClientReader extends Thread {
     private DataInputStream dis;
     private Socket s1;
     public volatile boolean exit = false;	
     Package p=new Package();
     public ClientReader(Socket s) {
         s1 = s;
     }
 
     public void run() {
    	 p.log("与AS成功建立连接。\r\n",true);
    	 new ClientWriter1(s1,"00000100000000000000").start();
         String info;
         String b="";
         try {
 			InputStream is = s1.getInputStream();
 			DataInputStream dis = new DataInputStream(is);
             while (!exit) {
                 info = dis.readUTF();
                 System.out.println("对方说: " + info);
            	 p.log("AS的返回信息"+info+"\r\n",true);
            	 if(p.error(info.substring(6, 9))==1)
            	 {new ClientWriter1(s1,"00000100000000000000").start();}
                 new ClientWriter1(s1,info).start();
                 if (info.subSequence(0, 6).equals("100010")) {
                	 p.log("AS服务请求完毕，进行数据处理。\r\n",true);
                    // exit=true;
                 }
             }
             //s1.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }
class ClientReader2 extends Thread {
    private DataInputStream dis;
    private Socket s1;
    public volatile boolean exit = false;
    Package p=new Package();
    public ClientReader2(Socket s) {
        s1 = s;
    }

    public void run() {
   	 p.log("与TGS成功建立连接。\r\n",true);
   	 new ClientWriter1(s1,"00001100000000000000").start();
        String info;
        String b="";
        try {
			InputStream is = s1.getInputStream();
			DataInputStream dis = new DataInputStream(is);
            while (!exit) {
                info = dis.readUTF();
                System.out.println("对方说: " + info);
                if(p.error(info.substring(6, 9))==2)
           	 	{new ClientWriter1(s1,"00001100000000000000").start();}
                if(p.error(info.substring(6, 9))==3)
           	 	{new ClientWriter1(s1,"00001100000000000000").start();}
           	 	p.log("TGS的返回信息"+info+"\r\n",true);
                new ClientWriter1(s1,info).start();
                if (info.subSequence(0, 6).equals("110010")) {
                  	 p.log("TGS服务结束，进行数据处理。\r\n",true);
                   // exit=true;
                }
            }
            //s1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class ClientReader3 extends Thread {
    private DataInputStream dis;
    private Socket s1;
    public static Client_UI ui;
    public volatile boolean exit = false;
    Package p=new Package();
    public ClientReader3(Socket s,Client_UI v) {
        s1 = s;
        ui = v;
    }

    public void run() {
     	 p.log("与server成功建立连接。\r\n",true);
     	 new ClientWriter1(s1,"000100000000000000000",ui).start();
        String info;
        String b="";
        Package pa=new Package();
        try {
			InputStream is = s1.getInputStream();
			DataInputStream dis = new DataInputStream(is);
            while (!exit) {
                info = dis.readUTF();
                System.out.println("对方说: " + info);
           	 	p.log("server的返回信息"+info+"\r\n",true);
           	 	if(p.error(info.substring(6, 9))==2)
       	 		{new ClientWriter1(s1,"00010000000000000000").start();}
           		if(p.error(info.substring(6, 9))==3)
       	 		{new ClientWriter1(s1,"00010000000000000000").start();}
                new ClientWriter1(s1,info,ui).start();
                if (info.subSequence(0, 6).equals("010010")) {
                 	 p.log("client初始化结束，进入运行状态。\r\n",true);
                   // exit=true;
                }
            }
            //s1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientWriter1 extends Thread {
    private Socket s1;
    private String mes;
    private Client_UI ui;
    public volatile boolean exit = false;
    Package p = new Package();
    public ClientWriter1(Socket s,String e,Client_UI v) {
        s1 = s;
        mes = e;
        ui=v;
    }
    public ClientWriter1(Socket s,String e) {
        s1 = s;
        mes = e;
    }

    public void run() {
        String info;
        String b="";
        try {
        	OutputStream os = s1.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
            while (!exit) {
            	if(mes.equals("ok"))
            	{
            		exit = true;
            	}
            	else
            	{
            		info = p.Determine(mes,ui);
                    dos.writeUTF(info);
                    exit = true;
            	}
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}















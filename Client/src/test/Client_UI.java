package test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
public class Client_UI extends JFrame{
	private Socket s;
	// 定义组件
	public static JPanel jp1, jp3;    
	// 定义组件名称
	public static JLabel jlb1, jlb2, jlb3, jlb4,jlb5,jlb9; // jp1:开奖日期；jp2:奖项名称；jp3：期数；jp4:上期中奖号码
	// 定义button
	public static JButton jb1, jb2, jb3, jb4,jb5; //jb1:买彩票；jb2:扫码；jb3：查看往期； jb4:确定; jb5退出
	// 定义文本框
	public static JTextField jtf1, jtf2, jtf3, jtf4; 
	public static JTextArea jtf5;
	static String JDBC_DRIVER = "com.mysql.cj.jdbc.Drive";
	static String DB_URL = "jdbc:mysql://localhost:3306/mima?serverTimezone=GMT&useSSL=false";
	static String USER = "root";
	static String PASS = "123456";
	public static Package p=new Package();
	//构造方法
	String number;
	public Client_UI(Socket s1) {
		s = s1;
		// 创建组件
		jp1 = new JPanel();
		
		jp3 = new JPanel();
		
		jlb1 = new JLabel("开奖日期：");
		jlb2 = new JLabel("奖项名称：");
		jlb3 = new JLabel("请输入要购买的号码：");
		jlb4 = new JLabel("期数");
		jlb9 = new JLabel("出现了一点小状况呢");
		
		ImageIcon ii=new ImageIcon("img1.png");
		jlb5=new JLabel(ii);
		jlb5.setBounds(0, 0, ii.getIconWidth(), ii.getIconHeight());
		
		jb1 = new JButton("买彩票");
		jb2 = new JButton("扫码");
		jb3 = new JButton("查看往期");
		jb4 = new JButton("确定");
		jb5 = new JButton("取消");
		
		jtf1 = new JTextField(20);
		jtf2 = new JTextField(20);
		jtf3 = new JTextField(20);
		jtf4 = new JTextField(10);
		jtf5=new JTextArea(60,40);
		
		// 设置布局管理器
		setLayout(new GridLayout(2, 1));
		
		// 加入组件
		jp1.add(jlb2);//奖项名称
		jp1.add(jtf2);
		
		jp1.add(jlb1);//开奖日期
		jp1.add(jtf1);	
		
		jp1.add(jlb3);//购买
		jp1.add(jtf3);
		
		jp1.add(jlb4);//期数
		jp1.add(jtf4);
		
		jp1.add(jlb5);
		jp1.add(jtf5);
		
		jp1.add(jlb9);
		
		jp3.add(jb1);
		jp3.add(jb2);
		jp3.add(jb3);
		jp3.add(jb4);
		jp3.add(jb5);
		
		// 加入到JFrame
		add(jp1);
		add(jp3);
		jlb9.setVisible(false);
		// 设置框体
		setTitle("用户主界面：");
		setSize(400, 400);
		setLocation(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		jb1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				b();//跳转购买界面
			}
		});
    	jb2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				d();
	    		p.log("点击扫码"+"\r]n",true);
				CameraPic pic=new CameraPic();
				try {
					String a = pic.PicQR();
					p.log("扫码结果为"+a+"\r\n",true);
					String a2=p.getstr(a, "*", "*");
					String a3=p.getstr(a, "|", "|");
					int a4=a2.length()+a3.length()+4;
					String a1=a.substring(0, 8);
					String nu="";
					String b22="select number from cp where name='"+a2+"' and period='"+a3+"'";
					try {
						nu=p.read1(b22);
						p.log("读取"+a2+"第"+a3+"期的中奖号码："+nu+"\r\n",true);
						if(a1.equals(nu))
						{e(1);}
						else if(nu.equals(""))
						{e(3);}
						else if(!a1.equals(nu))
						{e(2);}
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	jb4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
	    		p.log("点击确定购买"+"\r]n",true);
				number=jtf3.getText()+"*"+jtf2.getText()+"*|"+jtf4.getText()+"|";
				new ClientWriter1(s,"0001100000000000"+number).start();
				QRCode pp=new QRCode();
				pp.str2QR(number);
				f();
				setSize(400, 800);
			}
		});
    	jb3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				c();
	    		p.log("点击查看往期"+"\r]n",true);
				
			}
		});
    	jb5.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setSize(400, 400);
	    		p.log("已点击返回或取消"+"\r\n",true);
	    		File f=new File("img.png");
	    		delete(f);
				a();
			}
		});
	}
	
	public static void f()//二维码
	{
		jlb3.setVisible(false);//购买
		jtf3.setVisible(false);
		jtf5.setVisible(false);//history
		jb4.setVisible(false);//确认
		jb5.setVisible(true);//取消
		
		jlb1.setVisible(false);
		jlb2.setVisible(false);
		jlb4.setVisible(false);
		jlb5.setVisible(true);//图片
		jlb9.setVisible(false);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf4.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jb3.setVisible(false);
		
		jb5.setText("返回");
		
		Icon icon;
		try {
			icon = new ImageIcon(ImageIO.read(new File("img.png")));
			jlb5.setIcon(icon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void e(int s)
	{
		if(s==1)
		{
			jtf3.setText("恭喜您,您中奖了!");
		}
		else if(s==2)
		{
			jtf3.setText("很遗憾,您没有中奖!");
		}
		else if(s==3)
		{
			jtf3.setText("暂无该彩票信息！");
		}
		p.log("显示是否中奖！\r\n",true);
	}
	public static void d()//扫码
	{
		jlb3.setVisible(false);//购买
		jtf3.setVisible(true);
		jtf5.setVisible(false);//history
		jb4.setVisible(false);//确认
		jb5.setVisible(true);//取消
		
		jlb1.setVisible(false);
		jlb2.setVisible(false);
		jlb4.setVisible(false);
		jlb5.setVisible(false);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf4.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jb3.setVisible(false);
		jlb9.setVisible(false);
		
		jtf3.setText(null);
		jb5.setText("返回");
	}
	public static void a()//主界面
	{
		jlb3.setVisible(false);//购买
		jtf3.setVisible(false);
		jtf5.setVisible(false);//history
		jb4.setVisible(false);//确认
		jb5.setVisible(false);//取消
		
		jlb1.setVisible(true);
		jlb2.setVisible(true);
		jlb4.setVisible(true);
		jlb5.setVisible(false);
		jlb9.setVisible(false);
		jtf1.setVisible(true);
		jtf2.setVisible(true);
		jtf4.setVisible(true);
		jb1.setVisible(true);
		jb2.setVisible(true);
		jb3.setVisible(true);
	}
	public static void b()//购买界面
	{
		jlb3.setVisible(true);//购买
		jtf3.setVisible(true);
		jtf5.setVisible(false);//history
		jb4.setVisible(true);//确认
		jb5.setVisible(true);//取消
		
		jlb1.setVisible(false);
		jlb2.setVisible(false);
		jlb4.setVisible(false);
		jlb5.setVisible(false);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf4.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jb3.setVisible(false);
		jlb9.setVisible(false);
		
		jtf3.setText(null);
		jb5.setText("取消");
	}
	public static void bb()//错误
	{
		jlb3.setVisible(false);//购买
		jtf3.setVisible(false);
		jtf5.setVisible(false);//history
		jb4.setVisible(false);//确认
		jb5.setVisible(true);//
		
		jlb1.setVisible(false);
		jlb2.setVisible(false);
		jlb4.setVisible(false);
		jlb5.setVisible(false);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf4.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jb3.setVisible(false);
		jlb9.setVisible(true);
		
		jtf3.setText(null);
		jtf3.setText("");
		jb5.setText("返回");
	}
	public static void c()//查看往期
	{
		jlb3.setVisible(false);//购买
		jtf3.setVisible(false);
		jtf5.setVisible(true);//history
		jb4.setVisible(false);//确认
		jb5.setVisible(true);//取消
		
		jlb1.setVisible(false);
		jlb2.setVisible(false);
		jlb4.setVisible(false);
		jlb5.setVisible(false);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf4.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jb3.setVisible(false);
		jlb9.setVisible(false);
		
		jtf5.setText(null);
		jb5.setText("返回");
		try {
			read1("Select * from cp");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void read1(String word) throws SQLException, ClassNotFoundException
    {
    	String a="";
    	String b="";
    	String c="";
    	Connection conn = null;
        Statement stmt = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		//连接数据库
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        //实例化Statement对象
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(word);
        while(rs.next())
        {
        	jtf5.append(rs.getString(1)+"   ");
        	jtf5.append(rs.getString(2)+"   ");
        	jtf5.append(rs.getString(3));
        	jtf5.append("\r\n");
        }
        rs.close();
        stmt.close();
        conn.close();
    }
	public static void delete(File file) {
		if(!file.exists()) return;
		
		if(file.isFile() || file.list()==null) {
			file.delete();
			System.out.println("删除了"+file.getName());
		}else {
			File[] files = file.listFiles();
			for(File a:files) {
				delete(a);					
			}
			file.delete();
			System.out.println("删除了"+file.getName());
		}
		
	}
}
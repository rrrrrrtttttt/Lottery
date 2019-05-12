package Test;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.net.InetAddress;
import java.net.ServerSocket;


public class Server {
	//static 
	public static void start() throws IOException{
		int i = 0;
		Server_UI Server = new Server_UI();
		Server.ShowLotteryInfo();
		ServerSocket s2 = new ServerSocket(8888);
		String IP;
		while(true)
		{
				Socket s1 = s2.accept();
				i++;
				System.out.println("连接数： "+i);
				InetAddress addr = s1.getInetAddress();
				IP = addr.getHostAddress();
				new ServerReader(s1,IP,Server).start();
		}
	}
}

class Informing extends Thread {
	static String driver = "com.mysql.cj.jdbc.Driver";	//驱动名，默认
	static String url = "jdbc:mysql://localhost:3306/Lottery?serverTimezone=GMT&useSSL=false";	
	static String user = "root";	//mysql数据库用户名
	static String password = "123456";	//mysql数据库用户密码
	private DataOutputStream dos;
	private Socket s;
	private String IP;
	String Time = "";
	String Period = "";
	public Informing(Socket s1,String ip) {
		s = s1;
		IP =ip;
	}
	
	public void run() {
		System.out.println("Informing Thread Start!");	
		while(true)
		{
			try {
				Class.forName(driver);	//加载驱动
				Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
				//if(!conn.isClosed())
					//System.out.println("Succeed!");
				Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
				String sql = "select * from lottery_now;";	//执行的sql语句
				ResultSet rst = state.executeQuery(sql);
				while(rst.next())
				{
					Time = rst.getString("Date");     //获取彩票开奖的日期
				}
				/*String sql = "insert into lottery_now(Name,Number,Period,Date) values (\"中国福利彩票\",\"1234567\",\"100\",\"5/22 12:00\");";	//执行的sql语句
				int rst = state.executeUpdate(sql);*/
				rst.close();
				state.close();
				conn.close();	
			}catch(Exception e) {
				System.out.println("defeat!");
				System.out.println(e);
			}	
			String Month = Time.substring(0,2);
			String Day = Time.substring(3,5);
			String H = Time.substring(6,8);
			String M = Time.substring(9);
			String Now_H = "";
			String Now_M = "";
			String res = "";
			Date today=new Date();
			SimpleDateFormat f=new SimpleDateFormat("!MM!*dd* HH:mm:ss");
			String time=f.format(today);      //获取本地时间
			String Month_Now = select(time,'!','!');
			String Day_Now = select(time,'*','*');
			String H_Now = select(time,' ',':');
			String M_Now = select(time,':',':');
			//(H.equals(H_Now) == true) && (M.equals(M_Now) == true)
			if((Month.equals(Month_Now) == true) && (Day.equals(Day_Now) == true) && (H.equals(H_Now) == true) && (M.equals(M_Now) == true))
			{
				//System.out.println("Time is coming");
				try {
					Class.forName(driver);	//加载驱动
					Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
					//if(!conn.isClosed())
						//System.out.println("Succeed!");
					Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
					String sql = "select * from lottery_now;";	//执行的sql语句
					ResultSet rst = state.executeQuery(sql);
					while(rst.next())
					{
						res = res + "*" + rst.getString("Name") + "*|" + rst.getString("Period") + "|" + rst.getString("Number");					
					}
					/*String sql = "insert into lottery_now(Name,Number,Period,Date) values (\"中国福利彩票\",\"1234567\",\"100\",\"5/22 12:00\");";	//执行的sql语句
					int rst = state.executeUpdate(sql);*/
					rst.close();
					state.close();
					conn.close();	
				}catch(Exception e) {
					System.out.println("defeat!");
					System.out.println(e);
				}	
				//System.out.println("Yes: "+ res);
				String temp = "222222" + res;
				Update();	
				new ServerWriter(s,temp,IP).start();	
			}
		}
		
		
		
	}
	
	public static String select(String Message,char s,char e)
	{
		char[] temp= Message.toCharArray();
		String res = "";
		for(int i = 0;i < Message.length();i ++)
		{
			if(temp[i] == s)
			{
				i++;
				for(int j = i;temp[j] != e;j++)
				{
					res = res + temp[j];
				}
				return res;
			}
		}
		return res;
	}

	/*   更新三个数据库的信息  */
	public static void Update() {
		String Name = "";
		String Period = "";
		String Number = "";
		String Date = "";
		String Name_last = "";
		String Period_last = "";
		String Number_last = "";
		String Date_last = "";
		try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			String sql = "select * from lottery_next;";	//执行的sql语句
			ResultSet rst = state.executeQuery(sql);    //获取下一期彩票的信息
			while(rst.next())
			{
				Name = rst.getString("Name");
				Period = rst.getString("Period");
				Number = rst.getString("Number");
				Date = rst.getString("Date");
			}
			rst.close();
			String sql1 = "update lottery_now set Name = '" + Name + "',Period = '" + Period + "',Number = '" + Number + "',Date = '" + Date + "';";
		    String sql2 = "select * from lottery_now;";    //获取本期彩票的信息（已开奖),准备写入往期彩票信息数据库
		    ResultSet rst1 = state.executeQuery(sql2);
		    while(rst1.next())
			{
				Name_last = rst1.getString("Name");
				Period_last = rst1.getString("Period");
				Number_last = rst1.getString("Number");
				Date_last = rst1.getString("Date");
			}
		    rst1.close();
			String sql3 = "insert into lottery_last(Name,Period,Number) values ('" + Name_last + "','" + Period_last + "','" + Number_last + "');";
			int rst2 = state.executeUpdate(sql3);
			int rst3 = state.executeUpdate(sql1);		
			String sql4 = "delete from lottery_next where Period = '"+Period+"';";
			int rst4 = state.executeUpdate(sql4);
			/*String sql = "insert into lottery_now(Name,Number,Period,Date) values (\"中国福利彩票\",\"1234567\",\"100\",\"5/22 12:00\");";	//执行的sql语句
			int rst = state.executeUpdate(sql);*/
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}	
	}

}

class ServerReader extends Thread {
     private Socket s1;
     private String IP;
     private Server_UI UI;
     public ServerReader(Socket s,String ip,Server_UI ui) {
         s1 = s;
         IP = ip;
         UI = ui;
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
                 //if(!info.equals("over")) 
                 //{
                	 System.out.println("对方说: " + info);
                	 if(info.length()>6)
                	{
                		 String temp = info.substring(0,6);
                		 if(temp.equals("000101")) new Informing(s1,IP).start();
                	}
                     new ServerWriter(s1,info,IP,UI).start();
                 //}
                 /*if (info.equals("hello")) {
                	 try {
                		 System.out.println("对方下线!");
                	 }catch(Exception e) {
                		 e.printStackTrace();
                	 }
                     
                 }*/
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }

class ServerWriter extends Thread {
     private Socket s1;
     private String mes;
     private String IP;
     private Server_UI UI;
     public volatile boolean exit = false; 
     Package p = new Package();
     public ServerWriter(Socket s,String a,String ip,Server_UI ui) {
         s1 = s;
         mes = a;
         IP = ip;
         UI = ui;
     }
     public ServerWriter(Socket s,String a,String ip) {
         s1 = s;
         mes = a;
         IP = ip;
     }
 
     public void run() {
         // 读取键盘输入流
         InputStreamReader isr = new InputStreamReader(System.in);
         // 封装键盘输入流
         BufferedReader br = new BufferedReader(isr);

         String info;
         try {
        	 OutputStream os = s1.getOutputStream();
			 DataOutputStream dos = new DataOutputStream(os);
             while (!exit) {
            	 if (mes.equals("error")) {
                     System.out.println("出错");
                     exit = true;
                 }
            	 if(mes.equals("over"))
            	 {
            		 //System.out.println("接受结束");
                     exit = true;
            	 }
            	 else
            	 {
                	 info = p.Determine(mes,IP,UI);
                	 dos.writeUTF(info);
                	 exit = true;
            	 }

             }
             
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }













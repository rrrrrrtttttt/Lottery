package Test;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.net.Socket;
import java.net.InetAddress;
import java.net.ServerSocket;


public class Package {
	static BigInteger Rsa_PK = new BigInteger("2486064836964195870861856123635009957098143672244581025286135532878130543638777607195212349455134095418854835876437691259666166597390709569731633556821473");
	static BigInteger Rsa_n = new BigInteger("6134845666846995435976860636142316724088449501012617917545653453506816832534783145847717262030828282211781261512213357429125146843017551429292368809260761");
	static String Des_K;
	static String driver = "com.mysql.cj.jdbc.Driver";	//驱动名，默认
	static String url = "jdbc:mysql://localhost:3306/Lottery?serverTimezone=GMT&useSSL=false";	
	static String user = "root";	//mysql数据库用户名
	static String password = "123456";	//mysql数据库用户密码
	public static String Determine(String message,String ip,Server_UI UI) 
	{
		String res = "ok";
		String head = message.substring(0, 6);
		String IP = ip;
		//System.out.println(head);
		if(head.equals("000000")) res = SendCertificate();
		if(head.equals("000001")) res = Qualify(message);
		if(head.equals("000010")) res = ASBills(message,IP);
		if(head.equals("000011")) res = TGSBills(message);
		if(head.equals("000100")) res = ServerCertificate(message);
		if(head.equals("000101")) res = SendLotteryInfo(IP);
		if(head.equals("000110")) res = SendVoucher(message,IP);
		if(head.equals("222222")) res = Lottery(message,UI);
		return res;
	}
	
	public static String SendCertificate()
	{
		String res;
		res = "1000000000000000S";
		return res;
	}
	
	/*  AS解密得到用户口令  */
	public static String Qualify(String Message)
	{				
		boolean update = false;
		String res;
		res = "100001000000000011111111";
		String K = Message.substring(16);
		//System.out.println(K);
		RsaDecrypt R_D = new RsaDecrypt();
		String x = R_D.decrypt(K, Rsa_n, Rsa_PK);
		String ID = x.substring(0,4);
		Des_K = x.substring(4);
		//System.out.println(Des_K);
		//int id = Integer.valueOf(ID).intValue();
		//int k = Integer.valueOf(Des_K).intValue();
		try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			String search = "select k from ASkey where id ='"+ID+"';";	//执行的sql语句
			ResultSet rs = state.executeQuery(search);
			while(rs.next())
			{
				update = true;
			}
			if(!update)
			{
				String sql = "insert into ASkey(id,k) values ('"+ID+"','"+Des_K+"');";	//执行的sql语句
				int rst = state.executeUpdate(sql);
				//System.out.println("Insert!");
			}
			if(update)
			{
				state.executeUpdate("update ASkey set k="+Des_K+" where id='"+ID+"';"); 
				//System.out.println("Update!");
			}
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}
		String log = "  AS得到客户的用户口令！"+"\r\n";
		Log(log,true);
		return res;
	}
	
	/*  AS发送服务票据  */
	public static String ASBills(String Message,String IP) 
	{
		String res = "1000100000000000";
		String ID = Message.substring(16,20);
		String D_K;
		String Time = Message.substring(24);
		//System.out.println("wdnmd :  " + Time);
		IP = "{"+IP+"}";
		//int id = Integer.valueOf(ID).intValue();
		try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			String sql = "select k from ASkey where id ='"+ID+"';";	//执行的sql语句
			ResultSet rst = state.executeQuery(sql);
			while(rst.next())
			{
				//System.out.println("here");
				D_K = rst.getString("k");
				//System.out.println(D_K);
				DesEncrypt E = new DesEncrypt();
				DesDecrypt D = new DesDecrypt();
				String temp = "7654321"+ID+IP+Time+"(255)";
				String TICKETtgs = E.Encrypt(temp,"1111111");
				String temps = "7654321"+ID+Time+"(255)"+TICKETtgs;
				res = res + E.Encrypt(temps, D_K);
			}
			rst.close();
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}
		String log = "  由AS向客户：" + IP + "发送票据授权票据！"+"\r\n";
		Log(log,true);
		return res;
	}
	
	/*  TGS发送服务票据  */
	public static String TGSBills(String Message)
	{
		boolean update = false;
		String res = "1100100000000000";
		String IDv = Message.substring(16,20);
		Calendar calendar = Calendar.getInstance();
		int H = calendar.get(Calendar.HOUR);
		int M = calendar.get(Calendar.MINUTE);
		int S = calendar.get(Calendar.SECOND);
		Date date = new Date();
		SimpleDateFormat SDT = new SimpleDateFormat("HH:mm:ss");
		String time = SDT.format(date);
		String Send_time = "["+time+"]";
		DesEncrypt E = new DesEncrypt();
		DesDecrypt D = new DesDecrypt();
		String Authentication = select(Message,'$','$');
		int len = Authentication.length();
		String TICKETtgs = Message.substring(22+len);
		//String TICKETtgs = select(Message,'$','$');
		System.out.println("收到的AS封装的DES_TICKETtgs: "+TICKETtgs);
		String ticket = D.Decrypt(TICKETtgs, "1111111");
		System.out.println("解密AS封装的TICKETtgs: "+ticket);
		String IDc = ticket.substring(7,11);
		String IP = select(ticket,'{','}');
		System.out.println("IP: "+IP);
		String key = ticket.substring(0,7);
		/*try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			if(!conn.isClosed())
				System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			String search = "select k from server where id ='"+IDc+"';";	//执行的sql语句
			ResultSet rs = state.executeQuery(search);
			while(rs.next())
			{
				update = true;
			}
			if(!update)
			{
				String sql = "insert into server(id,k) values ("+IDc+","+key+");";	//执行的sql语句
				int rst = state.executeUpdate(sql);
				//System.out.println("Insert!");
			}
			if(update)
			{
				state.executeUpdate("update server set k="+key+" where id="+IDc+";"); 
				//System.out.println("Update!");
			}
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}*/
		//System.out.println("Key: "+key);
		if(!CheckTime(ticket))
		{
			res = "1111110110000000";
			return res;
		}
		else if(!CheckIP(Message,ticket))
		{
			res = "1111110100000000";
			return res;
		}
		else
		{
			String temp = "9876543"+IDc+"{"+IP+"}"+IDv+Send_time+"(255)";
			String TICKETv = E.Encrypt(temp, "2222222");
			String temps = "9876543"+ IDv+Send_time+TICKETv;
			String T = E.Encrypt(temps, key);
			res = res + T;
		}
		String log = "  由TGS向客户：" + IP + " 发送服务许可票据！"+"\r\n";
		Log(log,true);
		return res;
	}
	
	/*  与客户端互相认证  */
	public static String ServerCertificate(String Message)
	{
		boolean update = false;
		String res = "0100100000000000";
		Date date = new Date();
		SimpleDateFormat SDT = new SimpleDateFormat("HH:mm:ss");
		String time = SDT.format(date);
		String Time = "["+time+"]";
		DesEncrypt E = new DesEncrypt();
		DesDecrypt D = new DesDecrypt();
		String Authentication = select(Message,'$','$');
		String IDc = Authentication.substring(0,4);
		int len = Authentication.length();
		String TICKETtgs = Message.substring(18+len);
		//String TICKETtgs = select(Message,'$','$');
		System.out.println("收到的TGS封装的DES_TICKETtgs: "+TICKETtgs);
		String ticket = D.Decrypt(TICKETtgs, "2222222");
		System.out.println("解密的TGS封装的TICKETtgs: "+ticket);
		//String IDc = ticket.substring(7,11);
		String IP = select(ticket,'{','}');
		//System.out.println("IP: "+IP);
		String key = ticket.substring(0,7);
		//System.out.println("Key: "+key);
		/*if(!CheckTime(ticket)&&CheckIP(Message,ticket))
		{
			res = "1111110110000000";
			//return res;
		}
		else if(CheckTime(ticket)&&!CheckIP(Message,ticket))
		{
			res = "1111110100000000";
			//return res;
		}
		else
		{*/
			String temp = E.Encrypt(Time, key);
			res = res + temp;
			try {
				Class.forName(driver);	//加载驱动
				Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
				//if(!conn.isClosed())
					//System.out.println("Succeed!");
				Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
				String search = "select k from server_key where IP ='"+IP+"';";	//执行的sql语句
				ResultSet rs = state.executeQuery(search);
				while(rs.next())
				{
					update = true;
				}
				if(!update)
				{
					String sql = "insert into server_key(IP,k) values ('"+IP+"','"+key+"');";	//执行的sql语句
					int rst = state.executeUpdate(sql);
					//System.out.println("Insert!");
				}
				if(update)
				{
					state.executeUpdate("update server_key set k="+key+" where IP='"+IP+"';"); 
					//System.out.println("Update!");
				}
				state.close();
				conn.close();	
			}catch(Exception e) {
				System.out.println("defeat!");
				System.out.println(e);
			}
		//}
		System.out.println("Server发给客户端消息： "+res);
		return res;
	}
	
	/*  发送本期彩票信息  */
	public static String SendLotteryInfo(String ip)
	{
		String res = "0100110000000000";
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
				res = res + "*"+ rst.getString("Name") + "*|" + rst.getString("Period") + "|<" + rst.getString("Date") + ">" ;
			}
			/*String sql = "insert into lottery_now(Name) values (\"中国福利彩票\");";	//执行的sql语句
			int rst = state.executeUpdate(sql);*/
			rst.close();
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}
		String log = ip + "  请求本期彩票信息！"+"\r\n";
		Log(log,true);
		return res;
	}
	
	/*  发送购买凭证  */
	public static String SendVoucher(String Message,String IP)
	{
		String res = "0101000000000000";
		String key = "";
		String Name = "";
		String Number;
		String Period = "";
		String ip_recv;
		String tem = "";
		String hash;
		DesDecrypt D_D = new DesDecrypt();
		DesEncrypt D_E = new DesEncrypt();
		RsaEncrypt R_E = new RsaEncrypt();
		//System.out.println("Client_IP: "+IP);
		String temp = Message.substring(16); //去掉报头
		/*  get Server-Client Key   */
		try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			//String search = "select k from server_key where id ='"+IDc+"';";	//执行的sql语句
			String sql = "select k from server_key where IP='"+IP+"';";
			ResultSet rs = state.executeQuery(sql);
			while(rs.next())
			{
				//System.out.println(rs.getString("k"));
				key = rs.getString("k");
			}
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}
		
		/*   get Lottery_Info   */
		try {
			Class.forName(driver);	//加载驱动
			Connection conn1 = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state1 = conn1.createStatement();	//创建statement对象，用来执行sql语句
			//String search = "select k from server_key where id ='"+IDc+"';";	//执行的sql语句
			String search = "select * from lottery_now;";
			ResultSet rst = state1.executeQuery(search);
			while(rst.next())
			{
				Name = rst.getString("Name");
				Period = rst.getString("Period");
			}
			state1.close();
			conn1.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}
		
		
		
		String sign = Signature();
		String Info = D_D.Decrypt(temp, key);
		//System.out.println("Info: " + Info);
		ip_recv = select(Info,'{','}');
		int len = ip_recv.length();
		Number = Info.substring(len+2,len+10);
		//System.out.println("Name+Period+Number"+Name+Period+Number);
		tem = tem + "*" + Name + "*" + "|" + Period + "|" + Number + sign;
		hash = R_E.hash(tem, "MD5");
		String temps = tem + hash;
		String C = D_E.Encrypt(temps, key);
		res = res + C;
		String log = IP + " " + Name + " " + Period + " " + Number + "  发送购买凭证！"+"\r\n";
		Log(log,true);
		return res;
	}
	
	/*  发送开奖信息  */
	public static String Lottery(String Message,Server_UI UI)
	{
		String res = "0101010000000000";
		String temp = Message.substring(6);
		res = res + temp;
		String log = "  发送开奖信息！"+"\r\n";
		Log(log,true);
		UI.ShowLotteryInfo();
		return res;
	}
	
	/*  签名  */
	public static String Signature()
	{
		String res = "";
		RsaEncrypt R_E = new RsaEncrypt();
		String x = R_E.encrypt("193162", Rsa_n, Rsa_PK);
		int len = x.length();
		res = res + "!" + len + "!";
		res = res + x;
		return res;
	}
	
	/*  判断生命周期  */
	public static boolean CheckTime(String Message)
	{
		int H;
		int M;
		int S;
		int Life;
		int Life_M;
		int Life_S;
		Date today=new Date();
		SimpleDateFormat f=new SimpleDateFormat(" mm:ss ");
		String time=f.format(today); 
		String a = select(time,' ',':');
		String b = select(time,':',' ');
		int Now_M = Integer.valueOf(a).intValue();
		int Now_S = Integer.valueOf(b).intValue();
		//System.out.println(Now_M+"  " + Now_S);
		String Time = select(Message,'[',']');
		String life = select(Message,'(',')');
		String h = Time.substring(0,2);
		String m = Time.substring(3,5);
		String s = Time.substring(6);
		//System.out.println(m+ "  " + s);
		H = Integer.valueOf(h).intValue();
		M = Integer.valueOf(m).intValue();
		S = Integer.valueOf(s).intValue();
		Life = Integer.valueOf(life).intValue();
		Life_M = Life/60;
		Life_S = Life%60;
		if(Life_M+M < Now_M) return false;
		if(Life_M+M == Now_M&&Life_S+S < Now_S) return false;
		return true;
	}
	
	/*  检查IP正确性 */ 
	public static boolean CheckIP(String Message,String ticket)
	{
		String Authentication = select(Message,'$','$');
		String ip = select(Authentication,'{','}');
		String IP = select(ticket,'{','}');
		//System.out.println("ip: "+ip);
		//System.out.println("IP: "+IP);
		if(IP.equals(ip)) return true;
		else return false;
	}
	
	/*  截取字符串   */
	/**/
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

    /*  记录日志  */
	public static void Log(String w,boolean a)
    {
        String ts="";
        Date date = new Date(); 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
        ts= sdf.format(date);
        try 
        {
            File file=new File("log");
            FileWriter fileWritter = new FileWriter(file.getName(),a);
            fileWritter.write(ts+" "+w);
            fileWritter.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

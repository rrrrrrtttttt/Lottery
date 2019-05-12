package Test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class Server_UI extends JFrame{
	static String driver = "com.mysql.cj.jdbc.Driver";	//驱动名，默认
	static String url = "jdbc:mysql://localhost:3306/Lottery?serverTimezone=GMT&useSSL=false";	
	static String user = "root";	//mysql数据库用户名
	static String password = "123456";	//mysql数据库用户密码
	// 创建组件
	public static JPanel jp1, jp2;
	// 创建组件名称
	public static JLabel jlb1, jlb2, jlb3, jlb4;
	// 创建button
	public static JButton jb1, jb2, jb3, jb4, jb5;
	// 创建文本框
	public static JTextField jtf1, jtf2, jtf3, jtf4, jtf5, jtf6, jtf7, jtf8;
	// 创建表格
	public static JScrollPane scroll;  
	private static DefaultTableModel model = null;
	public static JTable table = null;
	
	// 构建函数
	public Server_UI() {
		// 创建组件
		jp1 = new JPanel();
		jp2 = new JPanel();
		//创建组件名称
		jlb1 = new JLabel("奖项名称");
		jlb2 = new JLabel("期数");
		jlb3 = new JLabel("中奖号码");
		jlb4 = new JLabel("开奖日期");
		// 创建button
		jb1 = new JButton("指定中奖号");
		jb2 = new JButton("查看往期");
		jb3 = new JButton("返回");
		jb4 = new JButton("确定");
		jb5 = new JButton("取消");
		
		jtf1 = new JTextField(10);
		jtf2 = new JTextField(10);
		jtf3 = new JTextField(10);
		jtf4 = new JTextField(10);
		jtf5 = new JTextField(10);
		jtf6 = new JTextField(10);
		jtf7 = new JTextField(10);
		jtf8 = new JTextField(10);
		
		String[][][] datas = {};
	    String[] titles = { "Name","Period","Number" };
	    model = new DefaultTableModel(datas, titles);
	    table = new JTable(model);
		// 设置布局管理器

		setLayout(new GridLayout(3, 1));
		// 加入组件
		jp1.add(table);
		jp1.add(jlb1);
		jp1.add(jtf1);
		jp1.add(jtf5);
		jp1.add(jlb2);
		jp1.add(jtf2);
		jp1.add(jtf6);
		jp1.add(jlb3);
		jp1.add(jtf3);
		jp1.add(jtf7);
		jp1.add(jlb4);
		jp1.add(jtf4);
		jp1.add(jtf8);
		jp2.add(jb1);
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		jp2.add(jb5);
		scroll = new JScrollPane(table);
		// 加入到JFrame
		scroll.setVisible(false);
		jb4.setVisible(false);
		jb5.setVisible(false);
		add(scroll);
		add(jp1);
		add(jp2);
		//jp1.add(new JScrollPane(table));
		table.setVisible(false);
		jb3.setVisible(false);
		// 设置框体
		setTitle("服务器主界面：");
		setSize(400, 300);
		setLocation(1500, 1500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		jb1.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e)
	         {
	            GetNextLottery();
	         }
	      });
		jb2.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e)
	         {
	            ShowLastLottery();
	         }
	      });
		jb3.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e)
	         {
	            ShowLotteryInfo();
	         }
	      });
		jb4.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e)
	         {
	            Sure();;
	         }
	      });
		jb5.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e)
	         {
	            ShowLotteryInfo();
	         }
	      });

	}
	public static void ShowLotteryInfo()
	{
		jlb1.setVisible(true);
		jlb2.setVisible(true);
		jlb3.setVisible(true);
		jlb4.setVisible(true);
		jtf1.setVisible(true);
		jtf2.setVisible(true);
		jtf3.setVisible(true);
		jtf4.setVisible(true);
		jtf5.setVisible(false);
		jtf6.setVisible(false);
		jtf7.setVisible(false);
		jtf8.setVisible(false);
		table.setVisible(false);
		jb1.setVisible(true);
		jb2.setVisible(true);
		jb3.setVisible(false);
		jb4.setVisible(false);
		jb5.setVisible(false);
		scroll.setVisible(false);
		
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
				jtf1.setText(rst.getString("Name"));
				jtf2.setText(rst.getString("Period"));
				jtf3.setText(rst.getString("Number"));
				jtf4.setText(rst.getString("Date"));
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

	}
	
	public static void ShowLastLottery()
	{
		jlb1.setVisible(false);
		jlb2.setVisible(false);
		jlb3.setVisible(false);
		jlb4.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf3.setVisible(false);
		jtf4.setVisible(false);
		jtf5.setVisible(false);
		jtf6.setVisible(false);
		jtf7.setVisible(false);
		jtf8.setVisible(false);
		table.setVisible(true);
		jb3.setVisible(true);
		scroll.setVisible(true);
		int num = model.getRowCount(); //得到此数据表中的行数        
		for (int i = 0; i < num; i++)     //利用循环依次清空所有行            
		model.removeRow(0);
		try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			String sql = "select * from lottery_last;";	//执行的sql语句
			ResultSet rst = state.executeQuery(sql);
			while(rst.next())
			{
				String[] x = { rst.getString("Name"), rst.getString("Period"), rst.getString("Number") }; 
				model.addRow(x);
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
		
	}

	public static void GetNextLottery()
	{
		jlb1.setVisible(true);
		jlb2.setVisible(true);
		jlb3.setVisible(true);
		jlb4.setVisible(true);
		jtf5.setVisible(true);
		jtf6.setVisible(true);
		jtf7.setVisible(true);
		jtf8.setVisible(true);
		jtf1.setVisible(false);
		jtf2.setVisible(false);
		jtf3.setVisible(false);
		jtf4.setVisible(false);
		table.setVisible(false);
		scroll.setVisible(false);
		jb1.setVisible(false);
		jb2.setVisible(false);
		jb3.setVisible(false);
		jb4.setVisible(true);
		jb5.setVisible(true);
	}

	public static void Sure()
	{
		String Name = jtf5.getText();
		String Period = jtf6.getText();
		String Number = jtf7.getText();
		String Date = jtf8.getText();
		try {
			Class.forName(driver);	//加载驱动
			Connection conn = DriverManager.getConnection(url, user, password);	//创建connection对象,用来连接数据库
			//if(!conn.isClosed())
				//System.out.println("Succeed!");
			Statement state = conn.createStatement();	//创建statement对象，用来执行sql语句
			String sql = "insert into lottery_next(Name,Period,Number,Date) values('"+Name+"','"+Period+"','"+Number+"','"+Date+"');";	//执行的sql语句
			int rst = state.executeUpdate(sql);
			/*String sql = "insert into lottery_now(Name,Number,Period,Date) values (\"中国福利彩票\",\"1234567\",\"100\",\"5/22 12:00\");";	//执行的sql语句
			int rst = state.executeUpdate(sql);*/
			state.close();
			conn.close();	
		}catch(Exception e) {
			System.out.println("defeat!");
			System.out.println(e);
		}
		ShowLotteryInfo();
	}
}
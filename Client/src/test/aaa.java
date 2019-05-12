package test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class aaa
{
	public static void main(String[] args) throws InterruptedException, Exception 
	{
		Package p=new Package();
		p.log("系统启动...\r\n",false);
		p.log("请求AS服务...\r\n",true);
		Client c = new Client();
		c.start();
		Thread.sleep(1000);
		p.log("请求TGS服务...\r\n",true);
		c.start1();
		Thread.sleep(1000);
		p.log("请求server服务...\r\n",true);
		c.start2();//*/
		
	}
}



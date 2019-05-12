package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.Inflater;

public class Package {
	
	static String JDBC_DRIVER = "com.mysql.cj.jdbc.Drive";
	static String DB_URL = "jdbc:mysql://localhost:3306/mima?serverTimezone=GMT&useSSL=false";
	static String USER = "root";
	static String PASS = "123456";
	public static String Determine(String message,Client_UI v)
	{
		BigInteger Rsa_Key = new BigInteger("65537");
		BigInteger Rsa_n = new BigInteger("6134845666846995435976860636142316724088449501012617917545653453506816832534783145847717262030828282211781261512213357429125146843017551429292368809260761");
		BigInteger Rsa_Key1 = new BigInteger("22111324936843962594302949968014183516529082440240267519123838460677915940430750624663505817328458673344195648166817483826110340164283929978160037113312581644467325023377987114598980734905306140064927442369902288670153740306775259129523572840818731923365674319218941286103282959629250944859817361441718054913");
		String des="1234567";
		
		String str ="";
		if(message.length()>6)
		{
			String head=message.substring(0, 6);
			if(head.equals("100000"))//3
			{
				str=SendKey();
			}
			else if(head.equals("000001"))//发送des密钥  3
			{
				String b22="select des from mi where name='AS'";
				try {
					des=read1(b22);
					log("从mi数据库读取client与AS的DES密钥："+des+"\r\n",true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				RsaEncrypt p =new RsaEncrypt();
				String b=p.encrypt("0000"+des, Rsa_n, Rsa_Key);
				log("RSA加密DES密钥："+des+"\r\n",true);
				str=message.substring(0,16)+b;
				log("向AS发送DES密钥："+b+"\r\n",true);
			}
			else if(head.equals("100001"))//请求票据授权票据  4 5
			{
				str=GetBills();
			}
			else if(head.equals("100010"))//票据授权票据  6
			{
				String a=message.substring(16);
				String b=jiemi(a, des);
				log("解密收到的数据："+b+"\r\n",true);
				String b1=b.substring(0, 7);
				String b2=b.substring(26);
				String c="update mi set des='"+b1+"' where name='TGS';";
				String c1="update mi set ticket='"+strto16(b2)+"' where name='AS';";
				try {
					write1(c);
					log("写入mi数据库client与TGS的DES密钥："+b1+"\r\n",true);
					write1(c1);
					log("写入mi数据库AS发放的ticket："+strto16(b2)+"\r\n",true);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				str=Over();
				log("向AS发送结束信号："+str+"\r\n",true);
				
			}
			else if(head.equals("000011"))//TGS(7)
			{
				String b11="select ticket from mi where name='AS'";
				String bb="";
				String ip="";
				try {
					bb = hexStringToString(read1(b11));
					log("从mi数据库读取AS发放的票据："+bb+"\r\n",true);
					ip=getLocalHostLANAddress();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				str="0000110000000000"+"0100"+"$"+"0000{"+ip+"}"+"["+gettime()+"]"+"$"+bb;
				log("向TGS发送TGS标识、本机标识、本机IP、时间、票据："+str+"\r\n",true);
			}
			else if(head.equals("110010"))//发放服务许可票据 TGS(8)
			{
				String b22="select des from mi where name='TGS'";
				try {
					des=read1(b22);
					log("从mi数据库读取client与TGS的DES密钥："+des+"\r\n",true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String a=message.substring(16);
				String b=jiemi(a, des);
				log("解密TGS发送的信息："+b+"\r\n",true);
				String b1=b.substring(0, 7);
				String b2=b.substring(21);
				String c="update mi set des='"+b1+"' where name='Server';";
				String c1="update mi set ticket='"+strto16(b2)+"' where name='TGS';";
				try {
					write1(c);
					log("写入mi数据库client与server的DES密钥："+b1+"\r\n",true);
					write1(c1);
					log("写入mi数据库TGS发放的ticket："+strto16(b2)+"\r\n",true);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//*/
				str=Over();
				log("向TGS发送结束信号："+str+"\r\n",true);
			}
			else if(head.equals("000100"))//Server(9)发送ticket(v)
			{
				String b11="select ticket from mi where name='TGS'";
				String bb="";
				String ip="";
				try {
					bb = hexStringToString(read1(b11));
					log("从mi数据库读取TGS发放的票据："+bb+"\r\n",true);
					ip=getLocalHostLANAddress();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				str="0001000000000000"+"$"+"0000{"+ip+"}"+"["+gettime()+"]"+"$"+bb;
				log("向Server发送本机标识、本机IP、时间、票据："+str+"\r\n",true);
			}
			else if(head.equals("010010"))//认证信息(10)(11)请求信息
			{
				String b22="select des from mi where name='Server'";
				try {
					des=read1(b22);
					log("从mi数据库读取client与server的DES密钥："+des+"\r\n",true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String a=message.substring(16);
				String b=jiemi(a, des);
				log("DES解密server发送的认证信息："+b+"\r\n",true);
				str="0001010000000000"+"11111111";
				log("向server发送数据请求："+str+"\r\n",true);
			}
			else if(head.equals("010011"))//处理信息（12）
			{
				String a=message.substring(16);
				v.jtf2.setText(getstr(a,"*","*"));
				log("设定奖项名称："+getstr(a,"*","*")+"\r\n",true);
				v.jtf4.setText(getstr(a,"|","|"));
				log("设定期数："+getstr(a,"|","|")+"\r\n",true);
				v.jtf1.setText(getstr(a,"<",">"));
				log("设定开奖时间："+getstr(a,"<",">")+"\r\n",true);
				str=Over();
				log("向AS发送结束信号："+str+"\r\n",true);
			}
			else if(head.equals("000110"))//发送购买信息（13）
			{
				String ip="";
				String number=message.substring(16);
				String b22="select des from mi where name='Server'";
				try {
					des=read1(b22);
					log("从mi数据库读取client与server的DES密钥："+des+"\r\n",true);
					ip=getLocalHostLANAddress();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				String a="{"+ip+"}"+number;
				str="0001100000000000"+jiami(a,des);
				log("发送加密后的IP、number："+str+"\r\n",true);
			}
			else if(head.equals("010100"))//购买反馈（14）
			{
				String b22="select des from mi where name='Server'";
				try {
					des=read1(b22);
					log("获取client与server之间的DES密钥:"+des+"\r\n",true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String a=message.substring(16);
				String b=jiemi(a, des);
				log("用DES解密购买凭证:"+b+"\r\n",true);
				System.out.printf("购买彩票的证明：%s\n",b);
				int a1=getstr(b,"*","*").length()+getstr(b,"|","|").length()+14+getstr(b,"!","!").length();
				QRCode.str2QR(b.substring(0, a1));//生成二维码
				log("生成二维码:"+b.substring(0, a1)+"\r\n",true);
				int a2 = Integer.parseInt((String)getstr(b,"!","!"));
				String c=b.substring(a1, a1+a2);
				RsaDecrypt p =new RsaDecrypt();
				String bb=p.decrypt(c, Rsa_n, Rsa_Key);
				log("RSA解密签名:"+bb+"\r\n",true);
				if(!bb.equals("193162"))
				{
					v.bb();
					//log("签名不正确\r\n",true);
				}
				if(!b.substring(a1+a2).equals(hash(b.substring(0, a1+a2),"MD5")))
				{
					v.bb();
					//log("hash不正确\r\n",true);
				}
				str=Over();
				log("向AS发送结束信号："+str+"\r\n",true);
			}
			else if(head.equals("010101"))//接收开奖信息（15）
			{
				String a=message.substring(16);
				String b= getstr(a,"*","*");
				String c=getstr(a,"|","|");
				int d=b.length()+c.length()+4;
				String c1="insert into cp values('"+b+"','"+c+"','"+a.substring(d)+"')";
				try {
					write1(c1);
					log("向cp数据库写入这一期的开奖信息："+a+"\r\n",true);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//*/
				str="0001010000000000"+"11111111";
				log("向server发送数据请求："+str+"\r\n",true);
			}
		}
		return str;
	}
    public static String SendKey()//发送用户口令
    {//
    	String s ="00000100000000000000";
    	String b="1234567";
    	s=s+b;
    	return s;
	}
    public static String GetBills()//请求票据授权票据
    {//client标识、AS标识、时间
    	String s ="0000100000000000";
    	String a="0000";
    	String b="1100";
    	Date date = new Date(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
        String c = sdf.format(date);
        s=s+a+b+"["+c+"]";
        log("向AS发送本机标识、目的服务器标识、时间："+a+b+"["+c+"]\r\n",true);
    	return s;
	}
    public static String Over()//
    {
    	String s ="over";
    	return s;
	}
    public static void log(String w,boolean a)
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
    public static String gettime()
    {
    	String ts="";
    	Date date = new Date(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
        ts= sdf.format(date);
    	return ts;
    }
    public static String getstr(String a,String b,String d) {
		String w="";
		int c=0;
    	for(int i=0;i<a.length();i++)
    	{
    		if(c==0&&a.subSequence(i, i+1).equals(b))
    		{
    			c=i+1;
    		}
    		else if(c!=0&&a.subSequence(i, i+1).equals(d))
    		{
    			w=a.substring(c, i);
    			break;
    		}
    	}
    	return w;
	}
    public static String strto16(String word)
    {
    	String w="";
    	for(int i=0;i<word.length();i++)
		{
			int ch=(int)word.charAt(i);
			String s=Integer.toHexString(ch);
			if(s.length()==1)
			{
				s="0"+s;
			}
			w=w+s;
		}
    	return w;
    }
    public static String getLocalHostLANAddress() throws Exception {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return String.valueOf(inetAddr).substring(1);
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return String.valueOf(candidateAddress);
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return String.valueOf(jdkSuppliedAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String jiami(String M,String des)
    {
    	String m="";
    	int i,j,k,h;
    	int []m1=new int[64];
    	int []k1=new int[56];
    	DesEncrypt p=new DesEncrypt();
    	h=M.length();
    	for(i=0;i<h;i++)
    	{
    		j=h-i-1;
    		if((i+1)%8==0)
    		{
    			String a=M.substring(0, 8);
    			M=M.substring(8);
    		    m1=StrToBit(a);
    		    k1=StrToBits(des);
    			p.encrypt(m1, k1);
    			String txt=BitToStr(m1);
    			m=m+txt;
    			if(j>0&&j<8)
    			{
    				int e=8-M.length();
    				for(k=0;k<e;k++)
    				{
    					M=M+"&";
    				}
    				
        		    m1=StrToBit(M);
        		    k1=StrToBits(des);
        			p.encrypt(m1, k1);
        			String txt1=BitToStr(m1);
        			m=m+txt1;
    				break;
    			}
    		}
    	}
    	return m;
    }
    public static String jiemi(String M,String des)
    {
    	String m="";
    	String b="";
    	int i,j,k,h;
    	int []m1=new int[64];
    	int []k1=new int[56];
    	DesDecrypt p=new DesDecrypt();
    	h=M.length();
    	for(i=0;i<h;i++)
    	{
    		j=h-i-1;
    		if((i+1)%8==0)
    		{
    			String a=M.substring(0, 8);
    			M=M.substring(8);
    		    m1=StrToBit(a);
    	    	k1=StrToBits(des);
    			p.decrypt(m1, k1);
    			String txt=BitToStr(m1);
    			m=m+txt;
    		}
    		if(M.length()==8)
    		{
    			String a=M.substring(0, 8);
    			m1=StrToBit(a);
    	    	k1=StrToBits(des);
    			p.decrypt(m1, k1);
    			String txt1=BitToStr(m1);
    			
    			for(k=M.length();k>0;k--)
    			{
    				
    				if(txt1.substring(k-1, k).equals("&"))
    				{
    				}
    				else
    				{
    					b=txt1.substring(0, k);
    					break;
    				}
    			}
    			m=m+b;
    			break;
    		}
    	}
    	return m;
    }
    public static void write1(String word) throws SQLException, ClassNotFoundException
    {
    	Connection conn = null;
        Statement stmt = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		//连接数据库
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        //实例化Statement对象
        stmt = conn.createStatement();
        int rs = stmt.executeUpdate(word);
        
        //rs.close();
        stmt.close();
        conn.close();
    }
    public static String read1(String word) throws SQLException, ClassNotFoundException
    {
    	String f="";
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
        	f=rs.getString(1);
        }
        rs.close();
        stmt.close();
        conn.close();
        return f;
    }
	public static int[] StrToBit(String str)
	{
		int[] end = new int[64];
		try {
			char []x = str.toCharArray();
			for(int i = 0,j = 0;i < 8;i ++)
			{
				int a = (int)x[i];
				end[j] = a/128;
				end[j+1] = (a%128)/64;
				end[j+2] = (a%64)/32;
				end[j+3] = (a%32)/16;
				end[j+4] = (a%16)/8;
				end[j+5] = (a%8)/4;
				end[j+6] = (a%4)/2;
				end[j+7] = a%2;
				j = j+8;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("转换错误！");
		}
		return end;
	}
	
	public static int[] StrToBits(String str)
	{
		int[] end = new int[56];
		try {
			char []x = str.toCharArray();
			for(int i = 0,j = 0;i < 7;i ++)
			{
				int a = (int)x[i];
				end[j] = a/128;
				end[j+1] = (a%128)/64;
				end[j+2] = (a%64)/32;
				end[j+3] = (a%32)/16;
				end[j+4] = (a%16)/8;
				end[j+5] = (a%8)/4;
				end[j+6] = (a%4)/2;
				end[j+7] = a%2;
				j = j+8;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("转换错误！");
		}
		return end;
	}
	
	public static String BitToStr(int[] bit)
	{
		String end;
		char[] temp = new char[8];
		try {
			for(int i = 0,j = 0;i < 64;j ++)
			{
				int a = bit[i]*128+bit[i+1]*64+bit[i+2]*32+bit[i+3]*16+bit[i+4]*8+bit[i+5]*4+bit[i+6]*2+bit[i+7];
				temp[j] = (char)a;
				i = i+8;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("转换错误！");
		}
		end = String.valueOf(temp);
		return end;
	}
	public static String hexStringToString(String s) //16进制转字符串
	{
	    if (s == null || s.equals("")) {
	        return null;
	    }
	    String sss="";
	    s = s.replace(" ", "");
	    int[] baKeyword = new int[s.length() / 2];
	    for (int i = 0; i < baKeyword.length; i++) {
	        try {
	            baKeyword[i] = (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    try {
	    	int i;
	    	for(i=0;i<baKeyword.length;i++)
	    	{
	    		char a=(char)baKeyword[i];
	    		String ss = String.valueOf(a);
	    		//System.out.printf("%d  %s  %s\n",baKeyword[i],a,ss);
	    		sss=sss+ss;
	    	}
	        //System.out.println(s);
	        new String();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	    return sss;
	}
	public static String hash(String string, String algorithm) {
        if (string.isEmpty()) {
            return "";
        }
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance(algorithm);
            byte[] bytes = hash.digest(string.getBytes("UTF-8"));
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	public static int error(String w)
	{
		int a=0;
		if(w.equals("001"))
		{
			log("用户口令发送失败！\r\n",true);
			a=1;
		}
		else if(w.equals("010"))
		{
			log("客户端身份验证失败，IP地址不正确！\r\n",true);
			a=2;
		}
		else if(w.equals("011"))
		{
			log("数据包超过生存周期！\r\n",true);
			a=3;
		}
		return a;
	}
}

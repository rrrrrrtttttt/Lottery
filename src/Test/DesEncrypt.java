package Test;


public class DesEncrypt {
	
	/*      置换表       */
    static int[] IP = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,
			           62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
			           57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
			           61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
	static int[] IP1 = {40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,
			            38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
			            36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,
			            34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};
	public static String Encrypt(String M,String des)
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
	
	
	
	
    /*      加密方法     */
    public static int[] encrypt(int[] M,int[] Keys)
    {
    	int[][] k = new int[16][48];
		int[] L = new int[32];
		int[] R = new int[32];
		int[] tem = new int[32];
		int[] temp = new int[32];
		DesFunction F = new DesFunction();
		
		/* KEY实例，获得每一轮的密钥     */
		DesKey K = new DesKey();
    	for(int i = 0;i < 16;i ++)
    	{
    		k[i] = K.GetKey(Keys,i);
    	}
    	
    	/*   加密过程    */
		IP(M,1);       //IP置换
    	for(int i = 0;i < 16;i ++)
    	{
    		
    		/*  明文分为L和R两部分     */
    		for(int l = 0;l < 32;l ++)
    		{
    			L[l] = M[l];
    		}
    		for(int l = 0,o = 32;l < 32&&o < 64;l++,o++)
    		{
    			R[l] = M[o];
    		}
    		tem = F.function(R,k[i]); //轮函数
    		
    		/*  轮函数输出与左部做异或操作，即模2加法运算      */
    		for(int j = 0;j < L.length;j ++)
    		{
    			temp[j] = (L[j]+tem[j])%2;
    		}
    		
    		/*   将R直接作为明文的左部，异或的结果作为右部，进行下一轮的加密    */
            for(int j = 0;j < 32;j ++)
            {
            	M[j] = R[j];
            }
            for(int j = 32,o = 0;j < 64&&o < 32;j ++,o ++)
            {
            	M[j] = temp[o];
            }
    	}   	
    	
    	/*   最后一轮结束后进行一次交换   */
        for(int i = 0,j = 32;i<32;i++,j++)
        {
        	int T = M[j];
        	M[j] = M[i];
        	M[i] = T;
        }     
    	IP(M,2);    	//IP逆置换
    	return M;
    }	
    
    /*      IP置换                   */
	public static void IP(int[] M,int i)
	{
		if(i == 1)
		{
			int[] temp = new int[64];
			temp = (int[])M.clone();
			for(int j = 0;j < 64;j ++)
			{
				M[j] = temp[IP[j]-1]; 
			}
		}
		else if(i == 2)
		{
			int[] temp = new int[64];
			temp = (int[])M.clone();
			for(int j = 0;j < 64;j ++)
			{
				M[j] = temp[IP1[j]-1]; 
			}
		}
	}
	
	/*   将输入的字符串明文转为64位bit（0/1）        */
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
			System.out.println("请检查明文长度");
		}
		return end;
	}
	
	/*   将输入的Key字符串转为56位bit（0/1）              */
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
			System.out.println("请检查明文长度");
		}
		return end;
	}
	
	/*   将结果产生的64位bit（0/1）转为字符串            */
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
				//System.out.print(temp[j]);
				
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("请检查明文长度");
		}
		end = String.valueOf(temp);
		return end;
	}

}
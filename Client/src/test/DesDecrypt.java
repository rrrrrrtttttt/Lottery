package test;

public class DesDecrypt {
	static int[] IP = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,
	           62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
	           57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
	           61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
    static int[] IP1 = {40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,
	            38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
	            36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,
	            34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};
    
    public static String Decrypt(String M,String K)
    {
    	String m = M;
    	int j = 0;
    	int p = 0;
    	int length = M.length();
    	int x = length%8;
    	for(int i = 0;i<8-x;i++)
    	{
    		m = m + "&";
    	}
    	String[] str = new String[m.length()/8];
    	for(int i = 0,l = 0;i<m.length()/8;i++)
    	{
    		str[i] = m.substring(l,l+8);
    		l = l+8;
    	}
    	int[] k = StrToBits(K);
    	int num = m.length()*64;
    	int[] result = new int[num];
    	int[] temp = new int[64];
    	String res = "";
    	//System.out.println(num);
    	for(int i = 0;i<(m.length()/8)&&j<num;i++)
    	{
    		int[] M_temp = StrToBit(str[i]);
    		temp = decrypt(M_temp,k);
    		//System.out.println(i);
    		for(int q = 0;q < 64;q++)
    		{
    			result[j] = temp[q];
    			j++;
    		}
    	}
    	for(int i = 0;i<(m.length()/8);i++)
    	{
    		for(int q = 0;q<64;q++,p++)
    		{
    			temp[q] = result[p];
    		}
    		res = res + BitToStr(temp);
    	}
    	return res;
    }
    public static int[] decrypt(int[] C,int[] Keys)
    {
    	int[][] k = new int[16][48];
		int[] L = new int[32];
		int[] R = new int[32];
		int[] tem = new int[32];
		int[] temp = new int[32];
		DesFunction F = new DesFunction();
		DesKEY K = new DesKEY();
    	for(int i = 0;i < 16;i ++)
    	{
    		k[i] = K.GetKey(Keys,i);
    	}
		IP(C,1);       //IP置换
    	/*  加密  */
    	for(int i = 0;i < 16;i ++)
    	{
    		for(int l = 0;l < 32;l ++)
    		{
    			L[l] = C[l];
    		}
    		for(int l = 0,o = 32;l < 32&&o < 64;l++,o++)
    		{
    			R[l] = C[o];
    		}
    		tem = F.function(R,k[15-i]); //轮函数
    		for(int j = 0;j < L.length;j ++)
    		{
    			temp[j] = (L[j]+tem[j])%2;
    		}
            for(int j = 0;j < 32;j ++)
            {
            	C[j] = R[j];
            }
            for(int j = 32,o = 0;j < 64&&o < 32;j ++,o ++)
            {
            	C[j] = temp[o];
            }
    	}   	
        for(int i = 0,j = 32;i<32;i++,j++)
        {
        	int T = C[j];
        	C[j] = C[i];
        	C[i] = T;
        }     
    	IP(C,2);  
    	return C;
    }
	public static void IP(int[] C,int i)
	{
		if(i == 1)
		{
			int[] temp = new int[64];
			temp = (int[])C.clone();
			for(int j = 0;j < 64;j ++)
			{
				C[j] = temp[IP[j]-1]; 
			}
		}
		else if(i == 2)
		{
			int[] temp = new int[64];
			temp = (int[])C.clone();
			for(int j = 0;j < 64;j ++)
			{
				C[j] = temp[IP1[j]-1]; 
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
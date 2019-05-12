package test;


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
    
    /*      加密方法     */
    public void encrypt(int[] M,int[] Keys)
    {
    	int[][] k = new int[16][48];
		int[] L = new int[32];
		int[] R = new int[32];
		int[] tem = new int[32];
		int[] temp = new int[32];
		DesFunction F = new DesFunction();
		
		/* KEY实例，获得每一轮的密钥     */
		DesKEY K = new DesKEY();
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

}
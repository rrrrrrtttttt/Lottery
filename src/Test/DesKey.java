package Test;

public class DesKey {
    static int[] pc1 = {50,43,36,29,22,15,8,1,51,44,37,30,23,16,9,2,52,45,38,31,24,17,10,3,53,46,39,32,56,49,42,35,28,21,14,7,55,48,41,34,27,20,13,6,54,47,40,33,26,19,12,5,53,46,39,32};
    static int[] pc2 = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};
    static int[] keyshift = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    
    /*  子密钥获取  */
    public static int[] GetKey(int[] Key,int i) 
	{
			PC1(Key);
			KeyShift(Key,i);
			return PC2(Key);		
	}
	
	/*  用PC1表进行置换  */
	public static void PC1(int[] Key)
	{
		int[] temp = (int[])Key.clone();
		for(int j = 0 ;j < 56;j ++)
		{

			Key[j] = temp[pc1[j]-1];
		}
	}
	
	/*  用keyshift表进行移位  */
	public static void KeyShift(int[] Key,int i)
	{
		int[] temp = new int[56];
		temp = (int[])Key.clone();
		int x;
		for(int j = 0;j < 28;j ++)
		{
			x = (j +28 -i)%28;
			Key[j] = temp[x];
		}
		for(int j = 28;j < 55;j++)
		{
			x = (i - i)%28;
			Key[j] = temp[x+28];
		}
	}
	
	/*  用PC2表进行置换  */
	public static int[] PC2(int[] Key)
	{
		int[] temp = new int[48];
		for(int j = 0;j < 48;j ++)
		{
			temp[j] = Key[pc2[j]-1];
		}
		return temp;
	}
}
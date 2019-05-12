package test;
import java.math.*;
import java.util.Random;
public class RsaGetKey {
	static BigInteger gcd;
	static BigInteger x;
	static BigInteger y;
    static BigInteger e = new BigInteger("65537");
    static BigInteger n;
    
    //主函数
	public static void getkey()
	{
		BigInteger p = GetBigInteger();
		BigInteger q = GetBigInteger();
		n = p.multiply(q);
		BigInteger p1 = p.subtract(BigInteger.ONE);
		BigInteger q1 = q.subtract(BigInteger.ONE);
		BigInteger var = p1.multiply(q1);
		gcd = ex_Eulid(var,e);
	}
	
	//返回公钥
	public static BigInteger GetPublicKey()
	{
		return e;
	}
	
	//返回私钥
	public static BigInteger GetPrivateKey()
	{
		return y;
	}

	//返回p*q
	public static BigInteger GetN()
	{
		return n;
	}
	
	//随机生成BigInteger数
	public static BigInteger GetBigInteger()
	{
		while(1!=0)
		{
			BigInteger a = new BigInteger(256,0,new Random());
		    if(IsPrime(a)) return a;
		}
	}
		
	//扩展欧几里得 Gcd（a，b） = a*x+b*y 
	/* 原理x1 = y2 , y1 = x2 - [a/b]*y2 ，利用递归函数，最内层函数中x = 1，y = 0*/
	public static BigInteger ex_Eulid(BigInteger a,BigInteger  b){
    	if(b.compareTo(BigInteger.ZERO) == 0){
    		x = BigInteger.ONE;y=BigInteger.ZERO;
    	    return a;
    	}
    		BigInteger ans = ex_Eulid(b,a.mod(b));
    		BigInteger temp=x;
    		x=y;
    		BigInteger q1 = a.divide(b);
    		BigInteger q2 = q1.multiply(y);
    		y = temp.subtract(q2);
    		return ans;   	
    }
	
	//快速幂取模
	public static BigInteger quickpow(int a,BigInteger b,BigInteger r)
	{
		BigInteger result = new BigInteger("1");
		BigInteger tem = new BigInteger("1");
		String temp = ""+a;
		BigInteger base = new BigInteger(temp);
		while(b.compareTo(BigInteger.ZERO)!=0) //以指数的2进制位数为循环次数
		{
			/*  判断指数的2进制表示的最后一位是否为1，是则乘  */
			if(b.and(tem).compareTo(BigInteger.ZERO)!=0) result = (result.multiply(base)).mod(r);
			base = (base.multiply(base)).mod(r);
			
			/*  右移一位，继续判断前一位  */
			b = b.shiftRight(1);
		}
		return result;
	}
	
	//MR素性检测法
	public static Boolean Miller_Rabbin(BigInteger n,int a)
	{
		int r = 0;
		int j;
		String temp = ""+a;
		BigInteger aa = new BigInteger(temp);
		BigInteger s = n.subtract(BigInteger.ONE);
		if(n.mod(aa).compareTo(BigInteger.ZERO)==0)
			return false;
		while(s.and(BigInteger.ONE).compareTo(BigInteger.ZERO)==0)
		{
			s = s.shiftRight(1);
			r++;
		}
		BigInteger k = quickpow(a,s,n);
		if(k.compareTo(BigInteger.ONE) == 0)
		{
			return true;
		}
		for(j = 0;j<r;j++,k = k.multiply(k).mod(n))
		{
			if(k.compareTo(n.subtract(BigInteger.ONE)) == 0) return true;
		}
		return false;
	}
	
	//判断是否为素数
	public static Boolean IsPrime(BigInteger n)
	{
		int tab[] = {2,3,5,7,11};
		
		for(int i = 0;i < 5;i ++)
		{
			if(n.intValue() == tab[i]) return true;
			if(!Miller_Rabbin(n,tab[i])) return false;
		}
		return true;
	}

}
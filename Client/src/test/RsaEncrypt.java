package test;

import java.math.*;

public class RsaEncrypt {
	public static String encrypt(String M,BigInteger n,BigInteger key)
	{

		String w="";
		BigInteger c;
		int i;
		for(i=0;i<M.length();i++)
		{
			int ch=(int)M.charAt(i);
			String s=Integer.toHexString(ch);
			w=w+s;
		}
		//System.out.printf("%s\n",w);
		c=new BigInteger(w,16);
		//System.out.printf("%s\n",c);
		BigInteger C = c.modPow(key,n);
		//System.out.printf("%s\n",C);
		w=C.toString(16);
		//System.out.printf("%s\n",w);
		return w;
	}
	public static String hexStringToString(String s) {
	    if (s == null || s.equals("")) {
	        return null;
	    }
	    s = s.replace(" ", "");
	    byte[] baKeyword = new byte[s.length() / 2];
	    for (int i = 0; i < baKeyword.length; i++) {
	        try {
	            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    try {
	        s = new String(baKeyword, "ascii");
	        new String();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	    return s;
	}
}
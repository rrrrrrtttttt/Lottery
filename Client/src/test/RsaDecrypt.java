package test;

import java.math.*;

public class RsaDecrypt {
	public static String decrypt(String C,BigInteger n,BigInteger key)
	{
		String w="";
		int i;
		BigInteger c=new BigInteger(C,16);
		//System.out.printf("%s\n",c);
		BigInteger M = c.modPow(key,n);
		w=M.toString(16);
		String w1=hexStringToString(w);
		//System.out.printf("%s\n",w1);
		return w1;
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
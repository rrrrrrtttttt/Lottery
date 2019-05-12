package Test;
import java.math.*;

public class RsaDecrypt {
	public static String decrypt(String M,BigInteger n,BigInteger key)
	{
		BigInteger C;
		String M_16;
		String C_16;
		String c; 
        BigInteger m;
		m = new BigInteger(M,16);
		//System.out.println(m);
		C = m.modPow(key, n);
		//System.out.println(C);
		C_16 = C.toString(16);
		//System.out.println(C_16);
        c = hexStringToString(C_16);
        //System.out.println(c);
		return c;
	}
	
	/*  字符串转16进制  */
	public static String strTo16(String s) {
	    String str = "";
	    for (int i = 0; i < s.length(); i++) {
	        int ch = (int) s.charAt(i);
	        String s4 = Integer.toHexString(ch);
	        str = str + s4;
	    }
	    return str;
	}
	
	/*  16进制转字符串  */
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
	        s = new String(baKeyword, "UTF-8");
	        new String();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	    return s;
	}
}
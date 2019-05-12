package Test;
import java.io.UnsupportedEncodingException;
import java.math.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RsaEncrypt {
	public static String encrypt(String M,BigInteger n,BigInteger key)
	{
		BigInteger C;
		String C_16 = "";
		String c = "";
		String M_16 = "";
		BigInteger m;
		M_16 = strTo16(M);
		m = new BigInteger(M_16,16);
		C = m.modPow(key, n);
        C_16 = C.toString(16);
        //c = hexStringToString(C_16);
		return C_16;
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
	
	//string 明文   algorithm 加密算法名
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
}
package Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		Server s = new Server();
		/*Server_UI Server = new Server_UI();
		Server.jtf1.setText("Server Start!");*/
		try {
			s.start();
		}catch(IOException e) {
			e.printStackTrace();
		}

	}
	

}


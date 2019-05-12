package test;

import java.io.OutputStream;
import java.io.*;
import java.time.*;

public class CameraPic {

    public static void main(String... args) throws IOException, InterruptedException {
        String a = PicQR();
        if( a.length() == 0){
            System.out.println("no QRcode");

        }
        else{
            System.out.println(a);
        }
    }


    public static String PicQR()throws IOException, InterruptedException{
        String txtPath = "./qr2str.txt";
        System.out.println();
        String [] cmd={"/bin/sh","-c","python3 ./picQR.py"};
        // "bin/sh" 指定环境
        // "-c"
        // "python3.6 /home/djj/IdeaProjects/CAIPIAO/picQR.py" python3.6 可以替换为可运行的python命令
        //             + picQR.py 的绝对路径
        //
        //String command = "sudo python3 /home/djj/IdeaProjects/CAIPIAO/picQR.py";

        String cmd2 = "google-chrome";

        Process p = Runtime.getRuntime().exec(cmd);
        // p.getOutputStream().write();
        p.waitFor();
        String mymr = readFile(txtPath);
       return mymr;
    }



    private static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }


    private static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        readToBuffer(sb, filePath);
        return sb.toString();
    }

}
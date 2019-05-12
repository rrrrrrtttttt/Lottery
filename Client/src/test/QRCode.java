package test;
//*
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.nio.file.Path;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;


/*
类名：
    Qrcode

生成二维码 以及解析二维码
使用的的jar包为 zxing 3.3.0

maven ：
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>3.3.0</version>
        </dependency>

输入对象：String 或 File（png or jpg）
输出对象：File（png or jpg） 或 String

图片文件的存储格式为 “tmp.png”   若有该文件：覆盖
文件存储路径： ./   即当前目录下

函数名称：
GetQRCode()

str2QR()
QR2str()

Query()
函数作用：

*/
public class QRCode{
    public static void main(String[] args){
        str2QR("qwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnmqwertyuioplkjhgfdddddsazxcvbnm");
        //System.out.println(QR2str("./img.png"));

    }

    public static void GetQRCode(String Information)
    {
        //将服务器传来的凭证信息生成二维码并保存为图片
        str2QR(Information);

    }

    public static void str2QR(String contents)
    {
        //将服务器传来的凭证信息生成二维码并保存为图片
        contents = "Chicken, you are too beautiful." + contents;
        String format="png";
        HashMap map=new HashMap();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bm = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, 300, 300);

            Path file;
            file = new File("./img.png").toPath();

            MatrixToImageWriter.writeToPath(bm, format, file);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String QR2str(String path){
        String mr = "";
        try {
            MultiFormatReader reader=new MultiFormatReader();
            File f=new File(path);
            BufferedImage image= ImageIO.read(f);
            BinaryBitmap bb=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            HashMap map =new HashMap();
            map.put(EncodeHintType.CHARACTER_SET, "utf-8");
            Result result = reader.decode(bb,map);
            //System.out.println("解析结果："+result.toString());
            //System.out.println("二维码格式类型："+result.getBarcodeFormat());
            //System.out.println("二维码文本内容："+result.getText());
            mr = result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mr;
    }
    public static void Query()   //查询
    {
        //扫描二维码，获取彩票信息，在数据库中查询改期彩票中奖号码，是否中奖
        String info = QR2str("./tmp.png");
    }
}//*/
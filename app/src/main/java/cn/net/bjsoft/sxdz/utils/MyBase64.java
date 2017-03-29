package cn.net.bjsoft.sxdz.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zkagang on 2016/9/28.
 */
public class MyBase64 {
    /**
     将文件转为String
      */
    public static String fileToString(String path) throws Exception {
        File file = new File(path);
        InputStream inStream = new FileInputStream(file);
        byte[] buffer = new byte[102400];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        String s = new String(Base64.encode(data, Base64.DEFAULT));
        outStream.close();
        inStream.close();
        return s;
    }


    /**
     将文件转为String
     */
    public static String file2String(String path){
        File file = new File(path);
        String result = "";
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            result = Base64.encodeToString(buffer, Base64.DEFAULT);
            //Log.e("Base64", "Base64---->" + encodedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bigFileToString(String path){
        String result = "";
        FileInputStream objFileIS = null;
        try
        {
            objFileIS = new FileInputStream(path);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
        byte[] byteBufferString = new byte[1024];
        try
        {
            for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1;)
            {
                objByteArrayOS.write(byteBufferString, 0, readNum);

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        result = Base64.encodeToString(objByteArrayOS.toByteArray(), Base64.DEFAULT);
       // result = Base64.encodeToString(byteBufferString, Base64.DEFAULT);
        //LogUtils.d("VideoData**>  " , result);
        return result;
    }

    public static String base64ToString(String base64){

        String decodedString =new String(Base64.decode(base64.getBytes(), Base64.DEFAULT));
//        String decodedString = "";
//        try {
//            decodedString = URLDecoder.decode(base64,"utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return decodedString;
    }

}

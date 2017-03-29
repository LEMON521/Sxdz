package cn.net.bjsoft.sxdz.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Zrzc on 2016/12/29.
 */

public class MyBase16 {
    /*
    * 16进制数字字符集
    */
    //private static String hexString = "0123456789ABCDEF";

    /*
    * 将字符串编码成16进制数字,适用于所有字符（包括中文）
    */
    public static String str2Base16(String str) {
//根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
//将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /*
    * 将16进制数字解码成字符串,适用于所有字符（包括中文）
    */
    public static String base162Str(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2 + 1);
//将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());


//        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
////将每2位16进制整数组装成一个字节
//        for (int i = 0; i < bytes.length(); i += 2)
//            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
//        return new String(baos.toByteArray());


//        byte[] data = bytes.getBytes();
//        String getString = "";
//        for(int i = 0; i < data.length; i++){
//            getString += String.format("%02X", data[i]);
//        }
//        return getString;
    }


    private static String hexString = "0123456789ABCDEF";

    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "GB2312");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode(String str) {
        //根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        String sb = null;
        try {
            sb = new String(b, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb1 = new StringBuilder();
//        sb.replace("<p>","");
//        sb.replace("</p>","");
        return sb;
//
//
//        char[] chars = sb.toCharArray();
//        int left = 0;
//        char startPart[] = "Serial=".toCharArray();
//        for(int i=0; i < chars.length; i++) {
//            if(chars[i] == '<'){
//                int matchLen = 0;
//                for(int j=0; j<startPart.length; j++) {
//                    if(chars[i+j+1] == startPart[j]){
//                        matchLen++;
//                    }else{
//                        break;
//                    }
//                }
//
//                if(matchLen == startPart.length) {
//                    left = i;
//                }
//            }
//
//            if(left > -1 && chars[i] == '>') {
//                String data = new String(chars, left+1, i-left-1);
//                left = -1;
//                System.out.println(data);
//            }
//        }
//        String c = "";
//        for (int i = 0;i<chars.length;i++)
//        {
//           sb1.append(chars[i]);
//        }
//        return sb1.toString();
    }
}

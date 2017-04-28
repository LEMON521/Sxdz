package cn.net.bjsoft.sxdz.utils.function;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

import static javax.xml.transform.OutputKeys.ENCODING;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class ReadFile {

    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

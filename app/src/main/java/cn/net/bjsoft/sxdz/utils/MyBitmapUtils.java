package cn.net.bjsoft.sxdz.utils;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by wiky on 2016/5/26.
 */
public class MyBitmapUtils {
    private static BitmapUtils bitmapUtils;
    private Context context;
    private MyBitmapUtils(){

    }

    /**
     * 获得对象
     * @return
     */
    public static BitmapUtils getInstance(Context context) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context, AddressUtils.img_cache_url);
        }
        return bitmapUtils;
    }
}

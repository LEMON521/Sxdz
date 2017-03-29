package cn.net.bjsoft.sxdz.utils;

import android.content.Context;

/**
 * 获得手机唯一编码
 * Created by zkagang on 2016/9/14.
 */
public class DeviceIdUtils {
    /**
     * 终端型号
     * @return
     */
    public static String getDeviceId(Context context) {
        return android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }
}

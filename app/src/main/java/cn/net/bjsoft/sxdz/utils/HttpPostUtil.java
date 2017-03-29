package cn.net.bjsoft.sxdz.utils;

import com.lidroid.xutils.HttpUtils;

/**
 * 得到HttpUtils对象
 * Created by zkagang on 2016/5/19.
 */
public class HttpPostUtil {
    private static HttpUtils httpUtils;
    private HttpPostUtil(){

    }

    /**
     * 获得对象
     * @return
     */
    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
            httpUtils.configCurrentHttpCacheExpiry(1000 * 1);
            httpUtils.configTimeout(120 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
            httpUtils.configSoTimeout(120 * 1000);
        }
        return httpUtils;
    }
}

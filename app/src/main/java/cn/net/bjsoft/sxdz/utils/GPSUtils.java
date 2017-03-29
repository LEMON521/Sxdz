package cn.net.bjsoft.sxdz.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.xutils.common.util.LogUtil;

/**
 * Created by Zrzc on 2017/2/7.
 */

public class GPSUtils {

    //声明AMapLocationClient类对象
    private static AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private static AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private static AMapLocationClientOption mLocationOption = null;
    private static String address;

    public static String getAddress(Context context){
       return getAddress(context,1);
    }

    public static String getAddress(Context context, final int time){
        initGPS(context,time);
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        address = amapLocation.getAddress();
                        LogUtil.e("第几个：："+time+"$$$$$静态方法定位地址为"+amapLocation.getAddress());
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtil.e("AmapErrorlocation Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        //MyToast.showLong(context, "定位开了吗"+mLocationClient.isStarted());
        mLocationClient.unRegisterLocationListener(mLocationListener);
        return mLocationClient.getLastKnownLocation().getAddress();
    }

    public static void initGPS(Context context,int time){

            //初始化定位
            mLocationClient = new AMapLocationClient(context);
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(time * 1000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否强制刷新WIFI，默认为true，强制刷新。
            mLocationOption.setWifiActiveScan(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();

    }
}

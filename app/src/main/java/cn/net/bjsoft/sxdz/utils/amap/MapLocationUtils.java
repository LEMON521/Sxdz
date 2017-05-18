package cn.net.bjsoft.sxdz.utils.amap;

/**
 * Created by Zrzc on 2017/5/18.
 */

public class MapLocationUtils {
//
//    //声明AMapLocationClient类对象
//    private AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    private AMapLocationListener mLocationListener = null;
//    //声明AMapLocationClientOption对象
//    private AMapLocationClientOption mLocationOption = null;
//    //地理位置信息
//    private  AMapLocation mAmapLocation;
//
//
//    private MapLocationUtils(Context context) {
//
//        if (mLocationListener != null) {
//
//        } else {
//            initMapLocation(2);
//        }
//
//    }
//
//
//    public static AMapLocation getInstance(Context context) {
//        mContext = context;
//        if (mMapLocationUtils == null) {
//            mMapLocationUtils = new MapLocationUtils(context);
//        }
//        return mAmapLocation;
//    }
//
//
//    /**
//     * 初始化定位
//     */
//    private void initMapLocation(int time) {
//        mLocationListener = new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation amapLocation) {
//                if (amapLocation != null) {
//                    if (amapLocation.getErrorCode() == 0) {
//                        //可在其中解析amapLocation获取相应内容。
//                        mAmapLocation = amapLocation;
//                        SPUtil.setLat(mContext, amapLocation.getLatitude() + "");
//                        SPUtil.setLong(mContext, amapLocation.getLongitude() + "");
//                        SPUtil.setAddress(mContext, amapLocation.getAddress());
//                        //T.showShort(MainActivity.this, amapLocation.getAddress());
//                        //sendLocation(amapLocation);
//                        LogUtil.e("定位地址为" + amapLocation.getAddress());
//                        LogUtil.e("纬度" + amapLocation.getLatitude());
//                        LogUtil.e("经度" + amapLocation.getLongitude());
//                    } else {
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        LogUtil.e("AmapErrorlocation Error, ErrCode:"
//                                + amapLocation.getErrorCode() + ", errInfo:"
//                                + amapLocation.getErrorInfo());
//                    }
//                }
//            }
//        };
//        //初始化定位
//        mLocationClient = new AMapLocationClient(mContext);
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(time * 1000);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否强制刷新WIFI，默认为true，强制刷新。
//        mLocationOption.setWifiActiveScan(true);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
//    }
}

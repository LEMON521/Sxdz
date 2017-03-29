package cn.net.bjsoft.sxdz.adapter.function.sign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import org.xutils.x;

import cn.net.bjsoft.sxdz.R;

/**
 * Created by Zrzc on 2017/3/6.
 */

public class SignInfoWindowAdapter implements AMap.InfoWindowAdapter {

    private Context context;
    private View infoWindow = null;
    private ImageView imageView;//签到地点图像
    private TextView time;
    private TextView space;
    private LinearLayout ll;


    public SignInfoWindowAdapter(Context context) {
        this.context = context;
    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    public View getInfoContents(Marker marker) {
        return null;
        //示例没有采用该方法。
    }



    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(context).inflate(
                    R.layout.sign_history_marker, null);
            imageView = (ImageView) infoWindow.findViewById(R.id.marker_sign_iv);
            ll = (LinearLayout) infoWindow.findViewById(R.id.marker_sign_ll);
            time = (TextView) infoWindow.findViewById(R.id.marker_sign_tv_time);;
            space = (TextView) infoWindow.findViewById(R.id.marker_sign_tv_space);
        }



        x.image().bind(imageView,"http://api.shuxin.net/Data/biip/upload/OA_USERS/2/avatar.png");

        render(marker, infoWindow);
        return infoWindow;
        //加载custom_info_window.xml布局文件作为InfoWindow的样式
        //该布局可在官方Demo布局文件夹下找到
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改

    }
}

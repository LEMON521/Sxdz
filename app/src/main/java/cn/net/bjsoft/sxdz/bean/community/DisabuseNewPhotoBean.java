package cn.net.bjsoft.sxdz.bean.community;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/1/18.
 */

public class DisabuseNewPhotoBean implements Serializable {
    public String name = "";
    public String id = "";
    public ImageView imgItem = null;
    public Uri imgUri;
    public String imgUrl;
    //照片类型，
    // 1:默认的图片，点击之后添加一个新图片
    // 2:已选择图片，点击之后浏览图片
    public String tag = "";
}

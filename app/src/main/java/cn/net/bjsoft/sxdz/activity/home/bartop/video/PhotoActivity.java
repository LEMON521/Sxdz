package cn.net.bjsoft.sxdz.activity.home.bartop.video;

import android.os.Bundle;
import android.widget.ImageView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;


/**
 * 图片预览
 */
@ContentView(R.layout.activity_photo)
public class PhotoActivity extends BaseActivity {

    @ViewInject(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        // x.image().bind(image,getIntent().getStringExtra("url"));
        LogUtil.e("图片地址为==="+getIntent().getStringExtra("url"));
        //MyBitmapUtils.getInstance(this).display(image, getIntent().getStringExtra("url"));//访问中文文件夹时，会将中文转换成乱码，以至于找不到图片
        x.image().bind(image,getIntent().getStringExtra("url"));
        //image.setImageURI(Uri.parse(getIntent().getStringExtra("url")));
//        if (getIntent().setAction("videoTask").equals("videoTask")){
//
//        }
    }

    @Event(value = {R.id.image})
    public void photoOnClick() {
        finish();
    }
}

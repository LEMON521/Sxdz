package cn.net.bjsoft.sxdz.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;

/**
 * Created by Zrzc on 2017/4/17.
 */
@ContentView(R.layout.fragment_test)
public class TestFragment_1 extends BaseFragment {
    @ViewInject(R.id.test_tv)
    private TextView tv;
    @ViewInject(R.id.test_iv)
    private ImageView iv;

    private int count = 0;

    @Event(value = {R.id.test_tv})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.test_tv:
                count++;
                tv.setText("点击次数" + count);
                PhotoOrVideoUtils.doPhoto(mActivity, this, view);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            String imagePath = PhotoOrVideoUtils.getPath(mActivity, uri);
             x.image().bind(iv, imagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initData() {


    }

}

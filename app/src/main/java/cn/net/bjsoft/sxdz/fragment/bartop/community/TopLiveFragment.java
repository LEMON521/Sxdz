package cn.net.bjsoft.sxdz.fragment.bartop.community;

import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * 直播
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_live)
public class TopLiveFragment extends BaseFragment {

    @ViewInject(R.id.community_live_title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView test;


    @Override
    public void initData() {
        title.setText("直播");
        test.setText("直播页面");
        MyToast.showShort(mActivity,"欢迎进入直播页面");
    }

    @Event(value = {R.id.community_live_back})
    private void liveOnClick(View view) {
        switch (view.getId()) {
            case R.id.community_live_back:
                mActivity.finish();
                break;
        }
    }
}

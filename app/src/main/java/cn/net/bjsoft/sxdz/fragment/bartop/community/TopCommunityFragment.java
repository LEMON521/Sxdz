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
 * 社区
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_community)
public class TopCommunityFragment extends BaseFragment {

    @ViewInject(R.id.community_community_title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView test;


    @Override
    public void initData() {
        title.setText("社区");
        test.setText("欢迎进入聊天室");
        MyToast.showShort(mActivity,"欢迎进入聊天室");
    }

    @Event(value = {R.id.community_community_back})
    private void communityOnClick(View view) {
        switch (view.getId()) {
            case R.id.community_community_back:
                mActivity.finish();
                break;
        }
    }
}

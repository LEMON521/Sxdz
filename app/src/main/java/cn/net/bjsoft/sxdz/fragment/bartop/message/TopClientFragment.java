package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_client)
public class TopClientFragment extends BaseFragment {
    @ViewInject(R.id.message_client_title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView test;


    @Override
    public void initData() {
        title.setText("客户");
        test.setText("客户页面");
    }

    @Event(value = {R.id.message_client_back,R.id.message_client_add})
    private void clientOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_client_back:
                mActivity.finish();
                break;
            case R.id.message_client_add:
                MyToast.showShort(mActivity,"新建客户");
                break;
        }
    }
}

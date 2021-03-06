package cn.net.bjsoft.sxdz.activity.home.bartop.message;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.message.MessageListItemFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * Created by Zrzc on 2017/1/17.
 */
@ContentView(R.layout.activity_empty)
public class MessageListItemActivity  extends BaseActivity {
    private static DatasBean mDatasBean;
    private static DatasBean.DataDao mDatas;
    private static String mJson;

    private static BaseFragment fragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJson = getIntent().getStringExtra("json");
        mDatasBean = GsonUtil.getDatasBean(mJson);
        mDatas = mDatasBean.data;
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragment = new MessageListItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("json",mJson);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "MESSAGE")
                .commit();
    }
}

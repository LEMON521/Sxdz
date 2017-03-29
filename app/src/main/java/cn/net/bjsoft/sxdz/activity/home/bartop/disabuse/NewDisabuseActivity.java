package cn.net.bjsoft.sxdz.activity.home.bartop.disabuse;

import android.os.Bundle;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.community.disabuse.NewDisabuseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * 添加解惑
 * Created by Zrzc on 2017/1/12.
 */
@ContentView(R.layout.activity_empty)
public class NewDisabuseActivity extends BaseActivity {



    private static DatasBean mDatasBean;
    private static DatasBean.DataDao mDatas;
    private static String mJson;

    private static BaseFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("NewDisabuseActivity==onCreate");
        mJson = getIntent().getStringExtra("json");
        mDatasBean = GsonUtil.getDatasBean(mJson);
        mDatas = mDatasBean.data;

        fragment = new NewDisabuseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("json",mJson);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "NEWDISABUSE")
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        fragment = new NewDisabuseFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("json",mJson);
//        fragment.setArguments(bundle);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.activity_empty_root, fragment, "NEWDISABUSE")
//                .commit();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("NewDisabuseActivity===="+"onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("NewDisabuseActivity==onDestroy");
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();

        LogUtil.e("NewDisabuseActivity==onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("NewDisabuseActivity==onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("NewDisabuseActivity==onStop");
    }
}

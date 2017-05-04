package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import org.xutils.view.annotation.ContentView;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.user.TopUserFragment;
import cn.net.bjsoft.sxdz.fragment.wlecome.LoginFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * 我的页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_user)
public class UserActivity extends BaseActivity {
    private BaseFragment fragment;
    private static DatasBean mDatasBean;
    private static DatasBean.DataDao mDatas;
    private static String mJson;


    private HashMap<String, Integer> pushNum;
    private int mMyself = 0;
    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();

//    private ALiPushType3Receiver aLiPushType3Receiver =new ALiPushType3Receiver();
//
//    private ALiPushMessageInAppPopupWindow showPushWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJson = getIntent().getStringExtra("json");
        mDatasBean = GsonUtil.getDatasBean(mJson);



        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.user"));

//        registerReceiver(aLiPushType3Receiver, new IntentFilter("cn.net.bjsoft.sxdz.alipush.notify_type_3"));
//
//        aLiPushType3Receiver.setOnData(new ALiPushType3Receiver.OnGetData() {
//            @Override
//            public void onDataCallBack(Bundle bundleData) {
//                LogUtil.e("推送通知拿到数据=============="+bundleData);
//                if (bundleData!=null) {
//
//                    showPushWindow = new ALiPushMessageInAppPopupWindow(UserActivity.this,bundleData,bar);
//
//                    showPushWindow.showWindow();
//                }
//            }
//        });

        initData();
        setPushNumber();
    }

    public void setPushNumber() {

    }

    /**
     * 初始化数据
     */
    private void initData() {

        BaseFragment fragment = null;
        Bundle bundle = null;

        if (mDatasBean.data.user.logined) {
            fragment = new TopUserFragment();
        } else {
            fragment = new LoginFragment();
        }



        bundle = new Bundle();

        bundle.putString("json", mJson);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.user_content, fragment, "USER")
                .commit();
    }



    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {
        /**
         * 接收广播
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            setPushNumber();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


}

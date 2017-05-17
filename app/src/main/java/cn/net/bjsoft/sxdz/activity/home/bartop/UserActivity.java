package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.wlecome.LoginFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.MineZDLFFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 我的页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_user)
public class UserActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;


    private BaseActivity mActivity;
    private BaseFragment fragment;
    private static AppBean mDatasBean;
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
        mDatasBean = GsonUtil.getAppBean(mJson);
        mActivity = this;

        title.setText("我的");
        back.setVisibility(View.VISIBLE);

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

        if (!TextUtils.isEmpty(SPUtil.getToken(mActivity))) {
            //fragment = new TopUserFragment();
            fragment = new MineZDLFFragment();
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

    @Event(value = {R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {

            case R.id.title_back:

                finish();
                break;
        }

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

package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.bean.PushBean;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJson = getIntent().getStringExtra("json");
        mDatasBean = GsonUtil.getDatasBean(mJson);

        pushNum = app.getmPushNum();
        mMyself = pushNum.get("myself");
        LogUtil.e("UserActivity---mMyself::"+mMyself);
        setPushNumber(mMyself+"");

        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.user"));
        initData();
    }

    public void setPushNumber(String userNum) {
        app.reFreshUesrPushNumList("myself",0-Integer.parseInt(userNum.trim()));

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
            String pushJson = intent.getStringExtra("pushjson");
            PushBean bean = GsonUtil.getPushBean(pushJson);
            //LogUtil.e("社区接收到了广播@@@@@,数据为===" + pushJson);


            int approve = bean.approve;
            int bug = bean.bug;
            int community = bean.community;
            int crm = bean.crm;
            int knowledge = bean.knowledge;
            int message = bean.message;
            int myself = bean.myself;
            int payment = bean.payment;
            int proposal = bean.proposal;
            int scan = bean.scan;
            int shoot = bean.shoot;
            int signin = bean.signin;
            int task = bean.task;
            int train = bean.train;

            DatasBean.ToolbarDao toolbarDao = mDatasBean.data.toolbar;

            if (toolbarDao.train){
                app.reFreshCommunityPushNumList("train", train);
            }
            if (toolbarDao.knowledge){
                app.reFreshCommunityPushNumList("knowledge", knowledge);
            }
            if (toolbarDao.proposal){
                app.reFreshCommunityPushNumList("proposal", proposal);
            }
            if (toolbarDao.bug){
                app.reFreshCommunityPushNumList("bug", bug);
            }
            //暂时没有社区
            if (toolbarDao.community){
                app.reFreshCommunityPushNumList("community", community);
            }


            if (toolbarDao.scan){
                app.reFreshFunctionPushNumList("scan", scan);
            }
            if (toolbarDao.shoot){
                app.reFreshFunctionPushNumList("shoot", shoot);
            }
            if (toolbarDao.signin){
                app.reFreshFunctionPushNumList("signin", signin);
            }
            if (toolbarDao.payment.size()>0){
                app.reFreshFunctionPushNumList("payment", payment);
            }

            if (toolbarDao.message){
                app.reFreshMessagePushNumList("message", message);
            }
            if (toolbarDao.task){
                app.reFreshMessagePushNumList("task", task);
            }
            if (toolbarDao.crm){
                app.reFreshMessagePushNumList("crm", crm);
            }
            if (toolbarDao.approve){
                app.reFreshMessagePushNumList("approve", approve);
            }

            if (toolbarDao.myself){
                app.reFreshUesrPushNumList("myself",myself);
            }


            pushNum = app.getmPushNum();
            setPushNumber(pushNum.get("myself").toString());
//            setPushNumber(pushNum.get("message").toString(), pushNum.get("task").toString(), pushNum.get("crm").toString(), pushNum.get("approve").toString());
//            LogUtil.e("设置社区页面消息===" +  pushNum.get("proposal").toString());
            //setPushNumber(comm+"", fun+"", mess+"", user+"");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


}

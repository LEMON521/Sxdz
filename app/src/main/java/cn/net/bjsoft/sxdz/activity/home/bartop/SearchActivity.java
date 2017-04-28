package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.bean.PushBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.search.TopSearchTextFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * 搜索页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_empty)
public class SearchActivity extends BaseActivity {

    private BaseFragment fragment;
    private static DatasBean mDatasBean;
    private static DatasBean.DataDao mDatas;
    private static String mJson;
    private String searchType = "";
    private String searchContent = "";


    private HashMap<String, Integer> pushNum;
    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJson = getIntent().getStringExtra("json");
        searchType = getIntent().getStringExtra("searchType");
        Log.e("****************=",getIntent().toString());
        mDatasBean = GsonUtil.getDatasBean(mJson);
        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.function"));

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        BaseFragment fragment = null;
        Bundle bundle = null;
        bundle = new Bundle();

        fragment = new TopSearchTextFragment();
        if (searchType.equals("text")) {
            bundle.putString("content","");
        } else if (searchType.equals("speech")) {
            //fragment = new TopSearchSpeechFragment();
            searchContent = getIntent().getStringExtra("speech_contant");
            bundle.putString("content",searchContent);
        }


        bundle.putString("json", mJson);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "SEARCH")
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SearchActivity=","onStop");
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

            int approve = bean.workflow;
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

            int comm = train + knowledge + proposal + bug + community;
            int fun = scan + shoot + signin + payment;
            int mess = message + task + crm + approve;
            int user = myself;

            app.reFreshPushNumList("Community", comm);
            app.reFreshPushNumList("Function", fun);
            app.reFreshPushNumList("Message", mess);
            app.reFreshPushNumList("User", user);

            app.reFreshCommunityPushNumList("train", train);
            app.reFreshCommunityPushNumList("knowledge", knowledge);
            app.reFreshCommunityPushNumList("proposal", proposal);
            app.reFreshCommunityPushNumList("bug", bug);
            app.reFreshCommunityPushNumList("community", community);

            app.reFreshFunctionPushNumList("scan", scan);
            app.reFreshFunctionPushNumList("shoot", shoot);
            app.reFreshFunctionPushNumList("signin", signin);
            app.reFreshFunctionPushNumList("payment", payment);

            app.reFreshMessagePushNumList("message", message);
            app.reFreshMessagePushNumList("task", task);
            app.reFreshMessagePushNumList("crm", crm);
            app.reFreshMessagePushNumList("approve", approve);

            app.reFreshUesrPushNumList("myself", myself);

            pushNum = app.getmPushNum();
//            setPushNumber(pushNum.get("scan").toString(), pushNum.get("shoot").toString(), pushNum.get("signin").toString(), pushNum.get("payment").toString());
            LogUtil.e("设置社区页面消息===" + pushNum.get("proposal").toString());
            //setPushNumber(comm+"", fun+"", mess+"", user+"");
        }
    }

    //TODO 因为Activity每次执行，不管是在前台后台，可见不可见，onStart是必经之路，所以将推送的数据在这里显示最合理
    @Override
    protected void onStart() {
        super.onStart();
        pushNum = app.getmPushNum();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}

package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.bean.PushBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

import static cn.net.bjsoft.sxdz.utils.function.InitFragmentUtil.getMessageFragments;

/**
 * 消息页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseActivity {
    @ViewInject(R.id.message_content)
    private FrameLayout content;

    @ViewInject(R.id.message_bar)
    private LinearLayout bar;

    //信息
    @ViewInject(R.id.message_message)
    private RelativeLayout message;
    @ViewInject(R.id.message_message_icon)
    private ImageView message_icon;
    @ViewInject(R.id.message_message_text)
    private TextView message_text;
    @ViewInject(R.id.message_message_num)
    private TextView message_num;
    @ViewInject(R.id.message_message_img)
    private ImageView message_img;

    //任务
    @ViewInject(R.id.message_task)
    private RelativeLayout task;
    @ViewInject(R.id.message_task_icon)
    private ImageView task_icon;
    @ViewInject(R.id.message_task_text)
    private TextView task_text;
    @ViewInject(R.id.message_task_num)
    private TextView task_num;
    @ViewInject(R.id.message_task_img)
    private ImageView task_img;

    //客户
    @ViewInject(R.id.message_client)
    private RelativeLayout client;
    @ViewInject(R.id.message_client_icon)
    private ImageView client_icon;
    @ViewInject(R.id.message_client_text)
    private TextView client_text;
    @ViewInject(R.id.message_client_num)
    private TextView client_num;
    @ViewInject(R.id.message_client_img)
    private ImageView client_img;

    //审批
    @ViewInject(R.id.message_approve)
    private RelativeLayout approve;
    @ViewInject(R.id.message_approve_icon)
    private ImageView approve_icon;
    @ViewInject(R.id.message_approve_text)
    private TextView approve_text;
    @ViewInject(R.id.message_approve_num)
    private TextView approve_num;
    @ViewInject(R.id.message_approve_img)
    private ImageView approve_img;


    private ArrayList<BaseFragment> mFragmentsList;
    private ArrayList<RelativeLayout> mBarList;
    private String mJson = "";
    private DatasBean mDatasBean;

    private HashMap<String, Integer> pushNum;
    private int mMessage, mTask, mCrm, mApprove;
    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();


    private String openTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentsList = new ArrayList<BaseFragment>();
        mBarList = new ArrayList<>();
        mJson = getIntent().getStringExtra("json");
        openTag = getIntent().getStringExtra("opentag");
        LogUtil.e("message接收到的opentag为=====" + openTag);
        mDatasBean = GsonUtil.getDatasBean(mJson);

        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.message"));

        initData();
        setView();
    }

    //TODO 因为Activity每次执行，不管是在前台后台，可见不可见，onStart是必经之路，所以将推送的数据在这里显示最合理
    @Override
    protected void onStart() {

        super.onStart();
        pushNum = app.getmPushNum();

        mMessage = pushNum.get("message");
        mTask = pushNum.get("task");
        mCrm = pushNum.get("crm");
        mApprove = pushNum.get("approve");

        setPushNumber(mMessage + "", mTask + "", mCrm + "", mApprove + "");
        LogUtil.e("社区页面====" + pushNum.get("proposal").toString());
        LogUtil.e("main==onStart");
    }

    public void setPushNumber(String messageNum, String taskNum, String crmNum, String approveNum) {

        //消息
        if (Integer.parseInt(messageNum) > 0) {
            message_num.setText(messageNum);
            message_num.setVisibility(View.VISIBLE);
        } else {
            message_num.setVisibility(View.INVISIBLE);
        }
        //任务
        if (Integer.parseInt(taskNum) > 0) {
            task_num.setText(taskNum);
            task_num.setVisibility(View.VISIBLE);
        } else {
            task_num.setVisibility(View.INVISIBLE);
        }
        //客户
        if (Integer.parseInt(crmNum) > 0) {
            client_num.setText(crmNum);
            client_num.setVisibility(View.VISIBLE);
        } else {
            client_num.setVisibility(View.INVISIBLE);
        }
        //审批
        if (Integer.parseInt(approveNum) > 0) {
            approve_num.setText(approveNum);
            approve_num.setVisibility(View.VISIBLE);
        } else {
            approve_num.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mFragmentsList = getMessageFragments(mJson);
    }

    /**
     * 显示数据
     */
    private void setView() {
        setMessageVisibility();
        //如果没有发送消息 的任务，那么就让他显示默认的
        if (openTag.equals("") || openTag.equals("defult")) {
            int seclect = mFragmentsList.size() / 2 + mFragmentsList.size() % 2 - 1;
            //onClick(mBarList.get(seclect));
            onClick(mBarList.get(0));
        } else {
            //onClick(mBarList.get(openTag));
            for (int i = 0; i < mFragmentsList.size(); i++) {
//            MyToast.showShort(this,"是否为：："+(v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())));
                //字符串判定   一定要用equals！！！
                if (openTag.equals(mFragmentsList.get(i).getArguments().get("tag").toString())) {
                    onClick(mBarList.get(i));
                }
            }
        }

    }

    /**
     * 设置Community按钮的显示与否
     */
    private void setMessageVisibility() {
        {
            message.setVisibility(View.GONE);
            message_img.setVisibility(View.GONE);

            task.setVisibility(View.GONE);
            task_img.setVisibility(View.GONE);

            client.setVisibility(View.GONE);
            client_img.setVisibility(View.GONE);

            approve.setVisibility(View.GONE);
            approve_img.setVisibility(View.GONE);

        }
        for (int i = 0; i < mFragmentsList.size(); i++) {

            if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((message.getTag().toString()))) {
                message.setVisibility(View.VISIBLE);
                message_img.setVisibility(View.VISIBLE);
                mBarList.add(message);
            } else if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((task.getTag().toString()))) {
                task.setVisibility(View.VISIBLE);
                task_img.setVisibility(View.VISIBLE);
                mBarList.add(task);
            } else if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((client.getTag().toString()))) {
                client.setVisibility(View.VISIBLE);
                client_img.setVisibility(View.VISIBLE);
                mBarList.add(client);
            } else if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((approve.getTag().toString()))) {
                approve.setVisibility(View.VISIBLE);
                approve_img.setVisibility(View.VISIBLE);
                mBarList.add(approve);
            }
        }

        //如果只有一个页面，那么让底部栏隐藏掉
        if (mFragmentsList.size() == 1) {
            bar.setVisibility(View.GONE);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 点击事件
     *
     * @param v
     */
    @Event(type = View.OnClickListener.class, value = {R.id.message_message, R.id.message_task, R.id.message_client, R.id.message_approve})
    private void onClick(View v) {
        for (int i = 0; i < mFragmentsList.size(); i++) {
//            MyToast.showShort(this,"是否为：："+(v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())));
            //字符串判定   一定要用equals！！！
            if (v.getTag().toString().equals(mFragmentsList.get(i).getArguments().get("tag").toString())) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.message_content, mFragmentsList.get(i), mFragmentsList.get(i).getArguments().get("tag").toString())
                        .commit();
            }
        }

        //MyToast.showShort(context, "点击了" + v.toString());
        setDefault();
        switch (v.getId()) {

            case R.id.message_message:
                message_icon.setImageResource(R.drawable.xinxi_s);
                message_text.setTextColor(getResources().getColor(R.color.blue));
                message_num.setText("");
                message_num.setVisibility(View.INVISIBLE);
                app.reFreshMessagePushNumList("message", 0 - mMessage);
                break;
            case R.id.message_task:
                task_icon.setImageResource(R.drawable.renwu_s);
                task_text.setTextColor(getResources().getColor(R.color.blue));
                task_num.setText("");
                task_num.setVisibility(View.INVISIBLE);
                app.reFreshMessagePushNumList("task", 0 - mTask);
                break;
            case R.id.message_client:
                client_icon.setImageResource(R.drawable.clientele_s);
                client_text.setTextColor(getResources().getColor(R.color.blue));
                client_num.setText("");
                client_num.setVisibility(View.INVISIBLE);
                app.reFreshMessagePushNumList("crm", 0 - mCrm);
                break;
            case R.id.message_approve:
                approve_icon.setImageResource(R.drawable.shenpi_s);
                approve_text.setTextColor(getResources().getColor(R.color.blue));
                approve_num.setText("");
                approve_num.setVisibility(View.INVISIBLE);
                app.reFreshMessagePushNumList("approve", 0 - mApprove);
                break;
        }
    }

    /**
     * 设置底部未选中的图片为默认颜色
     */
    private void setDefault() {
        message_icon.setImageResource(R.drawable.xinxi_n);
        message_text.setTextColor(getResources().getColor(R.color.gray));

        task_icon.setImageResource(R.drawable.renwu_n);
        task_text.setTextColor(getResources().getColor(R.color.gray));

        client_icon.setImageResource(R.drawable.clientele_n);
        client_text.setTextColor(getResources().getColor(R.color.gray));

        approve_icon.setImageResource(R.drawable.shenpi_n);
        approve_text.setTextColor(getResources().getColor(R.color.gray));
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
            DatasBean.ToolbarDao toolbarDao = mDatasBean.data.toolbar;

            if (toolbarDao.train) {
                app.reFreshCommunityPushNumList("train", train);
            }
            if (toolbarDao.knowledge) {
                app.reFreshCommunityPushNumList("knowledge", knowledge);
            }
            if (toolbarDao.proposal) {
                app.reFreshCommunityPushNumList("proposal", proposal);
            }
            if (toolbarDao.bug) {
                app.reFreshCommunityPushNumList("bug", bug);
            }
            if (toolbarDao.community){
                app.reFreshCommunityPushNumList("community", community);
            }


            if (toolbarDao.scan) {
                app.reFreshFunctionPushNumList("scan", scan);
            }
            if (toolbarDao.shoot) {
                app.reFreshFunctionPushNumList("shoot", shoot);
            }
            if (toolbarDao.signin) {
                app.reFreshFunctionPushNumList("signin", signin);
            }
            if (toolbarDao.payment.size() > 0) {
                app.reFreshFunctionPushNumList("payment", payment);
            }

            if (toolbarDao.message) {
                app.reFreshMessagePushNumList("message", message);
            }
            if (toolbarDao.task) {
                app.reFreshMessagePushNumList("task", task);
            }
            if (toolbarDao.crm) {
                app.reFreshMessagePushNumList("crm", crm);
            }
            if (toolbarDao.approve) {
                app.reFreshMessagePushNumList("approve", approve);
            }

            if (toolbarDao.myself) {
                app.reFreshUesrPushNumList("myself", myself);
            }


            pushNum = app.getmPushNum();
            setPushNumber(pushNum.get("message").toString(), pushNum.get("task").toString(), pushNum.get("crm").toString(), pushNum.get("approve").toString());
            LogUtil.e("设置社区页面消息===" + pushNum.get("proposal").toString());
            //setPushNumber(comm+"", fun+"", mess+"", user+"");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

package cn.net.bjsoft.sxdz.app;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.utils.SPJpushUtil;

//import com.alibaba.sdk.android.push.CloudPushService;
//import com.alibaba.sdk.android.push.CommonCallback;
//import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

/**
 * Created by jnn on 2017/1/3.
 */
public class MyApplication extends MultiDexApplication implements Serializable {

    //顶部栏每个按钮的推送数量
    private int mCommunityNum = 0;
    private int mFunctionNum = 0;
    private int mMessageNum = 0;
    private int mUserNum = 0;

    private int train;
    private int knowledge;
    private int proposal;
    private int bug;
    private int community;

    private int scan;
    private int shoot;
    private int signin;
    private int payment;

    private int message;
    private int task;
    private int crm;
    private int approve;

    private int myself;

    public Context context;

    private HashMap<String, Integer> mPushNum;

    public ArrayList<Activity> mActivityList;
    public int mOsVersion = 0;
    public int mCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();


        //xutils3.0初始化
        x.Ext.init(this);
        x.Ext.setDebug(true); //输出debug日志，开启会影响性能

        //极光推送初始化
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);

        //阿里推送初始化
        initCloudChannel(this);


        //初始化语音搜索
        // SpeechUtility.createUtility(context, SpeechConstant.APPID +"=58845eeb");

        LogUtil.e("==============开始初始化===============");
        context = getApplicationContext();
        LogUtil.e("==============初始化完毕===============");
        mActivityList = new ArrayList<>();
        mOsVersion = getAndroidOSVersion();
        mCount++;
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();


        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e("==============初始化推送成功init cloudchannel success===============");
                //cloudPushService.getDeviceId();
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtil.e("===========初始化推送失败init cloudchannel failed -- errorcode:========" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    /**
     * @return
     */
    public HashMap<String, Integer> getmPushNum() {
        if (mPushNum == null) {
            mPushNum = new HashMap<String, Integer>();

            train = SPJpushUtil.getTrain(context);
            knowledge = SPJpushUtil.getKnowledge(context);
            proposal = SPJpushUtil.getProposal(context);
            bug = SPJpushUtil.getBug(context);
            community = SPJpushUtil.getCommunity(context);

            scan = SPJpushUtil.getScan(context);
            shoot = SPJpushUtil.getShoot(context);
            signin = SPJpushUtil.getSignin(context);
            payment = SPJpushUtil.getPayment(context);

            message = SPJpushUtil.getMessage(context);
            task = SPJpushUtil.getTask(context);
            crm = SPJpushUtil.getCrm(context);
            approve = SPJpushUtil.getApprove(context);

            myself = SPJpushUtil.getMyself(context);

            mCommunityNum = train + knowledge + proposal + bug + community;
            mFunctionNum = scan + shoot + signin + payment;
            mMessageNum = message + task + crm + approve;
            mUserNum = myself;

            mPushNum.put("Community", mCommunityNum);
            mPushNum.put("Function", mFunctionNum);
            mPushNum.put("Message", mMessageNum);
            mPushNum.put("User", mUserNum);

            mPushNum.put("train", train);
            mPushNum.put("knowledge", knowledge);
            mPushNum.put("proposal", proposal);
            mPushNum.put("bug", bug);
            mPushNum.put("community", community);

            mPushNum.put("scan", scan);
            mPushNum.put("shoot", shoot);
            mPushNum.put("signin", signin);
            mPushNum.put("payment", payment);

            mPushNum.put("message", message);
            mPushNum.put("task", task);
            mPushNum.put("crm", crm);
            mPushNum.put("approve", approve);

            mPushNum.put("myself", myself);

        }
        return mPushNum;
    }

    /**
     * 总数（首页的数量）
     *
     * @param key
     * @param value
     */
    public void reFreshPushNumList(String key, int value) {
        //replace不能兼容所有的平台，所以只有使用笨方法了
        int community = mPushNum.get("train") +
                mPushNum.get("knowledge") +
                mPushNum.get("proposal") +
                mPushNum.get("bug") +
                mPushNum.get("community");

        int function = mPushNum.get("scan") +
                mPushNum.get("shoot") +
                mPushNum.get("signin") +
                mPushNum.get("payment");

        int message = mPushNum.get("message") +
                mPushNum.get("task") +
                mPushNum.get("crm") +
                mPushNum.get("approve");

        int user = mPushNum.get("myself");


        if (key.equals("Community")) {
            //int old = mPushNum.get("community");
            mPushNum.remove("Community");
            mPushNum.put("Community", value/*+old*/);
        } else if (key.equals("Function")) {
            int old = mPushNum.get("Function");
            mPushNum.remove("Function");
            mPushNum.put("Function", value/*+old*/);
        } else if (key.equals("Message")) {
            int old = mPushNum.get("Message");
            mPushNum.remove("Message");
            mPushNum.put("Message", value/*+old*/);
        } else if (key.equals("User")) {
            int old = mPushNum.get("User");
            mPushNum.remove("User");
            mPushNum.put("User", value/*+old*/);
        }

    }

    /**
     * 社区页面数量
     *
     * @param key
     * @param value
     */
    public void reFreshCommunityPushNumList(String key, int value) {
        //replace不能兼容所有的平台，所以只有使用笨方法了
        int community = mPushNum.get("train") +
                mPushNum.get("knowledge") +
                mPushNum.get("proposal") +
                mPushNum.get("bug") +
                mPushNum.get("community");
        if (key.equals("train")) {
            int old = mPushNum.get("train");
            mPushNum.remove("train");
            mPushNum.put("train", value /*+ old*/);
            reFreshPushNumList("Community", community + value);
        } else if (key.equals("knowledge")) {
            int old = mPushNum.get("knowledge");
            mPushNum.remove("knowledge");
            mPushNum.put("knowledge", value /*+ old*/);
            reFreshPushNumList("Community", community + value);
        } else if (key.equals("proposal")) {
            int old = mPushNum.get("proposal");
            mPushNum.remove("proposal");
            mPushNum.put("proposal", value/*+ old*/);
            reFreshPushNumList("Community", community + value);
        } else if (key.equals("bug")) {
            int old = mPushNum.get("bug");
            mPushNum.remove("bug");
            mPushNum.put("bug", value /*+ old*/);
            reFreshPushNumList("Community", community + value);
        } else if (key.equals("community")) {
            int old = mPushNum.get("community");
            mPushNum.remove("community");
            mPushNum.put("community", value /*+ old*/);
            reFreshPushNumList("Community", community + value);
        }
    }

    /**
     * 社区页面数量
     *
     * @param key
     * @param value
     */
    public void reFreshFunctionPushNumList(String key, int value) {
        //replace不能兼容所有的平台，所以只有使用笨方法了
        int function = mPushNum.get("scan") +
                mPushNum.get("shoot") +
                mPushNum.get("signin") +
                mPushNum.get("payment");
        if (key.equals("scan")) {
            int old = mPushNum.get("scan");
            mPushNum.remove("scan");
            mPushNum.put("scan", value /*+ old*/);
            reFreshPushNumList("Function", function + value);
        } else if (key.equals("shoot")) {
            int old = mPushNum.get("shoot");
            mPushNum.remove("shoot");
            mPushNum.put("shoot", value /*+ old*/);
            reFreshPushNumList("Function", function + value);
        } else if (key.equals("signin")) {
            int old = mPushNum.get("signin");
            mPushNum.remove("signin");
            mPushNum.put("signin", value /*+ old*/);
            reFreshPushNumList("Function", function + value);
        } else if (key.equals("payment")) {
            int old = mPushNum.get("payment");
            mPushNum.remove("payment");
            mPushNum.put("payment", value /*+ old*/);
            reFreshPushNumList("Function", function + value);
        }

    }

    /**
     * 消息页面数量
     *
     * @param key
     * @param value
     */
    public void reFreshMessagePushNumList(String key, int value) {
        //replace不能兼容所有的平台，所以只有使用笨方法了
        int message = mPushNum.get("message") +
                mPushNum.get("task") +
                mPushNum.get("crm") +
                mPushNum.get("approve");
        if (key.equals("message")) {
            int old = mPushNum.get("message");
            mPushNum.remove("message");
            mPushNum.put("message", value/*+ old*/);
            reFreshPushNumList("Message", message + value);
        } else if (key.equals("task")) {
            int old = mPushNum.get("task");
            mPushNum.remove("task");
            mPushNum.put("task", value /*+ old*/);
            reFreshPushNumList("Message", message + value);
        } else if (key.equals("crm")) {
            int old = mPushNum.get("crm");
            mPushNum.remove("crm");
            mPushNum.put("crm", value /*+ old*/);
            reFreshPushNumList("Message", message + value);
        } else if (key.equals("approve")) {
            int old = mPushNum.get("approve");
            mPushNum.remove("approve");
            mPushNum.put("approve", value /*+ old*/);
            reFreshPushNumList("Message", message + value);
        }

    }

    /**
     * 消息页面数量
     *
     * @param key
     * @param value
     */
    public void reFreshUesrPushNumList(String key, int value) {
        //replace不能兼容所有的平台，所以只有使用笨方法了
        int user = mPushNum.get("User");
        if (key.equals("myself")) {
            int old = mPushNum.get("myself");
            mPushNum.remove("myself");
            mPushNum.put("myself", value /*+ old*/);
            reFreshPushNumList("Uesr", user + value);
        }
    }


    public static int getAndroidOSVersion() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            osVersion = 0;
        }

        return osVersion;
    }


}

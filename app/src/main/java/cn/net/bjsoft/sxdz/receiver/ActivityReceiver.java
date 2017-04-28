package cn.net.bjsoft.sxdz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.xutils.common.util.LogUtil;

import cn.net.bjsoft.sxdz.bean.PushBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * Created by Zrzc on 2017/1/23.
 */

public class ActivityReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String pushJson = intent.getStringExtra("pushjson");
        PushBean bean = GsonUtil.getPushBean(pushJson);
        LogUtil.e("接收到了广播$$$$$$$,数据为===" + pushJson);

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
//
////            int comm = train + knowledge + proposal + bug + community;
////            int fun = scan + shoot + signin + payment;
////            int mess = message + task + crm + approve;
////            int user = myself;
//
////            app.reFreshPushNumList("Community", comm);
////            app.reFreshPushNumList("Function", fun);
////            app.reFreshPushNumList("Message", mess);
////            app.reFreshPushNumList("User", user);
//
//        DatasBean.ToolbarDao toolbarDao = mDatasBean.data.toolbar;
//
//        if (toolbarDao.train){
//            app.reFreshCommunityPushNumList("train", train);
//        }
//        if (toolbarDao.knowledge){
//            app.reFreshCommunityPushNumList("knowledge", knowledge);
//        }
//        if (toolbarDao.proposal){
//            app.reFreshCommunityPushNumList("proposal", proposal);
//        }
//        if (toolbarDao.bug){
//            app.reFreshCommunityPushNumList("bug", bug);
//        }
//        //暂时没有社区
////            if (toolbarDao.community){
////                app.reFreshCommunityPushNumList("community", community);
////            }
//
//
//        if (toolbarDao.scan){
//            app.reFreshFunctionPushNumList("scan", scan);
//        }
//        if (toolbarDao.shoot){
//            app.reFreshFunctionPushNumList("shoot", shoot);
//        }
//        if (toolbarDao.signin){
//            app.reFreshFunctionPushNumList("signin", signin);
//        }
//        if (toolbarDao.payment.size()>0){
//            app.reFreshFunctionPushNumList("payment", payment);
//        }
//
//        if (toolbarDao.message){
//            app.reFreshMessagePushNumList("message", message);
//        }
//        if (toolbarDao.task){
//            app.reFreshMessagePushNumList("task", task);
//        }
//        if (toolbarDao.crm){
//            app.reFreshMessagePushNumList("crm", crm);
//        }
//        if (toolbarDao.approve){
//            app.reFreshMessagePushNumList("approve", approve);
//        }
//
//        if (toolbarDao.myself){
//            app.reFreshUesrPushNumList("myself",myself);
//        }
//
//        pushNum = app.getmPushNum();
//        setPushNumber(pushNum.get("Community"), pushNum.get("Function"), pushNum.get("Message"), pushNum.get("User"));
//
//        //setPushNumber(comm+"", fun+"", mess+"", user+"");

    }
}

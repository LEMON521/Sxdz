package cn.net.bjsoft.sxdz.utils;

import android.content.Context;
import android.content.Intent;

import org.xutils.common.util.LogUtil;

import cn.net.bjsoft.sxdz.bean.PushBean;

/**
 * Created by Zrzc on 2017/1/19.
 */

public class BroadcastCallUtil {
//    public static int approve;
//    public int bug;
//    public int community;
//    public int crm;
//    public int knowledge;
//    public int message;
//    public int myself;
//    public int payment;
//    public int proposal;
//    public int scan;
//    public int shoot;
//    public int signin;
//    public int task;
//    public int train;

    public static void sendMessage2Activity(Context context, String json, PushBean bean) {
        LogUtil.e("自定义消息的数量：：：：" + bean.workflow + bean.community);

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

        approve = approve + SPJpushUtil.getApprove(context);
        bug = bug + SPJpushUtil.getBug(context);
        community = community + SPJpushUtil.getCommunity(context);
        crm = crm + SPJpushUtil.getCrm(context);
        knowledge = knowledge + SPJpushUtil.getKnowledge(context);
        message = message + SPJpushUtil.getMessage(context);
        myself = myself + SPJpushUtil.getMyself(context);
        payment = payment + SPJpushUtil.getPayment(context);
        proposal = proposal + SPJpushUtil.getProposal(context);
        scan = scan + SPJpushUtil.getScan(context);
        shoot = shoot + SPJpushUtil.getShoot(context);
        signin = signin + SPJpushUtil.getSignin(context);
        task = task + SPJpushUtil.getTask(context);
        train = train + SPJpushUtil.getTrain(context);

        SPJpushUtil.setApprove(context, approve);
        SPJpushUtil.setBug(context, bug);
        SPJpushUtil.setCommunity(context, community);
        SPJpushUtil.setCrm(context, crm);
        SPJpushUtil.setKnowledge(context, knowledge);
        SPJpushUtil.setMessage(context, message);
        SPJpushUtil.setMyself(context, myself);
        SPJpushUtil.setPayment(context, payment);
        SPJpushUtil.setProposal(context, proposal);
        SPJpushUtil.setScan(context, scan);
        SPJpushUtil.setShoot(context, shoot);
        SPJpushUtil.setSignin(context, signin);
        SPJpushUtil.setTask(context, task);
        SPJpushUtil.setTrain(context, train);


//        approve = bean.approve;
        if (bean.train > 0 || bean.knowledge > 0 || bean.proposal > 0 || bean.bug > 0 || bean.community > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.community");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }
        if (bean.scan > 0 || bean.shoot > 0 || bean.signin > 0 || bean.payment > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.function");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }
        if (bean.message > 0 || bean.task > 0 || bean.crm > 0 || bean.workflow > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.message");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }
        if (bean.myself > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.user");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }

        Intent main = new Intent();
        main.setAction("cn.net.bjsoft.sxdz.main");
        main.putExtra("pushjson", json);
        context.sendBroadcast(main);
//        if (bean.comm.get(bean.comm.indexOf("bug")).bug.size() > 0 ||
//                bean.comm.get(bean.comm.indexOf("community")).community.size() > 0 ||
//                bean.comm.get(bean.comm.indexOf("knowledge")).knowledge.size() > 0 ||
//                bean.comm.get(bean.comm.indexOf("proposal")).proposal.size() > 0 ||
//                bean.comm.get(bean.comm.indexOf("train")).train.size() > 0) {
//
//            int a = Integer.parseInt((bean.comm.get(bean.comm.indexOf("bug")).bug.get(bean.comm.indexOf("num")).num));
//            int b = Integer.parseInt((bean.comm.get(bean.comm.indexOf("community")).community.get(bean.comm.indexOf("num")).num));
//            int c = Integer.parseInt((bean.comm.get(bean.comm.indexOf("knowledge")).knowledge.get(bean.comm.indexOf("num")).num));
//            int d = Integer.parseInt((bean.comm.get(bean.comm.indexOf("proposal")).proposal.get(bean.comm.indexOf("num")).num));
//            int e = Integer.parseInt((bean.comm.get(bean.comm.indexOf("train")).train.get(bean.comm.indexOf("num")).num));
//            int f = a+b+c+d+e;
//            LogUtil.e("community----发送消息"+f);
//        }
//
//        if (bean.fun.get(bean.fun.indexOf("payment")).payment.size() > 0 ||
//                bean.fun.get(bean.fun.indexOf("scan")).scan.size() > 0 ||
//                bean.fun.get(bean.fun.indexOf("shoot")).shoot.size() > 0 ||
//                bean.fun.get(bean.fun.indexOf("signin")).signin.size() > 0) {
//            LogUtil.e("function----发送消息");
//        }
//
//        if (bean.mess.get(bean.mess.indexOf("approve")).approve.size() > 0 ||
//                bean.mess.get(bean.mess.indexOf("crm")).crm.size() > 0 ||
//                bean.mess.get(bean.mess.indexOf("message")).message.size() > 0 ||
//                bean.mess.get(bean.mess.indexOf("task")).task.size() > 0) {
//            LogUtil.e("message----发送消息");
//        }
//
//        if (bean.user.get(bean.user.indexOf("myself")).myself.size() > 0) {
//            LogUtil.e("myself----发送消息");
//        }

    }


//{"cumm":{"bug":{"num":1},"community":{"num":1},"knowledge":{"num":1},"proposal":{"num":1},"train":{"num":1}}}


}

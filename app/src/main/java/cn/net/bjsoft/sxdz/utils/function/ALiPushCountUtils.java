package cn.net.bjsoft.sxdz.utils.function;

import android.content.Context;
import android.content.Intent;

import cn.net.bjsoft.sxdz.bean.app.AppPushBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;

/**
 * Created by Zrzc on 2017/5/3.
 */

public class ALiPushCountUtils {


    public static void setPushCount(Context context, String json) {
        json = json.replace("\"\"", "0");

        AppPushBean pushBean = GsonUtil.getAppPushBean(json);

        //LogUtil.e(pushBean.测试数据+"================测试数据为===============");

        ////////////////////////////////顶部栏//////////////////////////////
        SPJpushUtil.setTrain(context, pushBean.train);
        SPJpushUtil.setKnowledge(context, pushBean.knowledge);
        SPJpushUtil.setProposal(context, pushBean.proposal);
        SPJpushUtil.setBug(context, pushBean.bug);
        SPJpushUtil.setCommunity(context, pushBean.community);
        SPJpushUtil.setScan(context, pushBean.scan);
        SPJpushUtil.setShoot(context, pushBean.shoot);
        SPJpushUtil.setSignin(context, pushBean.signin);
        SPJpushUtil.setPayment(context, pushBean.payment);
        SPJpushUtil.setMessage(context, pushBean.message);
        SPJpushUtil.setTask(context, pushBean.task);
        SPJpushUtil.setCrm(context, pushBean.crm);
        SPJpushUtil.setApprove(context, pushBean.workflow);
        SPJpushUtil.setMyself(context, pushBean.myself);

        /////////////////////////工作模块/////////////////////////
        SPJpushUtil.setProject(context, pushBean.project);
        SPJpushUtil.setEngineering(context, pushBean.engineering);
        SPJpushUtil.setContract(context, pushBean.contract);
        SPJpushUtil.setMarketchannel(context, pushBean.marketchannel);
        SPJpushUtil.setSalereport(context, pushBean.salereport);
        SPJpushUtil.setProjectstat(context, pushBean.projectstat);
        SPJpushUtil.setEngineeringlog(context, pushBean.engineeringlog);
        SPJpushUtil.setEmergency(context, pushBean.emergency);
        SPJpushUtil.setEngineeringeval(context, pushBean.engineeringeval);
        SPJpushUtil.setConstruction(context, pushBean.construction);
        SPJpushUtil.setConstructionteam(context, pushBean.constructionteam);
        SPJpushUtil.setWeekplan(context, pushBean.weekplan);
        SPJpushUtil.setCompanyrun(context, pushBean.companyrun);
        SPJpushUtil.setSitemsg(context, pushBean.sitemsg);


        /////////////////////////底部栏/////////////////////////
        SPJpushUtil.setHome_zdlf(context, pushBean.home_zdlf);
        SPJpushUtil.setWork_items(context, pushBean.work_items);
        SPJpushUtil.setKnowledge_zdlf(context, pushBean.knowledge_zdlf);
        SPJpushUtil.setCommunication_zdlf(context, pushBean.communication_zdlf);
        SPJpushUtil.setMine_zdlf(context, pushBean.mine_zdlf);

        if (pushBean.train > 0 || pushBean.knowledge > 0 || pushBean.proposal > 0 || pushBean.bug > 0 || pushBean.community > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.community");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }
        if (pushBean.scan > 0 || pushBean.shoot > 0 || pushBean.signin > 0 || pushBean.payment > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.function");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }
        if (pushBean.message > 0 || pushBean.task > 0 || pushBean.crm > 0 || pushBean.workflow > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.message");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }
        if (pushBean.myself > 0) {
            Intent i = new Intent();
            i.setAction("cn.net.bjsoft.sxdz.user");
            i.putExtra("pushjson", json);
            context.sendBroadcast(i);
        }

        Intent main = new Intent();
        main.setAction("cn.net.bjsoft.sxdz.main");
        main.putExtra("pushjson", json);
        context.sendBroadcast(main);

        /**
         * 中电联发工作模块
         */
        Intent work_items = new Intent();
        work_items.setAction("cn.net.bjsoft.sxdz.fragment_work");
        work_items.putExtra("pushjson", json);
        context.sendBroadcast(work_items);


        /**
         * 主页面--底部栏推送更新
         */
        Intent main_bottom_bar = new Intent();
        main_bottom_bar.setAction("cn.net.bjsoft.sxdz.main.bottombar");
        main_bottom_bar.putExtra("pushjson", json);
        context.sendBroadcast(main_bottom_bar);

    }
}

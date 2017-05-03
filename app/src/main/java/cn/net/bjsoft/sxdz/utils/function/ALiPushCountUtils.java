package cn.net.bjsoft.sxdz.utils.function;

import android.content.Context;

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


    }
}

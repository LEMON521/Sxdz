package cn.net.bjsoft.sxdz.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zrzc on 2017/1/20.
 */

public class SPJpushUtil {

    private static SharedPreferences sp;
    static SharedPreferences.Editor editor;
    private static String JPUSH_NUM = "jushNum";

    //=========================直播===train=========================
    /**得到直播未读数量*/
    public static int getTrain(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("train", 0);
    }
    /**得到直播未读数量*/
    public static boolean getTrainState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("train_statey", false);
    }

    /**设置直播数量*/
    public static void setTrain(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("train", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("train_state", num>0);
        editor.commit();
    }


//=========================帮助===knowledge=========================
    /**得到帮助未读数量*/
    public static int getKnowledge(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("knowledge", 0);
    }
    /**得到帮助未读数量*/
    public static boolean getKnowledgeState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("knowledge_statey", false);
    }

    /**设置帮助数量*/
    public static void setKnowledge(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("knowledge", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("knowledge_state", num>0);
        editor.commit();
    }



//=========================建议===proposal=========================
    /**得到建议未读数量*/
    public static int getProposal(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("proposal", 0);
    }
    /**得到建议未读数量*/
    public static boolean getProposalState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("proposal_statey", false);
    }

    /**设置建议数量*/
    public static void setProposal(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("proposal", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("proposal_statey", num>0);
        editor.commit();
    }

//=========================报错===bug=========================
    /**得到报错未读数量*/
    public static int getBug(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("bug", 0);
    }
    /**得到报错未读数量*/
    public static boolean getBugState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("bug_statey", false);
    }

    /**设置报错数量*/
    public static void setBug(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("bug", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("bug_statey", num>0);
        editor.commit();
    }


//=========================社区===community=========================
    /**得到社区未读数量*/
    public static int getCommunity(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("community", 0);
    }
    /**得到社区未读数量*/
    public static boolean getCommunityState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("community_statey", false);
    }

    /**设置社区数量*/
    public static void setCommunity(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("community", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("community_statey", num>0);
        editor.commit();
    }


//=========================扫一扫===scan=========================
    /**得到扫一扫未读数量*/
    public static int getScan(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("scan", 0);
    }
    /**得到扫一扫未读数量*/
    public static boolean getScanState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("scan_statey", false);
    }

    /**设置扫一扫数量*/
    public static void setScan(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("scan", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("scan_statey", num>0);
        editor.commit();
    }

//=========================拍摄===shoot=========================
    /**得到拍摄未读数量*/
    public static int getShoot(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("shoot", 0);
    }
    /**得到拍摄未读数量*/
    public static boolean getShootState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("shoot_statey", false);
    }

    /**设置拍摄数量*/
    public static void setShoot(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("shoot", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("shoot_statey", num>0);
        editor.commit();
    }

//=========================签到===signin=========================
    /**得到签到未读数量*/
    public static int getSignin(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("signin", 0);
    }
    /**得到签到未读数量*/
    public static boolean getSigninState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("signin_statey", false);
    }

    /**设置签到数量*/
    public static void setSignin(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("signin", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("signin_statey", num>0);
        editor.commit();
    }


//=========================支付===payment=========================
    /**得到支付未读数量*/
    public static int getPayment(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("payment", 0);
    }
    /**得到支付未读数量*/
    public static boolean getPaymentState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("payment_statey", false);
    }

    /**设置支付数量*/
    public static void setPayment(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("payment", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("payment_statey", num>0);
        editor.commit();
    }



//=========================消息===message=========================
    /**得到消息未读数量*/
    public static int getMessage(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("message", 0);
    }
    /**得到消息未读数量*/
    public static boolean getMessageState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("message_statey", false);
    }

    /**设置消息数量*/
    public static void setMessage(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("message", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("message_statey", num>0);
        editor.commit();
    }


//=========================任务===task=========================
    /**得到任务未读数量*/
    public static int getTask(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("task", 0);
    }
    /**得到任务未读数量*/
    public static boolean getTaskState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("task_statey", false);
    }

    /**设置任务数量*/
    public static void setTask(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("task", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("task_statey", num>0);
        editor.commit();
    }


//=========================客户===crm=========================
    /**得到客户未读数量*/
    public static int getCrm(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("crm", 0);
    }
    /**得到客户未读数量*/
    public static boolean getCrmState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("crm_statey", false);
    }

    /**设置客户数量*/
    public static void setCrm(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("crm", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("crm_statey", num>0);
        editor.commit();
    }


//=========================审批===approve=========================
    /**得到审批未读数量*/
    public static int getApprove(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("approve", 0);
    }
    /**得到审批未读数量*/
    public static boolean getApproveState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("approve_statey", false);
    }

    /**设置审批数量*/
    public static void setApprove(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("approve", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("approve_statey", num>0);
        editor.commit();
    }



//=========================用户===myself=========================
    /**得到用户未读数量*/
    public static int getMyself(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("myself", 0);
    }
    /**得到用户未读数量*/
    public static boolean getMyselfState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("myself_statey", false);
    }

    /**设置用户数量*/
    public static void setMyself(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("myself", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("myself_statey", num>0);
        editor.commit();
    }


    //=========================项目管理===project=========================

    /**得到--项目管理--未读数量*/
    public static int getProject(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("project", 0);
    }
    /**得到--项目管理--未读数量状态*/
    public static boolean getProjectState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("project_statey", false);
    }

    /**设置--项目管理--数量*/
    public static void setProject(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("project", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("project_statey", num>0);
        editor.commit();
    }


    //=========================工程管理===engineering=========================

    /**得到--工程管理--未读数量*/
    public static int getEngineering(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("engineering", 0);
    }
    /**得到--工程管理--未读数量状态*/
    public static boolean getEngineeringState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("engineering_statey", false);
    }

    /**设置--工程管理--数量*/
    public static void setEngineering(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("engineering", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("engineering_statey", num>0);
        editor.commit();
    }


    //=========================合同管理===contract=========================

    /**得到--合同管理--未读数量*/
    public static int getContract(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("contract", 0);
    }
    /**得到--合同管理--未读数量状态*/
    public static boolean getContractState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("contract_statey", false);
    }

    /**设置--合同管理--数量*/
    public static void setContract(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("contract", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("contract_statey", num>0);
        editor.commit();
    }

    //=========================市场通道===marketchannel=========================

    /**得到--市场通道--未读数量*/
    public static int getMarketchannel(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("marketchannel", 0);
    }
    /**得到--市场通道--未读数量状态*/
    public static boolean getMarketchannelState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("marketchannel_statey", false);
    }

    /**设置--市场通道--数量*/
    public static void setMarketchannel(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("marketchannel", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("marketchannel_statey", num>0);
        editor.commit();
    }

    //=========================销售日报===salereport=========================

    /**得到--销售日报--未读数量*/
    public static int getSalereport(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("salereport", 0);
    }
    /**得到--销售日报--未读数量状态*/
    public static boolean getSalereportState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("salereport_statey", false);
    }

    /**设置--销售日报--数量*/
    public static void setSalereport(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("salereport", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("salereport_statey", num>0);
        editor.commit();
    }


    //=========================项目统计===projectstat=========================

    /**得到--项目统计--未读数量*/
    public static int getProjectstat(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("projectstat", 0);
    }
    /**得到--项目统计--未读数量状态*/
    public static boolean getProjectstatState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("projectstat_statey", false);
    }

    /**设置--项目统计--数量*/
    public static void setProjectstat(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("projectstat", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("projectstat_statey", num>0);
        editor.commit();
    }


    //=========================工程日志===engineeringlog=========================

    /**得到--工程日志--未读数量*/
    public static int getEngineeringlog(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("engineeringlog", 0);
    }
    /**得到--工程日志--未读数量状态*/
    public static boolean getEngineeringlogState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("engineeringlog_statey", false);
    }

    /**设置--工程日志--数量*/
    public static void setEngineeringlog(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("engineeringlog", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("engineeringlog_statey", num>0);
        editor.commit();
    }


    //=========================紧急情况===emergency=========================

    /**得到--紧急情况--未读数量*/
    public static int getEmergency(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("emergency", 0);
    }
    /**得到--紧急情况--未读数量状态*/
    public static boolean getEmergencyState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("emergency_statey", false);
    }

    /**设置--紧急情况--数量*/
    public static void setEmergency(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("emergency", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("emergency_statey", num>0);
        editor.commit();
    }


    //=========================工程评价===engineeringeval=========================

    /**得到--工程评价--未读数量*/
    public static int getEngineeringeval(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("engineeringeval", 0);
    }
    /**得到--工程评价--未读数量状态*/
    public static boolean getEngineeringevalState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("engineeringeval_statey", false);
    }

    /**设置--工程评价--数量*/
    public static void setEngineeringeval(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("engineeringeval", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("engineeringeval_statey", num>0);
        editor.commit();
    }

    //=========================施工能力===construction=========================

    /**得到--施工能力--未读数量*/
    public static int getConstruction(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("construction", 0);
    }
    /**得到--施工能力--未读数量状态*/
    public static boolean getConstructionState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("construction_statey", false);
    }

    /**设置--施工能力--数量*/
    public static void setConstruction(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("construction", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("construction_statey", num>0);
        editor.commit();
    }



    //=========================施工队===constructionteam=========================

    /**得到--施工队--未读数量*/
    public static int getConstructionteam(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("constructionteam", 0);
    }
    /**得到--施工队--未读数量状态*/
    public static boolean getConstructionteamState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("constructionteam_statey", false);
    }

    /**设置--施工队--数量*/
    public static void setConstructionteam(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("constructionteam", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("constructionteam_statey", num>0);
        editor.commit();
    }

    //=========================周计划===weekplan=========================

    /**得到--周计划--未读数量*/
    public static int getWeekplan(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("weekplan", 0);
    }
    /**得到--周计划--未读数量状态*/
    public static boolean getWeekplanState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("weekplan_statey", false);
    }

    /**设置--周计划--数量*/
    public static void setWeekplan(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("weekplan", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("weekplan_statey", num>0);
        editor.commit();
    }


    //=========================公司运营===companyrun=========================

    /**得到--公司运营--未读数量*/
    public static int getCompanyrun(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("companyrun", 0);
    }
    /**得到--公司运营--未读数量状态*/
    public static boolean getCompanyrunState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("companyrun_statey", false);
    }

    /**设置--公司运营--数量*/
    public static void setCompanyrun(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("companyrun", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("companyrun_statey", num>0);
        editor.commit();
    }

    //=========================站内信===sitemsg=========================

    /**得到--站内信--未读数量*/
    public static int getSitemsg(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getInt("sitemsg", 0);
    }
    /**得到--站内信--未读数量状态*/
    public static boolean getSitemsgState(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        return sp.getBoolean("sitemsg_statey", false);
    }

    /**设置--站内信--数量*/
    public static void setSitemsg(Context context, int num) {
        sp = context.getApplicationContext().getSharedPreferences(JPUSH_NUM, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("sitemsg", num);
        //num>0有数据   num<0没有数据
        editor.putBoolean("sitemsg_statey", num>0);
        editor.commit();
    }

}

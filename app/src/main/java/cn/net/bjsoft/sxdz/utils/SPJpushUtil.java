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

}

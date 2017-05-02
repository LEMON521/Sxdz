package cn.net.bjsoft.sxdz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Zrzc on 2017/5/2.
 */

public class ALiPushType2Receiver extends BroadcastReceiver {


    //---------------社区--------------

    /**
     * 直播
     */
    public String train;
    /**
     * 帮助
     */
    public String knowledge;
    /**
     * 建议
     */
    public String proposal;
    /**
     * 解惑报错
     */
    public String bug;
    /**
     * 社区
     */
    public String community;//社区

    //--------------------功能------------------
    /**
     * 扫一扫
     */
    public String scan;
    /**
     * 拍照/拍摄任务
     */
    public String shoot;
    /**
     * 签到
     */
    public String signin;
    /**
     * 支付
     */
    public String payment;

    //---------------------信息--------------
    /**
     * 信息
     */
    public String message;
    /**
     * 任务
     */
    public String task;
    /**
     * 客户
     */
    public String crm;
    /**
     * 审批
     */
    public String workflow;//审批

    //-------------------我的----------------
    /**
     * 我的
     */
    public String myself;

    ///////////////////////////////////////////////
    public String calendar;
    //public int crm;
    public String email;
    //public int message;
    public String notice;
    public String order;
    public String sign;


    @Override
    public void onReceive(Context context, Intent intent) {


    }
}

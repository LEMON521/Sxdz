package cn.net.bjsoft.sxdz.bean.app;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class AppPushBean implements Serializable {

    //---------------社区--------------

    /**
     * 直播
     */
    public int train = 0;
    /**
     * 帮助
     */
    public int knowledge = 0;
    /**
     * 建议
     */
    public int proposal = 0;
    /**
     * 解惑报错
     */
    public int bug = 0;
    /**
     * 社区
     */
    public int community = 0;//社区

    //--------------------功能------------------
    /**
     * 扫一扫
     */
    public int scan = 0;
    /**
     * 拍照/拍摄任务
     */
    public int shoot = 0;
    /**
     * 签到
     */
    public int signin = 0;
    /**
     * 支付
     */
    public int payment = 0;

    //---------------------信息--------------
    /**
     * 信息
     */
    public int message = 0;
    /**
     * 任务
     */
    public int task = 0;
    /**
     * 客户
     */
    public int crm = 0;
    /**
     * 审批
     */
    public int workflow = 0;//审批

    //-------------------我的----------------
    /**
     * 我的
     */
    public int myself = 0;

    ///////////////////////////////////////////////


    /**
     * 工作模块
     */

    //-------------------主要应用----------------
    public int project = 0;//项目管理
    public int engineering = 0;//工程管理
    public int contract = 0;//合同管理
    public int marketchannel = 0;//市场通道
    //-------------------项目管理----------------
    public int salereport = 0;//销售日报
    public int projectstat = 0;//项目统计
    //-------------------日常工作----------------
    public int engineeringlog = 0;//工程日志
    public int emergency = 0;//紧急情况
    public int engineeringeval = 0;//工程评价
    public int construction = 0;//施工能力
    public int constructionteam = 0;//施工队
    //-------------------其他应用----------------
    public int weekplan = 0;//周计划
    public int companyrun = 0;//公司运营
    public int sitemsg = 0;//站内信


    ////////////////////////底部栏//////////////////////
    public int home_zdlf = 0;//主页--中电联发
    public int work_items = 0;//工作--中电联发
    public int knowledge_zdlf = 0;//知识--中电联发
    public int communication_zdlf = 0;//通讯--中电联发
    public int mine_zdlf = 0;//我的--中电联发

}

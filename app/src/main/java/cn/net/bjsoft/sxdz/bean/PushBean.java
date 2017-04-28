package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;


/**
 * 用于接收极光推送的解析的bean
 * Created by Zrzc on 2017/1/19.
 */

public class PushBean implements Serializable {

    //---------------社区--------------

    /**
     * 直播
     */
    public int train;
    /**
     * 帮助
     */
    public int knowledge;
    /**
     * 建议
     */
    public int proposal;
    /**
     * 解惑报错
     */
    public int bug;
    /**
     * 社区
     */
    public int community;//社区

    //--------------------功能------------------
    /**
     * 扫一扫
     */
    public int scan;
    /**
     * 拍照/拍摄任务
     */
    public int shoot;
    /**
     * 签到
     */
    public int signin;
    /**
     * 支付
     */
    public int payment;

    //---------------------信息--------------
    /**
     * 信息
     */
    public int message;
    /**
     * 任务
     */
    public int task;
    /**
     * 客户
     */
    public int crm;
    /**
     * 审批
     */
    public int workflow;//审批

    //-------------------我的----------------
    /**
     * 我的
     */
    public int myself;

///////////////////////////////////////////////
    public int calendar;
    //public int crm;
    public int email;
    //public int message;
    public int notice;
    public int order;
    public int sign;
    //public int task;
    //public int workflow;



//    public ArrayList<CommBean> comm;//社区
//    public ArrayList<FunBean> fun;//功能
//    public ArrayList<MessBean> mess;//消息
//    public ArrayList<UserBean> user;//用户
//
//    /**
//     * 社区
//     */
//    public class CommBean{
//        public BugBean bug;//解惑报错
//        public CommunityBean community;//社区
//        public KnowledgeBean knowledge;//帮助
//        public ProposalBean proposal;//建议
//        public TrainBean train;//直播
//
//    }
//
//    public class BugBean{
//        public String num;
//    }
//
//    public class CommunityBean{
//        public String num;
//    }
//
//    public class KnowledgeBean{
//        public String num;
//    }
//
//    public class ProposalBean{
//        public String num;
//    }
//
//    public class TrainBean{
//        public String num;
//    }
//
//    /**
//     * 功能
//     */
//    public class FunBean{
//        public PaymentBean payment;//支付
//        public ScanBean scan;//扫一扫
//        public ShootBean shoot;//拍摄
//        public SigninBean signin;//签到
//
//    }
//    public class PaymentBean{
//        public String num;
//    }
//
//    public class ScanBean{
//        public String num;
//    }
//
//    public class ShootBean{
//        public String num;
//    }
//
//    public class SigninBean{
//        public String num;
//    }
//    /**
//     * 消息
//     */
//    public class MessBean{
//        public ApproveBean approve;//审批
//        public CrmBean crm;//客户
//        public MessageBean message;//消息
//        public TaskBean task;//任务
//
//
//
//    }
//
//    public class ApproveBean{
//        public String num;
//    }
//
//    public class CrmBean{
//        public String num;
//    }
//
//    public class MessageBean{
//        public String num;
//    }
//
//    public class TaskBean{
//        public String num;
//    }
//
//    /**
//     * 用户
//     */
//    public class UserBean{
//        public MyselfBean myself;//用户
//
//
//
//    }
//    public class MyselfBean{
//        public String num;
//    }


}

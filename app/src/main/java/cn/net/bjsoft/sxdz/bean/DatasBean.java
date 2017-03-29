package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用来接收网络json的实体类
 * Created by lemon on 2016/12/20.
 */

public class DatasBean implements Serializable {
    public boolean success;
    public DataDao data;

    public class DataDao implements Serializable {
        public boolean authentication;
        public String company_id = "";
        public ArrayList<HomepageDao> homepage;
        public String id = "";
        public ArrayList<LoadersDao> loaders;
        public LoginDao login;
        public String logo = "";
        public String position = "";
        public PushDao push;
        public PushdataDao pushdata;
        public PushkeysDao pushkeys;
        public String title = "";
        public ToolbarDao toolbar;
        public UserDao user = new UserDao();
        public String waiting = "";
    }

    public class HomepageDao implements Serializable {
//        public String icon;
//        public String icon_default;
//        public String icon_position;
//        public String icon_selected;
//        public String linkto;
//        public String objname;
//        public String text;
//        public String url;

        public String text = "";
        public String icon = "";
        public String icon_default = "";
        public String icon_selected = "";
        public String icon_position = "";
        public String selected = "";
        public String url = "";
        public String tag = "";
        public String linkto = "";
        public String title = "";

    }

    public class LoadersDao implements Serializable {
        public String description = "";
        public String imgurl = "";
        public String jumpto = "";
        public String linkto = "";
        public String text = "";
    }

    public class LoginDao implements Serializable {
        public String background = "";
        public String btntext = "";
        public boolean canregister = false;
        public String loginurl = "";
        public boolean resetpassword = false;
        public boolean ismember = false;

    }

    public class PushDao implements Serializable {
        public String devapi = "";
        public String devkey = "";
        public String devsecret = "";
        public String name = "";
    }

    public class PushdataDao implements Serializable {
        //TODO服务端返回的都是   String —— int类型的数据
        public String approve_count = "";//需要审批的工作流简数
        public String customer_count = "";//要拜访的客户数量
        public String message_count = "";//到达未处理的总信息数
        public String order_count = "";//需要支付的订单数
        public String schedule_count = "";//需要今天处理的日程安排
        public String shoot_count = "";//当前用户需要处理的事项计数
        public SignLast sign_last;//最后一次签到信息，签到地址，签到时间
        public boolean sign_need = false;//需要马上签到，红点提示
        public String task_count = "";//需要办理未完成的任务数
        public String train_count = "";//需要拍摄采集数据的任务
    }

    public class SignLast implements Serializable {

        public String address = "";
        public String signtime = "";
    }


    public class PushkeysDao implements Serializable {
        public String sign_need = "";
        public String sign_last = "";
        public String order_count = "";
        public String task_count = "";
        public String approve_count = "";
        public String message_count = "";
        public String schedule_count = "";
        public String shoot_count = "";
        public String train_count = "";
        public String customer_count = "";
    }

    public class ToolbarDao implements Serializable {
        public boolean approve = false;//是否有审批标签
        public boolean bug = false;//是否有报错标签
        public boolean crm = false;//是否有客户管理
        public boolean knowledge = false;//是否有帮助标签
        public boolean message = false;//是否有信息标签
        public boolean myself = false;//是否有我的按钮
        public ArrayList<Paymentdao> payment;//支付方式
        public boolean proposal = false;//是否有建议标签
        public boolean scan = false;//是否有扫一扫标签
        public String scan_to = "";//不为空就是网页地址，扫一扫到这个网页地址
        public boolean search_by_voice = false;//是否有语音搜索
        public boolean search_by_word = false;//是否显示输入文字搜索
        public String search_to = "";//不为空就是网页地址，搜索参数传到这个网页地址
        public boolean shoot = false;//是否有拍摄任务标签
        public boolean signin = false;//是否有签到标签
        public boolean task = false;//是否启用任务标签
        public boolean train = false;//是否有直播培训标签
        public boolean community = false;//是否有社区标签
    }

    public class UserDao implements Serializable {
        public String asname = "";
        public String avatar = "";
        public String birthday = "";
        public String email = "";
        public boolean ismember = false;
        public boolean logined = false;//验证登录名(登录状态)
        public String name = "";
        public String phone = "";
        public String phone_uuid = "";
        public String randcode = "";//本次验证随机码
        public String user_id = "";
        public String uuid = "";
    }

    private class Paymentdao implements Serializable {
        public String accoutn = "";//将要支付的账号
        public String name = "";//将要支付的名称
        public String password = "";//密码
    }
}

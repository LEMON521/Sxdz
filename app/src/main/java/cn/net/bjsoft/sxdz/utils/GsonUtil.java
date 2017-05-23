package cn.net.bjsoft.sxdz.utils;

import com.google.gson.Gson;

import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.bean.PushBean;
import cn.net.bjsoft.sxdz.bean.UpdateBean;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.AppPushBean;
import cn.net.bjsoft.sxdz.bean.app.ali_push.ALiPushMessageBean;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowGroupBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsBean;
import cn.net.bjsoft.sxdz.bean.app.function.sign.SignUserLastBean;
import cn.net.bjsoft.sxdz.bean.app.logined.LoginedBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailBeanNew;
import cn.net.bjsoft.sxdz.bean.app.user.UserBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersAllBean;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveShowDao;
import cn.net.bjsoft.sxdz.bean.function.sign.FunctionSignHistoryBean;
import cn.net.bjsoft.sxdz.bean.message.MessageMessageBean;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailBean;
import cn.net.bjsoft.sxdz.bean.ylyd.form.YLYDFormDao;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;
import cn.net.bjsoft.sxdz.bean.zdlf.work.WorkBean;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListBean;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.ListTaskBean;

/**
 * Created by zkagang on 2016/1/8.
 */
public class GsonUtil {
    private static Gson gson;
    private static DatasBean datasBean;
    public static Gson getGson(){
        if(gson==null){
            gson=new Gson();
        }
        return gson;
    }


    /**
     * 总数据json
     * @param json
     * @return
     */
    public static AppBean getAppBean(String json){
        return getGson().fromJson(json , AppBean.class);
//        return datasBean;
    }

    /**
     * 已登录json
     * @param json
     * @return
     */
    public static LoginedBean getLoginedBean(String json){
        return getGson().fromJson(json , LoginedBean.class);
//        return datasBean;
    }

    /**
     * 已登录user json
     * @param json
     * @return
     */
    public static UserBean getUserBean(String json){
        return getGson().fromJson(json , UserBean.class);
//        return datasBean;
    }




    /**
     * 总数据json
     * @param json
     * @return
     */
    public static DatasBean getDatasBean(String json){
        return getGson().fromJson(json , DatasBean.class);
//        return datasBean;
    }

    /**
     * 获取推送json
     * @param json
     * @return
     */
    public static PushBean getPushBean(String json){
        return getGson().fromJson(json , PushBean.class);
//        return datasBean;
    }



    /**
     * 获取升级json
     * @param json
     * @return
     */
    public static UpdateBean getUpdateBean(String json){
        return getGson().fromJson(json , UpdateBean.class);
    }

    public static ApproveShowDao getApproveShowBean(String json){
        return getGson().fromJson(json , ApproveShowDao.class);
    }

    /**
     * 获取审批json
     * @param json
     * @return
     */
    public static ApproveDatasDao getApproveDatasBean(String json){
        return getGson().fromJson(json , ApproveDatasDao.class);
    }

    /**
     * 誉龙亚东报表json
     * @param json
     * @return
     */
    public static YLYDFormDao getYLYDFormBean(String json){
        return getGson().fromJson(json , YLYDFormDao.class);
    }

    /**
     * 签到历史json
     * @param json
     * @return
     */
    public static FunctionSignHistoryBean getSignLastBean(String json){
        return getGson().fromJson(json , FunctionSignHistoryBean.class);
    }


    /**
     * 中电联发工作模块json
     * @param json
     * @return
     */
    public static WorkBean getWorkBean(String json){
        return getGson().fromJson(json , WorkBean.class);
    }

    /**
     * 中电联发知识模块json
     * @param json
     * @return
     */
    public static KnowGroupBean getKnowGroupBean(String json){
        return getGson().fromJson(json , KnowGroupBean.class);
    }

    /**
     * 中电联发知识模块json
     * @param json
     * @return
     */
    public static KnowledgeBean.GroupBean getKnowledgeGroupBean(String json){
        return getGson().fromJson(json , KnowledgeBean.GroupBean.class);
    }

    /**
     * 中电联发知识条目模块json
     * @param json
     * @return
     */
    public static KnowItemsBean getKnowledgeItemsBean(String json){
        return getGson().fromJson(json , KnowItemsBean.class);
    }

    /**
     * 中电联发知识条目详情模块json
     * @param json
     * @return
     */
    public static KnowItemBean getKnowledgeItemsItemBean(String json){
        return getGson().fromJson(json , KnowItemBean.class);
    }

    /**
     * 中电联发知识条目详情模块json
     * @param json
     * @return
     */
    public static AddressListBean getAddressListBean(String json){
        return getGson().fromJson(json , AddressListBean.class);
    }

    /**
     * 中电联发信息模块-任务--模块json
     * @param json
     * @return
     */
    public static MessageTaskBean getMessageTaskBean(String json){
        return getGson().fromJson(json , MessageTaskBean.class);
    }


    /**
     * 中电联发信息模块-消息列表--模块json
     * @param json
     * @return
     */
    public static MessageMessageBean getMessageMessageBean(String json){
        return getGson().fromJson(json , MessageMessageBean.class);
    }

    /**
     * 中电联发信息模块-任务详情--模块json
     * @param json
     * @return
     */
    public static MessageTaskDetailBean getMessageTaskDetailBean(String json){
        return getGson().fromJson(json , MessageTaskDetailBean.class);
    }

    /**
     * 中电联发信息模块-任务详情--模块json
     * @param json
     * @return
     */
    public static MessageTaskDetailBeanNew getMessageTaskDetailBeanNew(String json){
        return getGson().fromJson(json , MessageTaskDetailBeanNew.class);
    }

    /**
     * 中电联发信息模块-下属任务-模块json
     * @param json
     * @return
     */
    public static ListTaskBean getListTaskBean(String json){
        return getGson().fromJson(json , ListTaskBean.class);
    }

    /**
     * 中电联发信息模块-下属任务-模块json
     * @param json
     * @return
     */
    public static TreeTaskAddAddressListBean getTreeTaskAddAddressListBean(String json){
        return getGson().fromJson(json , TreeTaskAddAddressListBean.class);
    }

    /**
     * 联系人组织架构json
     * @param json
     * @return
     */
    public static AddressBean getAddressBean(String json){
        return getGson().fromJson(json , AddressBean.class);
    }
    /**
     * 推送消息
     * @param json
     * @return
     */
    public static ALiPushMessageBean getALiPushMessageBean(String json){
        return getGson().fromJson(json , ALiPushMessageBean.class);
    }


    /**
     * 获取推送json
     * @param json
     * @return
     */
    public static AppPushBean getAppPushBean(String json){
        return getGson().fromJson(json , AppPushBean.class);
//        return datasBean;
    }

    /**
     * 获取推送json
     * @param json
     * @return
     */
    public static UsersAllBean getUsersAllBean(String json){
        return getGson().fromJson(json , UsersAllBean.class);
//        return datasBean;
    }


    /**
     * 获取审批json
     * @param json
     * @return
     */
    public static MessageApproveBean getMessageApproveBean(String json){
        return getGson().fromJson(json , MessageApproveBean.class);
//        return datasBean;
    }

    /**
     * 获取--FunctionFormBean --json
     * @param json
     * @return
     */
    public static FunctionFormBean getFunctionFormBean(String json){
        return getGson().fromJson(json , FunctionFormBean.class);
//        return datasBean;
    }

    /**
     * 获取--FunctionFormBean --json
     * @param json
     * @return
     */
    public static SignUserLastBean getSignUserLastBean(String json){
        return getGson().fromJson(json , SignUserLastBean.class);
//        return datasBean;
    }


}

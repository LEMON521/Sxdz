package cn.net.bjsoft.sxdz.bean.message;

import java.io.Serializable;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;

/**
 * Created by Zrzc on 2017/4/5.
 */

public class MessageTaskDetailBean implements Serializable {
    public boolean result = false;//
    public MessageTaskDetailDao data;//全部数据

    public class MessageTaskDetailDao implements Serializable {
        //        public ArrayList<AttachmentDao> attachment_list;
//        public ArrayList<AttachmentDao> attachment_list_add;
        public ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> attachment_list;//发起人附件列表
        public ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> attachment_list_add;//执行人附件列表

        public int degree = 0;//任务执行程度
        public String detail = "";//任务详情描述
        public TasksDao executor;//任务概述(顶部的栏目集合)
        public ArrayList<DetailAddDao> detail_list;//详情集合
        //public ArrayList<MessageTaskDetailAddBean> detail_list;


    }

    public class TasksDao implements Serializable {
        public int state = -1;//状态
        public String title = "";//标题
        public String classify = "";//分类
        public ArrayList<AddressListDao> name;//发起人名字
        public long start = 0;//开始时间--时间戳
        public long end = 0;//结束时间--时间戳
        public int level = -1;//任务等级
        public boolean isOverdue = false;//是否过期//
    }

    public class AddressListDao implements Serializable {
        public String name = "";
    }

    public class AttachmentDao implements Serializable {
        public String file_url = "";
        public String file_path = "";
        public String file_name = "";
        public boolean isEditing = false;
        public boolean isAdd = false;
    }

    /**
     * 添加详情
     */
    public class DetailAddDao implements Serializable {
        public String detail_description = "";//执行人的任务描述
        public String detail_title = "";//执行人的任务标题
        public boolean isEditing = false;//是否可编辑状态

    }
}

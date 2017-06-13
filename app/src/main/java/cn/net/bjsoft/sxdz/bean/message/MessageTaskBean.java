package cn.net.bjsoft.sxdz.bean.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 消息页面,任务页面   任务条目的Bean
 * Created by Zrzc on 2017/4/5.
 */

public class MessageTaskBean implements Serializable {
    public String code = "1";
    public TasksDao data;


    public class TasksDao implements Serializable {
        public String count = "0";
        public ArrayList<TasksAllDao> items;
        public TaskQueryDao query_dao;
        public String query_type;

    }

    public class TaskQueryDao implements Serializable {
        public String time_start = "";
        public String time_end = "";
        public ArrayList<TaskQueryTypeDao> type_list = new ArrayList<>();
        public ArrayList<TaskQueryLevelDao> level_list = new ArrayList<>();
    }

    public class TaskQueryTypeDao implements Serializable {
        public String type = "";
    }

    public class TaskQueryLevelDao implements Serializable {
        public String level = "";
    }

    public class TasksAllDao implements Serializable {
        public boolean alert = false;
        //        public int state = -1;//状态
//        public String title = "";//标题
//        public String classify = "";//分类
//        public ArrayList<AddressListDao> name;//发起人名字
//        public long start = 0;//开始时间--时间戳
//        public long end = 0;//结束时间--时间戳
        public int level = -1;//任务等级
        //        public boolean isOverdue = false;//是否过期
//        public int type = 0;//0发起人---要执行的任务,1执行人----看任务
        public int is_executant = 0;//0发起人---要执行的任务,1执行人----看任务
        public String url = "";

        public String ctime = "";
        public String description = "";
        public boolean finished = false;
        public String finishtime = "";
        public String hours = "";
        public String id = "";
        public String limittime = "";
        public String priority = "";//任务等级
        public String priority_color = "";//任务等级字体颜色
        public String progress = "";
        public String starttime = "";
        public String title = "";
        public String type = "";
        public String userid = "";
        public ArrayList<TasksAllWorkDao> worker;
    }

    public class TasksAllWorkDao implements Serializable {
        public String submited = "";
        public String userid = "";
        public String id = "";
    }

    public class AddressListDao implements Serializable {
        public String name = "";
    }

}

package cn.net.bjsoft.sxdz.bean.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 消息页面,任务页面   任务条目的Bean
 * Created by Zrzc on 2017/4/5.
 */

public class MessageTaskBean implements Serializable {
    public boolean result = false;
    public ArrayList<TasksDao> data;

    public class TasksDao implements Serializable {
        public int state = -1;//状态
        public String title = "";//标题
        public String classify = "";//分类
        public String name = "";//发起人名字
        public long start = 0;//开始时间--时间戳
        public long end = 0;//结束时间--时间戳
        public int level = -1;//任务等级
        public boolean isOverdue = false;//是否过期
    }
}

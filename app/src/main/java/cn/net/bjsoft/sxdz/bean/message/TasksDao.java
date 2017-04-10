package cn.net.bjsoft.sxdz.bean.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/10.
 */

public class TasksDao implements Serializable {
    public int state = -1;//状态
    public String title = "";//标题
    public String classify = "";//分类
    public ArrayList<AddressListDao> name;//发起人名字
    public long start = 0;//开始时间--时间戳
    public long end = 0;//结束时间--时间戳
    public int level = -1;//任务等级
    public boolean isOverdue = false;//是否过期

    public class AddressListDao implements Serializable {
        public String name = "";
    }
}

package cn.net.bjsoft.sxdz.bean.zdlf.knowledge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/17.
 */

public class KnowledgeBean implements Serializable {
    public boolean result = false;//返回状态
    public KnowledgeDataDao data;
    //public ArrayList<GroupDataDao> group;

    public class KnowledgeDataDao implements Serializable {
        public ArrayList<GroupDataDao> group;

    }

    ////////////////////////////////////////////
    public class GroupBean implements Serializable {
        public boolean result = false;//返回状态
        public ArrayList<GroupDataDao> group;
    }

    public class GroupDataDao implements Serializable {
        public String name;//分组名称
        public String type;//分组类型
        public String url;//url地址
        public ArrayList<ItemsDataDao> items;//,写个组的详细信息
    }

    ////////////////////////////////////////////
    public class ItemsBean implements Serializable {
        public boolean result = false;//返回状态
        public ArrayList<ItemsDataDao> items;
    }

    public class ItemsDataDao implements Serializable {
        public String image_url = "";
        public String author = "";//作者
        public String category = "";//分类
        public String date = "";//日期
        public String lookover_count = "";//查看数量
        public String reply_count = "";//回复数量
        public String title = "";//标题

    }
}

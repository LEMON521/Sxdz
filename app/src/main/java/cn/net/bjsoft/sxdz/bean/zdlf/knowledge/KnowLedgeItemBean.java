package cn.net.bjsoft.sxdz.bean.zdlf.knowledge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/20.
 */

public class KnowLedgeItemBean implements Serializable {

    public boolean result = false;//返回结果特征
    public KnowledgeItemData data;//数据

    /**
     * 封装的数据
     */
    public class KnowledgeItemData implements Serializable {
        public String title = "";//标题
        //public ArrayList<KnowledgeItemDao> knowledge_item;//回复列表
        public ArrayList<ReplyListDao> knowledge_item;//回复列表

    }

//    /**
//     * 楼层信息
//     */
//    public class KnowledgeItemDao implements Serializable {
//        public String avatar_url = "";//回复人的头像地址
//        public String description = "";//回复内容
//        public String name = "";//回复人的名字
//        public String time = "";//回复时间.时间戳
//        public String reply_to = "";//向谁回复
//        public ArrayList<ReplyListDao> reply_list;//回复列表信息
//
//    }

    /**
     * 楼层内回复的条目信息
     */
    public class ReplyListDao implements Serializable {
        public String avatar_url = "";//回复人的头像地址
        public String comment_text = "";//回复内容
        public String name = "";//回复人的名字
        public String time = "";//回复时间.时间戳
        public String reply_to = "";//向谁回复
        public ArrayList<ReplyListDao> reply_list;//回复列表信息
    }
}

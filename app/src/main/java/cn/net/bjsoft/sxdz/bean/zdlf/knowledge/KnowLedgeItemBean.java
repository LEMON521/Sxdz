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
        public HostKnowledgeItemDao host;
        //public ArrayList<KnowledgeItemDao> knowledge_item;//回复列表
        public ArrayList<ReplyListDao> knowledge_item;//回复列表

    }

    /**
     * 楼主的信息
     */
    public class HostKnowledgeItemDao implements Serializable {
        public String title = "";//楼主标题
        public String avatar = "";//楼主头像
        public String name = "";//楼主名字
        public String mark = "";//文章标签
        public String content = "";//文章内容
        public ArrayList<FilesKnowledgeItemDao> files;//附件列表
        public String time = "";//回复人的名字文章发布时间
        public String check_count = "";//文章查看数
        public String reply_count = "";//文章回复数
    }

    public class FilesKnowledgeItemDao implements Serializable {
        public String file_url = "";
        public String file_name = "";
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

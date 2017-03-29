package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;

/**
 * Created by Zrzc on 2016/12/28.
 */

public class NewArticaleBean implements Serializable {
    public String success;
    public NewsArticleData data;

    public class NewsArticleData{
        public String author;
        public String class_id;
        public String comefrom;
        public String contents;//正文
        public String ctime;
        public String en_eval;
        public String en_revi;
        public String id;//文章id
        public String image;
        public String images;
        public String keywords;
        public String limittime;
        public String name;
        public String publiced;
        public String publictime;//发布时间
        public String root_company_id;
        public String starttime;
        public String subcaption;
        public String summary;
        public String user_id;
    }
}

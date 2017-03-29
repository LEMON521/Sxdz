package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2016/12/23.
 */

public class NewsItemsBean implements Serializable {
    public int count;
    public ArrayList<NewsData> data;
    public boolean success;
    public String feedback;

    public class NewsData implements Serializable {
        public String author;
        public String comefrom;
        public String id;
        public String keywords;
        public String image;
        public String images;
        public String name;
        public String publictime;
        public String subcaption;
        public String summary;
    }
}

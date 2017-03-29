package cn.net.bjsoft.sxdz.bean.ylyd.form;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/10.
 */

public class YLYDFormDao implements Serializable {
    public boolean result;//图片地址
    public FormDataBean data;//轮播图集合


    public class FormDataBean {
        public ArrayList<ScrollPageBean> scroll_list;//轮播图集合
        public ArrayList<TreeListBean> tree_list;
    }

    public class ScrollPageBean {
        public String image_url;//图片地址
        public String file_url;//附件地址
        public String file_text;//附件提示信息
    }

    public class TreeListBean{
        public ArrayList<TreeListBean> children;
        public String id = "";
        public String name = "";
        public String pid = "";
        public String url = "";
    }

//
//    public class TreeBean {
//        public ArrayList<ChildrenDao> children;
//        public String id = "";
//        public String name = "";
//        public String pid = "";
//    }
//
//    public class ChildrenDao{
////        public ArrayList<TreeBean>
//    }
}

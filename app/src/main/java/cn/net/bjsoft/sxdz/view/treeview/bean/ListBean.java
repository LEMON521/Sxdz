package cn.net.bjsoft.sxdz.view.treeview.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lemon on 2017/3/12.
 */

public class ListBean implements Serializable {

    public String result = "";
    public DataDao data;

    public class DataDao{

        public ArrayList<ScrollListDao> scroll_list;
        public ArrayList<TreeListDao> tree_list;

    }

    public class TreeListDao{
        public ArrayList<TreeListDao> children;
        public String id = "";
        public String name = "";
        public String pid = "";
        public String url = "";
    }

//    public class ChildListDao{
//
//        public ArrayList<ChildListDao> chindren;
//
//    }


    public class ScrollListDao{
        public String file_text = "";
        public String file_url = "";
        public String image_url = "";
    }
}

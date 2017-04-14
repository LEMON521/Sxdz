package cn.net.bjsoft.sxdz.view.tree_task_underling.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lemon on 2017/3/12.
 */

public class ListTaskBean implements Serializable {

    public boolean result = false;
    public TaskDataDao data;

    public class TaskDataDao{

        public ArrayList<ScrollListDao> scroll_list;
        public ArrayList<TreeTaskListDao> tree_list;

    }

    public class TreeTaskListDao{
        public ArrayList<TreeTaskListDao> children;
        public String id = "";
        public String name = "";
        public String pid = "";
        public String url = "";
        public String department = "";
        public String task_num = "";
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

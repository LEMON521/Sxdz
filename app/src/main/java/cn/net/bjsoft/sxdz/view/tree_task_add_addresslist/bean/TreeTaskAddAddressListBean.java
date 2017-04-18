package cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lemon on 2017/3/12.
 */

public class TreeTaskAddAddressListBean implements Serializable {

    public boolean result = false;
    public DataDao data;

    public class DataDao {

        public ArrayList<TreeTaskAddAddressListDao> tree_task_add_address_list;

    }

    public class TreeTaskAddAddressListDao {
        public ArrayList<TreeTaskAddAddressListDao> children;
        public String id = "";
        public String pid = "";
        public String name = "";
        public String type = "";
        public String humen_num = "";
        public String avatar = "";
        public String department = "";
    }

}

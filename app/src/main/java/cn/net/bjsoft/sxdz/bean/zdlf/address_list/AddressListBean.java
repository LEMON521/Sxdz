package cn.net.bjsoft.sxdz.bean.zdlf.address_list;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/27.
 */

public class AddressListBean implements Serializable {
    public boolean result;//返回码
    public DataDao data;//联系人类

    public class DataDao{

        public ArrayList<AddressListDao> address_list;

    }

    public class AddressListDao implements Serializable {
        public ArrayList<AddressListDao> children;
        public String id = "";
        public String name = "";
        public String pid = "";
        public String url = "";
        public String station = "";
        public String phone_number = "";
        public String type = "";
        public String avatar_url = "";

    }
}

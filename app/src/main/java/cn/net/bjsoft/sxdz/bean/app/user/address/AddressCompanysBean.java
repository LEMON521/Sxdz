package cn.net.bjsoft.sxdz.bean.app.user.address;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/21.
 */

public class AddressCompanysBean implements Serializable {

    public ArrayList<AddressCompanysBean> children;
    public String id = "";
    public String name = "";
    public String pId = "";
    public String conpany_id = "";
    public AddressDeptsBean deptsBean;


}

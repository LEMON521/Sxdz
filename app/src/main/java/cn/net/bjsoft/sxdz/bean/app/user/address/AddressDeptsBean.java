package cn.net.bjsoft.sxdz.bean.app.user.address;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/21.
 */

public class AddressDeptsBean implements Serializable {

    public ArrayList<AddressDeptsBean> children;

    public String company_id = "";
    public String id = "";
    public String name = "";
    public String position_id = "";
    public String pId = "";
    public String employee_id="";
    public String company = "";
    //public String type = "";


    public AddressPositionsBean positionsBean;

}

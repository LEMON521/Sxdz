package cn.net.bjsoft.sxdz.bean.app.user.address;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/21.
 */

public class AddressPositionsBean implements Serializable {

//    public AddressCompanysBean companys;
//    public ArrayList<AddressDeptsBean> depts;
//    public ArrayList<AddressEmployeesBean> employees;
//    public AddressPositionsBean positions;
//
    public ArrayList<AddressPositionsBean> children;
    public String dept_id = "";
    public String employee_id = "";
    public String id ="";
    public String name = "";
    public String pId = "";
    public String type = "";

    public AddressEmployeesBean employee;

    public AddressDeptsBean depts;

}

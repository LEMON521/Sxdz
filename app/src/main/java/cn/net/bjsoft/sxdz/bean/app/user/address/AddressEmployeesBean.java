package cn.net.bjsoft.sxdz.bean.app.user.address;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/4/21.
 */

public class AddressEmployeesBean implements Serializable {
    public String email="";
    public String id="";
    public String name="";
    public String phone="";
    public String root_company_id="";
    public String source_id="";
    public String work_no="";
    public String avatar="";
    public AddressEmployeesUserBean user = new AddressEmployeesUserBean();


}

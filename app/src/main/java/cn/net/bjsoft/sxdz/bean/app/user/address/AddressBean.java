package cn.net.bjsoft.sxdz.bean.app.user.address;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/21.
 */

public class AddressBean implements Serializable {

    public AddressCompanysBean companys;
    public ArrayList<AddressDeptsBean> depts;
    public ArrayList<AddressEmployeesBean> employees;
    public AddressPositionsBean positions;
}

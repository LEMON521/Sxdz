package cn.net.bjsoft.sxdz.bean.app.user;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class UserOrganizationBean implements Serializable {
    public String all_company_ids = "";//头像
    public String child_company_ids = "";
    public String child_position_ids = "";
    public String company_id = "";
    public String company_name = "";
    public String dept_id;
    public String dept_name = "";//头像
    public String employee_id = "";
    public String parent_company_ids = "";
    public String position_id = "";
    public HashMap<String ,String> positions ;


    public String root_company_id = "";
    public String root_company_name = "";
    public String root_dept_id = "";
    public String root_position_id = "";
    public String url;

}

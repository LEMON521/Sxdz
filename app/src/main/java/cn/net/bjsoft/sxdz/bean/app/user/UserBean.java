package cn.net.bjsoft.sxdz.bean.app.user;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class UserBean implements Serializable {
    public ArrayList<UserAddinsBean> addins;
    public String avatar = "";//头像
    public String appid = "";//用户所在的app

    public boolean calendar = false;
    public boolean check = false;
    public boolean email = false;
    public boolean files = false;
    public Object functional;
    public int id = -1;
    public String lang = "";
    public String link_from;
    public String link_id = "";
    public boolean lock = false;
    public String loginname;
    public String menu_id;
    public boolean message= false;
    public String module = "";
    public String name = "";
    public boolean notepad= false;

    public UserOrganizationBean organization;
    public boolean reset = false;
    public boolean settings = false;
    public String source_id;
    public Object stars;
    public boolean task = false;
    public boolean update = false;

}

package cn.net.bjsoft.sxdz.bean.app.user;

import java.io.Serializable;

import cn.net.bjsoft.sxdz.bean.app.user.address.UserAddinsLinkBean;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class UserAddinsBean implements Serializable {
    public String description = "";
    public String id = "";
    public String logo = "";
    public String title;

    public UserAddinsLinkBean link;
}

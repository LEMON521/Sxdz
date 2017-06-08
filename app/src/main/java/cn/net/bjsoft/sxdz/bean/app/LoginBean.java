package cn.net.bjsoft.sxdz.bean.app;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class LoginBean implements Serializable {
    public String background = "";
    public String btntext = "";
    public boolean canregister = false;
    public boolean ismember = false;
    public String loginapi = "";
    public String loginurl = "";
    public String logoutapi = "";
    public String passreset = "";
    public boolean resetpassword = false;

    public String mobilecheck = "";//为1就是启用手机验证码验证，否则不启用
    public String registerapi = "";//注册提交的url
    public String serviceurl = "";//服务协议查看网页地址，如果为空就不显示链接
    public String privateurl = "";//隐私条款查看网页地址，如果为空就不现实链接
    //    serviceurl与privateurl都为空，整个这一个区域都不显示

}

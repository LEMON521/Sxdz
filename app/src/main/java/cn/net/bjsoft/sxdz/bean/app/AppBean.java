package cn.net.bjsoft.sxdz.bean.app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class AppBean implements Serializable {
    public String api_auth = "";
    public String api_open = "";
    public String api_upload = "";//上传文件的绝对url
    public String api_user = "";
    public String appid = "";
    public boolean authentication = true;
    public String description = "";
    public ArrayList<HomepageBean> homepage;
    public String id = "";
    public ArrayList<LoadersBean> loaders;
    public LoginBean login;
    public String logo = "";
    public String position = "";
    public PushBean push;
    public PushdataBean pushdata;
    public PushkeysBean pushkeys;
    public String secret = "";
    public String title = "";
    public ToolbarBean toolbar;
}

package cn.net.bjsoft.sxdz.bean.app.top.message.task;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/10.
 */

public class MessageTaskDetailDataBean implements Serializable {
    public String ctime = "";
    public String description = "";
    public ArrayList<MessageTaskDetailDataFilesBean> files ;
    public boolean finished = false;
    public String finishtime = "";
    public String hours = "";
    public String id = "";
    public String limittime = "";
    public String message = "";
    public String priority = "";
    public String priority_color = "";
    public String progress = "";
    public boolean shared = false;
    public String starttime = "";
    public String title = "";
    public String type = "";
    public String userid = "";
    public ArrayList<MessageTaskDetailDataUsersBean> users ;
}

package cn.net.bjsoft.sxdz.bean.app.top.message.task;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/10.
 */

public class MessageTaskDetailDataUsersBean implements Serializable {
    public String evaluate = "";
    public ArrayList<MessageTaskDetailDataFilesBean> files ;
    public String finished = "";
    public String finishtime = "";
    public String id = "";
    public ArrayList<MessageTaskDetailDataUsersPlanBean> plan ;
    public String position = "";
    public String progress = "";
    public String submited = "";
    public String submittime = "";
}

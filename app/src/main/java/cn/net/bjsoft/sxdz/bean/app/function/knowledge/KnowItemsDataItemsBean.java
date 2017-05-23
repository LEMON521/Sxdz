package cn.net.bjsoft.sxdz.bean.app.function.knowledge;

import java.io.Serializable;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataFilesBean;

/**
 * Created by Zrzc on 2017/5/23.
 */

public class KnowItemsDataItemsBean implements Serializable {

    public String abstract_str = "";
    public String author = "";
    public String company_id = "";
    public String content = "";
    public String ctime = "";
    public String data_from = "";
    public String id = "";
    public String labels = "";
    public String logo = "";
    public String title = "";
    public String type = "";
    public String userid = "";
    public String views = "";

    public ArrayList<KnowItemsDataItemsItemsBean> items;
    public Object tops = "";

    public ArrayList<MessageTaskDetailDataFilesBean> files;


}

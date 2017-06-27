package cn.net.bjsoft.sxdz.bean.app.top.message.task;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/10.
 */

public class MessageTaskDetailTypesBean implements Serializable {
    public String code = "";
    public MessageTaskDetailTypesDataBean data;

    public class MessageTaskDetailTypesDataBean {
        public ArrayList<MessageTaskDetailTypesTypeDataBean> types;
    }
    public class MessageTaskDetailTypesTypeDataBean {
        public String type = "";
    }

}

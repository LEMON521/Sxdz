package cn.net.bjsoft.sxdz.bean.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/10.
 */

public class MessageMessageBean implements Serializable {

    public String code = "1";
    public MessageMessageDatasDao data;
    public String msg ="";

    public class MessageMessageDatasDao implements Serializable {
        public String count = "";//总数
        public ArrayList<MessageDataDao> items;
    }

    public class MessageDataDao implements Serializable {
        public String id = "";
        public String link_from = "";
        public String link_id = "";
        public String link_params = "";
        public String link_url = "";
        public String message = "";
        public String name = "";
        public String progress = "";
        public String sendid = "";
        public String showtime = "";
        public String type = "";
        public String userid = "";
    }
}

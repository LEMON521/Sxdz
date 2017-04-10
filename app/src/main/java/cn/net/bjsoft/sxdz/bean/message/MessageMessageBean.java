package cn.net.bjsoft.sxdz.bean.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/10.
 */

public class MessageMessageBean implements Serializable {

    public boolean result = false;
    public MessageMessageDatasDao data;

    public class MessageMessageDatasDao implements Serializable {

        public ArrayList<MessageDataDao> message_list;
    }

    public class MessageDataDao implements Serializable {
        //         "avatar_url": "http://www.shuxin.net/api/app_json/android/form/form_2.jpg",
//                 "detail": "放假通知已经下发,点击查看详情",
//                 "name": "Charing",
//                 "time": "1489031169763",
//                 "title": "劳动节放假通知"
        public String avatar_url = "";
        public String name = "";
        public String title = "";
        public String detail = "";
        public long time = -1;
    }
}

package cn.net.bjsoft.sxdz.bean.app.top.message.approve;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/8.
 */

public class MessageApproveDataItemsBean implements Serializable {

    public String ctime = "";
    public String finished = "";
    public String id = "";
    public String node_id = "";
    public String node_name = "";
    public String node_time = "";
    public ArrayList<MessageApproveDataItemsUsersBean> node_users;
    public String title = "";
    public String userid = "";
    public String wf_id = "";
    public String wf_name = "";
    public String wf_type = "";


    public class MessageApproveDataItemsUsersBean{
        public String edit= "";
        public String id= "";
        public String position= "";
        public String reject= "";
        public String type= "";
    }
//     "ctime": "/Date(1491798201000)/",
//             "finished": false,
//             "id": "4940935902148911191",
//             "node_id": 3,
//             "node_name": "记账",
//             "node_time": "/Date(1493272863886)/",
//             "node_users": [
//    {
//        "edit": false,
//            "id": "10001",
//            "position": false,
//            "reject": true,
//            "type": 1
//    }
//                ],
//                        "title": "舒新东发起的报销申请",
//                        "userid": 10001,
//                        "wf_id": "baoxiao",
//                        "wf_name": "费用报销",
//                        "wf_name": "综合行政"


}


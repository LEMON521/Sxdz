package cn.net.bjsoft.sxdz.bean.approve;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/8.
 */

public class ApproveDatasDao implements Serializable {
    public ArrayList<ApproveItems> data;
    public boolean result = false;

    public class ApproveItems {
        public String department = "";//部门
        public String mater = "";//事由
        public String name = "";//姓名
        public String state = "";//审批状态
        public String time = "";//时间戳
        public String type = "";//审批类型
        public String url = "";
        public String approve_result = "";
    }
}

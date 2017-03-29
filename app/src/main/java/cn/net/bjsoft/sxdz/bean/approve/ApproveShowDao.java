package cn.net.bjsoft.sxdz.bean.approve;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/2/28.
 */

public class ApproveShowDao implements Serializable {
    public ApproveData approve_data;//数据
    public ArrayList<ApproveApprover> approve_list;//审批人列表
    public ArrayList<ApproveFiles> file_list;//附件列表
    public String number;//审批编号
    public ApproveSponsor sponsor;//发起者
    public String state;//审批状态
    public String time;//新建审批时的时间戳
    public String type;//审批类型

    public class ApproveApprover {
        public String aprover;//审批人
        public String state;//审批状态
        public String time;//审批时间
    }

    public class ApproveFiles {
        public String url;//文件路径
        public String uri;
        public String name;//文件名称
        /**
         * 1，添加类型
         * 2，Word类型
         * 3，xecl类型
         * 4，ppt类型
         * 5，图片类型
         * 6，其他类型
         */
        public int tag;//文件类型

    }

    public class ApproveSponsor {
        public String department;//部门
        public String name;//姓名
    }

    public class ApproveData{
//        public ApproveAgreementBean agreement;
//        public ApproveAgreementBean agreement;
//        public ApproveAgreementBean agreement;
//        public ApproveAgreementBean agreement;
//        public ApproveAgreementBean agreement;
//        public ApproveAgreementBean agreement;
//        public ApproveAgreementBean agreement;
    }
}

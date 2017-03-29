package cn.net.bjsoft.sxdz.bean.approve;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/2/21.
 */

public class ApproveContactDao extends ApproveNewDao implements Serializable{
        public int tag = -1;//状态-1--增加图标,1--指定图标
        public String name = "";//姓名
        public String department = "";//部门

}

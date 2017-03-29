package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;

/**
 * 联系人的实体类
 * Created by 靳宁宁 on 2017/2/28.
 */

public class ContactsBean implements Serializable {
    public int state = 0;//状态0--默认,1--指定
    public String name = "";//姓名
    public String department = "";//部门

}

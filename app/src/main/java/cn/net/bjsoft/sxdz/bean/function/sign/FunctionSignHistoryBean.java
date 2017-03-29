package cn.net.bjsoft.sxdz.bean.function.sign;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/3.
 */

public class FunctionSignHistoryBean implements Serializable {

    public ArrayList<DepartmentListDao> data;
    public boolean result = false;


    public class DepartmentListDao {
        public String department = "";//部门
        public ArrayList<HumenListDao> person;

    }


    public class HumenListDao {
        public String name = "";//姓名
        public ArrayList<SignListDatasDao> sign_last;
        public String color = "#FF0000";//
        public boolean isCheck = false;
    }


    public class SignListDatasDao {
        public String pic_url = "";//上次签到图片url
        public String sign_last_time = "";//上次签到时间
        public String sign_last_place = "";//上次签到地址
        public double latitude;
        public double longitude;
    }
}

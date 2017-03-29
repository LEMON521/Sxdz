package cn.net.bjsoft.sxdz.bean.approve;

import java.io.Serializable;

/**
 * 出差
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveTripDao extends ApproveNewDao implements Serializable {
    public String situs = "";//地点
    public String startTime_str = "";//开始时间
    public String endTime_str = "";//结束时间
    public int startTime_int = 0;//开始时间戳
    public int endTime_int = 0;//结束时间戳
    public String totleDate_str = "";//总时间
    public int totleDate_int = 0;//总时间

}

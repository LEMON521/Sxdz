package cn.net.bjsoft.sxdz.utils.function;


import android.text.TextUtils;

import org.xutils.common.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 关于日期的工具类
 * Created by Zrzc on 2017/3/9.
 */

public class TimeUtils {

    /**
     * 计算与当前的时间的时间差
     *
     * @param time
     * @return
     */
    public static String getTimeDifference(long time) {
        long timeNow = 0;
        timeNow = System.currentTimeMillis();
        long difference = timeNow - time;
        LogUtil.e("timeNow=" + timeNow + "::time=" + time + "::difference=" + difference);
        long day = difference / (24 * 60 * 60 * 1000);
        if (day > 0) {
            return day + "天前";
        }
        long hour = (difference / (60 * 60 * 1000) - day * 24);
        if (hour > 0) {
            return hour + "小时前";
        }
        long min = ((difference / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if (min > 0) {
            return min + "分钟前";
        }
        long s = (difference / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (s > 0) {
            if (s < 10)
                return "刚刚";
            else
                return s + "秒前";
        }
        return "";
    }

    /**
     * 日期格式化器 (不带时分秒)
     *
     * @param time       时间戳
     * @param decollator 分割符,用于区分年月日之间的分割,如果为空或者null,默认为"-"
     * @return yyyy-MM-dd(默认情况)
     */
    public static String getFormateDate(long time, String decollator) {
        if (decollator == null && decollator.equals("")) {
            decollator = "-";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy" + decollator + "MM" + decollator + "dd");
        //SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    /**
     * 日期格式化器(带时分秒)
     *
     * @param time           时间戳
     * @param dataDecollator 年月日分割符,用于区分年月日之间的分割,如果为空或者null,默认为"-"
     * @param timeDecollator 时分秒分割符,用于区分时分秒之间的分割,如果为空或者null,默认为":"
     * @return yyyy-MM-dd HH:mm:ss(默认情况)
     */
    public static String getFormateTime(long time, String dataDecollator, String timeDecollator) {
        if (dataDecollator == null && dataDecollator.equals("")) {
            dataDecollator = "-";
        }
        if (timeDecollator == null && timeDecollator.equals("")) {
            timeDecollator = ":";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy"
                + dataDecollator
                + "MM"
                + dataDecollator
                + "dd HH"
                + timeDecollator
                + "mm"
                /*+ timeDecollator
                + "ss"*/);
        //SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    /**
     * 字符串转时间戳
     *
     * @param timeString
     * @return
     */
    public static String getDateStamp(String timeString, String timeDecollator) {
        String timeStamp = null;

        if (TextUtils.isEmpty(timeDecollator)) {
            timeDecollator = "-";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + timeDecollator + "MM" + timeDecollator + "dd");
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 字符串转时间戳
     *
     * @param timeString
     * @return
     */
    public static String getTimeStamp(String timeString, String timeDecollator, String dataDecollator) {
        String timeStamp = null;

        if (TextUtils.isEmpty(timeDecollator)) {
            timeDecollator = "-";
        }
        if (TextUtils.isEmpty(dataDecollator)) {
            dataDecollator = ":";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + timeDecollator + "MM" + timeDecollator + "dd hh" + dataDecollator + "mm");
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

}

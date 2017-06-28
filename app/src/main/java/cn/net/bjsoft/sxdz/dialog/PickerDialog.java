package cn.net.bjsoft.sxdz.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Zrzc on 2017/2/23.
 */

public class PickerDialog {


    // 定义显示时间控件
    private static Calendar calendar; // 通过Calendar获取系统时间
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;


//    public MyDatePickerDialog(Context context, EditText editText) {
//        this.context = context;
//        this.editText = editText;
//        calendar = Calendar.getInstance();
//    }

    /**
     * 显示日期--选择日期并显示到指定的控件上
     *
     * @param context   上下文
     * @param editText  勇于显示日期的控件/可为空
     * @param separator 指定分隔符样式,可不指定,默认为"-"
     * @return String 返回获取到的日期信息
     */
    public static String showDatePickerDialog(Context context, final EditText editText, String separator) {
        calendar = Calendar.getInstance();
        final StringBuilder sb = new StringBuilder();
        if (separator == null || separator.equals("")) {
            separator = "-";
        }
        final String finalSeparate = separator;
        new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        // TODO Auto-generated method stub
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                        sb.append(mYear)
                                .append(finalSeparate)
                                .append((mMonth + 1) < 10 ? "0"
                                        + (mMonth + 1) : (mMonth + 1))
                                .append(finalSeparate)
                                .append((mDay < 10) ? "0" + mDay : mDay);
                        // 更新EditText控件日期 小于10加0
                        if (editText != null) {
                            editText.setText(sb.toString());
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar
                .get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show();
        return sb.toString();
    }

    /**
     * 显示时间--选择时间并显示到指定的控件上
     *
     * @param context   上下文
     * @param editText  勇于显示日期的控件/可为空
     * @param separator 指定分隔符样式,可不指定,默认为":"
     * @return String 返回获取到的日期信息
     */
    public static String showTimePickerDialog(Context context, final EditText editText, String separator) {
        calendar = Calendar.getInstance();

        final StringBuilder sb = new StringBuilder();
        if (separator == null || separator.equals("")) {
            separator = ":";
        }
        final String finalSeparate = separator;


        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                sb.append(mHour)
                        .append(finalSeparate)
                        .append(mMinute);
                if (editText != null)
                    editText.setText(sb.toString());
            }
        }
                , calendar.get(Calendar.DAY_OF_MONTH)
                , calendar.get(Calendar.MINUTE)
                , true).show();//要show出来
        return sb.toString();
    }



}
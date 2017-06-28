package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class TaskSearchPopupWindow/* extends PopupWindow*/ implements View.OnClickListener, OnDateSetListener {
    private AppProgressDialog progressDialog;

    private PopupWindow mSearchPopupWindow;
    private FragmentActivity mActivity;
    private View view;
    // 根视图
    private View mRootView;
    private LayoutInflater mInflater;

    //    private MessageTaskBean taskBean;
//    private MessageTaskBean.TaskQueryDao taskQueryDao;
    private String time_start = "";
    private String time_end = "";
    private ArrayList<MessageTaskBean.TaskQueryTypeDao> typeList;
    private ArrayList<MessageTaskBean.TaskQueryLevelDao> levelList;

//    private ArrayList<String> typeList;
//    private ArrayList<String> levelList;

    private LinearLayout left;
    private ImageView exit;
    private TextView type, level, /*start, end,*/
            submit, reset;
    private EditText start, end;
    private String startStr = "";
    private String endStr = "";

    // 数据接口
    OnGetData mOnGetData;

    private ListPopupWindow typePopupWindow;
    private ListPopupWindow levelPopupWindow;
    private ArrayList<String> typeStrList;
    private ArrayList<String> levelStrList;

    //时间选择器
    //https://github.com/JZXiang/TimePickerDialog/blob/master/sample/src/main/java/com/jzxiang/pickerview/sample/MainActivity.java
    private TimePickerDialog mDialogAll;
    private OnDateSetListener listener;
    private boolean isStart = true;

    public TaskSearchPopupWindow(FragmentActivity activity
            , View view) {
        this.mActivity = activity;
        this.view = view;

        //this.cacheItemsDataList = cacheItemsDataList;
    }

//    public void showWindow(ArrayList<String> taskTypeList
//            , ArrayList<String> taskLevelList
//            , String startTime
//            , String endTime) {
//        this.typeList = taskTypeList;
//        this.levelList = taskLevelList;
//        this.startStr = startTime;
//        this.endStr = endTime;
//        InitUI();
//    }

    public void showWindow(ArrayList<String> taskTypes, ArrayList<String> taskLevels) {
        if (taskTypes == null) {
            MyToast.showLong(mActivity, "请先设置查询分类数据!");
            return;
        }
        typeStrList = taskTypes;
        levelStrList = taskLevels;

        InitData();
        InitUI();
    }


    private void InitData() {

        mSearchPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_task_search_zdlf, null);
        mSearchPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mSearchPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);

        mSearchPopupWindow.setOutsideTouchable(true);
        mSearchPopupWindow.setFocusable(true);
        //下面两句话是将底部的popupWindow顶到软键盘上面去
        mSearchPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        //mSearchPopupWindow.showAtLocation(view, RIGHT, 0, 0);
        mSearchPopupWindow.showAsDropDown(view);
        mSearchPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mSearchPopupWindow.update();
        mSearchPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    private void InitUI() {
        left = (LinearLayout) mRootView.findViewById(R.id.pop_task_search_zdlf_left);
        exit = (ImageView) mRootView.findViewById(R.id.pop_task_search_zdlf_exit);
        type = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_type);
        level = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_level);
        start = (EditText) mRootView.findViewById(R.id.pop_task_search_zdlf_time_start);
        end = (EditText) mRootView.findViewById(R.id.pop_task_search_zdlf_time_end);
        submit = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_submit);
        reset = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_reset);

        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                /*.setMinMillseconds(System.currentTimeMillis())*/
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(mActivity.getResources().getColor(R.color.blue))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(mActivity.getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(mActivity.getResources().getColor(R.color.light_blue))
                .setWheelItemTextSize(12)
                .build();

//        if (typeList.size() > 0) {
//            type.setText(typeList.get(0).type);
//        }
//        if (levelList.size() > 0) {
//            level.setText(levelList.get(0).level);
//        }
//        start.setText(TimeUtils.getFormateDate(Long.parseLong(startStr), "-"));
//        end.setText(TimeUtils.getFormateDate(Long.parseLong(endStr), "-"));

        left.setOnClickListener(this);
        exit.setOnClickListener(this);
        type.setOnClickListener(this);
        level.setOnClickListener(this);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        submit.setOnClickListener(this);
        reset.setOnClickListener(this);


        typePopupWindow = new ListPopupWindow(mActivity, view);
        typePopupWindow.setOnData(new ListPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                type.setText(result);
            }
        });

        levelPopupWindow = new ListPopupWindow(mActivity, view);
        levelPopupWindow.setOnData(new ListPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                level.setText(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int[] location = null;
        location = new int[2];
        switch (v.getId()) {

            case R.id.pop_task_search_zdlf_left:
                mSearchPopupWindow.dismiss();
                break;
            case R.id.pop_task_search_zdlf_exit:
                mSearchPopupWindow.dismiss();
                break;

            case R.id.pop_task_search_zdlf_type:
                type.getLocationOnScreen(location);
                location[1] = location[1] + type.getHeight();
                //type.getLocationInWindow(location);
//                location[0] = type.getLeft();
//                location[1] = type.getRight();
//                LogUtil.e("type=getLeft=="+type.getLeft());
//                LogUtil.e("type=getRight=="+type.getRight());
//
//                LogUtil.e("type=getRight=="+location[0]+"::"+location[1]);
                typePopupWindow.showWindow(typeStrList, location);

                break;

            case R.id.pop_task_search_zdlf_level:
                level.getLocationOnScreen(location);
                location[1] = location[1] + type.getHeight();
                levelPopupWindow.showWindow(levelStrList, location);

                break;

            case R.id.pop_task_search_zdlf_time_start:
//                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//                        mActivity, initEndDateTime);
//                dateTimePicKDialog.dateTimePicKDialog(endDateTime);
                //PickerDialog.showDatePickerDialog(mActivity, start, "-");
                isStart = true;
                mDialogAll.show(mActivity.getSupportFragmentManager(), "all");
                break;

            case R.id.pop_task_search_zdlf_time_end:
                //PickerDialog.showDatePickerDialog(mActivity, end, "-");
                isStart = false;
                mDialogAll.show(mActivity.getSupportFragmentManager(), "all");
                break;

            case R.id.pop_task_search_zdlf_submit:
                String startStr = start.getText().toString().trim();
                String endStr = end.getText().toString().trim();
                String typeStr = type.getText().toString().trim();
                String levleStr = level.getText().toString().trim();

                if (TextUtils.isEmpty(startStr)) {
                    MyToast.showShort(mActivity, "请选择开始时间");
                    return;
                }

                if (TextUtils.isEmpty(endStr)) {
                    MyToast.showShort(mActivity, "请选择结束时间");
                    return;
                }

                if (TextUtils.isEmpty(typeStr)) {
                    MyToast.showShort(mActivity, "请选择任务类别");
                    return;
                }

                if (TextUtils.isEmpty(levleStr)) {
                    MyToast.showShort(mActivity, "请选择任务等级");
                    return;
                }

                mOnGetData.onDataCallBack(startStr, endStr, typeStr, levleStr);
                mSearchPopupWindow.dismiss();
                break;

            case R.id.pop_task_search_zdlf_reset:
                type.setText("");
                level.setText("");
                start.setText("");
                end.setText("");
                break;

        }

    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(String startStr, String endStr, String typeStr, String levleStr);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        mOnGetData = sd;
    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
//        LogUtil.e(timePickerView.getCurrentMillSeconds()+timePickerView.getArguments().toString()+"");
//        LogUtil.e(millseconds+"");
//        LogUtil.e("获取到了时间");
        if (isStart) {
            start.setText(TimeUtils.getFormateTime(millseconds, "-", ":"));
        } else {
            end.setText(TimeUtils.getFormateTime(millseconds, "-", ":"));
        }
    }
}

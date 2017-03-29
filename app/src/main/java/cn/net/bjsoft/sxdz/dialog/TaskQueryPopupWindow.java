package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class TaskQueryPopupWindow/* extends PopupWindow*/ implements View.OnClickListener {

    private PopupWindow mQueryPopupWindow;
    private FragmentActivity mActivity;
    private View view;
    // 根视图
    private View mRootView;

    private TextView submit, cancel;
    private EditText edit_name, edit_num, edit_start, edit_end;
    private ImageView iv_start, iv_end;

    // 数据接口
    OnGetData mOnGetData;

    private HashMap<String, String> content;

    public TaskQueryPopupWindow(FragmentActivity activity
            , View view) {
        this.mActivity = activity;
        this.view = view;


        if (content == null) {
            content = new HashMap<>();
        }

    }

    public void showWindow(){
        InitData();
        InitUI();
    }

    private void InitData() {
        mQueryPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_task_query, null);
        mQueryPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mQueryPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);

        mQueryPopupWindow.setOutsideTouchable(true);
        mQueryPopupWindow.setFocusable(true);
        //下面两句话是将底部的popupWindow顶到软键盘上面去
        mQueryPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mQueryPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        //mQueryPopupWindow.showAtLocation(view, RIGHT, 0, 0);
        mQueryPopupWindow.showAsDropDown(view);
        mQueryPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mQueryPopupWindow.update();
        mQueryPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    private void InitUI() {

        edit_name = (EditText) mRootView.findViewById(R.id.pop_query_name);
        edit_num = (EditText) mRootView.findViewById(R.id.pop_query_number);
        edit_start = (EditText) mRootView.findViewById(R.id.pop_query_start_time_et);
        edit_end = (EditText) mRootView.findViewById(R.id.pop_query_end_time_et);
        iv_start = (ImageView) mRootView.findViewById(R.id.pop_query_start_time_iv);
        iv_end = (ImageView) mRootView.findViewById(R.id.pop_query_end_time_iv);

        submit = (TextView) mRootView.findViewById(R.id.pop_query_submit);
        cancel = (TextView) mRootView.findViewById(R.id.pop_query_cancel);

        edit_start.setOnClickListener(this);
        edit_end.setOnClickListener(this);
        iv_start.setOnClickListener(this);
        iv_end.setOnClickListener(this);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.pop_query_start_time_iv://选择开始时间
                PickerDialog.showDatePickerDialog(mActivity, edit_start, "-");
                break;


            case R.id.pop_query_end_time_iv://选择结束时间
                PickerDialog.showDatePickerDialog(mActivity, edit_end, "-");
                break;

            case R.id.pop_query_start_time_et://选择开始时间
                PickerDialog.showDatePickerDialog(mActivity, edit_start, "-");
                break;


            case R.id.pop_query_end_time_et://选择结束时间
                PickerDialog.showDatePickerDialog(mActivity, edit_end, "-");
                break;


            case R.id.pop_query_submit://确定----提交到服务器

                /**
                 * 这里应该根据后台要求,判断哪一项不能为空,并且提示用户
                 */


                content.clear();
                content.put("edit_name", edit_name.getText().toString().trim());
                content.put("edit_num", edit_num.getText().toString().trim());
                content.put("edit_start", edit_start.getText().toString().trim());
                content.put("edit_end", edit_end.getText().toString().trim());

                mOnGetData.onDataCallBack(content);
                mQueryPopupWindow.dismiss();
                break;

            case R.id.pop_query_cancel://取消
                edit_name.setText("");
                edit_num.setText("");
                edit_start.setText("");
                edit_end.setText("");
                break;

        }


    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        /**
         * 对应的Map集合的键值对为:
         * <p>
         * edit_name:项目名称
         * <p>
         * edit_num:项目编号
         * <p>
         * edit_start:开始时间
         * <p>
         * edit_end:结束时间
         * <p>
         *
         * @param content
         */
        abstract void onDataCallBack(HashMap<String, String> content);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        mOnGetData = sd;
    }

    public boolean isShow(){
        return mQueryPopupWindow.isShowing();
    }

}

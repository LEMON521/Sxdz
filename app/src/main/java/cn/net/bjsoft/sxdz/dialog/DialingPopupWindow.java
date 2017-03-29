package cn.net.bjsoft.sxdz.dialog;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class DialingPopupWindow/* extends PopupWindow*/ implements View.OnClickListener {
    private AppProgressDialog progressDialog;

    private PopupWindow mDailingPopupWindow;
    private FragmentActivity mActivity;
    private View view;
    // 根视图
    private View mRootView;
    private LayoutInflater mInflater;

    private TextView diale, copy, cancel;
    private String phone_num = "";

    // 数据接口
    OnGetData mOnGetData;

    public DialingPopupWindow(FragmentActivity activity
            , View view
            , String phone_num) {
        this.mActivity = activity;
        this.view = view;
        this.phone_num = phone_num;
        InitData();
        InitUI();

    }

    private void InitData() {
        mDailingPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_dialing, null);
        mDailingPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mDailingPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);

        mDailingPopupWindow.setOutsideTouchable(true);
        mDailingPopupWindow.setFocusable(true);
        //下面两句话是将底部的popupWindow顶到软键盘上面去
        mDailingPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mDailingPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        //mDailingPopupWindow.showAtLocation(view, RIGHT, 0, 0);
        mDailingPopupWindow.showAtLocation((View) view.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mDailingPopupWindow.showAsDropDown(view);
        mDailingPopupWindow.setAnimationStyle(R.style.PopupUpDownAnimation);
        mDailingPopupWindow.update();
        mDailingPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    private void InitUI() {
        diale = (TextView) mRootView.findViewById(R.id.pop_dialing_diale);
        copy = (TextView) mRootView.findViewById(R.id.pop_dialing_copy);
        cancel = (TextView) mRootView.findViewById(R.id.pop_dialing_cancel);
        //delete = (ImageView) mRootView.findViewById(R.id.search_delete);
        //edit.setText(search);
        diale.setOnClickListener(this);
        copy.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.pop_dialing_diale:


                //意图：想干什么事
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:" + phone_num));
                //开启系统拨号器
                mActivity.startActivity(intent);
                mDailingPopupWindow.dismiss();
                break;


            case R.id.pop_dialing_copy:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(phone_num);

                MyToast.showShort(mActivity, "已复制到剪切板!");
                mDailingPopupWindow.dismiss();
                break;

            case R.id.pop_dialing_cancel:

                mDailingPopupWindow.dismiss();
                break;


        }


    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(String search, ArrayList<KnowledgeBean.ItemsDataDao> list);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        mOnGetData = sd;
    }


    public synchronized void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        progressDialog.show(mActivity);
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismissDialog();
        }
    }

    public synchronized AppProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        return progressDialog;
    }
}

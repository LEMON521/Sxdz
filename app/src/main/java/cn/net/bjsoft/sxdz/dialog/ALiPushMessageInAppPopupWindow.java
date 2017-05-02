package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzhoujay.richtext.RichText;

import cn.net.bjsoft.sxdz.R;

import static android.view.Gravity.CENTER;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class ALiPushMessageInAppPopupWindow/* extends PopupWindow*/ implements View.OnClickListener {
    private AppProgressDialog progressDialog;

    private PopupWindow mSearchPopupWindow;
    private FragmentActivity mActivity;
    private View view;
    // 根视图
    private View mRootView;
    private LayoutInflater mInflater;

    //数据
    private Bundle bundleData;

    private TextView message_id;
    private TextView message_title;
    private TextView message_content;
    private TextView message_appid;
    private TextView message_time;
    private RelativeLayout message_root,message_confirm;

    // 数据接口
    OnGetData mOnGetData;

    public ALiPushMessageInAppPopupWindow(FragmentActivity activity
            , Bundle bundleData
            , View view) {
        this.mActivity = activity;
        this.bundleData = bundleData;
        this.view = view;
    }

    public void showWindow() {

        InitData();
        InitUI();
    }


    private void InitData() {

        mSearchPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_show_pushnotyfy, null);
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
//        mSearchPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        mSearchPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        mSearchPopupWindow.showAtLocation(view, CENTER, 0, 0);
//        mSearchPopupWindow.showAsDropDown(view);
        //mSearchPopupWindow.setAnimationStyle(R.style.PopupAnimation);
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
        //message_id = (TextView) mRootView.findViewById(R.id.messageid);
        message_root = (RelativeLayout) mRootView.findViewById(R.id.pop_show_pushnotify_root);
        message_title = (TextView) mRootView.findViewById(R.id.pop_show_pushnotify_title);
        message_content = (TextView) mRootView.findViewById(R.id.pop_show_pushnotify_content);
        message_confirm = (RelativeLayout) mRootView.findViewById(R.id.pop_show_pushnotify_confirm);
        //message_time = (TextView) mRootView.findViewById(R.id.createtime);


        message_root.setOnClickListener(this);
        message_confirm.setOnClickListener(this);

        //message_content.setText(bundleData.getString("notify_type_3_content"));
        RichText.from(bundleData.getString("notify_type_3_content")).autoFix(false).into(message_content);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pop_show_pushnotify_root:
                mSearchPopupWindow.dismiss();
                break;

            case R.id.pop_show_pushnotify_confirm:
                mSearchPopupWindow.dismiss();
                break;
        }

    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(String strJson);
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

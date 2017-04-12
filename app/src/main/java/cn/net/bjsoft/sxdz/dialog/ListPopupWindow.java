package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.PopShowAdapter;
import cn.net.bjsoft.sxdz.view.ChildrenListView;

/**
 * Created by Zrzc on 2017/4/11.
 */

public class ListPopupWindow {

    private AppProgressDialog progressDialog;

    private PopupWindow mSearchPopupWindow;
    private FragmentActivity mActivity;
    private View view;

    // 根视图
    private View mRootView;

    private ChildrenListView listView;


//    private ArrayList<String> typeList;
//    private ArrayList<String> levelList;

    private ArrayList<String> stringList;
    private PopShowAdapter stringListAdapter;
    // 数据接口
    private OnGetData mOnGetData;


    public ListPopupWindow(FragmentActivity activity
            , View view) {
        this.mActivity = activity;
        this.view = view;
    }


    public void showWindow(ArrayList<String> list) {
        this.stringList = list;
        InitData();
        InitUI();
    }


    private void InitData() {

        mSearchPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_show_list, null);
        mSearchPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mSearchPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
//        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
//        lp.alpha = 0.4f;
//        mActivity.getWindow().setAttributes(lp);

        //这里很重要，不设置这个ListView得不到相应

        mSearchPopupWindow.setOutsideTouchable(true);
        mSearchPopupWindow.setFocusable(true);
        //下面两句话是将底部的popupWindow顶到软键盘上面去
        mSearchPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        //mSearchPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        mSearchPopupWindow.showAsDropDown(view);
        //mSearchPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mSearchPopupWindow.update();
//        mSearchPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            //在dismiss中恢复透明度
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
//                lp.alpha = 1f;
//                mActivity.getWindow().setAttributes(lp);
//            }
//        });
    }

    private void InitUI() {
        listView = (ChildrenListView) mRootView.findViewById(R.id.pop_list);

        if (stringListAdapter == null) {
            stringListAdapter = new PopShowAdapter(mActivity, stringList);
        }
        listView.setAdapter(stringListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnGetData.onDataCallBack(stringList.get(position));
                mSearchPopupWindow.dismiss();
            }
        });

    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(String result);
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

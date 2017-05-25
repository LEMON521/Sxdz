package cn.net.bjsoft.sxdz.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class KnowledgeSearchPopupWindow_2/* extends PopupWindow*/ implements View.OnClickListener {
    private InputMethodManager imm;//输入框相关

    private PopupWindow mSearchPopupWindow;
    private FragmentActivity mActivity;
    private View view;
    // 根视图
    private View mRootView;
    private LayoutInflater mInflater;

    private TextView text;
    private EditText edit;
    private ImageView delete;
    private String search = "";

    // 数据接口
    OnGetData mOnGetData;


    public KnowledgeSearchPopupWindow_2(FragmentActivity activity
            , View view) {
        this.mActivity = activity;
        this.view = view;

        //this.cacheItemsDataList = cacheItemsDataList;
        //软键盘管理器
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);


    }

    public void show(String search) {
        this.search = search;
        //弹出软键盘
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        mSearchPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_knowledge_search, null);
        mSearchPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

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

        InitUI();
    }

    private void InitUI() {
        text = (TextView) mRootView.findViewById(R.id.search_text);
        edit = (EditText) mRootView.findViewById(R.id.search_edittext);
        delete = (ImageView) mRootView.findViewById(R.id.search_delete);
        edit.setText(search);
        text.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.search_text:
                search = edit.getText().toString();
                if (search.equals("")) {
                    MyToast.showShort(mActivity, "请输入搜索内容!");
                    return;
                }
                mOnGetData.onDataCallBack(search);
                mSearchPopupWindow.dismiss();
                break;


            case R.id.search_delete:
                search = "";
                mOnGetData.onDataCallBack(search);
                mSearchPopupWindow.dismiss();
                break;


        }
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(String search);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        mOnGetData = sd;
    }
}

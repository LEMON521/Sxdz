package cn.net.bjsoft.sxdz.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class KnowledgeSearchPopupWindow {
    private PopupWindow mSearchPopupWindow;
    private static BaseFragment fragment;
    private static FragmentActivity activity;
    private static BaseAdapter adapter;
    private FragmentActivity mActivity;
    private View view;
    private BaseAdapter mAdapter;

    private ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList;
    private ArrayList<KnowledgeBean.ItemsDataDao> itemsDataList;
    private TextView hint;


    public ArrayList<KnowledgeBean.ItemsDataDao> getCacheItemsDataList() {
        return cacheItemsDataList;
    }

    public void setCacheItemsDataList(ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList) {
        this.cacheItemsDataList = cacheItemsDataList;
    }


    public static FragmentActivity getActivity() {
        return activity;
    }

    public static void setActivity(FragmentActivity activity) {
        KnowledgeSearchPopupWindow.activity = activity;
    }

    public static BaseFragment getFragment() {
        return fragment;
    }

    public static void setFragment(BaseFragment fragment) {
        KnowledgeSearchPopupWindow.fragment = fragment;
    }

    public static BaseAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(BaseAdapter adapter) {
        KnowledgeSearchPopupWindow.adapter = adapter;
    }

    public KnowledgeSearchPopupWindow(FragmentActivity activity
            , View view
            , BaseAdapter itemsAdapter
            , ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList
            , ArrayList<KnowledgeBean.ItemsDataDao> itemsDataList
            , TextView hint) {
        this.mActivity = activity;
        this.view = view;
        this.mAdapter = itemsAdapter;
        cacheItemsDataList.clear();
        this.cacheItemsDataList = cacheItemsDataList;
//        if (this.cacheItemsDataList == null){
//            this.cacheItemsDataList = new ArrayList<>();
//        }
//        this.cacheItemsDataList.clear();
//        this.cacheItemsDataList.addAll(cacheItemsDataList);
        this.itemsDataList = itemsDataList;
        this.hint = hint;

    }

    public void setmSearchPopupWindow() {

        //setActivity(activity);
        final TextView search;
        final EditText edit;
        View contentView;
        mSearchPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        contentView = mLayoutInflater.inflate(R.layout.pop_knowledge_search, null);
        mSearchPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        search = (TextView) contentView.findViewById(R.id.search_text);
        edit = (EditText) contentView.findViewById(R.id.search_edittext);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                KnowledgeBean bean = new KnowledgeBean();
//                KnowledgeBean.ItemsDataDao dao = bean.new ItemsDataDao();

                if (edit.getText().toString().equals("")) {
                    MyToast.showShort(getActivity(), "请输入搜索内容!");
                    return;
                }
                /**
                 * 在这里,从服务器获取
                 */
                String search = edit.getText().toString();
//                KnowledgeBean bean = new KnowledgeBean();
//                KnowledgeBean.ItemsDataDao dao = bean.new ItemsDataDao();

                for (KnowledgeBean.ItemsDataDao dao : itemsDataList) {

                    if (dao.title.contains(search)) {
                        cacheItemsDataList.add(dao);
                    } else if (dao.category.contains(search)) {
                        cacheItemsDataList.add(dao);
                    }
                    cacheItemsDataList.add(dao);
                }
                if (hint != null) {
                    if (!(cacheItemsDataList.size() > 0)) {
                        hint.setVisibility(View.VISIBLE);
                        hint.setText("未搜索到相关条目\n下拉刷新获取信息");
                    } else {
                        if (mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                    }
                }


                mSearchPopupWindow.dismiss();

                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


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



}

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

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class KnowledgeSearchPopupWindow_1/* extends PopupWindow*/ implements View.OnClickListener {
    private AppProgressDialog progressDialog;

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

    private KnowledgeBean.ItemsBean itemsBean;
    private ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList;
    private ArrayList<KnowledgeBean.ItemsDataDao> itemsDataList;

    public KnowledgeSearchPopupWindow_1(FragmentActivity activity
            , ArrayList<KnowledgeBean.ItemsDataDao> itemsDataList
            , String search
            , View view) {
        this.mActivity = activity;
        this.view = view;
        this.search = search;
        //this.cacheItemsDataList = cacheItemsDataList;
        this.itemsDataList = itemsDataList;
        InitData();
        InitUI();

    }

    private void InitData() {
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

                showProgressDialog();
//                KnowledgeBean bean = new KnowledgeBean();
//                KnowledgeBean.ItemsDataDao dao = bean.new ItemsDataDao();
                if (cacheItemsDataList == null) {
                    cacheItemsDataList = new ArrayList<>();
                }
                cacheItemsDataList.clear();
                /**
                 * 在这里,从服务器获取
                 */
                getDataFromService();
                break;


            case R.id.search_delete:
                search = "";
                mOnGetData.onDataCallBack(search, itemsDataList);
                // LogUtil.e("搜索后的条目数==@@@@"+this.cacheItemsDataList.size());
                mSearchPopupWindow.dismiss();
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

    /**
     * 查询服务器,获取列表信息
     */
    private void getDataFromService() {

        RequestParams params = new RequestParams("http://www.shuxin.net/api/app_json/android/knowledge/knowledge_items_life.json");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                itemsBean = GsonUtil.getKnowledgeItemsBean(result);
                if (itemsBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    itemsDataList.clear();
                    cacheItemsDataList.clear();
                    itemsDataList.addAll(itemsBean.items);


                    for (KnowledgeBean.ItemsDataDao dao : itemsDataList) {

                        if (dao.title.contains(search)) {
                            cacheItemsDataList.add(dao);
                        } else if (dao.category.contains(search)) {
                            cacheItemsDataList.add(dao);
                        }
                        //cacheItemsDataList.add(dao);

                    }
                    //LogUtil.e("搜索qian的条目数==()()()("+this.cacheItemsDataList.size());
                    mOnGetData.onDataCallBack(search, cacheItemsDataList);
                    // LogUtil.e("搜索后的条目数==@@@@"+this.cacheItemsDataList.size());
                    mSearchPopupWindow.dismiss();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到的条目--------失败!!!---" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
                mSearchPopupWindow.dismiss();
            }
        });
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

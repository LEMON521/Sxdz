package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class TaskSearchPopupWindow/* extends PopupWindow*/ implements View.OnClickListener {
    private AppProgressDialog progressDialog;

    private PopupWindow mSearchPopupWindow;
    private FragmentActivity mActivity;
    private View view;
    // 根视图
    private View mRootView;
    private LayoutInflater mInflater;

    private MessageTaskBean.TaskQueryDao taskQueryDao;
    public String time_start = "";
    public String time_end = "";
    public ArrayList<MessageTaskBean.TaskQueryTypeDao> typeList;
    public ArrayList<MessageTaskBean.TaskQueryLevelDao> levelList;

//    private ArrayList<String> typeList;
//    private ArrayList<String> levelList;

    private ImageView exit;
    private TextView type, level, start, end, submit, reset;
    private String startStr = "";
    private String endStr = "";

    // 数据接口
    OnGetData mOnGetData;


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

    public void showWindow(MessageTaskBean.TaskQueryDao taskQueryDao) {
        this.typeList = taskQueryDao.type_list;
        this.levelList = taskQueryDao.level_list;
        this.startStr = taskQueryDao.time_start;
        this.endStr = taskQueryDao.time_end;
        InitData();
        InitUI();
    }



    private void InitData() {

        mSearchPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.pop_task_search_zdlf, null);
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
        exit = (ImageView) mRootView.findViewById(R.id.pop_task_search_zdlf_exit);
        type = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_type);
        level = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_level);
        start = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_time_start);
        end = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_time_end);
        submit = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_submit);
        reset = (TextView) mRootView.findViewById(R.id.pop_task_search_zdlf_reset);

        if (typeList.size() > 0) {
            type.setText(typeList.get(0).type);
        }
        if (levelList.size() > 0) {
            level.setText(levelList.get(0).level);
        }
        start.setText(startStr);
        end.setText(endStr);

        exit.setOnClickListener(this);
        type.setOnClickListener(this);
        level.setOnClickListener(this);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        submit.setOnClickListener(this);
        reset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pop_task_search_zdlf_exit:
                break;

            case R.id.pop_task_search_zdlf_type:
                break;

            case R.id.pop_task_search_zdlf_level:
                break;

            case R.id.pop_task_search_zdlf_time_start:
                break;

            case R.id.pop_task_search_zdlf_time_end:
                break;

            case R.id.pop_task_search_zdlf_submit:
                break;

            case R.id.pop_task_search_zdlf_reset:
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

    /**
     * 查询服务器,获取列表信息
     */
    private void getDataFromService() {

        RequestParams params = new RequestParams("http://www.shuxin.net/api/app_json/android/knowledge/knowledge_items_life.json");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mOnGetData.onDataCallBack(result);
                // LogUtil.e("搜索后的条目数==@@@@"+this.cacheItemsDataList.size());

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

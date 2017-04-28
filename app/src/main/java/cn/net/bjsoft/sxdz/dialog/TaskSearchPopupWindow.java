package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;

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

    private MessageTaskBean taskBean;
    private MessageTaskBean.TaskQueryDao taskQueryDao;
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
        if (typeStrList == null) {
            typeStrList = new ArrayList<>();
        }
        typeStrList.clear();
        if (levelStrList == null) {
            levelStrList = new ArrayList<>();
        }
        levelStrList.clear();
        for (MessageTaskBean.TaskQueryTypeDao dao : typeList) {
            typeStrList.add(dao.type);
        }
        for (MessageTaskBean.TaskQueryLevelDao dao : levelList) {
            levelStrList.add(dao.level);
        }

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
                PickerDialog.showDatePickerDialog(mActivity, start, "-");
                break;

            case R.id.pop_task_search_zdlf_time_end:
                PickerDialog.showDatePickerDialog(mActivity, end, "-");
                break;

            case R.id.pop_task_search_zdlf_submit:
                getDataFromService();
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
        showProgressDialog();
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_list_url);
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

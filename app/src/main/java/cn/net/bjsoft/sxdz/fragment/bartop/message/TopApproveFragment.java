package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.ApproveNewActivity;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.TopApproveApprovalFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.TopApproveHistoricalFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.TopApproveWaitFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;

/**
 * 审批
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve)
public class TopApproveFragment extends BaseFragment {
    @ViewInject(R.id.message_approve_title)
    private TextView title;
    @ViewInject(R.id.message_approve_back)
    private TextView back;
    @ViewInject(R.id.empty_text)
    private TextView test;

    @ViewInject(R.id.approve_wait)
    private TextView wait;
    @ViewInject(R.id.approve_approval)
    private TextView approval;
    @ViewInject(R.id.approve_historical)
    private TextView historical;


    private String approveJson = "";
    private ApproveDatasDao approveDao;
    private ArrayList<ApproveDatasDao.ApproveItems> allList;
    private ArrayList<ApproveDatasDao.ApproveItems> waiteList;
    private ArrayList<ApproveDatasDao.ApproveItems> approvalList;
    private ArrayList<ApproveDatasDao.ApproveItems> historyList;

    private String fragmentTag = "";

    protected static final int SUCCESS = 1;
    protected static final int ERROR = -1;
    private Message message = null;
    /**
     * 向主线程发送消息的Handler
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 对数据进行解析和封装
                case SUCCESS:

                    setData();
                    dismissProgressDialog();
                    break;

                case ERROR:
                    //
                    test.setText("网络异常,暂时无法获取数据/n点击刷新");
                    dismissProgressDialog();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void initData() {
        showProgressDialog();
        title.setText("审批");
        back.setVisibility(View.VISIBLE);
        test.setText("");
        if (message != null) {
            message = null;
        }

        message = Message.obtain();
        getData();

    }

    /**
     * 设置数据
     */
    public void getData() {
        if (allList != null) {
            allList = null;
        }
        RequestParams params = new RequestParams(TestAddressUtils.test_get_approve_items_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                approveDao = GsonUtil.getApproveDatasBean(result);
                if (approveDao.result) {
                    LogUtil.e("获取到的条目-----------" + result);
                    allList = approveDao.data;
                    message.what = SUCCESS;
                } else {
                    message.what = ERROR;
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                message.what = ERROR;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 设置数据
     */
    public void setData() {
        if (waiteList == null) {
            waiteList = new ArrayList<>();
        } else {
            waiteList.clear();
        }
        if (approvalList == null) {
            approvalList = new ArrayList<>();
        } else {
            approvalList.clear();
        }

        if (historyList == null) {
            historyList = new ArrayList<>();
        } else {
            historyList.clear();
        }

        for (int i = 0; i < allList.size(); i++) {
            if (allList.get(i).state.equals("wait")) {
                waiteList.add(allList.get(i));
                //setWaiteList(waiteList);
            } else if (allList.get(i).state.equals("approval")) {
                approvalList.add(allList.get(i));
            } else if (allList.get(i).state.equals("history")) {
                historyList.add(allList.get(i));
            }
        }
        setWaiteList(waiteList);
        setApprovalList(approvalList);
        sethistoryList(historyList);

        approveChangeOnClick(wait);

    }

    //------------------------------方便其他Fragment获取到List数据.
    public void setWaiteList(ArrayList<ApproveDatasDao.ApproveItems> waiteList) {
        this.waiteList = waiteList;
    }

    public ArrayList<ApproveDatasDao.ApproveItems> getWaiteList() {
        return waiteList;
    }

    public void setApprovalList(ArrayList<ApproveDatasDao.ApproveItems> approvalList) {
        this.approvalList = approvalList;
    }

    public ArrayList<ApproveDatasDao.ApproveItems> getApprovalList() {
        return approvalList;
    }

    public void sethistoryList(ArrayList<ApproveDatasDao.ApproveItems> historyList) {
        this.historyList = historyList;
    }

    public ArrayList<ApproveDatasDao.ApproveItems> getHistoryList() {
        return historyList;
    }
    //------------------------------方便其他Fragment获取到List数据.

    /**
     * 新增审批，返回按钮事件
     *
     * @param view
     */
    @Event(value = {R.id.message_approve_back, R.id.message_approve_add, R.id.empty_text})
    private void approveOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_approve_back:
                mActivity.finish();
                break;
            case R.id.message_approve_add:
                MyToast.showShort(mActivity, "新建审批");
                Intent intent = new Intent();
                intent.setClass(mActivity, ApproveNewActivity.class);
                intent.putExtra("json", mJson);
                startActivity(intent);
                break;
            case R.id.empty_text:
                getData();
                break;
        }
    }

    /**
     * 待我审批，审批中，审批历史切换事件
     *
     * @param view
     */
    @Event(value = {R.id.approve_wait, R.id.approve_approval, R.id.approve_historical})
    private void approveChangeOnClick(View view) {
        BaseFragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("json", mJson);

        switch (view.getId()) {
            case R.id.approve_wait:
                fragment = new TopApproveWaitFragment();
                fragmentTag = "approve_wait";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                MyToast.showShort(mActivity, "待我审批");
                break;
            case R.id.approve_approval:
                fragment = new TopApproveApprovalFragment();
                fragmentTag = "approve_approval";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                MyToast.showShort(mActivity, "审批中");
                break;
            case R.id.approve_historical:
                fragment = new TopApproveHistoricalFragment();
                fragmentTag = "approve_history";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                MyToast.showShort(mActivity, "历史审批");
                break;
        }
        fragment.setArguments(bundle);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.approve_contant, fragment, fragmentTag)
                .commit();
    }

    /**
     * 设置切换按钮的背景颜色为默认状态
     */
    private void setBackgroundDefult() {
        wait.setBackgroundResource(R.drawable.approve_left_kongxin);
        wait.setTextColor(Color.rgb(0, 0, 0));
        approval.setBackgroundResource(R.drawable.approve_middle_kongxin);
        approval.setTextColor(Color.rgb(0, 0, 0));
        historical.setBackgroundResource(R.drawable.approve_right_kongxin);
        historical.setTextColor(Color.rgb(0, 0, 0));
    }

    /**
     * 设置切换按钮的背景颜色为选中状态
     */
    private void setBackgroundChack(TextView view) {

        switch (view.getId()) {
            case R.id.approve_wait:
                view.setBackgroundResource(R.drawable.approve_left_shixin);
                break;
            case R.id.approve_approval:
                view.setBackgroundResource(R.drawable.approve_middle_shixin);
                break;
            case R.id.approve_historical:
                view.setBackgroundResource(R.drawable.approve_right_shixin);
                break;
        }

        view.setTextColor(Color.rgb(255, 255, 255));
    }
}

package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.WebViewApproveActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowApprovalItemAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveDataItemsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 我执行的审批--已改成我发起的审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_approval)
public class TopApproveWorkingFragment extends BaseFragment {

    @ViewInject(R.id.approve_approval_list)
    private PullableListView listView;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;


    private MessageApproveBean messageApproveBean;
    private ArrayList<MessageApproveDataItemsBean> dataItemsBeenList;
    private ApproveShowApprovalItemAdapter approvalAdapter;

    private PostJsonBean pushWorkingBean;
    private String get_start = "0";

    private String type = "";

    private void initList() {
        pushWorkingBean = new PostJsonBean();

        if (dataItemsBeenList == null) {
            dataItemsBeenList = new ArrayList<>();
        }
        dataItemsBeenList.clear();

        if (approvalAdapter == null) {
            approvalAdapter = new ApproveShowApprovalItemAdapter(mActivity, dataItemsBeenList);
        }
        listView.setAdapter(approvalAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebViewApproveActivity.class);
                //目前还没有跳转字段
                intent.putExtra("type", "workflow");
                intent.putExtra("url", dataItemsBeenList.get(position).url);
                intent.putExtra("id", dataItemsBeenList.get(position).id);
                intent.putExtra("title", dataItemsBeenList.get(position).title);
//                Bundle bundle = new Bundle();
//                bundle.putString("task_id", formate_list.get(position).id);
//                intent.putExtra("isEdited", bundle);
                mActivity.startActivity(intent);

            }
        });
    }

    @Override
    public void initData() {

        type = getTag();

        initList();
        /**
         * 刷新///加载     的操作
         */
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                showProgressDialog();
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        get_start = "0";
                        dataItemsBeenList.clear();
                        getData();
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                showProgressDialog();
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        pushWorkingBean.start = get_start;//设置开始查询
                        getData();
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });


        //getData();
    }

    @Override
    public void onStart() {
        super.onStart();
        get_start = "0";
        dataItemsBeenList.clear();
        getData();

    }

    private void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);

        //workflow_finished
        //workflow_joined
        params.addBodyParameter("source_id", type);

        pushWorkingBean.start = get_start;//设置开始查询
        pushWorkingBean.limit = "10";
        params.addBodyParameter("data", pushWorkingBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushWorkingBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取正在进行审批消息----------------" + strJson);
                messageApproveBean = GsonUtil.getMessageApproveBean(strJson);
                if (messageApproveBean.code.equals("0")) {//数据正确
                    formateDatas(messageApproveBean.data.items);//格式化信息
                    if (messageApproveBean.data.items != null) {
                        if (messageApproveBean.data.count.equals("0")) {
                            MyToast.showLong(mActivity, "没有任何消息可查看!");
                        } else if (!(messageApproveBean.data.items.size() > 0)) {
                            dataItemsBeenList.addAll(messageApproveBean.data.items);
                            get_start = dataItemsBeenList.size() + "";//设置开始查询
                            approvalAdapter.notifyDataSetChanged();
                        } else {
                            MyToast.showLong(mActivity, "没有任何消息可查看!");
                        }
                    }
                } else {
                    MyToast.showLong(mActivity, "获取消息失败-"/*+taskBean.msg*/);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
    }


    /**
     * 格式化从后台拿过来的数据
     */
    private void formateDatas(ArrayList<MessageApproveDataItemsBean> list) {
        for (MessageApproveDataItemsBean bean : list) {
            String time = bean.ctime;
            time = time.replace("/Date(", "");
            time = time.replace(")/", "");
            bean.ctime = time;
            time = bean.node_time;
            time = time.replace("/Date(", "");
            time = time.replace(")/", "");
            bean.node_time = time;

        }
    }
}

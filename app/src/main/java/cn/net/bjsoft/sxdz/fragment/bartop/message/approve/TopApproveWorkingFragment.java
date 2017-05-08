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
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowApprovalItemAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopApproveFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 我执行的审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_approval)
public class TopApproveWorkingFragment extends BaseFragment {

    @ViewInject(R.id.approve_approval_list)
    private PullableListView listView;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;




    private TopApproveFragment fragment = null;

    private ArrayList<ApproveDatasDao.ApproveItems> approvalList;
    private ApproveShowApprovalItemAdapter approvalAdapter;

    private PostJsonBean pushWorkingBean;
    private String get_start = "0";
    private String get_count = "0";

    private void initList(){
        pushWorkingBean = new PostJsonBean();

        if (approvalList==null) {
            approvalList = new ArrayList<>();
        }
        approvalList.clear();


    }

    @Override
    public void initData() {
        initList();


        if (approvalAdapter == null) {
            approvalAdapter = new ApproveShowApprovalItemAdapter(mActivity, approvalList);
        }
        listView.setAdapter(approvalAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", approvalList.get(position).url);
                intent.putExtra("type", approvalList.get(position).type);
                startActivity(intent);

            }
        });



        /**
         * 刷新///加载     的操作
         */
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        get_start = "0";
                        approvalList.clear();

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        if (!get_start.equals(get_count)) {
                            pushWorkingBean.start = get_start;//设置开始查询
                            LogUtil.e("onLoadMore-----------");
                            getData();
                        } else {
                            MyToast.showLong(mActivity, "已经没有更多的消息了!");
                        }
                        LogUtil.e("onLoadMore-----------");
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });


        getData();
    }


    private void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "workflow_working");

        pushWorkingBean.start = get_start;//设置开始查询
        pushWorkingBean.limit = "10";
        params.addBodyParameter("data", pushWorkingBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushWorkingBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取正在进行审批消息----------------" + strJson);
//                taskBean = GsonUtil.getMessageTaskBean(strJson);
//                if (taskBean.code.equals("0")) {
//                    tasksDoingDao.addAll(taskBean.data.items);
//                    get_start = tasksDoingDao.size() + "";//设置开始查询
//                    get_count = taskBean.data.count + "";
//
//                    formateDatas();//格式化信息
//
//                    taskAdapter.notifyDataSetChanged();
//                    if (get_count.equals("0")) {
//                        MyToast.showLong(mActivity, "没有任何消息可查看!");
//                    }
//                } else {
//                    MyToast.showLong(mActivity, "获取消息失败-"/*+taskBean.msg*/);
//                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取消息-----------失败方法-----" + element.getMethodName());
                }

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
//    private void formateDatas() {
//        for (MessageTaskBean.TasksAllDao dao : tasksDoingDao) {
//            if (SPUtil.getUserId(mActivity).equals(dao.userid)) {
//                dao.is_executant = 0;
//
//                String time = dao.starttime;
//                time = time.replace("/Date(", "");
//                time = time.replace(")/", "");
//                dao.starttime = time;
//                time = dao.limittime;
//                time = time.replace("/Date(", "");
//                time = time.replace(")/", "");
//                dao.limittime = time;
//
//            }
//        }
//    }


}

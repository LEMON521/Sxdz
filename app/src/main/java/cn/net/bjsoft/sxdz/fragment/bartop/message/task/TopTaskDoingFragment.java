package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.WebViewApproveActivity;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskAllZDLFAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.dialog.TaskSearchPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 进行中任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_doing)
public class TopTaskDoingFragment extends BaseFragment {

    @ViewInject(R.id.root_view)
    private FrameLayout root_view;

    @ViewInject(R.id.fragment_task_list_doing)
    private PullableListView task_list;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;

    private PostJsonBean pushDoingBean;
    private MessageTaskBean taskBean;
    //private ArrayList<MessageTaskBean.TasksAllDao> tasksAllDao;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksDoingDao;
    private TaskAllZDLFAdapter taskAdapter;

    private String get_start = "0";
    private String get_count = "0";
    private String source_id = "";

    private MessageTaskBean.TaskQueryDao taskQueryDao;
    private TaskSearchPopupWindow window;


    @Override
    public void initData() {
        pushDoingBean = new PostJsonBean();
        source_id = getArguments().getString("source_id");
        if (!TextUtils.isEmpty(source_id)){
            pushDoingBean.data.source_id = source_id;
        }else {
            pushDoingBean.data.source_id  = SPUtil.getUsers_SourceId(mActivity);
        }

        if (tasksDoingDao == null) {
            tasksDoingDao = new ArrayList<>();
        } else {
            tasksDoingDao.clear();
        }

        if (taskAdapter == null) {
            taskAdapter = new TaskAllZDLFAdapter(mActivity, tasksDoingDao);
        }
        task_list.setAdapter(taskAdapter);

        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebViewApproveActivity.class);
                //目前还没有跳转字段
                intent.putExtra("type", "workflow");
                intent.putExtra("url", tasksDoingDao.get(position).url);
                intent.putExtra("id", tasksDoingDao.get(position).id);
                intent.putExtra("title", tasksDoingDao.get(position).title);
//                Bundle bundle = new Bundle();
//                bundle.putString("task_id", formate_list.get(position).id);
//                intent.putExtra("isEdited", bundle);
                mActivity.startActivity(intent);
//                Intent intent = new Intent(mActivity, TaskDetailActivity.class);
//                intent.putExtra("fragment_name", "task_detail");
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("isEdited", !tasksDoingDao.get(position).finished);
//                bundle.putString("task_id", tasksDoingDao.get(position).id);
//                intent.putExtra("isEdited", bundle);
//                mActivity.startActivity(intent);
            }
        });

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
//                        tasksAllDao.clear();
                        tasksDoingDao.clear();
                        LogUtil.e("setOnRefreshListener-----------");
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
                        if (!get_start.equals(get_count)) {
                            pushDoingBean.start = get_start;//设置开始查询
                            LogUtil.e("onLoadMore-----------");
                            getData();
                        } else {
                            MyToast.showShort(mActivity, "已经没有更多的消息了!");
                            dismissProgressDialog();
                        }
                        LogUtil.e("onLoadMore-----------");
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });
        window = new TaskSearchPopupWindow(mActivity, root_view);
        /**
         * 接口回调
         */
        window.setOnData(new TaskSearchPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String strJson) {
                taskBean = GsonUtil.getMessageTaskBean(strJson);
                if (taskBean.code.equals("0")) {
//                    tasksAllDao.clear();
//                    tasksAllDao.addAll(taskBean.data.task_list);
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });


        getData();
    }
    @Override
    public void onStart() {
        super.onStart();
        get_start = "0";
        tasksDoingDao.clear();
        getData();
    }

    private void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "task_working");

        pushDoingBean.start = get_start;//设置开始查询
        pushDoingBean.limit = "10";
        params.addBodyParameter("data", pushDoingBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushDoingBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取消息----------------" + strJson);
                taskBean = GsonUtil.getMessageTaskBean(strJson);
                if (taskBean.code.equals("0")) {
                    tasksDoingDao.addAll(taskBean.data.items);
                    get_start = tasksDoingDao.size() + "";//设置开始查询
                    get_count = taskBean.data.count + "";

                    formateDatas();//格式化信息

                    taskAdapter.notifyDataSetChanged();
                    if (get_count.equals("0")) {
                        MyToast.showShort(mActivity, "没有任何消息可查看!");
                    }
                } else {
                    MyToast.showShort(mActivity, "获取消息失败-"/*+taskBean.msg*/);
                }

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
    private void formateDatas() {
        for (MessageTaskBean.TasksAllDao dao : tasksDoingDao) {
            if (SPUtil.getUserId(mActivity).equals(dao.userid)) {
                dao.is_executant = 0;

                String time = dao.starttime;
                time = time.replace("/Date(", "");
                time = time.replace(")/", "");
                dao.starttime = time;
                time = dao.limittime;
                time = time.replace("/Date(", "");
                time = time.replace(")/", "");
                dao.limittime = time;

            }
        }
    }
//    private void sortData() {
//        for (MessageTaskBean.TasksAllDao dao : tasksAllDao) {
//            //
//            if (dao.state == 2) {
//                dao.state = -1;
//                tasksDoingDao.add(dao);
//            }
//        }
//        taskAdapter.notifyDataSetChanged();
//    }

    @Event(value = {R.id.fragment_task_list_all_search})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.fragment_task_list_all_search:

                window.showWindow(taskQueryDao);

                break;
        }
    }
}

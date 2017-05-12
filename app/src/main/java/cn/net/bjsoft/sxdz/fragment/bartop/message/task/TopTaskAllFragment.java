package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import cn.net.bjsoft.sxdz.activity.home.bartop.message.TaskDetailActivity;
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
 * 全部任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_all)
public class TopTaskAllFragment extends BaseFragment {

    @ViewInject(R.id.root_view)
    private FrameLayout root_view;

    @ViewInject(R.id.fragment_task_list_all)
    private PullableListView task_list;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;


    private MessageTaskBean taskBean;
    private MessageTaskBean taskCacheBean;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksAllDao;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksCacheAllDao;
    private TaskAllZDLFAdapter taskAdapter;

    private MessageTaskBean.TaskQueryDao taskQueryDao;
    private TaskSearchPopupWindow window;

    private PostJsonBean pushDoneBean;
    private String get_start = "0";
    private String get_count = "0";
    private String source_id = "";

    @Override
    public void initData() {

        pushDoneBean = new PostJsonBean();

        if (tasksAllDao == null) {
            tasksAllDao = new ArrayList<>();
        } else {
            tasksAllDao.clear();
        }
        if (tasksCacheAllDao == null) {
            tasksCacheAllDao = new ArrayList<>();
        } else {
            tasksCacheAllDao.clear();
        }

        if (taskAdapter == null) {
            taskAdapter = new TaskAllZDLFAdapter(mActivity, tasksAllDao);
        }

        task_list.setAdapter(taskAdapter);

        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, TaskDetailActivity.class);
                intent.putExtra("fragment_name", "task_detail");
                mActivity.startActivity(intent);
            }
        });

        /**
         * 刷新///加载     的操作
         */
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                //showProgressDialog();
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        get_start = "0";
                        //tasksAllDao.clear();
                        tasksAllDao.clear();
                        tasksCacheAllDao.clear();
                        LogUtil.e("setOnRefreshListener-----------");
                        getData();

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                //showProgressDialog();
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        if (!get_start.equals(get_count)){
                            pushDoneBean.start = get_start;//设置开始查询
                            LogUtil.e("onLoadMore-----------");
                            getData();
                        }else {
                            MyToast.showShort(mActivity,"已经没有更多的消息了!");
                            dismissProgressDialog();
                        }
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });

        window = new TaskSearchPopupWindow(mActivity,root_view);

        window.setOnData(new TaskSearchPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String strJson) {
                taskCacheBean = GsonUtil.getMessageTaskBean(strJson);
                if (taskCacheBean.code.equals("0")) {
                    tasksAllDao.clear();
                    tasksAllDao.addAll(taskCacheBean.data.items);
                    taskAdapter.notifyDataSetChanged();
                }
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
        params.addBodyParameter("source_id", "task_all");

        pushDoneBean.start = get_start;//设置开始查询
        pushDoneBean.limit = "10";
        params.addBodyParameter("data", pushDoneBean.toString());
        LogUtil.e("-------------------------bean.toString()"+pushDoneBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取消息----------------" + strJson);
                taskBean = GsonUtil.getMessageTaskBean(strJson);
                if (taskBean.code.equals("0")) {
                    tasksAllDao.addAll(taskBean.data.items);
                    get_start = tasksAllDao.size() + "";//设置开始查询
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
        for (MessageTaskBean.TasksAllDao dao : tasksAllDao) {
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

    @Event(value = {R.id.fragment_task_list_all_search})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.fragment_task_list_all_search:

                window.showWindow(taskQueryDao);

                break;
        }
    }
}

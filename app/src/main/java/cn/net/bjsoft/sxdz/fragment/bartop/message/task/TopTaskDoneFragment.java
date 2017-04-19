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
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.TaskDetailActivity;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskAllZDLFAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.dialog.TaskSearchPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 已完成任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_done)
public class TopTaskDoneFragment extends BaseFragment {

    @ViewInject(R.id.root_view)
    private FrameLayout root_view;

    @ViewInject(R.id.fragment_task_list_done)
    private PullableListView task_list;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;

    private MessageTaskBean taskBean;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksAllDao;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksDoneDao;
    private TaskAllZDLFAdapter taskAdapter;

    private MessageTaskBean.TaskQueryDao taskQueryDao;
    private TaskSearchPopupWindow window;


    @Override
    public void initData() {


        if (tasksAllDao == null) {
            tasksAllDao = new ArrayList<>();
        } else
            tasksAllDao.clear();
        if (tasksDoneDao == null) {
            tasksDoneDao = new ArrayList<>();
        } else
            tasksDoneDao.clear();

        if (taskAdapter == null) {
            taskAdapter = new TaskAllZDLFAdapter(mActivity, tasksDoneDao);
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

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                        tasksAllDao.clear();
                        LogUtil.e("setOnRefreshListener-----------");
                        getData();

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

                        LogUtil.e("onLoadMore-----------");
                        getData();
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });

        window = new TaskSearchPopupWindow(mActivity,root_view);

        /**
         * 搜索框接口回调
         */
        window.setOnData(new TaskSearchPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String strJson) {
                taskBean = GsonUtil.getMessageTaskBean(strJson);
                if (taskBean.result) {
                    tasksAllDao.clear();
                    tasksAllDao.addAll(taskBean.data.task_list);
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });

        getData();
    }

    private void getData() {
        showProgressDialog();
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_do_list_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                taskBean = GsonUtil.getMessageTaskBean(result);
                if (taskBean.result) {
                    taskQueryDao = taskBean.data.query_dao;
                    //LogUtil.e("获取到的条目-----------" + result);
                    //tasksAllDao.clear();
                    tasksAllDao.addAll(taskBean.data.task_list);
                    //taskAdapter.notifyDataSetChanged();
                    sortData();
                    taskBean = null;
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
            }
        });
    }

    private void sortData(){
        for (MessageTaskBean.TasksAllDao dao :tasksAllDao){
            //
            if (dao.state==1){
                dao.state = -1;
                tasksDoneDao.add(dao);
            }
        }
        taskAdapter.notifyDataSetChanged();
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

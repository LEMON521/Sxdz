package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskAllZDLFAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.RefreshListView_1;

/**
 * 我发布的任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_my_publish)
public class TopTaskMyPublishFragment extends BaseFragment {
    @ViewInject(R.id.fragment_task_list_my_publish)
    private RefreshListView_1 task_list;

    private MessageTaskBean taskBean;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksAllDao;
    private TaskAllZDLFAdapter taskAdapter;


    @Override
    public void initData() {

        if (tasksAllDao == null) {
            tasksAllDao = new ArrayList<>();
        } else
            tasksAllDao.clear();

        if (taskAdapter == null) {
            taskAdapter = new TaskAllZDLFAdapter(mActivity, tasksAllDao);
        }

        task_list.setAdapter(taskAdapter);

        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name", "task_detail");
                mActivity.startActivity(intent);
            }
        });

        task_list.setOnRefreshListener(new RefreshListView_1.OnRefreshListener() {
            @Override
            public void pullDownRefresh() {
                //SystemClock.sleep(2000);
                LogUtil.e("下拉刷新");
                taskAdapter.notifyDataSetChanged();
                task_list.onRefreshFinish();
            }

            @Override
            public void pullUpLoadMore() {
                LogUtil.e("上啦加载");
                taskAdapter.notifyDataSetChanged();
                task_list.onRefreshFinish();
                //SystemClock.sleep(2000);
            }
        });


        getData();
    }

    private void getData() {
        showProgressDialog();
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_list_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                taskBean = GsonUtil.getMessageTaskBean(result);
                if (taskBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    tasksAllDao.clear();
                    tasksAllDao.addAll(taskBean.data);
                    taskAdapter.notifyDataSetChanged();
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
}

package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskZDLFAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.dialog.TaskQueryPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.RefreshListView_1;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_task)
public class TopTaskFragment extends BaseFragment {

    @ViewInject(R.id.message_task_title)
    private TextView title;
    @ViewInject(R.id.message_task_query)
    private ImageView query;

    @ViewInject(R.id.fragment_task_all)
    private TextView task_all;
    @ViewInject(R.id.fragment_task_branch)
    private TextView task_branch;
    @ViewInject(R.id.fragment_task_on)
    private TextView task_on;
    @ViewInject(R.id.fragment_task_off)
    private TextView task_off;
    @ViewInject(R.id.fragment_task_mine)
    private TextView task_mine;

    @ViewInject(R.id.fragment_task_list)
    private RefreshListView_1 task_list;

    private MessageTaskBean taskBean;
    private ArrayList<MessageTaskBean.TasksDao> tasksDaos;
    private TaskZDLFAdapter taskAdapter;


    private TaskQueryPopupWindow window;

    @Override
    public void initData() {
        title.setText("任务");

        window = new TaskQueryPopupWindow(mActivity, query);


        window.setOnData(new TaskQueryPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(HashMap<String, String> content) {

                MyToast.showShort(mActivity, "查找任务");
                /**
                 * 在这里处理数据,并将数据发送到服务器上
                 *
                 * 两个时间并非是时间戳,应该传换成时间戳再做进一步处理
                 */


            }
        });

        taskChange(task_all);


        if (tasksDaos == null) {
            tasksDaos = new ArrayList<>();
        } else
            tasksDaos.clear();

        if (taskAdapter == null) {
            taskAdapter = new TaskZDLFAdapter(mActivity, tasksDaos);
        }

        task_list.setAdapter(taskAdapter);

        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name","task_detail");
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
    }

    @Event(value = {R.id.message_task_back, R.id.message_task_add, R.id.message_task_query})
    private void taskOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_task_back:
                mActivity.finish();
                break;
            case R.id.message_task_add:
                MyToast.showShort(mActivity, "添加新任务");
                break;
            case R.id.message_task_query:
                //MyToast.showShort(mActivity,"添加新任务");
                window.showWindow();
                break;

        }
    }
//
//    private TextView task_all;
//    private TextView task_branch;
//    private TextView task_on;
//    private TextView task_off;
//    private TextView task_mine;

    @Event(value = {R.id.fragment_task_all
            , R.id.fragment_task_branch
            , R.id.fragment_task_on
            , R.id.fragment_task_off
            , R.id.fragment_task_mine})
    private void taskChange(View view) {
        {//先把全部设置成默认的样式
            task_all.setTextColor(Color.parseColor("#000000"));
            task_all.setBackgroundResource(R.drawable.approve_left_kongxin);
            task_branch.setTextColor(Color.parseColor("#000000"));
            task_branch.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_on.setTextColor(Color.parseColor("#000000"));
            task_on.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_off.setTextColor(Color.parseColor("#000000"));
            task_off.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_mine.setTextColor(Color.parseColor("#000000"));
            task_mine.setBackgroundResource(R.drawable.approve_right_kongxin);
        }

        switch (view.getId()) {
            case R.id.fragment_task_all:
                task_all.setTextColor(Color.parseColor("#FFFFFF"));
                task_all.setBackgroundResource(R.drawable.approve_left_shixin);
                getData();
                break;
            case R.id.fragment_task_branch:
                task_branch.setTextColor(Color.parseColor("#FFFFFF"));
                task_branch.setBackgroundResource(R.drawable.approve_middle_shixin);
                getData();
                break;
            case R.id.fragment_task_on:
                task_on.setTextColor(Color.parseColor("#FFFFFF"));
                task_on.setBackgroundResource(R.drawable.approve_middle_shixin);
                getData();
                break;
            case R.id.fragment_task_off:
                task_off.setTextColor(Color.parseColor("#FFFFFF"));
                task_off.setBackgroundResource(R.drawable.approve_middle_shixin);
                getData();
                break;
            case R.id.fragment_task_mine:
                task_mine.setTextColor(Color.parseColor("#FFFFFF"));
                task_mine.setBackgroundResource(R.drawable.approve_right_shixin);
                getData();
                break;

        }

        /**
         * 这里添加切换任务
         */
    }

    private void getData() {
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_list_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                taskBean = GsonUtil.getMessageTaskBean(result);
                if (taskBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    tasksDaos.clear();
                    tasksDaos.addAll(taskBean.data);
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
            }
        });
    }
}

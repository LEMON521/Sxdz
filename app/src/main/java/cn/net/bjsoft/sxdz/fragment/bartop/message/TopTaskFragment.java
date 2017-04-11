package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.dialog.TaskSearchPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskAllFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskDoingFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskDoneFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskMyPublishFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskUnderlingFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;

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

    private TaskSearchPopupWindow window;

    private int viewId;
    private String fragmentTag = "";
    private String strJson = "";

    private MessageTaskBean taskBean;
    private MessageTaskBean.TaskQueryDao queryDao;


    @Override
    public void initData() {
        title.setText("任务");

        window = new TaskSearchPopupWindow(mActivity, query);

        window.setOnData(new TaskSearchPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String strJson) {

                MyToast.showShort(mActivity, "查找任务");
                /**
                 * 在这里处理数据,并将数据发送到服务器上
                 *
                 * 两个时间并非是时间戳,应该传换成时间戳再做进一步处理
                 */


            }
        });

        taskChange(task_all);
    }

    private void getData() {
        showProgressDialog();
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_list_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                taskBean = GsonUtil.getMessageTaskBean(result);
                if (taskBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    strJson = result;
                    queryDao = taskBean.data.query_dao;
                    changeFragment();
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


    @Event(value = {R.id.message_task_back, R.id.message_task_add, R.id.message_task_query})
    private void taskOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_task_back:
                mActivity.finish();
                break;
            case R.id.message_task_add:
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name", "addTaskFragment");
                mActivity.startActivity(intent);
                break;
            case R.id.message_task_query:
                //MyToast.showShort(mActivity,"添加新任务");
                //window.showWindow();

                window.showWindow(queryDao);
                break;

        }
    }


    @Event(value = {R.id.fragment_task_all
            , R.id.fragment_task_branch
            , R.id.fragment_task_on
            , R.id.fragment_task_off
            , R.id.fragment_task_mine})
    private void taskChange(View view) {
        //showProgressDialog();
        //getData();
        {//先把全部设置成默认的样式
            task_all.setTextColor(Color.parseColor("#999999"));
            task_all.setBackgroundResource(R.drawable.approve_left_kongxin);
            task_branch.setTextColor(Color.parseColor("#999999"));
            task_branch.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_on.setTextColor(Color.parseColor("#999999"));
            task_on.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_off.setTextColor(Color.parseColor("#999999"));
            task_off.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_mine.setTextColor(Color.parseColor("#999999"));
            task_mine.setBackgroundResource(R.drawable.approve_right_kongxin);
        }

        viewId = view.getId();

        switch (view.getId()) {
            case R.id.fragment_task_all:
                task_all.setTextColor(Color.parseColor("#FFFFFF"));
                task_all.setBackgroundResource(R.drawable.approve_left_shixin);
                break;
            case R.id.fragment_task_branch:
                task_branch.setTextColor(Color.parseColor("#FFFFFF"));
                task_branch.setBackgroundResource(R.drawable.approve_middle_shixin);
                break;
            case R.id.fragment_task_on:
                task_on.setTextColor(Color.parseColor("#FFFFFF"));
                task_on.setBackgroundResource(R.drawable.approve_middle_shixin);
                break;
            case R.id.fragment_task_off:
                task_off.setTextColor(Color.parseColor("#FFFFFF"));
                task_off.setBackgroundResource(R.drawable.approve_middle_shixin);
                break;
            case R.id.fragment_task_mine:
                task_mine.setTextColor(Color.parseColor("#FFFFFF"));
                task_mine.setBackgroundResource(R.drawable.approve_right_shixin);
                break;
        }
        getData();

    }

    /**
     * 切换Fragment
     */
    private void changeFragment() {
        BaseFragment fragment = null;

        Bundle bundle = new Bundle();
        switch (viewId) {
            case R.id.fragment_task_all:
                task_all.setTextColor(Color.parseColor("#FFFFFF"));
                task_all.setBackgroundResource(R.drawable.approve_left_shixin);
                fragment = new TopTaskAllFragment();
                fragmentTag = "TopTaskAllFragment";
                //getData();
                break;
            case R.id.fragment_task_branch:
                task_branch.setTextColor(Color.parseColor("#FFFFFF"));
                task_branch.setBackgroundResource(R.drawable.approve_middle_shixin);
                fragment = new TopTaskUnderlingFragment();
                fragmentTag = "TopTaskUnderlingFragment";
                //getData();
                break;
            case R.id.fragment_task_on:
                task_on.setTextColor(Color.parseColor("#FFFFFF"));
                task_on.setBackgroundResource(R.drawable.approve_middle_shixin);
                fragment = new TopTaskDoingFragment();
                fragmentTag = "TopTaskDoingFragment";
                //getData();
                break;
            case R.id.fragment_task_off:
                task_off.setTextColor(Color.parseColor("#FFFFFF"));
                task_off.setBackgroundResource(R.drawable.approve_middle_shixin);
                fragment = new TopTaskDoneFragment();
                fragmentTag = "TopTaskDoneFragment";
                //getData();
                break;
            case R.id.fragment_task_mine:
                task_mine.setTextColor(Color.parseColor("#FFFFFF"));
                task_mine.setBackgroundResource(R.drawable.approve_right_shixin);
                fragment = new TopTaskMyPublishFragment();
                fragmentTag = "TopTaskMyPublishFragment";
                //getData();
                break;

        }

        /**
         * 这里添加切换任务
         */
        bundle.putString("taskJson", strJson);
        bundle.putString("json", "");
        fragment.setArguments(bundle);
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_task_content, fragment, fragmentTag)
                .commit();

    }


}

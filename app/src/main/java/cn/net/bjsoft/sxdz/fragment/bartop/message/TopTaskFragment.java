package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.dialog.TaskQueryPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskAllFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskDoingFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskDoneFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskMyPublishFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskUnderlingFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

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



    }

    @Event(value = {R.id.message_task_back, R.id.message_task_add, R.id.message_task_query})
    private void taskOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_task_back:
                mActivity.finish();
                break;
            case R.id.message_task_add:
                Intent intent = new Intent(mActivity,EmptyActivity.class);
                intent.putExtra("fragment_name","addTaskFragment");
                mActivity.startActivity(intent);
                break;
            case R.id.message_task_query:
                //MyToast.showShort(mActivity,"添加新任务");
                window.showWindow();
                break;

        }
    }

    @Event(value = {R.id.fragment_task_all
            , R.id.fragment_task_mine
            , R.id.fragment_task_on
            , R.id.fragment_task_branch
            , R.id.fragment_task_off
            })
    private void taskChange(View view) {
        //showProgressDialog();

        {//先把全部设置成默认的样式
            task_all.setTextColor(Color.parseColor("#999999"));
            task_all.setBackgroundResource(R.drawable.approve_left_kongxin);
            task_mine.setTextColor(Color.parseColor("#999999"));
            task_mine.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_on.setTextColor(Color.parseColor("#999999"));
            task_on.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_branch.setTextColor(Color.parseColor("#999999"));
            task_branch.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_off.setTextColor(Color.parseColor("#999999"));
            task_off.setBackgroundResource(R.drawable.approve_right_kongxin);

        }

        BaseFragment fragment = null;
        String tag = "";
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.fragment_task_all:
                task_all.setTextColor(Color.parseColor("#FFFFFF"));
                task_all.setBackgroundResource(R.drawable.approve_left_shixin);
                fragment = new TopTaskAllFragment();
                tag = "TopTaskAllFragment";
                //getData();
                break;
            case R.id.fragment_task_mine:
                task_mine.setTextColor(Color.parseColor("#FFFFFF"));
                task_mine.setBackgroundResource(R.drawable.approve_middle_shixin);
                fragment = new TopTaskMyPublishFragment();
                tag = "TopTaskMyPublishFragment";
                //getData();
                break;
            case R.id.fragment_task_on:
                task_on.setTextColor(Color.parseColor("#FFFFFF"));
                task_on.setBackgroundResource(R.drawable.approve_middle_shixin);
                fragment = new TopTaskDoingFragment();
                tag = "TopTaskDoingFragment";
                //getData();
                break;
            case R.id.fragment_task_branch:
                task_branch.setTextColor(Color.parseColor("#FFFFFF"));
                task_branch.setBackgroundResource(R.drawable.approve_middle_shixin);
                fragment = new TopTaskUnderlingFragment();
                tag = "TopTaskUnderlingFragment";
                //getData();
                break;
            case R.id.fragment_task_off:
                task_off.setTextColor(Color.parseColor("#FFFFFF"));
                task_off.setBackgroundResource(R.drawable.approve_right_shixin);
                fragment = new TopTaskDoneFragment();
                tag = "TopTaskDoneFragment";
                //getData();
                break;


        }

        /**
         * 这里添加切换任务
         */
        bundle.putString("json","");
        fragment.setArguments(bundle);
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_task_content, fragment,tag)
                .commit();
    }


}
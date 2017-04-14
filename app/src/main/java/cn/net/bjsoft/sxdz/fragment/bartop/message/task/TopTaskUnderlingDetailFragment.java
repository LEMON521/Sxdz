package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_task_underling_detail)
public class TopTaskUnderlingDetailFragment extends BaseFragment {

    @ViewInject(R.id.title_title)
    private TextView title;
    @ViewInject(R.id.title_back)
    private ImageView back;

    @ViewInject(R.id.fragment_task_underling_detail_done)
    private TextView done;
    @ViewInject(R.id.fragment_task_underling_detail_doing)
    private TextView doing;
    @ViewInject(R.id.fragment_task_underling_detail_content)
    private LinearLayout content;


    @Override
    public void initData() {
        title.setText("下属任务");
        back.setVisibility(View.VISIBLE);


        taskChange(done);


    }

    @Event(value = {R.id.title_back, R.id.message_task_add, R.id.message_task_query})
    private void taskOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mActivity.finish();
                break;
            case R.id.message_task_add:
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name", "addTaskFragment");
                mActivity.startActivity(intent);
                break;
            case R.id.message_task_query:
                //MyToast.showShort(mActivity,"添加新任务");
                break;

        }
    }

    @Event(value = {R.id.fragment_task_underling_detail_done
            , R.id.fragment_task_underling_detail_doing})
    private void taskChange(View view) {
        //showProgressDialog();

        {//先把全部设置成默认的样式
            done.setTextColor(Color.parseColor("#999999"));
            done.setBackgroundResource(R.drawable.approve_left_kongxin);

            doing.setTextColor(Color.parseColor("#999999"));
            doing.setBackgroundResource(R.drawable.approve_right_kongxin);
        }

        BaseFragment fragment = null;
        String tag = "";
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.fragment_task_underling_detail_done:
                done.setTextColor(Color.parseColor("#FFFFFF"));
                done.setBackgroundResource(R.drawable.approve_left_shixin);
                fragment = new TopTaskDoneFragment();
                tag = "TopTaskDoneFragment";
                //getData();
                break;

            case R.id.fragment_task_underling_detail_doing:
                doing.setTextColor(Color.parseColor("#FFFFFF"));
                doing.setBackgroundResource(R.drawable.approve_right_shixin);
                fragment = new TopTaskDoingFragment();
                tag = "TopTaskDoingFragment";
                //getData();
                break;

        }

        /**
         * 这里添加切换任务
         */
        bundle.putString("json", "");
        fragment.setArguments(bundle);
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_task_underling_detail_content, fragment, tag)
                .commit();
    }


}
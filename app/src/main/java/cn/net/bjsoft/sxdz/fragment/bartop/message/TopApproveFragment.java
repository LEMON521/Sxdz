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

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.ApproveNewActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.TopApproveApplyFragment_new;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.TopApproveWorkingFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * 审批
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve)
public class TopApproveFragment extends BaseFragment {
    @ViewInject(R.id.message_approve_title)
    private TextView title;
    @ViewInject(R.id.message_approve_back)
    private ImageView back;

    @ViewInject(R.id.approve_apply)
    private TextView apply;
    @ViewInject(R.id.approve_working)
    private TextView working;
    @ViewInject(R.id.approve_finished)
    private TextView finished;
    @ViewInject(R.id.approve_joined)
    private TextView joined;



    private String fragmentTag = "";

    @Override
    public void initData() {
        title.setText("审批");
        back.setVisibility(View.VISIBLE);
        approveChangeOnClick(apply);

    }





    /**
     * 新增审批，返回按钮事件
     *
     * @param view
     */
    @Event(value = {R.id.message_approve_back, R.id.message_approve_add, R.id.empty_text})
    private void approveOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_approve_back:
                mActivity.finish();
                break;
            case R.id.message_approve_add:
                MyToast.showShort(mActivity, "新建审批");
                Intent intent = new Intent();
                intent.setClass(mActivity, ApproveNewActivity.class);
                intent.putExtra("json", mJson);
                startActivity(intent);
                break;
            case R.id.empty_text:
                break;
        }
    }

    /**
     * 待我审批，审批中，审批历史切换事件
     *
     * @param view
     */
    @Event(value = {R.id.approve_apply
            , R.id.approve_working
            , R.id.approve_finished
            ,R.id.approve_joined})
    private void approveChangeOnClick(View view) {
        BaseFragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("json", mJson);

        switch (view.getId()) {
            case R.id.approve_apply:
                fragment = new TopApproveWorkingFragment();
                fragmentTag = "workflow_apply";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                //MyToast.showShort(mActivity, "待我审批");
                break;
            case R.id.approve_working:
                fragment = new TopApproveApplyFragment_new();
                fragmentTag = "workflow_working";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                //MyToast.showShort(mActivity, "审批中");
                break;


            case R.id.approve_joined:
                fragment = new TopApproveWorkingFragment();
                //fragment = new TopApproveJoinedFragment();
                fragmentTag = "workflow_joined";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                //MyToast.showShort(mActivity, "历史审批");
                break;

            case R.id.approve_finished:
                //fragment = new TopApproveFinishedFragment();
                fragment = new TopApproveWorkingFragment();
                fragmentTag = "workflow_finished";
                setBackgroundDefult();
                setBackgroundChack((TextView) view);
                //MyToast.showShort(mActivity, "历史审批");
                break;
        }
        fragment.setArguments(bundle);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.approve_contant, fragment, fragmentTag)
                .commit();
    }

    /**
     * 设置切换按钮的背景颜色为默认状态
     */
    private void setBackgroundDefult() {
        apply.setBackgroundResource(R.drawable.approve_left_kongxin);
        apply.setTextColor(Color.rgb(0, 0, 0));
        working.setBackgroundResource(R.drawable.approve_middle_kongxin);
        working.setTextColor(Color.rgb(0, 0, 0));

        joined.setBackgroundResource(R.drawable.approve_middle_kongxin);
        joined.setTextColor(Color.rgb(0, 0, 0));
        finished.setBackgroundResource(R.drawable.approve_right_kongxin);
        finished.setTextColor(Color.rgb(0, 0, 0));
    }

    /**
     * 设置切换按钮的背景颜色为选中状态
     */
    private void setBackgroundChack(TextView view) {

        switch (view.getId()) {
            case R.id.approve_apply:
                view.setBackgroundResource(R.drawable.approve_left_shixin);
                break;
            case R.id.approve_working:
                view.setBackgroundResource(R.drawable.approve_middle_shixin);
                break;
            case R.id.approve_joined:
                view.setBackgroundResource(R.drawable.approve_middle_shixin);
                break;
            case R.id.approve_finished:
                view.setBackgroundResource(R.drawable.approve_right_shixin);
                break;
        }

        view.setTextColor(Color.rgb(255, 255, 255));
    }
}

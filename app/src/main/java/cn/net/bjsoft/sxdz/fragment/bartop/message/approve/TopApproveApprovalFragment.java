package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowApprovalItemAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopApproveFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 正在审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_approval)
public class TopApproveApprovalFragment extends BaseFragment {

    @ViewInject(R.id.approve_approval_list)
    private PullableListView listView;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;




    private TopApproveFragment fragment = null;

    private ArrayList<ApproveDatasDao.ApproveItems> approvalList;
    private ApproveShowApprovalItemAdapter approvalAdapter;

    @Override
    public void initData() {
        //mActivity.getFragmentManager().findFragmentByTag("approve_ylyd");
        fragment = (TopApproveFragment) mActivity.getSupportFragmentManager().findFragmentByTag("approve_ylyd");

        if (fragment==null) {
            fragment = (TopApproveFragment) mActivity.getSupportFragmentManager().findFragmentByTag("approve");
        }

        if (fragment != null) {
            approvalList = fragment.getApprovalList();
        }

        if (approvalAdapter == null) {
            approvalAdapter = new ApproveShowApprovalItemAdapter(mActivity, approvalList);
        }
        listView.setAdapter(approvalAdapter);
        Utility.setListViewHeightBasedOnChildren(listView);

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

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });
    }

}

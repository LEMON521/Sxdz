package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowHistoryItemAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopApproveFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 历史审批
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_approval)
public class TopApproveHistoricalFragment extends BaseFragment {

    @ViewInject(R.id.approve_approval_list)
    private PullableListView listView;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;


    private TopApproveFragment fragment = null;

    private ArrayList<ApproveDatasDao.ApproveItems> hostorylList;
    private ApproveShowHistoryItemAdapter historyAdapter;
    @Override
    public void initData() {
        //mActivity.getFragmentManager().findFragmentByTag("approve");
        fragment = (TopApproveFragment) mActivity.getSupportFragmentManager().findFragmentByTag("approve_ylyd");

        if (fragment==null) {
            fragment = (TopApproveFragment) mActivity.getSupportFragmentManager().findFragmentByTag("approve");
        }

        if (fragment!=null) {
            hostorylList = fragment.getHistoryList();
        }


        if (historyAdapter==null) {
            historyAdapter = new ApproveShowHistoryItemAdapter(mActivity,hostorylList);
        }
        listView.setAdapter(historyAdapter);
        Utility.setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyToast.showShort(mActivity,"点击了我"+position);
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

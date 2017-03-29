package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

/**
 * 正在审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_approval)
public class TopApproveApprovalFragment extends BaseFragment {
    @ViewInject(R.id.approve_approval_list)
    private ListView listView;


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
    }

}

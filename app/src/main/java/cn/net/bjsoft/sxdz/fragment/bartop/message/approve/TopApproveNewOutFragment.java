package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewOutAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveOutDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_out)
public class TopApproveNewOutFragment extends BaseFragment {

    @ViewInject(R.id.approve_new_out_entry)
    private ListView entry;
    @ViewInject(R.id.approve_new_out_total)
    private TextView total;
//
//    private Dialog dialog;
//    private ArrayList<String> result;
//    private ArrayList<String> list;

    //添加费用明细相关
    private ApproveOutDao outDao;
    private ArrayList<ApproveOutDao> dataList;
    private ApproveNewOutAdapter resListAdapter;


    @Override
    public void initData() {

        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        resListAdapter = new ApproveNewOutAdapter(mActivity, dataList);
        entry.setAdapter(resListAdapter);

        //默认添加一条数据
        outDao = new ApproveOutDao();
        dataList.add(outDao);
        resListAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(entry);
        entry.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽掉滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }




    @Event(value = {R.id.approve_new_out_add})
    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.approve_new_out_add://添加明细
                ApproveOutDao dao = new ApproveOutDao();
                dataList.add(dao);
                resListAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(entry);
                break;
        }
    }

    public ArrayList<ApproveOutDao> getEntryData() {

        return dataList;
    }

}

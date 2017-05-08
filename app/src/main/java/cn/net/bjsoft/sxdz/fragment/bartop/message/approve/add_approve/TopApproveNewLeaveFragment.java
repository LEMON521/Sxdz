package cn.net.bjsoft.sxdz.fragment.bartop.message.approve.add_approve;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewLeaveAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveLeaveDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_leave)
public class TopApproveNewLeaveFragment extends BaseFragment {

    @ViewInject(R.id.approve_new_leave_entry)
    private ListView entry;
    @ViewInject(R.id.approve_new_leave_total)
    private TextView total;
//    @ViewInject(R.id.approve_new_leave_mater)
//    private EditText type;

//
//    private Dialog dialog;
//    private ArrayList<String> result;
//    private ArrayList<String> list;

    //添加费用明细相关
    private ApproveLeaveDao leaveDao;
    private ArrayList<ApproveLeaveDao> dataList;
    private ApproveNewLeaveAdapter resListAdapter;
//
//    private ArrayList<String> typeList;
//    private ArrayAdapter<String> typeAdapter;

    @Override
    public void initData() {

//        typeList = new ArrayList<>();
//        typeList.add("事假");
//        typeList.add("病假");
//        typeList.add("年假");
//        typeList.add("调休");
//        typeList.add("婚假");
//        typeList.add("产假");
//        typeList.add("陪产假");
//        typeList.add("路途假");
//        typeList.add("其他");
//
//        typeAdapter = new ArrayAdapter<String>(mActivity)




        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        resListAdapter = new ApproveNewLeaveAdapter(mActivity, dataList);
        entry.setAdapter(resListAdapter);

        //默认添加一条数据
        leaveDao = new ApproveLeaveDao();
        dataList.add(leaveDao);
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




    @Event(value = {R.id.approve_new_leave_add})
    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.approve_new_leave_add://添加明细
                ApproveLeaveDao dao = new ApproveLeaveDao();
                dataList.add(dao);
                resListAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(entry);
                break;
        }
    }

    public ArrayList<ApproveLeaveDao> getEntryData() {

        return dataList;
    }
}

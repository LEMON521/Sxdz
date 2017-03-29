package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewResAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveResDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_res)
public class TopApproveNewResFragment extends BaseFragment {

    @ViewInject(R.id.approve_new_res_entry)
    private ListView entry;//

    private ApproveResDao approveResDao;
    private ArrayList<ApproveResDao> resList;
    private ApproveNewResAdapter resAdapter;


    @Override
    public void initData() {
        if (resList == null) {
            resList = new ArrayList<>();
        }



        if (resAdapter == null) {
            resAdapter = new ApproveNewResAdapter(mActivity, resList);
        }
        entry.setAdapter(resAdapter);

        //默认添加一条
        approveResDao = new ApproveResDao();
        resList.add(approveResDao);
        resAdapter.notifyDataSetChanged();
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


    @Event(value = {R.id.approve_new_res_add})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.approve_new_res_add://添加明细
                MyToast.showShort(mActivity,"新添加一条数据");
                approveResDao = new ApproveResDao();
                resList.add(approveResDao);
                resAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(entry);
                break;

        }
    }


    public ArrayList<ApproveResDao> getEntryData() {

        return resList;
    }

}

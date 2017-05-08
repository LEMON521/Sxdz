package cn.net.bjsoft.sxdz.fragment.bartop.message.approve.add_approve;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.ApproveNewParticularActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveNewDao;
import cn.net.bjsoft.sxdz.constant.ConstantApprove;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new)
public class TopApproveNewFragment extends BaseFragment {

    //附件
    @ViewInject(R.id.approve_new)
    private GridView open;

    //为每个Fragment添加TAG
    private String TAG = "";


    //上传附件相关
    private ApproveNewDao newDao = null;
    private ArrayList<ApproveNewDao> itemList;
    private ApproveNewAdapter itemListAdapter;

    @Override
    public void initData() {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_EXPENSES;
        newDao.name = "报销";
        itemList.add(newDao);

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_TRIP;
        newDao.name = "出差";
        itemList.add(newDao);

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_LEAVE;
        newDao.name = "请假";
        itemList.add(newDao);

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_OUT;
        newDao.name = "外出";
        itemList.add(newDao);

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_BUY;
        newDao.name = "采购";
        itemList.add(newDao);

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_AGREEMENT;
        newDao.name = "合同审批";
        itemList.add(newDao);

        newDao = new ApproveNewDao();
        newDao.particular_tag = ConstantApprove.NEW_RES;
        newDao.name = "物品领用";
        itemList.add(newDao);

        if (itemListAdapter == null) {
            itemListAdapter = new ApproveNewAdapter(mActivity, itemList);
        }

        open.setAdapter(itemListAdapter);

        open.setOnTouchListener(new View.OnTouchListener() {
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
        open.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ApproveNewParticularActivity.class);
                intent.putExtra("particular", itemList.get(position).particular_tag);
                intent.putExtra("title", itemList.get(position).name);
                intent.putExtra("json", "");
                mActivity.startActivity(intent);
                //mActivity.finish();
            }
        });
    }


    /**
     * 切换审批事项
     *
     * @param view
     */
    @Event(value = {})
    private void onChangeClick(View view) {

    }

    /**
     * 返回和提交
     *
     * @param view
     */
    @Event(value = {R.id.message_approve_new_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_approve_new_back://返回
                mActivity.finish();
                break;

        }
    }

}

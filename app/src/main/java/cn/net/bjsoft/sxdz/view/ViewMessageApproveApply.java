package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowWaiteItemAdapter_new;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveDataItemsBean;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * Created by Zrzc on 2017/5/9.
 */

public class ViewMessageApproveApply extends LinearLayout {
    private Context context;
    private View view;

    private TextView title;
    private ListView content;
    private ArrayList<MessageApproveDataItemsBean> datas;

    //private ArrayList<MessageApproveDataItemsBean> datas;
    private ApproveShowWaiteItemAdapter_new adapter;

    public ViewMessageApproveApply(Context context) {
        super(context);
        this.context = context;
        findView();
    }

    private void findView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_message_approve_apply_item, this, true);

        title = (TextView) view.findViewById(R.id.approve_apply_title);
        content = (ListView) view.findViewById(R.id.approve_apply_content);
        content.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽滑动事件
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
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyToast.showShort(context,"点击了-id"+datas.get(position).id);
            }
        });

    }

    public void setDatas(ArrayList<MessageApproveDataItemsBean> datas) {
        this.datas = datas;
        LogUtil.e("-----数量为-----"+datas.size());
        title.setText(datas.get(0).wf_type);
        if (adapter == null) {
            adapter = new ApproveShowWaiteItemAdapter_new(context, datas);
        }
//        if (content.getAdapter() != null) {
//            if (!(content.getAdapter().getCount() > 1)) {
//                content.setAdapter(adapter);
//            }
//        }
        content.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(content);
        adapter.notifyDataSetChanged();


    }

    public void refresh(){
        Utility.setListViewHeightBasedOnChildren(content);
        adapter.notifyDataSetChanged();
    }

    @Override

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}

package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveDataItemsBean;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;

/**
 * 审批-正在审批 ListView 的数据适配器
 * Created by 靳宁宁 on 2017/3/09.
 */

public class ApproveShowHistoryItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MessageApproveDataItemsBean> list;
    private int type = -1;

    public ApproveShowHistoryItemAdapter(Context context, ArrayList<MessageApproveDataItemsBean> list/*, int type*/) {
        this.context = context;
        this.list = list;
        //this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_approve_history, null);
            tag = new Holder();

            tag.time = (TextView) convertView.findViewById(R.id.approve_approval_time);
            tag.mater = (TextView) convertView.findViewById(R.id.approve_approval_mater);
            tag.state = (TextView) convertView.findViewById(R.id.approve_approval_state);
            tag.name = (TextView) convertView.findViewById(R.id.approve_approval_name);
            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.mater.setText(list.get(position).title);
//        LogUtil.e(list.get(position).time+"-------time------");
//
//        LogUtil.e(Long.parseLong(list.get(position).time)+"-------long------");
//        LogUtil.e(TimeUtils.getFormateDate(Long.parseLong(list.get(position).time),"/")+"-----date-----");
        holder.name.setText(UsersInforUtils.getInstance(context).getUserInfo(list.get(position).userid).nickname);
        holder.time.setText(TimeUtils.getFormateDate(Long.parseLong(list.get(position).ctime),"/"));
//        if (list.get(position).state.equals("history")){
//            //holder.state.setText("审批通过");
//            if (list.get(position).approve_result.equals("agree")){
//                holder.state.setText("已通过");
//                holder.state.setTextColor(Color.argb(255,42,194,128));
//            }else if (list.get(position).approve_result.equals("reject")){
//                holder.state.setText("驳回");
//                holder.state.setTextColor(Color.argb(255,242,38,38));
//            }else {
//                holder.state.setText("未知");
//                holder.state.setTextColor(Color.argb(255,255,255,255));
//            }
//        }else if (list.get(position).state.equals("approval")){
//            holder.state.setText("审批中");
//        }
        holder.state.setText(list.get(position).node_name);
        /**
         * 将list的数据设置到控件中
         */

        return convertView;
    }

    public static class Holder {


        public TextView state//状态
                , time//时间
                , mater
                , name;//事由
    }
}

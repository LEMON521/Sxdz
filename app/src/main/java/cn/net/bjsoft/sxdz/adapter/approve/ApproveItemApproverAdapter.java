package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.approve.ApproveContactDao;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveItemApproverAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApproveContactDao> list;

    public ApproveItemApproverAdapter(Context context, ArrayList<ApproveContactDao> list) {
        this.context = context;
        this.list = list;
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
                    R.layout.item_approve_approver, null);
            tag = new Holder();
            /**
             * 报销模块
             */
            tag.icon = (CircleImageView) convertView.findViewById(R.id.item_approve_approver_user_icon);
            tag.next = (ImageView) convertView.findViewById(R.id.item_approve_approver_next);
            tag.name = (TextView) convertView.findViewById(R.id.item_approve_approver_name);

//            tag.icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    list.remove(position);
//                    notifyDataSetChanged();
//                    Utility.setGridViewHeightBasedOnChildren((GridView) parent,4);
//                }
//            });

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();

        if (list.size() - 1 == position) {
            holder.icon.setImageResource(R.drawable.com_icon_add_1);
            holder.next.setVisibility(View.GONE);
        } else {
            holder.icon.setImageResource(R.drawable.tab_me_n);
            holder.next.setVisibility(View.VISIBLE);
        }
//        switch (list.get(position).tag){
//            case -1:
//                holder.icon.setImageResource(R.drawable.com_icon_add_1);
//                holder.next.setVisibility(View.INVISIBLE);
//                break;
//            case 1:
//                holder.icon.setImageResource(R.drawable.tab_me_n);
//                holder.next.setVisibility(View.VISIBLE);
//                break;
//        }
        holder.name.setText(list.get(position).name);

        return convertView;
    }

    public static class Holder {

        public CircleImageView icon;
        public ImageView next;
        public TextView name;//类型

    }
}

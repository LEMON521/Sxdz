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

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveShowItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<?> list;
    private int type = -1;

    public ApproveShowItemAdapter(Context context, ArrayList<?> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
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
                    R.layout.item_approve_show_details, null);
            tag = new Holder();
            /**
             * 报销模块
             */
            tag.type = (ImageView) convertView.findViewById(R.id.approve_show_item_type);
            tag.type_1 = (TextView) convertView.findViewById(R.id.approve_show_item_type_1);
            tag.time = (TextView) convertView.findViewById(R.id.approve_show_item_time);
            tag.department = (TextView) convertView.findViewById(R.id.approve_show_item_department);
            tag.name = (TextView) convertView.findViewById(R.id.approve_show_item_name);
            tag.type_2 = (TextView) convertView.findViewById(R.id.approve_show_item_type_2);
            tag.mater = (TextView) convertView.findViewById(R.id.approve_show_item_mater);

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        switch (type) {
            case 1/*ConstantApprove.NEW_EXPENSES*/://报销
                holder.type.setImageResource(R.drawable.common_av);
                /**
                 * list  在这里强转,然后将list中的数据设置在列表中
                 */
                break;
            case 2/*ConstantApprove.NEW_TRIP*/://出差
                holder.type.setImageResource(R.drawable.common_av);
                break;
            case 3/*ConstantApprove.NEW_LEAVE*/://请假
                holder.type.setImageResource(R.drawable.common_av);
                break;
            case 4/*ConstantApprove.NEW_OUT*/://外出
                holder.type.setImageResource(R.drawable.common_av);
                break;
            case 5/*ConstantApprove.NEW_BUY*/://采购
                holder.type.setImageResource(R.drawable.common_av);
                break;
            case 6/*ConstantApprove.NEW_AGREEMENT*/://合同
                holder.type.setImageResource(R.drawable.common_av);
                break;
            case 7/*ConstantApprove.NEW_RES*/://物品
                holder.type.setImageResource(R.drawable.common_av);
                break;
        }

        /**
         * 将list的数据设置到控件中
         */

        return convertView;
    }

    public static class Holder {

        public ImageView type;
        public TextView type_1//类型
                , time//时间
                , department//部门
                , name//姓名
                , type_2//类型
                , mater;//事由
    }
}

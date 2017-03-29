package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.approve.ApproveNewDao;

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveNewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApproveNewDao> list;

    public ApproveNewAdapter(Context context, ArrayList<ApproveNewDao> list) {
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
                    R.layout.item_approve_new, null);
            tag = new Holder();
            tag.name = (TextView) convertView.findViewById(R.id.approve_new_name);
            tag.file = (ImageView) convertView.findViewById(R.id.approve_new_icon);


            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(list.get(position).name);
        LogUtil.e("设置的图片为第==="+position);
        switch (list.get(position).particular_tag){
            case 1/*ConstantApprove.NEW_EXPENSES*/://增加新文件
                holder.file.setImageResource(R.drawable.com_icon_add_1);
                break;
            case 2/*ConstantApprove.NEW_TRIP*/://word文档
                holder.file.setImageResource(R.drawable.com_icon_word);
                break;
            case 3/*ConstantApprove.NEW_LEAVE*/://excl文档
                holder.file.setImageResource(R.drawable.com_icon_excel);
                break;
            case 4/*ConstantApprove.NEW_OUT*/://ppt文档
                holder.file.setImageResource(R.drawable.com_icon_ppt);
                break;
            case 5/*ConstantApprove.NEW_BUY*/://图片
                holder.file.setImageResource(R.drawable.common_audio);
                break;
            case 6/*ConstantApprove.NEW_AGREEMENT*/://其他
                holder.file.setImageResource(R.drawable.common_av);
                break;
            case 7/*ConstantApprove.NEW_RES*/://其他
                holder.file.setImageResource(R.drawable.common_av);
                break;
        }
        holder.name.setText(list.get(position).name);


        return convertView;
    }

    public static class Holder {
        public TextView name;//文件名称
        public ImageView file;//文件
    }
}

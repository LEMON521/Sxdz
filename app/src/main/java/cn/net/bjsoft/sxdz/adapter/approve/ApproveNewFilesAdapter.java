package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.approve.ApproveNewFilesDao;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveNewFilesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApproveNewFilesDao> list;

    public ApproveNewFilesAdapter(Context context, ArrayList<ApproveNewFilesDao> list) {
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
                    R.layout.item_approve_new_files, null);
            tag = new Holder();
            tag.name = (TextView) convertView.findViewById(R.id.approve_new_name);
            tag.file = (ImageView) convertView.findViewById(R.id.approve_new_file);
            tag.delect = (ImageView) convertView.findViewById(R.id.approve_new_delete);
            tag.delect.setVisibility(View.VISIBLE);
            tag.delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MyToast.showShort(context, "条目被删除");
                    list.remove(position);
                    notifyDataSetChanged();
                    Utility.setGridViewHeightBasedOnChildren((GridView) parent,4);
                }
            });

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(list.get(position).name);
        LogUtil.e("设置的图片为第==="+position);
        switch (list.get(position).tag){
            case -2://增加新文件
                holder.file.setImageResource(R.drawable.com_icon_add_1);
                holder.delect.setVisibility(View.GONE);
                break;
            case 0://word文档
                holder.file.setImageResource(R.drawable.com_icon_word);
                holder.delect.setVisibility(View.VISIBLE);
                break;
            case 1://excl文档
                holder.file.setImageResource(R.drawable.com_icon_excel);
                holder.delect.setVisibility(View.VISIBLE);
                break;
            case 2://ppt文档
                holder.file.setImageResource(R.drawable.com_icon_ppt);
                holder.delect.setVisibility(View.VISIBLE);
                break;
            case 3://图片
                holder.file.setImageResource(R.drawable.common_audio);
                holder.delect.setVisibility(View.VISIBLE);
                break;
            case -1://其他
                holder.file.setImageResource(R.drawable.common_av);
                break;
        }


        return convertView;
    }

    public static class Holder {
        public TextView name;//文件名称
        public ImageView file//文件
                , delect;//删除
    }
}

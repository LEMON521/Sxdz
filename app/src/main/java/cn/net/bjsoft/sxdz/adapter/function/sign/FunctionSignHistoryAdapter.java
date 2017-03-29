package cn.net.bjsoft.sxdz.adapter.function.sign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.function.sign.FunctionSignHistoryBean;

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class FunctionSignHistoryAdapter extends BaseAdapter {

    public TextView name;

    private Context context;
    private ArrayList<FunctionSignHistoryBean.HumenListDao> list;
    private boolean isCheck = false;

    public FunctionSignHistoryAdapter(Context context, ArrayList<FunctionSignHistoryBean.HumenListDao> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        LogUtil.e("-----"+list.size());
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_function_sign_history, null);
            tag = new Holder();
            tag.name = (TextView) convertView.findViewById(R.id.sign_history_name);

            final Holder finalTag = tag;
//            tag.name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (list.get(position).isCheck) {
//                        finalTag.name.setBackgroundResource(R.drawable.biankuang_blue);
//                        list.get(position).isCheck = false;
//                    }else {
//                        finalTag.name.setBackgroundResource(R.drawable.biankuang_gray);
//                        list.get(position).isCheck = true;
//                    }
//                }
//            });

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(list.get(position).name);
        if (list.get(position).isCheck) {
            holder.name.setBackgroundResource(R.drawable.biankuang_blue);
        }else {
            holder.name.setBackgroundResource(R.drawable.biankuang_gray);
        }
        LogUtil.e("设置的图片为第===" + position);
        LogUtil.e("设置的图片为第===" + list.get(position).name);
        return convertView;
    }

    public static class Holder {
        public TextView name;//文件名称
    }
}

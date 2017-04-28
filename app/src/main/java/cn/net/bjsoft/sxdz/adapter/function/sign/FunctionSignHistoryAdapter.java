package cn.net.bjsoft.sxdz.adapter.function.sign;

import android.content.Context;
import android.graphics.Color;
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
        LogUtil.e("-----" + list.size());
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
            convertView.setTag(tag);
        } else {
            tag = (Holder) convertView.getTag();
        }
        //设置数据
        tag.name.setText(list.get(position).name);

        //每次绘制就设置背景和字体的颜色
        tag.name.setTextColor(Color.parseColor("#666666"));
        tag.name.setBackgroundColor(Color.parseColor("#FFFFFF"));
        return convertView;
    }

    public static class Holder {
        public TextView name;//文件名称
    }
}

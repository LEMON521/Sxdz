package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;

/**
 * 中电联发---work页面的功能列表适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class KnowledgeGroupAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<KnowledgeBean.GroupDataDao> list;

    public KnowledgeGroupAdapter(Context context, ArrayList<KnowledgeBean.GroupDataDao> list) {
        this.context = context;
        this.list = list;
        //LogUtil.e("适配器中==="+list.getClass().toString());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_knowledge_group, null);
            tag = new Holder();
            tag.name = (TextView) convertView.findViewById(R.id.item_knowledge_group_name);

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(list.get(position).name);
        return convertView;
    }

    public static class Holder {
        public TextView name;//文件名称
    }
}

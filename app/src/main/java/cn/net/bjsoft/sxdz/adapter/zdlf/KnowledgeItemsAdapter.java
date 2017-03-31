package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;

/**
 * 中电联发---知识模块分组条目适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class KnowledgeItemsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<KnowledgeBean.ItemsDataDao> list;

    public KnowledgeItemsAdapter(Context context, ArrayList<KnowledgeBean.ItemsDataDao> list) {
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
                    R.layout.item_knowledge_items, null);
            tag = new Holder();
            tag.image = (ImageView) convertView.findViewById(R.id.item_knowledge_item_image);
            tag.title = (TextView) convertView.findViewById(R.id.item_knowledge_item_title);
            tag.author = (TextView) convertView.findViewById(R.id.item_knowledge_item_author);
            tag.date = (TextView) convertView.findViewById(R.id.item_knowledge_item_date);
            tag.category = (TextView) convertView.findViewById(R.id.item_knowledge_item_category);
            tag.reply_count = (TextView) convertView.findViewById(R.id.item_knowledge_item_reply_count);
            tag.lookover_count = (TextView) convertView.findViewById(R.id.item_knowledge_item_lookover_count);
            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        if (list.get(position).image_url != null && !list.get(position).image_url.equals("")) {
            holder.image.setVisibility(View.VISIBLE);
            x.image().bind(holder.image, list.get(position).image_url);
            //LogUtil.e("图片路径--"+list.get(position).image_url);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.title.setText(list.get(position).title);
        holder.author.setText(list.get(position).author);
        holder.date.setText(list.get(position).date);
        holder.category.setText(list.get(position).category);
        holder.reply_count.setText(list.get(position).reply_count);
        holder.lookover_count.setText(list.get(position).lookover_count);
        return convertView;
    }

    public static class Holder {
        public TextView title, author, date, category, reply_count, lookover_count;//文件名称
        public ImageView image;
    }
}

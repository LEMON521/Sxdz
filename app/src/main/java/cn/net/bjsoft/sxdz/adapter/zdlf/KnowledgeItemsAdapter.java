package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsTopsBean;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 中电联发---知识模块分组条目适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class KnowledgeItemsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<KnowItemsDataItemsBean> list;
    private ImageOptions imageOptions;

    public KnowledgeItemsAdapter(Context context, ArrayList<KnowItemsDataItemsBean> list) {
        this.context = context;
        this.list = list;
        imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.http_loading_image) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.drawable.http_loading_image).build();
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
            tag.top_count = (TextView) convertView.findViewById(R.id.item_knowledge_item_top_count);

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        if (list.get(position).logo != null && !list.get(position).logo.equals("")) {
            holder.image.setVisibility(View.VISIBLE);
            if (!list.get(position).logo.startsWith("http://")) {
                list.get(position).logo = SPUtil.getUser_ApiData(context) +"/"+ list.get(position).logo;
            }
            x.image().bind(holder.image, list.get(position).logo, imageOptions);
            //LogUtil.e("图片路径--"+list.get(position).image_url);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.title.setText(list.get(position).title);

        holder.author.setText(list.get(position).author);
        //holder.author.setText(UsersInforUtils.getInstance(context).getUserInfo(list.get(position).userid).nickname);

        holder.date.setText(list.get(position).ctime);
//        holder.category.setText(list.get(position).category);
//        holder.reply_count.setText(list.get(position).reply_count);
//        holder.lookover_count.setText(list.get(position).lookover_count);

        holder.category.setText(list.get(position).labels);
        if (list.get(position).items != null) {
            holder.reply_count.setText(list.get(position).items.size() + "");
        } else {
            holder.reply_count.setText("0");
        }

        if (list.get(position).views!=null) {
            holder.lookover_count.setText(list.get(position).views.size()+"");
        }


        //点赞数
        long top_count = 0l;
        for (KnowItemsDataItemsTopsBean top : list.get(position).tops) {
            if (top.valid.equals("1")) {//当valid=1时,才是点赞
                top_count++;
            }
        }
        holder.top_count.setText(top_count + "");
        return convertView;
    }

    public static class Holder {
        public TextView title, author, date, category, reply_count, lookover_count, top_count;//文件名称
        public ImageView image;
    }
}

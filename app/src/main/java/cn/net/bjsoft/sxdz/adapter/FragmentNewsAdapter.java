package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.NewsItemsBean;

/**
 * 新闻条目的Adapter
 * Created by Zrzc on 2016/12/23.
 */

public class FragmentNewsAdapter extends BaseAdapter {

    private TextView title;
    private TextView date;

    private Context context;
   // private NewsItemsBean news;
    private List<NewsItemsBean.NewsData> newsItem;
    private View item;//单个条目


    public FragmentNewsAdapter(Context context, List<NewsItemsBean.NewsData> news){
        this.context = context;
        //this.newsItem = news;
        this.newsItem = news;

//        LogUtils.e("Adapter==============="+newsItem.get(0).name);
    }
    @Override
    public int getCount() {
        return newsItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
                    R.layout.item_news, null);
            tag = new Holder();
            tag.title = (TextView) convertView.findViewById(R.id.news_title);
            tag.date = (TextView) convertView.findViewById(R.id.news_date);

            convertView.setTag(tag);
        }

        // 设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.title.setText(newsItem.get(position).name);
        holder.date.setText(newsItem.get(position).publictime);

        return convertView;

    }

    private class Holder{
        private TextView title;
        private TextView date;
    }
}

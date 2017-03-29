package cn.net.bjsoft.sxdz.adapter;

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
import cn.net.bjsoft.sxdz.bean.community.HelpItemBean;

/**
 * 单个帮助条目
 * Created by Zrzc on 2017/1/13.
 */

public class HelpItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<HelpItemBean> list;

    ImageOptions mImageOptions;

    public HelpItemAdapter(Context context, ArrayList<HelpItemBean> list) {
        this.context = context;
        this.list = list;
        mImageOptions = new ImageOptions.Builder().setUseMemCache(false).build();
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
                    R.layout.fragment_help_list, null);
            tag = new Holder();
            tag.title = (TextView) convertView.findViewById(R.id.help_item_title);
            tag.dis = (TextView) convertView.findViewById(R.id.help_item_dis);
            tag.iv = (ImageView) convertView.findViewById(R.id.help_item_iv);
            convertView.setTag(tag);
        }

        // 设置数据
        Holder holder = (Holder) convertView.getTag();
        //LogUtil.e("shuju----" +list.get(position).titleHelp);
        holder.title.setText(list.get(position).titleHelp);
        holder.dis.setText(list.get(position).discriptionHelp);

        x.image().bind(holder.iv,list.get(position).imageUrlHelp);

        return convertView;
    }

    private class Holder {
        public TextView title, dis;
        public ImageView iv;
    }
}

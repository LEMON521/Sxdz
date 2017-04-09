package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * 单个建议条目
 * Created by Zrzc on 2017/1/12.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MessageBean> list;
    private BitmapUtils bitmapUtils;

    private AppProgressDialog progressDialog;
    public MessageAdapter(Context context, ArrayList<MessageBean> list){
        this.context=context;
        this.list=list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fragment_message_list, null);
            tag = new Holder();
            tag.avatar= (CircleImageView) convertView.findViewById(R.id.message_item_avatar);
            tag.name= (TextView) convertView.findViewById(R.id.message_item_name);
            tag.title= (TextView) convertView.findViewById(R.id.message_item_title);
            tag.time= (TextView) convertView.findViewById(R.id.message_item_time);
            tag.dis= (TextView) convertView.findViewById(R.id.message_item_dis);

            bitmapUtils = new BitmapUtils(context, AddressUtils.img_cache_url);//初始化头像
            bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);//初始化头像
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);//初始化头像
            convertView.setTag(tag);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(list.get(position).name);
        holder.title.setText(list.get(position).title);
        holder.time.setText(list.get(position).time);
        holder.dis.setText(list.get(position).dis);


        bitmapUtils.display(holder.avatar, list.get(position).avatarUrl);

        return convertView;
    }

    public static class Holder {
        
        public CircleImageView avatar;//proposal_item_avatar
        public TextView name;//proposal_item_name
        public TextView title;//proposal_item_count
        public TextView time;//proposal_item_time
        public TextView dis;//proposal_item_dis
        
    }


}

package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersSingleBean;
import cn.net.bjsoft.sxdz.bean.message.MessageMessageBean;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.MyBase16;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * 单个建议条目
 * Created by Zrzc on 2017/1/12.
 */

public class MessageMessageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MessageMessageBean.MessageDataDao> list;
    private BitmapUtils bitmapUtils;
    private UsersInforUtils usersInfor;

    public MessageMessageAdapter(Context context, ArrayList<MessageMessageBean.MessageDataDao> list) {
        this.context = context;
        this.list = list;
        bitmapUtils = new BitmapUtils(context, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.tab_me_n);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.tab_me_n);//初始化头像
        usersInfor = UsersInforUtils.getInstance(context);
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
                    R.layout.item_message_list, null);
            tag = new Holder();
            tag.avatar = (CircleImageView) convertView.findViewById(R.id.message_item_avatar);
            tag.name = (TextView) convertView.findViewById(R.id.message_item_name);
            tag.title = (TextView) convertView.findViewById(R.id.message_item_title);
            tag.time = (TextView) convertView.findViewById(R.id.message_item_time);
            tag.dis = (TextView) convertView.findViewById(R.id.message_item_dis);
            convertView.setTag(tag);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据

        Holder holder = (Holder) convertView.getTag();

        UsersSingleBean user = usersInfor.getUserInfo(list.get(position).sendid);

        holder.name.setText(user.nickname);
        //holder.title.setText(list.get(position).title);
        if (list.get(position).showtime.equals("")) {
            holder.time.setText("未知时间");
        } else {
            //holder.time.setText(TimeUtils.getFormateTime(Long.parseLong(list.get(position).showtime), "-", ":"));
            holder.time.setText(list.get(position).showtime);//返回什么用什么---时间
        }
//        holder.dis.setText(list.get(position).message);

        //要防止后台数据长度为---0的时候
        if (list.get(position).message.startsWith("HEX")) {
            RichText.from(MyBase16.decode(list.get(position).message.substring(3, list.get(position).message.length()))).autoFix(false).into(holder.dis);
        } else {
            holder.dis.setText(list.get(position).message);
        }
        bitmapUtils.display(holder.avatar, user.avatar);

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

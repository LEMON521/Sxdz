package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.proposal.NewProposalActivity;
import cn.net.bjsoft.sxdz.bean.community.DisabuseBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.view.RoundImageView;

/**
 * Created by Zrzc on 2017/1/13.
 */

public class DisabuseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DisabuseBean> list;
    private String json;

    private AppProgressDialog progressDialog;
    public DisabuseAdapter(Context context, ArrayList<DisabuseBean> list,String json){
        this.context=context;
        this.list=list;
        this.json= json;
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
                    R.layout.fragment_disabuse_list, null);
            tag = new Holder();
            tag.avatar= (RoundImageView) convertView.findViewById(R.id.disabuse_item_avatar);
            tag.name= (TextView) convertView.findViewById(R.id.disabuse_item_name);
            tag.time= (TextView) convertView.findViewById(R.id.disabuse_item_time);
            tag.dis= (TextView) convertView.findViewById(R.id.disabuse_item_dis);
            tag.happenTime= (TextView) convertView.findViewById(R.id.disabuse_item_happentime);

            tag.replay= (TextView) convertView.findViewById(R.id.disabuse_item_replay);
            tag.already_replay = (LinearLayout) convertView.findViewById(R.id.disabuse_item_already_replay);
            tag.happen = (LinearLayout) convertView.findViewById(R.id.disabuse_item_happen);

            tag.replay.setVisibility(View.GONE);
            tag.already_replay.setVisibility(View.GONE);
            tag.happen.setVisibility(View.GONE);

            tag.reAvatar= (RoundImageView) convertView.findViewById(R.id.disabuse_item_replay_avatar);
            tag.reName= (TextView) convertView.findViewById(R.id.disabuse_item_replay_name);
            tag.reTime= (TextView) convertView.findViewById(R.id.disabuse_item_replay_time);
            tag.reDis= (TextView) convertView.findViewById(R.id.disabuse_item_replay_dis);

            tag.replay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, NewProposalActivity.class);
                    intent.putExtra("json",json);
                    context.startActivity(intent);
                }
            });

            convertView.setTag(tag);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        Holder holder = (Holder) convertView.getTag();

        x.image().bind(holder.avatar,list.get(position).avatarUrl);
        holder.name.setText(list.get(position).name);
        holder.time.setText(list.get(position).time);
        holder.dis.setText(list.get(position).dis);

        if (list.get(position).state == 0){
            holder.replay.setVisibility(View.GONE);
            holder.already_replay.setVisibility(View.VISIBLE);
            holder.happen.setVisibility(View.VISIBLE);

            holder.happenTime.setText(list.get(position).happenTime);

            x.image().bind(holder.reAvatar,list.get(position).avatarUrl);
            holder.reName.setText(list.get(position).reName);
            holder.reTime.setText(list.get(position).reTime);
            holder.reDis.setText(list.get(position).reDis);
        }else if (list.get(position).state ==1){
            holder.replay.setVisibility(View.VISIBLE);
            holder.happen.setVisibility(View.GONE);
            holder.already_replay.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class Holder {

        public RoundImageView avatar;//disabuse_item_avatar
        public TextView name;//disabuse_item_name
        public TextView time;//disabuse_item_time
        public TextView dis;//disabuse_item_dis
        public TextView happenTime;//disabuse_item_happentime
        public int state;

        public TextView replay;//disabuse_item_replay
        public LinearLayout already_replay;//disabuse_item_already_replay
        public LinearLayout happen;//disabuse_item_happen

        public RoundImageView reAvatar;//disabuse_item_replay_avatar
        public TextView reName;//disabuse_item_replay_name
        public TextView reTime;//disabuse_item_replay_time
        public TextView reDis;//disabuse_item_replay_dis

    }
}

package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.community.ProposalItemBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.view.RoundImageView;

/**
 * 单个建议条目
 * Created by Zrzc on 2017/1/12.
 */

public class ProposalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProposalItemBean> list;

    private AppProgressDialog progressDialog;
    public ProposalAdapter(Context context, ArrayList<ProposalItemBean> list){
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
                    R.layout.fragment_proposal_list, null);
            tag = new Holder();
            tag.avatar= (RoundImageView) convertView.findViewById(R.id.proposal_item_avatar);
            tag.name= (TextView) convertView.findViewById(R.id.proposal_item_name);
            tag.count= (TextView) convertView.findViewById(R.id.proposal_item_count);
            tag.time= (TextView) convertView.findViewById(R.id.proposal_item_time);
            tag.dis= (TextView) convertView.findViewById(R.id.proposal_item_dis);
            
            convertView.setTag(tag);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(list.get(position).name);
        holder.count.setText(list.get(position).count);
        holder.time.setText(list.get(position).time);
        holder.dis.setText(list.get(position).dis);
        x.image().bind(holder.avatar,list.get(position).avatarUrl);
        
        

        return convertView;
    }

    public static class Holder {
        
        public RoundImageView avatar;//proposal_item_avatar
        public TextView name;//proposal_item_name
        public TextView count;//proposal_item_count
        public TextView time;//proposal_item_time
        public TextView dis;//proposal_item_dis
        
    }


}

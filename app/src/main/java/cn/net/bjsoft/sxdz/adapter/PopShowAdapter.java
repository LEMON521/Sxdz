package cn.net.bjsoft.sxdz.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;

/**
 * Created by Zrzc on 2017/4/11.
 */

public class PopShowAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private FragmentActivity mActivity;
    public PopShowAdapter(FragmentActivity activity,ArrayList<String> list){
        this.mActivity = activity;
        this.list = list;
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
        Holder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_pop_show_list, null);
            viewHolder = new Holder();
            viewHolder.text= (TextView) convertView.findViewById(R.id.item_pop_show);

            convertView.setTag(viewHolder);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.text.setText(list.get(position));

        return convertView;
    }

    public static class Holder {

        public TextView text;

    }
}

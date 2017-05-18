package cn.net.bjsoft.sxdz.adapter.dailog;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.function.sign.SignUserDataBean;

/**
 * Created by Zrzc on 2017/4/11.
 */

public class CheckListAdapter extends BaseAdapter {

    private ArrayList<SignUserDataBean> list;
    private FragmentActivity mActivity;

    public CheckListAdapter(FragmentActivity activity, ArrayList<SignUserDataBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_pop_check_list, null);
            viewHolder = new Holder();
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.pop_dropdown_checkbox);
            viewHolder.text = (TextView) convertView.findViewById(R.id.pop_dropdown_user);

            convertView.setTag(viewHolder);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        else {
            viewHolder = (Holder) convertView.getTag();
        }
        //UsersInforUtils.getInstance(mActivity).getUserInfo(list.get(position).user_id).nickname
        viewHolder.check.setChecked(list.get(position).select);
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).select = ((CheckBox)v).isChecked();
                LogUtil.e("选择第几个-------------" + position + "::" + list.get(position).select);
                CheckListAdapter.this.notifyDataSetChanged();
            }
        });
//        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
        viewHolder.text.setText(list.get(position).user_name);

        return convertView;
    }

    public class Holder {
        public CheckBox check;
        public TextView text;

    }
}

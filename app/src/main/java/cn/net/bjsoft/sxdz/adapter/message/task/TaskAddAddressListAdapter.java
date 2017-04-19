package cn.net.bjsoft.sxdz.adapter.message.task;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListBean;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class TaskAddAddressListAdapter extends BaseAdapter {

    private ArrayList<TreeTaskAddAddressListBean.TreeTaskAddAddressListDao> humenList;
    private FragmentActivity mActivity;
    private BitmapUtils bitmapUtils;


    public TaskAddAddressListAdapter(FragmentActivity mActivity, ArrayList<TreeTaskAddAddressListBean.TreeTaskAddAddressListDao> humenList) {
        this.humenList = humenList;
        this.mActivity = mActivity;
        bitmapUtils = new BitmapUtils(mActivity, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.mipmap.application_zdlf_loding);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.application_zdlf_loding);//初始化头像

    }

    @Override
    public int getCount() {
        return humenList.size();
    }

    @Override
    public Object getItem(int position) {
        return humenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_task_add_address_list, null);
            viewHolder = new ViewHolder();

            viewHolder.avatar = (ImageView) convertView
                    .findViewById(R.id.item_task_add_address_avatar);
            viewHolder.remove = (ImageView) convertView
                    .findViewById(R.id.item_task_add_address_remove);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.item_task_add_address_name);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.item_task_add_address_department);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(humenList.get(position).name);
        viewHolder.department.setText(humenList.get(position).department);
        bitmapUtils.display(viewHolder.avatar, humenList.get(position).avatar);
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                humenList.remove(position);
                notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren((ListView) parent);
            }
        });

        return convertView;
    }

    private final class ViewHolder {
        public ImageView avatar, remove;
        public TextView name, department;

    }
}

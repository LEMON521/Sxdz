package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListBean;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.view.CircleImageView;

public class AddressListSearchResultAdapter extends BaseAdapter {

    private BitmapUtils bitmapUtils;
    private Context context;
    private ArrayList<AddressListBean.AddressListDao> resultList;

    public AddressListSearchResultAdapter(Context context
            , ArrayList<AddressListBean.AddressListDao> resultList) {
        this.context = context;
        this.resultList = resultList;
        bitmapUtils = new BitmapUtils(context, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.mipmap.application_zdlf_loding);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.tab_me_n);//初始化头像
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_address_list, null);
            viewHolder = new ViewHolder();
            viewHolder.parent_ll = (LinearLayout) convertView
                    .findViewById(R.id.item_address_list_parent);
            viewHolder.child_ll = (LinearLayout) convertView
                    .findViewById(R.id.item_address_list_child);

            viewHolder.child_icon = (CircleImageView) convertView
                    .findViewById(R.id.item_address_list_child_icon);
            viewHolder.child_name = (TextView) convertView
                    .findViewById(R.id.item_address_list_child_name);
            viewHolder.child_num = (TextView) convertView
                    .findViewById(R.id.item_address_list_child_number);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.parent_ll.setVisibility(View.GONE);
        viewHolder.child_ll.setVisibility(View.VISIBLE);


        bitmapUtils.display(viewHolder.child_icon, resultList.get(position).avatar_url);
        //x.image().bind(viewHolder.child_icon, node.getAvatar_url());
        viewHolder.child_name.setText(resultList.get(position).name);
        viewHolder.child_num.setText(resultList.get(position).phone_number);

        return convertView;
    }

    private final class ViewHolder {
        public LinearLayout parent_ll;
        public LinearLayout child_ll;
        public CircleImageView child_icon;
        public TextView child_name, child_num;

    }

}

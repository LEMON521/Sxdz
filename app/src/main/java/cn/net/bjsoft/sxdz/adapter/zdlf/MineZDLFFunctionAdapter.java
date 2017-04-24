package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.bean.app.user.UserAddinsBean;

/**
 * 中电联发---work页面的功能列表适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class MineZDLFFunctionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserAddinsBean> list;
    private ImageOptions imageOptions;

    public MineZDLFFunctionAdapter(Context context, ArrayList<UserAddinsBean> list) {
        this.context = context;
        this.list = list;
        imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.application_zdlf_loding) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.mipmap.application_zdlf_loding).build();
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
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_mine_zdlf_function, null);
            holder = new Holder();
            holder.ll = (LinearLayout) convertView.findViewById(R.id.mine_zdlf_personnel_file);
            holder.title = (TextView) convertView.findViewById(R.id.item_mine_zdlf_function_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_mine_zdlf_function_icon);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置数据
        //LogUtil.e("适配器中===" + list.get(position).type + "::" + list.get(position).name + "::" + position);

        holder.title.setText(list.get(position).title);//注意,这里要讲int类型转换为String的!!!!!!!
        x.image().bind(holder.icon, list.get(position).logo,imageOptions);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("title", list.get(position).title);
                intent.putExtra("url", list.get(position).link.url);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public static class Holder {
        public LinearLayout ll;
        public TextView title;//
        public ImageView icon;//文件
    }
}

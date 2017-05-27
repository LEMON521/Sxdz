package cn.net.bjsoft.sxdz.adapter.zdlf;

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
import cn.net.bjsoft.sxdz.bean.zdlf.work.WorkBean;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 中电联发---work页面的功能列表适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class WorkAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<WorkBean.FunctionListDao> list;
    private ImageOptions imageOptions;

    public WorkAdapter(Context context, ArrayList<WorkBean.FunctionListDao> list) {
        this.context = context;
        this.list = list;
        imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.application_zdlf_loding) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.mipmap.application_zdlf_loding).build();
        //LogUtil.e("适配器中==="+list.getClass().toString());
//        for (WorkBean.FunctionListDao dao : list) {
//            LogUtil.e("查看数据===" + dao.type + "::" + dao.name + "::");
//        }
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
                    R.layout.item_work, null);
            tag = new Holder();
            tag.work_name = (TextView) convertView.findViewById(R.id.work_name);
            tag.work_num = (TextView) convertView.findViewById(R.id.work_num);
            tag.work_icon = (ImageView) convertView.findViewById(R.id.work_icon);

            convertView.setTag(tag);
        }
        //设置数据
        //LogUtil.e("适配器中===" + list.get(position).type + "::" + list.get(position).name + "::" + position);
        Holder holder = (Holder) convertView.getTag();
        holder.work_name.setText(list.get(position).name);

        int count = list.get(position).push_count;
        if (count < 1) {
            holder.work_num.setVisibility(View.INVISIBLE);
        } else {
            holder.work_num.setVisibility(View.VISIBLE);
            holder.work_num.setText(count + "");//注意,这里要将int类型转换为String的!!!!!!!
        }
        if (!list.get(position).image_url.startsWith("http://")) {
            list.get(position).image_url = SPUtil.getUser_ApiData(context) + list.get(position).image_url;
        }
        x.image().bind(holder.work_icon, list.get(position).image_url, imageOptions);
        return convertView;
    }

    public static class Holder {
        public TextView work_name, work_num;//文件名称
        public ImageView work_icon;//文件
    }
}

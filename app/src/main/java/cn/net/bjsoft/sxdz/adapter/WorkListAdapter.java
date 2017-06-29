package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.image.ImageOptions;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.zdlf.WorkAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.work.WorkBean;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 中电联发---work页面的功能列表适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class WorkListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArrayList<WorkBean.FunctionListDao>> list;
    private ImageOptions imageOptions;

    public WorkListAdapter(Context context, ArrayList<ArrayList<WorkBean.FunctionListDao>> list) {
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
                    R.layout.item_work_items, null);
            holder = new Holder();
            holder.ll = (LinearLayout) convertView.findViewById(R.id.item_work_function_ll);
            holder.title = (TextView) convertView.findViewById(R.id.item_work_function_tv);
            holder.gv = (GridView) convertView.findViewById(R.id.item_work_function_gv);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置数据
        if (holder.gvAdapter == null) {
            holder.gvAdapter = new WorkAdapter(context, list.get(position));
        }
        holder.title.setText(list.get(position).get(0).type);
        holder.gv.setAdapter(holder.gvAdapter);
        Utility.setGridViewHeightBasedOnChildren(holder.gv, 4);
        holder.gvAdapter.notifyDataSetChanged();
        holder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionChildren, long id) {
                Intent intent = new Intent(context, WebActivity.class);
                String title = list.get(position).get(positionChildren).name;
                String url = list.get(position).get(positionChildren).url;
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public static class Holder {
        public LinearLayout ll;
        public TextView title;//
        public GridView gv;//文件
        public WorkAdapter gvAdapter;
    }
}

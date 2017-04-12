package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeNewPictureBean;

/**
 * 中电联发---知识模块分组条目适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class KnowledgeNewPicturesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<KnowledgeNewPictureBean> list;

    public KnowledgeNewPicturesAdapter(Context context, ArrayList<KnowledgeNewPictureBean> list) {
        this.context = context;
        this.list = list;
        //LogUtil.e("适配器中==="+list.getClass().toString());
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_knowledge_new_picture, null);
            viewHolder = new Holder();
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_knowledge_new_pic);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.item_knowledge_new_delete);
            viewHolder.pic_show = (RelativeLayout) convertView.findViewById(R.id.item_knowledge_new_pic_show);
            viewHolder.add_show = (LinearLayout) convertView.findViewById(R.id.item_knowledge_new_add);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        //设置数据

        if ((list.size()-1)==position) {
            viewHolder.pic_show.setVisibility(View.GONE);
            viewHolder.add_show.setVisibility(View.VISIBLE);

        }else {
            viewHolder.pic_show.setVisibility(View.VISIBLE);
            viewHolder.add_show.setVisibility(View.GONE);
            x.image().bind(viewHolder.pic,list.get(position).pic_path);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除动作
                    list.remove(position);
                    KnowledgeNewPicturesAdapter.this.notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    public static class Holder {
        public RelativeLayout pic_show;
        public LinearLayout add_show;
        public ImageView pic, delete;
    }
}

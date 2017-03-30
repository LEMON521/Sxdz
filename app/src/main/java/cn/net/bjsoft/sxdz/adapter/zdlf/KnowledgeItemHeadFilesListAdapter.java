package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;

/**
 * 中电联发---work页面的功能列表适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class KnowledgeItemHeadFilesListAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> list;

    public KnowledgeItemHeadFilesListAdapter(FragmentActivity mActivity, ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> list) {
        this.mActivity = mActivity;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_list_knowledge_headview_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.item_list_headview_knowledge_item_filename);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置数据
        LogUtil.e("适配器中files_url==="+list.get(position).file_name);
        holder.name.setText(list.get(position).file_name);
        return convertView;
    }

    public static class Holder {
        public TextView name;//文件名称
    }
}

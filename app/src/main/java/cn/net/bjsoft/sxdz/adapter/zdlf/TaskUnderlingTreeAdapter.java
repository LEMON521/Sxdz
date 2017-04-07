package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.view.treeview.helper.Node;
import cn.net.bjsoft.sxdz.view.treeview.helper.TreeListViewAdapter;

public class TaskUnderlingTreeAdapter<T> extends TreeListViewAdapter<T> {

    private BitmapUtils bitmapUtils;
    private Context context;

    public TaskUnderlingTreeAdapter(ListView mTree
            , Context context
            , List<T> datas
            , int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_address_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.position = (TextView) convertView
                    .findViewById(R.id.item_task_underling_position);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.item_task_underling_name);
            viewHolder.number = (TextView) convertView
                    .findViewById(R.id.item_task_underling_number);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (Integer.parseInt(node.getNumber())<1){
            viewHolder.number.setVisibility(View.GONE);
        }else {
            viewHolder.number.setVisibility(View.VISIBLE);
            viewHolder.number.setText(node.getNumber());
        }
        viewHolder.name.setText(node.getName());
        viewHolder.position.setText(node.getStation());


        return convertView;
    }

    private final class ViewHolder {
        public TextView position, name, number;

    }

}

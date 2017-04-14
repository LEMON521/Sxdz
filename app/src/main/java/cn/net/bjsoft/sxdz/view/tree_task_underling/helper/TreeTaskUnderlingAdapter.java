package cn.net.bjsoft.sxdz.view.tree_task_underling.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.net.bjsoft.sxdz.R;

public class TreeTaskUnderlingAdapter<T> extends TreeTaskListViewAdapter<T> {

    public TreeTaskUnderlingAdapter(ListView mTree, Context context, List<T> datas,
                                    int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(NodeTaskUnderling node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_task_underling, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.number_background = (FrameLayout) convertView
                    .findViewById(R.id.item_task_underling_number_background);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.item_task_underling_department);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.item_task_underling_label);
            viewHolder.number = (TextView) convertView
                    .findViewById(R.id.item_task_underling_number);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int number = Integer.parseInt(node.getTask_num());
        if (number<1) {
            viewHolder.number_background.setVisibility(View.GONE);
        } else {
            viewHolder.number_background.setVisibility(View.VISIBLE);
            viewHolder.number.setText(node.getTask_num());
        }
        viewHolder.label.setText(node.getName());
        viewHolder.department.setText(node.getDepartment());

        return convertView;
    }

    private final class ViewHolder {
        public FrameLayout number_background;
        public TextView department,label,number;
    }

}

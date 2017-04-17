package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.view.tree_task_underling.helper.NodeTaskUnderling;
import cn.net.bjsoft.sxdz.view.tree_task_underling.helper.TreeTaskListViewAdapter;

public class TaskUnderlingTreeAdapter<T> extends TreeTaskListViewAdapter<T> {

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
    public View getConvertView(NodeTaskUnderling node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_task_underling, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.node_iv = (ImageView) convertView.findViewById(R.id.item_task_underling_node);

            viewHolder.click = (LinearLayout) convertView.findViewById(R.id.item_task_underling_click);

            viewHolder.background = (FrameLayout) convertView
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

        viewHolder.node_iv.setImageResource(node.getIcon());

        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmptyActivity.class);
                intent.putExtra("fragment_name", "TopTaskUnderlingDetailFragment");
                context.startActivity(intent);
            }
        });
        viewHolder.department.setText(node.getDepartment());
        viewHolder.label.setText(node.getName());
        if (!(Integer.parseInt(node.getTask_num()) < 1)) {
            viewHolder.background.setVisibility(View.VISIBLE);
            viewHolder.number.setText(node.getTask_num());
        } else {
            viewHolder.background.setVisibility(View.GONE);
        }


        return convertView;
    }

    private final class ViewHolder {
        public ImageView node_iv;
        public LinearLayout click;
        public FrameLayout background;
        public TextView department, label, number;
    }

}

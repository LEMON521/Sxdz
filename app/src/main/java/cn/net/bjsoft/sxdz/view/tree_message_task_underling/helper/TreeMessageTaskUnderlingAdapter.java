package cn.net.bjsoft.sxdz.view.tree_message_task_underling.helper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.bartop.message.task.TopTaskUnderlingDetailActivity;
import cn.net.bjsoft.sxdz.utils.MyToast;

public class TreeMessageTaskUnderlingAdapter<T> extends TreeMessageTaskUnderlingListViewAdapter<T> {
    private Context context;

    public TreeMessageTaskUnderlingAdapter(ListView mTree, Context context, List<T> datas,
                                           int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;

    }

    @Override
    public View getConvertView(final NodeMessageTaskUnderling node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_task_underling, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.node_iv = (ImageView) convertView.findViewById(R.id.item_task_underling_icon);

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
                if (node.getPositionsBean().employee != null) {
                    Intent intent = new Intent(context, TopTaskUnderlingDetailActivity.class);
                    intent.putExtra("source_id", node.getPositionsBean().employee.source_id);
                    LogUtil.e("intent---source_id---"+intent.getStringExtra(""));
                    context.startActivity(intent);
                } else {
                    MyToast.showShort(context, "该岗位没有联系人!");
                }
            }
        });
        viewHolder.department.setText(node.getPositionsBean().name);

        if (node.getPositionsBean().employee != null) {//---------------防止空岗
            viewHolder.label.setText(node.getPositionsBean().employee.name);
        }
//        if (node.getTask_num() != null&&!node.getTask_num().equals("")) {
//            if (!(Integer.parseInt(node.getTask_num()) < 1)) {
//                viewHolder.background.setVisibility(View.VISIBLE);
//                viewHolder.number.setText(node.getTask_num());
//            } else {
//                viewHolder.background.setVisibility(View.GONE);
//            }
//        }


        return convertView;
    }

    private final class ViewHolder {
        public ImageView node_iv;
        public LinearLayout click;
        public FrameLayout background;
        public TextView department, label, number;
    }

}

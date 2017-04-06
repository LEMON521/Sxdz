package cn.net.bjsoft.sxdz.adapter.message.task;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.view.CircleImageView;

public class TaskZDLFAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private ArrayList<MessageTaskBean.TasksDao> tasksDaos;

    public TaskZDLFAdapter(FragmentActivity mActivity
            , ArrayList<MessageTaskBean.TasksDao> tasksDaos) {
        this.mActivity = mActivity;
        this.tasksDaos = tasksDaos;
    }


    @Override
    public int getCount() {
        return tasksDaos.size();
    }

    @Override
    public Object getItem(int position) {
        return tasksDaos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_task_zdlf, null);
            viewHolder = new ViewHolder();
            viewHolder.state = (TextView) convertView
                    .findViewById(R.id.task_item_state);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.task_item_title);
            viewHolder.overdue = (CircleImageView) convertView
                    .findViewById(R.id.task_item_overdue);
            viewHolder.classify = (TextView) convertView
                    .findViewById(R.id.task_item_classify);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.task_item_name);
            viewHolder.start = (TextView) convertView
                    .findViewById(R.id.task_item_start);
            viewHolder.end = (TextView) convertView
                    .findViewById(R.id.task_item_end);
            viewHolder.level = (TextView) convertView
                    .findViewById(R.id.task_item_level);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (tasksDaos.get(position).isOverdue) {
            viewHolder.overdue.setVisibility(View.VISIBLE);
        } else {
            viewHolder.overdue.setVisibility(View.GONE);
        }



        switch (tasksDaos.get(position).state){
            case 1:
                viewHolder.state.setText("非常重要");
                viewHolder.state.setTextColor(Color.parseColor("#FF0101"));
                break;
            case 2:
                viewHolder.state.setText("重要");
                viewHolder.state.setTextColor(Color.parseColor("#02ED02"));
                break;
            case 3:
                viewHolder.state.setText("一般");
                viewHolder.state.setTextColor(Color.parseColor("#666666"));
                break;
        }

        viewHolder.title.setText(tasksDaos.get(position).title);
        viewHolder.classify.setText(tasksDaos.get(position).classify);
        viewHolder.name.setText(tasksDaos.get(position).name);
        viewHolder.start.setText("开始时间:"+TimeUtils.getFormateTime(tasksDaos.get(position).start,"-",":"));
        viewHolder.end.setText("结束时间:"+TimeUtils.getFormateTime(tasksDaos.get(position).end,"-",":"));

        switch (tasksDaos.get(position).level){
            case 1:
                viewHolder.level.setText("完成");
                viewHolder.level.setTextColor(Color.parseColor("#FBBB0E"));
                break;
            case 2:
                viewHolder.level.setText("进行中");
                viewHolder.level.setTextColor(Color.parseColor("#0156E2"));
                break;
//            case 3:
//                viewHolder.level.setText("新到");
//                viewHolder.level.setTextColor(Color.parseColor("#FF0000"));
//                break;
            default:
                viewHolder.level.setText("");
                viewHolder.level.setTextColor(Color.parseColor("#FF0000"));
        }
        return convertView;
    }

    private final class ViewHolder {
        public CircleImageView overdue;
        public TextView state, title, classify, name, start, end, level;

    }

}

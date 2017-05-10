package cn.net.bjsoft.sxdz.adapter.message.task;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskBean;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;

public class TaskAllZDLFAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private ArrayList<MessageTaskBean.TasksAllDao> tasksDaos;
    private UsersInforUtils usersInfor;

    public TaskAllZDLFAdapter(FragmentActivity mActivity
            , ArrayList<MessageTaskBean.TasksAllDao> tasksDaos) {
        this.mActivity = mActivity;
        this.tasksDaos = tasksDaos;
        usersInfor = UsersInforUtils.getInstance(mActivity);
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
            viewHolder.overdue = (ImageView) convertView
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
        ///////////////////////是否快过期//////////////////////////
//        if (tasksDaos.get(position).isOverdue) {
//            viewHolder.overdue.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.overdue.setVisibility(View.GONE);
//        }


        /////////////////////////////////////////////////////

//        switch (tasksDaos.get(position).level) {
//            case 1:
//                viewHolder.level.setText("非常重要");
//                viewHolder.level.setTextColor(Color.parseColor("#FF0101"));
//                break;
//            case 2:
//                viewHolder.level.setText("重要");
//                viewHolder.level.setTextColor(Color.parseColor("#02ED02"));
//                break;
//            case 3:
//                viewHolder.level.setText("一般");
//                viewHolder.level.setTextColor(Color.parseColor("#666666"));
//                break;
//            default:
//                viewHolder.level.setText("");
//                viewHolder.level.setTextColor(Color.parseColor("#666666"));
//        }
        viewHolder.level.setText(tasksDaos.get(position).priority);
        viewHolder.level.setTextColor(Color.parseColor("#" + tasksDaos.get(position).priority_color));
        /////////////////////////////////////////////////////////////////
        viewHolder.title.setText(tasksDaos.get(position).title);

        viewHolder.classify.setText(tasksDaos.get(position).type);
        //viewHolder.classify.setTextColor(Color.parseColor("#"+tasksDaos.get(position).priority_color));

        /////////////////////////////////////////////////////
        StringBuffer name = new StringBuffer();
        switch (tasksDaos.get(position).is_executant) {
            case 0:
                name.append("发起人：");
                name.append(usersInfor.getUserInfo(tasksDaos.get(position).userid).nickname);
                break;

            case 1:
                name.append("执行人：");
                if (tasksDaos.get(position).worker != null) {
                    for (int i = 0; i < tasksDaos.get(position).worker.size(); i++) {
                        name.append(usersInfor.getUserInfo(tasksDaos.get(position).worker.get(i).userid).nickname);
                        if (i + 1 != tasksDaos.get(position).worker.size()) {
                            name.append("、");
                        }
                    }
                }
                break;
        }


        viewHolder.name.setText(name.toString());

//        //////////////////////////////////////////////////
        viewHolder.start.setText("开始时间:" + TimeUtils.getFormateTime(Long.parseLong(tasksDaos.get(position).starttime), "-", ":"));
        viewHolder.end.setText("结束时间:" + TimeUtils.getFormateTime(Long.parseLong(tasksDaos.get(position).limittime), "-", ":"));

        ///////////////////////////////////////////////////
        if (tasksDaos.get(position).finished) {
            viewHolder.state.setText("完成");
            viewHolder.state.setTextColor(Color.parseColor("#FBBB0E"));
        } else {
            viewHolder.state.setText("进行中");
            viewHolder.state.setTextColor(Color.parseColor("#0156E2"));
        }
//        switch (tasksDaos.get(position).state) {
//            case 1:
//                viewHolder.state.setText("完成");
//                viewHolder.state.setTextColor(Color.parseColor("#FBBB0E"));
//                break;
//            case 2:
//                viewHolder.state.setText("进行中");
//                viewHolder.state.setTextColor(Color.parseColor("#0156E2"));
//                break;
////            case 3:
////                viewHolder.level.setText("新到");
////                viewHolder.level.setTextColor(Color.parseColor("#FF0000"));
////                break;
//            default:
//                viewHolder.state.setText("");
//                viewHolder.state.setTextColor(Color.parseColor("#FF0000"));
//        }
        return convertView;
    }

    private final class ViewHolder {
        public ImageView overdue;
        public TextView state, title, classify, name, start, end, level;

    }

}

package cn.net.bjsoft.sxdz.adapter.message.task;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailAddBean;

public class TaskDetailAddAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private ArrayList<MessageTaskDetailAddBean> tasksDaos;

    public TaskDetailAddAdapter(FragmentActivity mActivity
            , ArrayList<MessageTaskDetailAddBean> tasksDaos) {
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
                    R.layout.item_task_detail_add, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (EditText) convertView
                    .findViewById(R.id.item_task_detail_add_title);
            viewHolder.discription = (EditText) convertView
                    .findViewById(R.id.item_task_detail_add_discription);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private final class ViewHolder {

        public EditText title,discription;

    }

}

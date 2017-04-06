package cn.net.bjsoft.sxdz.adapter.message.task;

import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailAddBean;
import cn.net.bjsoft.sxdz.utils.function.Utility;

public class TaskDetailAddAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private MessageTaskDetailAddBean addBean;
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
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_task_detail_add, null);
            viewHolder = new ViewHolder();
            viewHolder.title_tv = (TextView) convertView
                    .findViewById(R.id.item_task_detail_add_title_tv);
            viewHolder.discription_tv = (TextView) convertView
                    .findViewById(R.id.item_task_detail_add_discription_tv);
            viewHolder.title_et = (EditText) convertView
                    .findViewById(R.id.item_task_detail_add_title_et);
            viewHolder.discription_et = (EditText) convertView
                    .findViewById(R.id.item_task_detail_add_discription_et);

            viewHolder.discription_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Utility.setListViewHeightBasedOnChildren((ListView) parent);
                }
            });

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        addBean = tasksDaos.get(position);
        if (addBean.isEditing) {//判断是否可编辑状态
            viewHolder.title_tv.setVisibility(View.GONE);
            viewHolder.discription_tv.setVisibility(View.GONE);
            viewHolder.title_et.setVisibility(View.VISIBLE);
            viewHolder.discription_et.setVisibility(View.VISIBLE);
            viewHolder.title_et.setText(addBean.title);
            viewHolder.discription_et.setText(addBean.discription);
        }else {
            viewHolder.title_tv.setVisibility(View.VISIBLE);
            viewHolder.discription_tv.setVisibility(View.VISIBLE);
            viewHolder.title_et.setVisibility(View.GONE);
            viewHolder.discription_et.setVisibility(View.GONE);
            viewHolder.title_tv.setText(addBean.title);
            viewHolder.discription_tv.setText(addBean.discription);
        }


        return convertView;
    }

    private final class ViewHolder {

        public EditText title_et,discription_et;
        public TextView title_tv,discription_tv;

    }

}

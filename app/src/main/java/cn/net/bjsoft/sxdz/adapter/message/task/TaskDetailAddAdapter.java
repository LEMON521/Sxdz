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
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailBean;

public class TaskDetailAddAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private MessageTaskDetailBean.DetailAddDao detailAddDao;
    private ArrayList<MessageTaskDetailBean.DetailAddDao> tasksDaos;
    private ListView listView;

    public TaskDetailAddAdapter(FragmentActivity mActivity
            , ListView listView
            , ArrayList<MessageTaskDetailBean.DetailAddDao> tasksDaos) {
        this.mActivity = mActivity;
        this.tasksDaos = tasksDaos;
        this.listView = listView;
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
        detailAddDao = tasksDaos.get(position);
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

            viewHolder.title_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    detailAddDao.detail_title = s.toString();
                }
            });
            viewHolder.discription_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //Utility.setListViewHeightBasedOnChildren(listView);
                    detailAddDao.detail_description = editable.toString();


                }
            });

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (detailAddDao.isEditing) {//判断是否可编辑状态
            viewHolder.title_tv.setVisibility(View.GONE);
            viewHolder.discription_tv.setVisibility(View.GONE);
            viewHolder.title_et.setVisibility(View.VISIBLE);
            viewHolder.discription_et.setVisibility(View.VISIBLE);
            viewHolder.title_et.setText(detailAddDao.detail_title);
            viewHolder.discription_et.setText(detailAddDao.detail_description);
        } else {
            viewHolder.title_tv.setVisibility(View.VISIBLE);
            viewHolder.discription_tv.setVisibility(View.VISIBLE);
            viewHolder.title_et.setVisibility(View.GONE);
            viewHolder.discription_et.setVisibility(View.GONE);
            viewHolder.title_tv.setText(detailAddDao.detail_title);
            viewHolder.discription_tv.setText(detailAddDao.detail_description);
        }


        return convertView;
    }

    private final class ViewHolder {

        public EditText title_et, discription_et;
        public TextView title_tv, discription_tv;

    }

}

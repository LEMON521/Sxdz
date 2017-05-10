package cn.net.bjsoft.sxdz.adapter.message.task;

import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataUsersPlanBean;

public class TaskDetailAddAdapter_new extends BaseAdapter {

    private FragmentActivity mActivity;
    private MessageTaskDetailDataUsersPlanBean detailAddDao;
    private ArrayList<MessageTaskDetailDataUsersPlanBean> tasksDaos;

    public TaskDetailAddAdapter_new(FragmentActivity mActivity
            , ArrayList<MessageTaskDetailDataUsersPlanBean> tasksDaos) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
            viewHolder.delete = (ImageView) convertView
                    .findViewById(R.id.item_task_detail_delete);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (detailAddDao.isEditing) {//判断是否可编辑状态
            viewHolder.title_tv.setVisibility(View.GONE);
            viewHolder.discription_tv.setVisibility(View.GONE);
            viewHolder.title_et.setVisibility(View.VISIBLE);
            viewHolder.discription_et.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.title_et.setText(detailAddDao.title);
            viewHolder.discription_et.setText(detailAddDao.content);
            viewHolder.title_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    detailAddDao.title = s.toString();
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
                    detailAddDao.content = editable.toString();


                }
            });
            //一定要绑定到具体条目上,如果写在复用的里面,就会造成混乱
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tasksDaos.remove(position);
                    LogUtil.e("删除的条目为" + position);
                    TaskDetailAddAdapter_new.this.notifyDataSetChanged();
                }
            });
        } else {
            viewHolder.title_tv.setVisibility(View.VISIBLE);
            viewHolder.discription_tv.setVisibility(View.VISIBLE);
            viewHolder.title_et.setVisibility(View.GONE);
            viewHolder.discription_et.setVisibility(View.GONE);
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.title_tv.setText(detailAddDao.title);
            viewHolder.discription_tv.setText(detailAddDao.content);
        }


        return convertView;
    }

    private final class ViewHolder {

        public EditText title_et, discription_et;
        public TextView title_tv, discription_tv;
        public ImageView delete;

    }

}

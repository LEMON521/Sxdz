package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
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
import cn.net.bjsoft.sxdz.bean.approve.ApproveOutDao;
import cn.net.bjsoft.sxdz.dialog.PickerDialog;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批-新建-外出-添加请假明细ListView的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveNewOutAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApproveOutDao> list;

    public ApproveNewOutAdapter(Context context, ArrayList<ApproveOutDao> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_approve_new_out, null);
            tag = new Holder();
            tag.matter = (EditText) convertView.findViewById(R.id.item_approve_out_matter);
            tag.start = (EditText) convertView.findViewById(R.id.item_approve_out_start_time);
            tag.end = (EditText) convertView.findViewById(R.id.item_approve_out_end_time);
            tag.delect = (TextView) convertView.findViewById(R.id.item_approve_out_delete);

            tag.matter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).matter = s.toString();
                }
            });
            tag.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerDialog.showTimePickerDialog(context, (EditText) v,null);
                }
            });

            tag.start.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).startTime_str = s.toString();
                }
            });

            tag.end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerDialog.showTimePickerDialog(context, (EditText) v,null);
                }
            });

            tag.end.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).endTime_str = s.toString();
                }
            });


            tag.delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MyToast.showShort(context,"条目被删除");
                    list.remove(position);
                    notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren((ListView) parent);
                }
            });

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.matter.setText(list.get(position).matter);
        holder.start.setText(list.get(position).startTime_str);
        holder.end.setText(list.get(position).endTime_str);

        return convertView;
    }

    public static class Holder {
        public EditText matter//事由
                , start//单价
                , end;//数量

        public TextView delect;
    }
}

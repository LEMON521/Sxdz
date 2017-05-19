package cn.net.bjsoft.sxdz.adapter.function.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormDataItemsBean;

public class FunctionFormAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FunctionFormDataItemsBean> formList;

    public FunctionFormAdapter(Context context
            , ArrayList<FunctionFormDataItemsBean> formList) {
        this.context = context;
        this.formList = formList;
    }


    @Override
    public int getCount() {
        return formList.size();
    }

    @Override
    public Object getItem(int position) {
        return formList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_function_form, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.item_function_form_title);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(formList.get(position).name);

        return convertView;
    }

    private class ViewHolder {
        public TextView title;
    }

}

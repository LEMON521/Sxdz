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

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.approve.ApproveResDao;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批-新建-物品申领-添加费用明细ListView的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveNewResAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApproveResDao> list;
//    private Holder tag = null;


    public ApproveNewResAdapter(Context context, ArrayList<ApproveResDao> list) {
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
        //calendar = Calendar.getInstance();
        final Holder tag;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_approve_new_res, null);
            //初始化控件
            tag = new Holder();
            tag.res = (EditText) convertView.findViewById(R.id.item_approve_new_res_res);
            tag.use = (EditText) convertView.findViewById(R.id.item_approve_new_res_use);
            tag.num = (EditText) convertView.findViewById(R.id.item_approve_new_res_num);

            //控件监听事件
            //输入框的监听事件,这样做的好处就是讲获取到的数据实时存入到List中

            tag.res.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //LogUtil.e("填写数据"+s.toString());
                    list.get(position).res = s.toString();

                }
            });


            tag.use.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).use = s.toString();
                }
            });


            tag.num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).num = s.toString();
                }
            });


            tag.delete = (TextView) convertView.findViewById(R.id.item_approve_new_res_delete);
            tag.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MyToast.showShort(context,"条目被删除");
                    list.remove(position);
                    notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren((ListView) parent);
                    LogUtil.e("del还剩=========" + list.size() + "=======条信息!");

                }
            });

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
//        list.get(position).res = "";
//        list.get(position).unit_price = "";
//        list.get(position).quantity = "";
        holder.res.setText(list.get(position).res);
        holder.use.setText(list.get(position).use);
        holder.num.setText(list.get(position).num);


        return convertView;
    }


    public static class Holder {
        public EditText res//物品名称
                , use//单价
                , num;//数量
        public TextView delete;
    }
}

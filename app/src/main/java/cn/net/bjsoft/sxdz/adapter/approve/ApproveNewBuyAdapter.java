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
import cn.net.bjsoft.sxdz.bean.approve.ApproveBuyDao;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批-新建-报销-添加费用明细ListView的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveNewBuyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApproveBuyDao> list;
    private TextView textView;
//    private Holder tag = null;


    public ApproveNewBuyAdapter(Context context, ArrayList<ApproveBuyDao> list, TextView textView) {
        this.context = context;
        this.list = list;
        this.textView = textView;
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
        final Holder tag ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_approve_new_buy, null);
            //初始化控件
            tag = new Holder();
            tag.res = (EditText) convertView.findViewById(R.id.item_approve_buy_res);
            tag.unit_price = (EditText) convertView.findViewById(R.id.item_approve_buy_unit_price);
            tag.quantity = (EditText) convertView.findViewById(R.id.item_approve_buy_quantity);
            tag.money = (EditText) convertView.findViewById(R.id.item_approve_buy_money);


//            tag.res.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MyDatePickerDialog.showDialog(context,tag.res);
//                }
//            });

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


            tag.unit_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).unit_price = s.toString();
                    float unit_price = 0;
                    float quantity = 0;
                    LogUtil.e(tag.quantity.getText().toString().trim().equals("")+"是什么!!!!!!!!!!!");
                    if (tag.quantity.getText().toString().equals("")){
                        quantity = 0;
                    }else {
                        quantity = Float.parseFloat(tag.quantity.getText().toString().trim());
                    }
                    if (tag.unit_price.getText().toString().equals("")){
                        unit_price = 0;
                    }else {
                        unit_price = Float.parseFloat(tag.unit_price.getText().toString().trim());
                    }

                    tag.money.setText(quantity*unit_price+"");
                }
            });


            tag.quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).quantity = s.toString();
                    float unit_price = 0;
                    float quantity = 0;

                    if (tag.unit_price.getText().toString().trim().equals("")){
                        unit_price = 0;
                    }else {
                        unit_price = Float.parseFloat(tag.unit_price.getText().toString().trim());
                    }

                    if (tag.quantity.getText().toString().equals("")){
                        quantity = 0;
                    }else {
                        quantity = Float.parseFloat(tag.quantity.getText().toString().trim());
                    }
                    tag.money.setText(quantity*unit_price+"");
                }
            });

            tag.money.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).money = s.toString();
                    textView.setText(getSummer());
                }
            });

            tag.delect = (TextView) convertView.findViewById(R.id.item_approve_buy_delete);
            tag.delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MyToast.showShort(context,"条目被删除");
                    list.remove(position);
                    notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren((ListView) parent);
                    LogUtil.e("del还剩=========" + list.size() + "=======条信息!");
                    textView.setText(getSummer());
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
        holder.unit_price.setText(list.get(position).unit_price);
        holder.quantity.setText(list.get(position).quantity);
        holder.money.setText(list.get(position).money);

        return convertView;
    }

    public String getSummer() {
        float sum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).money.equals("")) {
                sum += Float.parseFloat(list.get(i).money);
            }
        }
        return sum + "";
    }

    public static class Holder {
        public EditText res//物品名称
                , unit_price//单价
                , quantity//数量
                , money;//金额
        //public TextView money;//金额
        public TextView delect;
    }
}

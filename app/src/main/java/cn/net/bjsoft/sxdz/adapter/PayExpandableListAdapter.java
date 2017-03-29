package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.PayTaskBean;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopPayFragment;

/**
 * 订单适配器
 * Created by zkagang on 2016/9/26.
 */
public class PayExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<PayTaskBean> payTaskBeanArrayList;
    private Context context;
    private TopPayFragment pf;

    public PayExpandableListAdapter(Context context, ArrayList<PayTaskBean> list, TopPayFragment pf){
        this.context=context;
        this.payTaskBeanArrayList=list;
        this.pf=pf;
    }

    @Override
    public int getGroupCount() {
        return payTaskBeanArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return payTaskBeanArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
         Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.pay_order, null);
            tag = new Holder();
            tag.name= (TextView) convertView.findViewById(R.id.paytitle);
            tag.money= (TextView) convertView.findViewById(R.id.paymoney);
            tag.number= (TextView) convertView.findViewById(R.id.paynumber);
            tag.xuan= (ImageView) convertView.findViewById(R.id.xuan);
            tag.kuang= (RelativeLayout) convertView.findViewById(R.id.kuang);
            tag.xiala= (ImageView) convertView.findViewById(R.id.xiala);
            convertView.setTag(tag);
        }

        // 设置数据
        final Holder holder = (Holder) convertView.getTag();
        if(payTaskBeanArrayList==null||payTaskBeanArrayList.get(groupPosition)==null){
            return convertView;
        }
        if(payTaskBeanArrayList.get(groupPosition).isCheck()){
            holder.xuan.setImageResource(R.drawable.common_check);
        }else{
            holder.xuan.setImageResource(R.drawable.common_wire);
        }
        holder.name.setText(payTaskBeanArrayList.get(groupPosition).getName());
        holder.money.setText("总金额："+payTaskBeanArrayList.get(groupPosition).getMoney()+"元");
        holder.number.setText("订单号："+payTaskBeanArrayList.get(groupPosition).getNumber());

        if(isExpanded == true){//展开状态
            holder.xiala.setImageResource(R.drawable.common_down);
        }else{//收起状态
            holder.xiala.setImageResource(R.drawable.common_right);
        }


        holder.kuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payTaskBeanArrayList.get(groupPosition).isCheck()){
                    holder.xuan.setImageResource(R.drawable.common_wire);
                    payTaskBeanArrayList.get(groupPosition).setCheck(false);
                    PayTaskBean p=payTaskBeanArrayList.get(groupPosition);
                    p.setCheck(false);
                    payTaskBeanArrayList.remove(groupPosition);
                    payTaskBeanArrayList.add(groupPosition,p);
                }else{
                    holder.xuan.setImageResource(R.drawable.common_check);
                    payTaskBeanArrayList.get(groupPosition).setCheck(true);
                    PayTaskBean p=payTaskBeanArrayList.get(groupPosition);
                    p.setCheck(true);
                    payTaskBeanArrayList.remove(groupPosition);
                    payTaskBeanArrayList.add(groupPosition,p);
                }
                pf.updataListData();
            }
        });


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Holder2 tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.pay_order_item, null);
            tag = new Holder2();
            tag.name= (TextView) convertView.findViewById(R.id.name);
            tag.price= (TextView) convertView.findViewById(R.id.price);
            tag.money= (TextView) convertView.findViewById(R.id.money);
            tag.num= (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(tag);
        }

        // 设置数据
        Holder2 holder = (Holder2) convertView.getTag();
        holder.name.setText("名称："+payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().get(childPosition).getName());
        holder.price.setText("单价："+payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().get(childPosition).getPrice()+"元");
        holder.num.setText("数量："+payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().get(childPosition).getNum()+payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().get(childPosition).getUnit());
        holder.money.setText("金额："+payTaskBeanArrayList.get(groupPosition).getPayTaskItemBeanArrayList().get(childPosition).getMoney());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class Holder {
        public RelativeLayout kuang;
        public TextView name,money,number;
        public ImageView xuan,xiala;
    }
    public static class Holder2 {
        public TextView name,price,money,num;
    }

    public void refresh(ArrayList<PayTaskBean> payTaskBeanArrayList){
        this.payTaskBeanArrayList=payTaskBeanArrayList;
        notifyDataSetChanged();
    }
}

package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveShowDetailsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<?> list;
    private int type = -1;

    public ApproveShowDetailsAdapter(Context context, ArrayList<?> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
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
                    R.layout.item_approve_show_details, null);
            tag = new Holder();
            /**
             * 报销模块
             */
            tag.expences = (LinearLayout) convertView.findViewById(R.id.approve_show_expences);
            tag.expences_type = (TextView) convertView.findViewById(R.id.approve_show_expences_type_information);
            tag.expences_res = (TextView) convertView.findViewById(R.id.approve_show_expences_res_information);
            tag.expences_unit_price = (TextView) convertView.findViewById(R.id.approve_show_expences_unit_price_information);
            tag.expences_quantity = (TextView) convertView.findViewById(R.id.approve_show_expences_quantity_information);
            tag.expences_money = (TextView) convertView.findViewById(R.id.approve_show_expences_money_information);
            tag.expences_money_totle = (TextView) convertView.findViewById(R.id.approve_show_expences_money_totle_information);

            /**
             * 出差模块
             */
            tag.trip = (LinearLayout) convertView.findViewById(R.id.approve_show_trip);
            tag.trip_mater = (TextView) convertView.findViewById(R.id.approve_show_trip_mater_information);
            tag.trip_situs = (TextView) convertView.findViewById(R.id.approve_show_trip_situs_information);
            tag.trip_starttime = (TextView) convertView.findViewById(R.id.approve_show_trip_starttime_information);
            tag.trip_endtime = (TextView) convertView.findViewById(R.id.approve_show_trip_endtime_information);
            tag.trip_datetime = (TextView) convertView.findViewById(R.id.approve_show_trip_datetime_information);

            /**
             * 请假模块
             */
            tag.leave = (LinearLayout) convertView.findViewById(R.id.approve_show_leave);
            tag.leave_type = (TextView) convertView.findViewById(R.id.approve_show_leave_type_information);
            tag.leave_matter = (TextView) convertView.findViewById(R.id.approve_show_leave_matter_information);
            tag.leave_starttime = (TextView) convertView.findViewById(R.id.approve_show_leave_starttime_information);
            tag.leave_endtime = (TextView) convertView.findViewById(R.id.approve_show_leave_endtime_information);
            tag.leave_datetime = (TextView) convertView.findViewById(R.id.approve_show_leave_datetime_information);

            /**
             * 外出模块
             */
            tag.out = (LinearLayout) convertView.findViewById(R.id.approve_show_out);
            tag.out_matter = (TextView) convertView.findViewById(R.id.approve_show_out_matter_information);
            tag.out_starttime = (TextView) convertView.findViewById(R.id.approve_show_out_starttime_information);
            tag.out_endtime = (TextView) convertView.findViewById(R.id.approve_show_out_endtime_information);
            tag.out_datetime = (TextView) convertView.findViewById(R.id.approve_show_out_datetime_information);

            /**
             * 采购模块
             */
            tag.buy = (LinearLayout) convertView.findViewById(R.id.approve_show_buy);
            tag.buy_type = (TextView) convertView.findViewById(R.id.approve_show_buy_type_information);
            tag.buy_mater = (TextView) convertView.findViewById(R.id.approve_show_buy_mater_information);
            tag.buy_res = (TextView) convertView.findViewById(R.id.approve_show_buy_res_information);
            tag.buy_unit_price = (TextView) convertView.findViewById(R.id.approve_show_buy_unit_price_information);
            tag.buy_quantity = (TextView) convertView.findViewById(R.id.approve_show_buy_quantity_information);
            tag.buy_money = (TextView) convertView.findViewById(R.id.approve_show_buy_money_information);
            tag.buy_money_totle = (TextView) convertView.findViewById(R.id.approve_show_buy_money_totle_information);

            /**
             * 合同审批模块
             */
            tag.agreement = (LinearLayout) convertView.findViewById(R.id.approve_show_agreement);
            tag.agreement_num = (TextView) convertView.findViewById(R.id.approve_show_agreement_num_information);
            tag.agreement_date = (TextView) convertView.findViewById(R.id.approve_show_agreement_date_information);
            tag.agreement_first = (TextView) convertView.findViewById(R.id.approve_show_agreement_first_information);
            tag.agreement_first_leading = (TextView) convertView.findViewById(R.id.approve_show_agreement_first_leading_information);
            tag.agreement_second = (TextView) convertView.findViewById(R.id.approve_show_agreement_second_information);
            tag.agreement_second_leading = (TextView) convertView.findViewById(R.id.approve_show_agreement_second_leading_information);


            /**
             * 物品领用模块
             */
            tag.res = (LinearLayout) convertView.findViewById(R.id.approve_show_res);
            tag.res_res = (TextView) convertView.findViewById(R.id.approve_show_res_res_information);
            tag.res_use = (TextView) convertView.findViewById(R.id.approve_show_res_use_information);
            tag.res_num = (TextView) convertView.findViewById(R.id.approve_show_res_num_information);


            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        switch (type) {
            case 1/*ConstantApprove.NEW_EXPENSES*/://报销
                holder.expences.setVisibility(View.VISIBLE);

                /**
                 * list  在这里强转,然后将list中的数据设置在列表中
                 */
                break;
            case 2/*ConstantApprove.NEW_TRIP*/://出差
                holder.trip.setVisibility(View.VISIBLE);
                break;
            case 3/*ConstantApprove.NEW_LEAVE*/://请假
                holder.leave.setVisibility(View.VISIBLE);
                break;
            case 4/*ConstantApprove.NEW_OUT*/://外出
                holder.out.setVisibility(View.VISIBLE);
                break;
            case 5/*ConstantApprove.NEW_BUY*/://采购
                holder.buy.setVisibility(View.VISIBLE);
                break;
            case 6/*ConstantApprove.NEW_AGREEMENT*/://合同
                holder.agreement.setVisibility(View.VISIBLE);
                break;
            case 7/*ConstantApprove.NEW_RES*/://物品
                holder.res.setVisibility(View.VISIBLE);
                break;
        }


        return convertView;
    }

    public static class Holder {

        /**
         * 报销模块
         */
        public LinearLayout expences;
        public TextView expences_type//类型
                , expences_res//物品信息
                , expences_unit_price//单价
                , expences_quantity//数量
                , expences_money//金额
                , expences_money_totle;//总金额

        /**
         * 出差模块
         */
        public LinearLayout trip;
        public TextView trip_mater//出差事由
                , trip_situs//出差地点
                , trip_starttime//开始时间
                , trip_endtime//结束时间
                , trip_datetime//出差天数
                ;

        /**
         * 请假模块
         */
        public LinearLayout leave;
        public TextView leave_type//请假类型
                , leave_matter//请假事由
                , leave_starttime//开始时间
                , leave_endtime//结束时间
                , leave_datetime//请假天数
                ;


        /**
         * 外出模块
         */
        public LinearLayout out;
        public TextView out_matter//外出事由
                , out_starttime//开始时间
                , out_endtime//结束时间
                , out_datetime//请假天数
                ;


        /**
         * 采购模块
         */
        public LinearLayout buy;
        public TextView buy_type//采购类型
                , buy_mater//采购事由
                , buy_res//物品信息
                , buy_unit_price//单价
                , buy_quantity//数量
                , buy_money//金额
                , buy_money_totle;//总金额


        /**
         * 合同审批模块
         */
        public LinearLayout agreement;
        public TextView agreement_num//合同编号
                , agreement_date//签约日期
                , agreement_first//甲方名称
                , agreement_first_leading//甲方负责人
                , agreement_second//乙方名称
                , agreement_second_leading//乙方负责人
                ;

        /**
         * 物品领用模块
         */
        public LinearLayout res;
        public TextView res_res//物品名称
                , res_use//物品用途
                , res_num;//物品数量

    }
}

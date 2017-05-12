package cn.net.bjsoft.sxdz.adapter.approve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveDataItemsBean;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;

/**
 * 审批-新建-添加附件 GridView 的数据适配器
 * Created by 靳宁宁 on 2017/2/21.
 */

public class ApproveShowWaiteItemAdapter_new_1 extends BaseAdapter {

    private Context context;
    private ArrayList<MessageApproveDataItemsBean> list;
    private int type = -1;

    private ImageOptions imageOptions;

    public ApproveShowWaiteItemAdapter_new_1(Context context, ArrayList<MessageApproveDataItemsBean> list/*, int type*/) {
        this.context = context;
        this.list = list;
        imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.examination_and_approval_zdlf_contract_approval) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.drawable.http_loading_image).build();
        //this.type = type;
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
                    R.layout.item_approve_show_1, null);
            tag = new Holder();
            /**
             * 报销模块
             */
            tag.root = (LinearLayout) convertView.findViewById(R.id.approve_apply_head);
            tag.title = (TextView) convertView.findViewById(R.id.approve_apply_title);

            tag.body = (LinearLayout) convertView.findViewById(R.id.approve_apply_body);
            tag.type = (ImageView) convertView.findViewById(R.id.approve_show_item_type);
            tag.type_1 = (TextView) convertView.findViewById(R.id.approve_show_item_type_1);
            tag.time = (TextView) convertView.findViewById(R.id.approve_show_item_time);
            tag.department = (TextView) convertView.findViewById(R.id.approve_show_item_department);
            tag.name = (TextView) convertView.findViewById(R.id.approve_show_item_name);
            tag.type_2 = (TextView) convertView.findViewById(R.id.approve_show_item_type_2);
            tag.mater = (TextView) convertView.findViewById(R.id.approve_show_item_mater);

            convertView.setTag(tag);
        }
        //设置数据
        Holder holder = (Holder) convertView.getTag();

        if (list.get(position).id.equals("-1")) {
            holder.root.setVisibility(View.VISIBLE);
            holder.body.setVisibility(View.GONE);
            holder.title.setText(list.get(position).title);
        } else {
            holder.root.setVisibility(View.GONE);
            holder.body.setVisibility(View.VISIBLE);

            x.image().bind(holder.type, list.get(position).wf_logo, imageOptions);

            holder.type_1.setText(list.get(position).title);

            //部门暂时写成人名
            holder.department.setVisibility(View.GONE);
            holder.department.setText(UsersInforUtils.getInstance(context).getUserInfo(list.get(position).userid).nickname);

            holder.name.setText(UsersInforUtils.getInstance(context).getUserInfo(list.get(position).userid).nickname);

            //事由
            holder.mater.setText(list.get(position).wf_type);

            holder.type_2.setText(list.get(position).wf_name);

            holder.time.setText(TimeUtils.getTimeDifference(Long.parseLong(list.get(position).ctime)));
            /**
             * 将list的数据设置到控件中
             */


        }

        return convertView;
    }

    public static class Holder {
        public LinearLayout root, body;
        public ImageView type;
        public TextView
                title //标题
                , type_1//类型
                , time//时间
                , department//部门
                , name//姓名
                , type_2//类型
                , mater;//事由
    }
}

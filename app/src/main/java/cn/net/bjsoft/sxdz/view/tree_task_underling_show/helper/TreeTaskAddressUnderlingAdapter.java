package cn.net.bjsoft.sxdz.view.tree_task_underling_show.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.AddressUtils;


public class TreeTaskAddressUnderlingAdapter<T> extends TreeListUnderlingViewAdapter<T> {

    private BitmapUtils bitmapUtils;
    private Context context;

    public TreeTaskAddressUnderlingAdapter(ListView mTree
            , Context context
            , List<T> datas
            , int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
        bitmapUtils = new BitmapUtils(context, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.tab_me_n);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.tab_me_n);//初始化头像
    }

    @Override
    public View getConvertView(TreeTaskUnderlingNode node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_task_underling, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.number_background = (FrameLayout) convertView
                    .findViewById(R.id.item_task_underling_number_background);
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.item_task_underling_node);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.item_task_underling_department);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.item_task_underling_label);
            viewHolder.number = (TextView) convertView
                    .findViewById(R.id.item_task_underling_number);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        int number = Integer.parseInt(node.getTask_num());
//        if (number<1) {
//            viewHolder.number_background.setVisibility(View.GONE);
//        } else {
//            viewHolder.number_background.setVisibility(View.VISIBLE);
//            viewHolder.number.setText(node.getTask_num());
//        }
        viewHolder.label.setText(node.getId() + "::" + node.getpId());
        viewHolder.department.setText(node.getName());
        viewHolder.icon.setImageResource(node.getIcon());
//        viewHolder.label.setText(node.getPositionsBean().name);
//        viewHolder.department.setText(node.getPositionsBean().employee.name);

//        viewHolder.department.setText(node.getDepartment());


        return convertView;
    }

    private final class ViewHolder {
        public FrameLayout number_background;
        public TextView department, label, number;
        public ImageView icon;

    }

}

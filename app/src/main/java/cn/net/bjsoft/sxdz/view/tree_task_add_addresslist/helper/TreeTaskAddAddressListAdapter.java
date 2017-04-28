package cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.view.CircleImageView;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.FileTreeTaskAddAddressListBean;

public class TreeTaskAddAddressListAdapter<T> extends TreeTaskAddAddressListListViewAdapter<T> {

    private BitmapUtils bitmapUtils;
    private Context context;
    private HashMap<Integer, NodeTaskAddAddressList> nodes;//用来存放选中人的信息

    private ArrayList<FileTreeTaskAddAddressListBean> mDatas;

    // 用来控制CheckBox的选中状况
    private HashMap<Integer, Boolean> isSelected;

    public TreeTaskAddAddressListAdapter(ListView mTree, Context context, List<T> datas,
                                         HashMap<Integer, NodeTaskAddAddressList> nodes,
                                         int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
        this.nodes = nodes;

        bitmapUtils = new BitmapUtils(context, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.mipmap.application_zdlf_loding);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.application_zdlf_loding);//初始化头像

        //设置复选框默认状态
        if (isSelected==null) {

        }
        isSelected = new HashMap<>();
        this.mDatas = (ArrayList<FileTreeTaskAddAddressListBean>) datas;
        for (FileTreeTaskAddAddressListBean data : mDatas) {
            isSelected.put(data.get_id(), false);
        }
        LogUtil.e(nodes.size() + "::" + "数量--------@@@@@@@@@@@@@--------");

    }

    @Override
    public View getConvertView(final NodeTaskAddAddressList node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_task_new_address_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.item_task_add_address_checkbox);
            viewHolder.ll_department = (LinearLayout) convertView
                    .findViewById(R.id.item_task_add_address_ll_department);
            viewHolder.ll_humen = (LinearLayout) convertView
                    .findViewById(R.id.item_task_add_address_ll_humen);
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.item_task_add_address_icon);
            viewHolder.department = (TextView) convertView
                    .findViewById(R.id.item_task_add_address_department);
            viewHolder.humen_num = (TextView) convertView
                    .findViewById(R.id.item_task_add_address_humen_num);
            viewHolder.humen_avatar = (CircleImageView) convertView
                    .findViewById(R.id.item_task_add_address_humen_avatar);
            viewHolder.humen_name = (TextView) convertView
                    .findViewById(R.id.item_task_add_address_humen_name);
            viewHolder.humen_department = (TextView) convertView
                    .findViewById(R.id.item_task_add_address_humen_department);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String type = node.getType().toString();
        LogUtil.e("-------------------"+node.getId()+"::"+node.getName()+"----------------");

        if (type.equals("department")) {
            viewHolder.ll_department.setVisibility(View.VISIBLE);
            viewHolder.ll_humen.setVisibility(View.INVISIBLE);

            viewHolder.department.setText(node.getName());
            viewHolder.humen_num.setText(node.getChildren().size() + "");
        } else if (type.equals("employee")) {
            viewHolder.ll_department.setVisibility(View.INVISIBLE);
            viewHolder.ll_humen.setVisibility(View.VISIBLE);

            bitmapUtils.display(viewHolder.humen_avatar, node.getAvatar());

            viewHolder.humen_name.setText(node.getName());

            viewHolder.humen_department.setText(node.getDepartment());


            viewHolder.checkBox.setTag(node.getId());
            LogUtil.e("外边id===" + node.getId() + "::" + viewHolder.checkBox.getTag());
            //isSelected.put(node.getId(), viewHolder.checkBox.isChecked());

            if ((Integer) viewHolder.checkBox.getTag() == node.getId()) {
                viewHolder.checkBox.setChecked(isSelected.get(node.getId()));
                LogUtil.e("里边id===" + node.getId() + node.getId());
                LogUtil.e("设置结果----"+isSelected.get(node.getId()));
            }

            // 选中监听
            //viewHolder.checkBox.setOnCheckedChangeListener(new onCbCheck(node));
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        nodes.put(node.getId(), node);
                        isSelected.put(node.getId(), isChecked);
                        LogUtil.e("添加" + nodes.size() + "::" + node.getId() + "::" + node.getName() + "--------$$$$$$$$$$$--------");
                        LogUtil.e("添加结果----"+isSelected.get(node.getId()));
                    } else {
                        nodes.remove(node.getId());
                        isSelected.put(node.getId(), isChecked);
                        LogUtil.e("删除" + nodes.size() + "::" + node.getId() + "::" + node.getName() + "--------￥￥￥￥￥￥￥￥￥￥--------");
                        LogUtil.e("删除结果----"+isSelected.get(node.getId()));
                    }
                }
            });
        }

        LogUtil.e("====================================================================================");
        viewHolder.icon.setImageResource(node.getIcon());


        return convertView;
    }

    private final class ViewHolder {
        public CheckBox checkBox;
        public LinearLayout ll_department, ll_humen;
        public ImageView icon;
        public CircleImageView humen_avatar;
        public TextView department, humen_num, humen_name, humen_department;
    }

    /**
     * CheckBox监听
     */
    public class onCbCheck implements CompoundButton.OnCheckedChangeListener {
        NodeTaskAddAddressList node;

        public onCbCheck(NodeTaskAddAddressList node) {
            this.node = node;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                MyToast.showShort(context, "选择第" + node.getName());

            } else {
                MyToast.showShort(context, "取消第" + node.getName());
            }
        }
    }

}

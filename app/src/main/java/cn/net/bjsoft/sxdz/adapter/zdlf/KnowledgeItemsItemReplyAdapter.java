package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsItemsBean;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersSingleBean;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.MyBase16;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * Created by Zrzc on 2017/3/20.
 */

public class KnowledgeItemsItemReplyAdapter extends BaseAdapter {
    private BitmapUtils bitmapUtils;
    private FragmentActivity mActivity;
    private Context context;
    private ArrayList<KnowItemsDataItemsItemsBean> list;
    private int pPosition = 0;


    public KnowledgeItemsItemReplyAdapter(FragmentActivity mActivity, ArrayList<KnowItemsDataItemsItemsBean> list, int position) {
        this.mActivity = mActivity;
        this.list = list;
        this.pPosition = position;
        for (KnowItemsDataItemsItemsBean dao : list) {
            //LogUtil.e("有数据===" + dao.name);
        }

        //LogUtil.e("适配器中==="+list.getClass().toString());
    }

    @Override
    public int getCount() {
        return list.get(pPosition).items.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(pPosition).items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_knowledge_items_item_reply, null);
            tag = new Holder();
            tag.avatar = (CircleImageView) convertView.findViewById(R.id.item_knowledge_item_reply_avatar);
            tag.name_main = (TextView) convertView.findViewById(R.id.item_knowledge_item_reply_main);
            tag.name_layer = (TextView) convertView.findViewById(R.id.item_knowledge_item_reply_layer);
            tag.text = (TextView) convertView.findViewById(R.id.item_knowledge_item_reply_descriptuon);
            tag.time = (TextView) convertView.findViewById(R.id.item_knowledge_item_reply_time);
            tag.reply = (TextView) convertView.findViewById(R.id.item_knowledge_item_reply_reply);
            tag.layer_ll = (LinearLayout) convertView.findViewById(R.id.item_knowledge_item_reply_layer_ll);
            convertView.setTag(tag);
        }
        Holder holder = (Holder) convertView.getTag();
        //设置数据
        bitmapUtils = new BitmapUtils(mActivity, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);//初始化头像

        UsersSingleBean usersSingleBean = UsersInforUtils.getInstance(mActivity).getUserInfo(list.get(pPosition).items.get(position).userid);
        if (usersSingleBean != null) {
            bitmapUtils.display(holder.avatar, usersSingleBean.avatar);
            holder.name_main.setText(usersSingleBean.nickname);
        }


        //找父userid
        {
            for (KnowItemsDataItemsItemsBean bean : list) {
                if (bean.id.equals(list.get(pPosition).items.get(position).reply_id)) {
                    UsersSingleBean rUsersSingleBean = UsersInforUtils.getInstance(mActivity).getUserInfo(bean.userid);
                    if (rUsersSingleBean != null) {
                        holder.layer_ll.setVisibility(View.VISIBLE);
                        holder.name_layer.setText(rUsersSingleBean.nickname);
                    }else {
                        holder.layer_ll.setVisibility(View.GONE);
                    }
                }
            }



        }


        //LogUtil.e("有数据="+position+"==" + list.get(position).name);
//        if (!TextUtils.isEmpty(userId)) {
//            UsersSingleBean rUsersSingleBean = UsersInforUtils.getInstance(mActivity).getUserInfo(list.get(position).userid);
//            if (rUsersSingleBean != null) {
//                holder.layer_ll.setVisibility(View.VISIBLE);
//                holder.name_layer.setText(rUsersSingleBean.nickname);
//            }
//
//        } else {
//            holder.layer_ll.setVisibility(View.GONE);
//        }

        holder.text.setText(MyBase16.decode(list.get(pPosition).items.get(position).content.substring(3, list.get(pPosition).items.get(position).content.length())));
//        RichText.from(list.get(position).comment_text).autoFix(false).fix(new SimpleImageFixCallback() {
//            @Override
//            public void onImageReady(ImageHolder holder, int width, int height) {
//                if (holder.getImageType() != ImageHolder.ImageType.GIF) {
//                    holder.setAutoFix(true);
//                } else {
//                    holder.setHeight(40);
//                    holder.setWidth(40);
//                }
//                if (position == 0) {
//                    holder.setAutoPlay(true);
//                } else {
//                    holder.setAutoPlay(false);
//                }
//                super.onImageReady(holder, width, height);
//            }
//        }).into(holder.text);


//        holder.time.setText(TimeUtils.getFormateTime(Long.parseLong(list.get(position).time), "-", ":"));
        holder.time.setText(list.get(pPosition).items.get(position).ctime);

//        holder.reply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }

    public static class Holder {
        //item_knowledge_item_reply_avatar
        //item_knowledge_item_reply_main
        //item_knowledge_item_reply_layer
        //item_knowledge_item_reply_reply
        //item_knowledge_item_reply_layer_ll
        //item_knowledge_item_reply_time
        //item_knowledge_item_reply_descriptuon
        public CircleImageView avatar;
        public TextView name_main, name_layer, text, time, reply;//
        public LinearLayout layer_ll;
    }
}

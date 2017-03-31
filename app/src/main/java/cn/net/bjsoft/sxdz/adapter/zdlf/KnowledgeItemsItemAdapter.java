package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zzhoujay.richtext.RichText;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.dialog.KnowledgeReplyPopupWindow_1;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.ChildrenListView;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * Created by Zrzc on 2017/3/20.
 */

public class KnowledgeItemsItemAdapter extends BaseAdapter {
    //private Holder holder;

    private BitmapUtils bitmapUtils;
    //private Context context;
    private FragmentActivity mActivity;
    private ArrayList<KnowLedgeItemBean.ReplyListDao> list;

    //private KnowledgeItemsItemReplyAdapter adapter;
    private ArrayList<KnowledgeItemsItemReplyAdapter> adaptersList;

    public KnowledgeItemsItemAdapter(FragmentActivity mActivity, ArrayList<KnowLedgeItemBean.ReplyListDao> list) {
        this.mActivity = mActivity;
        this.list = list;
        //LogUtil.e("适配器中==="+list.getClass().toString());
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
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_knowledge_items_item_1, null);
            holder = new Holder();


            holder.avatar = (CircleImageView) convertView.findViewById(R.id.knowledge_item_items_avatar);
            holder.name = (TextView) convertView.findViewById(R.id.knowledge_item_items_name);
            holder.text = (TextView) convertView.findViewById(R.id.knowledge_item_items_text);
            //tag.text = (ShowHtmlView) convertView.findViewById(R.id.knowledge_item_items_text);
            holder.leavel = (TextView) convertView.findViewById(R.id.knowledge_item_items_leavel);
            holder.time = (TextView) convertView.findViewById(R.id.knowledge_item_items_time);
            holder.reply = (CheckBox) convertView.findViewById(R.id.knowledge_item_items_isspread);
            //holder.lv_list = (/*Children*/ListView) convertView.findViewById(R.id.knowledge_item_items_reply_list);
            holder.lv_list = (ChildrenListView) convertView.findViewById(R.id.knowledge_item_items_reply_list);
            //holder.ll_host = (LinearLayout) convertView.findViewById(R.id.knowledge_item_items_reply_host_ll);
            holder.ll_host = (FrameLayout) convertView.findViewById(R.id.knowledge_item_items_reply_host_ll);
            holder.ll_reply = (LinearLayout) convertView.findViewById(R.id.knowledge_item_items_reply_ll);

            holder.replyList = new ArrayList<>();
            holder.adpter = new KnowledgeItemsItemReplyAdapter(mActivity, holder.replyList);
            final Holder finalTag = holder;

            holder.reply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    LogUtil.e("是否选中");
                    //用于记录本次点击的选中状态
                    if (isChecked) {
                        finalTag.lv_list.setVisibility(View.VISIBLE);
                        LogUtil.e("是否选中---显示");
                    } else {
                        finalTag.lv_list.setVisibility(View.GONE);
                        LogUtil.e("是否选中--不显示");
                    }
                    finalTag.isCheck = isChecked;
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置数据
        //Holder holder = (Holder) convertView.getTag();
        bitmapUtils = new BitmapUtils(mActivity, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);//初始化头像
        bitmapUtils.display(holder.avatar, list.get(position).avatar_url);
        //x.image().bind(holder.avatar,list.get(position).avatar_url);
        holder.name.setText(list.get(position).name);

        if (list.get(position).reply_list != null && list.get(position).reply_list.size() > 0) {
            holder.reply.setVisibility(View.VISIBLE);
            LogUtil.e("第几条有数据===" + list.get(position).reply_list.size() + "::楼==" + (position + 1));
            holder.replyList.clear();
            holder.replyList.addAll(list.get(position).reply_list);
            holder.lv_list.setAdapter(holder.adpter);
            Utility.setListViewHeightBasedOnChildren(holder.lv_list);

            //用于记录上次的选中状态并在显示的时候加载上次的状态
            if (holder.isCheck) {
                holder.lv_list.setVisibility(View.VISIBLE);
            } else {
                holder.lv_list.setVisibility(View.GONE);
            }

        } else {
            holder.reply.setVisibility(View.INVISIBLE);
            holder.lv_list.setVisibility(View.GONE);
            // holder.reply.setVisibility(View.GONE);
        }
        RichText.from(list.get(position).comment_text).autoFix(false).into(holder.text);
        holder.leavel.setText((position + 1) + "楼");
        holder.time.setText(TimeUtils.getFormateTime(Long.parseLong(list.get(position).time), "-", ":"));
        holder.lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionChild, long id) {
                LogUtil.e("点击前数量" + list.get(position).reply_list.size());
                MyToast.showShort(mActivity, "点击条目" + positionChild);

                //调出popuWindow
//
                KnowledgeReplyPopupWindow_1 replyWindow = new KnowledgeReplyPopupWindow_1(mActivity, view, list.get(position).reply_list.get(positionChild).name);
                replyWindow.setOnData(new KnowledgeReplyPopupWindow_1.OnGetData() {
                    @Override
                    public void onDataCallBack(KnowLedgeItemBean.ReplyListDao replyListDao) {
                        list.get(position).reply_list.add(replyListDao);
                    }
                });
            }
        });
        holder.ll_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(mActivity, "点击详情" + list.get(position).comment_text);
                //调出popuWindow
//
                KnowledgeReplyPopupWindow_1 replyWindow = new KnowledgeReplyPopupWindow_1(mActivity, view);
                replyWindow.setOnData(new KnowledgeReplyPopupWindow_1.OnGetData() {
                    @Override
                    public void onDataCallBack(KnowLedgeItemBean.ReplyListDao replyListDao) {
                        list.get(position).reply_list.add(replyListDao);
                    }
                });
            }
        });
        refresh();
        return convertView;
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    public static class Holder {
        public CircleImageView avatar;
        public TextView /*text,*/ time, name, leavel/*, reply*/;//
        public CheckBox reply;
        //public ShowHtmlView text;
        public TextView text;
        public ChildrenListView lv_list;
        //public ListView lv_list;
        public LinearLayout /*ll_host,*/ ll_reply;
        public FrameLayout ll_host;
        public KnowledgeItemsItemReplyAdapter adpter;
        public ArrayList<KnowLedgeItemBean.ReplyListDao> replyList;
        public boolean isCheck = true;
    }

    private void setReply(String show) {

    }
}

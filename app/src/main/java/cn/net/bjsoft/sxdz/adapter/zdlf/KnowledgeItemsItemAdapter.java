package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zzhoujay.richtext.RichText;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.dialog.KnowledgeReplyPopupWindow;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.reply = (TextView) convertView.findViewById(R.id.knowledge_item_items_reply);
            //holder.lv_list = (/*Children*/ListView) convertView.findViewById(R.id.knowledge_item_items_reply_list);
            holder.lv_list = (ChildrenListView) convertView.findViewById(R.id.knowledge_item_items_reply_list);
            holder.ll_host = (LinearLayout) convertView.findViewById(R.id.knowledge_item_items_reply_host_ll);
            holder.ll_reply = (LinearLayout) convertView.findViewById(R.id.knowledge_item_items_reply_ll);

            holder.replyList = new ArrayList<>();
            holder.adpter = new KnowledgeItemsItemReplyAdapter(mActivity, holder.replyList);

            final View finalConvertView = convertView;
            final Holder finalTag = holder;
            holder.ll_host.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.showShort(mActivity, "点击详情" + list.get(position).comment_text);
                    //调出popuWindow
                    KnowledgeReplyPopupWindow.setmReplayPopupWindow(mActivity
                            , null
                            , finalConvertView
                            , KnowledgeItemsItemAdapter.this
                            , finalTag.adpter
                            , list.get(position).reply_list
                            , list.get(position));
                }
            });


            holder.ll_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.showShort(mActivity, "点击回复" + position);
                    if (finalTag.reply.getText().toString().equals("收起")) {
                        finalTag.reply.setText("展开");
                        finalTag.lv_list.setVisibility(View.GONE);

                    } else {
                        finalTag.reply.setText("收起");
                        finalTag.lv_list.setVisibility(View.VISIBLE);
                    }
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
            holder.lv_list.setVisibility(View.VISIBLE);
            holder.reply.setVisibility(View.VISIBLE);
            holder.reply.setText("收起");
            //if (adapter == null) {
            LogUtil.e("第几条有数据===" + list.get(position).reply_list.size() + "::楼==" + (position + 1));
            holder.replyList.clear();
            holder.replyList.addAll(list.get(position).reply_list);
            holder.lv_list.setAdapter(holder.adpter);

            final Holder finalTag1 = holder;
            holder.lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positionChild, long id) {
//                    KnowLedgeItemBean bean = new KnowLedgeItemBean();
//                    KnowLedgeItemBean.ReplyListDao dao = bean.new ReplyListDao();
//                    dao.name = "张三";
//                    dao.avatar_url = "http://www.shuxin.net/api/app_json/wlh.jpg";
//                    dao.description = "添加测试数据,添加测试数据,添加测试数据,添加测试数据,添加测试数据,添加测试数据";
//                    dao.reply_to = "李四";
//                    dao.time = "11111111.";
                    LogUtil.e("点击前数量" + list.get(position).reply_list.size());
                    MyToast.showShort(mActivity, "点击条目" + positionChild);

                    //调出popuWindow
                    KnowledgeReplyPopupWindow.setmReplayPopupWindow(mActivity
                            , null
                            , view
                            , KnowledgeItemsItemAdapter.this
                            , finalTag1.adpter
                            , list.get(position).reply_list
                            , list.get(position).reply_list.get(positionChild));
                    //list.get(position).reply_list.add(dao);
                    LogUtil.e("点击后数量" + list.get(position).reply_list.size());
//                    adapter.notifyDataSetChanged();
//                    refresh();

                }
            });

            Utility.setListViewHeightBasedOnChildren(holder.lv_list);

        } else {
            holder.lv_list.setVisibility(View.GONE);
            holder.reply.setVisibility(View.INVISIBLE);
            //holder.reply.setText("回复");
        }
        RichText.from(list.get(position).comment_text).autoFix(true)/*.fix(new SimpleImageFixCallback() {
            @Override
            public void onImageReady(ImageHolder holder, int width, int height) {
                if (holder.getImageType() != ImageHolder.ImageType.GIF) {
                    holder.setAutoFix(true);
                } else {
                    holder.setHeight(100);
                    holder.setWidth(100);
                }
                if (position == 0) {
                    holder.setAutoPlay(true);
                } else {
                    holder.setAutoPlay(false);
                }
                super.onImageReady(holder, width, height);
            }
        })*/.into(holder.text);
//        holder.text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyToast.showShort(mActivity,"点击啦");
//            }
//        });
        //holder.text.setText(HtmlParser.buildSpannedText(list.get(position).comment_text,new CustomerTagHandler_1()));
        // holder.text.init(list.get(position).comment_text);
        //holder.text.setText(Html.fromHtml(list.get(position).comment_text));
        //holder.text.setText(list.get(position).comment_text);
        holder.leavel.setText("第" + (position + 1) + "楼");
        holder.time.setText(TimeUtils.getFormateTime(Long.parseLong(list.get(position).time), "-", ":"));
        refresh();

        return convertView;
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    public static class Holder {
        public CircleImageView avatar;
        public TextView /*text,*/ time, name, leavel, reply;//
        //public ShowHtmlView text;
        public TextView text;
        public ChildrenListView lv_list;
//        public ListView lv_list;
        public LinearLayout ll_host, ll_reply;
        public KnowledgeItemsItemReplyAdapter adpter;
        public ArrayList<KnowLedgeItemBean.ReplyListDao> replyList;
    }

    private void setReply(String show) {

    }
}

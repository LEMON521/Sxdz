package cn.net.bjsoft.sxdz.adapter.zdlf;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsItemsBean;
import cn.net.bjsoft.sxdz.dialog.KnowledgeReplyPopupWindow_1;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.MyBase16;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
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
    private ArrayList<KnowItemsDataItemsItemsBean> list;

    //private KnowledgeItemsItemReplyAdapter adapter;
    private ArrayList<KnowledgeItemsItemReplyAdapter> adaptersList;

    public KnowledgeItemsItemAdapter(FragmentActivity mActivity, ArrayList<KnowItemsDataItemsItemsBean> list) {
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


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
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
        //设置数据
        //Holder holder = (Holder) convertView.getTag();
        bitmapUtils = new BitmapUtils(mActivity, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.tab_me_n);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.tab_me_n);//初始化头像
        //bitmapUtils.display(holder.avatar, list.get(position).avatar_url);
        if (!TextUtils.isEmpty(list.get(position).userid)) {
            if (UsersInforUtils.getInstance(mActivity)
                    .getUserInfo(list.get(position).userid) != null) {
                bitmapUtils.display(holder.avatar
                        , UsersInforUtils.getInstance(mActivity)
                                .getUserInfo(list.get(position).userid)
                                .avatar);
                //x.image().bind(holder.avatar,list.get(position).avatar_url);
                holder.name.setText(UsersInforUtils
                        .getInstance(mActivity)
                        .getUserInfo(list.get(position).userid)
                        .nickname);
            }

        }

        if (list.get(position).items != null && list.get(position).items.size() > 0) {
            holder.reply.setVisibility(View.VISIBLE);
            LogUtil.e("第几条有数据===" + list.get(position).items.size() + "::楼==" + (position + 1));
            holder.replyList.clear();
            holder.replyList.addAll(list.get(position).items);
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
        RichText.from(MyBase16.decode(list.get(position).content.substring(3, list.get(position).content.length()))).autoFix(false).into(holder.text);
        //holder.text.setText(list.get(position).content);
        holder.leavel.setText((position + 1) + "楼");
        //holder.time.setText(TimeUtils.getFormateTime(Long.parseLong(list.get(position).time), "-", ":"));
        holder.time.setText(list.get(position).ctime);
        holder.lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionChild, long id) {
                LogUtil.e("点击前数量" + list.get(position).items.size());
                MyToast.showShort(mActivity, "点击条目" + positionChild);

                //调出popuWindow
//
                KnowledgeReplyPopupWindow_1 replyWindow = new KnowledgeReplyPopupWindow_1(mActivity
                        , view
                        , list.get(position).items.get(positionChild).reply_id);
                replyWindow.setOnData(new KnowledgeReplyPopupWindow_1.OnGetData() {
                    @Override
                    public void onDataCallBack(KnowItemsDataItemsItemsBean replyListDao) {
                        //list.get(position).items.add(replyListDao);
                        replayMessage(list, position, replyListDao, 2);
                    }
                });
            }
        });
        holder.ll_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(mActivity, "点击详情" + list.get(position).content);
                //调出popuWindow
//
                KnowledgeReplyPopupWindow_1 replyWindow = new KnowledgeReplyPopupWindow_1(mActivity
                        , view
                        , list.get(position).id);
                replyWindow.setOnData(new KnowledgeReplyPopupWindow_1.OnGetData() {
                    @Override
                    public void onDataCallBack(KnowItemsDataItemsItemsBean replyListDao) {
                        //TODO 待修改添加回复
                        replayMessage(list, position, replyListDao, 1);
                        //list.get(position).knowledge_item.add(replyListDao);

                    }
                });
            }
        });
        refresh();
        return convertView;
    }

    /**
     * 回复---楼主////楼层
     *
     * @param replyList    所在的items
     * @param position     第几楼
     * @param replyListDao 回复内容
     * @param type         楼主或者楼层--1,楼主,2,楼层
     */
    private void replayMessage(final ArrayList<KnowItemsDataItemsItemsBean> replyList, final int position, KnowItemsDataItemsItemsBean replyListDao, final int type) {

//        if (replyListDao.content.equals("")) {
//            MyToast.showShort(mActivity, "请输入回复内容");
//            return;
//        }


        //showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "shuxin_know_reply");


        final KnowItemsDataItemsItemsBean newDao = new KnowItemsDataItemsItemsBean();

        if (newDao.knowledge_item == null) {
            newDao.knowledge_item = new ArrayList<>();
        }
        newDao.knowledge_item.clear();
//        newDao.userid = SPUtil.getUserId(mActivity);
//        //newDao.avatar_url = SPUtil.getAvatar(mActivity);
        switch (type) {

            case 1:
                newDao.id = replyList.get(position).id;
                newDao.reply_id = replyListDao.reply_id;
                break;

            case 2:
                //newDao.id = replyList.get(position).reply_id;
                newDao.reply_id = replyListDao.reply_id;
                break;
        }

        newDao.know_id = replyList.get(position).know_id;
        newDao.ctime = TimeUtils.getFormateTime(System.currentTimeMillis(), "-", ":") + "";
        newDao.content = "HEX" + MyBase16.encode(replyListDao.content);
//        newDao.reply_id = SPUtil.getUserId(mActivity);
//        newDao.know_id = know_id;
        newDao.userid = SPUtil.getUserId(mActivity);


        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");


        sb.append("\"content\":\"");
        sb.append(newDao.content);
        sb.append("\",");
//        sb.append("\"content\":\"");
//        sb.append(newDao.content);
//        sb.append("\",");

        sb.append("\"ctime\":\"");
        sb.append(newDao.ctime);
        sb.append("\",");

        sb.append("\"reply_id\":\"");
        sb.append(newDao.reply_id);
        sb.append("\",");

        sb.append("\"know_id\":\"");
        sb.append(newDao.know_id);
        sb.append("\",");

//        sb.append("\"reply_id\":\"");
//        sb.append(newDao.reply_id);
//        sb.append("\",");

//        sb.append("\"author\":\"");
//        sb.append(UsersInforUtils.getInstance(mActivity).getUserInfo(SPUtil.getUserId(mActivity)).nickname);
//        sb.append("\",");
//
        sb.append("\"userid\":\"");
        sb.append(newDao.userid);
        sb.append("\"");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------回复楼层------上传楼层获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {
//                        switch (type) {
//
//                            case 1:
//                                replyList.add(newDao);
//                                break;
//
//                            case 2:
//                                replyList.get(position).items.add(newDao);
//                                break;
//                        }
                        replyList.get(position).items.add(newDao);
                        MyToast.showShort(mActivity, "评论成功");

                    } else {
                        MyToast.showLong(mActivity, "评论失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取消息-----------失败方法-----" + element.getMethodName());
                }

                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                dismissProgressDialog();
            }
        });


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
        public ArrayList<KnowItemsDataItemsItemsBean> replyList;
        public boolean isCheck = true;
    }

    private void setReply(String show) {

    }
}

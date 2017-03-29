package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemsItemAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.ShowHtmlView;

/**
 * 中电联发的知识---条目界面
 * Created by Zrzc on 2017/3/20.
 */

@ContentView(R.layout.fragment_knowledge_item)
public class KnowledgeItemZDLFFragment extends BaseFragment {
    @ViewInject(R.id.title_back)
    private ImageView title_back;
    @ViewInject(R.id.title_title)
    private TextView title;
    @ViewInject(R.id.knowledge_item_items)
    private ListView lv_items;
    @ViewInject(R.id.knowledge_item_edite)
    private EditText reply;

    private View headView;
    private ShowHtmlView headWebView;


    private KnowLedgeItemBean knowLedgeItemBean;
    private ArrayList<KnowLedgeItemBean.ReplyListDao> knowledgeItemList;
    private KnowledgeItemsItemAdapter knowledgeItemsItemAdapter;


    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("知识详情");

        showProgressDialog();

        if (knowledgeItemList == null) {
            knowledgeItemList = new ArrayList<>();
        }
        knowledgeItemList.clear();
        if (knowledgeItemsItemAdapter == null) {
            knowledgeItemsItemAdapter = new KnowledgeItemsItemAdapter(mActivity, knowledgeItemList);
        }

        headView = View.inflate(mActivity, R.layout.item_list_headview, null);
        headWebView = (ShowHtmlView) headView.findViewById(R.id.item_list_headview_knowledge);

        lv_items.addHeaderView(headView);
        lv_items.setAdapter(knowledgeItemsItemAdapter);
        getData();
    }


    /**
     * 获取分组数据
     */
    public void getData() {

        RequestParams params = new RequestParams(TestAddressUtils.test_get_knowledge_item_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                knowLedgeItemBean = GsonUtil.getKnowledgeItemsItemBean(result);
                if (knowLedgeItemBean.result) {

                    knowledgeItemList.addAll(knowLedgeItemBean.data.knowledge_item);
                    setData();
                    //LogUtil.e("获取到的条目-----------" + knowledgeItemList.get(0).avatar_url);
//                    classifyData();
//                    setData();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到的条目--------失败!!!---" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
    }


    private void classifyData() {
        //------分组开始
//        if (groupNameList == null) {
//            groupNameList = new ArrayList<>();
//        }
//        groupNameList.clear();
//
//        for (KnowledgeBean.GroupDataDao groupData: groupDataList){
//            groupNameList.add(groupData.name);
//        }
        //-----分组结束

    }


    private void setData() {
        headWebView.init(knowLedgeItemBean.data.title);
        knowledgeItemsItemAdapter.notifyDataSetChanged();

    }

    @Event(value = {R.id.knowledge_item_reply
            , R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mActivity.finish();
                break;

            case R.id.knowledge_item_reply:
                String replyStr = reply.getText().toString().trim();
                if (replyStr.equals("")) {
                    MyToast.showShort(mActivity, "请输入回复内容");
                    return;
                }

                replyHost();
                reply.setText("");//清除输入框
                break;
        }
    }


    /**
     * 回复楼主
     */
    private void replyHost() {
        KnowLedgeItemBean bean = new KnowLedgeItemBean();
        KnowLedgeItemBean.ReplyListDao newDao = bean.new ReplyListDao();

        if (newDao.reply_list == null) {
            newDao.reply_list = new ArrayList<>();
        }
        newDao.reply_list.clear();
        newDao.name = "李四";
        newDao.avatar_url = "";
        newDao.time = System.currentTimeMillis() + "";
        newDao.comment_text = reply.getText().toString().trim();
        newDao.reply_to = "";

        /**
         * 将数据推送到服务器,当返回成功的时候再将数据添加到本地
         */
        knowledgeItemList.add(newDao);
//        int i = 0;
//        for (KnowLedgeItemBean.ReplyListDao Dao : knowledgeItemList) {
//
//            if (Dao.reply_list != null) {
//                LogUtil.e("数量" + Dao.reply_list.size() + "::楼层" + (i+1));
//            }
//            i++;
//
//        }
        MyToast.showShort(mActivity, "评论成功");
        knowledgeItemsItemAdapter.notifyDataSetChanged();


    }
}

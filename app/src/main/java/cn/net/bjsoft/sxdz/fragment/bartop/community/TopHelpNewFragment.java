package cn.net.bjsoft.sxdz.fragment.bartop.community;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.KnowledgeNewZDLFActivity;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemsAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_help_new)
public class TopHelpNewFragment extends BaseFragment {

    @ViewInject(R.id.knowledge_items)
    private PullableListView items;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;
    @ViewInject(R.id.knowledge_message)
    private TextView message;

    @ViewInject(R.id.add)
    private ImageView add;


    private KnowItemsBean itemsBean;//文章条目
    private ArrayList<KnowItemsDataItemsBean> itemsDataList;
    private ArrayList<KnowItemsDataItemsBean> cacheItemsDataList;
    private KnowledgeItemsAdapter itemsAdapter;

    private String get_start = "0";

    private String type = "";
    private String isEditor = "false";//是否可编辑-可回复

    public void initData() {
        type = getTag();
        get_start = "0";
        if (type.equals("article")) {
            type = "news";
            add.setVisibility(View.VISIBLE);
            isEditor = "true";
        } else if (type.equals("help")) {
            add.setVisibility(View.VISIBLE);
            isEditor = "true";
        } else if (type.equals("proposal")) {
            add.setVisibility(View.GONE);
            isEditor = "false";
        } else if (type.equals("disabuse")) {
            add.setVisibility(View.GONE);
            isEditor = "false";
        }

        //type = getArguments().getString("type");
        //初始化条目信息

        if (cacheItemsDataList == null) {
            cacheItemsDataList = new ArrayList<>();
        }
        cacheItemsDataList.clear();

        if (itemsDataList == null) {
            itemsDataList = new ArrayList<>();
        }
        itemsDataList.clear();

        if (itemsAdapter == null) {
            itemsAdapter = new KnowledgeItemsAdapter(mActivity, cacheItemsDataList);
        }
        items.setAdapter(itemsAdapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, EmptyActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("know_id", itemsDataList.get(position).id);
                bundle.putString("isEditor",isEditor);
                intent.putExtra("knowledge_item_bundle", bundle);
                intent.putExtra("fragment_name", "knowledge_item");

                startActivity(intent);
            }
        });


        /**
         * 刷新///加载     的操作
         */
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                        itemsDataList.clear();
                        cacheItemsDataList.clear();
                        LogUtil.e("setOnRefreshListener-----------");
                        get_start = "0";
                        getItemsData();//group_id

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                        if (get_start.equals(get_count)) {
//                            MyToast.showShort(mActivity, "已经没有更多的信息!");
//                        } else {
                        getItemsData();//group_id
//                        }
                        LogUtil.e("onLoadMore-----------");

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });

        getItemsData();
    }

    /**
     * 获取条目数据
     */
    private void getItemsData(/*String groupId*/) {

        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$getItemsData$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "shuxin_know");
//        params.addBodyParameter("query", "2");
        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");

//        sb.append("\"query\":\"");
//        sb.append(searchStr);
//        sb.append("\",");

        sb.append("\"start\":\"");
        sb.append(get_start);
        sb.append("\",");

        sb.append("\"limit\":\"");
        sb.append("10");
        sb.append("\",");

        sb.append("\"data\":{");

        sb.append("\"type\":\"");
//        sb.append(group_id);
        sb.append(type);
        sb.append("\"");

        sb.append("}");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());


        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取文章条目----文章信息----------------" + strJson);


                itemsBean = GsonUtil.getKnowledgeItemsBean(strJson);
                if (itemsBean.code.equals("0")) {//获取成功
                    //LogUtil.e("获取到的条目-----------" + result);


                    if (itemsBean.data.items != null && itemsBean.data.items.size() > 0) {

                        itemsDataList.addAll(itemsBean.data.items);
                        cacheItemsDataList.addAll(itemsBean.data.items);

                        get_start = (itemsBean.data.items.size() + Integer.parseInt(get_start)) + "";
//                    get_cache_start= get_start;
//                        get_count = itemsBean.data.count;
                        //message.setVisibility(View.GONE);

                    } else {

                        MyToast.showShort(mActivity, "已经没有更多的信息!");
                    }


                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取条目消息-----------失败方法-----" + element.getMethodName());
                }
                MyToast.showShort(mActivity, "获取条目数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                setItemsData();
                //itemsAdapter.notifyDataSetChanged();
                dismissProgressDialog();
            }
        });
    }

    private void setItemsData() {

        LogUtil.e("获取条目数" + itemsDataList.size());
        LogUtil.e("获取条目数---高度为" + Utility.getListViewHeightBasedOnChildren(items));
        if (cacheItemsDataList.size() > 0) {
//            hint.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            itemsAdapter.notifyDataSetChanged();
        } else {
//            hint.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
//            hint.setText("没有数据,点击刷新");
        }


        //Utility.setListViewHeightBasedOnChildren(items);

    }

    /**
     * 向List添加数据（填充ListView 的数据信息）
     */
    private void setData() {
    }

    @Event(value = {R.id.community_help_back
            , R.id.add})
    private void helpOnclick(View view) {
        switch (view.getId()) {
            case R.id.community_help_back:
                getActivity().finish();
                break;

            case R.id.add:
                //MyToast.showShort(mActivity, "走,去发帖!");
                Intent intent = new Intent(mActivity, KnowledgeNewZDLFActivity.class);
                intent.putExtra("fragment_name", "KnowledgeNewZDLFFragment");
                startActivity(intent);

                break;
        }
    }
}

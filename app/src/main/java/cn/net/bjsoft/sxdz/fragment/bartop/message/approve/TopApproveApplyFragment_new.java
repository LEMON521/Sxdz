package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.WebViewApproveActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowWaiteItemAdapter_new_1;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.approve.MessageApproveDataItemsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.ViewMessageApproveApply;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * 我发起的审批--已改成需我审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_wait)
public class TopApproveApplyFragment_new extends BaseFragment {

    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;


    @ViewInject(R.id.approve_approval_root)
    private PullableListView root;

    @ViewInject(R.id.approve_approval_personnel)
    private ListView personnel;//人事审批
    @ViewInject(R.id.approve_approval_administration)
    private ListView administration;//行政审批
    @ViewInject(R.id.approve_approval_financial)
    private ListView financial;//财务审批
    @ViewInject(R.id.approve_approval_official)
    private ListView official;//办公审批

    private PostJsonBean pushApplyBean = new PostJsonBean();
    private ArrayList<MessageApproveDataItemsBean> list;
    private HashMap<String, ArrayList<MessageApproveDataItemsBean>> datas;
    private ArrayList<MessageApproveDataItemsBean> formate_list;
    private ApproveShowWaiteItemAdapter_new_1 adapter;

    private String get_start = "0";

    private MessageApproveBean messageApproveBean;

    private HashMap<String, ViewMessageApproveApply> views;

    @Override
    public void initData() {

        initList();

        /**
         * 刷新///加载     的操作
         */
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                showProgressDialog();
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        get_start = "0";
                        list.clear();
                        datas.clear();
                        getData();

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                showProgressDialog();
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        pushApplyBean.start = get_start;//设置开始查询
                        getData();
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });
    }


    private void initList() {
        pushApplyBean = new PostJsonBean();

        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();

        if (formate_list == null) {
            formate_list = new ArrayList<>();
        }
        formate_list.clear();

        if (adapter == null) {
            adapter = new ApproveShowWaiteItemAdapter_new_1(mActivity, formate_list);
        }
        root.setAdapter(adapter);
        root.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.e("点击---" + formate_list.get(position).wf_name + "::" + position);
                if (!formate_list.get(position).id.equals("-1")) {
                    Intent intent = new Intent(mActivity, WebViewApproveActivity.class);
                    //目前还没有跳转字段
                    intent.putExtra("type", "workflow");
                    intent.putExtra("url", formate_list.get(position).url);
                    intent.putExtra("id", formate_list.get(position).id);
                    intent.putExtra("title", formate_list.get(position).title);
                    mActivity.startActivity(intent);
                }
            }
        });

        if (datas == null) {
            datas = new HashMap<>();
        }
        datas.clear();

        if (views == null) {
            views = new HashMap<>();
        }
        views.clear();

    }

    @Override
    public void onStart() {
        super.onStart();

        get_start = "0";
        list.clear();
        datas.clear();
        getData();
    }

    private void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "workflow_working");


        pushApplyBean.start = get_start;//设置开始查询
        pushApplyBean.limit = "10";
        params.addBodyParameter("data", pushApplyBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushApplyBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取我发起的审批消息----------------" + strJson);
                messageApproveBean = GsonUtil.getMessageApproveBean(strJson);
                if (messageApproveBean.code.equals("0")) {
                    if (messageApproveBean.data.items != null) {
                        if (messageApproveBean.data.count.equals("0")) {
                            MyToast.showLong(mActivity, "没有任何消息可查看!");
                        } else if ((messageApproveBean.data.items.size() > 0)) {

                            if (get_start.equals(messageApproveBean.data.count)) {
                                MyToast.showLong(mActivity, "没有更多的消息可查看!");
                            } else {

                                list.addAll(messageApproveBean.data.items);
                                get_start = list.size() + "";//设置开始查询
                                formateDatas();
                                groupingDatas();
                            }
                        } else {
                            MyToast.showLong(mActivity, "没有任何消息可查看!");
                        }
                    }
                } else {
                    MyToast.showLong(mActivity, "获取消息失败-"/*+taskBean.msg*/);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
    }

    /**
     * 将数据分组
     */
    private void groupingDatas() {

        formate_list.clear();

        //获取分组数----去除重复
        for (MessageApproveDataItemsBean bean : list) {
            datas.put(bean.wf_type, new ArrayList<MessageApproveDataItemsBean>());
        }


        Iterator iter = datas.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            //将数据添加到不同的分组中
            for (MessageApproveDataItemsBean bean : list) {
                if (key.equals(bean.wf_type)) {
                    datas.get(key).add(bean);
                }
            }

            MessageApproveDataItemsBean title = new MessageApproveDataItemsBean();
            title.title = key;
            title.id = "-1";
            formate_list.add(title);
            formate_list.addAll(datas.get(key));
        }
        adapter.notifyDataSetChanged();


    }

    /**
     * 格式化从后台拿过来的数据
     */
    private void formateDatas() {
        for (MessageApproveDataItemsBean dao : list) {
            if (SPUtil.getUserId(mActivity).equals(dao.userid)) {

                String time = dao.ctime;
                time = time.replace("/Date(", "");
                time = time.replace(")/", "");
                dao.ctime = time;
                time = dao.node_time;
                time = time.replace("/Date(", "");
                time = time.replace(")/", "");
                dao.node_time = time;

            }
        }
    }

}

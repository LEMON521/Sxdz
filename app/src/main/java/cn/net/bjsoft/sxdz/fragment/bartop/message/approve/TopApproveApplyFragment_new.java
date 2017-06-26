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
    private String get_count = "0";

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
                        if (!get_start.equals(get_count)) {
                            pushApplyBean.start = get_start;//设置开始查询
                            LogUtil.e("onLoadMore-----------");
                            getData();
                        } else {
                            MyToast.showLong(mActivity, "已经没有更多的消息了!");
                            dismissProgressDialog();
                        }
                        LogUtil.e("onLoadMore-----------");
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });
        //getData();
        //test();
        //getListData();
        //setListAdapter();
    }

    private void test() {
        String s = "{\"code\":0,\"data\":{\"count\":7,\"items\":[{\"id\":\"4658686682444083413\",\"title\":\"综合行政费用报销舒新东\",\"wf_id\":\"baoxiao\",\"wf_name\":\"费用报销\",\"wf_type\":\"综合行政\",\"userid\":10001,\"ctime\":\"\\/Date(1493257553519)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493703320503)\\/\",\"node_id\":1,\"description\":null,\"node_name\":\"初审\",\"node_users\":[{\"position\":false,\"id\":\"10001\",\"type\":1,\"reject\":true,\"edit\":false}]},{\"id\":\"4681582354848769442\",\"title\":\"综合行政费用报销舒新东\",\"wf_id\":\"baoxiao\",\"wf_name\":\"费用报销\",\"wf_type\":\"综合行政\",\"userid\":10001,\"ctime\":\"\\/Date(1493703247407)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493703247464)\\/\",\"node_id\":1,\"description\":null,\"node_name\":\"初审\",\"node_users\":[{\"position\":false,\"id\":\"10001\",\"type\":1,\"reject\":false,\"edit\":false}]},{\"id\":\"4940935902148911191\",\"title\":\"舒新东发起的报销申请\",\"wf_id\":\"baoxiao\",\"wf_name\":\"费用报销\",\"wf_type\":\"综合行政\",\"userid\":10001,\"ctime\":\"\\/Date(1491798201000)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493272863886)\\/\",\"node_id\":3,\"description\":null,\"node_name\":\"记账\",\"node_users\":[{\"position\":false,\"id\":\"10001\",\"type\":1,\"reject\":true,\"edit\":false}]},{\"id\":\"5500507181100263467\",\"title\":\"项目管理等级变更审批许慧玲\",\"wf_id\":\"dengjibiangeng\",\"wf_name\":\"等级变更审批\",\"wf_type\":\"项目管理\",\"userid\":10001,\"ctime\":\"\\/Date(1493810270234)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493810270313)\\/\",\"node_id\":1,\"description\":null,\"node_name\":\"确定参与人\",\"node_users\":[{\"position\":false,\"id\":\"10001\",\"type\":2,\"reject\":false,\"edit\":true},{\"position\":false,\"id\":\"12336\",\"type\":2,\"reject\":false,\"edit\":true}]},{\"id\":\"4684978392075617072\",\"title\":\"项目管理立项审批舒新东\",\"wf_id\":\"lixiangshenpi\",\"wf_name\":\"立项审批\",\"wf_type\":\"项目管理\",\"userid\":10001,\"ctime\":\"\\/Date(1493801487008)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493801487097)\\/\",\"node_id\":1,\"description\":null,\"node_name\":\"确定参与人\",\"node_users\":[{\"position\":false,\"id\":\"11003\",\"type\":2,\"reject\":false,\"edit\":true},{\"position\":false,\"id\":\"12336\",\"type\":2,\"reject\":false,\"edit\":true}]},{\"id\":\"5259334455050929590\",\"title\":\"项目管理立项审批舒新东\",\"wf_id\":\"lixiangshenpi\",\"wf_name\":\"立项审批\",\"wf_type\":\"项目管理\",\"userid\":10001,\"ctime\":\"\\/Date(1493801385053)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493801385133)\\/\",\"node_id\":1,\"description\":null,\"node_name\":\"确定参与人\",\"node_users\":[{\"position\":false,\"id\":\"11003\",\"type\":2,\"reject\":false,\"edit\":true},{\"position\":false,\"id\":\"12336\",\"type\":2,\"reject\":false,\"edit\":true}]},{\"id\":\"5599153418535202739\",\"title\":\"项目管理立项审批舒新东\",\"wf_id\":\"lixiangshenpi\",\"wf_name\":\"立项审批\",\"wf_type\":\"项目管理\",\"userid\":10001,\"ctime\":\"\\/Date(1493800429982)\\/\",\"finished\":false,\"node_time\":\"\\/Date(1493800430058)\\/\",\"node_id\":1,\"description\":null,\"node_name\":\"确定参与人\",\"node_users\":[{\"position\":false,\"id\":\"11003\",\"type\":2,\"reject\":false,\"edit\":true},{\"position\":false,\"id\":\"12336\",\"type\":2,\"reject\":false,\"edit\":true}]}]},\"msg\":null}\n";

        messageApproveBean = GsonUtil.getMessageApproveBean(s);
        if (messageApproveBean.code.equals("0")) {
            list.addAll(messageApproveBean.data.items);
            get_start = list.size() + "";//设置开始查询
            get_count = messageApproveBean.data.count + "";

            formateDatas();//格式化信息

            groupingDatas();//将数据分组
            //taskAdapter.notifyDataSetChanged();
            if (get_count.equals("0")) {
                MyToast.showLong(mActivity, "没有任何消息可查看!");
            }
        } else {
            MyToast.showLong(mActivity, "获取消息失败-"/*+taskBean.msg*/);
        }

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
//                Bundle bundle = new Bundle();
//                bundle.putString("task_id", formate_list.get(position).id);
//                intent.putExtra("isEdited", bundle);
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
                    list.addAll(messageApproveBean.data.items);
                    get_start = list.size() + "";//设置开始查询
                    get_count = messageApproveBean.data.count + "";

                    formateDatas();//格式化信息

                    groupingDatas();//将数据分组
                    //taskAdapter.notifyDataSetChanged();
                    if (get_count.equals("0")) {
                        MyToast.showLong(mActivity, "没有任何消息可查看!");
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

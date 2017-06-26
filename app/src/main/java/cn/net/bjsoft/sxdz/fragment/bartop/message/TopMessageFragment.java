package cn.net.bjsoft.sxdz.fragment.bartop.message;

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
import cn.net.bjsoft.sxdz.adapter.MessageMessageAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.message.MessageMessageBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_message)
public class TopMessageFragment extends BaseFragment {

    @ViewInject(R.id.message_message_title)
    private TextView title;

    @ViewInject(R.id.message_message_lv)
    private PullableListView messageListView;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;

    @ViewInject(R.id.message_message_add)
    private ImageView add_message;

    private PostJsonBean pushMessageBean = null;
    private MessageMessageBean messageBean;
    private ArrayList<MessageMessageBean.MessageDataDao> dataDaos;
    private MessageMessageAdapter messageAdapter;


    private String message_Start = "0";
    private String message_count = "0";

    @Override
    public void initData() {
        title.setText("消息");
        add_message.setVisibility(View.INVISIBLE);

        if (dataDaos == null) {
            dataDaos = new ArrayList<>();
        }
        dataDaos.clear();

        if (messageAdapter == null) {
            messageAdapter = new MessageMessageAdapter(getActivity(), dataDaos);
        }

        pushMessageBean = null;
        pushMessageBean = new PostJsonBean();
        message_Start = "0";

        messageListView.setAdapter(messageAdapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //暂时不需要跳转

//                if (!dataDaos.get(position).link_url.equals("")) {
//                    Intent intent = new Intent();
//                    //intent.setClass(getActivity(), MessageListItemActivity.class);
//                    intent.setClass(getActivity(), WebActivity.class);
//                    intent.putExtra("url", dataDaos.get(position).link_url);
//                    intent.putExtra("title", dataDaos.get(position).name);
//                    //intent.putExtra("json", mJson);
//                    getContext().startActivity(intent);
//                }

            }
        });


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
                        message_Start = "0";
                        dataDaos.clear();
                        LogUtil.e("setOnRefreshListener-----------");
                        getData();
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                showProgressDialog();
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        if (!message_Start.equals(message_count)) {
                            pushMessageBean.start = message_Start;//设置开始查询
                            LogUtil.e("onLoadMore-----------");
                            getData();
                        } else {
                            MyToast.showLong(mActivity, "已经没有更多的消息了!");
                            dismissProgressDialog();
                        }

                    }
                }.sendEmptyMessageDelayed(0, 500);

            }

        });


        getData();
    }


    /**
     * 向列表添加数据
     */
    private void getData() {

        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "shuxin_alert");

        pushMessageBean.start = message_Start;//设置开始查询
        pushMessageBean.limit = "10";
        params.addBodyParameter("data", pushMessageBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushMessageBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取消息----------------" + strJson);
                messageBean = GsonUtil.getMessageMessageBean(strJson);
                if (messageBean.code.equals("0")) {
                    dataDaos.addAll(messageBean.data.items);
                    message_Start = dataDaos.size() + "";//设置开始查询
                    message_count = messageBean.data.count + "";
                    messageAdapter.notifyDataSetChanged();
                    if (message_count.equals("0")) {
                        MyToast.showLong(mActivity, "没有任何消息可查看!");
                    }
                } else {
                    MyToast.showLong(mActivity, "获取消息失败-" + messageBean.msg);
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
                dismissProgressDialog();
            }
        });
//        js_request.put("key1", value1);
//        js_request.put("key2", value2);


//        String usr_1 = "http://api.shuxinyun.com/data/load?source_id=shuxin_alert&data=%7B%22limit%22%3A20%2C%22start%22%3A0%2C%22condition%22%3A%5B%5D%2C%22data%22%3A%7B%7D%7D&appid=4895081593118139245&secret=4895081593118139245&token=8cd794bddbc1989f";
//        RequestParams params_1 = new RequestParams(usr_1);
//        x.http().get(params_1, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.e("-----------------获取消息----------------"+result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                LogUtil.e("-----------------获取消息----------------"+ex);
//                MyToast.showShort(mActivity,"获取数据失败!!");
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });


        //String url  = SPUtil.getApiUser(mActivity)+"/"+SPUtil.getUserId(mActivity)+"/notifications.json";
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                //LogUtil.e("获取到的条目-----------" + result);
//                messageBean = GsonUtil.getMessageMessageBean(result);
//                if (messageBean.result) {
//                    //LogUtil.e("获取到的条目-----------" + result);
//                    dataDaos.addAll(messageBean.data.message_list);
//                    messageAdapter.notifyDataSetChanged();
//                    messageBean = null;
//                } else {
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                LogUtil.e("获取到的条目--------失败!!!---" + ex);
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//                dismissProgressDialog();
//            }
//        });


    }

//    public class JsonBean {
//        //        "start":0,//记录数据开始索引
////                "limit":0,//提取记录数量
////                "condition":[],//查询条件，没有可以不指定
////                "data":{
////            "task_id":"5278735015174018824"}//这里参数随业务不同变化
//        public int start = 0;
//        public int limit = 10;
//        public int[] condition = {};
//        public DataBean data = new DataBean();
//
//        public class DataBean {
//            public String task_id = "5278735015174018824";
//        }
//    }


    @Event(value = {R.id.message_message_back, R.id.message_message_add})
    private void messageOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_message_back:
                mActivity.finish();
                break;
            case R.id.message_message_add:
                MyToast.showShort(mActivity, "添加新信息");
                break;
        }
    }
}

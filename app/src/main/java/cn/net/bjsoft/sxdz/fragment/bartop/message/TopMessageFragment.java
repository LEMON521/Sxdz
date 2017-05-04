package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.content.Intent;
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
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.MessageMessageAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.message.MessageMessageBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
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


    private MessageMessageBean messageBean;
    private ArrayList<MessageMessageBean.MessageDataDao> dataDaos;
    private MessageMessageAdapter messageAdapter;


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

        messageListView.setAdapter(messageAdapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //intent.setClass(getActivity(), MessageListItemActivity.class);
                intent.setClass(getActivity(), WebActivity.class);
                intent.putExtra("url","http://www.baidu.com");
                intent.putExtra("title","消息详情");
                //intent.putExtra("json", mJson);
                getContext().startActivity(intent);
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

                        dataDaos.clear();
                        LogUtil.e("setOnRefreshListener-----------");
                        getData();

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

                        LogUtil.e("onLoadMore-----------");
                        getData();
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

        String url = SPUtil.getApiAuth(mActivity)+"/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id","shuxin_alert");
        params.addBodyParameter("data","{" +
                "\"start\":0," +
                "\"limit\":0," +
                "\"condition\":[]," +
                "\"data\":{" +
                "\"task_id\":\"5278735015174018824\"}" +
                "}");


        httpPostUtils.post(mActivity,params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取消息----------------"+strJson);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取消息----------------"+ex);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


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

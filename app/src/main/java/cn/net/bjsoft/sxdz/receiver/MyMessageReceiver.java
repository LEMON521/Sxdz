package cn.net.bjsoft.sxdz.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.AdvancedCustomPushNotification;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.ali_push.ALiPushMessageBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.ALiPushCountUtils;

public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    private ArrayList<String> keys;
    private ArrayList<String> values;

    private Context mContext;

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        mContext = context;
        AdvancedCustomPushNotification notification = new AdvancedCustomPushNotification(R.layout.notification_layout, R.id.m_icon, R.id.m_title, R.id.m_text);//创建高级自定义样式通知,设置布局文件以及对应的控件ID
        notification.setServerOptionFirst(false);//设置服务端配置优先
        notification.setBuildWhenAppInForeground(true);//设置当推送到达时如果应用处于前台不创建通知
        boolean res = CustomNotificationBuilder.getInstance().setCustomNotification(2, notification);//注册该通知,并设置ID为2
        //Toast.makeText(context, "自定义通知样式" + res + ", id:" + 2, Toast.LENGTH_SHORT).show();

        //PushServiceFactory.getCloudPushService().clearNotifications();

        Log.e("--处理推送通知--", "title: " + title + "summary: " + summary + "extraMap: " + extraMap);

        LogUtil.e("-==-处理推送通知开始-==-");
        doNotification(context, extraMap);
        LogUtil.e("-==-处理推送通知结束-==-");

    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        mContext = context;
        // TODO 处理推送消息
        Log.e("--处理推送消息--", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());

        LogUtil.e("------------------------------------------------------------------------------------");

        LogUtil.e("getTitle==========" + cPushMessage.getTitle());
        LogUtil.e("getAppId==========" + cPushMessage.getAppId());
        LogUtil.e("getMessageId==========" + cPushMessage.getMessageId());
        LogUtil.e("getContent==========" + cPushMessage.getContent());

        LogUtil.e("------------------------------------------------------------------------------------");

        ALiPushMessageBean bean = GsonUtil.getALiPushMessageBean(cPushMessage.getContent());

        if (bean != null) {
            for (int i = 0; i < bean.notify_type.length; i++) {
                switch (bean.notify_type[i]) {
                    case 1:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getNotifications();
                        break;
                    case 2:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getPushitemcount();
                        break;
                    case 3:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getNotify();
                        break;
                }
            }
        }


    }

    /**
     * 点击打开通知
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        mContext = context;
        LogUtil.e("================onNotificationOpened=====================");
        Log.e("点击打开通知", "点击打开通知, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        //点击打开通知


    }


    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        mContext = context;
        LogUtil.e("================onNotificationClickedWithNoAction=====================");
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    /**
     * 打开app时接收的通知
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        mContext = context;
        LogUtil.e("================打开app时接收的通知=====================");
        Log.e("打开app时接收的通知", "打开app时接收的通知, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    /**
     * 移除通知
     * @param messageId
     */
    @Override
    protected void onNotificationRemoved(Context context,String messageId) {
        mContext = context;
        Log.e("移除通知", "移除通知==="+messageId);
    }


    private void getNotifications() {
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        String url = "http://api.shuxinyun.com/cache/users/" + SPUtil.getUserId(mContext) + "/notifications.json";
        LogUtil.e("请求数据-------------url-------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtils.get(mContext, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("getNotifications-------------成功-------------");
                LogUtil.e(strJson);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("getNotifications-------------失败-------------");
                LogUtil.e(ex + "");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getPushitemcount() {
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        String url = "http://api.shuxinyun.com/cache/users/" + SPUtil.getUserId(mContext) + "/pushitemcount.json";
        LogUtil.e("请求数据-------------url-------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtils.get(mContext, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("getPushitemcount-------------成功-------------");


                //TODO 防止后台给的数据是残疾的
                strJson = strJson.replace("\"\"","0");
                LogUtil.e(strJson);


                ALiPushCountUtils.setPushCount(mContext,strJson);

                //strJson = "{\"message\":9,\"plan\":8,\"calendar\":7,\"email\":6,\"order\":5,\"crm\":4,\"sign\":3,\"workflow\":2,\"task\":1}";
                //BroadcastCallUtil.sendMessage2Activity(mContext, strJson, GsonUtil.getPushBean(strJson));//发送消息,通知界面改数字


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("getPushitemcount-------------失败-------------");
                LogUtil.e(ex + "");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getNotify() {
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        String url = "http://api.shuxinyun.com/apps/" + SPUtil.getAppid(mContext) + "/notify.json";
        LogUtil.e("请求数据-------------url-------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtils.get(mContext, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("getNotify-------------成功-------------");
                LogUtil.e(strJson);

                String s = "<P>这是王力宏吗?</P><img src='http://www.shuxin.net/api/app_json/android/form/form_3.jpg' height='200' width='200' /><img src='http://www.shuxin.net/api/app_json/wlh.jpg' height='200' width='200' />";

                Intent i = new Intent();
                i.setAction("cn.net.bjsoft.sxdz.alipush.notify_type_3");
                Bundle bundle = new Bundle();
                bundle.putString("notify_type_3_content",s);
                i.putExtras(bundle);

                //i.putExtra("pushjson", s);
                mContext.sendBroadcast(i);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("getNotify-------------失败-------------");
                LogUtil.e(ex + "");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void doNotification(Context context, Map<String, String> extraMap) {
        if (keys == null) {
            keys = new ArrayList<>();
        }
        keys.clear();
        if (values == null) {
            values = new ArrayList<>();
        }
        values.clear();


        LogUtil.e("map的个数========" + extraMap);
        Iterator extraMapIterator = extraMap.entrySet().iterator();
        while (extraMapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) extraMapIterator.next();
            keys.add((String) entry.getKey());
            values.add((String) entry.getValue());
        }

        String tell_callback = "";
        for (int i = 0;i<keys.size();i++){
            LogUtil.e("key的值为========" + keys.get(i));
            LogUtil.e("values的值为========" + values.get(i));
            if (keys.get(i).equals("notify_type")) {
                tell_callback = "{\"notify_type\"="+values.get(i)+"}";
            }
        }

        ALiPushMessageBean bean = GsonUtil.getALiPushMessageBean(tell_callback);

        if (bean != null) {
            for (int i = 0; i < bean.notify_type.length; i++) {
                switch (bean.notify_type[i]) {
                    case 1:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getNotifications();
                        break;
                    case 2:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getPushitemcount();
                        break;
                    case 3:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getNotify();
                        break;
                }
            }
        }




//        for (int j = 0; j < keys.size(); j++) {
//            LogUtil.e("key的值为========" + keys.get(j));
//            if (keys.get(j).equals("tell_callback")) {
//                if (!values.get(j).equals("")) {
//                    ALiPushMessageBean bean = GsonUtil.getALiPushMessageBean(values.get(j));
//
//                    if (bean != null) {
//                        for (int i = 0; i < bean.notify_type.length; i++) {
//                            switch (bean.notify_type[i]) {
//                                case 1:
//                                    LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
//                                    getNotifications();
//                                    break;
//                                case 2:
//                                    LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
//                                    getPushitemcount();
//                                    break;
//                                case 3:
//                                    LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
//                                    getNotify();
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}
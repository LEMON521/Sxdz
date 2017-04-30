package cn.net.bjsoft.sxdz.receiver;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.ali_push.ALiPushMessageBean;
import cn.net.bjsoft.sxdz.utils.BroadcastCallUtil;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;

public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    private ArrayList<String> keys;
    private ArrayList<String> values;

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知

        Log.e("--处理推送通知--", "title: " + title + "summary: " + summary + "extraMap: " + extraMap);
        doNotification(context, extraMap);

    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
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
                        getNotifications(context);
                        break;
                    case 2:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getPushitemcount(context);
                        break;
                    case 3:
                        LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                        getNotify(context);
                        break;
                }
            }
        }


    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }


    private void getNotifications(Context context) {

        HttpPostUtils httpPostUtils = new HttpPostUtils();
        String url = "http://api.shuxinyun.com/cache/users/" + SPUtil.getUserId(context) + "/notifications.json";
        LogUtil.e("请求数据-------------url-------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtils.get(context, params);

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

    private void getPushitemcount(final Context context) {
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        String url = "http://api.shuxinyun.com/cache/users/" + SPUtil.getUserId(context) + "/pushitemcount.json";
        LogUtil.e("请求数据-------------url-------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtils.get(context, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("getPushitemcount-------------成功-------------");
                LogUtil.e(strJson);

                BroadcastCallUtil.sendMessage2Activity(context, strJson, GsonUtil.getPushBean(strJson));//发送消息,通知界面改数字
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

    private void getNotify(Context context) {
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        String url = "http://api.shuxinyun.com/apps/" + SPUtil.getAppid(context) + "/notify.json";
        LogUtil.e("请求数据-------------url-------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtils.get(context, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("getNotify-------------成功-------------");
                LogUtil.e(strJson);
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
        if (keys==null) {
            keys  = new ArrayList<>();
        }
        keys.clear();
        if (values==null) {
            values= new ArrayList<>();
        }
        values.clear();


        Iterator extraMapIterator = extraMap.entrySet().iterator();
        while (extraMapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) extraMapIterator.next();
            keys.add((String) entry.getKey());
            values.add((String) entry.getValue());
        }

        for (int j = 0; j < keys.size(); j++) {
            LogUtil.e("key的值为========" + keys.get(j));
            if (keys.get(j).equals("tell_callback")) {
                if (!values.get(j).equals("")) {
                    ALiPushMessageBean bean = GsonUtil.getALiPushMessageBean(values.get(j));

                    if (bean != null) {
                        for (int i = 0; i < bean.notify_type.length; i++) {
                            switch (bean.notify_type[i]) {
                                case 1:
                                    LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                                    getNotifications(context);
                                    break;
                                case 2:
                                    LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                                    getPushitemcount(context);
                                    break;
                                case 3:
                                    LogUtil.e("getContent==========的值为=====" + bean.notify_type[i] + "-----------------");
                                    getNotify(context);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
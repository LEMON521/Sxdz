package cn.net.bjsoft.sxdz.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.xutils.common.util.LogUtil;

import cn.net.bjsoft.sxdz.receiver.MyReceiver;

/**
 * Created by Zrzc on 2017/1/23.
 */

public class JPushService extends Service {
    //private BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("开启了服务！！！！！！！onCreate！！！！！！！！");
        registerReceiver(new MyReceiver(), new IntentFilter("cn.net.bjsoft.sxdz.main"));

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.e("开启了服务！！！！！！！！onStart！！！！！！！");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("开启了服务！！！！！！！！onStartCommand！！！！！！！");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("开启了服务！！！！！！！！onDestroy！！！！！！！");
    }
}

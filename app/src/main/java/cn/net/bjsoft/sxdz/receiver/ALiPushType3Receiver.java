package cn.net.bjsoft.sxdz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.xutils.common.util.LogUtil;

/**
 * Created by Zrzc on 2017/5/2.
 */

public class ALiPushType3Receiver extends BroadcastReceiver {
    // 数据接口
    private OnGetData mOnGetData;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();



        LogUtil.e("推送过来了----------");
        if (bundle != null) {
            LogUtil.e("推送消息进入了页面-----推送过来了----------");
            mOnGetData.onDataCallBack(bundle);
        }


    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(Bundle bundleData);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        mOnGetData = sd;
    }
}

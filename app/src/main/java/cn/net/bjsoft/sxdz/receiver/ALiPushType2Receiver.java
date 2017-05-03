package cn.net.bjsoft.sxdz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Zrzc on 2017/5/2.
 */

public class ALiPushType2Receiver extends BroadcastReceiver {
    // 数据接口
    private OnGetData mOnGetData;

    @Override
    public void onReceive(Context context, Intent intent) {


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

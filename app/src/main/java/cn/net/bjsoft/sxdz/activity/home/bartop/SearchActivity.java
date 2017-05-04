package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import org.xutils.view.annotation.ContentView;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.search.TopSearchTextFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * 搜索页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_empty)
public class SearchActivity extends BaseActivity {

    private BaseFragment fragment;
    private static DatasBean mDatasBean;
    private static DatasBean.DataDao mDatas;
    private static String mJson;
    private String searchType = "";
    private String searchContent = "";


    private HashMap<String, Integer> pushNum;
    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJson = getIntent().getStringExtra("json");
        searchType = getIntent().getStringExtra("searchType");
        Log.e("****************=",getIntent().toString());
        mDatasBean = GsonUtil.getDatasBean(mJson);
        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.function"));

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        BaseFragment fragment = null;
        Bundle bundle = null;
        bundle = new Bundle();

        fragment = new TopSearchTextFragment();
        if (searchType.equals("text")) {
            bundle.putString("content","");
        } else if (searchType.equals("speech")) {
            //fragment = new TopSearchSpeechFragment();
            searchContent = getIntent().getStringExtra("speech_contant");
            bundle.putString("content",searchContent);
        }


        bundle.putString("json", mJson);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "SEARCH")
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SearchActivity=","onStop");
    }

    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {
        /**
         * 接收广播
         */
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    //TODO 因为Activity每次执行，不管是在前台后台，可见不可见，onStart是必经之路，所以将推送的数据在这里显示最合理
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}

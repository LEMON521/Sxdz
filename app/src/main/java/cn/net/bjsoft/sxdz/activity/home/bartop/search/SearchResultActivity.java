package cn.net.bjsoft.sxdz.activity.home.bartop.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;


/**
 * 搜索结果页
 */
@ContentView(R.layout.activity_search_result)
public class SearchResultActivity extends BaseActivity {
    @ViewInject(R.id.search)
    private ImageView search;
    @ViewInject(R.id.search_edittext)
    private EditText searchEdittext;
    @ViewInject(R.id.searc_text)
    private TextView searcText;
    @ViewInject(R.id.webview)
    private WebView webview;
    @ViewInject(R.id.search_loading)
    private LinearLayout loading;

    private String json;
    private DatasBean datasBean;

    private String url = AddressUtils.search_url;
    private String search_contant = "";
    private String search_url = "";

    private HashMap<String, Integer> pushNum;
    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasBean = GsonUtil.getDatasBean(getIntent().getStringExtra("json"));
//        url=InitModel.getInstance(this).getToolBar().getScan();
//        url= UrlUtil.getUrl(url,this);
        url = UrlUtil.getUrl(url, this);
        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.function"));

        if (getIntent() != null) {
            if (getIntent().getStringExtra("contant") != null && !getIntent().getStringExtra("contant").equals("")) {
                search_contant = getIntent().getStringExtra("contant");
                LogUtil.e("搜索地址----" + search_contant);
                search_url = "";
                searchEdittext.setText(search_contant);
                if (datasBean.data.toolbar.search_to.equals("")){
                    search_url = url + "&query=" + search_contant;
                    LogUtil.e("将要加载==为空==" + url);
                }else {
                    search_url = UrlUtil.getUrl(datasBean.data.toolbar.search_to,this)+"&query=" + search_contant;
                    LogUtil.e("将要加载==不为空==" + url);
                }

            } else {
                search_url = url;
            }
        } else {
            search_url = url;
        }
        setWebview(search_url);
        searchEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search_contant = searchEdittext.getText().toString();
                    search_url = "";
                    if (datasBean.data.toolbar.search_to.equals("")){
                        search_url = url + "&query=" + search_contant;
                        LogUtil.e("将要加载==为空==" + url);
                    }else {
                        search_url = UrlUtil.getUrl(datasBean.data.toolbar.search_to,SearchResultActivity.this)+"&query=" + search_contant;
                        LogUtil.e("将要加载==不为空==" + url);
                    }
                    setWebview(search_url);
                    return true;
                }
                return false;
            }
        });
    }

    public void setWebview(String search) {
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setUseWideViewPort(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.loadUrl(search);
        //webview.loadUrl("https://www.baidu.com/");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 加载出传入的URL
                LogUtil.e("正在加载----" + url);
                return true;// 必须返回True，才使用自定义的webView加载数据
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //MyToast.showShort(SearchResultActivity.this,"加载完毕");
                LogUtil.e("onPageFinished----" + url);
                loading.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //MyToast.showShort(SearchResultActivity.this,"onPageStarted");
                LogUtil.e("onPageStarted----" + url);
                loading.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

        });
    }

    @Event(value = {R.id.search, R.id.searc_text, R.id.search_result_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_result_back:
                this.finish();
                break;
            case R.id.search:
                search_contant = searchEdittext.getText().toString();
                search_url = "";
                if (datasBean.data.toolbar.search_to.equals("")){
                    search_url = url + "&query=" + search_contant;
                    LogUtil.e("将要加载==为空==" + url);
                }else {
                    search_url = UrlUtil.getUrl(datasBean.data.toolbar.search_to,SearchResultActivity.this)+"&query=" + search_contant;
                    LogUtil.e("将要加载==不为空==" + url);
                }
                setWebview(search_url);
                break;
            case R.id.searc_text:
                search_contant = searchEdittext.getText().toString();
                search_url = "";
                if (datasBean.data.toolbar.search_to.equals("")){
                    search_url = url + "&query=" + search_contant;
                    LogUtil.e("将要加载==为空==" + url);
                }else {
                    search_url = UrlUtil.getUrl(datasBean.data.toolbar.search_to,SearchResultActivity.this)+"&query=" + search_contant;
                    LogUtil.e("将要加载==不为空==" + url);
                }
                setWebview(search_url);
                break;
        }
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

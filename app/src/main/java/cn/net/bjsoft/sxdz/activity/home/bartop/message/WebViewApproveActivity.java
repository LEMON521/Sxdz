package cn.net.bjsoft.sxdz.activity.home.bartop.message;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 专门显示Web页面的activity
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.activity_web)
public class WebViewApproveActivity extends BaseActivity {
    @ViewInject(R.id.web)
    private WebView web;//用于显示的web
    @ViewInject(R.id.title_back)
    private ImageView back;//返回
    @ViewInject(R.id.title_title)
    private TextView title;//标题
    @ViewInject(R.id.loading)
    private LinearLayout loading;//标题
//    @ViewInject(R.id.title)
//    private LinearLayout titles;//标题


    private String url = "";
    private String id = "";
    private String titleStr = "";
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        titles.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
        //LogUtil.e("底部栏url为=====" + url);
        titleStr = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        if (titleStr != null && !titleStr.equals("")) {
//            title.setText(titleStr);
            title.setText("");
        }

//在访问网络前先判断用户是否注销
        HttpPostUtils httpUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(SPUtil.getApiAuth(this) + "/load");
        params.addBodyParameter("source_id", "shuxin_know_type");
        httpUtils.get(this, params);
        httpUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("验证登录------------" + strJson);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(id)) {
            if (!url.contains("?")) {
                url = url + "?";
            }
            url = url /*+ "?"*/
                    + "&token=" + SPUtil.getToken(this)
                    + "&appid=" + SPUtil.getAppid(this)
                    + "&secret=" + SPUtil.getSecret(this)
                   /* + "&type=" + type*/
                    + "&id=" + id;
            setWebview();
        } else {
            MyToast.showShort(this, "参数错误,无法打开审批详情");
        }


    }

    public void setWebview() {
        web.getSettings().setSupportZoom(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(this,"android");

        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        web.getSettings().setAllowFileAccess(true);
//        web.getSettings().setAppCacheEnabled(true);
//        //web.getSettings().setPluginsEnabled(true);
//        web.getSettings().setSaveFormData(false);
        web.getSettings().setDomStorageEnabled(true);//DOM Storage
        //web.refreshPlugins(true);
//        web.getSettings().setLoadsImagesAutomatically(true);
        // http请求的时候，模拟为火狐的UA会造成实时公交那边的页面存在问题，所以模拟iPhone的ua来解决这个问题
//        String user_agent =
//                "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1";
//        web.getSettings().setUserAgentString(user_agent);
        /* Enable zooming */
//        web.getSettings().setSupportZoom(false);

//        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
//        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //web.loadUrl(url);

        web.loadUrl(url);// 加载出传入的URL
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("加载url为=====" + url);
                web.loadUrl(url);
                return true;// 必须返回True，才使用自定义的webView加载数据
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
                LogUtil.e("onPageFinished加载url为=====" + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loading.setVisibility(View.VISIBLE);
                LogUtil.e("onPageStarted加载url为=====" + url);
                super.onPageStarted(view, url, favicon);
            }

        });
    }


    //由于安全原因 targetSdkVersion>=17需要加 @JavascriptInterface
    //JS调用Android JAVA方法名和HTML中的按钮 onclick后的别名后面的名字对应

    @JavascriptInterface
    public void jsBack() {
//        ToastUtil.show(this,"点击返回");
        onClick(back);
    }

    @Event(value = {R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }
}

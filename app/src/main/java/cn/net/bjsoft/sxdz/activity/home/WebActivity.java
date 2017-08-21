package cn.net.bjsoft.sxdz.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 专门显示Web页面的activity
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.activity_web)
public class WebActivity extends BaseActivity {
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
    private String titleStr = "";
    private String userid = "";


    private OpenFileWebChromeClient mOpenFileWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        titles.setVisibility(View.GONE);
        mOpenFileWebChromeClient = new OpenFileWebChromeClient(this);
        back.setVisibility(View.VISIBLE);
        url = getIntent().getStringExtra("url");
        userid = getIntent().getStringExtra("userid");
        titleStr = getIntent().getStringExtra("title");


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

        if (!url.contains("?")) {
            url = url + "?";
        }
        if (!TextUtils.isEmpty(userid)) {
            userid.replace(".0", "");
        } else {
            userid = "";
        }


        url = url
                + "&id=" + userid
                + "&token=" + SPUtil.getToken(this)
                + "&appid=" + SPUtil.getAppid(this)
                + "&secret=" + SPUtil.getSecret(this);
        if (titleStr != null && !titleStr.equals("")) {
            title.setText(titleStr);
        }
        LogUtil.e("网页链接为----url" + url);
        setWebview();
    }

    public void setWebview() {
        web.setWebChromeClient(this.mOpenFileWebChromeClient);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(this, "android");

        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAppCacheEnabled(true);
//        web.getSettings().setPluginsEnabled(true);
        web.getSettings().setSaveFormData(true);
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

        // enablecrossdomain();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = web.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(web.getSettings(), true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


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

    /**
     * js调用的返回方法
     */
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == OpenFileWebChromeClient.REQUEST_FILE_PICKER) {
            if (mOpenFileWebChromeClient.mFilePathCallback != null) {
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                        : intent.getData();
                if (result != null) {
                    String path = MediaUtility.getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mOpenFileWebChromeClient.mFilePathCallback
                            .onReceiveValue(uri);
                } else {
                    mOpenFileWebChromeClient.mFilePathCallback
                            .onReceiveValue(null);
                }
            }
            if (mOpenFileWebChromeClient.mFilePathCallbacks != null) {
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                        : intent.getData();
                if (result != null) {
                    String path = MediaUtility.getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mOpenFileWebChromeClient.mFilePathCallbacks
                            .onReceiveValue(new Uri[]{uri});
                } else {
                    mOpenFileWebChromeClient.mFilePathCallbacks
                            .onReceiveValue(null);
                }
            }

            mOpenFileWebChromeClient.mFilePathCallback = null;
            mOpenFileWebChromeClient.mFilePathCallbacks = null;
        }
    }


//    public void enablecrossdomain() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            try {
//                Field webviewclassic_field = WebView.class.getDeclaredField("mProvider");
//                webviewclassic_field.setAccessible(true);
//                Object webviewclassic = webviewclassic_field.get(this);
//                Field webviewcore_field = webviewclassic.getClass().getDeclaredField("mWebViewCore");
//                webviewcore_field.setAccessible(true);
//                Object mWebViewCore = webviewcore_field.get(webviewclassic);
//                Field nativeclass_field = webviewclassic.getClass().getDeclaredField("mNativeClass");
//                nativeclass_field.setAccessible(true);
//                Object mNativeClass = nativeclass_field.get(webviewclassic);
//
//                Method method = mWebViewCore.getClass().getDeclaredMethod("nativeRegisterURLSchemeAsLocal", new Class[]{int.class, String.class});
//                method.setAccessible(true);
//                method.invoke(mWebViewCore, mNativeClass, "http");
//                method.invoke(mWebViewCore, mNativeClass, "https");
//            } catch (Exception e) {
//                LogUtil.e("enablecrossdomain error");
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                Field field = WebView.class.getDeclaredField("mWebViewCore");
//                field.setAccessible(true);
//                Object webviewcore = field.get(this);
//                Method method = webviewcore.getClass().getDeclaredMethod("nativeRegisterURLSchemeAsLocal", String.class);
//                method.setAccessible(true);
//                method.invoke(webviewcore, "http");
//                method.invoke(webviewcore, "https");
//            } catch (Exception e) {
//                LogUtil.e("enablecrossdomain error");
//                e.printStackTrace();
//            }
//        }
//
//
//    }

}

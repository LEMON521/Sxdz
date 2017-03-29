package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Zrzc on 2017/3/23.
 */

public class ShowHtmlView extends WebView {

    private String content = "";

    public ShowHtmlView(Context context) {
        super(context);

    }
    public  ShowHtmlView  (Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public  ShowHtmlView  (Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }
    public void init(String string){
        this.content = string;

        getSettings().setSupportZoom(false);
        getSettings().setUseWideViewPort(false);
        getSettings().setJavaScriptEnabled(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //web.loadData(sb.toString(), "text/html; charset=UTF-8", null);
        //web.loadUrl("https://www.baidu.com");
        loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                return true;// 必须返回True，才使用自定义的webView加载数据
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //view.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                super.onPageFinished(view, url);




            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //view.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                super.onPageStarted(view, url, favicon);
            }

        });
    }
}

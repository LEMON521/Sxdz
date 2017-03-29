package cn.net.bjsoft.sxdz.activity.welcome;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.utils.UrlUtil;

/**
 * 轮播图跳转页
 */
public class LinkToActivity extends BaseActivity {

    @ViewInject(R.id.web_linkto)
    WebView webLinkto;
    @ViewInject(R.id.title)
    TextView title;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_to);
        x.view().inject(this);

        url = getIntent().getStringExtra("url");
        title.setText(getIntent().getStringExtra("title"));
        url= UrlUtil.getUrl(url,this);
        setWebview();
    }

    public void setWebview() {
        webLinkto.getSettings().setSupportZoom(false);
        webLinkto.getSettings().setUseWideViewPort(false);
        webLinkto.getSettings().setJavaScriptEnabled(true);
        webLinkto.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
        webLinkto.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webLinkto.loadUrl(url);

        webLinkto.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 加载出传入的URL
                return true;// 必须返回True，才使用自定义的webView加载数据
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

        });
    }

}

package cn.net.bjsoft.sxdz.activity.home.barbotton;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;

/**
 * 扫一扫结果展示页
 */
public class ScanResultActivity extends BaseActivity {

    @ViewInject(R.id.web_scan)
    WebView webScan;

    private String url = "";
    private DatasBean datasBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        x.view().inject(this);

        url = UrlUtil.scanUrl +
                "module=rcjg&view=html&page=proving&appname=" + Constants.app_name +
                "&phone_uuid=" + SPUtil.getUserPUUID(this) +
                "&randcode=" + SPUtil.getUserRandCode(this) +
                "&uuid=" + SPUtil.getUserUUID(this) +
                "&user_id=" + SPUtil.getUserId(this);
        url = url+ "&query=" + getIntent().getStringExtra("scan");
        setWebview();
    }

    public void setWebview() {
        webScan.getSettings().setSupportZoom(false);
        webScan.getSettings().setUseWideViewPort(false);
        webScan.getSettings().setJavaScriptEnabled(true);
        webScan.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
        webScan.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webScan.loadUrl(url);

        webScan.setWebViewClient(new WebViewClient() {
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
    @Event(value = {R.id.scan_result_back})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.scan_result_back:
                finish();
                break;
        }
    }
}

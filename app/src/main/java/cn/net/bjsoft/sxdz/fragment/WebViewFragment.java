package cn.net.bjsoft.sxdz.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.utils.SPUtil;


/**
 * Homepage   ---- 报表页面
 * Created by zkagang on 2016/9/13.
 */
public class WebViewFragment extends BaseFragment {

    private View view;
    private WebView wv_home;
    private ImageView logo;
    private LinearLayout loading;

    private String url = "";
    private String tag = "";
    private String text = "";
    private TextView homepage_frag_toolbar;
    private ImageView back;
    private DatasBean datasBean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        text = getArguments().getString("text");
        url = getArguments().getString("url");

        //在访问网络前先判断用户是否注销
        HttpPostUtils httpUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(SPUtil.getApiAuth(this.getActivity()) + "/load");
        params.addBodyParameter("source_id", "shuxin_know_type");
        httpUtils.get(this.getActivity(), params);
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

        url = url
                + "&token=" + SPUtil.getToken(getContext())
                + "&appid=" + SPUtil.getAppid(getContext())
                + "&secret=" + SPUtil.getSecret(getContext());
        tag = getArguments().getString("tag");
        LogUtil.e("WebViewFragment==" + text);
        if (getArguments().getString("getBack") != null) {
            if (getArguments().getString("getBack").equals("false")) {
                back.setVisibility(View.GONE);
            }
        }
//        url = "https://www.baidu.com/";
        //url= UrlUtil.getUrl(url,getActivity());
        Log.e("tag", url);

        view = inflater.inflate(R.layout.fragment_home, container, false);
        back = (ImageView) view.findViewById(R.id.home_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        homepage_frag_toolbar = (TextView) view.findViewById(R.id.homepage_frag_toolbar);
        loading = (LinearLayout) view.findViewById(R.id.search_loading);
        homepage_frag_toolbar.setText(text);
        wv_home = (WebView) view.findViewById(R.id.home);
        logo = (ImageView) view.findViewById(R.id.home_logo);

        if (tag.equals("empty")) {
            wv_home.setVisibility(View.GONE);
            logo.setVisibility(View.VISIBLE);
        } else if (tag.equals("url")) {
            wv_home.setVisibility(View.VISIBLE);
            logo.setVisibility(View.GONE);
        } else {

        }
        setWebview();
        return view;
    }

    @Override
    public void initData() {

    }

    public void setWebview() {
        wv_home.getSettings().setSupportZoom(true);
        wv_home.getSettings().setUseWideViewPort(true);
        wv_home.getSettings().setJavaScriptEnabled(true);
        wv_home.addJavascriptInterface(this,"android");

        wv_home.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_home.getSettings().setAllowFileAccess(true);
        wv_home.getSettings().setAppCacheEnabled(true);
//        web.getSettings().setPluginsEnabled(true);
        wv_home.getSettings().setSaveFormData(true);
        wv_home.getSettings().setDomStorageEnabled(true);//DOM Storage

        wv_home.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
        wv_home.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_home.loadUrl(url);

        wv_home.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 加载出传入的URL
                return true;// 必须返回True，才使用自定义的webView加载数据
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loading.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

        });
    }

    @JavascriptInterface
    public void jsBack() {
//        ToastUtil.show(this,"点击返回");
        uploadOnClick(back);
    }

    @Event(value = {R.id.home_back})
    private void uploadOnClick(View view) {
        switch (view.getId()) {
            case R.id.home_back:
                getActivity().finish();
                break;
        }
    }
}

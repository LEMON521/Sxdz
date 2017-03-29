package cn.net.bjsoft.sxdz.activity.home.barbotton;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.URL;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.NewArticaleBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase16;
import cn.net.bjsoft.sxdz.utils.MyToast;

import static cn.net.bjsoft.sxdz.utils.AddressUtils.http_head;

/**
 * Created by Zrzc on 2016/12/27.
 */
public class NewsItemActivity extends Activity {

    private TextView news_item_title;
    private TextView news_item_author;
    private TextView news_item_time;
    private TextView news_item_body;
    private ImageView back;
    private WebView web;

    private Spanned sp;
    private Drawable drawable = null;
    private URL url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public NewArticaleBean getmNewsData() {
        return mNewsData;
    }

    public void setmNewsData(NewArticaleBean mNewsData) {
        this.mNewsData = mNewsData;
    }

    public NewArticaleBean mNewsData;

    Message msg = Message.obtain();
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //LogUtil.e("发送消息了");
            //init();
            super.handleMessage(msg);

            switch (msg.what) {
                case 100:
                    mNewsData = (NewArticaleBean) msg.obj;

                    initData(mNewsData);
                    break;

                case 200:
                    news_item_body.setText(sp);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_items_details);
        news_item_title = (TextView) findViewById(R.id.news_item_title);
        news_item_author = (TextView) findViewById(R.id.news_item_author);
        news_item_time = (TextView) findViewById(R.id.news_item_time);
        news_item_body = (TextView) findViewById(R.id.news_item_body);
        web = (WebView) findViewById(R.id.web);
        back = (ImageView) findViewById(R.id.news_item_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setId(getIntent().getStringExtra("id"));
        //news_item_title.setText(getId());
        // LogUtil.e("点击的新闻id为======="+getId());
        getData();
        //initData();
    }

    /**
     * 填充数据
     */
    private void initData(NewArticaleBean data) {
        news_item_title.setText(data.data.name);
        news_item_author.setText(data.data.author);
        news_item_time.setText(data.data.publictime);

//

        if (data.data.contents.startsWith("HEX")) {
            String contents = data.data.contents.substring(3);
            contents = MyBase16.decode(contents);


//            String a  =  contents.replace("<p>","");
//            String b  = a.replace("</p>","");
            LogUtil.e("返回的数据为----" + contents);

            StringBuilder sb = new StringBuilder(contents);


            //sb.insert(contents.indexOf("src=\"")+5,http_head);


            final String content = getNewContent(sb.toString());
            LogUtil.e("返回的数据为----" +content);
            //contents.indexOf("src=\"");
            web.getSettings().setSupportZoom(false);
            web.getSettings().setUseWideViewPort(false);
            web.getSettings().setJavaScriptEnabled(true);
            web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
            web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            //web.loadData(sb.toString(), "text/html; charset=UTF-8", null);
            //web.loadUrl("https://www.baidu.com");
            web.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

            web.setWebViewClient(new WebViewClient() {
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

    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.removeAttr("style");
            String src = element.toString();
            LogUtil.e("src------------" +src);
            String newSrc_1 = src.substring(src.indexOf("src=\"")+5);
            LogUtil.e("newSrc_1------------" +newSrc_1);
            String newSrc_2 = newSrc_1.substring(0,newSrc_1.indexOf("\""));
            LogUtil.e("newSrc_2------------" +newSrc_2);
            element.removeAttr("src");
            element.attr("src", http_head+newSrc_2);
            element.attr("width", "100%").attr("height", "auto");
            LogUtil.e("element------------" +element.toString());

        }

        return doc.toString();
    }

    /**
     * 指定保存照片或者视频的文件夹（绝对路径）
     *
     * @return 绝对路径地址
     */
    public String getSavePath() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //一定要指定文件名，不然会将下载的文件保存成为.tmp的文件
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + "shuxin" + File.separator + "picture" + File.separator + "picture.png";
        } else {
            path = getFilesDir().getAbsolutePath() + File.separator;
            //Toast.makeText(getActivity(), "SD卡不可用,将保存到手机内部", Toast.LENGTH_SHORT).show();
        }
        return path;
    }

    /**
     * 防止消息报错
     * error : This message is already in use
     *
     * @param msg
     */
    public void onMessage(Message msg) {
        if (mHandler.obtainMessage(msg.what, msg.obj) != null) {
            Message _msg = new Message();
            _msg.what = msg.what;
            _msg.obj = msg.obj;
            msg = _msg;
//			return;
        }
        mHandler.sendMessage(msg);
    }

    /**
     * 获取数据
     */
    private void getData() {
        //http://api.shuxin.net/Service/JsonData.aspx?
        // client_name=biip&
        // uuid=c2h1eGRAYmpzb2Z0Lm5ldC5jbnw5NmU3OTIxODk2NWViNzJjOTJhNTQ5ZGQ1YTMzMDExMnx0cnVl
        // action=load&
        // &method=load_article
        // moduleid=information&
        // start=0
        // &article_id=5110211411113889282&
        final String url = "http://api.shuxin.net/Service/JsonData.aspx?";
        final RequestParams params = new RequestParams(url);
        params.addBodyParameter("client_name", "biip");
        params.addBodyParameter("uuid", "cmNqZ19kZW1vfDk2ZTc5MjE4OTY1ZWI3MmM5MmE1NDlkZDVhMzMwMTEyfLK7v8nJvrP908O7p3wwfDg4OA==");
        params.addBodyParameter("action", "load");
        params.addBodyParameter("method", "load_article");
        params.addBodyParameter("moduleid", "information");
        //params.addBodyParameter("start", number);
        params.addBodyParameter("article_id", getId());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtils.e("上传结果为！！！！======" + responseInfo.result);
                //JSONObject jsonObject = new JSONObject(responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.optBoolean("success", false);
                    if (!success) {
                        MyToast.showShort(NewsItemActivity.this, "获取文章失败！");
                    } else {
                        setmNewsData(GsonUtil.getGson().fromJson(result, NewArticaleBean.class));

                        msg.obj = getmNewsData();
                        msg.what = 100;
                        //mHandler.sendMessage(msg);
                        onMessage(msg);
                        //LogUtils.e("获取到的文章详情："+responseInfo.result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

}

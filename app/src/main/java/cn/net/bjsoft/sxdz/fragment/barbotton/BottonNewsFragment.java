package cn.net.bjsoft.sxdz.fragment.barbotton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.barbotton.NewsItemActivity;
import cn.net.bjsoft.sxdz.adapter.FragmentNewsAdapter;
import cn.net.bjsoft.sxdz.bean.NewsItemsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.RefreshListView;


/**
 * Homepage -------新闻页面
 * Created by zkagang on 2016/9/13.
 */

public class BottonNewsFragment extends BaseFragment /*implements View.OnTouchListener*/ {


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    private int start = 0;//开始查询的条目数
    private int limit = 10;//一次查询数量
    private int current = 0;//当前的条目数
    /**
     * 是否加载更多
     * false：不加载更多--重新加载（加载最新）
     * true：加载更多，显示历史信息
     */
    private boolean getMore = false;//是否加载更多

    private View mView;
    private String url = "";
    private String text = "";

    private ImageView back;
    private RefreshListView listview_news;
    private LinearLayout fragmen_home_news_loading;
    private ProgressBar progress;
    private TextView hint;

    private TextView title;

    //private String news;
    private NewsItemsBean mNewsItemBean;
    public FragmentNewsAdapter mNewsAdapter;
    private ArrayList<NewsItemsBean.NewsData> mListNewsDatas = new ArrayList<NewsItemsBean.NewsData>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //savedInstanceState.size();
        if (mNewsAdapter != null) {
            mNewsAdapter = null;
            // mNewsAdapter.notifyDataSetChanged();
        }
        if (savedInstanceState != null) {
           //LogUtil.e("创建的嘿嘿savedInstanceState.size()" + savedInstanceState.size());
        }
        text = getArguments().getString("text");
        url = getArguments().getString("url");
       // LogUtil.e("BottonNewsFragment=="+text);
        mView = inflater.inflate(R.layout.homefragment_news, container, false);
        title = (TextView) mView.findViewById(R.id.include_title_news).findViewById(R.id.title_title);
        fragmen_home_news_loading = (LinearLayout) mView.findViewById(R.id.fragmen_home_news_loading);
        progress = (ProgressBar) mView.findViewById(R.id.fragmen_home_news_progress);
        back= (ImageView) mView.findViewById(R.id.include_title_news).findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        hint = (TextView) mView.findViewById(R.id.fragmen_home_news_hint);


        listview_news = (RefreshListView) mView.findViewById(R.id.fragmen_home_news);
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //LogUtil.e("新闻页面——--onActivityCreated");
        getData(false);//第一次加载，获取数据

        listview_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openNewsItemIntent = new Intent(getActivity(), NewsItemActivity.class);

                //因为我们给该ListView加上了一个刷新的头，所以position得-1
                //LogUtils.e("发送的id----"+getmNewsBean().data.get(position-1).id);
                //LogUtil.e("点击了第==" + position + "==个条目");
                openNewsItemIntent.putExtra("id", mListNewsDatas.get(position - 1).id);
                startActivity(openNewsItemIntent);
                //mNewsAdapter.notifyDataSetChanged();
            }
        });

        listview_news.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void pullDownRefresh() {
                //LogUtil.e("下拉刷新了=======");
                getData(false);
            }

            @Override
            public void pullUpLoadMore() {
               // LogUtil.e("上啦加载啦￥￥￥￥￥￥");
                getData(true);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initData() {

    }

    /**
     * 填充数据
     */
    private void initData(boolean b, NewsItemsBean newsItemsBean) {
        title.setText(text);
        if (newsItemsBean != null) {
            if (newsItemsBean.success) {
                //LogUtil.e("请求数量===" + newsItemsBean.data.size());
                if (newsItemsBean.data.size() > 0) {
                    if (!b) {//true 为加载更多，false为刷新最新的
                        //如果是刷新，就清除原有数据
                        mListNewsDatas.clear();
                        setStart(0);
                        //LogUtils.e("刷后前数量+++"+newsItemsBean.data.size());
                    }
                    //换上最新的
                    //LogUtils.e("加载更多前数量==="+newsItemsBean.data.size());
                    mListNewsDatas.addAll(newsItemsBean.data);
                    int x = 0;
                    for (NewsItemsBean.NewsData data : mListNewsDatas) {
                        //LogUtil.e("遍历的id为：：：：：" + data.id + ":::::" + x);
                        x++;
                    }
                    setCurrent(mListNewsDatas.size());
                    //LogUtil.e("加载更多后数量+++" + newsItemsBean.data.size());

                    if (mNewsAdapter == null) {
                      //  LogUtil.e("******为空，创建******");
                        mNewsAdapter = new FragmentNewsAdapter(getActivity(), mListNewsDatas);
                        listview_news.setAdapter(mNewsAdapter);
                    } else {
                      //  LogUtil.e("******已存在******");
                        mNewsAdapter.notifyDataSetInvalidated();
                        //mNewsAdapter.notifyDataSetChanged();
                    }
                }
                listview_news.onRefreshFinish();
                fragmen_home_news_loading.setVisibility(View.GONE);
                listview_news.setVisibility(View.VISIBLE);
            } else {//请求数据不成功
                MyToast.showShort(getActivity(), newsItemsBean.feedback);
                fragmen_home_news_loading.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                hint.setText(newsItemsBean.feedback);
                // reload.setVisibility(View.VISIBLE);
                listview_news.setVisibility(View.GONE);
            }
        } else {
            getData(false);
            //initData();
        }
    }


    /**
     * 获取新闻数据
     *
     * @return
     */
    private void getData(final boolean b) {

        if (b) {//加载更多
            setStart(getCurrent());
            LogUtil.e("setStart---" + getCurrent() + ":: getBoolean==" + b);
        } else {
            setStart(0);
            LogUtil.e("setStart---" + getStart() + ":: getBoolean==" + b);
        }
        LogUtil.e(SPUtil.getUserUUID(getActivity()));

        String url = "http://api.shuxin.net/service/jsondata.aspx?" + "client_name=biip" +
                "&phone_uuid=" + SPUtil.getUserPUUID(getActivity()) +
                "&randcode=" + SPUtil.getUserRandCode(getActivity()) +
                "&uuid=" + SPUtil.getUserUUID(getActivity()) + "" +

                /*上面的为总的url地址*/
                "&action=load" +
                "&moduleid=information&" +
//                "start=0" +
//                "&limit=10" +
                "&filter=&query=&" + ""
                + "method=load_articles_by_class";

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("start", getStart() + "");
        params.addBodyParameter("limit", limit + "");

        //LogUtil.e("请求新闻数据url======" + url);
        //这里写死了，分类加载在parmeter里面写

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    //NewsItemsBean newsBean = GsonUtil.getGson().fromJson(responseInfo.result, NewsItemsBean.class);
                    //NewsItemsBean.NewsData datas = GsonUtil.getGson().fromJson(responseInfo.result, NewsItemsBean.NewsData.class);
//                   if (newsBean.success) {
//                        getNewsDatas().addAll(newsBean.data);
//                        setCurrent(getNewsDatas().size());
//                        //news = responseInfo.result;
//                        LogUtils.e("发送消息zhiqian了");
//                        setNews(responseInfo.result.toString());
//                        setmNewsBean(newsBean);
                    LogUtil.e("请求新闻数据======" + result);
                    mNewsItemBean = GsonUtil.getGson().fromJson(result, NewsItemsBean.class);
                    initData(b, mNewsItemBean);
                    //这里应该将数据持久化，也就是说缓存
                    // mHandler.sendEmptyMessage(0);
//                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("请求数据失败！！");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Event(value = {R.id.title_back})
    private void newsOnClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                getActivity().finish();
                break;
        }
    }
}

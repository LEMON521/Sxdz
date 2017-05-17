package cn.net.bjsoft.sxdz.fragment.barbotton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.barbotton.NewsItemActivity;
import cn.net.bjsoft.sxdz.adapter.FragmentNewsAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.NewsItemsBean;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

import static cn.net.bjsoft.sxdz.utils.AddressUtils.http_shuxinyun_url;


/**
 * Homepage -------新闻页面
 * Created by zkagang on 2016/9/13.
 */
@ContentView(R.layout.fragment_news)
public class BottonNewsFragment_new extends BaseFragment  {

//    @ViewInject(R.id.swipeToLoadLayout)
    private SwipeToLoadLayout swipeLayout;

    @ViewInject(R.id.swipe_target)
    private ListView news_list;


    private NewsItemsBean mNewsItemBean;
    public FragmentNewsAdapter mNewsAdapter;
    private ArrayList<NewsItemsBean.NewsData> mListNewsDatas = new ArrayList<NewsItemsBean.NewsData>();

    private PostJsonBean postJsonBean;
    private String get_start = "0";
    private String get_count = "0";
    private String source_id = "";


    @Event(value = {R.id.title_back})
    private void newsOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);

        news_list=  (ListView) view.findViewById(R.id.swipe_target);

    }

    @Override
    public void initData() {
        initList();
    }

    private void initList() {
        if (mListNewsDatas == null) {
            mListNewsDatas = new ArrayList<>();
        }
        mListNewsDatas.clear();

        if (mNewsAdapter == null) {
            mNewsAdapter = new FragmentNewsAdapter(mActivity, mListNewsDatas);
        }

        news_list.setAdapter(mNewsAdapter);

        news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openNewsItemIntent = new Intent(getActivity(), NewsItemActivity.class);
                openNewsItemIntent.putExtra("id", mListNewsDatas.get(position).id);
                startActivity(openNewsItemIntent);
            }
        });

        getData();
    }

    private void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();
// /*上面的为总的url地址*/
//        "&action=load" +
//                "&moduleid=information&" +
////                "start=0" +
////                "&limit=10" +
//                "&filter=&query=&" + ""
//                + "method=load_articles_by_class";
//
//        RequestParams params = new RequestParams(url);
//        params.addBodyParameter("start", getStart() + "");
//        params.addBodyParameter("limit", limit + "");
        String url = http_shuxinyun_url;
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("action", "load");
        params.addBodyParameter("moduleid", "information");
        params.addBodyParameter("filter", "");
        params.addBodyParameter("method", "load_articles_by_class");
        params.addBodyParameter("start", get_start);
        params.addBodyParameter("limit", "10");
        params.addBodyParameter("query", "");

//        postJsonBean.start = get_start;//设置开始查询
//        postJsonBean.limit = "10";
//        params.addBodyParameter("data", postJsonBean.toString());
//        LogUtil.e("-------------------------bean.toString()" + postJsonBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {

                LogUtil.e("-----------------获取新闻消息----------------" + strJson);
//                taskBean = GsonUtil.getMessageTaskBean(strJson);
//                if (taskBean.code.equals("0")) {
//                    tasksDoingDao.addAll(taskBean.data.items);
//                    get_start = tasksDoingDao.size() + "";//设置开始查询
//                    get_count = taskBean.data.count + "";
//
//                    formateDatas();//格式化信息
//
//                    taskAdapter.notifyDataSetChanged();
//                    if (get_count.equals("0")) {
//                        MyToast.showShort(mActivity, "没有任何消息可查看!");
//                    }
//                } else {
//                    MyToast.showShort(mActivity, "获取消息失败-"/*+taskBean.msg*/);
//                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取消息-----------失败方法-----" + element.getMethodName());
                }

                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });

    }
}

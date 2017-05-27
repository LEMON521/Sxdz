package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.zdlf.WorkAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.zdlf.work.WorkBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.RollViewPager;

/**
 * Created by Zrzc on 2017/3/16.
 */
@ContentView(R.layout.fragment_work_zdlf)
public class WorkFragment extends BaseFragment {


    @ViewInject(R.id.fragment_work_root)
    private ScrollView root;//轮播图
//    @ViewInject(R.id.fragment_work_root_text)
//    private TextView root_text;//轮播图
    //==================轮播图================
    @ViewInject(R.id.scroll)
    private RelativeLayout scroll;//轮播图


    //放置顶部轮播图所在的线性布局
    @ViewInject(R.id.top_viewpager)
    private LinearLayout top_viewpager;
    //安放轮播图文字的TextView
    @ViewInject(R.id.top_title)
    private TextView top_title;
    //放置轮播图点对应的线性布局
    @ViewInject(R.id.dots_ll)
    private LinearLayout dots_ll;//标题


    //==================应用==================
    @ViewInject(R.id.ll_application_main)
    private LinearLayout ll_main;//主要应用
    @ViewInject(R.id.tv_application_main)
    private TextView tv_main;//主要应用
    @ViewInject(R.id.gv_application_main)
    private GridView gv_main;

    @ViewInject(R.id.ll_application_project)
    private LinearLayout ll_project;//项目应用
    @ViewInject(R.id.tv_application_project)
    private TextView tv_project;//项目应用
    @ViewInject(R.id.gv_application_project)
    private GridView gv_project;

    @ViewInject(R.id.ll_application_work)
    private LinearLayout ll_work;//工程应用
    @ViewInject(R.id.tv_application_work)
    private TextView tv_work;//工程应用
    @ViewInject(R.id.gv_application_work)
    private GridView gv_work;

    @ViewInject(R.id.ll_application_other)
    private LinearLayout ll_other;//其他应用
    @ViewInject(R.id.tv_application_other)
    private TextView tv_other;//其他应用
    @ViewInject(R.id.gv_application_other)
    private GridView gv_other;

    //数据类声明
    private WorkBean workBean;
    private WorkBean.WorkDataDao workDatas;
    private ArrayList<WorkBean.FunctionListDao> functionListDaos;
    private ArrayList<WorkBean.ScrollListDao> scrollListDaos;

    //存放顶部轮播图文字所在的集合
    private List<String> titleList = new ArrayList<String>();
    //存放顶部轮播图图片链接地址所在的集合
    private List<String> imgUrlList = new ArrayList<String>();
    //存放顶部轮播图url所在的集合
    private List<String> linktoUrlList = new ArrayList<String>();
    //存放点对应view对象的集合
    private List<ImageView> viewList = new ArrayList<ImageView>();


    private ArrayList<WorkBean.FunctionListDao> mainDaos;
    private ArrayList<WorkBean.FunctionListDao> projectDaos;
    private ArrayList<WorkBean.FunctionListDao> workDaos;
    private ArrayList<WorkBean.FunctionListDao> otherDaos;

    private WorkAdapter mainWorkAdapter;
    private WorkAdapter projectWorkAdapter;
    private WorkAdapter workWorkAdapter;
    private WorkAdapter otherWorkAdapter;

    private AdapterView.OnItemClickListener itemClickListener;
    private Intent intent;

    private PushCountReceiver pushCountReceiver;

    protected static final int SUCCESS = 1;
    protected static final int ERROR = -1;
    private Message message = null;
    /**
     * 向主线程发送消息的Handler
     */
    private Handler handlerWork = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 对数据进行解析和封装
                case SUCCESS:
                    LogUtil.e("成功啦!!!!!!!");
                    classifyData();
                    setData();
//                    dismissProgressDialog();
                    break;

                case ERROR:
                    //
                    //test.setText("网络异常,暂时无法获取数据/n点击刷新");
                    //LogUtil.e("获取到的条目--------失败!!!---");

                    dismissProgressDialog();
                    break;

                default:
                    break;
            }
        }

    };


    @Override
    public void initData() {
        if (message != null) {
            message = null;
        }

        showProgressDialog();
        message = new Message();
        getData();
    }


    /**
     * 获取数据
     */
    public void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$getItemsData$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "shuxin_work_top");


        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取文章条目----文章信息----------------" + strJson);
                workBean = GsonUtil.getWorkBean(strJson);
                if (workBean.code.equals("0")) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    workDatas = workBean.data;
                    message.what = SUCCESS;
                } else {
                    message.what = ERROR;
                }

//                itemsBean = GsonUtil.getKnowledgeItemsBean(strJson);
//                if (itemsBean.code.equals("0")) {//获取成功
//                    //LogUtil.e("获取到的条目-----------" + result);
//
//
//                    if (itemsBean.data.items != null && itemsBean.data.items.size() > 0) {
//
//                        itemsDataList.addAll(itemsBean.data.items);
//                        cacheItemsDataList.addAll(itemsBean.data.items);
//
//                        get_start = (itemsBean.data.items.size() + Integer.parseInt(get_start)) + "";
////                    get_cache_start= get_start;
////                        get_count = itemsBean.data.count;
//                        message.setVisibility(View.GONE);
//                        setItemsData();
//                    } else {
//                        message.setVisibility(View.VISIBLE);
//                        MyToast.showShort(mActivity, "已经没有更多的信息!");
//                    }

//
//                } else {
//                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取条目消息-----------失败方法-----" + element.getMethodName());
                }
                MyToast.showShort(mActivity, "获取数据失败!!");
                message.what = ERROR;
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                itemsAdapter.notifyDataSetChanged();
                handlerWork.sendMessage(message);
                dismissProgressDialog();
            }
        });


//        if (workDatas != null) {
//            workDatas = null;
//        }
//
//        /**
//         * 工作模块的数据,跟mobile.josn在一个文件夹下面
//         */
//        String url = apps_url + SPUtil.getAppid(mActivity) + "/work_items.json";
//        //String url = TestAddressUtils.test_get_work_url;
//        RequestParams params = new RequestParams(url);
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                workBean = GsonUtil.getWorkBean(result);
//                if (workBean.result) {
//                    //LogUtil.e("获取到的条目-----------" + result);
//                    workDatas = workBean.data;
//                    message.what = SUCCESS;
//                } else {
//                    message.what = ERROR;
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                //LogUtil.e("获取到的条目--------失败!!!---" + ex);
//                message.what = ERROR;
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                handlerWork.sendMessage(message);
//            }
//        });
    }

    /**
     * 将数据分类
     */
    private void classifyData() {
        //=================初始化数据容器
        if (functionListDaos == null) {
            functionListDaos = new ArrayList<>();
        }
        functionListDaos.clear();

        if (scrollListDaos == null) {
            scrollListDaos = new ArrayList<>();
        }
        scrollListDaos.clear();

        intent = new Intent(mActivity, WebActivity.class);
        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //LogUtil.e("parent" + parent.toString() + "::position" + position+"++parent.getId()"+parent.getId());
                String title = "";
                String url = "";
                switch (parent.getId()) {
                    case R.id.gv_application_main:
                        title = mainDaos.get(position).name;
                        url = mainDaos.get(position).url;
                        break;
                    case R.id.gv_application_project:
                        url = projectDaos.get(position).url;
                        title = projectDaos.get(position).name;
                        break;
                    case R.id.gv_application_work:
                        url = workDaos.get(position).url;
                        title = workDaos.get(position).name;
                        break;
                    case R.id.gv_application_other:
                        url = otherDaos.get(position).url;
                        title = otherDaos.get(position).name;
                        break;
                }
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        };

        //主要应用
        if (mainDaos == null) {
            mainDaos = new ArrayList<>();
        }
        mainDaos.clear();
        if (mainWorkAdapter == null) {
            mainWorkAdapter = new WorkAdapter(mActivity, mainDaos);
        }
        gv_main.setAdapter(mainWorkAdapter);
        gv_main.setOnItemClickListener(itemClickListener);


        //项目相关
        if (projectDaos == null) {
            projectDaos = new ArrayList<>();
        }
        projectDaos.clear();
        if (projectWorkAdapter == null) {
            projectWorkAdapter = new WorkAdapter(mActivity, projectDaos);
        }
        gv_project.setAdapter(projectWorkAdapter);
        gv_project.setOnItemClickListener(itemClickListener);


        //工作相关
        if (workDaos == null) {
            workDaos = new ArrayList<>();
        }
        workDaos.clear();
        if (workWorkAdapter == null) {
            workWorkAdapter = new WorkAdapter(mActivity, workDaos);
        }
        gv_work.setAdapter(workWorkAdapter);
        gv_work.setOnItemClickListener(itemClickListener);


        //其他相关
        if (otherDaos == null) {
            otherDaos = new ArrayList<>();
        }
        otherDaos.clear();
        if (otherWorkAdapter == null) {
            otherWorkAdapter = new WorkAdapter(mActivity, otherDaos);
        }
        gv_other.setAdapter(otherWorkAdapter);
        gv_other.setOnItemClickListener(itemClickListener);
        //=============初始化完毕

        //============添加数据
        //向功能列表添加数据
        functionListDaos.addAll(workDatas.function_list);
        //开始分类
        for (WorkBean.FunctionListDao function : functionListDaos) {
            if (function.type.equals("主要应用")) {
                mainDaos.add(function);
            } else if (function.type.equals("项目管理")) {
                projectDaos.add(function);
            } else if (function.type.equals("日常工作")) {
                workDaos.add(function);
            } else if (function.type.equals("其他应用")) {
                otherDaos.add(function);
            } else {

            }

        }


        //功能数据分类完毕

        //向轮播图添加数据
        scrollListDaos.addAll(workDatas.scroll_list);

    }

    /**
     * 将数据设置到页面中
     */
    public void setData() {

        setRollViewPage();

        if (mainDaos.size() > 0) {
            ll_main.setVisibility(View.VISIBLE);
            tv_main.setText(mainDaos.get(0).type);
            Utility.setGridViewHeightBasedOnChildren(gv_main, 4);
        } else {
            ll_main.setVisibility(View.GONE);
        }


        if (projectDaos.size() > 0) {
            ll_project.setVisibility(View.VISIBLE);
            tv_project.setText(projectDaos.get(0).type);
            Utility.setGridViewHeightBasedOnChildren(gv_project, 4);
        } else {
            ll_project.setVisibility(View.GONE);
        }


        if (workDaos.size() > 0) {
            ll_work.setVisibility(View.VISIBLE);
            tv_work.setText(workDaos.get(0).type);
            Utility.setGridViewHeightBasedOnChildren(gv_work, 4);
        } else {
            ll_work.setVisibility(View.GONE);
        }


        if (otherDaos.size() > 0) {
            ll_other.setVisibility(View.VISIBLE);
            tv_other.setText(otherDaos.get(0).type);
            Utility.setGridViewHeightBasedOnChildren(gv_other, 4);
        } else {
            ll_other.setVisibility(View.GONE);
        }

        if (!(scrollListDaos.size() > 0 || functionListDaos.size() > 0)) {
            root.setVisibility(View.GONE);
        } else {
            root.setVisibility(View.VISIBLE);
            setPushNum(getContext());
        }
    }

    //初始化轮播图-----------------------------开始--------------------------
    private void setRollViewPage() {
        //判断当前的轮播图中是否有数据，有数据做后续操作
        if (scrollListDaos.size() > 0) {
            scroll.setVisibility(View.VISIBLE);
            //通过顶部轮播图的数据填充界面
            RollViewPager rollViewPager = new RollViewPager(mActivity, viewList,
                    new RollViewPager.OnViewClickListener() {
                        @Override
                        public void viewClickListener(String title, String imgUrl, String LinktoUrl) {
                            //在当前的方法中要去获取图片的链接地址
                            //MyToast.showShort(mActivity, imgUrl);
                        }
                    });

            titleList.clear();
            imgUrlList.clear();
            linktoUrlList.clear();
            for (int i = 0; i < scrollListDaos.size(); i++) {
                titleList.add(scrollListDaos.get(i).file_text);

                String imageUrl = scrollListDaos.get(i).image_url;
                if (!imageUrl.startsWith("http:")) {
                    scrollListDaos.get(i).image_url = SPUtil.getUser_ApiData(mActivity) + "/" + imageUrl;
                }


                imgUrlList.add(scrollListDaos.get(i).image_url);
                linktoUrlList.add(scrollListDaos.get(i).url);
            }
            //点
            initDot();
            //文字
            rollViewPager.initTitle(titleList, top_title);
            //图片
            rollViewPager.initImgUrl(imgUrlList);

            //连接地址
            rollViewPager.initLinktoUrl(linktoUrlList);

            rollViewPager.startRoll();
            //底部一般列表页的数据填充界面

            //先将轮播图所在的线性布局内部内容全部移除掉
            top_viewpager.removeAllViews();
            //将轮播图对应的对象添加到线性布局中去
            top_viewpager.addView(rollViewPager);

//			if(lv_item_news.getHeaderViewsCount()<1){
//				lv_item_news.addHeaderView(layout_roll_view);
//			}
        } else {
            scroll.setVisibility(View.GONE);
        }
        dismissProgressDialog();
    }

    private void initDot() {
        //dots_ll添加点的线性布局
        dots_ll.removeAllViews();
        //安放点（view）对象的集合 viewList
        viewList.clear();

        for (int i = 0; i < imgUrlList.size(); i++) {
            ImageView view = new ImageView(mActivity);
//            View view = View.inflate(getContext(), R.layout.viewpager_item_dot, null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.item_img_dot);
            if (i == 0) {
                //构建第一个点
                view.setImageResource(R.drawable.page_on);
            } else {
                //后续的点
                view.setImageResource(R.drawable.page_off);
            }
            //将点放置到其所在的线性布局中

            //将宽高定义在夫控件上面，作用在子控件上面
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //控件间距
            layoutParams.setMargins(0, 0, 6, 0);
            dots_ll.addView(view, layoutParams);

            viewList.add(view);
        }
    }

    //初始化轮播图结束-------------------------------------------------------

    /**
     * 注销广播
     */
    @Override
    public void onDestroyView() {
        LogUtil.e("工作模块取消---了广播------------------");
        getActivity().unregisterReceiver(pushCountReceiver);
        super.onDestroyView();
    }

    /**
     * 注册广播
     *
     * @param context
     */
    @Override
    protected void onAttachToContext(Context context) {
        super.onAttachToContext(context);
        LogUtil.e("工作模块注册了广播------------------");
        /** 注册广播 */
        pushCountReceiver = new PushCountReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.net.bjsoft.sxdz.fragment_work");    //只有持有相同的action的接受者才能接收此广播
        context.registerReceiver(pushCountReceiver, filter);


    }

    private class PushCountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * 只需要改数据适配器的   集合的内容(刷新)---便可刷新GridView
             */
            LogUtil.e("工作模块获取到了推送数据==============");
//            private ArrayList<WorkBean.FunctionListDao> mainDaos;
//            private ArrayList<WorkBean.FunctionListDao> projectDaos;
//            private ArrayList<WorkBean.FunctionListDao> workDaos;
//            private ArrayList<WorkBean.FunctionListDao> otherDaos;
//            private WorkAdapter mainWorkAdapter;
//            private WorkAdapter projectWorkAdapter;
//            private WorkAdapter workWorkAdapter;
//            private WorkAdapter otherWorkAdapter;
            setPushNum(context);

        }
    }

    private void setPushNum(Context context) {
        if (mainDaos != null) {
            for (int i = 0; i < mainDaos.size(); i++) {
                if (mainDaos.get(i).tag.equals("project")) {
                    mainDaos.get(i).push_count = SPJpushUtil.getProject(context);
                } else if (mainDaos.get(i).tag.equals("engineering")) {
                    mainDaos.get(i).push_count = SPJpushUtil.getEngineering(context);
                } else if (mainDaos.get(i).tag.equals("contract")) {
                    mainDaos.get(i).push_count = SPJpushUtil.getContract(context);
                } else if (mainDaos.get(i).tag.equals("marketchannel")) {
                    mainDaos.get(i).push_count = SPJpushUtil.getMarketchannel(context);
                }
            }
            mainWorkAdapter.notifyDataSetChanged();
        }


        if (projectDaos != null) {
            for (int i = 0; i < projectDaos.size(); i++) {
                if (projectDaos.get(i).tag.equals("salereport")) {
                    projectDaos.get(i).push_count = SPJpushUtil.getSalereport(context);
                } else if (projectDaos.get(i).tag.equals("projectstat")) {
                    projectDaos.get(i).push_count = SPJpushUtil.getProjectstat(context);
                }
            }
            projectWorkAdapter.notifyDataSetChanged();
        }


        if (workDaos != null) {
            for (int i = 0; i < workDaos.size(); i++) {
                if (workDaos.get(i).tag.equals("engineeringlog")) {
                    workDaos.get(i).push_count = SPJpushUtil.getEngineeringlog(context);
                } else if (workDaos.get(i).tag.equals("emergency")) {
                    workDaos.get(i).push_count = SPJpushUtil.getEmergency(context);
                } else if (workDaos.get(i).tag.equals("engineeringeval")) {
                    workDaos.get(i).push_count = SPJpushUtil.getEngineeringeval(context);
                } else if (workDaos.get(i).tag.equals("construction")) {
                    workDaos.get(i).push_count = SPJpushUtil.getConstruction(context);
                } else if (workDaos.get(i).tag.equals("constructionteam")) {
                    workDaos.get(i).push_count = SPJpushUtil.getConstructionteam(context);
                }
            }
            workWorkAdapter.notifyDataSetChanged();
        }


        if (otherDaos != null) {
            for (int i = 0; i < otherDaos.size(); i++) {
                if (otherDaos.get(i).tag.equals("weekplan")) {
                    otherDaos.get(i).push_count = SPJpushUtil.getWeekplan(context);
                } else if (otherDaos.get(i).tag.equals("companyrun")) {
                    otherDaos.get(i).push_count = SPJpushUtil.getCompanyrun(context);
                } else if (otherDaos.get(i).tag.equals("sitemsg")) {
                    otherDaos.get(i).push_count = SPJpushUtil.getSitemsg(context);
                }
            }
            otherWorkAdapter.notifyDataSetChanged();
        }


    }


}

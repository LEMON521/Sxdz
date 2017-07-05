package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.WorkListAdapter;
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


    //    @ViewInject(R.id.fragment_work_root)
//    private ScrollView root;//轮播图
//    @ViewInject(R.id.scroll)
    private RelativeLayout scroll;//轮播图


    @ViewInject(R.id.fragment_work_functions)
    private ListView functionListView;

    @ViewInject(R.id.fragment_work_root_text)
    private TextView root_text;

    private View headView;
    //    @ViewInject(R.id.top_viewpager)
    private LinearLayout top_viewpager;
    //    @ViewInject(R.id.top_title)
    private TextView top_title;
    //    @ViewInject(R.id.dots_ll)
    private LinearLayout dots_ll;//标题

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

    private ArrayList<ArrayList<WorkBean.FunctionListDao>> fomateFunctionList;
    private WorkListAdapter workListAdapter;

    private PushCountReceiver pushCountReceiver;

    View.OnTouchListener touchListener;

    @Override
    public void initData() {
        touchListener = new View.OnTouchListener() {
            //屏蔽掉滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        };
        headView = View.inflate(mActivity, R.layout.head_work, null);
        top_viewpager = (LinearLayout) headView.findViewById(R.id.top_viewpager);
        top_title = (TextView) headView.findViewById(R.id.top_title);
        dots_ll = (LinearLayout) headView.findViewById(R.id.dots_ll);
        functionListView.addHeaderView(headView);//向listView增加head的时候,必须在ListView设置Adapter之前增加

        functionListView.setOnTouchListener(touchListener);

        if (fomateFunctionList == null) {
            fomateFunctionList = new ArrayList<>();
        }
        fomateFunctionList.clear();

        if (workListAdapter == null) {
            workListAdapter = new WorkListAdapter(mActivity, fomateFunctionList);
        }
        functionListView.setAdapter(workListAdapter);


        getData();
        //getDataTest();
    }

    private void getDataTest() {
        String s = "{\"code\":0,\"data\":{\"scroll_list\":[{\"id\":\"4718687404520726880\",\"file_text\":\"项目管理\",\"image_url\":\"Data/biip/upload/shuxin_work/4718687404520726880/image.jpg\",\"file_url\":\"http://api.shuxinyun.com/apps/preview.html?link_id=4669731623632737806\\u0026link_from=4895081593118139245\"},{\"id\":\"5633795284552484528\",\"file_text\":\"工程管理\",\"image_url\":\"Data/biip/upload/shuxin_work/5633795284552484528/image.jpg\",\"file_url\":\"http://api.shuxinyun.com/apps/preview.html?link_id=5120509048687776460\\u0026link_from=4895081593118139245\"},{\"id\":\"5495462057710350840\",\"file_text\":\"紧急情况\",\"image_url\":\"Data/biip/upload/shuxin_work/5495462057710350840/image.jpg\",\"file_url\":\"http://api.shuxinyun.com/apps/preview.html?link_id=app_eng_emergent_list\\u0026link_from=4895081593118139245\"}],\"function_list\":[{\"id\":\"4718687404520726880\",\"image_url\":\"Data/biip/upload/shuxin_work/4718687404520726880/logo.png\",\"name\":\"项目管理\",\"url\":\"http://api.shuxinyun.com/apps/preview.html?link_id=4669731623632737806\\u0026link_from=4895081593118139245\",\"tag\":\"project\",\"type\":\"主要应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5633795284552484528\",\"image_url\":\"Data/biip/upload/shuxin_work/5633795284552484528/logo.png\",\"name\":\"工程管理\",\"url\":\"http://api.shuxinyun.com/apps/preview.html?link_id=5120509048687776460\\u0026link_from=4895081593118139245\",\"tag\":\"engineering\",\"type\":\"主要应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5545422878315850041\",\"image_url\":\"Data/biip/upload/shuxin_work/5545422878315850041/logo.png\",\"name\":\"合同管理\",\"url\":\"\",\"tag\":\"contract\",\"type\":\"主要应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5428721251690695045\",\"image_url\":\"Data/biip/upload/shuxin_work/5428721251690695045/logo.png\",\"name\":\"市场通道\",\"url\":\"\",\"tag\":\"marketchannel\",\"type\":\"主要应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5416837067164988810\",\"image_url\":\"\",\"name\":\"测试应用\",\"url\":\"\",\"tag\":\"\",\"type\":\"主要应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5231771961045026914\",\"image_url\":\"Data/biip/upload/shuxin_work/5231771961045026914/logo.png\",\"name\":\"销售日报\",\"url\":\"\",\"tag\":\"salereport\",\"type\":\"项目管理\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5181108271404766184\",\"image_url\":\"Data/biip/upload/shuxin_work/5181108271404766184/logo.png\",\"name\":\"项目统计\",\"url\":\"\",\"tag\":\"projectstat\",\"type\":\"项目管理\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5261989518824174736\",\"image_url\":\"Data/biip/upload/shuxin_work/5261989518824174736/logo.png\",\"name\":\"工程日志\",\"url\":\"\",\"tag\":\"engineeringlog\",\"type\":\"项目管理\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5495462057710350840\",\"image_url\":\"Data/biip/upload/shuxin_work/5495462057710350840/logo.png\",\"name\":\"紧急情况\",\"url\":\"http://api.shuxinyun.com/apps/preview.html?link_id=app_eng_emergent_list\\u0026link_from=4895081593118139245\",\"tag\":\"emergency\",\"type\":\"日常工作\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"4842215491303777131\",\"image_url\":\"Data/biip/upload/shuxin_work/4842215491303777131/logo.png\",\"name\":\"工程评价\",\"url\":\"http://www.shuxin.net/api/nifty_demo/zdlf_app_grade_list.html\",\"tag\":\"engineeringeval\",\"type\":\"日常工作\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5474230569426966940\",\"image_url\":\"Data/biip/upload/shuxin_work/5474230569426966940/logo.png\",\"name\":\"施工能力\",\"url\":\"\",\"tag\":\"construction\",\"type\":\"日常工作\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"4615012956894582442\",\"image_url\":\"Data/biip/upload/shuxin_work/4615012956894582442/logo.png\",\"name\":\"施工队\",\"url\":\"\",\"tag\":\"\",\"type\":\"日常工作\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5723025381860387850\",\"image_url\":\"Data/biip/upload/shuxin_work/5723025381860387850/logo.png\",\"name\":\"周计划\",\"url\":\"\",\"tag\":\"weekplan\",\"type\":\"其他应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"5260828991081055753\",\"image_url\":\"Data/biip/upload/shuxin_work/5260828991081055753/logo.png\",\"name\":\"公司运营\",\"url\":\"\",\"tag\":\"companyrun\",\"type\":\"其他应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"4853777795985575160\",\"image_url\":\"Data/biip/upload/shuxin_work/4853777795985575160/logo.png\",\"name\":\"站内信\",\"url\":\"\",\"tag\":\"sitemsg\",\"type\":\"其他应用\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"},{\"id\":\"4774332732467809032\",\"image_url\":\"\",\"name\":\"测试\",\"url\":\"\",\"tag\":\"\",\"type\":\"测试添加\",\"type_icon\":\"\",\"type_url\":\"\",\"push_count\":\"0\"}]},\"msg\":\"\"}";
        workBean = GsonUtil.getWorkBean(s);
        if (workBean.code.equals("0")) {
            //LogUtil.e("获取到的条目-----------" + result);
            workDatas = workBean.data;
            classifyData();
            setData();
        } else {
            MyToast.showShort(mActivity, "获取数据失败!");
        }
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
        LogUtil.e("获取工作条目条目============params" + params.toString());

        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取工作条目条目----工作模块信息----------------" + strJson);
                workBean = GsonUtil.getWorkBean(strJson);
                if (workBean.code.equals("0")) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    workDatas = workBean.data;
                    classifyData();
                    setData();
                    root_text.setVisibility(View.GONE);
                } else {
                    root_text.setVisibility(View.VISIBLE);
                    MyToast.showShort(mActivity, "获取数据失败!");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取条目消息-----------失败方法-----" + element.getMethodName());
                }
                MyToast.showShort(mActivity, "获取数据失败!!");
                functionListView.removeHeaderView(headView);
                root_text.setVisibility(View.VISIBLE);
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

        functionListDaos = workDatas.function_list;

        HashMap<String, ArrayList<WorkBean.FunctionListDao>> map = new HashMap<>();
        ArrayList<String> listString = new ArrayList<>();
        for (WorkBean.FunctionListDao dao : functionListDaos) {
            if (!map.containsKey(dao.type)) {
                ArrayList<WorkBean.FunctionListDao> list = new ArrayList<>();
                list.add(dao);
                map.put(dao.type, list);
                listString.add(dao.type);//map的put方法会把key的顺序打乱,list不会打乱
            } else {
                map.get(dao.type).add(dao);
            }
        }

        for (int i = 0; i < map.size(); i++) {
            fomateFunctionList.add(map.get(listString.get(i)));
        }


        Utility.setListViewHeightBasedOnChildren(functionListView);
        workListAdapter.notifyDataSetChanged();
        setPushNum(mActivity);
        //功能数据分类完毕

        //向轮播图添加数据
        scrollListDaos.addAll(workDatas.scroll_list);

    }

    /**
     * 将数据设置到页面中
     */
    public void setData() {

        setRollViewPage();


//        if (!(scrollListDaos.size() > 0 || functionListDaos.size() > 0)) {
//            root.setVisibility(View.GONE);
//        } else {
//            root.setVisibility(View.VISIBLE);
//            setPushNum(getContext());
//        }

    }

    //初始化轮播图-----------------------------开始--------------------------
    private void setRollViewPage() {
        //判断当前的轮播图中是否有数据，有数据做后续操作
        if (scrollListDaos.size() > 0) {
//            scroll.setVisibility(View.VISIBLE);
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
            functionListView.removeHeaderView(headView);
//            scroll.setVisibility(View.GONE);
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
            setPushNum(context);

        }
    }

    private void setPushNum(Context context) {
        for (ArrayList<WorkBean.FunctionListDao> list : fomateFunctionList) {
            for (WorkBean.FunctionListDao dao : list) {
                if (dao.tag.equals("project")) {
                    dao.push_count = SPJpushUtil.getProject(context);
                } else if (dao.tag.equals("engineering")) {
                    dao.push_count = SPJpushUtil.getEngineering(context);
                } else if (dao.tag.equals("contract")) {
                    dao.push_count = SPJpushUtil.getContract(context);
                } else if (dao.tag.equals("marketchannel")) {
                    dao.push_count = SPJpushUtil.getMarketchannel(context);
                } else if (dao.tag.equals("salereport")) {
                    dao.push_count = SPJpushUtil.getSalereport(context);
                } else if (dao.tag.equals("projectstat")) {
                    dao.push_count = SPJpushUtil.getProjectstat(context);
                } else if (dao.tag.equals("engineeringlog")) {
                    dao.push_count = SPJpushUtil.getEngineeringlog(context);
                } else if (dao.tag.equals("emergency")) {
                    dao.push_count = SPJpushUtil.getEmergency(context);
                } else if (dao.tag.equals("engineeringeval")) {
                    dao.push_count = SPJpushUtil.getEngineeringeval(context);
                } else if (dao.tag.equals("construction")) {
                    dao.push_count = SPJpushUtil.getConstruction(context);
                } else if (dao.tag.equals("constructionteam")) {
                    dao.push_count = SPJpushUtil.getConstructionteam(context);
                } else if (dao.tag.equals("weekplan")) {
                    dao.push_count = SPJpushUtil.getWeekplan(context);
                } else if (dao.tag.equals("companyrun")) {
                    dao.push_count = SPJpushUtil.getCompanyrun(context);
                } else if (dao.tag.equals("sitemsg")) {
                    dao.push_count = SPJpushUtil.getSitemsg(context);
                }
            }
        }
        workListAdapter.notifyDataSetChanged();
    }


}

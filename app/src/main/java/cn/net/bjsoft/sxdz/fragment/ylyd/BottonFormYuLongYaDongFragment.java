package cn.net.bjsoft.sxdz.fragment.ylyd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.activity.home.function.form.FunctionFormActivity;
import cn.net.bjsoft.sxdz.adapter.function.form.FunctionFormAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.HomepageBean;
import cn.net.bjsoft.sxdz.bean.app.ModulesBean;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormBean;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormDataItemsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.RollViewPager;


/**
 * 裕隆亚东药业的报表页面   - -   我的页面
 * Created by 靳宁宁 on 2017/3/8.
 */
@ContentView(R.layout.fragment_form_ylyd)
public class BottonFormYuLongYaDongFragment extends BaseFragment {


    @ViewInject(R.id.root_ll)
    private LinearLayout root;//
    @ViewInject(R.id.title_back)
    private ImageView back;//返回
    @ViewInject(R.id.title_title)
    private TextView title;//标题
    //@ViewInject(R.id.fragment_form_ylyd_rollViewPager)
    //private RollViewPager rollViewPager;//标题


    //存放顶部轮播图文字所在的集合
    private List<String> titleList = new ArrayList<String>();
    //存放顶部轮播图图片链接地址所在的集合
    private List<String> imgUrlList = new ArrayList<String>();
    //存放顶部轮播图文字所在的集合
    private List<String> linktoUrlList = new ArrayList<String>();
    //存放点对应view对象的集合
    private List<ImageView> viewList = new ArrayList<ImageView>();
    //放置顶部轮播图所在的线性布局

    @ViewInject(R.id.fragment_form_roll)
    private RelativeLayout roll_root;//轮播图根节点,没有数据则隐藏


    @ViewInject(R.id.top_viewpager)
    private LinearLayout top_viewpager;
    //安放轮播图文字的TextView
    @ViewInject(R.id.top_title)
    private TextView top_title;
    //放置轮播图点对应的线性布局
    @ViewInject(R.id.dots_ll)
    private LinearLayout dots_ll;//标题

//    private YLYDFormDao formBean;
//    private YLYDFormDao.ScrollPageBean scrollPageBean;
//    private ArrayList<FunctionFormDataItemsBean> scroll_list;


    @ViewInject(R.id.tree_view)
    private ListView treeView;
    @ViewInject(R.id.fragment_form_info)
    private TextView form_info;

    private FunctionFormBean scrollFormBean;
    private ArrayList<FunctionFormDataItemsBean> scrollBeanList;

    private FunctionFormBean functionFormBean;
    private ArrayList<FunctionFormDataItemsBean> formBeanList;
    private FunctionFormAdapter functionFormAdapter;


//    private YLYDFormDao.TreeListBean treeListBean;
//    private ArrayList<YLYDFormDao.TreeListBean> tree_list;

//    private FileBean bean;
//    private List<FileBean> mDatas = new ArrayList<FileBean>();
//    private ListView mTree;
//    private TreeListViewAdapter mAdapter;


    private AppBean appBean;
    private ArrayList<HomepageBean> homepageBeen;
    private ModulesBean modulesBean;

    private Bundle mBundle;
    private String fragmentTag;

    private String img_source_id = "";
    private String img_target = "";
    private String stat_parent_id = "";
    private String stat_source_id = "";
    private String stat_target = "";

    @Override
    public void initData() {
        title.setText("报表");
        LogUtil.e("initData" + "1111111111111111111111111");
        appBean = GsonUtil.getAppBean(SPUtil.getMobileJson(getActivity()));

        homepageBeen = appBean.homepage;
        modulesBean = new ModulesBean();

        mBundle = getArguments();

        fragmentTag = mBundle.getString("tag");


        if (fragmentTag != null) {
            LogUtil.e("==========fragmentTag===========" + fragmentTag);
            if (fragmentTag.equals("form_ylyd")) {//底部栏创建
                for (int i = 0; i < homepageBeen.size(); i++) {
                    if (homepageBeen.get(i).tag.equals("form_ylyd")) {
                        modulesBean.form_ylyd = homepageBeen.get(i).tag_params;
                    }
                }
            } else if (fragmentTag.equals("")) {
                modulesBean.form_ylyd = appBean.modules.form_ylyd;

            }

        }

//        if (modulesBean.signin != null && !(modulesBean.signin.size() > 0)) {
//
//        }
        if (modulesBean.form_ylyd != null && modulesBean.form_ylyd.size() > 0) {
            img_source_id = modulesBean.form_ylyd.get("img_source_id");
            img_target = modulesBean.form_ylyd.get("img_target");
            stat_parent_id = modulesBean.form_ylyd.get("stat_parent_id");
            stat_source_id = modulesBean.form_ylyd.get("stat_source_id");
            stat_target = modulesBean.form_ylyd.get("stat_target");
            LogUtil.e("---------------.img_source_id----------------" + img_source_id);
            LogUtil.e("---------------.img_target----------------" + img_target);
            LogUtil.e("---------------.stat_parent_id----------------" + stat_parent_id);
            LogUtil.e("---------------.stat_source_id----------------" + stat_source_id);
            LogUtil.e("---------------.stat_target----------------" + stat_target);
        }


        initList();

        roll_root.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(img_source_id)) {
            roll_root.setVisibility(View.VISIBLE);
            getImageListData();
            //getTest();
        }

        getFormData();


        //getFormDataTest();

    }

    private void initList() {


        if (scrollBeanList == null) {
            scrollBeanList = new ArrayList<>();
        }
        scrollBeanList.clear();

        if (formBeanList == null) {
            formBeanList = new ArrayList<>();
        }
        formBeanList.clear();

        if (functionFormAdapter == null) {
            functionFormAdapter = new FunctionFormAdapter(mActivity, formBeanList);
        }

        treeView.setAdapter(functionFormAdapter);

        treeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (TextUtils.isEmpty(formBeanList.get(position).url)) {
                    Intent childIntent = new Intent(mActivity, FunctionFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("stat_source_id", stat_source_id);
                    bundle.putString("stat_target", stat_target);
                    bundle.putString("parent_id", formBeanList.get(position).id);
                    bundle.putString("title", formBeanList.get(position).name);
                    childIntent.putExtra("form_data", bundle);
                    startActivity(childIntent);
                } else {
                    if (!TextUtils.isEmpty(formBeanList.get(position).url)) {
                        Intent webIntent = new Intent(mActivity, WebActivity.class);
                        webIntent.putExtra("url", formBeanList.get(position).url);
                        webIntent.putExtra("title", formBeanList.get(position).name);
                        startActivity(webIntent);
                    } else {
                        MyToast.showShort(getActivity(), "此报表暂时没有信息");
                    }

                }
            }
        });
    }

    /**
     * 测试用例
     */
    private void getTest() {
        String s = "{\"code\":0,\"data\":{\"count\":4,\"items\":[{\"id\":\"4665476213310348420\",\"root_company_id\":\"1\",\"name\":\"采购\",\"type\":\"5105437112935500600\",\"company_id\":\"1\",\"url\":\"http://api.shuxinyun.com/apps/4762916269958594327/index.html\",\"logo\":\"http://www.shuxin.net/api/app_json/android/form/form_1.jpg\",\"sort_index\":\"2\",\"description\":\"HEXe98787e8b4ade68aa5e8a1a8e6b58be8af95\"},{\"id\":\"5638558434117563009\",\"root_company_id\":\"1\",\"name\":\"测试报表\",\"type\":\"5105437112935500600\",\"company_id\":\"1\",\"url\":\"http://api.shuxinyun.com/apps/4762916269958594327/index.html\",\"logo\":\"http://www.shuxin.net/api/app_json/android/form/form_2.jpg\",\"sort_index\":\"1\",\"description\":\"\"},{\"id\":\"5347274290810785541\",\"root_company_id\":\"1\",\"name\":\"出差报表\",\"type\":\"5105437112935500600\",\"company_id\":\"1\",\"url\":\"http://api.shuxinyun.com/apps/4762916269958594327/index.html\",\"logo\":\"http://www.shuxin.net/api/app_json/android/form/form_3.jpg\",\"sort_index\":\"3\",\"description\":\"HEXe587bae5b7aee68aa5e8a1a8e6b58be8af95\"},{\"id\":\"5241553196217971907\",\"root_company_id\":\"1\",\"name\":\"报表测试1\",\"type\":\"5617292309407608442\",\"company_id\":\"1\",\"url\":\"http://api.shuxinyun.com/apps/4762916269958594327/index.html\",\"logo\":\"http://www.shuxin.net/api/app_json/android/form/form_4.jpg\",\"sort_index\":\"4\",\"description\":\"HEXe68aa5e8a1a8e6b58be8af9531\"}]},\"msg\":\"select id,root_company_id,name,type,company_id,url,logo,sort_index,description from shuxin_report  where (root_company_id = 1)\"}\n";

        scrollFormBean = GsonUtil.getFunctionFormBean(s);

        if (scrollFormBean != null) {
            if (scrollFormBean.code.equals("0")) {

                if (scrollFormBean.data.items != null) {
                    scrollBeanList.addAll(scrollFormBean.data.items);
                    if (scrollBeanList.size() > 0) {
                        roll_root.setVisibility(View.VISIBLE);
                        setRollViewPage();
                    }

                }
            }
        }
    }

    private void getImageListData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", img_source_id);
        params.addBodyParameter("target", img_target);

        LogUtil.e("-------------------------source_id.toString()--" + img_target);
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取报表----轮播图信息----------------" + strJson);
                scrollFormBean = GsonUtil.getFunctionFormBean(strJson);

                if (scrollFormBean != null) {
                    if (scrollFormBean.code.equals("0")) {

                        if (scrollFormBean.data.items != null) {
                            scrollBeanList.addAll(scrollFormBean.data.items);
                            if (scrollBeanList.size() > 0) {
                                roll_root.setVisibility(View.VISIBLE);
                                setRollViewPage();
                            }

                        }
                    }
                }

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

    private void getFormData() {


        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", stat_source_id);
        params.addBodyParameter("target", stat_target);
//        {data:{parent_id:stat_parent_id}}
        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");


        sb.append("\"data\":{");

        sb.append("\"parent_id\":\"");
        sb.append(stat_parent_id);
        sb.append("\"");

        sb.append("}");
        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        LogUtil.e("-------------------------data.toString()--" + sb.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取报表----报表信息----------------" + strJson);

                functionFormBean = GsonUtil.getFunctionFormBean(strJson);

                if (functionFormBean.code.equals("0")) {
                    if (functionFormBean.data.items != null) {

                        formBeanList.addAll(functionFormBean.data.items);
                        functionFormAdapter.notifyDataSetChanged();
                        if (formBeanList.size() > 0) {
                            form_info.setVisibility(View.GONE);
                        } else {
                            form_info.setVisibility(View.VISIBLE);
                        }
                    } else {
                        form_info.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                form_info.setVisibility(View.VISIBLE);

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

    //初始化轮播图-----------------------------开始--------------------------
    private void setRollViewPage() {
        //判断当前的轮播图中是否有数据，有数据做后续操作
        if (scrollBeanList.size() > 0) {

            //通过顶部轮播图的数据填充界面
            RollViewPager rollViewPager = new RollViewPager(mActivity, viewList,
                    new RollViewPager.OnViewClickListener() {
                        @Override
                        public void viewClickListener(String title, String imgUrl, String LinktoUrl) {
                            //在当前的方法中要去获取图片的链接地址
                            //MyToast.showShort(mActivity, LinktoUrl);

                            if (!TextUtils.isEmpty(LinktoUrl)) {
                                Intent webIntent = new Intent(mActivity, WebActivity.class);
                                webIntent.putExtra("url", LinktoUrl);
                                webIntent.putExtra("title", title);
                                startActivity(webIntent);
                            } else {
                                MyToast.showShort(mActivity, "暂时没有数据!");
                            }
                        }
                    });

            titleList.clear();
            imgUrlList.clear();
            for (int i = 0; i < scrollBeanList.size(); i++) {
                titleList.add(scrollBeanList.get(i).name);
                imgUrlList.add(scrollBeanList.get(i).logo);
                linktoUrlList.add(scrollBeanList.get(i).url);
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
            top_viewpager.setVisibility(View.GONE);
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

    //初始化树形结构---------------------------开始----------------------------

//    private void setTreeView() {
//        mDatas.clear();
////        getItems(tree_list, 0);
//        try {
//
//            mAdapter = new SimpleTreeAdapter<FileBean>(treeView, mActivity, mDatas, 1);
//            treeView.setAdapter(mAdapter);
//
//            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//                @Override
//                public void onClick(Node node, int position) {
//                    Log.e("点击的条目", "tiaomu wei ====" + position);
//                    String url = mDatas.get(position).getUrl();
//
//                    if (url != null) {
//                        if (!url.equals("")) {
//                            //Toast.makeText(MainActivity.this, "点击了我!"+url, Toast.LENGTH_SHORT).show();
//                            //MyToast.showShort(mActivity, "文档的url为---" + url);
//                            if (!TextUtils.isEmpty(scrollBeanList.get(position).url)) {
//                                Intent webIntent = new Intent(mActivity, WebActivity.class);
//                                webIntent.putExtra("url", scrollBeanList.get(position).url);
//                                webIntent.putExtra("title", scrollBeanList.get(position).name);
//                                startActivity(webIntent);
//                            } else {
//                                MyToast.showShort(mActivity, "暂时没有数据!");
//                            }
//
//                        }
//                    }
//                }
//            });
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //将网络数据添加到本地的数据格式中去
//    public void getItems(ArrayList<YLYDFormDao.TreeListBean> list, int level) {
//        level++;
//
//        for (YLYDFormDao.TreeListBean children : list) {
//
//            if (children.children != null) {
//                bean = new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name);
//                if (!TextUtils.isEmpty(children.url)) {
//                    bean.setUrl(children.url);
//                }
//                mDatas.add(bean);
//
//                getItems(children.children, level);
//            } else {
//                if (!children.name.equals("")) {
//                    //mDatas.add(new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name));
//                    bean = new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name);
//                    if (!TextUtils.isEmpty(children.url)) {
//                        bean.setUrl(children.url);
//                    }
//                    mDatas.add(bean);
//                }
//            }
//        }
//    }
//    //初始化树形结构---------------------------结束----------------------------
}

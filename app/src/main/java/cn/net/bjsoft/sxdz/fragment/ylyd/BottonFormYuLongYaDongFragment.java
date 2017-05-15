package cn.net.bjsoft.sxdz.fragment.ylyd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.activity.home.function.form.FunctionFormActivity;
import cn.net.bjsoft.sxdz.adapter.function.form.FunctionFormAdapter;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormBean;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormDatasBean;
import cn.net.bjsoft.sxdz.bean.ylyd.form.YLYDFormDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.RollViewPager;
import cn.net.bjsoft.sxdz.view.treeview.bean.FileBean;
import cn.net.bjsoft.sxdz.view.treeview.helper.Node;
import cn.net.bjsoft.sxdz.view.treeview.helper.SimpleTreeAdapter;
import cn.net.bjsoft.sxdz.view.treeview.helper.TreeListViewAdapter;

import static cn.net.bjsoft.sxdz.utils.function.TestAddressUtils.test_get_function_form_url;


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
    //存放点对应view对象的集合
    private List<ImageView> viewList = new ArrayList<ImageView>();
    //放置顶部轮播图所在的线性布局
    @ViewInject(R.id.top_viewpager)
    private LinearLayout top_viewpager;
    //安放轮播图文字的TextView
    @ViewInject(R.id.top_title)
    private TextView top_title;
    //放置轮播图点对应的线性布局
    @ViewInject(R.id.dots_ll)
    private LinearLayout dots_ll;//标题

    private YLYDFormDao formBean;
    private YLYDFormDao.ScrollPageBean scrollPageBean;
    private ArrayList<YLYDFormDao.ScrollPageBean> scroll_list;


    @ViewInject(R.id.tree_view)
    private ListView treeView;

    private FunctionFormBean formsBean;
    private ArrayList<FunctionFormDatasBean> dataBeanList;
    private FunctionFormAdapter adapter;


//    private YLYDFormDao.TreeListBean treeListBean;
//    private ArrayList<YLYDFormDao.TreeListBean> tree_list;

    private FileBean bean;
    private List<FileBean> mDatas = new ArrayList<FileBean>();
    private ListView mTree;
    private TreeListViewAdapter mAdapter;

    @Override
    public void initData() {
        showProgressDialog();
        title.setText("报表");
        LogUtil.e("initData"+"1111111111111111111111111");
//        view = (RelativeLayout) View.inflate(mActivity,R.layout.layout_roll_view,null);
        //root.addView(view);
//        top_viewpager = (LinearLayout) view.findViewById(R.id.top_viewpager);
//        top_title = (TextView) view.findViewById(R.id.top_title);
//        dots_ll = (LinearLayout) view.findViewById(R.id.dots_ll);

        initList();
        getFormDataTest();





    }

    private void initList(){
        if (dataBeanList==null) {
            dataBeanList = new ArrayList<>();
        }
        dataBeanList.clear();

        if (adapter==null) {
            adapter = new FunctionFormAdapter(mActivity,dataBeanList);
        }

        treeView.setAdapter(adapter);

        treeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(dataBeanList.get(position).url)) {//不为空,那么就是子节点,并打开网页
                    Intent webIntent = new Intent(mActivity, WebActivity.class);
                    webIntent.putExtra("url", dataBeanList.get(position).url);
                    webIntent.putExtra("title", dataBeanList.get(position).title);
                    startActivity(webIntent);
                } else {//不为空,那么他就有子节点
                    Intent childIntent = new Intent(mActivity, FunctionFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("form_id", dataBeanList.get(position).id);
                    bundle.putString("title", dataBeanList.get(position).title);
                    childIntent.putExtra("form_data", bundle);
                    startActivity(childIntent);
                }
            }
        });
    }

    private void getFormDataTest(){

        String url = test_get_function_form_url;
        RequestParams params = new RequestParams(url);
        //params.addBodyParameter("form_id", formId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("-------------result------------"+result);
                formsBean = GsonUtil.getFunctionFormBean(result);
                dataBeanList.addAll(formsBean.data);

                LogUtil.e("-------------dataBeanList------------"+dataBeanList.size());

                adapter.notifyDataSetChanged();

                getFormData();
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

    private void getFormData() {
        if (scroll_list != null) {
            scroll_list = null;
        }

        RequestParams params = new RequestParams(TestAddressUtils.test_get_form_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                formBean = GsonUtil.getYLYDFormBean(result);
                if (formBean.result) {
                    LogUtil.e("获取到报表数据-----------" + result);
                    //scroll_list.clear();
                    scroll_list = formBean.data.scroll_list;
//                    tree_list = formBean.data.tree_list;

                    setRollViewPage();
                } else {
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

    //初始化轮播图-----------------------------开始--------------------------
    private void setRollViewPage() {
        //判断当前的轮播图中是否有数据，有数据做后续操作
        if (scroll_list.size() > 0) {

            //通过顶部轮播图的数据填充界面
            RollViewPager rollViewPager = new RollViewPager(mActivity, viewList,
                    new RollViewPager.OnViewClickListener() {
                        @Override
                        public void viewClickListener(String imgUrl) {
                            //在当前的方法中要去获取图片的链接地址
                            MyToast.showShort(mActivity, imgUrl);
                        }
                    });

            titleList.clear();
            imgUrlList.clear();
            for (int i = 0; i < scroll_list.size(); i++) {
                titleList.add(scroll_list.get(i).file_text);
                imgUrlList.add(scroll_list.get(i).image_url);

            }
            //点
            initDot();
            //文字
            rollViewPager.initTitle(titleList, top_title);
            //图片
            rollViewPager.initImgUrl(imgUrlList);
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

    private void setTreeView(){
        mDatas.clear();
//        getItems(tree_list, 0);
        try {

            mAdapter = new SimpleTreeAdapter<FileBean>(treeView, mActivity, mDatas, 1);
            treeView.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    Log.e("点击的条目","tiaomu wei ===="+position);
                    String url= mDatas.get(position).getUrl();

                    if (url!=null) {
                        if (!url.equals("")) {
                            //Toast.makeText(MainActivity.this, "点击了我!"+url, Toast.LENGTH_SHORT).show();
                            MyToast.showShort(mActivity,"文档的url为---"+url);
                        }
                    }
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //将网络数据添加到本地的数据格式中去
    public void getItems(ArrayList<YLYDFormDao.TreeListBean> list, int level) {
        level++;

        for (YLYDFormDao.TreeListBean children : list) {

            if (children.children != null) {
                bean = new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name);
                if (!TextUtils.isEmpty(children.url)){
                    bean.setUrl(children.url);
                }
                mDatas.add(bean);

                getItems(children.children, level);
            } else {
                if (!children.name.equals("")) {
                    //mDatas.add(new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name));
                    bean = new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name);
                    if (!TextUtils.isEmpty(children.url)){
                        bean.setUrl(children.url);
                    }
                    mDatas.add(bean);
                }
            }
        }
    }
    //初始化树形结构---------------------------结束----------------------------
}

package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.proposal.NewProposalActivity;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeGroupAdapter;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemsAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;
import cn.net.bjsoft.sxdz.dialog.KnowledgeSearchPopupWindow_1;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullToRefreshLayout;
import cn.net.bjsoft.sxdz.view.pull_to_refresh.PullableListView;

/**
 * Created by Zrzc on 2017/3/17.
 */


@ContentView(R.layout.fragment_konwledge)
public class KnowledgeZDLFFragment extends BaseFragment {
    @ViewInject(R.id.knowledge_search)
    private LinearLayout search;
    //    @ViewInject(R.id.search_edittext)
//    private EditText search_edit;
    @ViewInject(R.id.knowledge_rg_group)
    private RadioGroup rg_group;

    //    @ViewInject(R.id.knowledge_hint)
//    private TextView hint;
    @ViewInject(R.id.knowledge_group)
    private ListView group;
    //    @ViewInject(R.id.knowledge_items)
//    private RefreshListView_1 items;
    @ViewInject(R.id.knowledge_items)
    private PullableListView items;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refresh_view;


    private ArrayList<RadioButton> radioButtons;


    private KnowledgeBean knowledgeBean;
    private KnowledgeBean.KnowledgeDataDao knowledgeDataDao;
    private KnowledgeBean.GroupDataDao groupDataDao;
    private KnowledgeBean.GroupBean groupBean;
    private KnowledgeBean.ItemsBean itemsBean;
    private KnowledgeBean.ItemsDataDao itemsDataDao;

    private HashMap<String, ArrayList<KnowledgeBean.GroupDataDao>> groupsMap;
    private ArrayList<KnowledgeBean.GroupDataDao> groupDataList;
    private ArrayList<String> groupNameList;
    private ArrayList<KnowledgeBean.ItemsDataDao> itemsDataList;
    private ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList;
    private ArrayList<KnowledgeBean.ItemsDataDao> searchItemsDataList;

    private KnowledgeGroupAdapter groupAdapter;
    private KnowledgeItemsAdapter itemsAdapter;

    private String searchStr = "";

    private boolean isFirst = true;
    private String mSearchUrl = "";


    @Override
    public void onStart() {
        super.onStart();
        //search_edit.setText("");
    }

    @Override
    public void initData() {
        showProgressDialog();
        isFirst = true;


        //初始化组信息
        if (groupDataList == null) {
            groupDataList = new ArrayList<>();
        }
        groupDataList.clear();

        group.setVisibility(View.GONE);


//        if (groupAdapter == null) {
//            groupAdapter = new KnowledgeGroupAdapter(mActivity, groupDataList);
//        }
//        group.setAdapter(groupAdapter);
//
//        group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showProgressDialog();
//                getItemsData(groupDataList.get(position).url);
//            }
//        });


        //初始化条目信息

        if (cacheItemsDataList == null) {
            cacheItemsDataList = new ArrayList<>();
        }
        cacheItemsDataList.clear();

        if (itemsDataList == null) {
            itemsDataList = new ArrayList<>();
        }
        itemsDataList.clear();

        if (itemsAdapter == null) {
            itemsAdapter = new KnowledgeItemsAdapter(mActivity, cacheItemsDataList);
        }
        items.setAdapter(itemsAdapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name", "knowledge_item");
                startActivity(intent);
            }
        });
//        items.setOnRefreshListener(new RefreshListView_1.OnRefreshListener() {
//            @Override
//            public void pullDownRefresh() {
//                itemsDataList.clear();
//                cacheItemsDataList.clear();
//                getItemsData(mSearchUrl);
//                items.onRefreshFinish();
//            }
//
//            @Override
//            public void pullUpLoadMore() {
//                getItemsData(mSearchUrl);
//                items.onRefreshFinish();
//            }
//        });


        //初始化搜索条目信息
        if (searchItemsDataList == null) {
            searchItemsDataList = new ArrayList<>();
        }
        searchItemsDataList.clear();

//        search_edit.setOnKeyListener(new View.OnKeyListener()
//
//        {//EditText软键盘的回车事件
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
//                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
//                    //处理事件
//
//                    String search = "";
//                    search = search_edit.getText().toString();
//                    //MyToast.showShort(mActivity, "点击回车" + search_edit.getText().toString());
//                    cacheItemsDataList.clear();
//                    for (KnowledgeBean.ItemsDataDao dao : itemsDataList) {
//
//                        if (dao.title.contains(search)) {
//                            cacheItemsDataList.add(dao);
//                        } else if (dao.category.contains(search)) {
//                            cacheItemsDataList.add(dao);
//                        }
//                    }
//                    if (!(cacheItemsDataList.size() > 0)) {
////                        hint.setVisibility(View.VISIBLE);
////                        hint.setText("未搜索到相关条目\n清除搜索框显示全部条目");
//                    }
//                    itemsAdapter.notifyDataSetChanged();
//
//
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        search_edit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!isFirst) {
//                    if (s.toString().equals("")) {
//                        //hint.setVisibility(View.GONE);
//                        cacheItemsDataList.clear();
//                        cacheItemsDataList.addAll(itemsDataList);
//                        itemsAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });


        /**
         * 刷新///加载     的操作
         */
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                    }
                }.sendEmptyMessageDelayed(0, 500);
                itemsDataList.clear();
                cacheItemsDataList.clear();
                LogUtil.e("setOnRefreshListener-----------");
                getItemsData(mSearchUrl);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 500);
                LogUtil.e("onLoadMore-----------");
                getItemsData(mSearchUrl);
            }

        });

        getData();

    }


    /**
     * 获取分组数据
     */
    public void getData() {

        RequestParams params = new RequestParams(TestAddressUtils.test_get_knowledge_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                knowledgeBean = GsonUtil.getKnowledgeBean(result);
                if (knowledgeBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    groupDataList.addAll(knowledgeBean.data.group);
                    knowledgeBean = null;
                    setGroupData();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到的条目--------失败!!!---" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void setGroupData() {
//        groupAdapter.notifyDataSetChanged();
//        Utility.setListViewHeightBasedOnChildren(group);
        //dismissProgressDialog();
        if (radioButtons == null) {
            radioButtons = new ArrayList<>();
        }
        radioButtons.clear();
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 5;
        params.rightMargin = 5;
        //对radio添加数据
        for (int i = 0; i < groupDataList.size(); i++) {

            RadioButton tempButton = new RadioButton(mActivity);
            tempButton.setBackgroundResource(R.drawable.function_sign_history_selector);   // 设置RadioButton的背景图片--这里为选择器
            tempButton.setButtonDrawable(android.R.color.transparent);// 设置按钮的样式//隐藏单选圆形按钮
            tempButton.setPadding(5, 2, 5, 2);                 // 设置文字距离按钮四周的距离
            tempButton.setText(groupDataList.get(i).name);
            tempButton.setId(i);
            radioButtons.add(tempButton);
            rg_group.addView(tempButton, params);
        }


        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                LogUtil.e("获取到的id" + checkedId);
                showProgressDialog();
                //RadioButton tempButton = (RadioButton) group.getChildAt(checkedId);
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (i == checkedId) {
                        radioButtons.get(i).setTextColor(Color.rgb(0, 93, 209));
                    } else {
                        radioButtons.get(i).setTextColor(Color.rgb(0, 0, 0));
                    }
                }
                itemsDataList.clear();
                cacheItemsDataList.clear();
                mSearchUrl = groupDataList.get(checkedId).url;
                LogUtil.e("setOnCheckedChangeListener-----------");
                getItemsData(mSearchUrl);

            }
        });
        //rg_group.check(0);//如果用这个方法,那么会被调用多次
        ((RadioButton)rg_group.getChildAt(0)).setChecked(true);
//        if (isFirst) {
//            showProgressDialog();
//            LogUtil.e("第一次获取数据");
//            getItemsData(groupDataList.get(0).url);
//            isFirst = false;
//        }
    }


    /**
     * 获取条目数据
     */
    public void getItemsData(String url) {

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                itemsBean = GsonUtil.getKnowledgeItemsBean(result);
                if (itemsBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);

                    itemsDataList.addAll(itemsBean.items);
                    cacheItemsDataList.addAll(itemsBean.items);
                    itemsBean = null;
                    setItemsData();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到的条目--------失败!!!---" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void setItemsData() {

        LogUtil.e("获取条目数" + itemsDataList.size());
        LogUtil.e("获取条目数---高度为" + Utility.getListViewHeightBasedOnChildren(items));
        if (cacheItemsDataList.size() > 0) {
//            hint.setVisibility(View.GONE);
        } else {
//            hint.setVisibility(View.VISIBLE);
//            hint.setText("没有数据,点击刷新");
        }
        itemsAdapter.notifyDataSetChanged();

        //Utility.setListViewHeightBasedOnChildren(items);
        dismissProgressDialog();

    }

    @Event(value = {R.id.knowledge_search_popup, R.id.knowledge_add})
    private void onKnowledgeClick(View view) {
        switch (view.getId()) {
            case R.id.knowledge_search_popup:
                //MyToast.showShort(mActivity,"点击了头像@@@");
                //cacheItemsDataList.clear();
//                KnowledgeSearchPopupWindow popupWindow = new KnowledgeSearchPopupWindow(mActivity
//                        , view
//                        , itemsAdapter
//                        , cacheItemsDataList
//                        , itemsDataList
//                        , hint);
//                popupWindow.setmSearchPopupWindow();
                LogUtil.e("搜索qian的条目数==" + cacheItemsDataList.size());
                KnowledgeSearchPopupWindow_1 popupWindow_1 = new KnowledgeSearchPopupWindow_1(mActivity
                        , itemsDataList
                        , searchStr
                        , view);
                popupWindow_1.setOnData(new KnowledgeSearchPopupWindow_1.OnGetData() {


                    @Override
                    public void onDataCallBack(String search, ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList) {
                        searchStr = search;
                        KnowledgeZDLFFragment.this.cacheItemsDataList.clear();
                        KnowledgeZDLFFragment.this.cacheItemsDataList.addAll(cacheItemsDataList);
                        if (KnowledgeZDLFFragment.this.cacheItemsDataList.size() > 0) {
                            KnowledgeZDLFFragment.this.itemsAdapter.notifyDataSetChanged();
                        } else {
//                            hint.setText("未搜索到相关条目\n下拉刷新获取信息");
                        }
                    }
                });

                break;

            case R.id.knowledge_add:
                MyToast.showShort(mActivity, "走,去发帖!");
                Intent intent = new Intent(mActivity, NewProposalActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e("暂停了");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("销毁了");
    }


}

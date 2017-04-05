package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.adapter.zdlf.AddressListTreeAdapter;
import cn.net.bjsoft.sxdz.bean.ylyd.form.YLYDFormDao;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListBean;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListFileBean;
import cn.net.bjsoft.sxdz.dialog.DialingPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.treeview.helper.Node;
import cn.net.bjsoft.sxdz.view.treeview.helper.TreeListViewAdapter;

/**
 * Created by Zrzc on 2017/3/24.
 */
@ContentView(R.layout.fragment_address_list)
public class MineAddressListFragment extends BaseFragment {
    @ViewInject(R.id.title_back)
    private ImageView title_back;
    @ViewInject(R.id.title_title)
    private TextView title;

    @ViewInject(R.id.search_edittext)
    private EditText search_edittext;

    @ViewInject(R.id.address_parent)
    private TextView address_parent;
    @ViewInject(R.id.address_filiale)
    private TextView address_filiale;
    @ViewInject(R.id.address_list)
    private ListView treeView;
    @ViewInject(R.id.address_change)
    private LinearLayout address_change;


    private AddressListBean formBean;

    private YLYDFormDao.TreeListBean treeListBean;
    private ArrayList<AddressListBean.AddressListDao> tree_list;
    private ArrayList<String> childAvatarList;
    private ArrayList<String> childNameList;
    private ArrayList<String> childNumList;

    private AddressListFileBean bean;
    private List<AddressListFileBean> mDatas;
    private ListView mTree;
    private AddressListTreeAdapter mAdapter;

    private int level = 1;


    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("通讯录");
        //setTreeView();

        if (childAvatarList==null) {
            childAvatarList = new ArrayList<>();
        }
        childAvatarList.clear();

        if (childNameList==null) {
            childNameList = new ArrayList<>();
        }
        childNameList.clear();

        if (childNumList==null) {
            childNumList = new ArrayList<>();
        }
        childNumList.clear();

        addressChange(address_parent);
    }


    /**
     * 功能按钮
     *
     * @param view
     */
    @Event(value = {R.id.title_back
            , R.id.search_text
            , R.id.search_delete})
    private void addressOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;

            case R.id.search_text://搜索按钮
                String searchStr = search_edittext.getText().toString().trim();
                if (!searchStr.equals("")) {
                    Intent searchIntent = new Intent(mActivity, EmptyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("address_list_avatar",childAvatarList);
                    bundle.putStringArrayList("address_list_name",childNameList);
                    bundle.putStringArrayList("address_list_num",childNumList);
                    bundle.putString("address_list_search_str",searchStr);
                    searchIntent.putExtra("address_list_search_result_bundle",bundle);
                    searchIntent.putExtra("fragment_name","mine_zdlf_address_search");
                    mActivity.startActivity(searchIntent);
                } else {
                    MyToast.showShort(mActivity, "请输入搜索内容!");
                    return;
                }
                break;

            case R.id.search_delete://清空按钮
                MyToast.showShort(mActivity, "清空输入框");
                search_edittext.setText("");
                break;

        }
    }


    /**
     * 切换
     *
     * @param view
     */
    @Event(value = {R.id.address_parent
            , R.id.address_filiale})
    private void addressChange(View view) {

        switch (view.getId()) {
            case R.id.address_parent://
                showProgressDialog();

                //MyToast.showShort(mActivity, "总公司");
                address_parent.setBackgroundResource(R.drawable.approve_left_shixin);
                address_parent.setTextColor(Color.parseColor("#FFFFFF"));
                address_filiale.setBackgroundResource(R.drawable.approve_right_kongxin);
                address_filiale.setTextColor(Color.parseColor("#000000"));

                initList();
                getFormData();

                break;

            case R.id.address_filiale:
                showProgressDialog();
                MyToast.showShort(mActivity, "分公司");
                address_parent.setBackgroundResource(R.drawable.approve_left_kongxin);
                address_parent.setTextColor(Color.parseColor("#000000"));
                address_filiale.setBackgroundResource(R.drawable.approve_right_shixin);
                address_filiale.setTextColor(Color.parseColor("#FFFFFF"));

                initList();
                getFormData();

                break;
        }

    }

    /**
     * 从服务端获取数据
     */
    private void getFormData() {


        RequestParams params = new RequestParams(TestAddressUtils.test_get_address_list_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                formBean = GsonUtil.getAddressListBean(result);
                if (formBean.result) {
                    LogUtil.e("获取到报表数据-----------" + result);
                    //scroll_list.clear();

                    tree_list.addAll(formBean.data.address_list);
                    //LogUtil.e("获取到报表数据-----------" + result);
                    getItems(tree_list, 1);

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

    //初始化树形结构---------------------------开始----------------------------

    private void initList() {
        if (tree_list == null) {
            tree_list = new ArrayList<>();
        }
        tree_list.clear();
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
    }

    private void setTreeView() {

        //getItems(tree_list, 0);
        try {
            mAdapter = new AddressListTreeAdapter<AddressListFileBean>(treeView, mActivity, mDatas, 1);
            treeView.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    Log.e("点击的条目", "tiaomu wei ====" + position);
                    String url = mDatas.get(position).getUrl();

                    if (node.getType().equals("employee")) {
                        DialingPopupWindow window = new DialingPopupWindow(mActivity, address_change, node.getPhone_number());
                    }

                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        dismissProgressDialog();
    }

    //将网络数据添加到本地的数据格式中去
    public void getItems(ArrayList<AddressListBean.AddressListDao> list, int level) {
        level++;

        for (AddressListBean.AddressListDao children : list) {
            bean = null;
            if (children.children != null) {
                bean = new AddressListFileBean(Integer.parseInt(children.id)
                        , Integer.parseInt(children.pid)
                        , children.name
                        , children.station
                        , children.phone_number
                        , children.type
                        , children.avatar_url);
                if (!TextUtils.isEmpty(children.type)) {
                    bean.setUrl(children.type);
                }
                mDatas.add(bean);

                getItems(children.children, level);
            } else {
                if (!children.name.equals("")) {
                    //mDatas.add(new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name));
                    bean = new AddressListFileBean(Integer.parseInt(children.id)
                            , Integer.parseInt(children.pid)
                            , children.name
                            , children.station
                            , children.phone_number
                            , children.type
                            , children.avatar_url);
                    if (!TextUtils.isEmpty(children.url)) {
                        bean.setUrl(children.url);
                    }
                    //搜索相关
                    childAvatarList.add(children.avatar_url);
                    childNameList.add(children.name);
                    childNumList.add(children.phone_number);

                    LogUtil.e("size"+childAvatarList.size()+"::"+childAvatarList.size()+"::"+childAvatarList.size()+"::");

                    mDatas.add(bean);
                }
            }

        }
        setTreeView();
    }
    //初始化树形结构---------------------------结束----------------------------
}

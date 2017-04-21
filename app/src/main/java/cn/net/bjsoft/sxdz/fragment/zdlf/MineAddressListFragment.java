package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressCompanysBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
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

    //    @ViewInject(R.id.search_edittext)
//    private EditText search_edittext;
    @ViewInject(R.id.address_search)
    private ImageView address_search;


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

    private ArrayList<AddressPositionsBean> addressPositionsBeenList;

    private int level = 1;

    private String organization_url = "";


    private AddressBean addressBean;
    private AddressCompanysBean companysBean;
    private ArrayList<AddressDeptsBean> deptsBean;
    private ArrayList<AddressDeptsBean> format_deptsBean;
    private ArrayList<AddressEmployeesBean> employeesBean;
    private ArrayList<AddressPositionsBean> positionsBean;
    private ArrayList<AddressPositionsBean> format_positionsBean;


    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("通讯录");
        //setTreeView();

        organization_url = getArguments().getBundle("organization_url").getString("organization_url");

        LogUtil.e("通讯录地址=====" + organization_url);


        initList();

        addressChange(address_parent);
    }

    private void initList() {


        if (deptsBean == null) {
            deptsBean = new ArrayList<>();
        }
        deptsBean.clear();

        if (employeesBean == null) {
            employeesBean = new ArrayList<>();
        }
        employeesBean.clear();

        if (positionsBean == null) {
            positionsBean = new ArrayList<>();
        }
        positionsBean.clear();


        if (format_deptsBean == null) {
            format_deptsBean = new ArrayList<>();
        }
        format_deptsBean.clear();

        if (format_positionsBean == null) {
            format_positionsBean = new ArrayList<>();
        }
        format_positionsBean.clear();


        //------------------------
        if (childAvatarList == null) {
            childAvatarList = new ArrayList<>();
        }
        childAvatarList.clear();

        if (childNameList == null) {
            childNameList = new ArrayList<>();
        }
        childNameList.clear();

        if (childNumList == null) {
            childNumList = new ArrayList<>();
        }
        childNumList.clear();

        //------------------------
        if (tree_list == null) {
            tree_list = new ArrayList<>();
        }
        tree_list.clear();
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
    }


    /**
     * 功能按钮
     *
     * @param view
     */
    @Event(value = {R.id.title_back
            , R.id.address_search
            /*, R.id.search_text
            , R.id.search_delete*/})
    private void addressOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;

            case R.id.address_search://返回
                searchAddressList();
                break;

//            case R.id.search_text://搜索按钮
//                searchAddressList();
//                break;

//            case R.id.search_delete://清空按钮
//                MyToast.showShort(mActivity, "清空输入框");
//                search_edittext.setText("");
//                break;

        }
    }

    private void searchAddressList() {
        //String searchStr = search_edittext.getText().toString().trim();
        //if (!searchStr.equals("")) {
        Intent searchIntent = new Intent(mActivity, EmptyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("address_list_avatar", childAvatarList);
        bundle.putStringArrayList("address_list_name", childNameList);
        bundle.putStringArrayList("address_list_num", childNumList);
//            bundle.putString("address_list_search_str",searchStr);
        bundle.putString("address_list_search_str", "");
        searchIntent.putExtra("address_list_search_result_bundle", bundle);
        searchIntent.putExtra("fragment_name", "mine_zdlf_address_search");
        mActivity.startActivity(searchIntent);
//    } else
//
//    {
//        MyToast.showShort(mActivity, "请输入搜索内容!");
//        return;
//    }

    }

    /**
     * 切换
     *
     * @param view
     */
    @Event(value = {R.id.address_parent
            , R.id.address_filiale})
    private void addressChange(View view) {


        tree_list.clear();
        mDatas.clear();
        //搜索相关
        childAvatarList.clear();
        childNameList.clear();
        childNumList.clear();


        switch (view.getId()) {
            case R.id.address_parent://


                //MyToast.showShort(mActivity, "总公司");
                address_parent.setBackgroundResource(R.drawable.approve_left_shixin);
                address_parent.setTextColor(Color.parseColor("#FFFFFF"));
                address_filiale.setBackgroundResource(R.drawable.approve_right_kongxin);
                address_filiale.setTextColor(Color.parseColor("#000000"));

                //getFormData();
                getOrganizationData();
                break;

            case R.id.address_filiale:

                MyToast.showShort(mActivity, "分公司");
                address_parent.setBackgroundResource(R.drawable.approve_left_kongxin);
                address_parent.setTextColor(Color.parseColor("#000000"));
                address_filiale.setBackgroundResource(R.drawable.approve_right_shixin);
                address_filiale.setTextColor(Color.parseColor("#FFFFFF"));

                //getFormData();

                getOrganizationData();
                break;
        }

    }

    private void getOrganizationData() {
        showProgressDialog();

        RequestParams params = new RequestParams("http://api.shuxinyun.com/cache/users/10001/organization.json");
//        RequestParams params = new RequestParams(organization_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到联系人数据-----------" + result);
                if (addressBean != null) {
                    addressBean = null;
                }
                deptsBean.clear();
                employeesBean.clear();
                positionsBean.clear();
                mDatas.clear();

                addressBean = GsonUtil.getAddressBean(result);

                companysBean = addressBean.companys;
                deptsBean.addAll(addressBean.depts);
                employeesBean.addAll(addressBean.employees);
                positionsBean.add(addressBean.positions);

                //


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("===========开始递归22222222222==========" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("===========开始递归1==========" + (companysBean == null)
                        + ":deptsBean:" + deptsBean.size()
                        + ":employeesBean:" + employeesBean.size()
                        + ":positionsBean:" + positionsBean.size());

                getPositions(positionsBean, "0");
//                showMDatas();
                mDatas.clear();
//                for (AddressPositionsBean bean : format_positionsBean) {
//                    LogUtil.e("岗位的全部信息++++++++++++" + bean.dept_id +"employee"+ bean.employee);
//                }
                getDepts(deptsBean, "0");
                setTreeView();
                dismissProgressDialog();
            }
        });

    }

    private void showMDatas() {
    }

    /**
     * 部门树形结构格式化
     *
     * @param deptsBean
     * @param pid
     */
    private void getDepts(ArrayList<AddressDeptsBean> deptsBean, String pid) {


        for (AddressDeptsBean addressDeptsBean : deptsBean) {
            addressDeptsBean.pId = pid;
            bean = null;
            //LogUtil.e("遍历子节点------addressDeptsBean.id" + addressDeptsBean.id);
            for (AddressPositionsBean addressPositionsBean : format_positionsBean) {

                if (addressPositionsBean.dept_id.equals(addressDeptsBean.id)) {

                    if (addressDeptsBean.children == null) {
                        addressDeptsBean.children = new ArrayList<AddressDeptsBean>();
                    } else {

                        //过滤重复的联系人
                        if (addressPositionsBean.employee != null) {

//                            for (int i = 0; i < addressDeptsBean.children.size(); i++) {
//                                LogUtil.e("添加子节点=======addressDeptsBean.id=====" + addressDeptsBean.id+"----i"+i);
//                                if (!(addressDeptsBean.children.get(i).id.equals(addressPositionsBean.employee.source_id))) {
                            AddressDeptsBean bean = new AddressDeptsBean();
                            bean.id = addressPositionsBean.id;
                            LogUtil.e("添加子节点=======bean.id=====" + bean.id);
                            bean.positionsBean = addressPositionsBean;
                            bean.name = addressPositionsBean.employee.name;
                            bean.employee_id = addressPositionsBean.employee_id;
                            addressDeptsBean.children.add(bean);
//                                }
//
//                            }

                        }

                    }

                }
                LogUtil.e("添加子节点=======完毕-----------------------");

            }

            if (addressDeptsBean.children != null && addressDeptsBean.children.size() > 0) {

                getDepts(addressDeptsBean.children, addressDeptsBean.id);
            } else {

            }
            bean = new AddressListFileBean(Integer.parseInt(addressDeptsBean.id)
                    , Integer.parseInt(addressDeptsBean.pId)
                    , addressDeptsBean.name
                    , ""
                    , ""
                    , /*children.type*//*"employee"*/"department"
                    , "");
            mDatas.add(bean);


        }

    }

    /**
     * 组织架构树形----格式化
     *
     * @param childrens
     * @param pId
     */
    private void getPositions(ArrayList<AddressPositionsBean> childrens, String pId) {

        for (AddressPositionsBean children : childrens) {
            children.pId = pId;
            bean = null;
            //添加职工信息
            for (AddressEmployeesBean employee : employeesBean) {
                if (children.employee_id.equals(employee.id)) {
                    children.employee = employee;
                }
            }
            //如果有就添加,没有就 不添加

            if (children.children != null && children.children.size() > 0) {

                getPositions(children.children, children.id);

            }
            format_positionsBean.add(children);
            bean = new AddressListFileBean(Integer.parseInt(children.id)
                    , Integer.parseInt(children.pId)
                    , children.name
                    , ""
                    , ""
                    , /*children.type*/"employee"
                    , "");
            mDatas.add(bean);
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

                    LogUtil.e("size" + childAvatarList.size() + "::" + childAvatarList.size() + "::" + childAvatarList.size() + "::");

                    mDatas.add(bean);
                }
            }

        }
        setTreeView();
    }
    //初始化树形结构---------------------------结束----------------------------
}

package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressCompanysBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesUserBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListBean;
import cn.net.bjsoft.sxdz.dialog.DialingPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AssetsUtil;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean.FileTreeBean;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeAddressZDLFAdapter;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeNode;

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


    private ArrayList<AddressListBean.AddressListDao> tree_list;
    private ArrayList<String> childAvatarList;
    private ArrayList<String> childNameList;
    private ArrayList<String> childNumList;

    private FileTreeBean treeBean;
    private List<FileTreeBean> fileBeanDatas;
    private TreeAddressZDLFAdapter mAdapter;

    private String organization_url = "";


    private AddressBean addressBean;
    private AddressCompanysBean companysBean;
    private ArrayList<AddressDeptsBean> deptsBean;
    private ArrayList<AddressDeptsBean> format_deptsBean;//格式化岗位集合
    private ArrayList<AddressEmployeesBean> employeesBean;
    private ArrayList<AddressPositionsBean> positionsBean;
    private ArrayList<AddressPositionsBean> format_positionsBean;

    private HashMap<String, AddressDeptsBean> map;


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


        if (fileBeanDatas == null) {
            fileBeanDatas = new ArrayList<>();
        }
        fileBeanDatas.clear();


        if (tree_list == null) {
            tree_list = new ArrayList<>();
        }
        tree_list.clear();
//        if (mDatas == null) {
//            mDatas = new ArrayList<>();
//        }
//        mDatas.clear();
    }


    /**
     * 功能按钮
     *
     * @param view
     */
    @Event(value = {R.id.title_back
            , R.id.address_search
            , R.id.search_text
            /*, R.id.search_delete*/})
    private void addressOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;

            case R.id.address_search://返回
                searchAddressList();
                break;
//
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

        childAvatarList.clear();
        childNameList.clear();
        childNumList.clear();
        LogUtil.e("获取到联系人数据-----------" + employeesBean.size());
        for (AddressEmployeesBean bean : employeesBean) {//以防后台给的数据是残疾的
            if (bean.user != null) {
                childAvatarList.add(bean.user.avatar);
            }else {
                AddressEmployeesUserBean userBean = new AddressEmployeesUserBean();
                childAvatarList.add(userBean.avatar);
            }
            childNameList.add(bean.name);
            childNumList.add(bean.phone);
        }


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
//        mDatas.clear();
        fileBeanDatas.clear();
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
                //showMDatas();
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

       // RequestParams params = new RequestParams("http://api.shuxinyun.com/cache/users/10001/organization.json");
        RequestParams params = new RequestParams(organization_url);
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
                fileBeanDatas.clear();

                addressBean = GsonUtil.getAddressBean(result);

                companysBean = addressBean.companys;
                deptsBean.addAll(addressBean.depts);
                employeesBean.addAll(addressBean.employees);
                positionsBean.add(addressBean.positions);

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

                getPositions(positionsBean, "0");
                fileBeanDatas.clear();
                getDepts(deptsBean, "0");
                setTreeView();
                dismissProgressDialog();
            }
        });

    }


    /**
     * 测试,运用本地数据
     */
    private void showMDatas() {
        String result = "";

        result = AssetsUtil.getStringFromAssets(mActivity, "positions.json");

        addressBean = GsonUtil.getAddressBean(result);

        companysBean = addressBean.companys;
        deptsBean.addAll(addressBean.depts);
        employeesBean.addAll(addressBean.employees);
        positionsBean.add(addressBean.positions);


        getPositions(positionsBean, "0");
        fileBeanDatas.clear();
        getDepts(deptsBean, "0");
        setTreeView();
        dismissProgressDialog();

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
            treeBean = null;
            //LogUtil.e("遍历子节点------addressDeptsBean.id" + addressDeptsBean.id);
            //向部门添加联系人
            map = new HashMap<>();
            for (AddressPositionsBean addressPositionsBean : format_positionsBean) {

                if (addressPositionsBean.dept_id.equals(addressDeptsBean.id)) {

                    if (addressDeptsBean.children == null) {
                        addressDeptsBean.children = new ArrayList<AddressDeptsBean>();
                    } else {
                        //过滤重复的联系人
                        if (addressPositionsBean.employee != null) {
                            AddressDeptsBean bean = new AddressDeptsBean();
                            bean.id = addressPositionsBean.employee.id;
                            LogUtil.e("添加子节点=======bean.id=====" + bean.id);
                            bean.positionsBean = addressPositionsBean;
                            bean.name = addressPositionsBean.employee.name;
                            bean.employee_id = addressPositionsBean.employee_id;
                            map.put(bean.id, bean);//用map是为了去除重复元素
                        }
                    }
                }
            }
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                AddressDeptsBean bean = (AddressDeptsBean) entry.getValue();
                addressDeptsBean.children.add(bean);
            }

            map = null;


            if (addressDeptsBean.children != null && addressDeptsBean.children.size() > 0) {

                getDepts(addressDeptsBean.children, addressDeptsBean.id);
            } else {

            }
            treeBean = new FileTreeBean(Long.parseLong(addressDeptsBean.id)
                    , Long.parseLong(addressDeptsBean.pId)
                    , addressDeptsBean.name
                    , addressDeptsBean);
            fileBeanDatas.add(treeBean);

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
            treeBean = null;
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
        }
    }


    //初始化树形结构---------------------------开始----------------------------


    private void setTreeView() {

        try {
            mAdapter = new TreeAddressZDLFAdapter<FileTreeBean>(treeView, mActivity, fileBeanDatas, 1);
            treeView.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, int position) {
                    if (!node.getAddressDeptsBean().employee_id.equals("")) {
                        DialingPopupWindow window = new DialingPopupWindow(mActivity, address_change, node.getAddressDeptsBean().positionsBean.employee.phone);
                    }
                }
            });

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        dismissProgressDialog();
    }

    //初始化树形结构---------------------------结束----------------------------
}

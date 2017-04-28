package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressCompanysBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesUserBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
import cn.net.bjsoft.sxdz.dialog.DialingPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
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


    private ArrayList<String> childAvatarList;
    private ArrayList<String> childNameList;
    private ArrayList<String> childNumList;

    private FileTreeBean treeBean;
    private List<FileTreeBean> fileBeanDatas;
    private List<FileTreeBean> fileBeanDatas_head;
    private List<FileTreeBean> fileBeanDatas_branch;

    private TreeAddressZDLFAdapter mAdapter;

    private String organization_url = "";


    private AddressBean addressBean;
    private AddressCompanysBean companysBean;
    private ArrayList<AddressCompanysBean> list_companysBean;
    private ArrayList<AddressCompanysBean> format_companysBean;
    private ArrayList<AddressCompanysBean> format_companysBean_list;

    private ArrayList<AddressDeptsBean> list_deptsBean;
    private ArrayList<AddressDeptsBean> format_deptsBean;//格式化岗位集合
    private ArrayList<AddressDeptsBean> list_head_deptsBean;//总公司
    private ArrayList<AddressDeptsBean> list_branch_deptsBean;//分公司
    private ArrayList<AddressDeptsBean> list_change_deptsBean;


    private ArrayList<AddressEmployeesBean> list_employeesBean;
    private ArrayList<AddressPositionsBean> list_positionsBean;
    private ArrayList<AddressPositionsBean> format_positionsBean;//格式化职位

    private HashMap<String, AddressDeptsBean> map;//过滤重复联系人的中间集合


    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("通讯录");
        //setTreeView();
        //organization_url = getArguments().getBundle("organization_url").getString("organization_url");
        initList();
        getOrganizationData();
        addressChange(address_parent);
    }

    private void initList() {


        if (list_head_deptsBean == null) {
            list_head_deptsBean = new ArrayList<>();
        }
        list_head_deptsBean.clear();

        if (list_branch_deptsBean == null) {
            list_branch_deptsBean = new ArrayList<>();
        }
        list_branch_deptsBean.clear();


        if (list_change_deptsBean == null) {
            list_change_deptsBean = new ArrayList<>();
        }
        list_change_deptsBean.clear();


        if (list_companysBean == null) {
            list_companysBean = new ArrayList<>();
        }
        list_companysBean.clear();

        if (list_deptsBean == null) {
            list_deptsBean = new ArrayList<>();
        }
        list_deptsBean.clear();

        if (list_employeesBean == null) {
            list_employeesBean = new ArrayList<>();
        }
        list_employeesBean.clear();

        if (list_positionsBean == null) {
            list_positionsBean = new ArrayList<>();
        }
        list_positionsBean.clear();

        if (format_companysBean == null) {
            format_companysBean = new ArrayList<>();
        }
        format_companysBean.clear();

        if (format_companysBean_list == null) {
            format_companysBean_list = new ArrayList<>();
        }
        format_companysBean_list.clear();


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

        if (fileBeanDatas_head == null) {
            fileBeanDatas_head = new ArrayList<>();
        }
        fileBeanDatas_head.clear();

        if (fileBeanDatas_branch == null) {
            fileBeanDatas_branch = new ArrayList<>();
        }
        fileBeanDatas_branch.clear();

        if (fileBeanDatas == null) {
            fileBeanDatas = new ArrayList<>();
        }
        fileBeanDatas.clear();


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
//        LogUtil.e("获取到联系人数据-----------" + list_employeesBean.size());
        for (AddressEmployeesBean bean : list_employeesBean) {//以防后台给的数据是残疾的
            if (bean.user != null) {
                childAvatarList.add(bean.user.avatar);
            } else {
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
        fileBeanDatas.clear();
        //搜索相关
        childAvatarList.clear();
        childNameList.clear();
        childNumList.clear();


        switch (view.getId()) {
            case R.id.address_parent://
                address_parent.setBackgroundResource(R.drawable.approve_left_shixin);
                address_parent.setTextColor(Color.parseColor("#FFFFFF"));
                address_filiale.setBackgroundResource(R.drawable.approve_right_kongxin);
                address_filiale.setTextColor(Color.parseColor("#000000"));
                //getFormData();
                //getOrganizationData();
                //showMDatas();
                changeTreeData(1);
                break;

            case R.id.address_filiale:
                address_parent.setBackgroundResource(R.drawable.approve_left_kongxin);
                address_parent.setTextColor(Color.parseColor("#000000"));
                address_filiale.setBackgroundResource(R.drawable.approve_right_shixin);
                address_filiale.setTextColor(Color.parseColor("#FFFFFF"));
                changeTreeData(2);
                //getFormData();
                //getOrganizationData();

                break;
        }

    }

    /**
     * 切换岗位切换数据
     *
     * @param change
     */
    private void changeTreeData(int change) {
        fileBeanDatas.clear();
        switch (change) {
            case 1:
                fileBeanDatas.addAll(fileBeanDatas_head);
                break;

            case 2:
                fileBeanDatas.addAll(fileBeanDatas_branch);
                break;
        }

        setTreeView();
    }


    private void getOrganizationData() {

        showProgressDialog();
        if (addressBean != null) {
            addressBean = null;
        }

        list_companysBean.clear();
        list_deptsBean.clear();
        list_employeesBean.clear();
        list_positionsBean.clear();

        fileBeanDatas.clear();

        addressBean = GsonUtil.getAddressBean(SPUtil.getUserOrganizationJson(mActivity));

        LogUtil.e("SPUtil.getUserOrganizationJson(mActivity)" + SPUtil.getUserOrganizationJson(mActivity));

        if (addressBean == null) {//如果为空,就return
            MyToast.showShort(mActivity, "获取通讯录信息失败!");
            return;
        }

        list_companysBean.add(addressBean.companys);
        list_deptsBean.addAll(addressBean.depts);
        list_employeesBean.addAll(addressBean.employees);
        list_positionsBean.add(addressBean.positions);


        format_positionsBean.clear();
        getPositions(list_positionsBean, "0");
        if (Long.parseLong(list_deptsBean.get(0).id) > 0) {//至少一个以上才能成为树
            getDepts(list_deptsBean, list_deptsBean.get(0).id);//总部
        }
        getFormatCompany(list_companysBean, "0");

        for (AddressCompanysBean bean : format_companysBean_list) {
            LogUtil.e(bean.name + "::" + bean.id);
            bean.conpany_id = bean.id;
            //bean.pId = "0";
        }


        getCompanys(list_companysBean, "0");

        //setTreeDatas(format_companysBean);
        LogUtil.e("====================");
        ArrayList<AddressCompanysBean> head = new ArrayList<>();
        head.add(format_companysBean_list.get(0));
        setTreeHeadDatas(head, format_companysBean);


        ArrayList<AddressCompanysBean> branch = new ArrayList<>();
        for (int i = 1; i < format_companysBean_list.size(); i++) {
            branch.add(format_companysBean_list.get(i));
        }
        setTreeBranchDatas(branch, format_companysBean);

        dismissProgressDialog();

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
            //添加职工信息
            for (AddressEmployeesBean employee : list_employeesBean) {
                if (children.employee_id.equals(employee.id)) {
                    children.id = employee.id;
                    children.employee = employee;
                }
            }
            //如果有就添加,没有就 不添加
            format_positionsBean.add(children);
            if (children.children != null && children.children.size() > 0) {
                getPositions(children.children, children.id);
            }

        }
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
            //LogUtil.e("遍历子节点------addressDeptsBean.id" + addressDeptsBean.id);
            //向部门添加联系人
//            map = new HashMap<>();
            for (AddressPositionsBean addressPositionsBean : format_positionsBean) {

                if (addressPositionsBean.dept_id.equals(addressDeptsBean.id)) {

                    if (addressDeptsBean.children == null) {
                        addressDeptsBean.children = new ArrayList<AddressDeptsBean>();
                    } else {
                        //过滤重复的联系人
                        if (addressPositionsBean.employee != null) {
                            AddressDeptsBean bean = new AddressDeptsBean();
                            bean.id = addressPositionsBean.id;
                            bean.positionsBean = addressPositionsBean;
                            bean.name = addressPositionsBean.employee.name;
                            bean.employee_id = addressPositionsBean.employee_id;
                            bean.company_id = addressDeptsBean.company_id;
                            LogUtil.e("添加子节点=======bean.id=====" + bean.name);


                            if (addressPositionsBean.type.equals("1")) {
                                addressDeptsBean.children.add(bean);
                            }

                        }
                    }
                }
            }

            list_change_deptsBean.add(addressDeptsBean);
            if (addressDeptsBean.children != null && addressDeptsBean.children.size() > 0) {
                getDepts(addressDeptsBean.children, addressDeptsBean.id);
            }


        }

    }


    /**
     * 格式化公司信息
     *
     * @param companys
     * @param id
     */
    private void getCompanys(ArrayList<AddressCompanysBean> companys, String id) {
        //String pid = id;
        LogUtil.e("过来的pd===" + id + "==================================");
        for (AddressCompanysBean company : companys) {
            company.pId = id;
            //company.conpany_id = company.id;
            for (AddressDeptsBean addressDeptsBean : list_change_deptsBean) {
                if (addressDeptsBean.company_id.equals(company.id)) {
                    if (company.children == null) {
                        company.children = new ArrayList<>();
                    } else {
                        AddressCompanysBean companysBean = new AddressCompanysBean();
                        companysBean.id = addressDeptsBean.id;
//                        LogUtil.e("子节点的id===" + addressDeptsBean.id);
//                        //companysBean.pId = addressDeptsBean.pId;
//                        LogUtil.e("子节点的pid===" + addressDeptsBean.pId);
//                        LogUtil.e("子节点的name===" + addressDeptsBean.name);

                        companysBean.name = addressDeptsBean.name;
                        companysBean.deptsBean = addressDeptsBean;
                        LogUtil.e("子节点的name===" + companysBean.deptsBean.name);
                        companysBean.conpany_id = addressDeptsBean.company_id;

                        company.children.add(companysBean);
                        LogUtil.e("=========一条==========" + addressDeptsBean.name);
                    }
                }
            }
            format_companysBean.add(company);
            if (company.children != null && company.children.size() > 0) {
                getCompanys(company.children, company.id);
            }

        }
    }

    private void getFormatCompany(ArrayList<AddressCompanysBean> companys, String id) {
        for (AddressCompanysBean company : companys) {
            company.pId = id;
            company.conpany_id = company.id;
            format_companysBean_list.add(company);
            if (company.children != null && company.children.size() > 0) {
                getFormatCompany(company.children, company.id);
            }
        }
    }

    //初始化树形结构---------------------------开始----------------------------

    private void setTreeHeadDatas(ArrayList<AddressCompanysBean> companys, ArrayList<AddressCompanysBean> children) {

        for (AddressCompanysBean bean : companys) {
            for (AddressCompanysBean children_bean : children) {
                //LogUtil.e("children_bean==" + children_bean.deptsBean.name + "::" + children_bean.deptsBean.id + "::" + children_bean.deptsBean.pId);
                if (bean.conpany_id.equals(children_bean.conpany_id)) {
                    if (children_bean.deptsBean != null) {
                        LogUtil.e("bean.deptsBean != null=="+bean.name);

                        FileTreeBean treeBean = new FileTreeBean(Long.parseLong(children_bean.deptsBean.id)
                                , Long.parseLong(children_bean.deptsBean.pId)
                                , children_bean.deptsBean.name
                                , children_bean);
                        fileBeanDatas_head.add(treeBean);
                    } else {
                        LogUtil.e("为空添加=="+children_bean.name);
                        FileTreeBean treeBean = new FileTreeBean(Long.parseLong(children_bean.id)
                                , Long.parseLong(children_bean.pId)
                                , children_bean.name
                                , children_bean);
                        fileBeanDatas_head.add(treeBean);
                    }
                }
            }
        }
    }


    private void setTreeBranchDatas(ArrayList<AddressCompanysBean> companys, ArrayList<AddressCompanysBean> children) {
        for (AddressCompanysBean bean : companys) {
            for (AddressCompanysBean children_bean : children) {
                if (bean.conpany_id.equals(children_bean.conpany_id)) {
                    if (children_bean.deptsBean != null) {
                        FileTreeBean treeBean = new FileTreeBean(Long.parseLong(children_bean.id)
                                , Long.parseLong(children_bean.deptsBean.pId)
                                , children_bean.deptsBean.name
                                , children_bean);
                        fileBeanDatas_branch.add(treeBean);
                    } else {
                        FileTreeBean treeBean = new FileTreeBean(Long.parseLong(children_bean.id)
                                , Long.parseLong(children_bean.pId)
                                , children_bean.name
                                , children_bean);
                        fileBeanDatas_branch.add(treeBean);
                    }
                }
            }
        }
    }


    private void setTreeDatas(ArrayList<AddressCompanysBean> companysBeen) {


        for (AddressCompanysBean bean : companysBeen) {

            if (bean.conpany_id.equals("1")) {
                LogUtil.e("总公司==" + bean.name + ":--conpany_id===:" + bean.conpany_id + ":---pid:==" + bean.pId + ":---id:==" + bean.id);
                if (bean.deptsBean != null) {
                    FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.deptsBean.id)
                            , Long.parseLong(bean.deptsBean.pId)
                            , bean.deptsBean.name
                            , bean);
                    fileBeanDatas_head.add(treeBean);
                } else {
                    FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.id)
                            , Long.parseLong(bean.pId)
                            , bean.name
                            , bean);
                    fileBeanDatas_head.add(treeBean);
                }
            } else {
                LogUtil.e("分公司==" + bean.name + ":--conpany_id===:" + bean.conpany_id + ":---pid:==" + bean.pId + ":---id:==" + bean.id);
                if (bean.deptsBean != null) {
                    FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.id)
                            , Long.parseLong(bean.deptsBean.pId)
                            , bean.deptsBean.name
                            , bean);
                    fileBeanDatas_branch.add(treeBean);
                } else {
                    FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.id)
                            , Long.parseLong(bean.pId)
                            , bean.name
                            , bean);
                    fileBeanDatas_branch.add(treeBean);
                }
            }

        }

    }


    private void setTreeView() {

        try {
            mAdapter = new TreeAddressZDLFAdapter<FileTreeBean>(treeView, mActivity, fileBeanDatas, 1);
            treeView.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, int position) {
                    if (node.getCompanysBean().deptsBean != null) {
                        if (!node.getCompanysBean().deptsBean.employee_id.equals("")) {
                            if (TextUtils.isEmpty(node.getCompanysBean().deptsBean.positionsBean.employee.phone)) {
                                MyToast.showShort(mActivity, "该联系人没有设置电话号码!");
                            } else {
                                DialingPopupWindow window = new DialingPopupWindow(mActivity, address_change, node.getCompanysBean().deptsBean.positionsBean.employee.phone);
                            }
                        }
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

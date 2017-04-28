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
    private ArrayList<AddressCompanysBean> list_companysBean_head;
    private ArrayList<AddressCompanysBean> list_companysBean_branch;
    private ArrayList<AddressCompanysBean> format_companysBean_list;//
    private ArrayList<AddressCompanysBean> format_companysBean_all;//将部门格式化后的,不包含company的公司信息

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

        if (list_companysBean_head == null) {
            list_companysBean_head = new ArrayList<>();
        }
        list_companysBean_head.clear();

        if (list_companysBean_branch == null) {
            list_companysBean_branch = new ArrayList<>();
        }
        list_companysBean_branch.clear();


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

        if (format_companysBean_all == null) {
            format_companysBean_all = new ArrayList<>();
        }
        format_companysBean_all.clear();

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

        if (addressBean != null) {
            addressBean = null;
        }

        list_companysBean.clear();
        list_deptsBean.clear();
        list_employeesBean.clear();
        list_positionsBean.clear();

        fileBeanDatas.clear();

        addressBean = GsonUtil.getAddressBean(SPUtil.getUserOrganizationJson(mActivity));

        // LogUtil.e("SPUtil.getUserOrganizationJson(mActivity)" + SPUtil.getUserOrganizationJson(mActivity));

        if (addressBean == null) {//如果为空,就return
            MyToast.showShort(mActivity, "获取通讯录信息失败!");
            return;
        }

        list_companysBean.add(addressBean.companys);
        list_deptsBean.addAll(addressBean.depts);
        list_positionsBean.add(addressBean.positions);
        list_employeesBean.addAll(addressBean.employees);

        //公司
        getFormatCompany(list_companysBean, "0");
        getHeadCompany(format_companysBean_list);
        getBranchCompany(format_companysBean_list);

        //岗位
        getPositions(list_positionsBean, list_positionsBean.get(0).dept_id);

        getDepts(list_deptsBean, list_deptsBean.get(0).company_id);

        getDeptsToCompany(format_deptsBean);

        LogUtil.e("$$$$$$$$--------fileBeanDatas_head-----------$$$$$$$$");
        for (AddressCompanysBean bean : format_companysBean_all) {
            LogUtil.e(bean.name + ":=id=:" + bean.id + ":=pId=:" + bean.pId + ":==:" + (bean.deptsBean != null) + ":=company_id=:" + bean.company_id + ":==:" + (bean.deptsBean.positionsBean != null));
        }

        getHeadCompanys(format_companysBean_all);

        getBranchCompanys(format_companysBean_all);

        LogUtil.e("==========fileBeanDatas_head==========");
        for (FileTreeBean bean : fileBeanDatas_branch) {
            LogUtil.e(bean.getName() + ":==:" + bean.get_id() + ":==:" + bean.getParentId() + ":==:" + (bean.getCompanysBean() != null) + ":==:");
        }


    }

    public void getHeadCompany(ArrayList<AddressCompanysBean> companysBean) {
        if (companysBean.size() > 0) {
            list_companysBean_head.add(companysBean.get(0));
        }
    }


    public void getBranchCompany(ArrayList<AddressCompanysBean> companysBean) {
        if (companysBean.size() > 1) {
            for (int i = 1; i < companysBean.size(); i++) {
                list_companysBean_branch.add(companysBean.get(i));
            }
        }

    }

    /**
     * 部门转换为公司
     *
     * @param deptsBean
     */
    private void getDeptsToCompany(ArrayList<AddressDeptsBean> deptsBean) {
        for (AddressDeptsBean bean : deptsBean) {
            AddressCompanysBean companysBean = new AddressCompanysBean();
            companysBean.id = bean.id;
            companysBean.pId = bean.pId;
            companysBean.deptsBean = bean;
            companysBean.name = bean.name;
            companysBean.company_id = bean.company_id;
            format_companysBean_all.add(companysBean);
        }

    }

    /**
     * 获取到总公司的数据
     *
     * @param companysBean
     */
    public void getHeadCompanys(ArrayList<AddressCompanysBean> companysBean) {

        for (AddressCompanysBean bean : companysBean) {
            if (list_companysBean_head.size() > 0) {

                if (list_companysBean_head.get(0).id.equals(bean.company_id)) {
                    list_companysBean_head.add(bean);
                }
            }

        }
        for (AddressCompanysBean bean : list_companysBean_head) {

//            if (bean.deptsBean != null) {
//                FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.deptsBean.id)
//                        , Long.parseLong(bean.deptsBean.pId)
//                        , bean.deptsBean.name
//                        , bean);
//                fileBeanDatas_head.add(treeBean);
//            } else {
            FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.id)
                    , Long.parseLong(bean.pId)
                    , bean.name
                    , bean);
            fileBeanDatas_head.add(treeBean);
//            }

        }


    }

    public void getBranchCompanys(ArrayList<AddressCompanysBean> companysBean) {

        for (AddressCompanysBean bean : companysBean) {
            if (list_companysBean_branch.size() > 0) {
                if (list_companysBean_branch.get(0).id.equals(bean.company_id)) {
                    list_companysBean_branch.add(bean);
                }
            }

        }
        for (AddressCompanysBean bean : list_companysBean_branch) {

            if (bean.deptsBean != null) {
                FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.deptsBean.id)
                        , Long.parseLong(bean.company_id)//这里暂时这样写//------这是一个大坑,bug
                        , bean.deptsBean.name
                        , bean);
                fileBeanDatas_branch.add(treeBean);
            } else {
                FileTreeBean treeBean = new FileTreeBean(Long.parseLong(bean.id)
                        , Long.parseLong(bean.company_id)//这里暂时这样写//------这是一个大坑,bug
                        , bean.name
                        , bean);
                fileBeanDatas_branch.add(treeBean);
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
    }

    /**
     * 组织架构树形----格式化
     *
     * @param childrens
     * @param pId
     */
    private void getPositions(ArrayList<AddressPositionsBean> childrens, String pId) {

        for (AddressPositionsBean bean : childrens) {
            bean.pId = pId;
            //添加职工信息
            for (AddressEmployeesBean employee : list_employeesBean) {
                if (bean.employee_id.equals(employee.id)) {
                    //bean.id = employee.id;
                    bean.employee = employee;
                }
            }
            //如果有就添加,没有就 不添加
            format_positionsBean.add(bean);
            if (bean.children != null && bean.children.size() > 0) {
                getPositions(bean.children, bean.id);
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
//                            bean.employee_id = addressPositionsBean.employee_id;
                            bean.company_id = addressDeptsBean.company_id;
                            LogUtil.e("添加子节点=======bean.id=====" + bean.name+"::"+addressPositionsBean.type);
                            if (addressPositionsBean.type.equals("1")) {
                                addressDeptsBean.children.add(bean);
                            }
                        }
                    }
                }
            }
            LogUtil.e("添加子jinqu节点=======addressDeptsBean.name=====" + addressDeptsBean.name);
            format_deptsBean.add(addressDeptsBean);
            if (addressDeptsBean.children != null && addressDeptsBean.children.size() > 0) {
                getDepts(addressDeptsBean.children, addressDeptsBean.id);
            }


        }

    }


    private void getFormatCompany(ArrayList<AddressCompanysBean> companys, String id) {
        for (AddressCompanysBean company : companys) {
            company.pId = id;
            company.company_id = company.id;
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
                if (bean.company_id.equals(children_bean.company_id)) {
                    if (children_bean.deptsBean != null) {
                        LogUtil.e("bean.deptsBean != null==" + bean.name);

                        FileTreeBean treeBean = new FileTreeBean(Long.parseLong(children_bean.deptsBean.id)
                                , Long.parseLong(children_bean.deptsBean.pId)
                                , children_bean.deptsBean.name
                                , children_bean);
                        fileBeanDatas_head.add(treeBean);
                    } else {
                        LogUtil.e("为空添加==" + children_bean.name);
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
                if (bean.company_id.equals(children_bean.company_id)) {
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


    private void setTreeView() {

        try {
            mAdapter = new TreeAddressZDLFAdapter<FileTreeBean>(treeView, mActivity, fileBeanDatas, 1);
            treeView.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, int position) {

                    if (node.getCompanysBean().deptsBean != null) {
                        //MyToast.showShort(mActivity,"点击了");
                        if (node.getCompanysBean().deptsBean.positionsBean!=null) {//有联系人再判断是否有号码
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

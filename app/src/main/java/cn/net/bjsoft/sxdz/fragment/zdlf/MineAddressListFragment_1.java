//package cn.net.bjsoft.sxdz.fragment.zdlf;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import org.xutils.common.util.LogUtil;
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.Event;
//import org.xutils.view.annotation.ViewInject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import cn.net.bjsoft.sxdz.R;
//import cn.net.bjsoft.sxdz.activity.EmptyActivity;
//import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
//import cn.net.bjsoft.sxdz.bean.app.user.address.AddressCompanysBean;
//import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;
//import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;
//import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesUserBean;
//import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
//import cn.net.bjsoft.sxdz.dialog.DialingPopupWindow;
//import cn.net.bjsoft.sxdz.fragment.BaseFragment;
//import cn.net.bjsoft.sxdz.utils.GsonUtil;
//import cn.net.bjsoft.sxdz.utils.MyToast;
//import cn.net.bjsoft.sxdz.utils.SPUtil;
//import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean.FileTreeBean;
//import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeAddressZDLFAdapter;
//import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeNode;
//
///**
// * Created by Zrzc on 2017/3/24.
// */
//@ContentView(R.layout.fragment_address_list)
//public class MineAddressListFragment_1 extends BaseFragment {
//    @ViewInject(R.id.title_back)
//    private ImageView title_back;
//    @ViewInject(R.id.title_title)
//    private TextView title;
//
//    //    @ViewInject(R.id.search_edittext)
////    private EditText search_edittext;
//    @ViewInject(R.id.address_search)
//    private ImageView address_search;
//
//
//    @ViewInject(R.id.address_parent)
//    private TextView address_parent;
//    @ViewInject(R.id.address_filiale)
//    private TextView address_filiale;
//    @ViewInject(R.id.address_list)
//    private ListView treeView;
//    @ViewInject(R.id.address_change)
//    private LinearLayout address_change;
//
//
//    private ArrayList<String> childAvatarList;
//    private ArrayList<String> childNameList;
//    private ArrayList<String> childNumList;
//
//    private FileTreeBean treeBean;
//    private List<FileTreeBean> fileBeanDatas;
//    private List<FileTreeBean> fileBeanDatas_head;
//    private List<FileTreeBean> fileBeanDatas_branch;
//
//    private TreeAddressZDLFAdapter mAdapter;
//
//    private String organization_url = "";
//
//
//    private AddressBean addressBean;
//    private AddressCompanysBean companysBean;
//    private ArrayList<AddressCompanysBean> list_companysBean;
//    private ArrayList<AddressCompanysBean> format_companysBean;
//
//    private ArrayList<AddressDeptsBean> list_deptsBean;
//    private ArrayList<AddressDeptsBean> format_deptsBean;//格式化岗位集合
//    private ArrayList<AddressDeptsBean> list_head_deptsBean;//总公司
//    private ArrayList<AddressDeptsBean> list_branch_deptsBean;//分公司
//    private ArrayList<AddressDeptsBean> list_change_deptsBean;
//
//
//    private ArrayList<AddressEmployeesBean> list_employeesBean;
//    private ArrayList<AddressPositionsBean> list_positionsBean;
//    private ArrayList<AddressPositionsBean> format_positionsBean;//格式化职位
//
//    private HashMap<String, AddressDeptsBean> map;//过滤重复联系人的中间集合
//
//
//    @Override
//    public void initData() {
//        title_back.setVisibility(View.VISIBLE);
//        title.setText("通讯录");
//        //setTreeView();
//        //organization_url = getArguments().getBundle("organization_url").getString("organization_url");
//        initList();
//        getOrganizationData();
//        addressChange(address_parent);
//    }
//
//    private void initList() {
//
//
//        if (list_head_deptsBean == null) {
//            list_head_deptsBean = new ArrayList<>();
//        }
//        list_head_deptsBean.clear();
//
//        if (list_branch_deptsBean == null) {
//            list_branch_deptsBean = new ArrayList<>();
//        }
//        list_branch_deptsBean.clear();
//
//
//        if (list_change_deptsBean == null) {
//            list_change_deptsBean = new ArrayList<>();
//        }
//        list_change_deptsBean.clear();
//
//
//        if (list_companysBean == null) {
//            list_companysBean = new ArrayList<>();
//        }
//        list_companysBean.clear();
//
//        if (list_deptsBean == null) {
//            list_deptsBean = new ArrayList<>();
//        }
//        list_deptsBean.clear();
//
//        if (list_employeesBean == null) {
//            list_employeesBean = new ArrayList<>();
//        }
//        list_employeesBean.clear();
//
//        if (list_positionsBean == null) {
//            list_positionsBean = new ArrayList<>();
//        }
//        list_positionsBean.clear();
//
//        if (format_companysBean == null) {
//            format_companysBean = new ArrayList<>();
//        }
//        format_companysBean.clear();
//
//        if (format_deptsBean == null) {
//            format_deptsBean = new ArrayList<>();
//        }
//        format_deptsBean.clear();
//
//        if (format_positionsBean == null) {
//            format_positionsBean = new ArrayList<>();
//        }
//        format_positionsBean.clear();
//
//
//        //------------------------
//        if (childAvatarList == null) {
//            childAvatarList = new ArrayList<>();
//        }
//        childAvatarList.clear();
//
//        if (childNameList == null) {
//            childNameList = new ArrayList<>();
//        }
//        childNameList.clear();
//
//        if (childNumList == null) {
//            childNumList = new ArrayList<>();
//        }
//        childNumList.clear();
//
//        //------------------------
//
//        if (fileBeanDatas_head == null) {
//            fileBeanDatas_head = new ArrayList<>();
//        }
//        fileBeanDatas_head.clear();
//
//        if (fileBeanDatas_branch == null) {
//            fileBeanDatas_branch = new ArrayList<>();
//        }
//        fileBeanDatas_branch.clear();
//
//        if (fileBeanDatas == null) {
//            fileBeanDatas = new ArrayList<>();
//        }
//        fileBeanDatas.clear();
//
//
//    }
//
//
//    /**
//     * 功能按钮
//     *
//     * @param view
//     */
//    @Event(value = {R.id.title_back
//            , R.id.address_search
//            , R.id.search_text
//            /*, R.id.search_delete*/})
//    private void addressOnClick(View view) {
//        switch (view.getId()) {
//            case R.id.title_back://返回
//                mActivity.finish();
//                break;
//
//            case R.id.address_search://返回
//                searchAddressList();
//                break;
////
////            case R.id.search_text://搜索按钮
////                searchAddressList();
////                break;
//
////            case R.id.search_delete://清空按钮
////                MyToast.showShort(mActivity, "清空输入框");
////                search_edittext.setText("");
////                break;
//
//        }
//    }
//
//    private void searchAddressList() {
//        //String searchStr = search_edittext.getText().toString().trim();
//        //if (!searchStr.equals("")) {
//
//        childAvatarList.clear();
//        childNameList.clear();
//        childNumList.clear();
////        LogUtil.e("获取到联系人数据-----------" + list_employeesBean.size());
//        for (AddressEmployeesBean bean : list_employeesBean) {//以防后台给的数据是残疾的
//            if (bean.user != null) {
//                childAvatarList.add(bean.user.avatar);
//            } else {
//                AddressEmployeesUserBean userBean = new AddressEmployeesUserBean();
//                childAvatarList.add(userBean.avatar);
//            }
//            childNameList.add(bean.name);
//            childNumList.add(bean.phone);
//        }
//
//
//        Intent searchIntent = new Intent(mActivity, EmptyActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("address_list_avatar", childAvatarList);
//        bundle.putStringArrayList("address_list_name", childNameList);
//        bundle.putStringArrayList("address_list_num", childNumList);
////            bundle.putString("address_list_search_str",searchStr);
//        bundle.putString("address_list_search_str", "");
//        searchIntent.putExtra("address_list_search_result_bundle", bundle);
//        searchIntent.putExtra("fragment_name", "mine_zdlf_address_search");
//        mActivity.startActivity(searchIntent);
////    } else
////
////    {
////        MyToast.showShort(mActivity, "请输入搜索内容!");
////        return;
////    }
//
//    }
//
//    /**
//     * 切换
//     *
//     * @param view
//     */
//    @Event(value = {R.id.address_parent
//            , R.id.address_filiale})
//    private void addressChange(View view) {
//        fileBeanDatas.clear();
//        //搜索相关
//        childAvatarList.clear();
//        childNameList.clear();
//        childNumList.clear();
//
//
//        switch (view.getId()) {
//            case R.id.address_parent://
//                address_parent.setBackgroundResource(R.drawable.approve_left_shixin);
//                address_parent.setTextColor(Color.parseColor("#FFFFFF"));
//                address_filiale.setBackgroundResource(R.drawable.approve_right_kongxin);
//                address_filiale.setTextColor(Color.parseColor("#000000"));
//                //getFormData();
//                //getOrganizationData();
//                //showMDatas();
//                changeTreeData(1);
//                break;
//
//            case R.id.address_filiale:
//                address_parent.setBackgroundResource(R.drawable.approve_left_kongxin);
//                address_parent.setTextColor(Color.parseColor("#000000"));
//                address_filiale.setBackgroundResource(R.drawable.approve_right_shixin);
//                address_filiale.setTextColor(Color.parseColor("#FFFFFF"));
//                changeTreeData(2);
//                //getFormData();
//                //getOrganizationData();
//
//                break;
//        }
//
//    }
//
//    private void getCompanys(ArrayList<AddressCompanysBean> companys, String id) {
//        String pid = id;
//        for (AddressCompanysBean company : companys) {
//            company.pId = pid;
//            if (company.children != null && company.children.size() > 0) {
//                getCompanys(company.children, company.id);
//            } else {
//            }
//            format_companysBean.add(company);
//        }
//    }
//
//
//    /**
//     * 切换岗位切换数据
//     *
//     * @param change
//     */
//    private void changeTreeData(int change) {
//        fileBeanDatas.clear();
//        switch (change) {
//            case 1:
//                fileBeanDatas.addAll(fileBeanDatas_head);
//                break;
//
//            case 2:
//                fileBeanDatas.addAll(fileBeanDatas_branch);
//                break;
//        }
//
//        setTreeView();
//    }
//
//
//    private void getOrganizationData() {
//
//        showProgressDialog();
//        if (addressBean != null) {
//            addressBean = null;
//        }
//
//        list_companysBean.clear();
//        list_deptsBean.clear();
//        list_employeesBean.clear();
//        list_positionsBean.clear();
//
//        fileBeanDatas.clear();
//
//        addressBean = GsonUtil.getAddressBean(SPUtil.getUserOrganizationJson(mActivity));
//
//        LogUtil.e("SPUtil.getUserOrganizationJson(mActivity)" + SPUtil.getUserOrganizationJson(mActivity));
//
//        if (addressBean == null) {//如果为空,就return
//            MyToast.showShort(mActivity, "获取通讯录信息失败!");
//            return;
//        }
//
//        list_companysBean.add(addressBean.companys);
//        list_deptsBean.addAll(addressBean.depts);
//        list_employeesBean.addAll(addressBean.employees);
//        list_positionsBean.add(addressBean.positions);
//
//
//        getCompanys(list_companysBean, "0");
//
//        //添加总公司数据
//        list_head_deptsBean.clear();
//        AddressDeptsBean deptsBean_head = new AddressDeptsBean();
//        deptsBean_head.id = format_companysBean.get(format_companysBean.size() - 1).id;
//        deptsBean_head.pId = format_companysBean.get(format_companysBean.size() - 1).pId;
//        deptsBean_head.name = format_companysBean.get(format_companysBean.size() - 1).name;
//        //deptsBean_head.company_id = format_companysBean.get(format_companysBean.size() - 1).id;
//
//        LogUtil.e("deptsBean_head====id==="+deptsBean_head.id+":name:"+deptsBean_head.name);
//
//        for (AddressDeptsBean bean_head : list_deptsBean) {
//            if (bean_head.company_id.equals(deptsBean_head.id)) {
//                if (deptsBean_head.children == null) {
//                    deptsBean_head.children = new ArrayList<>();
//                }
//                if (bean_head.company_id.equals(deptsBean_head.id)) {
//                    LogUtil.e("deptsBean.children_head====id==="+bean_head.company_id+":name:"+bean_head.name);
//                    deptsBean_head.children.add(bean_head);
//                }
//
//            }
//        }
//        list_head_deptsBean.add(deptsBean_head);
//
//
//        //添加子公司数据
//        list_branch_deptsBean.clear();
//
//        if (format_companysBean.size() > 1) {
//            for (int i = 0; i < format_companysBean.size() - 1; i++) {
//                AddressDeptsBean deptsBean_branch = new AddressDeptsBean();
//                deptsBean_branch.id = format_companysBean.get(i).id;
//                deptsBean_branch.pId = format_companysBean.get(i).pId;
//                deptsBean_branch.name = format_companysBean.get(i).name;
//                LogUtil.e("deptsBean_branch====id==="+deptsBean_branch.id+":name:"+deptsBean_branch.name);
//
//                for (AddressDeptsBean bean_branch : list_deptsBean) {
//                    if (bean_branch.company_id.equals(deptsBean_branch.id)) {
//                        if (deptsBean_branch.children == null) {
//                            deptsBean_branch.children = new ArrayList<>();
//                        }
//
//                        if (bean_branch.company_id.equals(deptsBean_head.id)) {
//                            LogUtil.e("deptsBean_branch.children_head====id==="+deptsBean_branch.company_id+":name:"+deptsBean_branch.name);
//
//                            deptsBean_branch.children.add(bean_branch);
//                        }
//
//                    }
//                }
//                list_branch_deptsBean.add(deptsBean_branch);
//            }
//        }
//
//
//        format_positionsBean.clear();
//        getPositions(list_positionsBean, "0");
//        fileBeanDatas_head.clear();
//        getDepts(list_head_deptsBean, "0", 1);//总部
//        LogUtil.e("添加子节点=======总部=====");
//        fileBeanDatas_branch.clear();
//        getDepts(list_branch_deptsBean, "0", 2);//分公司
//
////        fileBeanDatas_head.clear();
////        getDepts(list_deptsBean, "0", 1);//总部
////        LogUtil.e("添加子节点=======总部=====");
////        fileBeanDatas_branch.clear();
////        getDepts(list_deptsBean, "0", 2);//分公司
//
//        dismissProgressDialog();
//
//    }
//
//
//    /**
//     * 部门树形结构格式化
//     *
//     * @param deptsBean
//     * @param pid
//     */
//    private void getDepts(ArrayList<AddressDeptsBean> deptsBean, String pid, int change) {
//
//
//        for (AddressDeptsBean addressDeptsBean : deptsBean) {
//            addressDeptsBean.pId = pid;
//            treeBean = null;
//            //LogUtil.e("遍历子节点------addressDeptsBean.id" + addressDeptsBean.id);
//            //向部门添加联系人
////            map = new HashMap<>();
//            for (AddressPositionsBean addressPositionsBean : format_positionsBean) {
//
//                if (addressPositionsBean.dept_id.equals(addressDeptsBean.id)) {
//
//                    if (addressDeptsBean.children == null) {
//                        addressDeptsBean.children = new ArrayList<AddressDeptsBean>();
//                    } else {
//                        //过滤重复的联系人
//                        if (addressPositionsBean.employee != null) {
//                            AddressDeptsBean bean = new AddressDeptsBean();
//                            bean.id = addressPositionsBean.employee.id;
//
//                            bean.positionsBean = addressPositionsBean;
//                            bean.name = addressPositionsBean.employee.name;
//                            bean.employee_id = addressPositionsBean.employee_id;
//                            LogUtil.e("添加子节点=======bean.id=====" + bean.name);
//                            if (addressPositionsBean.type.equals("1")) {
//                                addressDeptsBean.children.add(bean);
//                            }
//
//                            //map.put(bean.id, bean);//用map是为了去除重复元素
//                        }
//                    }
//                }
//            }
////            Iterator iter = map.entrySet().iterator();
////            while (iter.hasNext()) {
////                Map.Entry entry = (Map.Entry) iter.next();
////                Object key = entry.getKey();
////                AddressDeptsBean bean = (AddressDeptsBean) entry.getValue();
////                addressDeptsBean.children.add(bean);
////            }
////
////            map = null;
//
//
//            if (addressDeptsBean.children != null && addressDeptsBean.children.size() > 0) {
//                getDepts(addressDeptsBean.children, addressDeptsBean.id, change);
//            } else {
//
//            }
//            treeBean = new FileTreeBean(Long.parseLong(addressDeptsBean.id)
//                    , Long.parseLong(addressDeptsBean.pId)
//                    , addressDeptsBean.name
//                    , addressDeptsBean);
//
//            if (change == 1) {
//                fileBeanDatas_head.add(treeBean);
//                LogUtil.e("添加子节点=======总公司=====" + fileBeanDatas_head.size());
//            } else if (change == 2) {
//                fileBeanDatas_branch.add(treeBean);
//                LogUtil.e("添加子节点=======分公司=====" + fileBeanDatas_branch.size());
//            }
////
////            switch (change) {
////                case 1:
////
////                    break;
////
////                case 2:
////
////                    break;
////            }
//
//        }
//
//    }
//
//    /**
//     * 组织架构树形----格式化
//     *
//     * @param childrens
//     * @param pId
//     */
//    private void getPositions(ArrayList<AddressPositionsBean> childrens, String pId) {
//
//        for (AddressPositionsBean children : childrens) {
//            children.pId = pId;
//            treeBean = null;
//            //添加职工信息
//            for (AddressEmployeesBean employee : list_employeesBean) {
//                if (children.employee_id.equals(employee.id)) {
//                    children.employee = employee;
//                }
//            }
//            //如果有就添加,没有就 不添加
//            format_positionsBean.add(children);
//            if (children.children != null && children.children.size() > 0) {
//                getPositions(children.children, children.id);
//            }
//
//        }
//    }
//
//
//    //初始化树形结构---------------------------开始----------------------------
//
//
//    private void setTreeView() {
//
//        try {
//            mAdapter = new TreeAddressZDLFAdapter<FileTreeBean>(treeView, mActivity, fileBeanDatas, 1);
//            treeView.setAdapter(mAdapter);
//
//            mAdapter.setOnTreeNodeClickListener(new cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper.TreeListViewAdapter.OnTreeNodeClickListener() {
//                @Override
//                public void onClick(TreeNode node, int position) {
//                    if (!node.getAddressDeptsBean().employee_id.equals("")) {
//
//                        if (TextUtils.isEmpty(node.getAddressDeptsBean().positionsBean.employee.phone)) {
//                            MyToast.showShort(mActivity, "该联系人没有设置电话号码!");
//                        } else {
//                            DialingPopupWindow window = new DialingPopupWindow(mActivity, address_change, node.getAddressDeptsBean().positionsBean.employee.phone);
//                        }
//                    }
//                }
//            });
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        dismissProgressDialog();
//    }
//
//    //初始化树形结构---------------------------结束----------------------------
//}

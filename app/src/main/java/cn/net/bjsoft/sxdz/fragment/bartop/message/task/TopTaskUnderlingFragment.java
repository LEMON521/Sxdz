package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.widget.ListView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.bean.FileTreeTaskUnderlingBean;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.helper.TreeListUnderlingViewAdapter;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.helper.TreeTaskAddressUnderlingAdapter;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.helper.TreeTaskUnderlingNode;

/**
 * 下属任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_underling)
public class TopTaskUnderlingFragment extends BaseFragment {

    @ViewInject(R.id.fragment_task_list_underling)
    private ListView list_underling;


    private PostJsonBean pushUnderlingBean;

//    private ListTaskBean bean;
//    private FileTaskBean fileTaskBean;
//    private List<FileTaskBean> mDatas;
//    private ListTaskBean.TaskDataDao treeListDao;
//    private ArrayList<ListTaskBean.TreeTaskListDao> tree_list;


    private AddressBean addressBean;
    private AddressEmployeesBean employeesBean;
    private AddressPositionsBean positionsBean;
    private ArrayList<AddressEmployeesBean> employeesBeanList;
    private ArrayList<AddressPositionsBean> positionsBeanList;
    private ArrayList<AddressPositionsBean> formate_positionsBeanList;

    private FileTreeTaskUnderlingBean fileBean;
    private ArrayList<FileTreeTaskUnderlingBean> fileBeanList;
    private TreeTaskAddressUnderlingAdapter mAdapter;

    private String get_start = "0";
    private String get_count = "0";

    @Override
    public void initData() {


        initList();

        getOrganizationData();


        //getFormData();
    }

    private void initList() {
        pushUnderlingBean = new PostJsonBean();

        if (employeesBeanList == null) {
            employeesBeanList = new ArrayList<>();
        }
        employeesBeanList.clear();

        if (positionsBeanList == null) {
            positionsBeanList = new ArrayList<>();
        }
        positionsBeanList.clear();


        if (formate_positionsBeanList == null) {
            formate_positionsBeanList = new ArrayList<>();
        }
        formate_positionsBeanList.clear();


        if (fileBeanList == null) {
            fileBeanList = new ArrayList<>();
        }
        fileBeanList.clear();


//        if (treeDatas == null) {
//            treeDatas = new ArrayList<>();
//        }
//        treeDatas.clear();
//
//
//        if (tree_list == null) {
//            tree_list = new ArrayList<>();
//        }
//        tree_list.clear();
//        if (mDatas == null) {
//            mDatas = new ArrayList<>();
//        }
//        mDatas.clear();
    }


    private void getOrganizationData() {

        showProgressDialog();
        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = SPUtil.getApiUser(mActivity) + "/" + SPUtil.getUserId(mActivity) + "/organization.json";
        LogUtil.e("公司架构userOrganizationBean url----===========" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                SPUtil.setUserOrganizationJson(mActivity, strJson);//缓存公司架构信息
                //LogUtil.e("我的页面json");
//                LogUtil.e("公司架构==========="+SPUtil.getUserOrganizationJson(mActivity));

                addressBean = GsonUtil.getAddressBean(strJson);
                if (addressBean != null) {
                    employeesBeanList.addAll(addressBean.employees);
                    if (addressBean.positions != null) {
                        positionsBeanList.add(addressBean.positions);

                        formateEmployees();

                        if (positionsBeanList.size() > 0) {
                            getPositions(positionsBeanList, "0");
                        }
                    } else {
                        MyToast.showLong(mActivity, "获取组织架构信息失败!");
                    }
                } else {
                    MyToast.showLong(mActivity, "获取组织架构信息失败!");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SPUtil.setUserOrganizationJson(mActivity, "");
                LogUtil.e("我的页面json-----错误" + ex);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                getFormatePositions();
                setTreeView();
                dismissProgressDialog();
            }
        });

    }

    /**
     * 防止后台数据给的是残疾的
     */
    private void formateEmployees() {
        for (AddressEmployeesBean employeesBean : employeesBeanList) {
            if (employeesBean.user != null) {
                employeesBean.user.id = employeesBean.user.id.replace(".0", "");
                employeesBean.user.source_id = employeesBean.user.source_id.replace(".0", "");
            }
        }
    }

    /**
     * 格式化岗位---从userID相同的添加开始
     *
     * @param childrens
     * @param pId
     */
    private void getPositions(ArrayList<AddressPositionsBean> childrens, String pId) {

        for (AddressPositionsBean bean : childrens) {
            bean.pId = pId;
            //添加职工信息
            for (AddressEmployeesBean employee : employeesBeanList) {
                if (bean.employee_id.equals(employee.id)) {
                    //bean.id = employee.id;
                    bean.employee = employee;
                }
            }

            if (bean.children != null && bean.children.size() > 0) {
                getPositions(bean.children, bean.id);
            }
            if (formate_positionsBeanList.size() == 0) {
                if (bean.employee.user != null) {
                    if (!SPUtil.getUserId(mActivity).equals(bean.employee.user.id)) {
                        break;
                    }
                } else {
                    break;
                }
            }

            //如果有就添加,没有就 不添加
            formate_positionsBeanList.add(formate_positionsBeanList.size(), bean);


        }

    }

    private void getFormatePositions() {
        for (AddressPositionsBean positionsBean : formate_positionsBeanList) {
            if (positionsBean.employee != null) {
                LogUtil.e("------------------岗位信息====" + positionsBean.name + "::"+positionsBean.id+"::"+positionsBean.pId+"::" + positionsBean.employee.name);
                fileBean = new FileTreeTaskUnderlingBean(
                        Long.parseLong(positionsBean.id)
                        , Long.parseLong(positionsBean.pId)
                        , positionsBean.name
                        , positionsBean);
                fileBeanList.add(fileBean);
            }
        }

        for (FileTreeTaskUnderlingBean bean:fileBeanList){
            LogUtil.e("联系人信息-------------"+bean.getName()+"::"+bean.get_id()+"::"+bean.getParentId()+"::"+bean.getPositionsBean());
        }
    }


    //初始化树形结构---------------------------开始----------------------------


    private void setTreeView() {

        //getItems(tree_list, 0);
        try {
            mAdapter = new TreeTaskAddressUnderlingAdapter<FileTreeTaskUnderlingBean>(list_underling, mActivity, fileBeanList, 1);
            list_underling.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new TreeListUnderlingViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(TreeTaskUnderlingNode node, int position) {

                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        dismissProgressDialog();
    }
    //初始化树形结构---------------------------结束----------------------------
}

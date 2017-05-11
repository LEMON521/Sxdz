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
import cn.net.bjsoft.sxdz.bean.app.user.UserBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.bean.FileMessageTaskUnderlingBean;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.helper.NodeMessageTaskUnderling;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.helper.TreeMessageTaskUnderlingAdapter;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.helper.TreeMessageTaskUnderlingListViewAdapter;

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


    private UserBean userBean;
    private String position_id = "";

    private AddressBean addressBean;
    private AddressEmployeesBean employeesBean;
    private AddressPositionsBean positionsBean;
    private ArrayList<AddressEmployeesBean> employeesBeanList;
    private ArrayList<AddressPositionsBean> positionsBeanList;
    private ArrayList<AddressPositionsBean> buffer_positionsBeanList;
    private ArrayList<AddressPositionsBean> formate_positionsBeanList;

    private FileMessageTaskUnderlingBean fileBean;
    private ArrayList<FileMessageTaskUnderlingBean> fileBeanList;
    private TreeMessageTaskUnderlingAdapter mAdapter;

    private String get_start = "0";
    private String get_count = "0";

    @Override
    public void initData() {
        initList();
        userOrganizationData();
        //getUserInfos();
    }

    private void userOrganizationData(){

        userBean = GsonUtil.getUserBean(SPUtil.getUserJson(mActivity));
        position_id = userBean.organization.position_id;

        addressBean = GsonUtil.getAddressBean(SPUtil.getUserOrganizationJson(mActivity));

        if (addressBean != null) {
            employeesBeanList.addAll(addressBean.employees);
            if (addressBean.positions != null) {
                positionsBeanList.add(addressBean.positions);

                formateEmployees();

                if (positionsBeanList.size() > 0) {
                    getBufferPosition(positionsBeanList);
                    if (buffer_positionsBeanList.size() > 0) {
                        getPositions(buffer_positionsBeanList, "0");
                    }
                }

                getFormatePositions();

                setTreeView();

            } else {
                MyToast.showLong(mActivity, "获取组织架构信息失败!");
            }
        } else {
            MyToast.showLong(mActivity, "获取组织架构信息失败!");
        }
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

        if (buffer_positionsBeanList == null) {
            buffer_positionsBeanList = new ArrayList<>();
        }
        buffer_positionsBeanList.clear();


        if (formate_positionsBeanList == null) {
            formate_positionsBeanList = new ArrayList<>();
        }
        formate_positionsBeanList.clear();


        if (fileBeanList == null) {
            fileBeanList = new ArrayList<>();
        }
        fileBeanList.clear();
    }

    private void getUserInfos() {
        showProgressDialog();
        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = SPUtil.getApiUser(mActivity) + "/" + SPUtil.getUserId(mActivity) + "/my.json";
        LogUtil.e("公司架构userOrganizationBean url----===========" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                //SPUtil.setUserJson(mActivity, strJson);//缓存公司架构信息
                //LogUtil.e("我的页面json");
//                LogUtil.e("公司架构==========="+SPUtil.getUserOrganizationJson(mActivity));
                userBean = GsonUtil.getUserBean(strJson);
                if (userBean != null) {
                    position_id = userBean.organization.position_id;
                    getOrganizationData();
                } else {
                    MyToast.showLong(mActivity, "获取信息失败!");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("我的页面json-----错误" + ex);
                MyToast.showLong(mActivity, "获取信息失败!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getOrganizationData() {

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
                            getBufferPosition(positionsBeanList);
                            if (buffer_positionsBeanList.size() > 0) {
                                getPositions(buffer_positionsBeanList, "0");
                            }
                        }

                        getFormatePositions();

                        setTreeView();

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
     * 截取
     * @param childrens
     */
    private void getBufferPosition(ArrayList<AddressPositionsBean> childrens) {


        for (AddressPositionsBean bean : childrens) {


            if (bean.id.equals(position_id)) {
                buffer_positionsBeanList.add(bean);
                LogUtil.e("--------bean---------"+bean.children.size());
                return;
            } else {
                if (bean.children != null && bean.children.size() > 0) {
                    getBufferPosition(bean.children);
                }

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
            LogUtil.e("========bean.name========"+bean.name);
            LogUtil.e("========bean.children.size()========"+bean.children.size());
            formate_positionsBeanList.add(bean);
            if (bean.children != null && bean.children.size() > 0) {
                getPositions(bean.children, bean.id);
            }
            LogUtil.e("========formate_positionsBeanList.size()========"+formate_positionsBeanList.size());
        }

    }


    private void getFormatePositions() {
        LogUtil.e("========formate_positionsBeanList.size()========"+formate_positionsBeanList.size());
        for (AddressPositionsBean positionsBean : formate_positionsBeanList) {
            if (positionsBean.employee != null) {
                LogUtil.e("------------------岗位信息====" + positionsBean.name + "::" + positionsBean.id + "::" + positionsBean.pId + "::" + positionsBean.employee.name);
                fileBean = new FileMessageTaskUnderlingBean(
                        positionsBean.id
                        , positionsBean.pId
                        , positionsBean);
                fileBeanList.add(fileBean);
            }
        }

//        for (FileMessageTaskUnderlingBean bean : fileBeanList) {
//            LogUtil.e("联系人信息-------------" + "::" + bean.get_id() + "::" + bean.getParentId() + "::" + bean.getPositionsBean());
//        }
    }


    //初始化树形结构---------------------------开始----------------------------


    private void setTreeView() {

        //getItems(tree_list, 0);
        try {
            mAdapter = new TreeMessageTaskUnderlingAdapter<FileMessageTaskUnderlingBean>(list_underling, mActivity, fileBeanList, 1);
            list_underling.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new TreeMessageTaskUnderlingListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(NodeMessageTaskUnderling node, int position) {

                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        dismissProgressDialog();
    }
    //初始化树形结构---------------------------结束----------------------------
}

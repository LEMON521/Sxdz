package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.net.bjsoft.sxdz.dialog.ListPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.FileTreeTaskAddAddressListBean;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListBean;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.helper.NodeTaskAddAddressList;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.helper.TreeTaskAddAddressListAdapter;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.helper.TreeTaskAddAddressListListViewAdapter;

/**
 * Created by Zrzc on 2017/4/18.
 */
@ContentView(R.layout.fragment_task_add_address_list)
public class TopAddTaskAddressListFragment extends BaseFragment {
    @ViewInject(R.id.title_title)
    private TextView title;
    @ViewInject(R.id.title_back)
    private ImageView back;

    @ViewInject(R.id.fragmen_task_add_address_checkbox)
    private CheckBox totle_humen;
    @ViewInject(R.id.fragmen_task_add_address_department)
    private EditText department;
    @ViewInject(R.id.fragmen_task_add_address_list_view)
    private ListView humens;

    private ArrayList<String> departmentList;
    private ListPopupWindow window;

    private TreeTaskAddAddressListBean bean;
    private FileTreeTaskAddAddressListBean dao;
    private List<FileTreeTaskAddAddressListBean> mDatas;
    private ArrayList<TreeTaskAddAddressListBean.TreeTaskAddAddressListDao> childrenDao;

    private HashMap<Integer, NodeTaskAddAddressList> nodes;//用来存放选中人的信息

    private ArrayList<String> nodeId;
    private ArrayList<String> nodeAvatar;
    private ArrayList<String> nodeName;
    private ArrayList<String> nodeDepartment;

    private TreeTaskAddAddressListAdapter adapter;

    @Event(value = {R.id.title_back
            , R.id.fragmen_task_add_address_department
            , R.id.fragmen_task_add_address_list_submin})
    private void onClick(View view) {


        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;

            case R.id.fragmen_task_add_address_department://选择分公司
                int[] location = new int[2];//窗口位置
                department.getLocationOnScreen(location);
                location[1] = location[1] + department.getHeight();

                window.showWindow(departmentList, location);

                break;
            case R.id.fragmen_task_add_address_list_submin://确定

                Iterator iter = nodes.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    nodeId.add(((NodeTaskAddAddressList) entry.getValue()).getId() + "");
                    nodeAvatar.add(((NodeTaskAddAddressList) entry.getValue()).getAvatar());
                    nodeName.add(((NodeTaskAddAddressList) entry.getValue()).getName());
                    nodeDepartment.add(((NodeTaskAddAddressList) entry.getValue()).getDepartment());
                    LogUtil.e(nodes.size() + ((NodeTaskAddAddressList) entry.getValue()).getAvatar() + "数量--------+++++++++++++--------");
                }

                LogUtil.e(nodes.size() + "数量--------+++++++++++++--------");


                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("nodeId", nodeId);
                bundle.putStringArrayList("nodeAvatar", nodeAvatar);
                bundle.putStringArrayList("nodeName", nodeName);
                bundle.putStringArrayList("nodeDepartment", nodeDepartment);
                intent.putExtras(bundle);
                mActivity.setResult(1000, intent);
                mActivity.finish();
                break;


        }
    }


    @Override
    public void initData() {
        title.setText("");
        back.setVisibility(View.VISIBLE);


        initList();
        listeners();


    }

    private void listeners() {


        department.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                childrenDao.clear();
                mDatas.clear();
                getFormData();//请求数据
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (departmentList.size() > 0) {
            department.setText(departmentList.get(0));//第一次自动设置数据
        }
    }


    private void setTreeView() {

        //getItems(tree_list, 0);

        try {
            adapter = new TreeTaskAddAddressListAdapter<FileTreeTaskAddAddressListBean>(humens, mActivity, mDatas, nodes, 1);
            humens.setAdapter(adapter);

            adapter.setOnTreeNodeClickListener(new TreeTaskAddAddressListListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(NodeTaskAddAddressList node, int position) {
                    MyToast.showShort(mActivity, "点击" + position + "::" + node.getName());
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从服务端获取数据
     */
    private void getFormData() {
        showProgressDialog();

        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_add_address_listl_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                bean = GsonUtil.getTreeTaskAddAddressListBean(result);
                if (bean.result) {
                    //LogUtil.e("获取到报表数据-----------" + result);
                    childrenDao.addAll(bean.data.tree_task_add_address_list);
                    getItems(childrenDao, 1);
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
                dismissProgressDialog();
            }
        });
    }

    private void initList() {


        window = new ListPopupWindow(mActivity, department);
        //popupWindw 的回调接口
        window.setOnData(new ListPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                LogUtil.e("获取到的结果为====");
                department.setText(result);
            }
        });

        if (departmentList == null) {
            departmentList = new ArrayList<>();
        }
        departmentList.clear();
        departmentList.add("总公司");
        departmentList.add("山东分公司");
        departmentList.add("北京分公司");
        departmentList.add("河北分公司");
        departmentList.add("广州分公司");


        if (nodes == null) {
            nodes = new HashMap<>();
        }
        nodes.clear();

        if (nodeId == null) {
            nodeId = new ArrayList<>();
        }
        nodeId.clear();

        if (nodeAvatar == null) {
            nodeAvatar = new ArrayList<>();
        }
        nodeAvatar.clear();

        if (nodeName == null) {
            nodeName = new ArrayList<>();
        }
        nodeName.clear();

        if (nodeDepartment == null) {
            nodeDepartment = new ArrayList<>();
        }
        nodeDepartment.clear();

        if (childrenDao == null) {
            childrenDao = new ArrayList<>();
        }
        childrenDao.clear();

        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();

    }


    //将网络数据添加到本地的数据格式中去
    public void getItems(ArrayList<TreeTaskAddAddressListBean.TreeTaskAddAddressListDao> list, int level) {

        level++;

        for (TreeTaskAddAddressListBean.TreeTaskAddAddressListDao children : list) {
            bean = null;
            if (children.children != null) {
                dao = new FileTreeTaskAddAddressListBean(Integer.parseInt(children.id)
                        , Integer.parseInt(children.pid)
                        , children.name
                        , children.type
                        , children.avatar
                        , children.department
                        , children.humen_num);
                mDatas.add(dao);

                getItems(children.children, level);
            } else {
                if (!children.name.equals("")) {
                    //mDatas.add(new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name));
                    dao = new FileTreeTaskAddAddressListBean(Integer.parseInt(children.id)
                            , Integer.parseInt(children.pid)
                            , children.name
                            , children.type
                            , children.avatar
                            , children.department
                            , children.humen_num);

                    mDatas.add(dao);
                }
            }

        }
        setTreeView();
    }
    //初始化树形结构---------------------------结束----------------------------
}

package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.FileTaskBean;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.ListTaskBean;
import cn.net.bjsoft.sxdz.view.tree_task_underling.helper.NodeTaskUnderling;
import cn.net.bjsoft.sxdz.view.tree_task_underling.helper.TreeTaskUnderlingAdapter;

/**
 * 下属任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_underling)
public class TopTaskUnderlingFragment extends BaseFragment {

    @ViewInject(R.id.fragment_task_list_underling)
    private ListView list_underling;


    private ListTaskBean bean;
    private FileTaskBean fileTaskBean;
    private List<FileTaskBean> mDatas;
    private ListTaskBean.TaskDataDao treeListDao;
    private ArrayList<ListTaskBean.TreeTaskListDao> tree_list;
    private TreeTaskUnderlingAdapter mAdapter;

    @Override
    public void initData() {


        initList();
        getFormData();
    }


    /**
     * 从服务端获取数据
     */
    private void getFormData() {
        showProgressDialog();
        LogUtil.e("获取到报表数据&&&&&&&&" + tree_list.size());
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_list_underling_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                bean = GsonUtil.getListTaskBean(result);
                LogUtil.e("获取到报表数据&&&&&&&&" + bean.result);
                if (bean.result) {
                    //LogUtil.e("获取到报表数据-----------" + result);
                    //scroll_list.clear();
                    treeListDao = bean.data;

                    tree_list.addAll(treeListDao.tree_list);
                    //LogUtil.e("获取到报表数据-----------" + result);
                    getItems(tree_list, 1);

                    LogUtil.e("获取到报表数据&&&&&&&&" + tree_list.size());

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到报表数据&&&&&&&&错误信息" + ex);
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
            mAdapter = new TreeTaskUnderlingAdapter<FileTaskBean>(list_underling, mActivity, mDatas, 1);
            list_underling.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new TreeTaskUnderlingAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(NodeTaskUnderling node, int position) {
                    Log.e("点击的条目", "tiaomu wei ====" + position);
                    Intent intent = new Intent(mActivity, EmptyActivity.class);
                    intent.putExtra("fragment_name", "TopTaskUnderlingDetailFragment");
                    mActivity.startActivity(intent);

                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        dismissProgressDialog();
    }

    //将网络数据添加到本地的数据格式中去
    public void getItems(ArrayList<ListTaskBean.TreeTaskListDao> list, int level) {
        level++;

        for (ListTaskBean.TreeTaskListDao children : list) {
            fileTaskBean = null;
            if (children.children != null) {
                fileTaskBean = new FileTaskBean(Integer.parseInt(children.id)
                        , Integer.parseInt(children.pid)
                        , children.name
                        , children.url
                        , children.department
                        , children.task_num);

                mDatas.add(fileTaskBean);

                LogUtil.e("获取到报表数据&&&&&&&&" + fileTaskBean.getName());

                getItems(children.children, level);
            } else {
                if (!children.name.equals("")) {
                    //mDatas.add(new FileBean(Integer.parseInt(children.id), Integer.parseInt(children.pid), children.name));
                    fileTaskBean = new FileTaskBean(Integer.parseInt(children.id)
                            , Integer.parseInt(children.pid)
                            , children.name
                            , children.url
                            , children.department
                            , children.task_num);

                    mDatas.add(fileTaskBean);
                    LogUtil.e("获取到报表数据&&&&&&&&" + fileTaskBean.getName());
                }
            }

        }
        setTreeView();
    }
    //初始化树形结构---------------------------结束----------------------------
}

package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

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
import cn.net.bjsoft.sxdz.adapter.zdlf.TaskUnderlingTreeAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
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


    private PostJsonBean pushUnderlingBean;

    private ListTaskBean bean;
    private FileTaskBean fileTaskBean;
    private List<FileTaskBean> mDatas;
    private ListTaskBean.TaskDataDao treeListDao;
    private ArrayList<ListTaskBean.TreeTaskListDao> tree_list;
    private TaskUnderlingTreeAdapter mAdapter;

    private String get_start = "0";
    private String get_count = "0";

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
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "task_finished");

        pushUnderlingBean.start = get_start;//设置开始查询
        pushUnderlingBean.limit = "10";
        params.addBodyParameter("data", pushUnderlingBean.toString());
        LogUtil.e("-------------------------bean.toString()"+pushUnderlingBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取消息----------------" + strJson);
//                taskBean = GsonUtil.getMessageTaskBean(strJson);
//                if (taskBean.code.equals("0")) {
//                    tasksDoneDao.addAll(taskBean.data.items);
//                    get_start = tasksDoneDao.size() + "";//设置开始查询
//                    get_count = taskBean.data.count + "";
//
//                    formateDatas();//格式化信息
//
//                    taskAdapter.notifyDataSetChanged();
//                    if (get_count.equals("0")) {
//                        MyToast.showLong(mActivity, "没有任何消息可查看!");
//                    }
//                } else {
//                    MyToast.showLong(mActivity, "获取消息失败-"/*+taskBean.msg*/);
//                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取消息-----------失败方法-----" + element.getMethodName());
                }

                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
        showProgressDialog();

        RequestParams params_1 = new RequestParams(TestAddressUtils.test_get_message_task_list_underling_url);
        x.http().get(params_1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                bean = GsonUtil.getListTaskBean(result);
                if (bean.result) {
                    //LogUtil.e("获取到报表数据-----------" + result);
                    //scroll_list.clear();
                    treeListDao = bean.data;

                    tree_list.addAll(treeListDao.tree_list);
                    //LogUtil.e("获取到报表数据-----------" + result);
                    getItems(tree_list, 1);

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
        pushUnderlingBean = new PostJsonBean();

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
            mAdapter = new TaskUnderlingTreeAdapter<FileTaskBean>(list_underling, mActivity, mDatas, 1);
            list_underling.setAdapter(mAdapter);

            mAdapter.setOnTreeNodeClickListener(new TreeTaskUnderlingAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(NodeTaskUnderling node, int position) {
                    Log.e("点击的条目", "tiaomu wei ====" + position);


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

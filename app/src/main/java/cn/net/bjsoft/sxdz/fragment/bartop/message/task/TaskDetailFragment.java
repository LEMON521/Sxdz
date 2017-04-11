package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskDetailAddAdapter;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemHeadFilesListAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.DownLoadFilesUtils;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.ChildrenListView;
import cn.net.bjsoft.sxdz.view.observable_scroll_view.ObservableScrollView;

/**
 * Created by Zrzc on 2017/4/5.
 */

@ContentView(R.layout.fragment_task_detail)
public class TaskDetailFragment extends BaseFragment {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;

//    @ViewInject(R.id.item_task_sxdz_name)
//    private TextView sxdz_title;
//    @ViewInject(R.id.item_task_sxdz_title)
//    private TextView sxdz_name;
//    @ViewInject(R.id.item_task_sxdz_start)
//    private TextView sxdz_start;
//    @ViewInject(R.id.item_task_sxdz_end)
//    private TextView sxdz_end;
//    @ViewInject(R.id.item_task_sxdz_state)
//    private TextView sxdz_state;

    @ViewInject(R.id.task_item_title)
    private TextView task_title;
    @ViewInject(R.id.task_item_overdue)
    private ImageView task_overdue;
    @ViewInject(R.id.task_item_classify)
    private TextView task_classify;
    @ViewInject(R.id.task_item_name)
    private TextView task_name;
    @ViewInject(R.id.task_item_start)
    private TextView task_start;
    @ViewInject(R.id.task_item_end)
    private TextView task_end;
    @ViewInject(R.id.task_item_state)
    private TextView task_state;
    @ViewInject(R.id.task_item_level)
    private TextView task_level;


    @ViewInject(R.id.fragment_task_scroll)
    private ObservableScrollView scroll;
    @ViewInject(R.id.fragment_task_detail_host)
    private TextView detail;
    @ViewInject(R.id.fragment_task_attachment_show)
    private LinearLayout attachment_show;
    @ViewInject(R.id.fragment_task_attachment)
    private ChildrenListView attachment;//任务附件
    @ViewInject(R.id.fragment_task_detail_list)
    private ChildrenListView detail_list;
    @ViewInject(R.id.fragment_task_add_detail)
    private TextView add_detail;
    @ViewInject(R.id.fragment_task_progress)
    private BubbleSeekBar progress;
    @ViewInject(R.id.fragment_task_files)
    private ChildrenListView files;//执行人新增附件
    @ViewInject(R.id.fragment_task_add_files)
    private TextView add_files;

    private MessageTaskDetailBean detailBean;
    private MessageTaskDetailBean.MessageTaskDetailDao detailDao;
    private MessageTaskDetailBean.TasksDao taskDao;


    private KnowLedgeItemBean bean;
    //发起者的附件列表
    private KnowLedgeItemBean.FilesKnowledgeItemDao filesHostDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesHostList;
    private KnowledgeItemHeadFilesListAdapter filesHostAdapter;

    //执行者添加详情
    private MessageTaskDetailBean.DetailAddDao addBean;
    private ArrayList<MessageTaskDetailBean.DetailAddDao> detailList;
    //private ArrayList<MessageTaskDetailAddBean> addDetailList;
    private TaskDetailAddAdapter detailAddAdapter;

    //执行者添加附件
    private KnowLedgeItemBean.FilesKnowledgeItemDao filesAddDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesAddList;
    private KnowledgeItemHeadFilesListAdapter filesAddAdapter;


    private View.OnTouchListener touchListener;//屏蔽滑动事件的监听器

    @Override
    public void initData() {
        back.setVisibility(View.VISIBLE);
        title.setText("任务详情");

        touchListener = new View.OnTouchListener() {
            //屏蔽掉滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        };

        //发起者的附件列表
        if (filesHostList == null) {
            filesHostList = new ArrayList<>();
        }
        filesHostList.clear();

        if (filesHostAdapter == null) {
            filesHostAdapter = new KnowledgeItemHeadFilesListAdapter(mActivity, filesHostList);
        }
        attachment.setAdapter(filesHostAdapter);
        attachment.setOnTouchListener(touchListener);
        attachment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!filesAddList.get(position).isEditing) {
                    //不可编辑状态,就下载文件
                    DownLoadFilesUtils downLoad = new DownLoadFilesUtils();
                    String url = "";
                    url = filesHostList.get(position).file_url;
                    if (downLoad.downloadFile(mActivity, url)) {
                        filesAddList.get(position).file_path = url.substring(url.lastIndexOf("/") + 1);//不包含 (/)线
                        filesAddAdapter.notifyDataSetChanged();
                    }
                } else {
                    MyToast.showShort(mActivity, "打开文件！");
                }
            }
        });


        //执行人添加详情的列表
        if (detailList == null) {
            detailList = new ArrayList<>();
        }
        detailList.clear();

        if (detailAddAdapter == null) {
            detailAddAdapter = new TaskDetailAddAdapter(mActivity, detail_list, detailList);
        }
        detail_list.setAdapter(detailAddAdapter);
        detail_list.setOnTouchListener(touchListener);

        //执行人添加附件的列表
        if (filesAddList == null) {
            filesAddList = new ArrayList<>();
        }
        filesAddList.clear();

        //添加新增附件条目
//        bean = new KnowLedgeItemBean();
//        filesAddDao = bean.new FilesKnowledgeItemDao();
//        filesAddDao.isAdd = true;
//        filesAddList.add(filesAddDao);


        if (filesAddAdapter == null) {
            filesAddAdapter = new KnowledgeItemHeadFilesListAdapter(mActivity, filesAddList);
        }
        files.setAdapter(filesAddAdapter);
        files.setOnTouchListener(touchListener);
        files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!filesAddList.get(position).isEditing) {
                    //不可编辑状态,就下载文件
                    DownLoadFilesUtils downLoad = new DownLoadFilesUtils();
                    String url = "";
                    url = filesAddList.get(position).file_url;
                    if (downLoad.downloadFile(mActivity, url)) {
                        filesAddList.get(position).file_path = url.substring(url.lastIndexOf("/") + 1);//不包含 (/)线
                        filesAddAdapter.notifyDataSetChanged();
                    }
                } else {
                    MyToast.showShort(mActivity, "打开文件！");
                }
            }
        });

        //滚动监听
        scroll.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                progress.correctOffsetWhenContainerOnScrolling();
            }
        });
        //高度改变的时候的监听
        scroll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                progress.correctOffsetWhenContainerOnScrolling();
            }
        });

        //设置数据
        getData();
    }


    /**
     * 从服务器获取数据
     */
    private void getData() {
        showProgressDialog();
        RequestParams params = new RequestParams(TestAddressUtils.test_get_message_task_detail_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                detailBean = GsonUtil.getMessageTaskDetailBean(result);
                LogUtil.e("获取到的条目-----------" + detailBean.result);
                if (detailBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);

                    setData();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到的条目--------失败!!!---" + ex);
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

    /**
     * 设置数据
     */
    private void setData() {

        detailDao = detailBean.data;
        taskDao = detailDao.executor;

        progress.setProgress(detailDao.degree);

        /*
        设置任务一个条目信息
         */

        if (taskDao.isOverdue) {
            task_overdue.setVisibility(View.VISIBLE);
        } else {
            task_overdue.setVisibility(View.GONE);
        }


        switch (taskDao.level) {
            case 1:
                task_level.setText("非常重要");
                task_level.setTextColor(Color.parseColor("#FF0101"));
                break;
            case 2:
                task_level.setText("重要");
                task_level.setTextColor(Color.parseColor("#02ED02"));
                break;
            case 3:
                task_level.setText("一般");
                task_level.setTextColor(Color.parseColor("#666666"));
                break;
            default:
                task_level.setText("");
                task_level.setTextColor(Color.parseColor("#666666"));
        }

        task_title.setText(taskDao.title);
        task_classify.setText(taskDao.classify);
        StringBuffer name = new StringBuffer();
        for (int i = 0; i < taskDao.name.size(); i++) {
            name.append(taskDao.name.get(i).name);
            if (i + 1 != taskDao.name.size()) {
                name.append("、");
            }
        }
        task_name.setText(name);
        task_start.setText("开始时间:" + TimeUtils.getFormateTime(taskDao.start, "-", ":"));
        task_end.setText("结束时间:" + TimeUtils.getFormateTime(taskDao.end, "-", ":"));

        switch (taskDao.state) {
            case 1:
                task_state.setText("完成");
                task_state.setTextColor(Color.parseColor("#FBBB0E"));
                break;
            case 2:
                task_state.setText("进行中");
                task_state.setTextColor(Color.parseColor("#0156E2"));
                break;
//            case 3:
//                level.setText("新到");
//                level.setTextColor(Color.parseColor("#FF0000"));
//                break;
            default:
                task_state.setText("");
                task_state.setTextColor(Color.parseColor("#FF0000"));
        }
        //设置任务一个条目信息完毕
        detail.setText(detailDao.detail);

        detailList.addAll(detailDao.detail_list);

        filesHostList.addAll(detailDao.attachment_list);
        filesAddList.addAll(detailDao.attachment_list_add);


        if (!(filesHostList.size() > 0)) {
            attachment_show.setVisibility(View.GONE);
        } else {
            attachment_show.setVisibility(View.VISIBLE);
        }


        //增加一个添加条目
        //if (!(filesAddList.size() > 0)) {

//        bean = new KnowLedgeItemBean();
//        filesAddDao = bean.new FilesKnowledgeItemDao();
//
//        filesAddDao.isAdd = true;
//        filesAddList.add(filesAddList.size(), filesAddDao);
//}
        filesHostAdapter.notifyDataSetChanged();
        detailAddAdapter.notifyDataSetChanged();
        filesAddAdapter.notifyDataSetChanged();


    }

    @Event(value = {R.id.title_back
            , R.id.fragment_task_add_detail
            , R.id.fragment_task_add_files})
    private void taskDetailOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://添加详情条目
                mActivity.finish();
                break;
            case R.id.fragment_task_add_detail://添加详情条目
                //addBean = new MessageTaskDetailAddBean();
                detailBean = new MessageTaskDetailBean();
                addBean = detailBean.new DetailAddDao();
                addBean.isEditing = true;
                detailList.add(detailList.size(), addBean);
                detailAddAdapter.notifyDataSetChanged();
                LogUtil.e("正价的条目为" + detailList.size());
                //Utility.setListViewHeightBasedOnChildren(detail_list);


                break;

            case R.id.fragment_task_add_files://添加任务附件条目
                PhotoOrVideoUtils.doFiles(mActivity, this);//打开文件管理器意图

                break;
        }
    }

    /**
     * 选择附件的返回获取
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        String path = "";
        if (uri != null) {
            path = PhotoOrVideoUtils.getPath(mActivity, uri);
            bean = new KnowLedgeItemBean();
            filesAddDao = bean.new FilesKnowledgeItemDao();
            filesAddDao.file_path = path;
            filesAddDao.file_name = path.substring(path.lastIndexOf("/") + 1);//不包含 (/)线
            filesAddDao.isAdd = false;
            filesAddDao.isEditing = true;
            filesAddList.add(filesAddList.size(), filesAddDao);
            filesAddAdapter.notifyDataSetChanged();
            Utility.setListViewHeightBasedOnChildren(files);
        }


    }
}

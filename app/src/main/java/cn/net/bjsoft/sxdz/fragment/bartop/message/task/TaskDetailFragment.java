package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskDetailAddAdapter;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemHeadFilesListAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailAddBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.ChildrenListView;
import cn.net.bjsoft.sxdz.view.CircleImageView;

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
    private CircleImageView task_overdue;
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
    private ScrollView scroll;
    @ViewInject(R.id.fragment_task_detail_host)
    private TextView detail;
    @ViewInject(R.id.fragment_task_attachment)
    private ListView attachment;
    @ViewInject(R.id.fragment_task_detail_list)
    private ChildrenListView detail_list;
    @ViewInject(R.id.fragment_task_add_detail)
    private TextView add_detail;
    @ViewInject(R.id.fragment_task_progress)
    private BubbleSeekBar progress;
    @ViewInject(R.id.fragment_task_files)
    private ListView files;

    private KnowLedgeItemBean bean;
    //发起者的附件列表
    private KnowLedgeItemBean.FilesKnowledgeItemDao filesHostDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesHostList;
    private KnowledgeItemHeadFilesListAdapter filesHostAdapter;

    //执行者添加详情
    private MessageTaskDetailAddBean addBean;
    private ArrayList<MessageTaskDetailAddBean> addBeenList;
    private TaskDetailAddAdapter addAdapter;

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

        //执行人添加详情的列表
        if (addBeenList == null) {
            addBeenList = new ArrayList<>();
        }
        addBeenList.clear();

        if (addAdapter == null) {
            addAdapter = new TaskDetailAddAdapter(mActivity, detail_list, addBeenList);
        }
        detail_list.setAdapter(addAdapter);
        detail_list.setOnTouchListener(touchListener);

        //执行人添加附件的列表
        if (filesAddList == null) {
            filesAddList = new ArrayList<>();
        }
        filesAddList.clear();

        if (filesAddAdapter == null) {
            filesAddAdapter = new KnowledgeItemHeadFilesListAdapter(mActivity, filesAddList);
        }
        files.setAdapter(filesAddAdapter);
        files.setOnTouchListener(touchListener);
        files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (filesAddList.size() - 1)) {
                    //添加附件动作
                    PhotoOrVideoUtils.doFiles(mActivity, TaskDetailFragment.this);//打开文件管理器意图
                } else {
                    MyToast.showShort(mActivity, "打开文件！");
                }
            }
        });

        //设置数据
        setData();
    }


    private void setData() {
        getData();
        if (!(filesAddList.size() > 0)) {
            bean = new KnowLedgeItemBean();
            filesAddDao = bean.new FilesKnowledgeItemDao();

            filesAddDao.isAdd = true;
            filesAddList.add(filesAddList.size(), filesAddDao);
        }

    }

    /**
     * 从服务器获取数据
     */
    private void getData() {

    }


    @Event(value = {R.id.title_back
            , R.id.fragment_task_add_detail})
    private void taskDetailOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://添加详情条目
                mActivity.finish();
                break;
            case R.id.fragment_task_add_detail://添加详情条目
                progress.correctOffsetWhenContainerOnScrolling();
                addBean = null;
                addBean = new MessageTaskDetailAddBean();
                addBean.isEditing = true;
                addBeenList.add(addBean);
                addAdapter.notifyDataSetChanged();
                //Utility.setListViewHeightBasedOnChildren(detail_list);

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
            filesAddList.add(filesAddList.size() - 1, filesAddDao);
            filesAddAdapter.notifyDataSetChanged();
            Utility.setListViewHeightBasedOnChildren(files);
        }


    }
}

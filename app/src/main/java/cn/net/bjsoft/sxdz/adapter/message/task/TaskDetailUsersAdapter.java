package cn.net.bjsoft.sxdz.adapter.message.task;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataFilesBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataUsersBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataUsersPlanBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.view.ChildrenListView;

public class TaskDetailUsersAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private BaseFragment mFragment;
    private ArrayList<MessageTaskDetailDataUsersBean> usersBeen;
    private UsersInforUtils usersInfor;
    private View.OnTouchListener touchListener;//屏蔽滑动事件的监听器

    public TaskDetailUsersAdapter(FragmentActivity mActivity
                                  ,BaseFragment mFragment
            , ArrayList<MessageTaskDetailDataUsersBean> usersBeen) {
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.usersBeen = usersBeen;
        usersInfor = UsersInforUtils.getInstance(mActivity);
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
    }


    @Override
    public int getCount() {
        return usersBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return usersBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.item_task_detail_users, null);
            viewHolder = new ViewHolder();


            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.item_task_detail_name);
            viewHolder.detail_list = (ChildrenListView) convertView
                    .findViewById(R.id.item_task_detail_list);
            viewHolder.add_detail = (TextView) convertView
                    .findViewById(R.id.item_task_add_detail);
            viewHolder.progress = (BubbleSeekBar) convertView
                    .findViewById(R.id.item_task_progress);
            viewHolder.task_files = (ChildrenListView) convertView
                    .findViewById(R.id.item_task_files);
            viewHolder.add_files = (TextView) convertView
                    .findViewById(R.id.item_task_add_files);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //viewHolder.progress.setTag(position, "progress");//设置tag



        viewHolder.name.setText(usersInfor.getUserInfo(usersBeen.get(position).id).nickname);

        viewHolder.progress.setProgress(Float.parseFloat(usersBeen.get(position).progress));

        final ViewHolder finalViewHolder = viewHolder;
        ((ListView)parent).setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                finalViewHolder.progress.correctOffsetWhenContainerOnScrolling();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if (usersBeen.get(position).finished.equals("true")) {
            viewHolder.add_detail.setFocusable(false);
            viewHolder.add_files.setFocusable(false);
        }

        //////////////////////////////////任务详情///////////////////////////////////


        if (usersBeen.get(position).plan != null) {
            viewHolder.usersPlanBeenList = usersBeen.get(position).plan;

            if (viewHolder.planAddAdapter == null) {
                viewHolder.planAddAdapter = new TaskDetailAddAdapter_new(mActivity, viewHolder.usersPlanBeenList);
            }
            viewHolder.detail_list.setAdapter(viewHolder.planAddAdapter);
        }else {

        }
        viewHolder.detail_list.setOnTouchListener(touchListener);
        viewHolder.add_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageTaskDetailDataUsersPlanBean detailBean = new MessageTaskDetailDataUsersPlanBean();
                detailBean.isEditing = true;
                usersBeen.get(position).plan.add(usersBeen.get(position).plan.size(), detailBean);
//                Utility.setListViewHeightBasedOnChildren(viewHolder.detail_list);
//                viewHolder.planAddAdapter.notifyDataSetChanged();
            }
        });


        ////////////////////////////////////////////////////////////////

        ArrayList<MessageTaskDetailDataFilesBean> usersFilesBeenList = null;//执行者上传的附件
        KnowledgeItemHeadFilesListAdapter_new filesAddAdapter = null;

        if (usersBeen.get(position).files != null) {
            usersFilesBeenList = usersBeen.get(position).files;

            if (filesAddAdapter == null) {
                filesAddAdapter = new KnowledgeItemHeadFilesListAdapter_new(mActivity, usersFilesBeenList);
            }
            viewHolder.task_files.setAdapter(filesAddAdapter);
        }else {

        }
        viewHolder.task_files.setOnTouchListener(touchListener);

        viewHolder.add_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoOrVideoUtils.doFiles(null, mFragment);//打开文件管理器意图

            }
        });




//        viewHolder.task_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (!usersFilesBeenList.get(position).isEditing) {
//                    //不可编辑状态,就下载文件
//                    DownLoadFilesUtils downLoad = new DownLoadFilesUtils();
//                    String url = "";
//                    url = usersFilesBeenList.get(position).url;
//                    if (url.equals("")) {
//                        if (downLoad.downloadFile(mActivity, url)) {
//                            usersFilesBeenList.get(position).url = url.substring(url.lastIndexOf("/") + 1);//不包含 (/)线
//                            filesAddAdapter.notifyDataSetChanged();
//                        }
//                    } else {
//                        MyToast.showShort(mActivity, "打开文件！");
//                    }
//                } else {
//                    MyToast.showShort(mActivity, "下载路径不能为空!");
//                }
//            }
//        });


        return convertView;
    }

    private final class ViewHolder {
        public ArrayList<MessageTaskDetailDataUsersPlanBean> usersPlanBeenList = null;//执行者上传的附件
        public TaskDetailAddAdapter_new planAddAdapter = null;
        public BubbleSeekBar progress;
        public ChildrenListView detail_list, task_files;
        public TextView name, add_detail, add_files;

    }

}

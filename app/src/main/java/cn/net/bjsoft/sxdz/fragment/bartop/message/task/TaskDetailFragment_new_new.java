package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.message.task.KnowledgeItemHeadFilesListAdapter_new;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskDetailUsersAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailBeanNew;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataFilesBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataUsersBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.DownLoadFilesUtils;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.ChildrenListView;

/**
 * Created by Zrzc on 2017/4/5.
 */

@ContentView(R.layout.fragment_task_detail_new_new)
public class TaskDetailFragment_new_new extends BaseFragment {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;


    private View headView;
    //@ViewInject(R.id.task_item_title)
    private TextView headView_task_title;
    //@ViewInject(R.id.task_item_overdue)
    private ImageView headView_task_overdue;
    //@ViewInject(R.id.task_item_classify_root)
    private RelativeLayout headView_task_classify_root;
    //@ViewInject(R.id.task_item_classify)
    private TextView headView_task_classify;
    // @ViewInject(R.id.task_item_name)
    private TextView headView_task_name;
    // @ViewInject(R.id.task_item_start)
    private TextView headView_task_start;
    //@ViewInject(R.id.task_item_end)
    private TextView headView_task_end;
    //@ViewInject(R.id.task_item_state)
    private TextView headView_task_state;
    //@ViewInject(R.id.task_item_level)
    private TextView headView_task_level;
    //@ViewInject(R.id.fragment_task_detail_host)
    private TextView headView_detail;
    //@ViewInject(R.id.fragment_task_attachment_show)
    private LinearLayout headView_attachment_show;
    //@ViewInject(R.id.fragment_task_attachment)
    private ChildrenListView headView_attachment;//任务附件


    @ViewInject(R.id.fragment_task_detail_users)
    private ListView users_list;


    private View footView;
    //@ViewInject(R.id.fragment_task_add_submit)
    private TextView footView_submit;

//    @ViewInject(R.id.fragment_task_detail_list)
//    private ChildrenListView detail_list;
//    @ViewInject(R.id.fragment_task_add_detail)
//    private TextView add_detail;
//    @ViewInject(R.id.fragment_task_progress)
//    private BubbleSeekBar progress;
//    @ViewInject(R.id.fragment_task_files)
//    private ChildrenListView files;//执行人新增附件
//    @ViewInject(R.id.fragment_task_add_files)
//    private TextView add_files;


    ////////////////////////////////////数据相关
    private PostJsonBean pushMessageBean;
    private String task_id = "";

    private MessageTaskDetailBeanNew taskDetailBean;
    private MessageTaskDetailDataBean dataBean;


    ///////////////////////////////发起者任务附件/////////////////////////////////
    private ArrayList<MessageTaskDetailDataFilesBean> filesHostList;
    private KnowledgeItemHeadFilesListAdapter_new filesHostAdapter;


    ///////////////////////////////执行者条目/////////////////////////////////
    private ArrayList<MessageTaskDetailDataUsersBean> usersBeenList;
    private TaskDetailUsersAdapter usersAdapter;
//    private ArrayList<MessageTaskDetailDataFilesBean> usersFilesBeenList;//执行者上传的附件
//    private KnowledgeItemHeadFilesListAdapter_new filesAddAdapter;
//    private ArrayList<MessageTaskDetailDataUsersPlanBean> usersPlanBeenList;//执行者添加的详情
//    private TaskDetailAddAdapter_new detailAddAdapter;


    private boolean isEdited = false;

    private View.OnTouchListener touchListener;//屏蔽滑动事件的监听器

    @Override
    public void initData() {
        back.setVisibility(View.VISIBLE);
        title.setText("任务详情");


        pushMessageBean = new PostJsonBean();

        Bundle bundle = getArguments().getBundle("isEdited");
        if (bundle != null) {
            isEdited = bundle.getBoolean("isEdited");
            task_id = bundle.getString("task_id");
        }

        //TODO 初始化页面的时候,或者获取到数据的时候,如果姓名中没有我,或者id不是用户id,那么就是不可编辑状态

        if (!isEdited) {
            footView_submit.setVisibility(View.GONE);
//            add_detail.setVisibility(View.GONE);
//            add_files.setVisibility(View.GONE);
        }


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


        inflateHeadView();
        inflateFootView();
        initList();

        //设置数据
        //getData();
        getDataTest();
    }

    private void inflateHeadView() {
        headView = View.inflate(mActivity, R.layout.view_head_task_detail, null);
        headView_task_title = (TextView) headView.findViewById(R.id.task_item_title);
        headView_task_overdue = (ImageView) headView.findViewById(R.id.task_item_overdue);
        headView_task_classify_root = (RelativeLayout) headView.findViewById(R.id.task_item_classify_root);
        headView_task_name = (TextView) headView.findViewById(R.id.task_item_name);
        headView_task_start = (TextView) headView.findViewById(R.id.task_item_start);
        headView_task_end = (TextView) headView.findViewById(R.id.task_item_end);
        headView_task_state = (TextView) headView.findViewById(R.id.task_item_state);
        headView_task_level = (TextView) headView.findViewById(R.id.task_item_level);
        headView_detail = (TextView) headView.findViewById(R.id.fragment_task_detail_host);
        headView_attachment_show = (LinearLayout) headView.findViewById(R.id.fragment_task_attachment_show);
        headView_attachment = (ChildrenListView) headView.findViewById(R.id.fragment_task_attachment);


        users_list.addHeaderView(headView);
    }

    private void inflateFootView() {
        footView = View.inflate(mActivity, R.layout.view_foot_task_detail, null);
        footView_submit = (TextView) footView.findViewById(R.id.fragment_task_add_submit);

        users_list.addFooterView(footView);
    }


    private void getDataTest() {
        String s = "{\"code\":0,\"data\":{\"id\":\"4658686682444083412\",\"title\":\"BIIP界面设计\",\"userid\":10001,\"ctime\":\"\\/Date(1493257553519)\\/\",\"type\":\"日常任务\",\"finished\":false,\"finishtime\":\"\\/Date(-62135596800000)\\/\",\"shared\":false,\"progress\":20,\"starttime\":\"\\/Date(1493261629922)\\/\",\"limittime\":\"\\/Date(1493261629922)\\/\",\"hours\":0,\"message\":\"\",\"priority\":\"紧急\",\"priority_color\":null,\"description\":\"\",\"files\":[{\"id\":null,\"title\":\"\",\"ctime\":\"\\/Date(-62135596800000)\\/\",\"url\":\"\"}],\"users\":[{\"position\":false,\"id\":\"10001\",\"progress\":80,\"submited\":false,\"submittime\":\"\\/Date(-62135596800000)\\/\",\"finished\":false,\"finishtime\":\"\\/Date(-62135596800000)\\/\",\"evaluate\":\"\",\"files\":[{\"id\":null,\"title\":\"\",\"ctime\":\"\\/Date(-62135596800000)\\/\",\"url\":\"\"}],\"plan\":[{\"id\":null,\"title\":\"第一阶段\",\"content\":\"测试工作计划\",\"startime\":\"\\/Date(-62135596800000)\\/\",\"endtime\":\"\\/Date(-62135596800000)\\/\",\"finished\":true,\"finishtime\":\"\\/Date(-62135596800000)\\/\"}]}]},\"msg\":null}\n";
        taskDetailBean = GsonUtil.getMessageTaskDetailBeanNew(s);
        if (taskDetailBean.code.equals("0")) {
            dataBean = taskDetailBean.data;
            fomateDataDate();
            setData();
//                    dataDaos.addAll(messageBean.data.items);
//                    message_Start = dataDaos.size()+"";//设置开始查询
//                    message_count = messageBean.data.count+"";
//                    messageAdapter.notifyDataSetChanged();
        } else {
            MyToast.showLong(mActivity, "获取消息失败");
        }

    }

    private void initList() {

        //发起者的附件列表
        if (filesHostList == null) {
            filesHostList = new ArrayList<>();
        }
        filesHostList.clear();
        if (filesHostAdapter == null) {
            filesHostAdapter = new KnowledgeItemHeadFilesListAdapter_new(mActivity, filesHostList);
        }
        headView_attachment.setAdapter(filesHostAdapter);

        headView_attachment.setOnTouchListener(touchListener);
        headView_attachment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!filesHostList.get(position).isEditing) {
                    //不可编辑状态,就下载文件
                    DownLoadFilesUtils downLoad = new DownLoadFilesUtils();
                    String url = "";
                    url = filesHostList.get(position).url;
                    if (url.equals("")) {
                        if (downLoad.downloadFile(mActivity, url)) {
                            filesHostList.get(position).url = url.substring(url.lastIndexOf("/") + 1);//不包含 (/)线
                            filesHostAdapter.notifyDataSetChanged();
                        }
                    } else {
                        MyToast.showShort(mActivity, "打开文件！");
                    }
                } else {
                    MyToast.showShort(mActivity, "下载路径不能为空!");
                }
            }
        });


        //////////////////////执行者相关/////////////////////////
        if (usersBeenList == null) {
            usersBeenList = new ArrayList<>();
        }
        usersBeenList.clear();

        if (usersAdapter == null) {
            usersAdapter = new TaskDetailUsersAdapter(mActivity, this, usersBeenList);
        }
        users_list.setAdapter(usersAdapter);


        //----------------------------------

//        if (usersFilesBeenList == null) {
//            usersFilesBeenList = new ArrayList<>();
//        }
//        usersFilesBeenList.clear();
//        if (filesAddAdapter == null) {
//            filesAddAdapter = new KnowledgeItemHeadFilesListAdapter_new(mActivity, usersFilesBeenList);
//        }
//        files.setAdapter(filesAddAdapter);
//        files.setOnTouchListener(touchListener);
//
//        files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
//
//
//        if (usersPlanBeenList == null) {
//            usersPlanBeenList = new ArrayList<>();
//        }
//        usersPlanBeenList.clear();
//        if (detailAddAdapter == null) {
//            detailAddAdapter = new TaskDetailAddAdapter_new(mActivity, usersPlanBeenList);
//        }
//
//        detail_list.setAdapter(detailAddAdapter);
//        detail_list.setOnTouchListener(touchListener);
    }


    /**
     * 从服务器获取数据
     */
    private void getData() {

        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "task_look");
        //这两个字段加上去就必须有值
        pushMessageBean.start = "0";
        pushMessageBean.limit = "10";
        pushMessageBean.data.task_id = task_id;//设置开始查询
        params.addBodyParameter("data", pushMessageBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushMessageBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------任务详情------获取消息----------------" + strJson);
                //strJson = fomateString(strJson);
                LogUtil.e("-----------------任务详情------获取消息----------------" + strJson);
                taskDetailBean = GsonUtil.getMessageTaskDetailBeanNew(strJson);
                if (taskDetailBean.code.equals("0")) {
                    dataBean = taskDetailBean.data;
                    fomateDataDate();
                    setData();
//                    dataDaos.addAll(messageBean.data.items);
//                    message_Start = dataDaos.size()+"";//设置开始查询
//                    message_count = messageBean.data.count+"";
//                    messageAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showLong(mActivity, "获取消息失败");
                }

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


    }

    private String fomateString(String json) {
        json = json.replace("null", "\"\"");
        return json;

    }

    /**
     * 格式化数据
     */
    private void fomateDataDate() {
        String time = "";
        time = dataBean.ctime;
        time = time.replace("/Date(", "");
        time = time.replace(")/", "");
        dataBean.ctime = time;

        time = dataBean.finishtime;
        time = time.replace("/Date(", "");
        time = time.replace(")/", "");
        dataBean.finishtime = time;

        time = dataBean.limittime;
        time = time.replace("/Date(", "");
        time = time.replace(")/", "");
        dataBean.limittime = time;

        time = dataBean.starttime;
        time = time.replace("/Date(", "");
        time = time.replace(")/", "");
        dataBean.starttime = time;

    }

    /**
     * 设置数据
     */
    private void setData() {
        //任务头
        headView_task_title.setText(dataBean.title);
        headView_task_classify_root.setVisibility(View.GONE);
        headView_task_name.setText(UsersInforUtils.getInstance(mActivity).getUserInfo(dataBean.userid).nickname);
        headView_task_start.setText("开始时间:" + TimeUtils.getFormateTime(Long.parseLong(dataBean.starttime), "-", ":"));
        headView_task_end.setText("结束时间:" + TimeUtils.getFormateTime(Long.parseLong(dataBean.ctime), "-", ":"));
        headView_task_level.setText(dataBean.priority);
        if (!TextUtils.isEmpty(dataBean.priority_color)) {
            headView_task_level.setTextColor(Color.parseColor("#" + dataBean.priority_color));
        }

        if (dataBean.finished) {
            headView_task_state.setText("已完成");
            footView_submit.setVisibility(View.GONE);
//            progress.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });

        } else {
            headView_task_state.setText("进行中");
            footView_submit.setVisibility(View.VISIBLE);
        }

        //任务描述
        headView_detail.setText(dataBean.description);


        //任务进度
        //progress.setProgress(Float.parseFloat(dataBean.progress));


        //任务附件
        if (dataBean.files != null) {
            filesHostList.addAll(dataBean.files);
            filesHostAdapter.notifyDataSetChanged();
        }
        if (!(filesHostList.size() > 0)) {
            headView_attachment_show.setVisibility(View.GONE);
        }

        if (dataBean.users != null) {
            usersBeenList.addAll(dataBean.users);
        }


//
//        //执行人相关
//        if (dataBean.users != null) {
//            usersBeenList.addAll(dataBean.users);
//
//            for (MessageTaskDetailDataUsersBean usersBean : usersBeenList) {
//                if (usersBean.files != null) {
//                    usersFilesBeenList.addAll(usersBean.files);
//                }
//                if (usersBean.plan != null) {
//                    usersPlanBeenList.addAll(usersBean.plan);
//                }
//            }
//        }
    }

    //    @Event(value = {R.id.title_back
//            , R.id.fragment_task_add_detail
//            , R.id.fragment_task_add_files
//            , R.id.fragment_task_add_submit})
    private void taskDetailOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://添加详情条目
                mActivity.finish();
                break;
            case R.id.fragment_task_add_detail://添加详情条目
                //addBean = new MessageTaskDetailAddBean();
//                MessageTaskDetailDataUsersPlanBean detailBean = new MessageTaskDetailDataUsersPlanBean();
//                detailBean.isEditing = true;
//                usersPlanBeenList.add(usersPlanBeenList.size(), detailBean);
//                Utility.setListViewHeightBasedOnChildren(detail_list);
//                detailAddAdapter.notifyDataSetChanged();
//                LogUtil.e("正价的条目为" + usersPlanBeenList.size());


                //Utility.setListViewHeightBasedOnChildren(detail_list);


                break;

            case R.id.fragment_task_add_files://添加任务附件条目

                PhotoOrVideoUtils.doFiles(null, TaskDetailFragment_new_new.this);//打开文件管理器意图


                break;

            case R.id.fragment_task_add_submit://添加任务附件条目
                submitToServicer();


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

        if (data != null) {
            Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
            String path = "";
            LogUtil.e("onActivityResult-----uri" + uri);
            if (uri != null) {
                path = PhotoOrVideoUtils.getPath(mActivity, uri);
                LogUtil.e("onActivityResult-----path" + path);

                MessageTaskDetailDataFilesBean filesAddDao = new MessageTaskDetailDataFilesBean();
                filesAddDao.url = path;
                filesAddDao.title = path.substring(path.lastIndexOf("/") + 1);//不包含 (/)线
                filesAddDao.isAdd = false;
                filesAddDao.isEditing = true;
//                usersBeenList.add(usersBeenList.size(), filesAddDao);
                usersAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(users_list);

            }
        }


    }

    /**
     * 提交数据到服务器
     */
    private void submitToServicer() {


        mActivity.finish();
    }
}

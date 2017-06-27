package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskAddAddressListAdapter;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemHeadFilesListAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataFilesBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailTypesBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskPushAddBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.dialog.PickerDialog;
import cn.net.bjsoft.sxdz.dialog.SideRightPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.ReadFile;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListBean;

import static cn.net.bjsoft.sxdz.utils.UrlUtil.api_base;

/**
 * Created by Zrzc on 2017/4/6.
 */
@ContentView(R.layout.fragment_task_new)
public class TopAddTaskFragment extends BaseFragment {
    @ViewInject(R.id.title_title)
    private TextView title;
    @ViewInject(R.id.title_back)
    private ImageView back;

    @ViewInject(R.id.pop_position)
    private RelativeLayout pop_position;

    @ViewInject(R.id.fragment_task_new_name)
    private EditText new_name;
    @ViewInject(R.id.fragment_task_new_data)
    private EditText new_data;
    @ViewInject(R.id.fragment_task_new_message)
    private EditText new_message;
    @ViewInject(R.id.fragment_task_new_discription)
    private EditText new_discription;
    @ViewInject(R.id.fragment_task_new_files)
    private ListView new_files;
    @ViewInject(R.id.fragment_task_new_classify)
    private TextView new_classify;
    @ViewInject(R.id.fragment_task_new_classify_show)
    private RelativeLayout new_classify_show;
    @ViewInject(R.id.fragment_task_new_leave)
    private TextView new_leave;
    @ViewInject(R.id.fragment_task_new_level_show)
    private RelativeLayout banew_level_showck;
    @ViewInject(R.id.fragment_task_new_add_humen)
    private RelativeLayout new_add_humen;
    @ViewInject(R.id.fragment_task_new_humens)
    private ListView new_humens;
    @ViewInject(R.id.fragment_task_new_submit)
    private TextView new_submit;

    String type_url = "";
    private SideRightPopupWindow typePopupWindow;
    private ArrayList<String> typeStrList;
    private SideRightPopupWindow levelPopupWindow;
    private ArrayList<String> levelStrList;


    private KnowLedgeItemBean bean;
    private KnowLedgeItemBean.FilesKnowledgeItemDao filesAddDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesAddList;
    private KnowledgeItemHeadFilesListAdapter filesAddAdapter;

    private ArrayList<String> nodeId;
    private ArrayList<String> nodeAvatar;
    private ArrayList<String> nodeName;
    private ArrayList<String> nodeDepartment;

    private ArrayList<TreeTaskAddAddressListBean.TreeTaskAddAddressListDao> humenList;
    private TaskAddAddressListAdapter adapter;

    private AdapterView.OnItemClickListener itemClickListener;
    private View.OnTouchListener onTouchListener;

    private static final int ADD_ADDRESS_LIST = 1000;


    @Event(value = {R.id.title_back
            , R.id.fragment_task_new_data
            , R.id.fragment_task_new_classify_show
            , R.id.fragment_task_new_level_show
            , R.id.fragment_task_new_add_humen
            , R.id.fragment_task_new_submit})
    private void onClick(View view) {


        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;
            case R.id.fragment_task_new_data://设置时间
                PickerDialog.showDatePickerDialog(mActivity, new_data, "-");
                break;
            case R.id.fragment_task_new_classify_show://设置任务分类
                /**
                 * 从后台获取类别,现在
                 */
//                typeStrList.clear();//先清空
//                typeStrList.add("生活");
//                typeStrList.add("体育");
//                typeStrList.add("音乐");
//                typeStrList.add("近期热门");
//                typeStrList.add("科技");
//                typeStrList.add("健身");

                typePopupWindow.showWindow(typeStrList);
                break;
            case R.id.fragment_task_new_level_show://设置任务性质--等级
                /**
                 * 从后台获取类别,现在
                 */

                levelStrList.clear();
                levelStrList.add("重要");
                levelStrList.add("非常重要");
                levelStrList.add("一般");

                levelPopupWindow.showWindow(levelStrList);
                break;
            case R.id.fragment_task_new_add_humen://添加执行者
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name", "TopAddTaskAddressListFragment");
                startActivityForResult(intent, ADD_ADDRESS_LIST);


                break;
            case R.id.fragment_task_new_submit://提交
                //MyToast.showShort(mActivity, "提交任务到服务器");

                submit2Service();


                break;
        }
    }


    @Override
    public void initData() {

        //添加附件暂时不需要
        new_files.setVisibility(View.GONE);

        title.setText("新建任务");
        back.setVisibility(View.VISIBLE);

        //联系人相关开始

        if (humenList == null) {
            humenList = new ArrayList<>();
        }
        humenList.clear();

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

        if (adapter == null) {
            adapter = new TaskAddAddressListAdapter(mActivity, humenList);
        }
        new_humens.setAdapter(adapter);
        //联系人相关结束
        /**
         * 分类---性质侧拉框相关
         */
        if (typeStrList == null) {
            typeStrList = new ArrayList<>();
        }
        typeStrList.clear();
        typePopupWindow = new SideRightPopupWindow(mActivity, pop_position);
        typePopupWindow.setOnData(new SideRightPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                new_classify.setText(result);
            }
        });

        if (levelStrList == null) {
            levelStrList = new ArrayList<>();
        }
        levelStrList.clear();
        levelPopupWindow = new SideRightPopupWindow(mActivity, pop_position);
        levelPopupWindow.setOnData(new SideRightPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                new_leave.setText(result);
            }
        });

        //侧拉狂结束

        //附件相关
        if (filesAddList == null) {
            filesAddList = new ArrayList<>();
        }
        filesAddList.clear();
        bean = new KnowLedgeItemBean();

        filesAddDao = bean.new FilesKnowledgeItemDao();
        filesAddDao.isAdd = true;
        filesAddList.add(filesAddDao);

        if (filesAddAdapter == null) {
            filesAddAdapter = new KnowledgeItemHeadFilesListAdapter(mActivity, filesAddList);
        }

        new_files.setAdapter(filesAddAdapter);
        //附件相关结束

        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filesAddList.get(position).isAdd) {//是新增的,就执行添加附件
                    PhotoOrVideoUtils.doFiles(mActivity, TopAddTaskFragment.this);
                } else {

                }
            }
        };


        onTouchListener = new View.OnTouchListener() {
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


        new_files.setOnItemClickListener(itemClickListener);
        new_files.setOnTouchListener(onTouchListener);

        type_url = api_base + "/apps/" + SPUtil.getAppid(mActivity) + "/task_type.json";
        getTypes();
    }

    /**
     * 获取任务类别
     */
    private void getTypes() {
        showProgressDialog();


        HttpPostUtils httpPostUtils = new HttpPostUtils();
        httpPostUtils.get(mActivity, new RequestParams(type_url));
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                //result = "{\"code\":1,\"data\":null,\"msg\":\"unauthorized\"}";
                MessageTaskDetailTypesBean typesBean =  GsonUtil.getMessageTaskDetailTypesBean(strJson);
                typeStrList.clear();
                if (typesBean.code.equals("0")) {
                    for (MessageTaskDetailTypesBean.MessageTaskDetailTypesTypeDataBean type : typesBean.data.types) {
                        typeStrList.add(type.type);
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dismissProgressDialog();
                //当服务器没有类别文件时,就加载app本地的
                type_url = ReadFile.getFromAssets(mActivity, "json/task_type.json");
                getTypes();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == ADD_ADDRESS_LIST) {
                //获取到选择的联系人
                Bundle bundle = data.getExtras();
//                nodeId.addAll(bundle.getStringArrayList("nodeId"));
//                nodeAvatar.addAll(bundle.getStringArrayList("nodeAvatar"));
//                nodeName.addAll(bundle.getStringArrayList("nodeName"));
//                nodeDepartment.addAll(bundle.getStringArrayList("nodeDepartment"));

                String userId = bundle.getString("nodeId");
                String userAvatar = bundle.getString("nodeAvatar");
                String userName = bundle.getString("nodeName");
                String userDepartment = bundle.getString("nodeDepartment");
                TreeTaskAddAddressListBean bean = new TreeTaskAddAddressListBean();
                TreeTaskAddAddressListBean.TreeTaskAddAddressListDao dao = bean.new TreeTaskAddAddressListDao();
                dao.id = userId;
                dao.avatar = userAvatar;
                dao.name = userName;
                dao.department = userDepartment;
                humenList.add(dao);

//                for (int i = 0; i < nodeId.size(); i++) {
//                    TreeTaskAddAddressListBean.TreeTaskAddAddressListDao dao = bean.new TreeTaskAddAddressListDao();
//                    dao.id = nodeId.get(i);
//                    dao.avatar = nodeAvatar.get(i);
//                    dao.name = nodeName.get(i);
//                    dao.department = nodeDepartment.get(i);
//                    humenList.add(dao);
//                }
//                nodeId.clear();
//                nodeAvatar.clear();
//                nodeName.clear();
//                nodeDepartment.clear();
                adapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(new_humens);

                LogUtil.e("返回结果===========" + nodeId.size() + nodeName.size() + nodeDepartment.size());


            } else {//如果是选择附件---则上传

                Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
                if (uri != null) {
                    String path = PhotoOrVideoUtils.getPath(mActivity, uri);
                    upLoadFile(path);

                }
            }
        }
    }


    private void submit2Service() {


        String title = new_name.getText().toString().trim();
        Long subTime = System.currentTimeMillis();
        String time = new_data.getText().toString().trim();
        String message = new_message.getText().toString().trim();
        String discription = new_discription.getText().toString().trim();
        String classify = new_classify.getText().toString().trim();
        String leave = new_leave.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            MyToast.showShort(mActivity, "请输入标题");
            return;
        }

        if (TextUtils.isEmpty(time)) {
            MyToast.showShort(mActivity, "请添加截止时间");
            return;
        }

        if (TextUtils.isEmpty(discription)) {
            MyToast.showShort(mActivity, "请添加任务描述");
            return;
        }

        if (TextUtils.isEmpty(classify)) {
            MyToast.showShort(mActivity, "请添加任务分类");
            return;
        }

        if (TextUtils.isEmpty(leave)) {
            MyToast.showShort(mActivity, "请添加任务性质");
            return;
        }

        if (!(humenList.size() > 0)) {
            MyToast.showShort(mActivity, "请至少添加一个任务执行者");
            return;
        }

        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "task_save");

        MessageTaskPushAddBean pushData = new MessageTaskPushAddBean();

        pushData.title = title;
        pushData.type = classify;//类别
        pushData.message = message;
        pushData.description = discription;
        pushData.starttime = TimeUtils.getFormateTime(subTime, "-", ":");
        pushData.limittime = TimeUtils.getFormateTime(Long.parseLong(TimeUtils.getDateStamp(time, "-")), "-", ":");
        pushData.priority = leave;

        for (int i = 0; i < filesAddList.size(); i++) {
            MessageTaskDetailDataFilesBean filesBean = new MessageTaskDetailDataFilesBean();
            filesBean.title = filesAddList.get(i).file_name;
            filesBean.url = filesAddList.get(i).file_url;
            filesBean.ctime = TimeUtils.getFormateTime(subTime, "-", ":");

            pushData.files.add(filesBean);
        }

        //联系人暂时未添加
        for (int i = 0; i < humenList.size(); i++) {
            pushData.userIds.add(humenList.get(i).id.replace(".0", ""));
        }
//        MessageTaskDetailDataUsersBean user = new MessageTaskDetailDataUsersBean();
//        user.id = "12341";
//        user.userid = "12341";
//        MessageTaskDetailDataUsersBean user1 = new MessageTaskDetailDataUsersBean();
//        user1.userid = "10001";
//        pushData.users.add(user);
//        pushData.users.add(user1);
        pushData.id = "";//新建任务---id应该为空
        params.addBodyParameter("data", pushData.toString());


        LogUtil.e("-------------------------bean.toString()" + pushData.toString());
        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------任务详情------上传获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {

                        MyToast.showShort(mActivity, "提交成功!");

                    } else {
                        MyToast.showLong(mActivity, "提交任务失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.finish();
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


    /**
     * 上传文件到服务器
     *
     * @param filePath
     */
    private void upLoadFile(final String filePath) {
        showProgressDialog();
        RequestParams params = new RequestParams(SPUtil.getApiUpload(mActivity));
        params.setMultipart(true);
        File file = new File(filePath);

        params.addBodyParameter("file", file);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传成功onSuccess" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("code") == 0) {

                        //返回路径名
                        String url = jsonObject.optJSONObject("data").optString("src");

//                        MessageTaskDetailDataFilesBean filesAddDao = new MessageTaskDetailDataFilesBean();
//                        filesAddDao.url = url;
//                        filesAddDao.title = imagePath.substring(imagePath.lastIndexOf("/") + 1);//不包含 (/)线
//                        filesAddDao.isAdd = false;
//                        filesAddDao.isEditing = true;
//                        usersFilesBeenList.add(usersFilesBeenList.size(), filesAddDao);
//                        filesAddAdapter.notifyDataSetChanged();
//                        Utility.setListViewHeightBasedOnChildren(files);


                        filesAddDao = bean.new FilesKnowledgeItemDao();
                        filesAddDao.isEditing = true;
                        filesAddDao.file_path = filePath;
                        filesAddDao.file_name = filePath.substring(filePath.lastIndexOf("/") + 1);//不包含 (/)线
                        filesAddDao.file_url = url;
                        filesAddList.add(filesAddList.size() - 1, filesAddDao);
                        filesAddAdapter.notifyDataSetChanged();
                        Utility.setListViewHeightBasedOnChildren(new_files);


                    } else {
                        MyToast.showLong(mActivity, "文件上传失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("上传失败onError" + ex);
                MyToast.showLong(mActivity, "上传头像失败,请联系管理员");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.e("上传onLoading--" + isDownloading + ":total:==" + total + ":current:==" + current);
            }
        });

    }
}

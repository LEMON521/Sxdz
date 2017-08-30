package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeNewPicturesAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowGroupBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowGroupDataItemsBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataFilesBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskPushAddBean;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersSingleBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeNewPictureBean;
import cn.net.bjsoft.sxdz.dialog.SideRightPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase16;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListBean;

/**
 * Created by Zrzc on 2017/4/12.
 */


@ContentView(R.layout.fragment_knowledge_new)
public class KnowledgeNewZDLFFragment extends BaseFragment {
    @ViewInject(R.id.title_title)
    private TextView title;
    @ViewInject(R.id.title_back)
    private ImageView back;

    @ViewInject(R.id.pop_position)
    private RelativeLayout pop_position;

    @ViewInject(R.id.knowledge_new_title)
    private EditText new_title;
    @ViewInject(R.id.knowledge_new_detail)
    private EditText new_detail;
    @ViewInject(R.id.knowledge_new_type_show)
    private TextView new_type_show;
    @ViewInject(R.id.knowledge_new_type)
    private RelativeLayout new_type;
    @ViewInject(R.id.knowledge_new_keyowrd)
    private EditText new_keyowrd;
    @ViewInject(R.id.knowledge_new_submit)
    private TextView new_submit;

    @ViewInject(R.id.knowledge_new_picture)
    private GridView new_picture;
    @ViewInject(R.id.knowledge_new_files)
    private ListView new_files;

    @ViewInject(R.id.knowledge_new_add_user)
    private ListView add_user;

    @ViewInject(R.id.knowledge_new_add)
    private RelativeLayout new_add;


    private KnowledgeNewZDLFFragment fragment;

    private SideRightPopupWindow rightPopupWindow;
    private ArrayList<String> typeStrList;

    private KnowledgeNewPictureBean picBean;
    private KnowledgeNewPicturesAdapter picAdapter;
    private ArrayList<KnowledgeNewPictureBean> picList;

    private KnowLedgeItemBean bean;
    private KnowLedgeItemBean.FilesKnowledgeItemDao filesAddDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesAddList;
    private KnowledgeItemHeadFilesListAdapter filesAddAdapter;

    private AdapterView.OnItemClickListener itemClickListener;
    private View.OnTouchListener onTouchListener;
    private final int REQUEST_CODE_PICTURE = 100;//拍照
    private final int REQUEST_CODE_FILES = 200;//从相片中获取照片
    private int selectType = -1;

    private static final int ADD_ADDRESS_LIST = 1000;
    private ArrayList<TreeTaskAddAddressListBean.TreeTaskAddAddressListDao> humenList;
    private TaskAddAddressListAdapter adapter;


    private KnowGroupBean knowGroupBean;//知识分类实体
    private ArrayList<KnowGroupDataItemsBean> groupDataList;//分组所在集合

    private String knowledge_type = "";


    @Event(value = {R.id.title_back
            , R.id.knowledge_new_type
            , R.id.knowledge_new_submit
            , R.id.knowledge_new_add})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mActivity.finish();
                break;
            case R.id.knowledge_new_type://调出侧拉框,选择所属类别
                /**
                 * 从后台获取类别,现在
                 */
                rightPopupWindow.showWindow(typeStrList);
                break;
            case R.id.knowledge_new_submit://发表,推送数据到服务器上
                submitToService();
                break;

            case R.id.knowledge_new_add:
                Intent intent = new Intent(mActivity, EmptyActivity.class);
                intent.putExtra("fragment_name", "TopAddTaskAddressListFragment");
                startActivityForResult(intent, ADD_ADDRESS_LIST);
                break;
        }
    }


    @Override
    public void initData() {
        title.setText("发布");
        back.setVisibility(View.VISIBLE);

        fragment = this;
        knowledge_type = getArguments().getString("knowledge_type");

        if (knowledge_type != null) {
            new_type_show.setText(knowledge_type);
        }
        //knowledge_type = getActivity().getIntent().getStringExtra("knowledge_type");

        //初始化组信息
        if (groupDataList == null) {
            groupDataList = new ArrayList<>();
        }
        groupDataList.clear();
        //分类
        if (typeStrList == null) {
            typeStrList = new ArrayList<>();
        }
        typeStrList.clear();

        //图片相关
        if (picList == null) {
            picList = new ArrayList<>();
        }
        picList.clear();
        picBean = new KnowledgeNewPictureBean();
        picList.add(picBean);

        if (picAdapter == null) {
            picAdapter = new KnowledgeNewPicturesAdapter(mActivity, picList);
        }

        new_picture.setAdapter(picAdapter);
        //图片相关结束

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
        if (humenList == null) {
            humenList = new ArrayList<>();
        } else {
            humenList.clear();
        }
        if (adapter == null) {
            adapter = new TaskAddAddressListAdapter(mActivity, humenList);
        }

        add_user.setAdapter(adapter);

        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.knowledge_new_picture:
                        if ((picList.size() - 1) == position) {//如果是最后一个条目,就是添加图片的动作
                            selectType = REQUEST_CODE_PICTURE;
                            PhotoOrVideoUtils.doPhoto(mActivity, fragment, new_submit);
                        }
                        break;
                    case R.id.knowledge_new_files:
                        if (filesAddList.get(position).isAdd) {//是新增的,就执行添加附件
                            selectType = REQUEST_CODE_FILES;
                            PhotoOrVideoUtils.doFiles(mActivity, fragment);
                        } else {
                        }
                        break;
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

        new_picture.setOnItemClickListener(itemClickListener);
        new_files.setOnItemClickListener(itemClickListener);
        new_picture.setOnTouchListener(onTouchListener);
        new_files.setOnTouchListener(onTouchListener);

        rightPopupWindow = new SideRightPopupWindow(mActivity, pop_position);
        rightPopupWindow.setOnData(new SideRightPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                new_type_show.setText(result);
            }
        });


        getGroupData();
    }

    /**
     * 获取分组数据
     */
    public void getGroupData() {

        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "shuxin_know_type");
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取文章分组----分组信息----------------" + strJson);
                knowGroupBean = GsonUtil.getKnowGroupBean(strJson);
                if (knowGroupBean.code.equals("0")) {//0的时候获取成功
                    //LogUtil.e("获取到的条目-----------" + result);
                    groupDataList.addAll(knowGroupBean.data.items);
                    setGroupData();
                } else if (knowGroupBean.code.equals("1")) {
                    MyToast.showShort(mActivity, "获取文章分组失败");
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

    /**
     * 设置分类
     */
    private void setGroupData() {

        typeStrList.clear();

        for (KnowGroupDataItemsBean bean : groupDataList) {
            typeStrList.add(bean.name);
        }

    }

    private String title_str = "";
    private String resuld_id = "";

    /**
     * 提交数据到服务器
     */
    private void submitToService() {
//        MyToast.showShort(mActivity, "提交到服务器");
        //mActivity.finish();
        title_str = new_title.getText().toString().trim();
        String discription = new_detail.getText().toString().trim();
        String type = new_type_show.getText().toString().trim();
        String keyowrd = new_keyowrd.getText().toString().trim();

        UsersSingleBean usersSingleBean = UsersInforUtils.getInstance(mActivity).getUserInfo(SPUtil.getUserId(mActivity));
        String author = "";
        if (usersSingleBean != null) {//防止没有 user 对象
            author = usersSingleBean.nickname;
        }


        Long subTime = System.currentTimeMillis();

        if (TextUtils.isEmpty(title_str)) {
            MyToast.showShort(mActivity, "请输入标题");
            return;
        }


        if (TextUtils.isEmpty(discription)) {
            MyToast.showShort(mActivity, "请输入将要发布的知识内容");
            return;
        }

        if (TextUtils.isEmpty(type)) {
            MyToast.showShort(mActivity, "请添加任务分类");
            return;
        }

        if (groupDataList.size() > 0) {
            for (KnowGroupDataItemsBean bean : groupDataList) {
                if (bean.name.equals(type)) {
                    type = bean.id;
                    break;
                }
            }
        } else {
            groupDataList.clear();
            getGroupData();
            new_type_show.setText("");
            MyToast.showShort(mActivity, "获取任务分类错误,请重新提交!");
            return;
        }

//        if (TextUtils.isEmpty(keyowrd)) {
//            MyToast.showShort(mActivity, "请添加关键字");
//            return;
//        }


        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "shuxin_know");


        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");

        sb.append("\"title\":\"");
        sb.append(title_str);
        sb.append("\",");

        sb.append("\"users_id\":\"");
        if (humenList.size() > 0) {
            for (int i = 0; i < humenList.size(); i++) {
                LogUtil.e("id=======" + humenList.get(i).id);
                humenList.get(i).id = humenList.get(i).id.replace(".0", "");
                LogUtil.e("id=======" + humenList.get(i).id);
                sb.append(humenList.get(i).id);
                if (i != (humenList.size() - 1)) {
                    sb.append(",");
                }
            }
        }
        sb.append("\",");
        if (picList.size() > 0) {
            for (int i = 0; i < picList.size() - 1; i++) {
                discription += "<p><img src=\"" + picList.get(i).url + "\" alt=\"" + picList.get(i).pic_name + "\" width=\"100%\"><br></p>\n";
            }
        }
        if (filesAddList.size() > 0) {
            for (int i = 0; i < filesAddList.size() - 1; i++) {
                discription += "<p><a style=\"text-decoration:underline;color:blue;\" href=\"" + filesAddList.get(i).file_url + "\" target=\"_blank\" alt=\"" + filesAddList.get(i).file_name + "\"> " + filesAddList.get(i).file_name + " </a>&nbsp; 点击下载<br></p>";
            }
        }


        sb.append("\"content\":\"HEX");
        sb.append(MyBase16.encode(discription));
        sb.append("\",");


//        sb.append("\"files\":[");
//        if (filesAddList.size() > 0) {
//            for (int i = 0; i < filesAddList.size() - 1; i++) {
//                sb.append("{");
//
//                sb.append("\"url\":\"");
//                sb.append("filesAddList.get(i).file_url+");
//                sb.append("\",");
//
//                sb.append("\"name\":\"");
//                sb.append(filesAddList.get(i).file_name);
//                sb.append("\"");
//
//
//                if (i != (filesAddList.size() - 2)) {
//                    sb.append("},");
//                } else {
//                    sb.append("}");
//                }
//            }
//        }
//        sb.append("],");
//
//
//        sb.append("\"images\":[");
//        if (picList.size() > 0) {
//            for (int i = 0; i < picList.size() - 1; i++) {
//                sb.append("{");
//
//                sb.append("\"url\":\"");
//                sb.append(picList.get(i).url);
//                sb.append("\",");
//
//                sb.append("\"name\":\"");
//                sb.append(picList.get(i).pic_name);
//                sb.append("\"");
//
//                if (i != (picList.size() - 2)) {
//                    sb.append("},");
//                } else {
//                    sb.append("}");
//                }
//            }
//        }
//        sb.append("],");


        sb.append("\"type\":\"");
        sb.append(type);
        sb.append("\",");

        sb.append("\"labels\":\"");
        sb.append(keyowrd);
        sb.append("\",");

        sb.append("\"author\":\"");
        sb.append(author);
        sb.append("\",");

        sb.append("\"userid\":\"");
        sb.append(SPUtil.getUserId(mActivity));
        sb.append("\"");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());


        MessageTaskPushAddBean pushData = new MessageTaskPushAddBean();

        pushData.title = title_str;
        pushData.description = discription;
        pushData.starttime = TimeUtils.getFormateTime(subTime, "-", ":");

        for (int i = 0; i < filesAddList.size(); i++) {
            MessageTaskDetailDataFilesBean filesBean = new MessageTaskDetailDataFilesBean();
            filesBean.title = filesAddList.get(i).file_name;
            filesBean.url = filesAddList.get(i).file_url;
            filesBean.ctime = TimeUtils.getFormateTime(subTime, "-", ":");

            pushData.files.add(filesBean);
        }
//        params.addBodyParameter("data", pushData.toString());
        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------添加任务详情------上传获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {

                        MyToast.showShort(mActivity, "提交成功!");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        resuld_id = dataObject.getString("id");

                        if (humenList.size() > 0) {
                            sendMessage2Users();
                        } else {
                            mActivity.finish();
                        }


                    } else {
                        MyToast.showLong(mActivity, "提交任务失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    /**
     * 向添加通知人发送消息
     */
    private void sendMessage2Users() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "shuxin_alert");
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"id\":\"");
        sb.append("\",");

        sb.append("\"name\":\"");
        sb.append("有新发表的知识@了你");
        sb.append("\",");

        sb.append("\"message\":\"");
        sb.append(UsersInforUtils.getInstance(mActivity).getUserInfo(SPUtil.getUserId(mActivity)).nickname + "发表的知识【" + title_str + "】@了你,请及时回复");
        sb.append("\",");

        sb.append("\"users_id\":\"");
        if (humenList.size() > 0) {
            for (int i = 0; i < humenList.size(); i++) {
                humenList.get(i).id = humenList.get(i).id.replace(".0", "");
                sb.append(humenList.get(i).id);
                if (i != (humenList.size() - 1)) {
                    sb.append(",");
                }
            }
        }
        sb.append("\",");

        sb.append("\"type\":\"");
        sb.append("know");
        sb.append("\",");

        sb.append("\"progress\":\"");
        sb.append("0");
        sb.append("\",");

        sb.append("\"is_readed\":\"");
        sb.append("0");
        sb.append("\",");

        sb.append("\"par_id\":\"");
        sb.append(resuld_id);
        sb.append("\"");


        sb.append("}");

        params.addBodyParameter("data", sb.toString());
        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------发送知识消息------上传消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {

                        MyToast.showShort(mActivity, "已发送消息!");
                        mActivity.finish();
                    } else {
                        MyToast.showLong(mActivity, "提交任务失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            if (selectType != -1) {
                String path = PhotoOrVideoUtils.getPath(mActivity, uri);
                upLoadFile(selectType, path);
            }
        }

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

                //去除重复联系人
                if (humenList.size() > 0) {
                    for (int i = 0; i < humenList.size(); i++) {
                        if (!userId.equals(humenList.get(i).id)) {
                            if (i == humenList.size() - 1) {//当都比对完的时候,没有重复的,就添加一条新的,
                                TreeTaskAddAddressListBean bean = new TreeTaskAddAddressListBean();
                                TreeTaskAddAddressListBean.TreeTaskAddAddressListDao dao = bean.new TreeTaskAddAddressListDao();
                                dao.id = userId;
                                dao.avatar = userAvatar;
                                dao.name = userName;
                                dao.department = userDepartment;
                                humenList.add(dao);//
                                break;//跳出循环,避免ConcurrentModificationException异常
                            }
                        } else {
                            MyToast.showLong(mActivity, "已添加(" + userName + ")为消息接收人,请重新添加");
                            break;//跳出循环,防止到最后一个节点重复添加
                        }
                    }
                } else {
                    TreeTaskAddAddressListBean bean = new TreeTaskAddAddressListBean();
                    TreeTaskAddAddressListBean.TreeTaskAddAddressListDao dao = bean.new TreeTaskAddAddressListDao();
                    dao.id = userId;
                    dao.avatar = userAvatar;
                    dao.name = userName;
                    dao.department = userDepartment;
                    humenList.add(dao);
                }
                adapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(add_user);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 上传文件到服务器
     *
     * @param filePath
     */
    private void upLoadFile(final int type, final String filePath) {
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

                        switch (type) {
                            case REQUEST_CODE_PICTURE://添加图片后返回
                                picBean = new KnowledgeNewPictureBean();
                                picBean.pic_path = filePath;
                                //picBean.pic_uri = uri;
                                picBean.url = url;
                                picBean.pic_name = filePath.substring(filePath.lastIndexOf("/") + 1);//不包含 (/)线
                                picList.add(picList.size() - 1, picBean);
                                picAdapter.notifyDataSetChanged();
                                Utility.setGridViewHeightBasedOnChildren(new_picture, 4);

                                break;
                            case REQUEST_CODE_FILES://添加文件后返回
                                filesAddDao = bean.new FilesKnowledgeItemDao();
                                filesAddDao.isEditing = true;
                                filesAddDao.file_path = filePath;
                                filesAddDao.file_name = filePath.substring(filePath.lastIndexOf("/") + 1);//不包含 (/)线
                                filesAddDao.file_url = url;
                                filesAddList.add(filesAddList.size() - 1, filesAddDao);
                                filesAddAdapter.notifyDataSetChanged();
                                Utility.setListViewHeightBasedOnChildren(new_files);
                                break;
                        }

                        selectType = -1;


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

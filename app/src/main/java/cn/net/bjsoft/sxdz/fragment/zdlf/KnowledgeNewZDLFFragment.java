package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.net.Uri;
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
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemHeadFilesListAdapter;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeNewPicturesAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowGroupBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowGroupDataItemsBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskDetailDataFilesBean;
import cn.net.bjsoft.sxdz.bean.app.top.message.task.MessageTaskPushAddBean;
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


    private KnowGroupBean knowGroupBean;//知识分类实体
    private ArrayList<KnowGroupDataItemsBean> groupDataList;//分组所在集合


    @Event(value = {R.id.title_back
            , R.id.knowledge_new_type
            , R.id.knowledge_new_submit})
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
        }
    }


    @Override
    public void initData() {
        title.setText("发布");
        back.setVisibility(View.VISIBLE);

        fragment = this;

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

    /**
     * 提交数据到服务器
     */
    private void submitToService() {
        MyToast.showShort(mActivity, "提交到服务器");
        //mActivity.finish();
        String title = new_title.getText().toString().trim();
        String discription = new_detail.getText().toString().trim();
        String type = new_type_show.getText().toString().trim();
        String keyowrd = new_keyowrd.getText().toString().trim();


        Long subTime = System.currentTimeMillis();

        if (TextUtils.isEmpty(title)) {
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
        sb.append(title);
        sb.append("\",");

        sb.append("\"content\":\"HEX");
        sb.append(MyBase16.encode(discription));
        sb.append("\",");


        for (KnowGroupDataItemsBean bean : groupDataList) {
            if (bean.name.equals(type)) {
                type = bean.id;
                break;
            }
        }

        sb.append("\"files\":[");
        if (filesAddList.size() > 0) {
            for (int i = 0;i<filesAddList.size();i++) {
                sb.append("{");

                sb.append("\"url\":\"");
                sb.append(filesAddList.get(i).file_url);
                sb.append("\",");

                sb.append("\"url\":\"");
                sb.append(filesAddList.get(i).file_name);
                sb.append("\",");

                sb.append("}");
                if (i!=(filesAddList.size()-1)){
                    sb.append("},");
                }
            }
        }
        sb.append("],");


        sb.append("\"images\":[");
        if (picList.size() > 0) {
            for (int i = 0;i<picList.size();i++) {
                sb.append("{");

                sb.append("\"url\":\"");
                sb.append(picList.get(i).url);
                sb.append("\",");

                sb.append("\"url\":\"");
                sb.append(picList.get(i).pic_name);
                sb.append("\",");

                sb.append("}");
                if (i!=(picList.size()-1)){
                    sb.append("},");
                }
            }
        }
        sb.append("],");


        sb.append("\"type\":\"");
        sb.append(type);
        sb.append("\",");

        sb.append("\"labels\":\"");
        sb.append(keyowrd);
        sb.append("\",");

        sb.append("\"author\":\"");
        sb.append(UsersInforUtils.getInstance(mActivity).getUserInfo(SPUtil.getUserId(mActivity)).nickname);
        sb.append("\",");

        sb.append("\"userid\":\"");
        sb.append(SPUtil.getUserId(mActivity));
        sb.append("\"");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());


        MessageTaskPushAddBean pushData = new MessageTaskPushAddBean();

        pushData.title = title;
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
                LogUtil.e("-----------------任务详情------上传获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {

                        MyToast.showShort(mActivity, "提交成功!");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            if (selectType != -1) {
                String path = PhotoOrVideoUtils.getPath(mActivity, uri);
                upLoadFile(selectType, path);
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

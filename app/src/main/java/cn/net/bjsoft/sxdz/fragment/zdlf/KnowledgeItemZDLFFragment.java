package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.zzhoujay.richtext.RichText;

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
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemsItemAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsItemsBean;
import cn.net.bjsoft.sxdz.bean.app.function.knowledge.KnowItemsDataItemsTopsBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase16;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.ChildrenListView;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * 中电联发的知识---条目界面
 * Created by Zrzc on 2017/3/20.
 * <p>
 * 添加注释,同步git
 */

@ContentView(R.layout.fragment_knowledge_item)
public class KnowledgeItemZDLFFragment extends BaseFragment {
    @ViewInject(R.id.title_back)
    private ImageView title_back;
    @ViewInject(R.id.title_title)
    private TextView title;
    @ViewInject(R.id.knowledge_item_items)
    private ListView lv_items;
    @ViewInject(R.id.root_reply)
    private LinearLayout root_reply;
    @ViewInject(R.id.knowledge_item_edite)
    private EditText reply;

    //headView 的控件
    private View headView;
    //private ShowHtmlView headWebView;

    //@ViewInject(R.id.item_list_headview_knowledge_title)
    private TextView head_title;
    // @ViewInject(R.id.item_list_headview_knowledge_avatar)
    private CircleImageView head_avatar;
    //@ViewInject(R.id.item_list_headview_knowledge_name)
    private TextView head_name;
    //@ViewInject(R.id.item_list_headview_knowledge_mark)
    private TextView head_mark;
    //@ViewInject(R.id.item_list_headview_knowledge_content)
    private TextView head_content;
    //@ViewInject(R.id.item_list_headview_knowledge_files)
    private ChildrenListView head_files;
    //@ViewInject(R.id.item_list_headview_knowledge_time)
    private TextView head_time;
    //@ViewInject(R.id.item_list_headview_knowledge_check_count)
    private TextView head_check_count;
    //@ViewInject(R.id.item_list_headview_knowledge_reply_count)
    private TextView head_reply_count;
    private ImageView head_top_image;
    private TextView head_top_count;
    private LinearLayout head_top;


    private boolean top_valid = false;
    private long top_count = 0l;

    private BitmapUtils bitmapUtils;

    private KnowItemsDataItemsBean hostBean;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesList;
    private KnowledgeItemHeadFilesListAdapter filesListAdapter;

    private KnowItemBean knowLedgeItemBean;
    private ArrayList<KnowItemsDataItemsItemsBean> knowledgeItemList;
    private KnowledgeItemsItemAdapter knowledgeItemsItemAdapter;

    //下载文件保存的地址
    private static String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "shuxin" + File.separator + "download" + File.separator;


    private InputMethodManager imm;


    private String know_id = "";

    private String isEditor = "false";

    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("知识详情");

        Bundle bundle = getArguments().getBundle("knowledge_item_bundle");
        know_id = bundle.getString("know_id");
        LogUtil.e("-----------------bundle.getString(\"know_id\");---------" + know_id);
        isEditor = bundle.getString("isEditor");
        if (isEditor.equals("true")) {
            root_reply.setVisibility(View.VISIBLE);
        } else if (isEditor.equals("false")) {
            root_reply.setVisibility(View.GONE);
        }


        //软键盘管理器
        imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);


        headView = View.inflate(mActivity, R.layout.item_list_knowledge_headview, null);
        head_title = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_title);
        head_avatar = (CircleImageView) headView.findViewById(R.id.item_list_headview_knowledge_avatar);
        head_name = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_name);
        head_mark = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_mark);
        head_content = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_content);
        head_files = (ChildrenListView) headView.findViewById(R.id.item_list_headview_knowledge_files);
        head_time = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_time);
        head_check_count = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_check_count);
        head_reply_count = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_reply_count);
        head_top_image = (ImageView) headView.findViewById(R.id.item_list_headview_knowledge_top_count_img);
        head_top_count = (TextView) headView.findViewById(R.id.item_list_headview_knowledge_top_count);
        head_top = (LinearLayout) headView.findViewById(R.id.item_list_headview_knowledge_top);

        head_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTops();
            }
        });

        head_files.setOnTouchListener(new View.OnTouchListener() {
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
        });

        if (filesList == null) {
            filesList = new ArrayList<>();
        }
        filesList.clear();

        if (filesListAdapter == null) {
            filesListAdapter = new KnowledgeItemHeadFilesListAdapter(mActivity, filesList);
        }
        head_files.setAdapter(filesListAdapter);


        if (knowledgeItemList == null) {
            knowledgeItemList = new ArrayList<>();
        }
        knowledgeItemList.clear();
        if (knowledgeItemsItemAdapter == null) {
            knowledgeItemsItemAdapter = new KnowledgeItemsItemAdapter(mActivity, knowledgeItemList, isEditor);
        }


        lv_items.addHeaderView(headView);
        lv_items.setAdapter(knowledgeItemsItemAdapter);

        //submitReadToService();
        getData();
        //getDataTest();

    }

    /**
     * 提交阅读数量
     */
    private void submitReadToService() {

        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "shuxin_know_read");


        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");

        sb.append("\"know_id\":\"");
        sb.append(know_id);
        sb.append("\",");

        sb.append("\"readed\":\"");
        sb.append("1");
        sb.append("\"");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        LogUtil.e("--------=====---------任务详情------上传阅读数量获取消息--------====--------");
        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------任务详情------上传阅读数量获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {
                        LogUtil.e("提交成功!阅读+1");
                        if (hostBean.views != null) {
                            head_check_count.setText(hostBean.views.size() + 1 + "");
                        } else {
                            head_check_count.setText("1");
                        }
                        //MyToast.showShort(mActivity, "提交成功!阅读+1");
                    } else {
                        //MyToast.showLong(mActivity, "提交任务失败,请联系管理员");
                        LogUtil.e("提交阅读失败,请联系管理员");
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
                LogUtil.e("-----------------获取消息----------上传阅读数量取消------");
            }

            @Override
            public void onFinished() {
                LogUtil.e("-----------------获取消息----------上传阅读数量完成------");
                dismissProgressDialog();
            }
        });

    }

    /**
     * 获取分组数据
     */
    public void getData() {


        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");


        sb.append("\"data\":{");

        sb.append("\"id\":\"");
        sb.append(know_id);
        sb.append("\"");

        sb.append("}");
        sb.append("}");

        params.addBodyParameter("data", sb.toString());
        params.addBodyParameter("source_id", "shuxin_know");
        LogUtil.e("-------------------------data.toString()--" + sb.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取知识详细----知识详细信息----------------" + strJson);


                knowLedgeItemBean = GsonUtil.getKnowledgeItemsItemBean(strJson);
                if (knowLedgeItemBean.code.equals("0")) {

                    hostBean = knowLedgeItemBean.data;
                    knowledgeItemList.addAll(knowLedgeItemBean.data.items);
                    //TODO 接口有问题
                    setData();
                    submitReadToService();

                    //LogUtil.e("获取到的条目-----------" + knowledgeItemList.get(0).avatar_url);
//                    classifyData();
//                    setData();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("-----------------获取知识详细消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取知识详细消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取知识详细消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取知识详细消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取知识详细消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取消息-----------失败方法-----" + element.getMethodName());
                }
                MyToast.showShort(mActivity, "获取知识详细数据失败!!");
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


    private void classifyData() {
        //------分组开始
//        if (groupNameList == null) {
//            groupNameList = new ArrayList<>();
//        }
//        groupNameList.clear();
//
//        for (KnowledgeBean.GroupDataDao groupData: groupDataList){
//            groupNameList.add(groupData.name);
//        }
        //-----分组结束

    }


    private void setData() {


        head_title.setText(hostBean.title);
        bitmapUtils = new BitmapUtils(mActivity, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);//初始化头像
        if (UsersInforUtils.getInstance(mActivity).getUserInfo(hostBean.userid) != null) {
            bitmapUtils.display(head_avatar, UsersInforUtils.getInstance(mActivity).getUserInfo(hostBean.userid).avatar);
            head_name.setText(UsersInforUtils.getInstance(mActivity).getUserInfo(hostBean.userid).nickname);

        }

        head_mark.setText(hostBean.labels);
        RichText.from(MyBase16.decode(hostBean.content.substring(3, hostBean.content.length()))).autoFix(true).into(head_content);
        //head_content.setText(MyBase16.decode(hostBean.content.substring(3,hostBean.content.length())));

        //附件
        filesList.clear();
        //暂时没有附件
        //filesList.addAll(hostBean.files);
        filesListAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(head_files);
        head_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                downloadFile(position);
            }
        });

        //head_time.setText(TimeUtils.getTimeDifference(Long.parseLong(hostBean.time)));
        head_time.setText(hostBean.ctime);

        if (hostBean.views != null) {
            head_check_count.setText(hostBean.views.size() + 1 + "");
        }


        if (hostBean.items != null) {
            head_reply_count.setText(hostBean.items.size() + "");
        }

        //获取点赞数量
        if (hostBean.tops != null) {
            for (KnowItemsDataItemsTopsBean top : hostBean.tops) {
                if (top.userid.equals(SPUtil.getUserId(mActivity))) {
                    top_valid = true;
                }
                switch (Integer.parseInt(top.valid)) {
                    case 1:

                        top_count++;
                        break;

                    case 0:
                        //top_valid = false;
                        break;
                }


            }

            if (top_valid) {
                head_top_image.setImageResource(R.drawable.knowledge_zdlf_zan_s);
            } else {
                head_top_image.setImageResource(R.drawable.knowledge_zdlf_zan_n);
            }
            head_top_count.setText(top_count + "");
        }


        knowledgeItemsItemAdapter.notifyDataSetChanged();

    }

    private void setTops() {


        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "shuxin_know_top");


        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");

        sb.append("\"know_id\":\"");
        sb.append(know_id);
        sb.append("\",");

        top_valid = !top_valid;
        sb.append("\"valid\":\"");
        sb.append(top_valid);
        sb.append("\"");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        LogUtil.e("--------=====---------任务详情------上传点赞数量获取消息--------====--------");
        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------任务详情------上传阅读数量获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {
                        LogUtil.e("-----------------点赞+1-----------------------");

                        if (top_valid) {
                            top_count = top_count + 1;
                            head_top_image.setImageResource(R.drawable.knowledge_zdlf_zan_s);
                        } else {
                            top_count = top_count - 1;
                            head_top_image.setImageResource(R.drawable.knowledge_zdlf_zan_n);
                        }
                        head_top_count.setText(top_count + "");

                        //MyToast.showShort(mActivity, "提交成功!阅读+1");
                    } else {
                        MyToast.showLong(mActivity, "点赞失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取点赞点赞消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取点赞消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取点赞消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取点赞消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取点赞消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取点赞消息-----------失败方法-----" + element.getMethodName());
                }

                MyToast.showShort(mActivity, "获取点赞数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                LogUtil.e("-----------------获取点赞消息----------上传点赞数量取消------");
            }

            @Override
            public void onFinished() {
                LogUtil.e("-----------------获取点赞消息----------上传点赞数量完成------");
                dismissProgressDialog();
            }
        });

    }


    /**
     * 下载附件
     *
     * @param positon
     */
    private void downloadFile(int positon) {
        String url = hostBean.files.get(positon).url;
        String file_name = url.substring(url.lastIndexOf("/"));
        String path = "";
        // 首先判定是否有SDcard,并且可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //一定要指定文件名，不然会将下载的文件保存成为.tmp的文件
            path = BASE_PATH + file_name;
        } else {
            path = mActivity.getFilesDir().getAbsolutePath() + File.separator + file_name;
            Toast.makeText(mActivity, "SD卡不可用,将下载到手机内部", Toast.LENGTH_SHORT).show();
        }

        File file = new File(path);
        MyToast.showShort(mActivity, "开始下载附件!");
        if (file.exists() && file.length() > 0) {
            MyToast.showShort(mActivity, "文件已经下载过了,无需重新下载");
            return;
        } else {
            RequestParams params = new RequestParams(url);
            params.setSaveFilePath(path);
            //设置断点续传
            params.setAutoResume(true);

            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {

                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    // 设置下载进度信息
                    if (isDownloading) {
                    }
                }

                @Override
                public void onSuccess(File file) {
                    // 下载成功之后就安装akp
                    MyToast.showShort(mActivity, "下载成功\n请到相册或文件管理器查看");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    MyToast.showShort(mActivity, "下载失败了");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

            });
        }
    }

    @Event(value = {R.id.knowledge_item_reply
            /*, R.id.item_list_headview_knowledge_top*/
            , R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mActivity.finish();
                break;

            case R.id.knowledge_item_reply:
                replyHost();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;

//            case R.id.item_list_headview_knowledge_top:
//                LogUtil.e("=============点击点赞==============");
//                setTops();
//                LogUtil.e("=============点击点赞==============");
//                break;
        }
    }


    /**
     * 回复楼主
     */
    private void replyHost() {

        String replyStr = reply.getText().toString().trim();
        if (replyStr.equals("")) {
            MyToast.showShort(mActivity, "请输入回复内容");
            return;
        }


        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "shuxin_know_reply");


        final KnowItemsDataItemsItemsBean newDao = new KnowItemsDataItemsItemsBean();

        if (newDao.items == null) {
            newDao.items = new ArrayList<>();
        }
        newDao.items.clear();
        newDao.userid = SPUtil.getUserId(mActivity);
        //newDao.avatar_url = SPUtil.getAvatar(mActivity);
        newDao.ctime = TimeUtils.getFormateTime(System.currentTimeMillis(), "-", ":") + "";
        newDao.content = "HEX" + MyBase16.encode(reply.getText().toString().trim());
        newDao.reply_id = "";
        newDao.know_id = know_id;
        newDao.userid = SPUtil.getUserId(mActivity);


        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");


        sb.append("\"content\":\"");
        sb.append(newDao.content);
        sb.append("\",");

//        sb.append("\"ctime\":\"");
//        sb.append(newDao.ctime);
//        sb.append("\",");


        sb.append("\"know_id\":\"");
        sb.append(know_id);
        sb.append("\",");

//        sb.append("\"reply_id\":\"");
//        sb.append(newDao.reply_id);
//        sb.append("\",");

//        sb.append("\"author\":\"");
//        sb.append(UsersInforUtils.getInstance(mActivity).getUserInfo(SPUtil.getUserId(mActivity)).nickname);
//        sb.append("\",");
//
        sb.append("\"userid\":\"");
        sb.append(newDao.userid);
        sb.append("\"");

        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------任务详情------上传获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        newDao.id = data.optString("id");
                        knowledgeItemList.add(newDao);
                        MyToast.showShort(mActivity, "评论成功" + newDao.reply_id);
                        knowledgeItemsItemAdapter.notifyDataSetChanged();
                        reply.setText("");//清除输入框
                        //mActivity.finish();
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


//        KnowLedgeItemBean bean = new KnowLedgeItemBean();

        /**
         * 将数据推送到服务器,当返回成功的时候再将数据添加到本地
         */

//        int i = 0;
//        for (KnowLedgeItemBean.ReplyListDao Dao : knowledgeItemList) {
//
//            if (Dao.reply_list != null) {
//                LogUtil.e("数量" + Dao.reply_list.size() + "::楼层" + (i+1));
//            }
//            i++;
//
//        }


    }
}

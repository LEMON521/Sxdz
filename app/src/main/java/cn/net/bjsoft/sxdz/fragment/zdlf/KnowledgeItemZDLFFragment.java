package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Context;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.zzhoujay.richtext.RichText;

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
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
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
    private BitmapUtils bitmapUtils;

    private KnowLedgeItemBean.HostKnowledgeItemDao hostDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesList;
    private KnowledgeItemHeadFilesListAdapter filesListAdapter;

    private KnowLedgeItemBean knowLedgeItemBean;
    private ArrayList<KnowLedgeItemBean.ReplyListDao> knowledgeItemList;
    private KnowledgeItemsItemAdapter knowledgeItemsItemAdapter;

    //下载文件保存的地址
    private static String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "shuxin" + File.separator + "download" + File.separator;


    private InputMethodManager imm;

    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("知识详情");

        //软键盘管理器
        imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        showProgressDialog();
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
            knowledgeItemsItemAdapter = new KnowledgeItemsItemAdapter(mActivity, knowledgeItemList);
        }


        lv_items.addHeaderView(headView);
        lv_items.setAdapter(knowledgeItemsItemAdapter);
        getData();
    }


    /**
     * 获取分组数据
     */
    public void getData() {

        RequestParams params = new RequestParams(TestAddressUtils.test_get_knowledge_item_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                knowLedgeItemBean = GsonUtil.getKnowledgeItemsItemBean(result);
                if (knowLedgeItemBean.result) {
                    hostDao = knowLedgeItemBean.data.host;
                    knowledgeItemList.addAll(knowLedgeItemBean.data.knowledge_item);
                    setData();
                    //LogUtil.e("获取到的条目-----------" + knowledgeItemList.get(0).avatar_url);
//                    classifyData();
//                    setData();
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


        head_title.setText(hostDao.title);
        bitmapUtils = new BitmapUtils(mActivity, AddressUtils.img_cache_url);//初始化头像
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);//初始化头像
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);//初始化头像
        bitmapUtils.display(head_avatar, hostDao.avatar);
        head_name.setText(hostDao.name);
        head_mark.setText(hostDao.mark);
        RichText.from(hostDao.content).autoFix(false).into(head_content);
        //附件
        filesList.clear();
        filesList.addAll(hostDao.files);
        filesListAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(head_files);
        head_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                downloadFile(position);
            }
        });

        head_time.setText(TimeUtils.getTimeDifference(Long.parseLong(hostDao.time)));
        head_check_count.setText(hostDao.check_count);
        head_reply_count.setText(hostDao.reply_count);


        knowledgeItemsItemAdapter.notifyDataSetChanged();

    }

    /**
     * 下载附件
     *
     * @param positon
     */
    private void downloadFile(int positon) {
        String url = hostDao.files.get(positon).file_url;
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
            , R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mActivity.finish();
                break;

            case R.id.knowledge_item_reply:
                String replyStr = reply.getText().toString().trim();
                if (replyStr.equals("")) {
                    MyToast.showShort(mActivity, "请输入回复内容");
                    return;
                }

                replyHost();
                reply.setText("");//清除输入框

                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }


    /**
     * 回复楼主
     */
    private void replyHost() {
        KnowLedgeItemBean bean = new KnowLedgeItemBean();
        KnowLedgeItemBean.ReplyListDao newDao = bean.new ReplyListDao();

        if (newDao.reply_list == null) {
            newDao.reply_list = new ArrayList<>();
        }
        newDao.reply_list.clear();
        newDao.name = SPUtil.getUserName(mActivity);
        newDao.avatar_url = SPUtil.getAvatar(mActivity);
        newDao.time = System.currentTimeMillis() + "";
        newDao.comment_text = reply.getText().toString().trim();
        newDao.reply_to = "";

        /**
         * 将数据推送到服务器,当返回成功的时候再将数据添加到本地
         */
        knowledgeItemList.add(newDao);
//        int i = 0;
//        for (KnowLedgeItemBean.ReplyListDao Dao : knowledgeItemList) {
//
//            if (Dao.reply_list != null) {
//                LogUtil.e("数量" + Dao.reply_list.size() + "::楼层" + (i+1));
//            }
//            i++;
//
//        }
        MyToast.showShort(mActivity, "评论成功");
        knowledgeItemsItemAdapter.notifyDataSetChanged();


    }
}

package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemHeadFilesListAdapter;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeNewPicturesAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeNewPictureBean;
import cn.net.bjsoft.sxdz.dialog.SideRightPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
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
                if (typeStrList == null) {
                    typeStrList = new ArrayList<>();
                }
                typeStrList.clear();

                typeStrList.add("生活");
                typeStrList.add("体育");
                typeStrList.add("音乐");
                typeStrList.add("近期热门");
                typeStrList.add("科技");
                typeStrList.add("健身");

                rightPopupWindow.showWindow(typeStrList);
                break;
            case R.id.knowledge_new_submit://发表,推送数据到服务器上
                MyToast.showShort(mActivity, "提交到服务器");
                mActivity.finish();
                break;
        }
    }


    @Override
    public void initData() {
        title.setText("发布");
        back.setVisibility(View.VISIBLE);

        fragment = this;

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            if (selectType != -1) {
                String path = PhotoOrVideoUtils.getPath(mActivity, uri);
                switch (selectType) {
                    case REQUEST_CODE_PICTURE://添加图片后返回
                        picBean = new KnowledgeNewPictureBean();
                        picBean.pic_path = path;
                        picBean.pic_uri = uri;
                        picList.add(picList.size() - 1, picBean);
                        picAdapter.notifyDataSetChanged();
                        Utility.setGridViewHeightBasedOnChildren(new_picture, 4);

                        break;
                    case REQUEST_CODE_FILES://添加文件后返回
                        filesAddDao = bean.new FilesKnowledgeItemDao();
                        filesAddDao.isEditing = true;
                        filesAddDao.file_path = path;
                        filesAddDao.file_name = path.substring(path.lastIndexOf("/") + 1);//不包含 (/)线
                        filesAddList.add(filesAddList.size() - 1, filesAddDao);
                        filesAddAdapter.notifyDataSetChanged();
                        Utility.setListViewHeightBasedOnChildren(new_files);

                        break;
                }
                selectType = -1;


            }
        }

    }
}

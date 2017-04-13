package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.dialog.PickerDialog;
import cn.net.bjsoft.sxdz.dialog.SideRightPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;

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

    private SideRightPopupWindow typePopupWindow;
    private ArrayList<String> typeStrList;
    private SideRightPopupWindow levelPopupWindow;
    private ArrayList<String> levelStrList;


    private KnowLedgeItemBean bean;
    private KnowLedgeItemBean.FilesKnowledgeItemDao filesAddDao;
    private ArrayList<KnowLedgeItemBean.FilesKnowledgeItemDao> filesAddList;
    private KnowledgeItemHeadFilesListAdapter filesAddAdapter;

    private AdapterView.OnItemClickListener itemClickListener;
    private View.OnTouchListener onTouchListener;


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
                typeStrList.clear();//先清空
                typeStrList.add("生活");
                typeStrList.add("体育");
                typeStrList.add("音乐");
                typeStrList.add("近期热门");
                typeStrList.add("科技");
                typeStrList.add("健身");

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
                break;
            case R.id.fragment_task_new_submit://提交
                MyToast.showShort(mActivity, "提交任务到服务器");
                mActivity.finish();
                break;
        }
    }


    @Override
    public void initData() {
        title.setText("新建任务");
        back.setVisibility(View.VISIBLE);

        /**
         * 分类---性质侧拉狂相关
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            String path = PhotoOrVideoUtils.getPath(mActivity, uri);
            filesAddDao = bean.new FilesKnowledgeItemDao();
            filesAddDao.isEditing = true;
            filesAddDao.file_path = path;
            filesAddDao.file_name = path.substring(path.lastIndexOf("/") + 1);//不包含 (/)线
            filesAddList.add(filesAddList.size() - 1, filesAddDao);
            filesAddAdapter.notifyDataSetChanged();
            Utility.setListViewHeightBasedOnChildren(new_files);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

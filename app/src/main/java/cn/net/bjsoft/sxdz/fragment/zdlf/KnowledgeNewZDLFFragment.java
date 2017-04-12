package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Intent;
import android.net.Uri;
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
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeNewPicturesAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeNewPictureBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
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
    private EditText new_submit;

    @ViewInject(R.id.knowledge_new_picture)
    private GridView new_picture;
    @ViewInject(R.id.knowledge_new_files)
    private ListView new_files;

    private KnowledgeNewPictureBean picBean;
    private KnowledgeNewPicturesAdapter picAdapter;
    private ArrayList<KnowledgeNewPictureBean> picList;

    private AdapterView.OnItemClickListener itemClickListener;
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
                break;
            case R.id.knowledge_new_submit://发表,推送数据到服务器上

                break;
        }
    }


    @Override
    public void initData() {
        title.setText("发布");
        back.setVisibility(View.VISIBLE);

        if (picList == null) {
            picList = new ArrayList<>();
        }
        picList.clear();
        picBean = new KnowledgeNewPictureBean();

        if (picAdapter == null) {
            picAdapter = new KnowledgeNewPicturesAdapter(mActivity, picList);
        }

        new_picture.setAdapter(picAdapter);


        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.knowledge_new_picture:
                        if ((picList.size() - 1) == position) {

                            selectType = REQUEST_CODE_PICTURE;
                            PhotoOrVideoUtils.doPhoto(null, KnowledgeNewZDLFFragment.this, view);
                        }
                        break;
                    case R.id.knowledge_new_files:
                        break;
                }
            }
        };

        new_picture.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            if (selectType != -1) {

                switch (selectType) {
                    case REQUEST_CODE_PICTURE:

                        String imagePath = PhotoOrVideoUtils.getPath(mActivity, uri);
                        picBean = new KnowledgeNewPictureBean();
                        picBean.pic_path = imagePath;
                        picBean.pic_uri = uri;
                        picList.add(picBean);
                        picAdapter.notifyDataSetChanged();
                        Utility.setGridViewHeightBasedOnChildren(new_picture, 4);

                        break;
                    case REQUEST_CODE_FILES:
                        break;
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

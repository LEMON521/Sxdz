package cn.net.bjsoft.sxdz.fragment.bartop.community.disabuse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.NewDisabuseAdapter;
import cn.net.bjsoft.sxdz.bean.community.DisabuseNewPhotoBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.PhotoUtils;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;

/**
 * 添加解惑
 * Created by Zrzc on 2017/1/12.
 */
@ContentView(R.layout.fragment_disabuse_new)
public class NewDisabuseFragment extends BaseFragment {
    @ViewInject(R.id.disabuse_new_title)
    private TextView title;
    @ViewInject(R.id.disabuse_new_back)
    private TextView back;
    @ViewInject(R.id.disabuse_new_add)
    private TextView add;
    @ViewInject(R.id.disabuse_new_content)
    private EditText content;
    @ViewInject(R.id.disabuse_new_imgs)
    private GridView imgsGridView;

    private static int REQUEST_CODE_TAKE_PHOTO = 100;
    private static int REQUEST_CODE_GET_PHOTO = 200;

    public NewDisabuseAdapter adapter;
    private static ArrayList<ImageView> imageViews;
    private DisabuseNewPhotoBean photoBean;
    public ArrayList<DisabuseNewPhotoBean> imgList;

    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e("NewDisabuseFragment==onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果输入法在窗口上已经显示，则隐藏，反之则显示
        // 默认软键盘不弹出

        LogUtil.e("NewDisabuseFragment==onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (content.hasFocus()){
//            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
        //如果输入法在窗口上已经显示，则隐藏，反之则显示
//        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void initData() {

        imgList = getSingleList();
        photoBean = new DisabuseNewPhotoBean();
        photoBean.tag = "1";
        photoBean.name = "点击添加图片";
        imgList.add(photoBean);
        LogUtil.e("initData图片条目为========="+imgList.size());
        adapter = new NewDisabuseAdapter(mActivity, imgList, this);
        imgsGridView.setAdapter(adapter);
        LogUtil.e("initData图片条目为========="+imgList.size());
    }

    public ArrayList<DisabuseNewPhotoBean> getSingleList(){
        if (imgList == null){
            imgList = new ArrayList<>();
        }
        return imgList;

    }

    @Event(type = View.OnClickListener.class, value = {R.id.disabuse_new_back, R.id.disabuse_new_add})
    private void newDisabuseOnClick(View view) {
        switch (view.getId()) {
            case R.id.disabuse_new_back:
                mActivity.finish();
                break;
            case R.id.disabuse_new_add:
                addDisabuse();
                mActivity.finish();
                break;
        }
    }

    /**
     * 添加解惑的推送数据放在这里
     */
    private void addDisabuse() {
        MyToast.showShort(mActivity, "添加了一条新建议");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e("获取图片URI"+ PhotoOrVideoUtils.getFileUri(requestCode,resultCode,data).toString());
        //获取到了图片，并设置图片
        if (requestCode == REQUEST_CODE_GET_PHOTO) {
            if (data != null) {
                photoBean = new DisabuseNewPhotoBean();
                photoBean.imgUri = data.getData();
                LogUtil.e("图片条目uri为========="+data.getData());
                photoBean.tag = "2";
                String imageUrl = PhotoUtils.getPath(mActivity,data.getData());
                photoBean.imgUrl = imageUrl;
                photoBean.name = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
                imgList.add(0, photoBean);
                //adapter.notifyDataSetChanged();
                adapter.refresh(imgList);
                LogUtil.e("图片条目为========="+imgList.size());

            }

        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            LogUtil.e("data=="+(data == null));
            if (data != null) {//TODO 必须分开判断，不然不拍摄照片直接返回的话就会报空指针异常：data.getExtras()
                LogUtil.e("data.getExtras()=="+(data.getExtras() == null));
                if (data.getExtras() != null) {
                    LogUtil.e("data.getExtras().get(\"data\")=="+(data.getExtras().get("data") == null));
                    if (data.getExtras().get("data") != null /*|| data.getExtras().get("data").equals("")*/) {
                        LogUtil.e("data.getExtras().get(\"data\")=="+(data.getExtras().get("data")));
                        LogUtil.e("拍摄了照片uri=="+(data != null));
                        //  相机照相获取文件路径开始
                        String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                            Log.i("TestFile",
                                    "SD card is not avaiable/writeable right now.");
                            MyToast.showShort(mActivity,"SD卡不可用，请检查！");
                            return;
                        }

                        Bitmap bitmap = null;
                        String fileName = "";
                        Bundle bundle = data.getExtras();
                        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                        bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                        Uri uri = null;
                        if (data.getData() != null)
                        {
                            uri = data.getData();
                            LogUtil.e("data.getData()-uri=="+uri.toString());
                        }
                        else
                        {
                            uri  = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, null,null));
                            LogUtil.e("Uri.parse-uri=="+uri.toString());
                        }
                        FileOutputStream b = null;
                        File file = new File("/sdcard/Image/");
                        file.mkdirs();// 创建文件夹
                        fileName = "/sdcard/Image/" + name;
                        try {
                            b = new FileOutputStream(fileName);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                b.flush();
                                b.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        photoBean = new DisabuseNewPhotoBean();
                        LogUtil.e("uri=="+uri.toString());
                        photoBean.imgUri = uri;
                        photoBean.imgUrl = fileName;
                        LogUtil.e("uri=="+uri.toString());
                        photoBean.tag = "2";
                        photoBean.name = fileName.substring(fileName.lastIndexOf("/")+1);
                        imgList.add(0, photoBean);
                        //adapter.notifyDataSetChanged();
                        adapter.refresh(imgList);
                        LogUtil.e("图片条目为========="+imgList.size());
                    }

                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e("onDestroyView：：：：");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy：：：：");
    }
}

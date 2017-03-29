package cn.net.bjsoft.sxdz.fragment.barbotton;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.PhotoUtils;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UriUtils;
import cn.net.bjsoft.sxdz.utils.UrlUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Homepage    ------上传页面
 * Created by zkagang on 2016/9/13.
 */
public class BottonUploadFragment extends BaseFragment {

    private View view;
    private String url = "";
    private String text = "";

    private ImageView back;
    private PopupWindow popupWindow1;
    //private TextView fragmen_upload_tv_toolbar;//顶部显示文字
    private ImageView imageView;
    private EditText editText;
    private TextView title;
    private TextView btn_upload;
    //    private RadioButton rb_pic;
//    private RadioButton rb_video;
    private FrameLayout fragmen_upload_fl;
    private TextView fragmen_upload_tv_discription;

    private static int REQUEST_CODE_TAKE_PHOTO = 100;
    private static int REQUEST_CODE_TAKE_VIDEO = 200;
    private static int REQUEST_CODE_GET_VIDEOORPHOTO = 300;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 视频／图像的本地路径
     */
    private String filePath = "";
    /**
     * 文件的字符串形式
     */
    private String fileIOStr;

    public String getFileIOStr() {
        return fileIOStr;
    }

    public void setFileIOStr(String fileIOStr) {
        this.fileIOStr = fileIOStr;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        text = getArguments().getString("text");
        url = getArguments().getString("url");
        //url= UrlUtil.getUrl(url,getActivity());
        text = getArguments().getString("text");
        Log.e("BottonUploadFragment", text);
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        back = (ImageView) view.findViewById(R.id.include_title_upload).findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        fragmen_upload_tv_toolbar = (TextView) view.findViewById(R.id.include_title_upload).findViewById(R.id.title_title);
//        fragmen_upload_tv_toolbar.setText("上传Upload");
        title = (TextView) view.findViewById(R.id.include_title_upload).findViewById(R.id.title_title);
        title.setText(text);
        imageView = (ImageView) view.findViewById(R.id.frgment_ouload_iv_upimg);
        editText = (EditText) view.findViewById(R.id.fragment_upload_name);
        btn_upload = (TextView) view.findViewById(R.id.fragmen_upload_tv_btn);
//        rb_pic = (RadioButton) view.findViewById(R.id.fragmen_upload_rb_pic);
//        rb_video = (RadioButton) view.findViewById(R.id.fragmen_upload_rb_video);
        fragmen_upload_fl = (FrameLayout) view.findViewById(R.id.fragmen_upload_fl);
        fragmen_upload_tv_discription = (TextView) view.findViewById(R.id.fragmen_upload_tv_discription);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString().trim();
                if (number.equals("") || number == null) {
                    MyToast.showShort(getActivity(), "编号不能为空，请输入编号");
                } else {
                    String ext = "";//后缀名称

                    if (!getFilePath().equals("")) {
                        upLoadFile(getFilePath(), number, ext);
                    } else {
                        MyToast.showShort(getActivity(), "请选择文件");
                    }
                }
            }
        });


        /**
         * 选择图片的点击事件
         */
        fragmen_upload_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getContext().getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                if (!permission) {
                    MyToast.showLong(getActivity(), "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                    return;
                } else {
                    getPhotoOrVideo(v);
                }
//                }
            }
        });


        return view;
    }

    @Override
    public void initData() {

    }

    /**
     * 设置视频
     * 选择是相册还是拍照
     *
     * @param v
     */
    private void getPhotoOrVideo(View v) {

        View contentView;
        popupWindow1 = null;
        if (popupWindow1 == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
            contentView = mLayoutInflater.inflate(R.layout.pop_select, null);
            popupWindow1 = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView paizhaopian = (TextView) contentView.findViewById(R.id.paizhaopian);
            TextView paishipin = (TextView) contentView.findViewById(R.id.paishipin);
            paizhaopian.setText("拍摄照片");
            paishipin.setText("拍摄视频");
            TextView xiangce = (TextView) contentView.findViewById(R.id.xiangce);
            TextView quxiao = (TextView) contentView.findViewById(R.id.quxiao);

            paishipin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开照相机的意图

                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                    startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);

                    popupWindow1.dismiss();
                }
            });
            paizhaopian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开照相机的意图
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Intent i = new Intent();
                        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, REQUEST_CODE_TAKE_PHOTO);
                    } else {
                        Toast.makeText(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                    }
                    popupWindow1.dismiss();
                }
            });
            xiangce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //打开相册的意图
                    Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*;video/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                    startActivityForResult(intent, REQUEST_CODE_GET_VIDEOORPHOTO);
                    popupWindow1.dismiss();
                }
            });
            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow1.dismiss();
                }
            });
        }

        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow1.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);

        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setFocusable(true);
        popupWindow1.showAtLocation((View) v.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow1.update();
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }


    /**
     * 获取到的图片路径
     */
    private String picPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    private String videoPath;
    public static Uri photoUri;


    /**
     * 视频路径
     * imageView.setImageBitmap(bitmap);
     */
    private String strRecorderPath;

    public String getIsWitchFile() {
        return isWitchFile;
    }

    public void setIsWitchFile(String isWitchFile) {
        this.isWitchFile = isWitchFile;
    }

    private String isWitchFile = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //获取到了图片或者视频，并设置图片
        if (requestCode == REQUEST_CODE_GET_VIDEOORPHOTO) {
            if (data != null) {
                String path = "";
                if (mOsVersion < 19) {
                    if (data != null) {
                        path = PhotoUtils.getPath(getContext().getApplicationContext(), data.getData());
                        setPicPath(path);//图片路径赋值
                        upLoadFile(path, "头像", "jpg");
                    }
                } else {
                    if (resultCode == RESULT_OK && data != null) {
                        path = PhotoUtils.getPath(getContext().getApplicationContext(), data.getData());
                        setPicPath(path);//图片路径赋值
                        upLoadFile(path, "头像", "jpg");
                    }
                }
                ContentResolver cr = getActivity().getContentResolver();
                if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".bmp")) {
                    setIsWitchFile("pic");
                    setPicPath(path);//图片路径赋值
                    setFilePath(path);
                    setVideoPath("");
                } else if (path.endsWith(".rmvb") || path.endsWith(".3gp") || path.endsWith(".mp4") || path.endsWith(".mov")) {
                    setIsWitchFile("video");
                    setVideoPath(path);
                    setFilePath(path);
                    setPicPath("");
                } else {
                    Toast.makeText(getActivity(), "请选择正确的图像或者视频模式！", Toast.LENGTH_LONG).show();
                    setIsWitchFile("");
                    setFilePath("");
                }

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
                    /* 将Bitmap设定到ImageView */
                    fragmen_upload_tv_discription.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);// 将图片显示在ImageView里
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }


            }

        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {

            if (data != null) {
                if (data.getExtras() != null /*|| data.getExtras().get("data").equals("")*/) {
                    if (data.getExtras().get("data") != null) {
                        //  相机照相获取文件路径开始
                        String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                            Log.i("TestFile",
                                    "SD card is not avaiable/writeable right now.");
                            return;
                        }
                        Bitmap bitmap = null;
                        String fileName = "";
                        Bundle bundle = data.getExtras();
                        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                        //Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();
                        new DateFormat();

                        bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                        imageView.setImageBitmap(bitmap);// 将图片显示在ImageView里
                        FileOutputStream b = null;
                        File file = new File("/sdcard/Image/");
                        file.mkdirs();// 创建文件夹
                        fileName = "/sdcard/Image/" + name;

                        //LogUtil.e("相机获取到的文件路径为：" + fileName);
                        //LogUtil.e("相机获取到的文件uri为：" + fileName);
                        try {
                            b = new FileOutputStream(fileName);
                            fragmen_upload_tv_discription.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
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
                        //setFilePath(fileName);
                        setPicPath(fileName);
                        setFilePath(fileName);
                        LogUtil.e("拍摄了照片");
                    }
                }
            }

            //setPicPath(fileName);

            //  相机照相获取文件路径结束


        } else if (requestCode == REQUEST_CODE_TAKE_VIDEO) {
            LogUtil.e("拍摄了视频");
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                LogUtil.e("拍摄视频的路径" + data.getData().getPath());
                //Display the video
                String mPath = UriUtils.getPath(getActivity(), data.getData());
                setVideoPath(mPath);
                setFilePath(mPath);
                // setFilePath(mPath);
                imageView.setImageResource(R.drawable.shipin);// 将图片显示在ImageView里
                fragmen_upload_tv_discription.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    ProgressDialog dialog;

    /**
     * 上传动作
     *
     * @param filepath 要上传的文件路径
     * @param number   要上传文件的对应的编号
     */
    public void upLoadFile(String filepath, String number, String ext) {
        //LogUtils.e("传入的路径名==" + filepath.substring(filepath.lastIndexOf(".")));
        String s = "2.3.6.98";
        //s.lastIndexOf(".",0);
        //LogUtils.e("获取到的lastIndexOf为"+s.lastIndexOf(".",s.length()-1));

        String io = MyBase64.file2String(filepath);
        dialog = ProgressDialog.show(getActivity(), "上传文件", "正在上传", true, false);
        ext = filepath.substring(filepath.lastIndexOf(".") + 1);
        try {
            LogUtil.e("上传地址为！！！===" + io);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));


        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));
        params.addBodyParameter("action", "submit");
        params.addBodyParameter("method", "media_capture_upload");
        params.addBodyParameter("ext", ext);
        params.addBodyParameter("name", number);
        params.addBodyParameter("media", io);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传结果为！！！！======" + result);
                //JSONObject jsonObject = new JSONObject(responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.optBoolean("success", false);
                    if (!success) {
                        MyToast.showShort(getActivity(), "上传失败，请联系管理员");
                    } else {
                        MyToast.showShort(getActivity(), "上传成功！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("失败原因为！！！！======" + ex);
                MyToast.showShort(getActivity(), "上传失败，网络超时");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    @Event(value = {R.id.title_back})
    private void uploadOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
        }
    }
}
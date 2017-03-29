package cn.net.bjsoft.sxdz.fragment.barbotton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.welcome.SplashActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.PhotoUtils;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;
import cn.net.bjsoft.sxdz.view.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * Homepage   - -   我的页面
 * Created by zkagang on 2016/9/13.
 */
@ContentView(R.layout.homepagefragment_mine)
public class BottonMineFragment extends BaseFragment {
//    @ViewInject(R.id.sign_user_icon)
//    private RoundImageView user_icon;


    @ViewInject(R.id.homepagefrag_mine_riv_avatar)
    //private ImageView avatar;
//    private RoundImageView avatar;
    private CircleImageView avatar;
    @ViewInject(R.id.homepagefrag_mine_tv_name)
    private TextView name;
    @ViewInject(R.id.homepagefrag_mine_tv_email)
    private TextView email;
    @ViewInject(R.id.homepagefrag_mine_tv_birthday)
    private TextView birthday;
    @ViewInject(R.id.homepagefrag_mine_tv_phone)
    private TextView phone;
    @ViewInject(R.id.main_botton_title)
    private TextView title;
    @ViewInject(R.id.fragmen_mine_btn_exit)
    private Button btn_exit;

    private View view;
    //private ArrayList<String> detail;
    private ImageOptions mImageOptions;
    private String mJson;
    private DatasBean.UserDao mUserDao;

    private BitmapUtils bitmapUtils;

    private static int REQUEST_CODE_TAKE_PHOTO = 100;
    private static int REQUEST_CODE_GET_PHOTO = 200;

    private PopupWindow popupWindow1;

    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public BottonMineFragment() {
    }

    //private String text = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        x.image().clearCacheFiles();
//        x.image().clearMemCache();
        mJson = getArguments().get("json").toString();
        mUserDao = GsonUtil.getDatasBean(mJson).data.user;
        mImageOptions = new ImageOptions.Builder().setCircular(true).setUseMemCache(false).build();
        view = x.view().inject(this, inflater, null);
        //text = getArguments().get("text").toString();
        //LogUtil.e("BottonMineFragment==" + text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void initData() {
//        if (!text.equals("")) {
//            title.setText(text);
//        }

        LogUtil.e("头像地址" + mUserDao.avatar);
        bitmapUtils = new BitmapUtils(getActivity(), AddressUtils.img_cache_url);
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);
        //获取详细信息
        //detail = getArguments().getStringArrayList("detail");
        if (mUserDao.avatar.equals("")) {
            avatar.setImageBitmap(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.wlh));
//            x.image().clearCacheFiles();
//            x.image().clearMemCache();
            //x.image().bind(avatar,mUserDao.avatar,mImageOptions);
        } else {


            bitmapUtils.display(avatar, mUserDao.avatar);
//            MyBitmapUtils.getInstance(getActivity()).clearCache(mUserDao.avatar);
//            MyBitmapUtils.getInstance(getActivity()).display(avatar, mUserDao.avatar);
//            x.image().clearCacheFiles();
//            x.image().clearMemCache();
//            x.image().bind(avatar,mUserDao.avatar,mImageOptions);
        }
        if (mUserDao.name == null || mUserDao.name.equals("")) {
            name.setText("姓名 ：" + "未设置");
        } else {
            name.setText("姓名 ：" + mUserDao.name);
        }
        if (mUserDao.email == null || mUserDao.email.equals("")) {
            email.setText("邮箱 ：" + "未设置");
        } else {
            email.setText("邮箱 ：" + mUserDao.email);
        }
        if (mUserDao.birthday == null || mUserDao.birthday.equals("")) {
            birthday.setText("生日 ：" + "未设置");
        } else {
            birthday.setText("生日 ：" + mUserDao.birthday);
        }
        if (mUserDao.phone == null || mUserDao.phone.equals("")) {
            phone.setText("账号 ：" + "未设置");
        } else {
            phone.setText("账号 ：" + mUserDao.phone);
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getphoto(view);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SplashActivity.class);
                startActivity(i);
                SPUtil.setUserUUID(getActivity(), "");
                getActivity().finish();
            }
        });

    }

    /**
     * 设置图片
     * 选择是相册还是拍照
     *
     * @param v
     */
    private void getphoto(View v) {

        View contentView;
        popupWindow1 = null;
        if (popupWindow1 == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
            contentView = mLayoutInflater.inflate(R.layout.pop_select_photo, null);
            popupWindow1 = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView paizhao = (TextView) contentView.findViewById(R.id.paizhao);
            TextView xiangce = (TextView) contentView.findViewById(R.id.xiangce);
            TextView quxiao = (TextView) contentView.findViewById(R.id.quxiao);

            paizhao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开照相机的意图
                    //MyToast.showShort(getActivity(), "点击了！");
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Intent i = new Intent();
                        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, REQUEST_CODE_TAKE_PHOTO);
                        MyToast.showShort(getActivity(), "打开了照相机！");
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

                    if (mOsVersion < 19) {
                        /* 开启Pictures画面Type设定为image */
                        intent.setType("image/*");
                         /* 使用Intent.ACTION_GET_CONTENT这个Action */
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        /* 取得相片后返回本画面 */
                        startActivityForResult(intent, REQUEST_CODE_GET_PHOTO);
                    } else {
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("image/*"); // Or 'image/ jpeg '
                        startActivityForResult(intent, REQUEST_CODE_GET_PHOTO);
                    }


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //获取到了图片，并设置图片
        if (requestCode == REQUEST_CODE_GET_PHOTO) {
            if (mOsVersion < 19) {
                if (data != null) {
                    String path = PhotoUtils.getPath(getContext().getApplicationContext(), data.getData());
                    setPicPath(path);//图片路径赋值
                    upLoadFile(path, "头像", "jpg");
                    LogUtil.e("相机获取到的文件路径为：" + path);
                    LogUtil.e("相机获取到的文件uri为：" + data.getData());
                }
            } else {
                if (resultCode == RESULT_OK && data != null) {
                    String path = PhotoUtils.getPath(mActivity, data.getData());
                    setPicPath(path);//图片路径赋值
                    upLoadFile(path, "头像", "jpg");
                }
            }

        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {

            if (data != null) {//TODO 必须分开判断，不然不拍摄照片直接返回的话就会报空指针异常：data.getExtras()
                if (data.getExtras() != null) {
                    if (data.getExtras().get("data") != null /*|| data.getExtras().get("data").equals("")*/) {
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
//                        avatar.setImageBitmap(bitmap);// 将图片显示在ImageView里
                        //x.image().bind(avatar,mUserDao.avatar,mImageOptions);
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
                        //setFilePath(fileName);
                        setPicPath(fileName);
                        upLoadFile(fileName, "头像", "jpg");
                    }

                }
            }

            //setPicPath(fileName);
            //  相机照相获取文件路径结束
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
        LogUtil.e("传入的路径名==" + filepath);
        String io = MyBase64.file2String(filepath);
        dialog = ProgressDialog.show(getActivity(), "上传文件", "正在上传", true, false);
        final RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));

        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));

        params.addBodyParameter("action", "submit");
        params.addBodyParameter("method", "set_user_avatar_asname");
        params.addBodyParameter("asname", "asname");
        params.addBodyParameter("name", "asname");

        params.addBodyParameter("avatar", io);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传结果为！！！！======" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.optBoolean("success", false);
                    if (!success) {
                        MyToast.showShort(getActivity(), "上传失败，请联系管理员");
                        LogUtil.e("上传失败，请联系管理员！！！！======");
                    } else {
                        MyToast.showShort(getActivity(), "上传成功！");
                        LogUtil.e("上传成功！！！！======");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

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
                MyBitmapUtils.getInstance(getActivity()).clearCache(mUserDao.avatar);
                bitmapUtils.display(avatar, mUserDao.avatar);
                MainActivity a = (MainActivity) getActivity();
                a.setUserIcon();
                dialog.dismiss();
//                bitmapUtils = new BitmapUtils(getActivity(), AddressUtils.img_cache_url);
//                bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);
//                bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);
//                MyBitmapUtils.getInstance(getActivity()).clearCache(mUserDao.avatar);
//                MyBitmapUtils.getInstance(getActivity()).display(avatar, mUserDao.avatar);


                //获取到MainActivity的实例，设置头像图片

            }
        });


    }

}

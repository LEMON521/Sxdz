package cn.net.bjsoft.sxdz.fragment.bartop.function;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.welcome.LinkToActivity;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.HomepageBean;
import cn.net.bjsoft.sxdz.bean.app.ModulesBean;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersSingleBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.view.RoundImageView;

/**
 * Created by Zrzc on 2017/1/10.
 */

@ContentView(R.layout.fragment_sign)
public class TopSignFragment extends BaseFragment {
    @ViewInject(R.id.title)
    private TextView title;
    //    @ViewInject(R.id.titlebar)
//    RelativeLayout titlebar;
    @ViewInject(R.id.sign_back)
    private ImageView back;
    @ViewInject(R.id.sign_user_icon)
    private RoundImageView userIcon;
    @ViewInject(R.id.sign_name)
    private TextView name;
    @ViewInject(R.id.sign_bumen)
    private TextView bumen;
    @ViewInject(R.id.sign_time)
    private TextView time;
    @ViewInject(R.id.sign_address)
    private TextView address;
    @ViewInject(R.id.sign_history)
    private LinearLayout sign_history;
    @ViewInject(R.id.history)
    private TextView history;
    @ViewInject(R.id.sign_picture)
    private RoundImageView picture;
    //    @ViewInject(R.id.history_back)
//    LinearLayout historyBack;
    @ViewInject(R.id.sing_past_text)
    private TextView pastText;
    @ViewInject(R.id.sign_pic_icon)
    private ImageView picIcon;
    @ViewInject(R.id.sign_btn)
    private ImageView pastBtn;
    @ViewInject(R.id.sign_last_time)
    private TextView oletime;
    @ViewInject(R.id.sign_last_address)
    private TextView oldaddress;

    private BitmapUtils bitmapUtils;

    private String url_image = "";

    private String imgdata = "";

    private AppProgressDialog progressDialog;

    private String mJson = "";

    private TopSignFragment mFragment;
    
    private UsersSingleBean usersSingleBean;
    private AppBean appBean;
    private ArrayList<HomepageBean> homepageBeen;
    private ModulesBean modulesBean;

    private String source_id = "";
    private String submit_id = "";

    private ImageOptions imageOptions;

    @Override
    public void initData() {
        mFragment =this;
        
        imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.get_back_passwoed) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.drawable.get_back_passwoed).build();

        appBean = GsonUtil.getAppBean(SPUtil.getMobileJson(getActivity()));

        homepageBeen = appBean.homepage;
        modulesBean = new ModulesBean();

        for (int i = 0; i < homepageBeen.size(); i++) {
            if (homepageBeen.get(i).tag.equals("signin")) {
                modulesBean.signin = homepageBeen.get(i).tag_params;
            }
        }

        if (modulesBean.signin != null && !(modulesBean.signin.size() > 0)) {
            modulesBean.signin = appBean.modules.signin;
        }

        if (modulesBean.signin != null && modulesBean.signin.size() > 0) {
            source_id = modulesBean.signin.get("source_id");
            submit_id = modulesBean.signin.get("submit_id");
        }

        usersSingleBean = UsersInforUtils.getInstance(getActivity()).getUserInfo(SPUtil.getUserId(getActivity()));

        setData();
    }

    public void setData() {

        time.setText(TimeUtils.getFormateTime(System.currentTimeMillis(), "-", ":"));


        x.image().bind(userIcon, usersSingleBean.avatar, imageOptions);
        name.setText(usersSingleBean.nickname);
//      oldaddress.setText(InitModel.getInstance(getActivity()).getUserData().getAddress());
//      oletime.setText(InitModel.getInstance(getActivity()).getUserData().getSigntime());
        //----------------------注意


//        if (mDatasBean.data.pushdata.sign_last != null) {
//            if (mDatasBean.data.pushdata.sign_last.address != null) {
//                if (!mDatasBean.data.pushdata.sign_last.address.equals("")) {
//                    oldaddress.setText(mDatasBean.data.pushdata.sign_last.address);
//                }
//            } else {
//                oldaddress.setText("未获取到信息");
//            }
//            if (mDatasBean.data.pushdata.sign_last.signtime != null) {
//                if (!mDatasBean.data.pushdata.sign_last.signtime.equals("")) {
//                    oletime.setText(mDatasBean.data.pushdata.sign_last.signtime);
//                }
//            } else {
//                oletime.setText("未获取到信息");
//            }
//            if (SPUtil.getAddress(getActivity()) != null) {
//                if (!SPUtil.getAddress(getActivity()).equals("")) {
//                    address.setText(SPUtil.getAddress(getActivity()));
//                }
//            } else {
//                address.setText("未获取到信息");
//            }
//        } else {
//            oldaddress.setText("未获取到信息");
//            oletime.setText("未获取到信息");
//            address.setText("未获取到信息");
//
//        }


    }

    @Event(type = View.OnClickListener.class, value = {R.id.sign_back, R.id.sign_history, R.id.sign_picture, R.id.sign_btn})
    private void onSignClick(View view) {
        switch (view.getId()) {
            case R.id.sign_back:
                //MyToast.showShort(mActivity, "返回");
                mActivity.finish();

                break;
            case R.id.sign_history:
                //MyToast.showShort(mActivity, "查看历史");
                Intent i = new Intent(getActivity(), LinkToActivity.class);
//                i.putExtra("url", CacheSerializableUtil.getlist(getActivity()).getToolBar().getSignurl());
                i.putExtra("url", "www.baidu.com");
                i.putExtra("title", "网页");
                startActivity(i);
                break;
            case R.id.sign_picture:
                PackageManager pm = getActivity().getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                if (!permission) {
                    MyToast.showLong(mActivity, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                    return;
                }
                // MyToast.showShort(mActivity, "拍照片");
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    mFragment.startActivityForResult(intent, 100);
                    MyToast.showShort(getActivity(), "打开了照相机！");
                } else {
                    Toast.makeText(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.sign_btn:

                break;
        }
    }

    /**
     * 签到
     */
    public void upData() {
//        RequestParams params = new RequestParams(AddressUtils.httpurl);
//        params.addBodyParameter("client_name", Constants.app_name);
//        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
//        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
//        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
//        params.addBodyParameter("method", "auto_position");
//        params.addBodyParameter("action", "submit");
//        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));
//        params.addBodyParameter("abs_x", SPUtil.getLong(getActivity()) + "");
//        params.addBodyParameter("abs_y", SPUtil.getLat(getActivity()) + "");
//        params.addBodyParameter("abs_z", "");
//        params.addBodyParameter("address", SPUtil.getAddress(getActivity()));
//        params.addBodyParameter("imgdata", imgdata);
//
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                dismissProgressDialog();
//                Log.e("tag", "video" + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject != null) {
//                        boolean tag = jsonObject.optBoolean("success");
//                        if (tag) {
//                            MyToast.showShort(getActivity(), "签到成功");
////                            InitModel.getInstance(getActivity()).getUserData().setAddress(jsonObject.optJSONObject("data").optString("address"));
////                            InitModel.getInstance(getActivity()).getUserData().setSigntime(jsonObject.optJSONObject("data").optString("signtime"));
////                            oldaddress.setText(jsonObject.optJSONObject("data").optString("address"));
////                            oletime.setText(jsonObject.optJSONObject("data").optString("signtime"));
//                        } else {
//                            MyToast.showShort(getActivity(), jsonObject.optString("feedback"));
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//                MyToast.showShort(getActivity(), "网络连接异常");
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                dismissProgressDialog();
//            }
//        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("上传成功头像onSuccess1111" +  SPUtil.getAvatar(getActivity()));
        if (data != null) {
            LogUtil.e("上传成功头像onSuccess2222" +  SPUtil.getAvatar(getActivity()));
            Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
            if (uri != null) {
                LogUtil.e("上传成功头像onSuccess3333" +  SPUtil.getAvatar(getActivity()));
                String imagePath = PhotoOrVideoUtils.getPath(mActivity, uri);
                //upLoadFile(imagePath, "", "");
                upLoadAvatar(imagePath);
            }
        }

    }

    /**
     * 上传图片到服务器
     *
     * @param imagePath
     */
    private void upLoadAvatar(String imagePath) {
        LogUtil.e("上传成功头像onSuccess444" +  SPUtil.getAvatar(getActivity()));
        showProgressDialog();
        RequestParams params = new RequestParams(SPUtil.getApiUpload(mActivity));
        params.setMultipart(true);
        File file = new File(imagePath);

        params.addBodyParameter("avatar", file);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传成功onSuccess" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("code") == 0) {
                        SPUtil.setAvatar(mActivity, jsonObject.optJSONObject("data").optString("src"));

                        LogUtil.e("上传成功头像onSuccess" +  SPUtil.getAvatar(getActivity()));
                    } else {
                        MyToast.showLong(mActivity, "上传头像失败,请联系管理员");
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

    public synchronized void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        progressDialog.show(getActivity());
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismissDialog();
        }
    }

    public synchronized AppProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        return progressDialog;
    }
}

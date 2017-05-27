package cn.net.bjsoft.sxdz.fragment.bartop.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
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

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.welcome.NewSplashActivity;
import cn.net.bjsoft.sxdz.activity.welcome.SplashActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.user.UserBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * 用户
 * Created by 靳宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_top_user)
public class TopUserFragment extends BaseFragment {
    @ViewInject(R.id.user_icon)
    private CircleImageView avatar;
    @ViewInject(R.id.user_name)
    private TextView name;

    private ProgressDialog dialog;//上传时的提示框
    private UserBean mUserDao;
    private ImageOptions mImageOptions;
    private BitmapUtils bitmapUtils;

    private String imagePath = "";

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mUserDao = GsonUtil.getDatasBean(mJson).data.user;
//        mImageOptions = new ImageOptions.Builder().setCircular(true).setUseMemCache(false).build();
//        view = x.view().inject(this, inflater, null);
//        return view;
//    }

    @Override
    public void initData() {
        mUserDao = GsonUtil.getUserBean(SPUtil.getUserJson(mActivity));
        mImageOptions = new ImageOptions.Builder().setCircular(true).setUseMemCache(false).build();
        bitmapUtils = new BitmapUtils(getActivity(), AddressUtils.img_cache_url);
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);
        //获取详细信息
        //detail = getArguments().getStringArrayList("detail");
        if (mUserDao.avatar.equals("")) {
            avatar.setImageBitmap(BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.wlh));
        } else {
            bitmapUtils.display(avatar, mUserDao.avatar);
        }
        name.setText(mUserDao.name);
    }


    @Event(value = {R.id.user_icon/*头像*/,
            R.id.user_back, R.id.user_exit, R.id.user_function3})
    private void userOnClick(View view) {
        switch (view.getId()) {
            case R.id.user_icon:
                PhotoOrVideoUtils.doPhoto(mActivity, this, view);
                break;
            case R.id.user_back:
                mActivity.finish();
                break;
            case R.id.user_exit:
                Intent i = new Intent(getActivity(), SplashActivity.class);
                startActivity(i);
                SPUtil.setUserUUID(getActivity(), "");
                getActivity().finish();

                break;

            case R.id.user_function3://注销

//                Intent intent = new Intent(getActivity(), SplashActivity.class);
//                startActivity(intent);
//                SPUtil.setUserUUID(getActivity(), "");
//                getActivity().finish();
                new AlertDialog.Builder(mActivity).setTitle("友情提示").setMessage("确定要退出登录吗?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                logoutOnservice();//从服务器注销

                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
                break;
        }
    }
    /**
     * 告诉服务器__注销登录
     */
    private void logoutOnservice() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(SPUtil.getLogoutApi(mActivity));
        //params.setMultipart(true);
        httpPostUtils.post(mActivity, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("logoutOnservice---注销onSuccess" + strJson);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(strJson);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {


                        CloudPushService pushService = PushServiceFactory.getCloudPushService();
                        pushService.unbindAccount(new CommonCallback() {
                            @Override
                            public void onSuccess(String s) {
                                LogUtil.e("推送解绑状态-----成功===" + s);
                            }

                            @Override
                            public void onFailed(String s, String s1) {
                                LogUtil.e("推送解绑状态-----失败===" + s + "::::::" + s1);
                            }
                        });
                        SPUtil.setUserUUID(getActivity(), "");
                        SPUtil.setUserId(getContext(), "");
                        SPUtil.setToken(getContext(), "");
                        SPUtil.setAvatar(getContext(), "");


                        Intent i = new Intent(getActivity(), NewSplashActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        MyToast.showLong(mActivity, "注销失败!");
                        MyToast.showLong(mActivity, jsonObject.optString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(mActivity, "注销失败,请联系管理员");
                LogUtil.e("上传失败onError========UpDateAvatar" + ex);
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
            imagePath = PhotoOrVideoUtils.getPath(mActivity, uri);
            upLoadFile(imagePath, "", "");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传动作
     *
     * @param filepath 要上传的文件路径
     * @param number   要上传文件的对应的编号
     */
    public void upLoadFile(String filepath, String number, String ext) {
        LogUtil.e("传入的路径名==" + filepath);
        String io = MyBase64.file2String(filepath);
        dialog = ProgressDialog.show(getActivity(), "更新头像", "正在上传新头像", true, false);
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
                if (getActivity() instanceof MainActivity) {
                    MainActivity a = (MainActivity) getActivity();
                    a.setUserIcon();
                }
                dialog.dismiss();
            }
        });

    }
}

package cn.net.bjsoft.sxdz.fragment.ylyd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.welcome.NewSplashActivity;
import cn.net.bjsoft.sxdz.activity.ylyd.ShowYLYDActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MD5;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;
import cn.net.bjsoft.sxdz.view.WindowRecettingPasswordView;


/**
 * 裕隆亚东药业的我的页面   - -   我的页面
 * Created by 靳宁宁 on 2017/3/8.
 */
@ContentView(R.layout.fragment_mine_ylyd)
public class BottonMineYuLongYaDongFragment extends BaseFragment {
    @ViewInject(R.id.title_back)
    private ImageView back;//返回
    @ViewInject(R.id.title_title)
    private TextView title;//标题

    @ViewInject(R.id.fragment_mine_ylyd_tv_name)
    private TextView name;
    @ViewInject(R.id.fragment_mine_ylyd_tv_quarters)
    private TextView quarters;
    @ViewInject(R.id.fragment_mine_ylyd_tv_company)
    private TextView company;
    @ViewInject(R.id.fragment_mine_ylyd_tv_resetting)
    private TextView resetting;


    private WindowRecettingPasswordView passwordView;
    private Dialog dialog;
    private String password = "";
    private String old_password = "";
    @Override
    public void initData() {
        title.setText("我的");
    }


    /**
     * 待我审批，审批中，审批历史切换事件
     *
     * @param view
     */
    @Event(value = {R.id.title_back, R.id.fragment_mine_ylyd_ll_resetting, R.id.fragment_mine_ylyd_tv_exit})
    private void approveChangeOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
              mActivity.finish();
                break;
            case R.id.fragment_mine_ylyd_ll_resetting:
//                passwordView = new WindowRecettingPasswordView(mActivity);
//                showRecettingPassword(mActivity,passwordView);

                Intent intent = new Intent(mActivity, ShowYLYDActivity.class);
                intent.setAction("resetting_password");
                startActivity(intent);
                break;

            case R.id.fragment_mine_ylyd_tv_exit:
//                SPUtil.setUserUUID(getActivity(), "");
//                Intent i = new Intent(getActivity(), SplashActivity.class);
//                startActivity(i);
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

    /**
     * 弹出修改密码窗口
     * @param context
     * @param view
     */
    public void showRecettingPassword(Context context, final WindowRecettingPasswordView view){
        if (dialog == null) {
            dialog = new Dialog(context, R.style.MIDialog);

            view.getConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.submit();//获取到的数据
                    if (!view.getPassword().equals("")){
                        password = view.getPassword();
                        old_password = view.getOldPassword();
                        LogUtil.e("获取到的密码为===="+password);
                        LogUtil.e("获取到的原始密码为===="+old_password);
                        setPassword();//设置密码
                        dialog.dismiss();
                    }
                }
            });

            view.getCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.setCancelable(false);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 访问网络进行修改密码
     */
    private void setPassword(){
        final RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));
        params.addBodyParameter("action", "submit");


        params.addBodyParameter("method", "edit_password");
        params.addBodyParameter("old_password", MD5.getMessageDigest(old_password.getBytes()));
        params.addBodyParameter("password", MD5.getMessageDigest(password.getBytes()));

        //LogUtil.e("params"+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("修改密码结果为！！！！======" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.optBoolean("success", false);
                    if (!success) {
                        MyToast.showShort(getActivity(), jsonObject.optString("feedback", "密码修改失败"));
                    } else {
                        SPUtil.setUserUUID(mActivity,jsonObject.optString("uuid", ""));
                        MyToast.showShort(getActivity(), "修改密码成功！");
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
}

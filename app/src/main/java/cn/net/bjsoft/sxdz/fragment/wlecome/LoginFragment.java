package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.UserActivity;
import cn.net.bjsoft.sxdz.activity.login.ForgetPasswordActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.activity.login.RegisterActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.DeviceIdUtils;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MDUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;


/**
 * Created by Zrzc on 2016/12/30.
 */

@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseFragment {

    @ViewInject(R.id.user_edit)
    private EditText userEdit;
    @ViewInject(R.id.login_tv_title)
    private TextView title;
    @ViewInject(R.id.pass_edit)
    private EditText passEdit;
    @ViewInject(R.id.fm_btn_login_login)
    private Button btnLogin;
    @ViewInject(R.id.regiest)
    private TextView regiest;
    @ViewInject(R.id.forget)
    private TextView forget;
    @ViewInject(R.id.line)
    private ImageView line;
    @ViewInject(R.id.login_logo)
    private ImageView loginLogo;
    @ViewInject(R.id.login_back)
    private ImageView back;
    //    @ViewInject(R.id.login_title)
//    private TextView login_title;
    @ViewInject(R.id.bg)
    private ImageView bg;

    private View view;
    private boolean submitLock = false;

    private String json = "";
    private DatasBean datasBean;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        json = getActivity().getIntent().getStringExtra("json");
        datasBean = GsonUtil.getDatasBean(json);
        context = getContext();
        title.setText(datasBean.data.login.btntext);
        //SPUtil.setUserUUID(getActivity(),"");

        setLogo();
        if (getArguments().getString("getBack") != null) {
            if (getArguments().getString("getBack").equals("false")) {
                back.setVisibility(View.GONE);
            }
        }

        btnLogin.setText(datasBean.data.login.btntext);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("点击登录按钮了");
                if (userEdit.getText().toString().isEmpty() || passEdit.getText().toString().isEmpty()) {
                    MyToast.showShort(getActivity(), "用户名和密码不能为空");
                } else {
                    LogUtil.e("登录方法");
                    login();
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(i2);
            }
        });
        regiest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                i.putExtra("url", "");
                i.putExtra("json", mJson);
                i.putExtra("function", "register");
                startActivity(i);
                //MyToast.showShort(getActivity(), "暂时无法注册，请联系管理员");
            }
        });
    }

    @Override
    public void initData() {
        LogUtil.e("mActivity=&&&&&&&&&&=" + mActivity);
    }

    /**
     * 设置logo和按钮
     */
    public void setLogo() {

        x.image().bind(bg, datasBean.data.login.background);
//        @ViewInject(R.id.regiest)
//        private TextView regiest;
//        @ViewInject(R.id.forget)
//        private TextView forget;
        if (datasBean.data.login.canregister) {
            regiest.setVisibility(View.VISIBLE);
        } else {
            regiest.setVisibility(View.GONE);
        }
        if (datasBean.data.login.resetpassword) {
            forget.setVisibility(View.VISIBLE);
        } else {
            forget.setVisibility(View.GONE);
        }
        if (datasBean.data.login.resetpassword && datasBean.data.login.canregister) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
//        login_title.setText(datasBean.data.title);
//
        //方便开发，讲数据填充到EditorText中
        userEdit.setText("demo");
        passEdit.setText("demo");
        //title.setVisibility(View.GONE);
        //zhuce = getIntent().getStringExtra("zhuce");--没有url，暂时屏蔽
    }

    /**
     * 登录
     */
    public void login() {
        LogUtil.e("验证submitLock==" + submitLock);
        if (submitLock) {
            return;
        }
        submitLock = true;
//        getContext().showProgressDialog();
        RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("app_id", Constants.app_id);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("action", AddressUtils.login_action);
        params.addBodyParameter("method", AddressUtils.login_method);
        params.addBodyParameter("username", userEdit.getText().toString());
        params.addBodyParameter("password", MDUtil.MD5(passEdit.getText().toString()));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(getActivity()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                submitLock = false;
//                dismissProgressDialog();
                LogUtil.e("登录返回值=onSuccess=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean success = jsonObject.optBoolean("success");
                        if (success) {

                            SPUtil.setUserRandCode(getActivity(), jsonObject.optString("randcode"));
                            SPUtil.setUserEmail(getActivity(), jsonObject.optString("email"));
                            SPUtil.setUserId(getActivity(), jsonObject.optString("user_id"));
                            SPUtil.setUserName(getActivity(), jsonObject.optString("name"));
                            SPUtil.setUserUUID(getActivity(), jsonObject.optString("uuid"));

                            getDeviceId();
                        } else {
                            MyToast.showShort(getActivity(), jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                submitLock = false;

                LogUtil.e("登录返回值=onError=" + ex);
                LogUtil.e("登录返回值=isOnCallback=" + isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("登录返回值=onCancelled=" + cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("登录返回值=onFinished=");
            }
        });


    }

    /**
     * 初始化
     */
    public void getDeviceId() {
        //showProgressDialog();
        RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("app_id", Constants.app_id);
        params.addBodyParameter("appname", Constants.app_name);
        params.addBodyParameter("phone_uuid", DeviceIdUtils.getDeviceId(getActivity()));//"shufuma"
        //Log.e("tag",DeviceIdUtils.getDeviceId(this));
        params.addBodyParameter("ostype", "android");
        params.addBodyParameter("action", "init");
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));//"c2h1eGRAYmpzb2Z0Lm5ldC5jbnw5NmU3OTIxODk2NWViNzJjOTJhNTQ5ZGQ1YTMzMDExMnx0cnVl"
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("初始化返回值=onSuccess=" + result);
                json = "";
                json = result;
                datasBean = null;
                datasBean = GsonUtil.getDatasBean(json);
                if (datasBean.success) {//数据请求成功
                    SPUtil.setUserEmail(getActivity(), datasBean.data.user.email);
                    SPUtil.setUserId(getActivity(), datasBean.data.user.user_id);
                    SPUtil.setUserName(getActivity(), datasBean.data.user.name);
                    SPUtil.setUserUUID(getActivity(), datasBean.data.user.uuid);
                    SPUtil.setUserRandCode(getActivity(), datasBean.data.user.randcode);
                    SPUtil.setUserPUUID(getActivity(), datasBean.data.user.phone_uuid);
                    SPUtil.setAvatar(getActivity(), datasBean.data.user.avatar);
                    LogUtil.e("头像====="+datasBean.data.user.avatar);
                    //T.showShort(LoginActivity.this, datasBean.toString());
                    Intent i3 = new Intent(getActivity(), MainActivity.class);
                    LogUtil.e("数据请求=json=" + json);
                    i3.putExtra("json", json);
                    startActivity(i3);
                    LogUtil.e("mActivity=&&&&&&&&&&=" + mActivity);
                    LogUtil.e("getActivity=&&&&&&&&&&=" + getActivity());
                    LogUtil.e("getContext=&&&&&&&&&&=" + getContext());
                    if (getActivity() instanceof MainActivity) {
                        getActivity().finish();
                    }
                    if (getActivity() instanceof UserActivity) {
                        MainActivity.getMainActivity().finish();//不这样做的话，登录之后，点击返回按钮会返回到未登录状态的主页
                        getActivity().finish();
                    }
                    if (getActivity() instanceof LoginActivity) {
                        LoginActivity.getLoginActivity().finish();//不这样做的话，登录之后，点击返回按钮会返回到未登录状态的主页
                        getActivity().finish();
                    }


                } else {//数据请求失败
                    MyToast.showShort(getActivity(), "数据解析失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(getActivity(), "网络连接错误");
                LogUtil.e("数据请求=onCancelled=" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("数据请求=onCancelled=" + cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("数据请求=onFinished=");
            }
        });
    }

    @Event(value = {R.id.login_back})
    private void loginOnclick(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                getActivity().finish();
                break;
        }
    }

}

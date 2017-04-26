package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.LoginBean;
import cn.net.bjsoft.sxdz.bean.app.logined.LoginedBean;
import cn.net.bjsoft.sxdz.bean.app.logined.LoginedDataBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MDUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;


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
    //private DatasBean datasBean;
    private AppBean appBean;
    private LoginBean loginBean;
    private LoginedBean loginedBean;
    private LoginedDataBean loginedDataBean;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        json = getActivity().getIntent().getStringExtra("json");
        appBean = GsonUtil.getAppBean(json);
        loginBean = appBean.login;

        context = getContext();
        title.setText(loginBean.btntext);
        //SPUtil.setUserUUID(getActivity(),"");

        setLogo();
        if (getArguments().getString("getBack") != null) {
            if (getArguments().getString("getBack").equals("false")) {
                back.setVisibility(View.GONE);
            }
        }

        btnLogin.setText(loginBean.btntext);

    }

    @Override
    public void initData() {
        LogUtil.e("mActivity=&&&&&&&&&&=" + mActivity);
    }

    /**
     * 设置logo和按钮
     */
    public void setLogo() {

        x.image().bind(bg, loginBean.background);
        if (loginBean.canregister) {
            regiest.setVisibility(View.VISIBLE);
        } else {
            regiest.setVisibility(View.GONE);
        }
        if (loginBean.resetpassword) {
            forget.setVisibility(View.VISIBLE);
        } else {
            forget.setVisibility(View.GONE);
        }
        if (!loginBean.passreset.equals("") && loginBean.canregister) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
//        login_title.setText(datasBean.data.title);
//
        //方便开发，讲数据填充到EditorText中
        userEdit.setText("shuxd@bjsoft.net.cn");
        //userEdit.setText("pengdeqiang@bjsoft.net.cn");
        passEdit.setText("111111");
        //title.setVisibility(View.GONE);
        //zhuce = getIntent().getStringExtra("zhuce");--没有url，暂时屏蔽
    }

    /**
     * 登录
     */

    public void login() {
        if (userEdit.getText().toString().isEmpty() || passEdit.getText().toString().isEmpty()) {
            MyToast.showShort(getActivity(), "用户名和密码不能为空");
            return;
        }

        showProgressDialog();
        //showProgressDialog();
        HttpPostUtils postUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(loginBean.loginapi);
        params.addBodyParameter("username", userEdit.getText().toString());
        params.addBodyParameter("password", MDUtil.MD5(passEdit.getText().toString()));


        postUtils.post(getContext(), params);
        postUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("登录结果" + strJson);
                loginedBean = GsonUtil.getLoginedBean(strJson);

                if (Integer.parseInt(loginedBean.data.userid) > 0) {//如果登录成功,那么user_id是>0的
                    loginedDataBean = loginedBean.data;

                    SPUtil.setUserId(getContext(), loginedDataBean.userid);
                    SPUtil.setToken(getContext(), loginedDataBean.token);
                    //SPUtil.setIsMember(getContext(), loginedDataBean.ismember);
                    SPUtil.setAvatar(getContext(), loginedDataBean.avatar);
                    SPUtil.setLoginUserName(getContext(),loginedDataBean.loginname);


                    LogUtil.e("登录结果");

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
                } else {

                    MyToast.showLong(getActivity(), "登录失败,用户名或密码错误");
                    passEdit.setText("");

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(getContext(), "登录失败");
                LogUtil.e("登录返回值=onError=" + ex);
                LogUtil.e("登录返回值=isOnCallback=" + isOnCallback);
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

    @Event(value = {R.id.login_back
            , R.id.fm_btn_login_login
            , R.id.regiest
            , R.id.forget})
    private void loginOnclick(View v) {
        switch (v.getId()) {
            case R.id.login_back://返回
                getActivity().finish();
                break;

            case R.id.fm_btn_login_login://登录
                login();
                break;

            case R.id.regiest://注册
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                i.putExtra("url", "");
                i.putExtra("json", mJson);
                i.putExtra("function", "register");
                startActivity(i);
                break;

            case R.id.forget://忘记密码
                Intent i2 = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(i2);
                break;

            case EditorInfo.IME_FLAG_NO_ENTER_ACTION:
                login();
                break;

        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//            /*隐藏软键盘*/
//            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            if(inputMethodManager.isActive()){
//                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//            }
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }

}

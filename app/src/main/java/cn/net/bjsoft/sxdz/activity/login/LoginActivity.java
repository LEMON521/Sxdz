package cn.net.bjsoft.sxdz.activity.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.fragment.WebViewFragment;
import cn.net.bjsoft.sxdz.fragment.wlecome.LoginFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * Created by 靳宁宁 on 2017/1/5.
 */

@ContentView(R.layout.activity_empty)
public class LoginActivity extends BaseActivity {

//    @ViewInject(R.id.activity_empty_root)
//    private FrameLayout fl_root;
    //@ViewInject(R.id.activity_empty_root)

    private static LoginActivity activity;
    private String json = "";
    private Fragment fragment;
    //private DatasBean datasBean;
    private AppBean appBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        activity = this;
        json = getIntent().getExtras().getString("json");
        appBean = GsonUtil.getAppBean(json);

        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        bundle.putString("setBack", "false");

        if (appBean.login == null) {
            fragment = new LoginFragment();
        } else if (appBean.login.loginurl == null) {
            fragment = new LoginFragment();

        } else if (appBean.login.loginurl.equals("")) {
            fragment = new LoginFragment();

        } else {
            bundle.putString("text", appBean.login.btntext);
            bundle.putString("url", appBean.login.loginurl);
            bundle.putString("tag", "login");
            fragment = new WebViewFragment();
        }
        fragment.setArguments(bundle);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "LOGIN")
                .commit();

    }

    public static LoginActivity getLoginActivity() {
        return activity;
    }
}

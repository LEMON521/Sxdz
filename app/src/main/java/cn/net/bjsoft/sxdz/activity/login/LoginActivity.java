package cn.net.bjsoft.sxdz.activity.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
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
    private DatasBean datasBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        activity = this;
        json = getIntent().getExtras().getString("json");
        datasBean = GsonUtil.getDatasBean(json);

        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        bundle.putString("setBack", "false");

        if (datasBean.data.login == null) {
            fragment = new LoginFragment();
        } else if (datasBean.data.login.loginurl == null) {
            fragment = new LoginFragment();

        } else if (datasBean.data.login.loginurl.equals("")) {
            fragment = new LoginFragment();

        } else {
            bundle.putString("text", datasBean.data.login.btntext);
            bundle.putString("url", datasBean.data.login.loginurl);
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

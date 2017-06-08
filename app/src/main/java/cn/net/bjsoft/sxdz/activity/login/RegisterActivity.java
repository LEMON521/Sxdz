package cn.net.bjsoft.sxdz.activity.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.fragment.wlecome.RegisterCompanyFragment;
import cn.net.bjsoft.sxdz.fragment.wlecome.RegisterFragment;
import cn.net.bjsoft.sxdz.fragment.wlecome.RegisterHumenFragment;

/**
 * Created by Zrzc on 2017/2/14.
 */
@ContentView(R.layout.activity_empty)
public class RegisterActivity extends BaseActivity {

    private static RegisterActivity activity;
    private String function = "";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        activity = this;
        Bundle bundle = new Bundle();
        bundle.putString("json", "");

        function = getIntent().getStringExtra("function");
        bundle.putString("username", getIntent().getStringExtra("username"));

        //LogUtil.e("RegisterActivity中@@@@@@@"+"getActivity()::"+this+"/n"+"getContext()::"+getBaseContext());
        if (function.equals("register")) {
            fragment = new RegisterFragment();
        } else if (function.equals("register_humen")) {
            fragment = new RegisterHumenFragment();
        } else if (function.equals("register_company")) {
            fragment = new RegisterCompanyFragment();

        }


        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "REGESTER")
                .commit();

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        LogUtil.e("Activity获取图片URI" + PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data).toString());
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    public static RegisterActivity getLoginActivity() {
        return activity;
    }

}

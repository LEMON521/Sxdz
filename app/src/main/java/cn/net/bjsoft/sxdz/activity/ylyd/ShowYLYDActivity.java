package cn.net.bjsoft.sxdz.activity.ylyd;

import android.os.Bundle;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.ylyd.YLYDResettingPasswordFragment;

/**
 * Created by Zrzc on 2017/3/13.
 */

@ContentView(R.layout.activity_empty)
public class ShowYLYDActivity extends BaseActivity {

    private String action = "";
    private BaseFragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        action = getIntent().getAction().toString();
        LogUtil.e("action==="+action);
        if (action!=null){
            Bundle bundle = new Bundle();
            bundle.putString("json","");
            if (action.equals("resetting_password")){//修改密码
                fragment =new  YLYDResettingPasswordFragment();
            }
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_empty_root, fragment, action)
                    .commit();

        }
    }
}

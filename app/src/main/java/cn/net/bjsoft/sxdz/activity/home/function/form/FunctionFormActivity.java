package cn.net.bjsoft.sxdz.activity.home.function.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.function.form.FunctionFormFragment;

/**
 * Created by Zrzc on 2017/5/15.
 */
@ContentView(R.layout.activity_function_form)
public class FunctionFormActivity extends FragmentActivity{

    private FunctionFormFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initData();

        getData();
    }

    private void initData(){
        fragment = new FunctionFormFragment();

        Bundle bundle = getIntent().getBundleExtra("form_data");

        LogUtil.e("========bundle========"+bundle.getString("title"));

        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_function_form_content
                        , fragment
                        , "FunctionFormFragment")
                .commit();

    }

    private void getData(){

    }
}

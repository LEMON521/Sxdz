package cn.net.bjsoft.sxdz.activity.home.bartop.message;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.add_approve.TopApproveNewFragment;

/**
 * 新建审批页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_empty)
public class ApproveNewActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        TopApproveNewFragment fragment = new TopApproveNewFragment();

        Bundle bundle = new Bundle();
        bundle.putString("json",getIntent().getStringExtra("json"));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "APPROVE_NEW")
                .commit();
    }

}

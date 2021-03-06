package cn.net.bjsoft.sxdz.activity.home.bartop.message;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.approve.add_approve.TopApproveNewParticularFragment;

/**
 * 新建审批页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_empty)
public class ApproveNewParticularActivity extends BaseActivity {

    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


        fragment = new TopApproveNewParticularFragment();

        Bundle bundle = new Bundle();
        bundle.putString("json",getIntent().getStringExtra("json"));
        bundle.putInt("particular",getIntent().getIntExtra("particular",1));
        bundle.putString("title",getIntent().getStringExtra("title"));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_empty_root, fragment, "APPROVE_PARTICULAR")
                .commit();
    }

}

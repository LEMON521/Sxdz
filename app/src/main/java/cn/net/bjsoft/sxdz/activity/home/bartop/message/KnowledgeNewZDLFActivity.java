package cn.net.bjsoft.sxdz.activity.home.bartop.message;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.KnowledgeNewZDLFFragment;

/**
 * Created by Zrzc on 2017/4/19.
 */
@ContentView(R.layout.activity_empty)
public class KnowledgeNewZDLFActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fragment_name = "KnowledgeNewZDLFFragment";

        BaseFragment fragment = new KnowledgeNewZDLFFragment();
        Bundle bundle = new Bundle();
        bundle.putString("json", "");
        fragment.setArguments(bundle);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_empty_root
                        , fragment
                        , fragment_name)
                .commit();

    }


}

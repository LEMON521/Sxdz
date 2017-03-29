package cn.net.bjsoft.sxdz.activity;

import android.os.Bundle;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.AddressListFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.KnowledgeItemZDLFFragment;

/**
 * Created by Zrzc on 2017/3/20.
 */
@ContentView(R.layout.activity_empty)
public class EmptyActivity extends BaseActivity {
//    @ViewInject(R.id.activity_empty_root)
//    private FrameLayout root;

    private String fragment_name = "";
    private BaseFragment fragment;


    @Override
    protected void onStart() {
        super.onStart();

        fragment_name = getIntent().getStringExtra("fragment_name");

        if (fragment_name != null) {
            if (!fragment_name.equals("")) {
                Bundle bundle = new Bundle();
                if (fragment_name.equals("knowledge_item")) {
                    fragment = new KnowledgeItemZDLFFragment();//中电联发知识详情条目
                    LogUtil.e("Fragment 的值为" + fragment_name);
                }else if (fragment_name.equals("addressList")) {
                    fragment = new AddressListFragment();//中电联发知识详情条目
                    LogUtil.e("Fragment 的值为" + fragment_name);
                }

                bundle.putString("json","");
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_empty_root
                                , fragment
                                , fragment_name)
                        .commit();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

package cn.net.bjsoft.sxdz.fragment.bartop.community;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.disabuse.HelpItemActivity;
import cn.net.bjsoft.sxdz.adapter.HelpItemAdapter;
import cn.net.bjsoft.sxdz.bean.community.HelpItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_help)
public class TopHelpFragment extends BaseFragment {

    @ViewInject(R.id.community_help_title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView test;
    @ViewInject(R.id.community_help_list)
    private ListView helpList;

    private ArrayList<HelpItemBean> helpsItem;
    private HelpItemBean helpItemBeans;
    private HelpItemAdapter helpItemAdapter;

    public void initData() {
        if (helpsItem == null){
            helpsItem = new ArrayList<>();
        }

        title.setText("帮助");
        test.setText("");
        MyToast.showShort(mActivity,"欢迎进入帮助页面");
        showProgressDialog();
        setData();

        helpItemAdapter = new HelpItemAdapter(mActivity,helpsItem);
        helpList.setAdapter(helpItemAdapter);

        helpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.e("点击了第"+position+"条帮助");
                Intent intent = new Intent();
                intent.setClass(getActivity(), HelpItemActivity.class);
                intent.putExtra("json",mJson);
                getContext().startActivity(intent);
            }
        });

        dismissProgressDialog();
    }

    /**
     * 向List添加数据（填充ListView 的数据信息）
     */
    private void setData() {
        if (helpsItem.size()>0){
            helpsItem.clear();
        }


        for (int i = 0;i<=10;i++){
            if (helpItemBeans !=null){
                helpItemBeans = null;
            }
            helpItemBeans = new HelpItemBean();
            helpItemBeans.titleHelp = "标题"+i;
            helpItemBeans.discriptionHelp = "描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，描述信息，"+i;
            helpItemBeans.imageUrlHelp = "http://api.shuxin.net/Data/biip/upload/OA_USERS/2/avatar.png";
            helpsItem.add(helpItemBeans);
        }
    }

    @Event(value = {R.id.community_help_back})
    private void helpOnclick(View view){
        switch (view.getId()){
            case R.id.community_help_back:
                getActivity().finish();
                break;
        }
    }
}

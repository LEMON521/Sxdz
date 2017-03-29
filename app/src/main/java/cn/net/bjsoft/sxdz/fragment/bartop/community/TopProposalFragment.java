package cn.net.bjsoft.sxdz.fragment.bartop.community;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.proposal.NewProposalActivity;
import cn.net.bjsoft.sxdz.adapter.ProposalAdapter;
import cn.net.bjsoft.sxdz.bean.community.ProposalItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * 建议页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_proposal)
public class TopProposalFragment extends BaseFragment {
    @ViewInject(R.id.community_proposal_title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView test;
    @ViewInject(R.id.community_proposal_list)
    private ListView proposalList;

    private ArrayList<ProposalItemBean> proposaItemList;
    private ProposalAdapter adapter;
    private ProposalItemBean proposalItemBean;


    @Override
    public void initData() {
        title.setText("建议");
        test.setText("建议页面");
        MyToast.showShort(mActivity,"欢迎进入建议页面");
        if(proposaItemList !=null){
            proposaItemList = null;
        }
        proposaItemList = new ArrayList<>();
        getData();
    }

    /**
     * 向列表添加数据
     */
    private void getData() {

        for (int i = 0 ; i<10;i++){
            if (proposalItemBean!=null){
                proposalItemBean = null;
            }
            proposalItemBean = new ProposalItemBean();
            proposalItemBean.avatarUrl = "http://api.shuxin.net/Data/biip/upload/OA_USERS/2/avatar.png";
            proposalItemBean.count = (100+i)+"";
            proposalItemBean.name = "名字"+i;
            proposalItemBean.time = "2017.01."+i;
            proposalItemBean.dis = "测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，"+i;
            proposaItemList.add(proposalItemBean);
        }


        adapter = new ProposalAdapter(getActivity(),proposaItemList);
        proposalList.setAdapter(adapter);

    }


    @Event(value = {R.id.community_proposal_back,R.id.community_proposal_new})
    private void proposalOnclick(View view){

        switch (view.getId()){
            case R.id.community_proposal_back:
                mActivity.finish();
                break;
            case R.id.community_proposal_new:
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewProposalActivity.class);
                intent.putExtra("json",mJson);
                startActivity(intent);
                break;
        }
    }
}

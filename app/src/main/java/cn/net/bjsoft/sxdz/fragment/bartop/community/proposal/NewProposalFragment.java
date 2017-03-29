package cn.net.bjsoft.sxdz.fragment.bartop.community.proposal;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/1/12.
 */
@ContentView(R.layout.fragment_proposal_new)
public class NewProposalFragment extends BaseFragment {
    @ViewInject(R.id.proposal_new_title)
    private TextView title;
    @ViewInject(R.id.proposal_new_back)
    private TextView back;
    @ViewInject(R.id.proposal_new_add)
    private TextView add;
    @ViewInject(R.id.proposal_new_content)
    private EditText content;

    @Override
    public void initData() {

    }

    @Event(type = View.OnClickListener.class,value = {R.id.proposal_new_back,R.id.proposal_new_add})
    private void newProposalOnClick(View view){
        switch (view.getId()){
            case R.id.proposal_new_back:
                mActivity.finish();
                break;
            case R.id.proposal_new_add:
                addProposal();
                mActivity.finish();
                break;
        }
    }

    /**
     * 添加建议的推送数据放在这里
     */
    private void addProposal() {
        MyToast.showShort(mActivity,"添加了一条新建议");
    }
}

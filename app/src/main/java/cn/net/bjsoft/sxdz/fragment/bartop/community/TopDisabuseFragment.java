package cn.net.bjsoft.sxdz.fragment.bartop.community;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.disabuse.NewDisabuseActivity;
import cn.net.bjsoft.sxdz.adapter.DisabuseAdapter;
import cn.net.bjsoft.sxdz.bean.community.DisabuseBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * 解惑页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_disabuse)
public class TopDisabuseFragment extends BaseFragment {
        @ViewInject(R.id.community_disabuse_title)
    private TextView title;
    @ViewInject(R.id.empty_text)
    private TextView test;
    @ViewInject(R.id.community_disabuse_list)
    private ListView proposalList;

    private ArrayList<DisabuseBean> disabuseItemList;
    private DisabuseAdapter adapter;
    private DisabuseBean disabuseBean;

    @Override
    public void initData() {
        title.setText("报错");
        test.setText("");
        MyToast.showShort(mActivity, "欢迎进入报错页面");
        if (disabuseItemList != null) {
            disabuseItemList = null;
        }
        disabuseItemList = new ArrayList<>();
        getData();

    }

    /**
     * 向列表添加数据
     */
    private void getData() {

        for (int i = 0; i < 10; i++) {
            if (disabuseBean != null) {
                disabuseBean = null;
            }
            disabuseBean = new DisabuseBean();

            disabuseBean.avatarUrl = "http://api.shuxin.net/Data/biip/upload/OA_USERS/2/avatar.png";
            disabuseBean.name = "名字" + i;
            disabuseBean.time = "2017.01." + i;
            disabuseBean.dis = "测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，" + i;

            int state = i % 2;
            LogUtil.e("取余=====" + state + "::" + i);
            if (state == 0) {
                disabuseBean.happenTime = "2016.01." + state;
                disabuseBean.reAvatarUrl = "http://api.shuxin.net/Data/biip/upload/OA_USERS/2/avatar.png";
                disabuseBean.reName = "管理员" + state;
                disabuseBean.reDis = "回复专用，回复专用，回复专用，回复专用，回复专用，回复专用，回复专用，回复专用，回复专用，回复专用，" + i;
                disabuseBean.reTime = "2017.02." + state;
                disabuseBean.state = state;

            } else if (state == 1) {
                disabuseBean.state = state;
            }
            state = -1;
            disabuseItemList.add(disabuseBean);
        }


        adapter = new DisabuseAdapter(getActivity(), disabuseItemList, mJson);
        proposalList.setAdapter(adapter);
    }

    @Event(value = {
            R.id.community_disabuse_back,
            R.id.community_disabuse_new})
    private void helpOnclick(View view) {
        switch (view.getId()) {
            case R.id.community_disabuse_back:
                mActivity.finish();
                break;
            case R.id.community_disabuse_new:
                //MyToast.showShort(mActivity,"添加一条新的");
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewDisabuseActivity.class);
                intent.putExtra("json", mJson);
                startActivity(intent);

                break;
        }
    }
}

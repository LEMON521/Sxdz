package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.message.MessageListItemActivity;
import cn.net.bjsoft.sxdz.adapter.MessageAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_message)
public class TopMessageFragment extends BaseFragment {

    @ViewInject(R.id.message_message_title)
    private TextView title;
    @ViewInject(R.id.message_message_lv)
    private ListView messageListView;

    private ArrayList<MessageBean> messageItemList;
    private MessageAdapter messageAdapter;
    private MessageBean messageBean;


    @Override
    public void initData() {
        title.setText("消息");

        if (messageItemList != null) {
            messageItemList = null;
        }
        messageItemList = new ArrayList<>();
        getData();
    }


    /**
     * 向列表添加数据
     */
    private void getData() {

        for (int i = 0; i < 10; i++) {
            if (messageBean != null) {
                messageBean = null;
            }
            messageBean = new MessageBean();
            messageBean.avatarUrl = "http://api.shuxin.net/Data/biip/upload/OA_USERS/2/avatar.png";
            messageBean.title = "标题" + i;
            messageBean.name = "管理员" + i;
            messageBean.time = "04月0" + i + "号" + "  08:3" + i;
            messageBean.dis = "测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，测试专用，" + i;
            messageItemList.add(messageBean);
        }


        messageAdapter = new MessageAdapter(getActivity(), messageItemList);
        messageListView.setAdapter(messageAdapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MessageListItemActivity.class);
                intent.putExtra("json",mJson);
                getContext().startActivity(intent);
            }
        });

    }


    @Event(value = {R.id.message_message_back, R.id.message_message_add})
    private void messageOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_message_back:
                mActivity.finish();
                break;
            case R.id.message_message_add:
                MyToast.showShort(mActivity, "添加新信息");
                break;
        }
    }
}

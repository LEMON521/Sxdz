package cn.net.bjsoft.sxdz.fragment.bartop.message.message;

import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * Created by Zrzc on 2017/1/17.
 */
@ContentView(R.layout.fragment_message_list_item)
public class MessageListItemFragment extends BaseFragment {
    @Override
    public void initData() {

    }

    @Event(value = {R.id.message_list_item_back})
    private void messageListItemOnClick(View view){
        switch (view.getId()){
            case R.id.message_list_item_back:
                mActivity.finish();
                break;
        }
    }
}

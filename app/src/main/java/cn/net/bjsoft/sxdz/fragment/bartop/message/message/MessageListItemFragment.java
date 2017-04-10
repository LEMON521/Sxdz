package cn.net.bjsoft.sxdz.fragment.bartop.message.message;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * Created by Zrzc on 2017/1/17.
 */
@ContentView(R.layout.fragment_message_list_item)
public class MessageListItemFragment extends BaseFragment {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;




    @Override
    public void initData() {
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.INVISIBLE);

        getData();
    }

    private void getData() {

    }



    @Event(value = {R.id.title_back})
    private void messageListItemOnClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                mActivity.finish();
                break;
        }
    }
}

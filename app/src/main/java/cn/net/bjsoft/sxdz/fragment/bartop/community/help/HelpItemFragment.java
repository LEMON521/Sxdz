package cn.net.bjsoft.sxdz.fragment.bartop.community.help;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.NewDisabuseAdapter;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * 显示解惑/帮助单个条目
 * Created by Zrzc on 2017/1/12.
 */
@ContentView(R.layout.activity_new_items_details)
public class HelpItemFragment extends BaseFragment {
    @ViewInject(R.id.fragmen_upload_tv_toolbar)
    private TextView toolbar;//顶部栏名称
    @ViewInject(R.id.news_item_back)
    private ImageView back;//顶部栏名称
    @ViewInject(R.id.news_item_title)
    private TextView title;
    @ViewInject(R.id.news_item_author)
    private TextView author;
    @ViewInject(R.id.news_item_time)
    private TextView time;
    @ViewInject(R.id.news_item_body)
    private TextView body;

    private NewDisabuseAdapter adapter;
    private static ArrayList<ImageView> imageViews;

    @Override
    public void initData() {

        addDisabuse();
    }

    @Event(type = View.OnClickListener.class, value = {R.id.news_item_back})
    private void newDisabuseOnClick(View view) {
        switch (view.getId()) {
            case R.id.news_item_back:
                mActivity.finish();
                break;
        }
    }

    /**
     * 添加解惑的推送数据放在这里
     */
    private void addDisabuse() {
        toolbar.setText("");
        title.setText("初次配置必读");
        time.setText("2017.01.13");
        author.setText("管理员");
        body.setText("测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，测试用，");
    }
}

package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * 下属任务列表
 * Created by Zrzc on 2017/4/7.
 */

@ContentView(R.layout.fragment_task_underling)
public class TopTaskUnderlingFragment extends BaseFragment {

    @ViewInject(R.id.fragment_task_list_underling)
    private ListView list_underling;

    @Override
    public void initData() {

    }
}

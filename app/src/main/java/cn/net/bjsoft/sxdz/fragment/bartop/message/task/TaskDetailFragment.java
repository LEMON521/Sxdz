package cn.net.bjsoft.sxdz.fragment.bartop.message.task;

import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.message.task.TaskDetailAddAdapter;
import cn.net.bjsoft.sxdz.bean.message.MessageTaskDetailAddBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * Created by Zrzc on 2017/4/5.
 */

@ContentView(R.layout.fragment_task_detail)
public class TaskDetailFragment extends BaseFragment {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;

    @ViewInject(R.id.item_task_sxdz_name)
    private TextView sxdz_title;
    @ViewInject(R.id.item_task_sxdz_title)
    private TextView sxdz_name;
    @ViewInject(R.id.item_task_sxdz_start)
    private TextView sxdz_start;
    @ViewInject(R.id.item_task_sxdz_end)
    private TextView sxdz_end;
    @ViewInject(R.id.item_task_sxdz_state)
    private TextView sxdz_state;

    @ViewInject(R.id.fragment_task_detail)
    private TextView detail;
    @ViewInject(R.id.fragment_task_attachment)
    private GridView attachment;
    @ViewInject(R.id.fragment_task_detail_list)
    private ListView detail_list;
    @ViewInject(R.id.fragment_task_add_detail)
    private TextView add_detail;
    @ViewInject(R.id.fragment_task_progress)
    private BubbleSeekBar progress;
    @ViewInject(R.id.fragment_task_files)
    private ListView files;


    //添加详情相关
    private MessageTaskDetailAddBean addBean;
    private ArrayList<MessageTaskDetailAddBean> addBeenList;
    private TaskDetailAddAdapter addAdapter;


    @Override
    public void initData() {
        back.setVisibility(View.VISIBLE);
        title.setText("任务详情");

        if (addBeenList == null) {
            addBeenList = new ArrayList<>();
        }
        addBeenList.clear();

        if (addAdapter == null) {
            addAdapter = new TaskDetailAddAdapter(mActivity, addBeenList);
        }
        detail_list.setAdapter(addAdapter);
        detail_list.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽掉滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });


    }


    @Event(value = {R.id.fragment_task_add_detail})
    private void taskDetailOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://添加详情条目
                mActivity.finish();
                break;
            case R.id.fragment_task_add_detail://添加详情条目
                addBean = null;
                addBean = new MessageTaskDetailAddBean();
                addBeenList.add(addBean);
                addAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(detail_list);
                break;
        }
    }
}

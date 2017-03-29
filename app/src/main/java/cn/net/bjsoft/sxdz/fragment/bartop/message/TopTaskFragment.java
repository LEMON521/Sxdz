package cn.net.bjsoft.sxdz.fragment.bartop.message;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.zdlf.KnowledgeItemsAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowledgeBean;
import cn.net.bjsoft.sxdz.dialog.TaskQueryPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.view.RefreshListView_1;

/**
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_task)
public class TopTaskFragment extends BaseFragment {

    @ViewInject(R.id.message_task_title)
    private TextView title;
    @ViewInject(R.id.message_task_query)
    private ImageView query;

    @ViewInject(R.id.fragment_task_on)
    private TextView task_on;
    @ViewInject(R.id.fragment_task_off)
    private TextView task_off;
    @ViewInject(R.id.fragment_task_allocation)
    private TextView task_allocation;
    @ViewInject(R.id.fragment_task_alltask)
    private TextView task_alltask;

    @ViewInject(R.id.fragment_task_list)
    private RefreshListView_1 task_list;

    private KnowledgeBean.ItemsBean itemsBean;
    private ArrayList<KnowledgeBean.ItemsDataDao> itemsDataList;
    private KnowledgeItemsAdapter itemsAdapter;


    private TaskQueryPopupWindow window;

    @Override
    public void initData() {
        title.setText("任务");

        window = new TaskQueryPopupWindow(mActivity, query);


        window.setOnData(new TaskQueryPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(HashMap<String, String> content) {

                MyToast.showShort(mActivity, "查找任务");
                /**
                 * 在这里处理数据,并将数据发送到服务器上
                 *
                 * 两个时间并非是时间戳,应该传换成时间戳再做进一步处理
                 */


            }
        });

        taskChange(task_on);


        if (itemsDataList == null) {
            itemsDataList = new ArrayList<>();
        } else
            itemsDataList.clear();

        if (itemsAdapter == null) {
            itemsAdapter = new KnowledgeItemsAdapter(mActivity, itemsDataList);
        }

        task_list.setAdapter(itemsAdapter);

        task_list.setOnRefreshListener(new RefreshListView_1.OnRefreshListener() {
            @Override
            public void pullDownRefresh() {
                //SystemClock.sleep(2000);
                LogUtil.e("下拉刷新");
                itemsAdapter.notifyDataSetChanged();
                task_list.onRefreshFinish();
            }

            @Override
            public void pullUpLoadMore() {
                LogUtil.e("上啦加载");
                itemsAdapter.notifyDataSetChanged();
                task_list.onRefreshFinish();
                //SystemClock.sleep(2000);
            }
        });
    }

    @Event(value = {R.id.message_task_back, R.id.message_task_add, R.id.message_task_query})
    private void taskOnClick(View view) {
        switch (view.getId()) {
            case R.id.message_task_back:
                mActivity.finish();
                break;
            case R.id.message_task_add:
                MyToast.showShort(mActivity, "添加新任务");
                break;
            case R.id.message_task_query:
                //MyToast.showShort(mActivity,"添加新任务");
                window.showWindow();
                break;

        }
    }

    @Event(value = {R.id.fragment_task_on
            , R.id.fragment_task_off
            , R.id.fragment_task_allocation
            , R.id.fragment_task_alltask})
    private void taskChange(View view) {
        {//先把全部设置成默认的样式
            task_on.setTextColor(Color.parseColor("#000000"));
            task_on.setBackgroundResource(R.drawable.approve_left_kongxin);
            task_off.setTextColor(Color.parseColor("#000000"));
            task_off.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_allocation.setTextColor(Color.parseColor("#000000"));
            task_allocation.setBackgroundResource(R.drawable.approve_middle_kongxin);
            task_alltask.setTextColor(Color.parseColor("#000000"));
            task_alltask.setBackgroundResource(R.drawable.approve_right_kongxin);
        }

        switch (view.getId()) {
            case R.id.fragment_task_on:
                task_on.setTextColor(Color.parseColor("#FFFFFF"));
                task_on.setBackgroundResource(R.drawable.approve_left_shixin);
                getData();
                break;
            case R.id.fragment_task_off:
                task_off.setTextColor(Color.parseColor("#FFFFFF"));
                task_off.setBackgroundResource(R.drawable.approve_middle_shixin);
                getData();
                break;
            case R.id.fragment_task_allocation:
                task_allocation.setTextColor(Color.parseColor("#FFFFFF"));
                task_allocation.setBackgroundResource(R.drawable.approve_middle_shixin);
                getData();
                break;
            case R.id.fragment_task_alltask:
                task_alltask.setTextColor(Color.parseColor("#FFFFFF"));
                task_alltask.setBackgroundResource(R.drawable.approve_right_shixin);
                getData();
                break;

        }

        /**
         * 这里添加切换任务
         */
    }

    private void getData(){
        RequestParams params = new RequestParams("http://www.shuxin.net/api/app_json/android/knowledge/knowledge_items_life.json");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //LogUtil.e("获取到的条目-----------" + result);
                itemsBean = GsonUtil.getKnowledgeItemsBean(result);
                if (itemsBean.result) {
                    //LogUtil.e("获取到的条目-----------" + result);
                    itemsDataList.clear();
                    itemsDataList.addAll(itemsBean.items);
                    itemsAdapter.notifyDataSetChanged();
                    itemsBean = null;
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("获取到的条目--------失败!!!---" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}

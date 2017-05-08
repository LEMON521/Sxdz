package cn.net.bjsoft.sxdz.fragment.bartop.message.approve.add_approve;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewTripAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveTripDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 *  新建出差审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_trip)
public class TopApproveNewTripFragment extends BaseFragment {

    @ViewInject(R.id.approve_new_trip_entry)
    private ListView entry;
    @ViewInject(R.id.approve_new_trip_total)
    private TextView total;
//
//    private Dialog dialog;
//    private ArrayList<String> result;
//    private ArrayList<String> list;

    //添加费用明细相关
    private ApproveTripDao tripDao;
    private ArrayList<ApproveTripDao> dataList;
    private ApproveNewTripAdapter resListAdapter;


    @Override
    public void initData() {


        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        resListAdapter = new ApproveNewTripAdapter(mActivity, dataList,total);
        entry.setAdapter(resListAdapter);

        tripDao = new ApproveTripDao();
        dataList.add(tripDao);
        resListAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(entry);
        entry.setOnTouchListener(new View.OnTouchListener() {
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




    @Event(value = {R.id.approve_new_trip_add})
    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.approve_new_trip_add://添加明细
                ApproveTripDao dao = new ApproveTripDao();
                dataList.add(dao);
                resListAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(entry);
                break;
        }
    }

    public ArrayList<ApproveTripDao> getEntryData() {

        return dataList;
    }

//    /**
//     * 添加明细的 输入对话框
//     *
//     * @param context 上下文
//     * @param strs    指定的显示信息.具体参数的信息详情参照WindowEntryView的参数要求设置
//     */
//    public void showDialog(Context context, ArrayList<String> strs) {
//        if (dialog == null) {
//            dialog = new Dialog(context, R.style.MIDialog);
//            final WindowEntryView view = new WindowEntryView(context, strs);
//
//            view.getConfirm().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    result = view.submit();//获取到的数据
//                    if (result.size() < list.size() / 2) {//用于判断信息是否填完整
//                        MyToast.showShort(mActivity, "每项信息都不能为空！");
//                        //result.clear();
//                    } else {
//                        dialog.dismiss();
//                        //提交成功一次，清除一次输入框中的内容
//                        view.clear();
//
//                        //刷新界面操作
//                        tripDao = new ApproveTripDao();
//                        tripDao.situs = result.get(0);
//                        float f1 = Float.parseFloat(result.get(1));
//                        float f2 = Float.parseFloat(result.get(2));
//                        tripDao.startTime_str = f1 + "";
//                        tripDao.endTime_str = f2 + "";
//                        tripDao.totleDate = result.get(3);
//                        dataList.add(tripDao);
//                        resListAdapter.notifyDataSetChanged();
//
//                        //********动态设置高度
//                        Utility.setListViewHeightBasedOnChildren(entry);
//
//                        float sum = 0;
//                        for (int i = 0; i < dataList.size(); i++) {
//                            sum = Integer.parseInt(dataList.get(i).totleDate) + sum;
//                        }
//                        total.setText(sum + "");
//
//                    }
////                    for(int i = 0;i<result.size();i++){
////                        LogUtil.e("获取到的添加信息为"+result.get(i));
////                    }
//                    //LogUtil.e("获取到的添加信息为");
//
//                }
//            });
//
//            view.getCancle().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.setContentView(view);
//            dialog.setCancelable(false);
//        }
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//    }
}

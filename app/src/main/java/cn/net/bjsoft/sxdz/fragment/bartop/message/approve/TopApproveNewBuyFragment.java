package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewBuyAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveBuyDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_buy)
public class TopApproveNewBuyFragment extends BaseFragment {
    @ViewInject(R.id.approve_new_buy_entry)
    private ListView entry;
    //@ViewInject(R.id.approve_new_buy_total)
    private TextView total;


//    private Dialog dialog;
//    private ArrayList<String> result;
//    private ArrayList<String> list;

    //添加费用明细相关
    private ApproveBuyDao buyDao = null;
    private ArrayList<ApproveBuyDao> resList;
    private ApproveNewBuyAdapter resListAdapter;

    //    @ViewInject(R.id.message_approve_new_back)
//    private ImageView back;//返回按钮

    int i = 0;
    @Override
    public void initData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        total = (TextView) getView().findViewById(R.id.approve_new_buy_total);
        resList = new ArrayList<>();
        resListAdapter = new ApproveNewBuyAdapter(mActivity, resList,total);
        entry.setAdapter(resListAdapter);


        //默认添加一条数据
        buyDao = new ApproveBuyDao();
        resList.add(buyDao);
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
//                        buyDao = new ApprovebuyDao();
//                        buyDao.res = result.get(0);
//                        float f1 = Float.parseFloat(result.get(1));
//                        float f2 = Float.parseFloat(result.get(2));
//                        float f3 = f1 * f2;
//                        buyDao.unit_price = f1 + "";
//                        buyDao.quantity = f2 + "";
//                        buyDao.money = f3 + "";
//                        resList.add(buyDao);
//                        resListAdapter.notifyDataSetChanged();
//
//                        //********动态设置高度
//                        Utility.setListViewHeightBasedOnChildren(entry);
//
//                        float sum = 0;
//                        for (int i = 0; i < resList.size(); i++) {
//                            sum = Float.parseFloat(resList.get(i).money) + sum;
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


    @Event(value = {R.id.approve_new_buy_add})
    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.approve_new_buy_add://添加明细

                //创建条目的时候一定要创建完整,如果不不赋("") ,那么在创建类的时候一定将字符串赋("")
                ApproveBuyDao dao = new ApproveBuyDao();
//                buyDao.res = "";
//                buyDao.unit_price = "";
//                buyDao.quantity = "";

                resList.add(dao);
                resListAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(entry);
                dao = null;
                LogUtil.e("add还剩========="+resList.size()+"=======条信息!");
                //showDialog(mActivity, list);
                break;
        }
    }

    public ArrayList<ApproveBuyDao> getEntryData() {

        return resList;
    }

}

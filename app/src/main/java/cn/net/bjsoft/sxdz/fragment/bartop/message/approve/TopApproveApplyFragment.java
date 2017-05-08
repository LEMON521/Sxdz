package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowWaiteItemAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.push_json_bean.PostJsonBean;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopApproveFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 我发起的审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_wait)
public class TopApproveApplyFragment extends BaseFragment {

    @ViewInject(R.id.approve_approval_personnel)
    private ListView personnel;//人事审批
    @ViewInject(R.id.approve_approval_administration)
    private ListView administration;//行政审批
    @ViewInject(R.id.approve_approval_financial)
    private ListView financial;//财务审批
    @ViewInject(R.id.approve_approval_official)
    private ListView official;//办公审批

    private TopApproveFragment fragment = null;

    private ArrayList<ApproveDatasDao.ApproveItems> waiteList;

    private PostJsonBean pushDoingBean = new PostJsonBean();
    private String get_start = "0";
    private String get_count = "0";


    private ArrayList<ApproveDatasDao.ApproveItems> personnelList;////人事审批
    private ApproveShowWaiteItemAdapter personneAdapter;


    private ArrayList<ApproveDatasDao.ApproveItems> administrationList;//行政审批
    private ApproveShowWaiteItemAdapter administrationAdapter;


    private ArrayList<ApproveDatasDao.ApproveItems> financialList;//财务审批
    private ApproveShowWaiteItemAdapter financialAdapter;


    private ArrayList<ApproveDatasDao.ApproveItems> officialList;//办公审批
    private ApproveShowWaiteItemAdapter officialAdapter;

    @Override
    public void initData() {

        initList();

        getData();

        //getListData();
        //setListAdapter();
    }

    private void initList() {
        pushDoingBean = new PostJsonBean();

    }

    private void getData() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", "workflow_apply");

        pushDoingBean.start = get_start;//设置开始查询
        pushDoingBean.limit = "10";
        params.addBodyParameter("data", pushDoingBean.toString());
        LogUtil.e("-------------------------bean.toString()" + pushDoingBean.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取我发起的审批消息----------------" + strJson);
//                taskBean = GsonUtil.getMessageTaskBean(strJson);
//                if (taskBean.code.equals("0")) {
//                    tasksDoingDao.addAll(taskBean.data.items);
//                    get_start = tasksDoingDao.size() + "";//设置开始查询
//                    get_count = taskBean.data.count + "";
//
//                    formateDatas();//格式化信息
//
//                    taskAdapter.notifyDataSetChanged();
//                    if (get_count.equals("0")) {
//                        MyToast.showLong(mActivity, "没有任何消息可查看!");
//                    }
//                } else {
//                    MyToast.showLong(mActivity, "获取消息失败-"/*+taskBean.msg*/);
//                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取消息-----------失败方法-----" + element.getMethodName());
                }

                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
    }


    /**
     * 格式化从后台拿过来的数据
     */
//    private void formateDatas() {
//        for (MessageTaskBean.TasksAllDao dao : tasksDoingDao) {
//            if (SPUtil.getUserId(mActivity).equals(dao.userid)) {
//                dao.is_executant = 0;
//
//                String time = dao.starttime;
//                time = time.replace("/Date(", "");
//                time = time.replace(")/", "");
//                dao.starttime = time;
//                time = dao.limittime;
//                time = time.replace("/Date(", "");
//                time = time.replace(")/", "");
//                dao.limittime = time;
//
//            }
//        }
//    }

    /**
     * 获取各个列表的内容
     */
    private void getListData() {
        if (personnelList == null) {
            personnelList = new ArrayList<>();
        }
        if (administrationList == null) {
            administrationList = new ArrayList<>();
        }
        if (financialList == null) {
            financialList = new ArrayList<>();
        }
        if (officialList == null) {
            officialList = new ArrayList<>();
        }


        for (int i = 0; i < waiteList.size(); i++) {
            if (waiteList.get(i).type.equals("leave")) {
                personnelList.add(waiteList.get(i));
            } else if (waiteList.get(i).type.equals("trip")) {
                personnelList.add(waiteList.get(i));
            } else if (waiteList.get(i).type.equals("out")) {
                personnelList.add(waiteList.get(i));
            } else if (waiteList.get(i).type.equals("res")) {
                administrationList.add(waiteList.get(i));
            } else if (waiteList.get(i).type.equals("expenses")) {
                financialList.add(waiteList.get(i));
            } else if (waiteList.get(i).type.equals("buy")) {
                financialList.add(waiteList.get(i));
            } else if (waiteList.get(i).type.equals("agreement")) {
                officialList.add(waiteList.get(i));
            }
        }
    }

    /**
     * 为list设置适配器
     */
    private void setListAdapter() {
        if (personneAdapter == null) {
            personneAdapter = new ApproveShowWaiteItemAdapter(mActivity, personnelList);
        }
        personnel.setAdapter(personneAdapter);
        Utility.setListViewHeightBasedOnChildren(personnel);
        personnel.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽滑动事件
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
        personnel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", personnelList.get(position).url);
                intent.putExtra("type", personnelList.get(position).type);
                startActivity(intent);

            }
        });

        if (administrationAdapter == null) {
            administrationAdapter = new ApproveShowWaiteItemAdapter(mActivity, administrationList);
        }
        administration.setAdapter(administrationAdapter);
        Utility.setListViewHeightBasedOnChildren(administration);
        administration.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽滑动事件
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
        administration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", administrationList.get(position).url);
                intent.putExtra("type", administrationList.get(position).type);
                startActivity(intent);

            }
        });

        if (financialAdapter == null) {
            financialAdapter = new ApproveShowWaiteItemAdapter(mActivity, financialList);
        }
        financial.setAdapter(financialAdapter);
        Utility.setListViewHeightBasedOnChildren(financial);
        financial.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽滑动事件
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
        financial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", financialList.get(position).url);
                intent.putExtra("type", financialList.get(position).type);
                startActivity(intent);

            }
        });

        if (officialAdapter == null) {
            officialAdapter = new ApproveShowWaiteItemAdapter(mActivity, officialList);
        }
        official.setAdapter(officialAdapter);
        Utility.setListViewHeightBasedOnChildren(official);
        official.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽滑动事件
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
        official.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", officialList.get(position).url);
                intent.putExtra("type", officialList.get(position).type);
                startActivity(intent);

            }
        });
    }


}

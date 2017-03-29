package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveShowWaiteItemAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveDatasDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopApproveFragment;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 待我审批
 * Created by Zrzc on 2017/3/9.
 */
@ContentView(R.layout.fragment_approve_wait)
public class TopApproveWaitFragment extends BaseFragment {

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
        //mActivity.getFragmentManager().findFragmentByTag("approve_ylyd");
        fragment = (TopApproveFragment) mActivity.getSupportFragmentManager().findFragmentByTag("approve_ylyd");


        if (fragment==null) {
            fragment = (TopApproveFragment) mActivity.getSupportFragmentManager().findFragmentByTag("approve");
        }


        if (fragment!=null) {
            waiteList = fragment.getWaiteList();
        }

        getListData();
        setListAdapter();
    }

    /**
     * 获取各个列表的内容
     */
    private void getListData(){
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
    private void setListAdapter(){
        if (personneAdapter==null) {
            personneAdapter = new ApproveShowWaiteItemAdapter(mActivity,personnelList);
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
                intent.putExtra("url",personnelList.get(position).url);
                intent.putExtra("type",personnelList.get(position).type);
                startActivity(intent);

            }
        });

        if (administrationAdapter==null) {
            administrationAdapter = new ApproveShowWaiteItemAdapter(mActivity,administrationList);
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
                intent.putExtra("url",administrationList.get(position).url);
                intent.putExtra("type",administrationList.get(position).type);
                startActivity(intent);

            }
        });

        if (financialAdapter==null) {
            financialAdapter = new ApproveShowWaiteItemAdapter(mActivity,financialList);
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
                intent.putExtra("url",financialList.get(position).url);
                intent.putExtra("type",financialList.get(position).type);
                startActivity(intent);

            }
        });

        if (officialAdapter==null) {
            officialAdapter = new ApproveShowWaiteItemAdapter(mActivity,officialList);
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
                intent.putExtra("url",officialList.get(position).url);
                intent.putExtra("type",officialList.get(position).type);
                startActivity(intent);

            }
        });
    }



}

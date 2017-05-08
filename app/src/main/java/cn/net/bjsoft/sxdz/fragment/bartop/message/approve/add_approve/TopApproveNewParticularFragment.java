package cn.net.bjsoft.sxdz.fragment.bartop.message.approve.add_approve;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveItemApproverAdapter;
import cn.net.bjsoft.sxdz.adapter.approve.ApproveNewFilesAdapter;
import cn.net.bjsoft.sxdz.bean.approve.ApproveAgreementDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveBuyDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveContactDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveExpensesDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveLeaveDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveNewFilesDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveOutDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveResDao;
import cn.net.bjsoft.sxdz.bean.approve.ApproveTripDao;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_particular)
public class TopApproveNewParticularFragment extends BaseFragment {

    @ViewInject(R.id.message_approve_new_back)
    private ImageView back;//返回按钮

    @ViewInject(R.id.message_approve_title)
    private TextView title;//返回按钮

//    @ViewInject(R.id.approve_new_expenses)
//    private RadioButton expenses;//报销
//    @ViewInject(R.id.approve_new_trip)
//    private RadioButton trip;//出差
//    @ViewInject(R.id.approve_new_leave)
//    private RadioButton leave;//请假
//    @ViewInject(R.id.approve_new_out)
//    private RadioButton out;//外出
//    @ViewInject(R.id.approve_new_buy)
//    private RadioButton buy;//采购
//    @ViewInject(R.id.approve_new_agreement)
//    private RadioButton agreement;//合同
//    @ViewInject(R.id.approve_new_res)
//    private RadioButton res;//物品领用

    //附件
    @ViewInject(R.id.approve_new_expenses_accessory)
    private GridView accessory;

    //审批人
    @ViewInject(R.id.approve_new_expenses_humen)
    private GridView humen;


    //为每个Fragment添加TAG
    private String TAG = "";

    private int TAG_INt = 1;

    private BaseFragment fragment;


    //上传附件相关
    private ApproveNewFilesDao newFilesDao = null;
    private ArrayList<ApproveNewFilesDao> filesList;
    private ApproveNewFilesAdapter filesListAdapter;

    private ApproveContactDao approverDao = null;
    private ArrayList<ApproveContactDao> approverList;
    private ApproveItemApproverAdapter approverListAdapter;


    //    private String submit_json = "";
    private StringBuilder submit_json;

    @Override
    public void initData() {

        TAG_INt = getArguments().getInt("particular");
        title.setText(getArguments().getString("title"));
        Bundle bundle = new Bundle();
        bundle.putString("json", mJson);
        switch (TAG_INt) {
            case 1/*ConstantApprove.NEW_EXPENSES*/://报销
                fragment = new TopApproveNewExpensesFragment();
                TAG = "APPROVE_EXPENSES";
                bundle.putString("title", "报销");
                break;
            case 2/*ConstantApprove.NEW_TRIP*/://出差
                fragment = new TopApproveNewTripFragment();
                bundle.putString("title", "出差");
                TAG = "APPROVE_TRIP";
                break;
            case 3/*ConstantApprove.NEW_LEAVE*/://请假
                fragment = new TopApproveNewLeaveFragment();
                bundle.putString("title", "请假");
                TAG = "APPROVE_LEAVE";
                break;
            case 4/*ConstantApprove.NEW_OUT*/://外出
                fragment = new TopApproveNewOutFragment();
                bundle.putString("title", "外出");
                TAG = "APPROVE_OUT";
                break;
            case 5/*ConstantApprove.NEW_BUY*/://采购
                fragment = new TopApproveNewBuyFragment();
                bundle.putString("title", "采购");
                TAG = "APPROVE_BUY";
                break;
            case 6/*ConstantApprove.NEW_AGREEMENT*/://合同
                fragment = new TopApproveNewAgreementFragment();
                bundle.putString("title", "合同审批");
                TAG = "APPROVE_AGREEMENT";
                break;
            case 7/*ConstantApprove.NEW_RES*/://物品领用
                fragment = new TopApproveNewResFragment();
                bundle.putString("title", "物品领用");
                TAG = "APPROVE_RES";
                break;
        }
        fragment.setArguments(bundle);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.approve_new_contant, fragment, TAG)
                .commit();


        //-------------------添加附件初始化--开始-------------------
        newFilesDao = new ApproveNewFilesDao();
        newFilesDao.name = "点击添加附件";
        newFilesDao.tag = -2;


        if (filesList == null) {
            filesList = new ArrayList<>();
        }

        filesList.add(newFilesDao);
        filesListAdapter = new ApproveNewFilesAdapter(mActivity, filesList);

        accessory.setAdapter(filesListAdapter);
        accessory.setOnTouchListener(new View.OnTouchListener() {
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

        accessory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override//参数parent--GridView，view--LinearLayout
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.e("点击参数===" + "--position" + position + "--id" + id);

                if (position == (filesList.size() - 1)) {
                    //添加附件动作
                    PhotoOrVideoUtils.doFiles(mActivity, TopApproveNewParticularFragment.this);//打开文件管理器意图
                } else {
                    MyToast.showShort(mActivity, "打开文件！");
                }
            }
        });

        //-------------------添加附件初始化--结束-------------------

        //-------------------添加审批人初始化--开始-------------------
        approverDao = new ApproveContactDao();
        approverDao.tag = -1;
        approverDao.name = "点击添加审批人";

        if (approverList == null) {
            approverList = new ArrayList<>();
        }
        approverList.add(approverDao);

        if (approverListAdapter == null) {
            approverListAdapter = new ApproveItemApproverAdapter(mActivity, approverList);
        }
        humen.setAdapter(approverListAdapter);
        humen.setOnTouchListener(new View.OnTouchListener() {
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
        humen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (approverList.size() - 1)) {
                    //在这里应该打开应用联系人界面并选择联系人,根据联系人的信息
                    approverDao = new ApproveContactDao();
                    approverDao.tag = 1;
                    approverDao.name = "舒心";


                    approverList.add(approverList.size() - 1, approverDao);
                    approverListAdapter.notifyDataSetChanged();
                    Utility.setGridViewHeightBasedOnChildren(humen, 4);
                } else {
                    approverList.remove(position);
                    approverListAdapter.notifyDataSetChanged();
                    Utility.setGridViewHeightBasedOnChildren(humen, 4);
                }
            }
        });

        //-------------------添加审批人初始化--结束-------------------
        //设置默认页面
//        onChangeClick(expenses);
    }


//    /**
//     * 切换审批事项
//     *
//     * @param view
//     */
//    @Event(value = {R.id.approve_new_expenses, R.id.approve_new_trip,
//            R.id.approve_new_leave, R.id.approve_new_out,
//            R.id.approve_new_buy, R.id.approve_new_agreement,
//            R.id.approve_new_res})
//    private void onChangeClick(View view) {
//        showProgressDialog();
//        BaseFragment fragment = null;
//        Bundle bundle = new Bundle();
//        bundle.putString("json", mJson);
//        switch (view.getId()) {
//            case R.id.approve_new_expenses://报销
//                fragment = new TopApproveNewExpensesFragment();
//                TAG = "APPROVE_EXPENSES";
//                break;
//            case R.id.approve_new_trip://出差
//                fragment = new TopApproveNewTripFragment();
//                TAG = "APPROVE_TRIP";
//                break;
//            case R.id.approve_new_leave://请假
//                fragment = new TopApproveNewLeaveFragment();
//                TAG = "APPROVE_LEAVE";
//                break;
//            case R.id.approve_new_out://外出
//                fragment = new TopApproveNewOutFragment();
//                TAG = "APPROVE_OUT";
//                break;
//            case R.id.approve_new_buy://采购
//                fragment = new TopApproveNewBuyFragment();
//                TAG = "APPROVE_BUY";
//                break;
//            case R.id.approve_new_agreement://合同
//                fragment = new TopApproveNewAgreementFragment();
//                TAG = "APPROVE_AGREEMENT";
//                break;
//            case R.id.approve_new_res://物品领用
//                fragment = new TopApproveNewResFragment();
//                TAG = "APPROVE_RES";
//                break;
//        }
//        fragment.setArguments(bundle);
//        mActivity.getSupportFragmentManager().beginTransaction()
//                .replace(R.id.approve_new_contant, fragment, TAG)
//                .commit();
//
//        //切换页面时清空附件列表
//        if (filesList.size() > 1) {
//            for (int i = filesList.size(); i > 1; i--) {//保留第0个文件--也就是增加文件的按钮
//                //filesList.clear();//如果单纯的清除,那么添加的按钮也将被清除
//                filesList.remove(0);
//                filesListAdapter.notifyDataSetChanged();
//            }
//            Utility.setGridViewHeightBasedOnChildren(accessory, 4);
//        }
//
//        dismissProgressDialog();
//    }

    /**
     * 返回和提交
     *
     * @param view
     */
    @Event(value = {R.id.message_approve_new_back, R.id.approve_new_submit})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_approve_new_back://返回
                mActivity.finish();
                break;
            case R.id.approve_new_submit://提交
                submit_json = new StringBuilder();
                //报销审批数据
                if (TAG.equals("APPROVE_EXPENSES")) {
                    TopApproveNewExpensesFragment fragment = (TopApproveNewExpensesFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ArrayList<ApproveExpensesDao> list = fragment.getEntryData();
                    if (list != null && list.size() > 0) {
                        //LogUtil.e("返回键---" + list.get(0).res);
                        boolean isHasData = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).res.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).unit_price.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品单价信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).quantity.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品数量信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).money.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品金额信息");
                                isHasData = false;
                                return;
                            }
                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
                            submit_json.append("{");


                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }


                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }
                }

                //出差审批页面
                if (TAG.equals("APPROVE_TRIP")) {
                    TopApproveNewTripFragment fragment = (TopApproveNewTripFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ArrayList<ApproveTripDao> list = fragment.getEntryData();
                    if (list != null && list.size() > 0) {
                        boolean isHasData = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).situs.equals("")) {
                                MyToast.showShort(mActivity, "请输入出差地点");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).startTime_str.equals("")) {
                                MyToast.showShort(mActivity, "请输入出差开始时间");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).endTime_str.equals("")) {
                                MyToast.showShort(mActivity, "请输入出差结束时间");
                                isHasData = false;
                                return;
                            }

                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
//
                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }

                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }

                }

                //请假审批页面
                if (TAG.equals("APPROVE_LEAVE")) {
                    TopApproveNewLeaveFragment fragment = (TopApproveNewLeaveFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ArrayList<ApproveLeaveDao> list = fragment.getEntryData();
                    if (list != null && list.size() > 0) {
                        boolean isHasData = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).matter.equals("")) {
                                MyToast.showShort(mActivity, "请输入请假事由");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).startTime_str.equals("")) {
                                MyToast.showShort(mActivity, "请输入出差开始时间");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).endTime_str.equals("")) {
                                MyToast.showShort(mActivity, "请输入出差结束时间");
                                isHasData = false;
                                return;
                            }

                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
//
                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }

                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }

                }


                //外出审批页面
                if (TAG.equals("APPROVE_OUT")) {
                    TopApproveNewOutFragment fragment = (TopApproveNewOutFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ArrayList<ApproveOutDao> list = fragment.getEntryData();
                    if (list != null && list.size() > 0) {
                        boolean isHasData = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).matter.equals("")) {
                                MyToast.showShort(mActivity, "请输入外出事由");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).startTime_str.equals("")) {
                                MyToast.showShort(mActivity, "请输入外出开始时间");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).endTime_str.equals("")) {
                                MyToast.showShort(mActivity, "请输入外出结束时间");
                                isHasData = false;
                                return;
                            }

                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
//
                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }

                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }

                }

                //采购审批数据
                if (TAG.equals("APPROVE_BUY")) {
                    TopApproveNewBuyFragment fragment = (TopApproveNewBuyFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ArrayList<ApproveBuyDao> list = fragment.getEntryData();
                    if (list != null && list.size() > 0) {
                        //LogUtil.e("返回键---" + list.get(0).res);
                        boolean isHasData = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).res.equals("")) {
                                MyToast.showShort(mActivity, "请输入采购物品");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).unit_price.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品单价信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).quantity.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品数量信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).money.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品金额信息");
                                isHasData = false;
                                return;
                            }
                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
//                            for (int i = 0;i<list.size();i++){
//                                LogUtil.e(list.get(i).res);
//                                LogUtil.e(list.get(i).unit_price);
//                                LogUtil.e(list.get(i).quantity);
//                                LogUtil.e(list.get(i).money);
//                            }
                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }


                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }
                }

                //合同审批页面
                if (TAG.equals("APPROVE_AGREEMENT")) {
                    TopApproveNewAgreementFragment fragment = (TopApproveNewAgreementFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ApproveAgreementDao dao = fragment.getEntryData();
                    if (dao != null) {
                        boolean isHasData = true;
                        if (dao.num.equals("")) {
                            MyToast.showShort(mActivity, "请输入合同编号信息");
                            isHasData = false;
                            return;
                        }
                        if (dao.date.equals("")) {
                            MyToast.showShort(mActivity, "请输入合同日期信息");
                            isHasData = false;
                            return;
                        }
                        if (dao.first.equals("")) {
                            MyToast.showShort(mActivity, "请输入甲方名称信息");
                            isHasData = false;
                            return;
                        }
                        if (dao.first_leading.equals("")) {
                            MyToast.showShort(mActivity, "请输入甲方负责人信息");
                            isHasData = false;
                            return;
                        }
                        if (dao.second.equals("")) {
                            MyToast.showShort(mActivity, "请输入乙方名称信息");
                            isHasData = false;
                            return;
                        }
                        if (dao.second_leading.equals("")) {
                            MyToast.showShort(mActivity, "请输入乙方负责人信息");
                            isHasData = false;
                            return;
                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
//
                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }

                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }

                }

                //物品申领审批数据
                if (TAG.equals("APPROVE_RES")) {
                    TopApproveNewResFragment fragment = (TopApproveNewResFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
                    ArrayList<ApproveResDao> list = fragment.getEntryData();
                    if (list != null && list.size() > 0) {
                        //LogUtil.e("返回键---" + list.get(0).res);
                        boolean isHasData = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).res.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).use.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品用途信息");
                                isHasData = false;
                                return;
                            }
                            if (list.get(i).num.equals("")) {
                                MyToast.showShort(mActivity, "请输入物品数量信息");
                                isHasData = false;
                                return;
                            }

                        }
                        if (isHasData) {
                            /**
                             * 应该将,将要上传的数据参数在这里添加
                             *
                             */
//                            for (int i = 0;i<list.size();i++){
//                                LogUtil.e(list.get(i).res);
//                                LogUtil.e(list.get(i).unit_price);
//                                LogUtil.e(list.get(i).quantity);
//                                LogUtil.e(list.get(i).money);
//                            }
                            MyToast.showShort(mActivity, "数据填写完整!传送至服务器!");
                        } else {
                            MyToast.showShort(mActivity, "请将数据填写完整!");
                        }


                    } else {
                        MyToast.showShort(mActivity, "请添加数据后再提交!");
                    }
                }


                /**
                 * TODO 在这里获取附件列表信息
                 */

                /**
                 * TODO 在这里获取审批人列表信息
                 */

                setSubmit_json();
                break;
        }
    }

    /**
     * 将数据推送到服务器上
     */
    public void setSubmit_json(){

    }

    /**
     * 选择附件的返回获取
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        String path = "";
        if (uri != null) {
            path = PhotoOrVideoUtils.getPath(mActivity, uri);
            ApproveNewFilesDao newFilesDaoNew = new ApproveNewFilesDao();
            newFilesDaoNew.uri = uri.toString();
            LogUtil.e("获取到的uri" + uri.toString());
            newFilesDaoNew.url = path;
            LogUtil.e("获取到的文件路径" + path);
            newFilesDaoNew.name = path.substring(path.lastIndexOf("/") + 1);//不包含 (/)线
            LogUtil.e("获取到的文件名" + newFilesDaoNew.name);
            newFilesDaoNew.tag = PhotoOrVideoUtils.getLastnameType(path);
            LogUtil.e("获取到的tag" + newFilesDaoNew.tag);
            if (newFilesDaoNew.tag >= 0 && newFilesDaoNew.tag <= 3) {//0,1,2,3
                filesList.add(filesList.size() - 1, newFilesDaoNew);//将新文件加到倒数第二个上(倒数第一个是添加的)
                filesListAdapter.notifyDataSetChanged();
                //动态设置GridView的高度
                Utility.setGridViewHeightBasedOnChildren(accessory, 4);
            } else {
                MyToast.showShort(mActivity, "请上传正确格式的文件！");
            }
            newFilesDaoNew = null;
        }


    }
//

    private void getChildFragmentData() {
        if (TAG.equals("APPROVE_EXPENSES")) {
            TopApproveNewExpensesFragment fragment = (TopApproveNewExpensesFragment) mActivity.getSupportFragmentManager().findFragmentByTag(TAG);
            fragment.getEntryData();
        }
    }


}

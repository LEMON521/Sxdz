package cn.net.bjsoft.sxdz.fragment.bartop.message.approve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.approve.ApproveAgreementDao;
import cn.net.bjsoft.sxdz.dialog.PickerDialog;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * 审批
 * Created by 靳宁宁 on 2017/1/10.
 */
@ContentView(R.layout.fragment_approve_new_agreement)
public class TopApproveNewAgreementFragment extends BaseFragment {

    @ViewInject(R.id.approve_new_agreement_num)
    private EditText num;//合同编号
    @ViewInject(R.id.approve_new_agreement_date)
    private EditText date;//签约日期
    @ViewInject(R.id.approve_new_agreement_first)
    private EditText first;//甲方名称
    @ViewInject(R.id.approve_new_agreement_first_leading)
    private EditText first_leading;//甲方负责人
    @ViewInject(R.id.approve_new_agreement_second)
    private EditText second;//乙方名称
    @ViewInject(R.id.approve_new_agreement_second_leading)
    private EditText second_leading;//乙方负责人

    private ApproveAgreementDao agreementDao;

    @Override
    public void initData() {
        if (agreementDao == null) {
            agreementDao = new ApproveAgreementDao();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //合同编号
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agreementDao.num = s.toString().trim();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerDialog.showDatePickerDialog(mActivity,date,null);
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agreementDao.date = s.toString().trim();
            }
        });

        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agreementDao.first = s.toString().trim();
            }
        });

        first_leading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agreementDao.first_leading = s.toString().trim();
            }
        });

        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agreementDao.second = s.toString().trim();
            }
        });

        second_leading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agreementDao.second_leading = s.toString().trim();
            }
        });
    }

    @Event(value = {})
    private void onChangeClick(View view) {
        BaseFragment fragment = null;
        switch (view.getId()) {
//            case R.id.approve_new_expenses://报销
//                break;

        }
    }

    public ApproveAgreementDao getEntryData() {

        return agreementDao;
    }

}

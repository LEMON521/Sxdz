package cn.net.bjsoft.sxdz.fragment.function.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.WebActivity;
import cn.net.bjsoft.sxdz.activity.home.function.form.FunctionFormActivity;
import cn.net.bjsoft.sxdz.adapter.function.form.FunctionFormAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormBean;
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormDataItemsBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by Zrzc on 2017/5/15.
 */
@ContentView(R.layout.fragment_function_form)
public class FunctionFormFragment extends Fragment {
    private FragmentActivity mActivity;
    private View view;
    private AppProgressDialog progressDialog;


    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;

    @ViewInject(R.id.fragment_function_form_listview)
    private ListView listView;
    @ViewInject(R.id.fragment_function_form_info)
    private TextView form_info;


    private FunctionFormBean functionFormBean;
    private ArrayList<FunctionFormDataItemsBean> dataBeanList;
    private FunctionFormAdapter functionFormAdapter;


    private Bundle bundle;
    private String titleStr = "";
    private String parent_id = "";
    private String stat_source_id = "";
    private String stat_target = "";


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = x.view().inject(this, inflater, null);
        mActivity = getActivity();

        bundle = getArguments();
        LogUtil.e("========bundle========" + bundle.getString("title"));

        if (bundle != null) {
            parent_id = bundle.getString("parent_id");
            titleStr = bundle.getString("title");
            stat_source_id = bundle.getString("stat_source_id");
            stat_target = bundle.getString("stat_target");
        }

        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        } else {
            title.setText("报表");
        }
        back.setVisibility(View.VISIBLE);
        initData();
        getData();
        return view;
    }

    @Event(value = {R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mActivity.finish();
                break;

        }
    }

    private void initData() {

        if (dataBeanList == null) {
            dataBeanList = new ArrayList<>();
        }
        dataBeanList.clear();

        if (functionFormAdapter == null) {
            functionFormAdapter = new FunctionFormAdapter(mActivity, dataBeanList);
        }

        listView.setAdapter(functionFormAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (TextUtils.isEmpty(dataBeanList.get(position).url)) {
                        Intent childIntent = new Intent(mActivity, FunctionFormActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("stat_source_id", stat_source_id);
                        bundle.putString("stat_target", stat_target);
                        bundle.putString("parent_id", dataBeanList.get(position).id);
                        bundle.putString("title", dataBeanList.get(position).name);
                        childIntent.putExtra("form_data", bundle);
                        startActivity(childIntent);
                    } else {
                        if (!TextUtils.isEmpty(dataBeanList.get(position).url)) {
                            Intent webIntent = new Intent(mActivity, WebActivity.class);
                            webIntent.putExtra("url", dataBeanList.get(position).url);
                            webIntent.putExtra("title", dataBeanList.get(position).name);
                            startActivity(webIntent);
                        } else {
                            MyToast.showShort(getActivity(), "此报表暂时没有信息");
                        }

                    }
            }
        });

    }


    private void getData() {


        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", stat_source_id);
        params.addBodyParameter("target", stat_target);
//        {data:{parent_id:stat_parent_id}}
        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");


        sb.append("\"data\":{");

        sb.append("\"parent_id\":\"");
        sb.append(parent_id);
        sb.append("\"");

        sb.append("}");
        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        LogUtil.e("-------------------------data.toString()--" + sb.toString());
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取报表----报表信息----------------" + strJson);

                functionFormBean = GsonUtil.getFunctionFormBean(strJson);

                if (functionFormBean.code.equals("0")) {
                    if (functionFormBean.data.items != null) {

                        dataBeanList.addAll(functionFormBean.data.items);
                        functionFormAdapter.notifyDataSetChanged();
                        if (dataBeanList.size()>0){
                            form_info.setVisibility(View.GONE);
                        }else {
                            form_info.setVisibility(View.VISIBLE);
                        }
                    }else {
                        form_info.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                form_info.setVisibility(View.VISIBLE);

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

    public synchronized void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        progressDialog.show(getActivity());
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismissDialog();
        }
    }

    public synchronized AppProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        return progressDialog;
    }
}

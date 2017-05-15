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
import cn.net.bjsoft.sxdz.bean.app.function.form.FunctionFormDatasBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

import static cn.net.bjsoft.sxdz.utils.function.TestAddressUtils.test_get_function_form_url;

/**
 * Created by Zrzc on 2017/5/15.
 */
@ContentView(R.layout.fragment_function_form)
public class FunctionFormFragment extends Fragment {
    private FragmentActivity mActivity;
    private View view;


    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;

    @ViewInject(R.id.fragment_function_form_listview)
    private ListView listView;

    private FunctionFormBean formBean;
    private ArrayList<FunctionFormDatasBean> dataBeanList;
    private FunctionFormAdapter adapter;


    private Bundle bundle;
    private String titleStr = "";
    private String formId = "";


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
        LogUtil.e("========bundle========"+bundle.getString("title"));

        if (bundle != null) {
            formId = bundle.getString("form_id");
            titleStr = bundle.getString("title");
        }

        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        } else {
            title.setText("报表");
        }
        back.setVisibility(View.VISIBLE);
        initData();
        //getData();
        getDataTest();
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

        if (adapter == null) {
            adapter = new FunctionFormAdapter(mActivity, dataBeanList);
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(dataBeanList.get(position).url)) {//不为空,那么就是子节点,并打开网页
                    Intent webIntent = new Intent(mActivity, WebActivity.class);
                    webIntent.putExtra("url", dataBeanList.get(position).url);
                    webIntent.putExtra("title", dataBeanList.get(position).title);
                    startActivity(webIntent);
                } else {//不为空,那么他就有子节点
                    Intent childIntent = new Intent(mActivity, FunctionFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("form_id", dataBeanList.get(position).id);
                    bundle.putString("title", dataBeanList.get(position).title);
                    childIntent.putExtra("form_data", bundle);
                    startActivity(childIntent);
                }
            }
        });

    }



    private void getData() {
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("form_id", formId);
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {


                formBean = GsonUtil.getFunctionFormBean(strJson);
                dataBeanList.addAll(formBean.data);
                adapter.notifyDataSetChanged();

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
            }
        });
    }

    private void getDataTest() {

        String url = test_get_function_form_url;
        RequestParams params = new RequestParams(url);
        //params.addBodyParameter("form_id", formId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                formBean = GsonUtil.getFunctionFormBean(result);
                dataBeanList.addAll(formBean.data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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

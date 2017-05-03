package cn.net.bjsoft.sxdz.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import cn.net.bjsoft.sxdz.app.MyApplication;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * Created by Zrzc on 2017/1/5.
 */

public abstract class BaseFragment extends Fragment {
    public FragmentActivity mActivity;
    public String mJson = "";
    public View view;
    public DatasBean mDatasBean;
    public MyApplication app;
    public int mOsVersion;

    private AppProgressDialog progressDialog;

    /*
 * onAttach(Context) is not called on pre API 23 versions of Android and onAttach(Activity) is deprecated
 * Use onAttachToContext instead
 */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivity = getActivity();
        app = (MyApplication) mActivity.getApplication();
        mOsVersion = app.mOsVersion;
        if (getArguments().get("json") != null) {
            mJson = getArguments().get("json").toString();
        }
        mDatasBean = GsonUtil.getDatasBean(mJson);
        view = x.view().inject(this, inflater, null);
        //MyToast.showLong(getActivity(),view.toString());
        initData();
        LogUtil.e("BaseFragment::onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e("BaseFragment::onActivityCreated");
        //设置打开界面时,不弹出软键盘
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 用于初始化控件的方法
     */
    public abstract void initData();


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



    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("BaseFragment==onDestroy");

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e("BaseFragment==onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtil.e("BaseFragment==onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e("BaseFragment==onStart");
    }
    @Override
    public void onStop() {
        super.onStop();
        LogUtil.e("BaseFragment==onStop");
    }



}

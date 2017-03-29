package cn.net.bjsoft.sxdz.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import cn.net.bjsoft.sxdz.app.MyApplication;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;

/**
 * Created by jnn on 2017/1/3.
 */

public class BaseActivity extends FragmentActivity  /*implements Animation.AnimationListener*/{
//
//    //顶部栏每个按钮的推送数量
//    public int mCommunityNum = 0;
//    public int mFunctionNum = 0;
//    public int mMessageNum = 0;
//    public int mUserNum = 0;
//    public ArrayList<Integer> mPushNum;

    private AppProgressDialog progressDialog;
    public Context context;
    public int mOsVersion = 0;
    public MyApplication app;

    //public int mCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // mCount++;
       // LogUtil.e("BaseActivity::count-----"+mCount);

        context = this;
        mOsVersion = getAndroidOSVersion();
        app = (MyApplication) getApplication();
        LogUtil.e("SDK版本号为======"+mOsVersion);
        x.view().inject(this);
    }

//    private ArrayList<Integer> getmPushNum(){
//
//    }

    public synchronized void showProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new AppProgressDialog();

        }
        progressDialog.show(this);
    }

    public void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismissDialog();
        }
    }

    public synchronized AppProgressDialog getProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new AppProgressDialog();
        }
        return progressDialog;
    }

    //获取SDK版本号
    public static int getAndroidOSVersion()
    {
        int osVersion;
        try
        {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        }
        catch (NumberFormatException e)
        {
            osVersion = 0;
        }

        return osVersion;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //动画效果
//        @Override
//        public void onAnimationStart(Animation animation) {
//
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//
//        }



    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("BaseActivity=="+"onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("BaseActivity==onDestroy");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("BaseActivity==onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        LogUtil.e("BaseActivity==onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("BaseActivity==onStart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("BaseActivity==onStop");
    }

}

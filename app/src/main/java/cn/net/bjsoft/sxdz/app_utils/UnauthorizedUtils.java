package cn.net.bjsoft.sxdz.app_utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import cn.net.bjsoft.sxdz.activity.welcome.SplashActivity;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by Zrzc on 2017/5/22.
 */

public class UnauthorizedUtils {
    //private static FragmentActivity mActivity;

    public static void callBackUnauthorized(final FragmentActivity activity) {
        // mActivity = activity;
        new AlertDialog.Builder(activity).setTitle("友情提示").setMessage("该用户已被注销,点击确定重新登录")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
//                        loginAgain();//从服务器注销
                        SPUtil.setUserUUID(activity, "");
                        SPUtil.setUserId(activity, "");
                        SPUtil.setToken(activity, "");
                        SPUtil.setAvatar(activity, "");


                        Intent i = new Intent(activity, SplashActivity.class);
                        activity.startActivity(i);
                        activity.finish();

                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                        activity.finish();
                    }
                }).show();

    }

//    private static void loginAgain(){
//
//
//
//    }
}

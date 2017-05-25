package cn.net.bjsoft.sxdz.app_utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.net.bjsoft.sxdz.activity.welcome.SplashActivity;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

import static cn.net.bjsoft.sxdz.utils.UrlUtil.users_all;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class HttpPostUtils {

    private OnSetData mOnCallBack;
    private Context mActivity;

    private Callback.CommonCallback callBack = new Callback.CommonCallback<String>() {
        @Override
        public void onSuccess(String result) {

            //result = "{\"code\":1,\"data\":null,\"msg\":\"unauthorized\"}";
            try {
                JSONObject jsonObject = new JSONObject(result);
                int code = jsonObject.optInt("code");
                if (code == 1) {//请求失败
                    if (jsonObject.optString("msg").equals("unauthorized")) {
                        callBackUnauthorized(mActivity);
                    }

                } else if (code == 0) {
                    mOnCallBack.onSuccess(result);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            mOnCallBack.onError(ex, isOnCallback);
        }

        @Override
        public void onCancelled(CancelledException cex) {
            mOnCallBack.onCancelled(cex);
        }

        @Override
        public void onFinished() {
            mOnCallBack.onFinished();
        }
    };

    // 数据接口抽象方法
    public interface OnSetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onSuccess(String strJson);

        abstract void onError(Throwable ex, boolean isOnCallback);

        abstract void onCancelled(Callback.CancelledException cex);

        abstract void onFinished();

    }

    // 数据接口设置,数据源接口传入
    public void OnCallBack(OnSetData back) {
        mOnCallBack = back;
    }


    public void post(Context activity, RequestParams params) {
        mActivity = activity;
        params.addBodyParameter("token", SPUtil.getToken(activity));
        params.addBodyParameter("appid", SPUtil.getAppid(activity));
        params.addBodyParameter("secret", SPUtil.getSecret(activity));
        LogUtil.e("================params============" + params.toString());
        x.http().post(params, callBack);

    }

    public void get(Context activity, RequestParams params) {
        mActivity = activity;
        params.addBodyParameter("token", SPUtil.getToken(activity));
        params.addBodyParameter("appid", SPUtil.getAppid(activity));
        params.addBodyParameter("secret", SPUtil.getSecret(activity));
        LogUtil.e("================params============" + params.toString());
        x.http().get(params, callBack);

    }


    private AlertDialog dialog;

    private void callBackUnauthorized(final Context activity) {
        // mActivity = activity;
        if (dialog == null) {//防止创建多个dialog
            dialog = new AlertDialog.Builder(activity).setTitle("友情提示").setMessage("该用户已被注销,点击确定重新登录")
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
                            //activity.finish();

                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                            //activity.finish();
                        }
                    }).show();
        }
    }

    /**
     * 获取全部联系人信息
     * @param context
     */
    public static void getUserInfo(final Context context){


        LogUtil.e("联系人信息---------字符串开始-------");
        RequestParams params = new RequestParams(users_all);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                result = "{\"users_all\":" + result + "}";
                LogUtil.e("联系人信息---------字符串-------"+result);
                SPUtil.setUsersAll(context, result);
                LogUtil.e("联系人信息----------------"+SPUtil.getUsersAll(context));

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(context, "获取联系人信息失败!");
                LogUtil.e("联系人信息---------字符串失败-------"+ex);
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

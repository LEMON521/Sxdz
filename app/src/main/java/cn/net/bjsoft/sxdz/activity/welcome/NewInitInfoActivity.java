package cn.net.bjsoft.sxdz.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

import static cn.net.bjsoft.sxdz.utils.AddressUtils.http_shuxinyun_url;
import static cn.net.bjsoft.sxdz.utils.UrlUtil.api_base;
import static cn.net.bjsoft.sxdz.utils.UrlUtil.users_all;

/**
 * Created by 靳宁宁 on 2017/1/3.
 */
@ContentView(R.layout.activity_welcome)
public class NewInitInfoActivity extends BaseActivity {
    @ViewInject(R.id.splash_progress)
    private TextView progressText;
    @ViewInject(R.id.splash_version)
    private TextView versionText;

    private FragmentActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        //x.view().inject(this);
        versionText.setText("正在初始化用户数据,请稍后");
        LogUtil.e("=============================NewInitInfoActivity===============================");
        checkAppinfo();


    }

    /**
     * 从服务器检查app---根据token值
     */
    private void checkAppinfo() {
        showProgressDialog();

        String url = SPUtil.getApiAuth(mActivity) + "/check";
        LogUtil.e("url$$$$$$$$$$$$$$getItemsData$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("token", SPUtil.getToken(mActivity));


        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("-----------------checkApp----信息----------------" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.optInt("code");
                    if (code == 1) {//请求失败
                        if (jsonObject.optString("msg").equals("unauthorized")) {
                            MyToast.showShort(mActivity, "用户登录时效已过期,请重新登录!");
                            Intent intent = new Intent(mActivity, LoginActivity.class);
                            //将返回的json传递过去，在下一个页面将必要的参数本地化
                            intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                            // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                            startActivity(intent);
                            finish();

                        }

                    } else if (code == 0) {
                        SPUtil.setUserId(mActivity, jsonObject.optString("data"));
                        getMyJson();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取条目消息-----------失败方法-----" + element.getMethodName());
                }
                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });

    }

    /**
     * 获取用户信息
     */
    private void getMyJson() {
        showProgressDialog();

        String url = SPUtil.getApiUser(mActivity) + "/" + SPUtil.getUserId(mActivity) + "/my.json";
        LogUtil.e("url$$$$$$$$$$$$$$getItemsData$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);


        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SPUtil.setUserJson(mActivity,result);
                LogUtil.e("-----------------getMyJson----信息----------------" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String appid = jsonObject.optString("appid");
                    LogUtil.e("----------appid-----------"+(!TextUtils.isEmpty(appid) && appid.equals(SPUtil.getAppid(mActivity)))+"::sppid=="+appid);
                    LogUtil.e("-----------------appid----信息----------------" + appid+"::SPUtil.getAppid=="+SPUtil.getAppid(mActivity));
                    if (!TextUtils.isEmpty(appid) && !appid.equals(SPUtil.getAppid(mActivity))) {
                        SPUtil.setAppid(mActivity, appid);
                        getMobileJson();

                    } else {
                        getOrganizationData();
                        getUsersInfo();
//                        Intent intent = new Intent(mActivity, MainActivity.class);
//                        //将返回的json传递过去，在下一个页面将必要的参数本地化
//                        intent.putExtra("json", SPUtil.getMobileJson(mActivity));
//                        //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
//                        startActivity(intent);
//                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getLocalizedMessage());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getMessage());
                LogUtil.e("-----------------获取条目消息----------失败------" + ex.getCause());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex.getStackTrace());
                LogUtil.e("-----------------获取条目消息-----------失败-----" + ex);
                ex.printStackTrace();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    LogUtil.e("-----------------获取条目消息-----------失败方法-----" + element.getMethodName());
                }
                MyToast.showShort(mActivity, "获取数据失败!!");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                dismissProgressDialog();
            }
        });


    }

    /**
     * 获取工资组织架构信息
     */
    private void getOrganizationData() {
        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = http_shuxinyun_url + "cache/users/" + SPUtil.getUserId(mActivity) + "/organization.json";
        LogUtil.e("公司架构userOrganizationBean url----===========" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                SPUtil.setUserOrganizationJson(mActivity, strJson);//缓存公司架构信息
//                //LogUtil.e("我的页面json");
////                LogUtil.e("公司架构==========="+SPUtil.getUserOrganizationJson(mActivity));
//                Intent i3 = new Intent(mActivity, MainActivity.class);
//                LogUtil.e("数据请求=json=" + json);
//                i3.putExtra("json", json);
//                startActivity(i3);
//                LogUtil.e("mActivity=&&&&&&&&&&=" + mActivity);
//                LogUtil.e("getActivity=&&&&&&&&&&=" + mActivity);
//                LogUtil.e("getContext=&&&&&&&&&&=" + getContext());
//
//                if (mActivity instanceof MainActivity) {
//                    mActivity.finish();
//                }
//                if (mActivity instanceof UserActivity) {
//                    MainActivity.getMainActivity().finish();//不这样做的话，登录之后，点击返回按钮会返回到未登录状态的主页
//                    mActivity.finish();
//                }
//                if (mActivity instanceof LoginActivity) {
//                    LoginActivity.getLoginActivity().finish();//不这样做的话，登录之后，点击返回按钮会返回到未登录状态的主页
//                    mActivity.finish();
//                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SPUtil.setUserOrganizationJson(mActivity, "");
                LogUtil.e("我的页面json-----错误" + ex);
                MyToast.showShort(mActivity, "程序初始化出错,正在重新启动程序");
                SPUtil.setToken(mActivity, "");
                Intent intent = new Intent(mActivity, NewSplashActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();


            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    //private void getMobileJson()




    private AppBean appBean;

    /**
     * 获取程序初始化信息
     */

    private void getMobileJson() {

        LogUtil.e("------------------getMobileJson---------------------");
        HttpPostUtils postUtils = new HttpPostUtils();
        String url = api_base + "/apps/" + SPUtil.getAppid(mActivity) + "/mobile.json";
        String url_test = "http://192.168.1.119:8080/android/form" + "/mobile.json";
        RequestParams params = new RequestParams(url);
        postUtils.get(this, params);
        postUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                appBean = GsonUtil.getAppBean(strJson);

                SPUtil.setMobileJson(mActivity, strJson);

                SPUtil.setAppid(mActivity, appBean.appid);
                SPUtil.setSecret(mActivity, appBean.secret);
                SPUtil.setApiUpload(mActivity, appBean.api_upload);
                SPUtil.setApiAuth(mActivity, appBean.api_auth);
                SPUtil.setResetPasswordUrl(mActivity, appBean.login.passreset);
                SPUtil.setLogoutApi(mActivity, appBean.login.logoutapi);
                SPUtil.setApiUser(mActivity, appBean.api_user);
                SPUtil.setUser_ApiData(mActivity, appBean.api_data);


                //----------------------按用户id绑定推送-------------------------
                String user_id = SPUtil.getUserId(mActivity);
                if (!TextUtils.isEmpty(user_id)) {
                    CloudPushService pushService = PushServiceFactory.getCloudPushService();
                    pushService.bindAccount(user_id, new CommonCallback() {
                        @Override
                        public void onSuccess(String s) {
                            LogUtil.e("==============初始化绑定成功!!!===============" + s);
                        }

                        @Override
                        public void onFailed(String s, String s1) {
                            LogUtil.e("==============初始化绑定失败!!!===============" + s + "---原因---" + s1);
                        }
                    });
                }
                LogUtil.e("----######------getMobileJson-----====------" + strJson);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(mActivity, "请打开网络后重新启动程序！");
                SystemClock.sleep(2000);
                finish();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                getOrganizationData();
                getUsersInfo();
            }
        });


    }


    private void getUsersInfo() {
        LogUtil.e("联系人信息---------字符串开始-------");
        RequestParams params = new RequestParams(users_all);
        LogUtil.e("联系人信息url-----"+params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                result = "{\"users_all\":" + result + "}";
                LogUtil.e("联系人信息---------字符串-------" + result);
                SPUtil.setUsersAll(mActivity, result);
                LogUtil.e("联系人信息----------------" + SPUtil.getUsersAll(mActivity));

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(mActivity, "获取联系人信息失败!");
                LogUtil.e("联系人信息---------字符串失败-------" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                jump();

            }
        });

    }

    /**
     * 页面的跳转
     */
    private void jump() {
        appBean = GsonUtil.getAppBean(SPUtil.getMobileJson(this));
        if (appBean.loaders.size() == 0) {
            if (appBean.authentication) {//需要验证
                if (SPUtil.getToken(this).equals("")) {//未登录状态

                    Intent intent = new Intent(this, LoginActivity.class);
                    //将返回的json传递过去，在下一个页面将必要的参数本地化
                    intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                    // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());

                    startActivity(intent);
                    finish();
                } else {//登录状态
                    Intent intent = new Intent(this, MainActivity.class);
                    //将返回的json传递过去，在下一个页面将必要的参数本地化
                    intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                    // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());

                    startActivity(intent);
                    finish();
                    //getUserData();
                }

            } else {//不需要验证
                Intent intent = new Intent(this, MainActivity.class);
                //将返回的json传递过去，在下一个页面将必要的参数本地化
                intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                finish();
                startActivity(intent);
            }
        }
        if (appBean.loaders.size() == 1) {
            Intent intentJumpOver = new Intent(this, JumpOverActivity.class);
            //Intent intentJumpOver = new Intent(SplashActivity.this, MainActivity.class);
            intentJumpOver.putExtra("json", SPUtil.getMobileJson(mActivity));
            finish();
            startActivity(intentJumpOver);

        } else if (appBean.loaders.size() > 1) {//跳到轮播图
            Intent intent = new Intent(this, CarouselFigureActivity.class);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            intent.putExtra("json", SPUtil.getMobileJson(mActivity));
            intent.setAction("SplashActivity");
            //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
            finish();
            startActivity(intent);

        }


    }

}

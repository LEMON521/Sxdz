package cn.net.bjsoft.sxdz.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.ReadFile;

import static cn.net.bjsoft.sxdz.utils.UrlUtil.api_base;
import static cn.net.bjsoft.sxdz.utils.UrlUtil.init_url;

/**
 * Created by 靳宁宁 on 2017/1/3.
 */
@ContentView(R.layout.activity_touming)
public class NewSplashActivity extends BaseActivity {
//    @ViewInject(R.id.splash_progress)
//    private TextView progressText;
//    @ViewInject(R.id.splash_version)
//    private TextView versionText;

    private FragmentActivity mActivity;

    private AppBean appBean;//最新数据结构


    protected static final String TAG = "SplashActivity";// 标识,就是说打印的时候是隶属于SplashActivity类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动服务
       // x.view().inject(this);
        mActivity = this;
//        versionText.setText(getVersionName());
        //startService(new Intent(this, JPushService.class));
        getData();
        //get();

        //MyToast.showShort(this,"加载....");
    }



    private void getData() {

        //清空推送数据

        SPJpushUtil.setApprove(this, 0);
        SPJpushUtil.setBug(this, 0);
        SPJpushUtil.setCommunity(this, 0);
        SPJpushUtil.setCrm(this, 0);
        SPJpushUtil.setKnowledge(this, 0);
        SPJpushUtil.setMessage(this, 0);
        SPJpushUtil.setMyself(this, 0);
        SPJpushUtil.setPayment(this, 0);
        SPJpushUtil.setProposal(this, 0);
        SPJpushUtil.setScan(this, 0);
        SPJpushUtil.setShoot(this, 0);
        SPJpushUtil.setSignin(this, 0);
        SPJpushUtil.setTask(this, 0);
        SPJpushUtil.setTrain(this, 0);


        /////////////////////////工作模块/////////////////////////
        SPJpushUtil.setProject(this, 0);
        SPJpushUtil.setEngineering(this, 0);
        SPJpushUtil.setContract(this, 0);
        SPJpushUtil.setMarketchannel(this, 0);
        SPJpushUtil.setSalereport(this, 0);
        SPJpushUtil.setProjectstat(this, 0);
        SPJpushUtil.setEngineeringlog(this, 0);
        SPJpushUtil.setEmergency(this, 0);
        SPJpushUtil.setEngineeringeval(this, 0);
        SPJpushUtil.setConstruction(this, 0);
        SPJpushUtil.setConstructionteam(this, 0);
        SPJpushUtil.setWeekplan(this, 0);
        SPJpushUtil.setCompanyrun(this, 0);
        SPJpushUtil.setSitemsg(this, 0);


        /////////////////////////底部栏/////////////////////////
        SPJpushUtil.setHome_zdlf(this, 0);
        SPJpushUtil.setWork_items(this, 0);
        SPJpushUtil.setKnowledge_zdlf(this, 0);
        SPJpushUtil.setCommunication_zdlf(this, 0);
        SPJpushUtil.setMine_zdlf(this, 0);


        LogUtil.e("初始化数据了=================" + SPJpushUtil.getSignin(this));

        getDataFromService();
        //getDataFromFile();
        //getUsersInfo();


    }
    /**
     * 开发的时候用网络地址,发布版本后用本地文件
     */
    private void getDataFromService() {

        HttpPostUtils postUtils = new HttpPostUtils();
        String url = api_base + "/apps/" + SPUtil.getAppid(this) + "/mobile.json";
        String initUrl = init_url;
        RequestParams params = new RequestParams(initUrl);
        postUtils.get(this, params);
        postUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                appBean = GsonUtil.getAppBean(strJson);
                SPUtil.setAppid(mActivity, appBean.appid);
                SPUtil.setSecret(mActivity, appBean.secret);
                SPUtil.setApiUpload(mActivity, appBean.api_upload);
                SPUtil.setApiAuth(mActivity, appBean.api_auth);
                SPUtil.setResetPasswordUrl(mActivity, appBean.login.passreset);
                SPUtil.setLogoutApi(mActivity, appBean.login.logoutapi);
                SPUtil.setApiUser(mActivity, appBean.api_user);
                SPUtil.setUser_ApiData(mActivity, appBean.api_data);


                LogUtil.e("----######------getMobileJson-----====------" + strJson);
                SPUtil.setMobileJson(mActivity, strJson);

                if (appBean.authentication) {
                    initAppInfo();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(context, "请打开网络后重新启动程序！");
                SystemClock.sleep(2000);
                finish();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void getDataFromFile() {
        String result = "";
        result = ReadFile.getFromAssets(this, "json/mobile.json");
        appBean = GsonUtil.getAppBean(result);
        SPUtil.setAppid(this, appBean.appid);
        SPUtil.setSecret(this, appBean.secret);
        SPUtil.setApiUpload(this, appBean.api_upload);
        SPUtil.setApiAuth(this, appBean.api_auth);
        SPUtil.setResetPasswordUrl(this, appBean.login.passreset);
        SPUtil.setLogoutApi(this, appBean.login.logoutapi);
        SPUtil.setApiUser(this, appBean.api_user);
//        String api_data = appBean.api_auth;
//        api_data = api_data.replace("data", "");
//        SPUtil.setUser_ApiData(SplashActivity.this, api_data);
        SPUtil.setUser_ApiData(this, appBean.api_data);
//        SPUtil.setHomepageBean(SplashActivity.this, appBean.homepage.toString());
//
//        LogUtil.e("-----@@@@@@-----getHomepageBean------%%%%%%%%%----"+SPUtil.getHomepageBean(mActivity));
        //----------------------按用户id绑定推送-------------------------
        String user_id = SPUtil.getUserId(this);
//        if (!TextUtils.isEmpty(user_id)) {
//            CloudPushService pushService = PushServiceFactory.getCloudPushService();
//            pushService.bindAccount(user_id, new CommonCallback() {
//                @Override
//                public void onSuccess(String s) {
//                    LogUtil.e("==============初始化绑定成功!!!===============" + s);
//                }
//
//                @Override
//                public void onFailed(String s, String s1) {
//                    LogUtil.e("==============初始化绑定失败!!!===============" + s + "---原因---" + s1);
//                }
//            });
//        }
        LogUtil.e("----######------getMobileJson-----====------" + result);
        SPUtil.setMobileJson(mActivity, result);

        if (appBean.authentication) {
            initAppInfo();
        }

        //jump();
    }

    /**
     * 初始化程序信息
     */
    private void initAppInfo() {

        if (TextUtils.isEmpty(SPUtil.getToken(this))) {
            Intent intent = new Intent(this, LoginActivity.class);
            LogUtil.e("----######------initAppInfo-----====------" + "getToken--------空"+intent);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            intent.putExtra("json", SPUtil.getMobileJson(mActivity));
            // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());

            startActivity(intent);
            finish();
        } else {

            Intent intent = new Intent(this, NewInitInfoActivity.class);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
            LogUtil.e("----######------initAppInfo-----====------" + "getToken--------不是----空"+intent);

            startActivity(intent);
            finish();

        }

    }


    /**
     * 页面的跳转
     */
    private void jump() {

        if (appBean.loaders.size() == 0) {
            if (appBean.authentication) {//需要验证
                if (SPUtil.getToken(this).equals("")) {//未登录状态

                    Intent intent = new Intent(NewSplashActivity.this, LoginActivity.class);
                    //将返回的json传递过去，在下一个页面将必要的参数本地化
                    intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                    // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                    finish();
                    startActivity(intent);
                } else {//登录状态
                    //checkApp();
                    //getUserData();
                    Intent intent = new Intent(NewSplashActivity.this, NewSplashActivity.class);
                    //将返回的json传递过去，在下一个页面将必要的参数本地化
                    intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                    // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                    finish();
                    startActivity(intent);
                }

            } else {//不需要验证
                Intent intent = new Intent(NewSplashActivity.this, MainActivity.class);
                //将返回的json传递过去，在下一个页面将必要的参数本地化
                intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                finish();
                startActivity(intent);
            }
        }
        if (appBean.loaders.size() == 1) {
            Intent intentJumpOver = new Intent(NewSplashActivity.this, JumpOverActivity.class);
            //Intent intentJumpOver = new Intent(SplashActivity.this, MainActivity.class);
            intentJumpOver.putExtra("json", SPUtil.getMobileJson(mActivity));
            finish();
            startActivity(intentJumpOver);

        } else if (appBean.loaders.size() > 1) {//跳到轮播图
            Intent intent = new Intent(NewSplashActivity.this, CarouselFigureActivity.class);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            intent.putExtra("json", SPUtil.getMobileJson(mActivity));
            intent.setAction("SplashActivity");
            //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
            finish();
            startActivity(intent);
        }
    }

}

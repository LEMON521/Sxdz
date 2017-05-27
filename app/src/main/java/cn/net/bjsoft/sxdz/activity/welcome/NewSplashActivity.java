package cn.net.bjsoft.sxdz.activity.welcome;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.service.JPushService;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.ReadFile;

/**
 * Created by 靳宁宁 on 2017/1/3.
 */
@ContentView(R.layout.activity_welcome)
public class NewSplashActivity extends BaseActivity {
    @ViewInject(R.id.splash_progress)
    private TextView progressText;
    @ViewInject(R.id.splash_version)
    private TextView versionText;

    private FragmentActivity mActivity;

    private AppBean appBean;//最新数据结构


    protected static final String TAG = "SplashActivity";// 标识,就是说打印的时候是隶属于SplashActivity类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动服务
        mActivity = this;
        versionText.setText(getVersionName());
        startService(new Intent(this, JPushService.class));
        getData();
        //get();

        //MyToast.showShort(this,"加载....");
    }


    /**
     * 获取该app的版本号
     *
     * @return 获取该app的版本号
     */
    private String getVersionName() {

        // 获取到包管理器
        PackageManager pm = getPackageManager();
        try {
            // 获取在系统上安装的应用程序包的整体信息。

            PackageInfo info = pm.getPackageInfo("cn.net.bjsoft.sxdz", 0);
            //PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // 获取版本号
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

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
        SPJpushUtil.setProject(context, 0);
        SPJpushUtil.setEngineering(context, 0);
        SPJpushUtil.setContract(context, 0);
        SPJpushUtil.setMarketchannel(context, 0);
        SPJpushUtil.setSalereport(context, 0);
        SPJpushUtil.setProjectstat(context, 0);
        SPJpushUtil.setEngineeringlog(context, 0);
        SPJpushUtil.setEmergency(context, 0);
        SPJpushUtil.setEngineeringeval(context, 0);
        SPJpushUtil.setConstruction(context, 0);
        SPJpushUtil.setConstructionteam(context, 0);
        SPJpushUtil.setWeekplan(context, 0);
        SPJpushUtil.setCompanyrun(context, 0);
        SPJpushUtil.setSitemsg(context, 0);


        /////////////////////////底部栏/////////////////////////
        SPJpushUtil.setHome_zdlf(context, 0);
        SPJpushUtil.setWork_items(context, 0);
        SPJpushUtil.setKnowledge_zdlf(context, 0);
        SPJpushUtil.setCommunication_zdlf(context, 0);
        SPJpushUtil.setMine_zdlf(context, 0);


        LogUtil.e("初始化数据了=================" + SPJpushUtil.getSignin(this));

        //getDataFromService();
        getDataFromFile();
        //getUsersInfo();


    }

    private void getDataFromFile() {
        String result = "";
        result = ReadFile.getFromAssets(this, "json/mobile.json");
        appBean = GsonUtil.getAppBean(result);
        SPUtil.setAppid(NewSplashActivity.this, appBean.appid);
        SPUtil.setSecret(NewSplashActivity.this, appBean.secret);
        SPUtil.setApiUpload(NewSplashActivity.this, appBean.api_upload);
        SPUtil.setApiAuth(NewSplashActivity.this, appBean.api_auth);
        SPUtil.setResetPasswordUrl(NewSplashActivity.this, appBean.login.passreset);
        SPUtil.setLogoutApi(NewSplashActivity.this, appBean.login.logoutapi);
        SPUtil.setApiUser(NewSplashActivity.this, appBean.api_user);
//        String api_data = appBean.api_auth;
//        api_data = api_data.replace("data", "");
//        SPUtil.setUser_ApiData(SplashActivity.this, api_data);
        SPUtil.setUser_ApiData(NewSplashActivity.this, appBean.api_data);
//        SPUtil.setHomepageBean(SplashActivity.this, appBean.homepage.toString());
//
//        LogUtil.e("-----@@@@@@-----getHomepageBean------%%%%%%%%%----"+SPUtil.getHomepageBean(mActivity));
        //----------------------按用户id绑定推送-------------------------
        String user_id = SPUtil.getUserId(NewSplashActivity.this);
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
            LogUtil.e("----######------initAppInfo-----====------" + "getToken--------空");
            Intent intent = new Intent(NewSplashActivity.this, LoginActivity.class);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            intent.putExtra("json", SPUtil.getMobileJson(mActivity));
            // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());

            finish();
            startActivity(intent);
        } else {

            Intent intent = new Intent(NewSplashActivity.this, NewInitInfoActivity.class);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
            LogUtil.e("----######------initAppInfo-----====------" + "getToken--------不是----空");
            finish();
            startActivity(intent);

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

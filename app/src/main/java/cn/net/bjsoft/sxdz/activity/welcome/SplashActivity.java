package cn.net.bjsoft.sxdz.activity.welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.bean.UpdateBean;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.service.JPushService;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.ReadFile;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;

import static cn.net.bjsoft.sxdz.utils.AddressUtils.http_shuxinyun_url;
import static cn.net.bjsoft.sxdz.utils.UrlUtil.init_url;
import static cn.net.bjsoft.sxdz.utils.UrlUtil.users_all;

/**
 * Created by 靳宁宁 on 2017/1/3.
 */
@ContentView(R.layout.activity_welcome)
public class SplashActivity extends BaseActivity {
    @ViewInject(R.id.splash_progress)
    private TextView progressText;
    @ViewInject(R.id.splash_version)
    private TextView versionText;

    private FragmentActivity mActivity;

    private AppBean appBean;//最新数据结构

    private UpdateBean updateBean;
    private DatasBean datasBean;
    private String mJson;

    private static String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "shuxin" + File.separator + "app" + File.separator;


    protected static final String TAG = "SplashActivity";// 标识,就是说打印的时候是隶属于SplashActivity类
    protected static final int ENTER_HOME = 1;
    protected static final int SHOW_UPDATE_DIALOG = 2;
    protected static final int DOWNLOAD_ERROR = 3;
    protected static final int NETWORK_ERROR = 4;
    protected static final int CHECKVERSION_ERROR = 5;

    /**
     * 获取到服务器端的软件版本号
     */
    private String version;
    /**
     * 获取到的描述信息
     */
    private String description;
    /**
     * apk的下载地址
     */
    private String apkurl;


    Message message = Message.obtain();
    /**
     * 向主线程发送消息的Handler
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 进入主界面
                case ENTER_HOME:
                    Log.e(TAG, "进入主页面" + msg.what);
                    getData();
                    break;
                // 弹出对话框升级
                case SHOW_UPDATE_DIALOG:
                    //
                    Log.e(TAG, "弹出升级对话框" + msg.what);
                    showUpdateDialog();
                    break;
                // URL异常
                case DOWNLOAD_ERROR:
                    Log.e(TAG, "URL异常" + msg.what);
                    Toast.makeText(SplashActivity.this, "URL异常", Toast.LENGTH_SHORT).show();
                    getData();
                    break;
                // 网络异常
                case CHECKVERSION_ERROR:
                    Log.e(TAG, "版本检查异常" + msg.what);
                    // 在这里getApplicationContext()与SplashActivity.this等同
                    Toast.makeText(getApplicationContext(), "版本检查异常", Toast.LENGTH_SHORT).show();
                    getData();
                    break;

                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动服务
        mActivity = this;
        versionText.setText(getVersionName());
        BASE_PATH = context.getFilesDir() + File.separator;
        startService(new Intent(this, JPushService.class));
        checkVersion();
        //getData();
        //get();

        //MyToast.showShort(this,"加载....");
    }

    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(description);

        // 强制升级----不推荐,或者升级漏洞的时候使用
        // builder.setCancelable(false);//取消按钮不可用
        // 针对弹出对话框的bug---用户点击返回键或者空白处时,对话框会消失,但是会停留在当前页面(缓冲页面)
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                getData();
            }
        });
        // 取消(下次再说)按钮的对应的事件
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 令对话框消失
                dialog.dismiss();
                // 进入主页面
                getData();
            }
        });
        // 立刻升级(确定)按钮对应的事件
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 首先判定是否有SDcard,并且可用
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //一定要指定文件名，不然会将下载的文件保存成为.tmp的文件
                    BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "shuxin" + File.separator + "app" + File.separator + "shuxin.apk";
                } else {
                    BASE_PATH = context.getFilesDir().getAbsolutePath() + File.separator + "shuxin.apk";
                    Toast.makeText(getApplicationContext(), "SD卡不可用,将下载到手机内部", Toast.LENGTH_SHORT).show();
                }
                RequestParams params = new RequestParams(apkurl);
                params.setSaveFilePath(BASE_PATH);
                //设置断点续传
                params.setAutoResume(true);
                x.http().get(params, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onWaiting() {

                    }

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        // 设置下载进度信息
                        if (isDownloading) {
                            //SystemClock.sleep(500);
                            progressText.setVisibility(View.VISIBLE);
                            int progress = (int) (current * 100 / total);
                            progressText.setText("下载进度:" + progress + "%");
                        }
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 下载成功之后就安装akp
                        installAPK(file);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        message.what = DOWNLOAD_ERROR;
                        Toast.makeText(getApplicationContext(), "下载失败了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }

                    /**
                     * 安装apk
                     *
                     * 在这里我们引用系统默认的安装意图
                     */
                    private void installAPK(File file) {

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");
                        // 一定不要忘记开启意图
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//没有这一句的话，安装完成后打开的不是新版本的apk
                        startActivityForResult(intent, 0);// 如果用户取消安装的话,
                        // 会返回结果,回调方法onActivityResult
                        android.os.Process.killProcess(android.os.Process.myPid());//安装完之后会提示”完成” “打开”。
                    }
                });
                // 替换apk
                // enterHome();
            }
        });

        // 注意,这行代码不能忘记,如果不show的话对话框是不会出来的
        builder.show();

    }


    /**
     * 检查是否有新版本,如果有就升级
     */
    private void checkVersion() {
        // 检查是否有新版本,那么就应该去服务器端检查,那么就应该联网
        // 在4.0以后,联网就需要在子线程中
        new Thread() {
            public void run() {
                // 将Handler的消息定义出来
                //Message message = Message.obtain();
                long startTime = System.currentTimeMillis();// 获取到当前时间
                // 请求网络,得到最新版本
                // 在这里,我们将网络地址存放进了values文件夹下的config文件中
                RequestParams params = new RequestParams(TestAddressUtils.test_update_url);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "result=======" + result);// 将结果打印出来
                        //updateBean = GsonUtil.getUpdateBean(StreamTools.readFromFile(result));
                        updateBean = GsonUtil.getUpdateBean(result);
                        version = updateBean.android_version;
                        description = updateBean.description;
                        apkurl = updateBean.android_apkurl;
                        Log.e(TAG, "version=======" + version);// 将结果打印出来
                        Log.e(TAG, "description=======" + description);// 将结果打印出来
                        Log.e(TAG, "apkurl=======" + apkurl);// 将结果打印出来

                        // 判断是否更新
                        if (!(getVersionCode() < Integer.parseInt(version))) {
                            // 没有新版本,直接进入主界面
                            message.what = ENTER_HOME;// 设置消息
                        } else {
                            // 弹出对话框
                            message.what = SHOW_UPDATE_DIALOG;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e(TAG, isOnCallback + "$$$$$$$$$$$$$$isOnCallback=======" + ex);// 将结果打印出来
                        message.what = CHECKVERSION_ERROR;
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.e(TAG, "CancelledException=======" + cex);// 将结果打印出来
                    }

                    @Override
                    public void onFinished() {

                    }

                });
                // 向handler发送消息
                long endTime = System.currentTimeMillis();
                long dTime = endTime - startTime;
                if (dTime < 2000) {
                    SystemClock.sleep(2000 - dTime);
                }
                handler.sendMessage(message);
            }
        }.start();
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

    /**
     * 获取该app的版本号
     *
     * @return 获取该app的版本号
     */
    private int getVersionCode() {

        // 获取到包管理器
        PackageManager pm = getPackageManager();
        try {
            // 获取在系统上安装的应用程序包的整体信息。

            PackageInfo info = pm.getPackageInfo("cn.net.bjsoft.sxdz", 0);
            //PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // 获取版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 1;
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


        getDataFromService();
        //getDataFromFile();
        getUsersInfo();


    }

    private void getUsersInfo() {
        HttpPostUtils postUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(users_all);
        postUtils.get(this, params);
        postUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {

                strJson = "{\"users_all\":" + strJson + "}";

                SPUtil.setUsersAll(SplashActivity.this, strJson);


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(context, "获取联系人信息失败!");
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
        SPUtil.setAppid(SplashActivity.this, appBean.appid);
        SPUtil.setSecret(SplashActivity.this, appBean.secret);
        SPUtil.setApiUpload(SplashActivity.this, appBean.api_upload);
        SPUtil.setApiAuth(SplashActivity.this, appBean.api_auth);
        SPUtil.setResetPasswordUrl(SplashActivity.this, appBean.login.passreset);
        SPUtil.setLogoutApi(SplashActivity.this, appBean.login.logoutapi);

        //----------------------按用户id绑定推送-------------------------
        String user_id = SPUtil.getUserId(this);
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
        SPUtil.setMobileJson(mActivity,result);
        jump();
    }


    /**
     * 开发的时候用网络地址,发布版本后用本地文件
     */
    private void getDataFromService() {

        HttpPostUtils postUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(init_url);
        postUtils.get(this, params);
        postUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                appBean = GsonUtil.getAppBean(strJson);
                SPUtil.setAppid(SplashActivity.this, appBean.appid);
                SPUtil.setSecret(SplashActivity.this, appBean.secret);
                SPUtil.setApiUpload(SplashActivity.this, appBean.api_upload);
                SPUtil.setApiAuth(SplashActivity.this, appBean.api_auth);
                SPUtil.setResetPasswordUrl(SplashActivity.this, appBean.login.passreset);
                SPUtil.setLogoutApi(SplashActivity.this, appBean.login.logoutapi);
                SPUtil.setApiUser(SplashActivity.this, appBean.api_user);
                //----------------------按用户id绑定推送-------------------------
                String user_id = SPUtil.getUserId(SplashActivity.this);
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
                LogUtil.e("----######------getMobileJson-----====------"+strJson);
                SPUtil.setMobileJson(mActivity,strJson);
                jump();

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



    /**
     * 如果Token值存在,那么就初始化用户信息
     */
    private void getUserData() {
        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = http_shuxinyun_url + "cache/users/" + SPUtil.getUserId(mActivity) + "/" + "my.json";
        LogUtil.e("url--------------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                SPUtil.setUserJson(mActivity, strJson);//缓存用户信息
                getOrganizationData();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("我的页面json-----错误" + ex);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 获取工资组织架构信息
     */
    private void getOrganizationData() {
        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = http_shuxinyun_url + "cache/users/"+SPUtil.getUserId(mActivity)+"/organization.json";
        LogUtil.e("公司架构userOrganizationBean url----===========" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                SPUtil.setUserOrganizationJson(mActivity, strJson);//缓存公司架构信息
                //LogUtil.e("我的页面json");
//                LogUtil.e("公司架构==========="+SPUtil.getUserOrganizationJson(mActivity));
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //将返回的json传递过去，在下一个页面将必要的参数本地化
                LogUtil.e("----------getMobileJson-----------"+SPUtil.getMobileJson(mActivity));
                intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                finish();
                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SPUtil.setUserOrganizationJson(mActivity, "");
                LogUtil.e("我的页面json-----错误" + ex);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

//    private void getData() {
//        RequestParams params = new RequestParams(UrlUtil.baseUrl);
//        params.addBodyParameter("action", "init");
//        params.addBodyParameter("app_id", Constants.app_id);
//        params.addBodyParameter("client_name", Constants.app_name);
////        params.addBodyParameter("client_name", "0.0");//测试用
//        params.addBodyParameter("phone_uuid", DeviceIdUtils.getDeviceId(this));
//        params.addBodyParameter("uuid", SPUtil.getUserUUID(this));
//        params.addBodyParameter("ostype", "android");
//        //params.setConnectTimeout(5000);//设置超时时间
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                //LogUtil.e("请求网络onSuccess--" + result);
//                datasBean = GsonUtil.getDatasBean(result);
//                if (datasBean.success) {
//                    SPUtil.setUserRandCode(SplashActivity.this, datasBean.data.user.randcode);
//                    mJson = result;
//                    LogUtil.e("获取到的返回参数为：：：" + mJson);
//                    jump(result);
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                LogUtil.e("请求网络onError--" + ex);
//
//                MyToast.showLong(context, "请打开网络后重新启动程序！");
//                SystemClock.sleep(2000);
//                finish();
//
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                LogUtil.e("请求网络onCancelled==" + cex);
//                try {
//                    Thread.sleep(2000);
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFinished() {
//                LogUtil.e("请求网络onFinished");
//                //jump(mJson);
//            }
//        });
//    }


    /**
     * 页面的跳转
     */
    private void jump() {

        if (appBean.loaders.size() == 0) {
            if (appBean.authentication) {//需要验证
                if (SPUtil.getToken(this).equals("")) {//未登录状态

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    //将返回的json传递过去，在下一个页面将必要的参数本地化
                    intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                    // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                    finish();
                    startActivity(intent);
                } else {//登录状态
                    getUserData();
                }

            } else {//不需要验证
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //将返回的json传递过去，在下一个页面将必要的参数本地化
                intent.putExtra("json", SPUtil.getMobileJson(mActivity));
                //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                finish();
                startActivity(intent);
            }
        }
        if (appBean.loaders.size() == 1) {
            Intent intentJumpOver = new Intent(SplashActivity.this, JumpOverActivity.class);
            //Intent intentJumpOver = new Intent(SplashActivity.this, MainActivity.class);
            intentJumpOver.putExtra("json", SPUtil.getMobileJson(mActivity));
            finish();
            startActivity(intentJumpOver);

        } else if (appBean.loaders.size() > 1) {//跳到轮播图
            Intent intent = new Intent(SplashActivity.this, CarouselFigureActivity.class);
            //将返回的json传递过去，在下一个页面将必要的参数本地化
            intent.putExtra("json", SPUtil.getMobileJson(mActivity));
            intent.setAction("SplashActivity");
            //LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
            finish();
            startActivity(intent);

        }


    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("SplashActivity====" + "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("SplashActivity==onDestroy");
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("SplashActivity==onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        LogUtil.e("SplashActivity==onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("SplashActivity==onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("SplashActivity==onStop");
    }

//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//
//        switch (requestCode){
//            case 0:
//                getData();
//        }
//    }
}

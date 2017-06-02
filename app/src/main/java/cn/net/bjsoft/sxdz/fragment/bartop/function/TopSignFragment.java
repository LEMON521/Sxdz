package cn.net.bjsoft.sxdz.fragment.bartop.function;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.mutils.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.function.SignHistoryActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.HomepageBean;
import cn.net.bjsoft.sxdz.bean.app.ModulesBean;
import cn.net.bjsoft.sxdz.bean.app.function.sign.SignUserLastBean;
import cn.net.bjsoft.sxdz.bean.app.function.sign.SignUserLastDataBean;
import cn.net.bjsoft.sxdz.bean.app.user.UserBean;
import cn.net.bjsoft.sxdz.bean.app.user.UserOrganizationBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.TimeUtils;
import cn.net.bjsoft.sxdz.view.RoundImageView;

/**
 * Created by Zrzc on 2017/1/10.
 */

@ContentView(R.layout.fragment_sign)
public class TopSignFragment extends BaseFragment {
    @ViewInject(R.id.title)
    private TextView title;
    //    @ViewInject(R.id.titlebar)
//    RelativeLayout titlebar;
    @ViewInject(R.id.sign_back)
    private ImageView back;
    @ViewInject(R.id.sign_user_icon)
    private RoundImageView userIcon;
    @ViewInject(R.id.sign_name)
    private TextView name;
    @ViewInject(R.id.sign_bumen)
    private TextView bumen;
    @ViewInject(R.id.sign_time)
    private TextView time;
    @ViewInject(R.id.sign_address)
    private TextView address;
    @ViewInject(R.id.sign_history)
    private LinearLayout sign_history;
    @ViewInject(R.id.history)
    private TextView history;
    @ViewInject(R.id.sign_picture)
    private RoundImageView picture;
    //    @ViewInject(R.id.history_back)
//    LinearLayout historyBack;
    @ViewInject(R.id.sing_past_text)
    private TextView pastText;
    @ViewInject(R.id.sign_pic_icon)
    private ImageView picIcon;
    @ViewInject(R.id.sign_btn)
    private ImageView pastBtn;
    @ViewInject(R.id.sign_last_time)
    private TextView oletime;
    @ViewInject(R.id.sign_last_address)
    private TextView oldaddress;


    private UserBean userBean;
    private UserOrganizationBean organizationBean;
    private String mJson = "";

    private TopSignFragment mFragment;

    //private UsersSingleBean usersSingleBean;
    private AppBean appBean;
    private ArrayList<HomepageBean> homepageBeen;
    private ModulesBean modulesBean;


    private String source_id = "";
    private String submit_id = "";
    private String load_list = "";

    private String sign_image_url = "";

    private ImageOptions imageOptions;
    private ImageOptions pictureOptions;

    private Bundle mBundle;
    private String fragmentTag;


    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    //地理位置信息
    private AMapLocation mAmapLocation;


    @Override
    public void initData() {
        mFragment = this;

        imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.get_back_passwoed) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.drawable.get_back_passwoed).build();

        pictureOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.drawable.get_back_passwoed) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.drawable.get_back_passwoed).build();

        appBean = GsonUtil.getAppBean(SPUtil.getMobileJson(getActivity()));

        homepageBeen = appBean.homepage;
        modulesBean = new ModulesBean();

        mBundle = getArguments();

        fragmentTag = mBundle.getString("tag");


        if (fragmentTag != null) {
            LogUtil.e("==========fragmentTag===========" + fragmentTag);
            if (fragmentTag.equals("signin")) {//底部栏创建
                for (int i = 0; i < homepageBeen.size(); i++) {
                    if (homepageBeen.get(i).tag.equals("signin")) {
                        modulesBean.signin = homepageBeen.get(i).tag_params;
                    }
                }
            } else if (fragmentTag.equals("sign")) {
                modulesBean.signin = appBean.modules.signin;

            }

        }

//        if (modulesBean.signin != null && !(modulesBean.signin.size() > 0)) {
//
//        }
        if (modulesBean.signin != null && modulesBean.signin.size() > 0) {
            source_id = modulesBean.signin.get("source_id");
            submit_id = modulesBean.signin.get("submit_id");
            load_list = modulesBean.signin.get("load_list");
        }

        userBean = GsonUtil.getUserBean(SPUtil.getUserJson(mActivity));

        if (userBean == null) {
            MyToast.showShort(mActivity, "数据初始化尚未完成,请稍后进入!");
            mActivity.finish();
        } else {
            organizationBean = userBean.organization;
        }
        //usersSingleBean = UsersInforUtils.getInstance(getActivity()).getUserInfo(SPUtil.getUserId(getActivity()));

        initDataLast();
        setData();
    }

    @Override
    public void onStart() {
        super.onStart();
        initMapLocation(5);
    }

    @Override
    public void onStop() {
        super.onStop();
        //声明AMapLocationClient类对象
        mLocationClient = null;
        //声明定位回调监听器
        mLocationListener = null;
        //声明AMapLocationClientOption对象
        mLocationOption = null;
    }

    public void setData() {

        time.setText(TimeUtils.getFormateTime(System.currentTimeMillis(), "-", ":"));
        x.image().bind(userIcon, userBean.avatar, imageOptions);
        name.setText(userBean.name);
        bumen.setText(organizationBean.dept_name);
        if (TextUtils.isEmpty(load_list)) {
            sign_history.setVisibility(View.GONE);
        } else {
            sign_history.setVisibility(View.VISIBLE);
        }
    }

    @Event(type = View.OnClickListener.class, value = {R.id.sign_back, R.id.sign_history, R.id.sign_picture, R.id.sign_btn})
    private void onSignClick(View view) {
        switch (view.getId()) {
            case R.id.sign_back:
                //MyToast.showShort(mActivity, "返回");
                mActivity.finish();

                break;
            case R.id.sign_history:
                //MyToast.showShort(mActivity, "查看历史");
                Intent i = new Intent(getActivity(), SignHistoryActivity.class);
//                i.putExtra("url", CacheSerializableUtil.getlist(getActivity()).getToolBar().getSignurl());
                i.putExtra("url", "www.baidu.com");
                i.putExtra("title", "签到历史");
                startActivity(i);
                break;
            case R.id.sign_picture:
                PackageManager pm = mActivity.getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                if (!permission) {
                    MyToast.showLong(mActivity, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                    return;
                }
                // MyToast.showShort(mActivity, "拍照片");
                takePhoto();
                break;
            case R.id.sign_btn:
                if (TextUtils.isEmpty(sign_image_url)) {
                    MyToast.showShort(mActivity, "请先拍照!");
                    return;
                } else {
                    submitInforToService();
                }
                break;
        }
    }


    /**
     * 获取上次签到信息
     */
    private void initDataLast() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/load";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("source_id", source_id);

        LogUtil.e("-------------------------source_id.toString()--" + source_id);
        httpPostUtils.get(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取签到----消息----------------" + strJson);

                SignUserLastBean lastBean = GsonUtil.getSignUserLastBean(strJson);
                if (lastBean.code.equals("0")) {
                    SignUserLastDataBean dataBean = lastBean.data;
                    oletime.setText(dataBean.ctime);
                    oldaddress.setText(dataBean.address);
                } else {
                    MyToast.showLong(mActivity, "获取数据失败");
                }
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
                dismissProgressDialog();
            }
        });


    }

    /**
     * 上传图片到服务器
     *
     * @param imagePath
     */
    private void upLoadAvatar(final String imagePath) {
        LogUtil.e("上传成功头像onSuccess444" + SPUtil.getAvatar(getActivity()));
        showProgressDialog();
        RequestParams params = new RequestParams(SPUtil.getApiUpload(mActivity));
        params.setMultipart(true);
        File file = new File(imagePath);

        params.addBodyParameter("avatar", file);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传成功onSuccess" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("code") == 0) {

                        MyBitmapUtils.getInstance(getActivity()).display(picture, imagePath);
                        //x.image().bind(picture, imagePath, pictureOptions);
                        sign_image_url = jsonObject.optJSONObject("data").optString("src");
                        MyToast.showLong(mActivity, "上传图片成功");
                    } else {
                        MyToast.showLong(mActivity, "上传头像失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("上传失败onError" + ex);
                MyToast.showLong(mActivity, "上传头像失败,请联系管理员");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.e("上传onLoading--" + isDownloading + ":total:==" + total + ":current:==" + current);
            }
        });

    }

    /**
     * 提交到服务器
     */

    private void submitInforToService() {
        showProgressDialog();

        if (mAmapLocation == null) {
            MyToast.showShort(mActivity, "获取位置失败,请打开权限并重启软件");
            return;
        }

        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = SPUtil.getApiAuth(mActivity) + "/submit";
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", submit_id);

        LogUtil.e("-------------------------submit_id--" + submit_id);

        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");


        sb.append("\"userid\":\"");
        sb.append(SPUtil.getUserId(mActivity));
        sb.append("\",");

        sb.append("\"abs_x\":\"");
        sb.append(mAmapLocation.getLongitude());
        sb.append("\",");

        sb.append("\"abs_y\":\"");
        sb.append(mAmapLocation.getLatitude());
        sb.append("\",");

        sb.append("\"abs_z\":\"");
        sb.append("");
        sb.append("\",");

        sb.append("\"address\":\"");
        sb.append(mAmapLocation.getAddress());
        sb.append("\",");

        sb.append("\"ctime\":\"");
        sb.append(TimeUtils.getFormateTime(System.currentTimeMillis(), "-", ":"));
        sb.append("\",");

        sb.append("\"img_url\":\"");
        sb.append(sign_image_url);
        sb.append("\"");

        sb.append("}");


        params.addBodyParameter("data", sb.toString());
        LogUtil.e("-------------------------source_id.toString()--" + sb.toString());

        LogUtil.e("-------------------------params.toString()--" + params.toString());
        httpPostUtils.post(mActivity, params);
        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------获取签到后++++++++++++++++----消息----------------" + strJson);
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {
                        MyToast.showLong(mActivity, "签到成功");
                        initDataLast();
                    } else {
                        MyToast.showLong(mActivity, "签到失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                dismissProgressDialog();
            }
        });

    }


    /**
     * 初始化定位
     */
    private void initMapLocation(int time) {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        mAmapLocation = amapLocation;
                        SPUtil.setLat(mActivity, amapLocation.getLatitude() + "");
                        SPUtil.setLong(mActivity, amapLocation.getLongitude() + "");
                        SPUtil.setAddress(mActivity, amapLocation.getAddress());
                        //T.showShort(MainActivity.this, amapLocation.getAddress());
                        //sendLocation(amapLocation);
                        address.setText(mAmapLocation.getAddress());
//                        LogUtil.e("定位地址为" + amapLocation.getAddress());
//                        LogUtil.e("纬度" + amapLocation.getLatitude());
//                        LogUtil.e("经度" + amapLocation.getLongitude());
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtil.e("AmapErrorlocation Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(mActivity);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(time * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("tag", "onActivityResult");
        LogUtil.e("-----------------onActivityResult----------------" + requestCode);
        if (requestCode == 100) {
            if (resultCode== Activity.RESULT_OK) {
                doPhoto();
//                if (data != null) {
//                    if (data.getData() != null) {
//
//                    }
//                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Uri photoUri;

    private String picPath;

    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            this.startActivityForResult(intent, 100);
        } else {
            Toast.makeText(getActivity(), "内存卡不存在", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * 选择图片后，获取图片的路径
     */
    private void doPhoto() {
        Log.e("tag", "doPhoto");
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            if (Build.VERSION.SDK_INT < 14) {

                cursor.close();
            }
        }
        Log.e("tag", "imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            Log.e("tag", "最终选择的图片=" + picPath);
            //MyBitmapUtils.getInstance(getActivity()).display(picture,picPath);
            Bitmap bm = BitmapFactory.decodeFile(picPath);
            //toRoundCorner(bm, 60);
            //picture.setImageBitmap(toRoundCorner(bm, 60));
            //MyBitmapUtils.getInstance(getActivity()).display(picture, picPath);
            upLoadAvatar(picPath);
        } else {
            Toast.makeText(getActivity(), "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }
}

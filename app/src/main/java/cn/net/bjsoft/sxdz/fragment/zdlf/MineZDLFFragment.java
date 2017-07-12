package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lidroid.mutils.utils.Log;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.EmptyActivity;
import cn.net.bjsoft.sxdz.activity.welcome.NewSplashActivity;
import cn.net.bjsoft.sxdz.adapter.zdlf.MineZDLFFunctionAdapter;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.LoginBean;
import cn.net.bjsoft.sxdz.bean.app.user.UserAddinsBean;
import cn.net.bjsoft.sxdz.bean.app.user.UserBean;
import cn.net.bjsoft.sxdz.bean.app.user.UserOrganizationBean;
import cn.net.bjsoft.sxdz.dialog.PickerScrollViewPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.BroadcastCallUtil;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.CircleImageView;
import cn.net.bjsoft.sxdz.view.WindowRecettingPasswordView;
import cn.net.bjsoft.sxdz.view.picker_scroll_view.Pickers;


/**
 * 中电联发的我的页面   - -   我的页面
 * Created by 靳宁宁 on 2017/3/8.
 */
@ContentView(R.layout.fragment_mine_zdlf)
public class MineZDLFFragment extends BaseFragment {
    @ViewInject(R.id.title_back)
    private ImageView back;//返回
    @ViewInject(R.id.title_title)
    private TextView title;//标题

    @ViewInject(R.id.mine_zdlf_name)
    private TextView name;
    @ViewInject(R.id.mine_zdlf_icon)
    private CircleImageView avatar;
    @ViewInject(R.id.mine_zdlf_company)
    private TextView company;

    @ViewInject(R.id.mine_zdlf_address_list)
    private LinearLayout address_list;

    @ViewInject(R.id.mine_zdlf_department)
    private TextView department;
    @ViewInject(R.id.mine_zdlf_positions)
    private TextView positions;
    @ViewInject(R.id.mine_zdlf_function)
    private ListView function;

    //选择岗位相关
    private PickerScrollViewPopupWindow pickerPopupWindow;
    private ArrayList<Pickers> pickersCacheList;
    private ArrayList<Pickers> pickersItemList;
    private ArrayList<String> pikersKey;
    private int pickerSelecect = 0;


    private WindowRecettingPasswordView passwordView;
    private Dialog dialog;
    private String password = "";
    private String old_password = "";


    //    private DatasBean mDatasBean = null;
//    private DatasBean.UserDao mUserDao;
    private AppBean appBean;
    private LoginBean loginBean;
    private UserBean userBean;
    private UserOrganizationBean userOrganizationBean;

    private ArrayList<UserAddinsBean> addinsBeen;
    private MineZDLFFunctionAdapter addinsAdapter;

    private BitmapUtils bitmapUtils;

    @Override
    public void initData() {
        title.setText("我的");

        if (pikersKey == null) {
            pikersKey = new ArrayList<>();
        }
        pikersKey.clear();


        if (pickersCacheList == null) {
            pickersCacheList = new ArrayList<>();
        }
        pickersCacheList.clear();
        if (pickersItemList == null) {
            pickersItemList = new ArrayList<>();
        }
        pickersItemList.clear();


        //功能
        if (addinsBeen == null) {
            addinsBeen = new ArrayList<>();
        }
        addinsBeen.clear();

        if (addinsAdapter == null) {
            addinsAdapter = new MineZDLFFunctionAdapter(mActivity, addinsBeen);
        }
        function.setAdapter(addinsAdapter);

        function.setOnTouchListener(new View.OnTouchListener() {

            //屏蔽掉滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        return false;
                }

            }

        });

        //----------------------------


        pickerPopupWindow = new PickerScrollViewPopupWindow();
        pickerPopupWindow.setOnData(new PickerScrollViewPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(int select) {
                //pickerSelecect = Integer.parseInt(pickers.getShowId());
                changePosition(select);
            }
        });

        bitmapUtils = new BitmapUtils(getActivity(), AddressUtils.img_cache_url);
        bitmapUtils.configDefaultLoadingImage(R.drawable.tab_me_n);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.tab_me_n);

        setUserData();
        //getUserData();
    }

    /**
     * 切换岗位
     *
     * @param selecect
     */
    private void changePosition(int selecect) {
        String position = "";
        String value = "";
        value = pickersItemList.get(selecect).getShowConetnt();
        for (String getKey : userOrganizationBean.positions.keySet()) {
            if (userOrganizationBean.positions.get(getKey).equals(value)) {
                position = getKey;
            }
        }

        RequestParams params = new RequestParams(SPUtil.getApiAuth(mActivity) + "/position");

        params.addBodyParameter("position_id", position);
        LogUtil.e("position================" + SPUtil.getApiAuth(mActivity) + "/position" + "::::" + position);

        HttpPostUtils httpPostUtil = new HttpPostUtils();

        httpPostUtil.post(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                //code = 0 时,表示切换成功
                LogUtil.e("切换岗位" + strJson);
                try {
                    JSONObject obj = new JSONObject(strJson);
                    int code = obj.optInt("code");
                    if (code == 0) {
                        getUserData();

                        //切换成功,清空推送数据
                        SPJpushUtil.setApprove(getContext(), 0);
                        SPJpushUtil.setBug(getContext(), 0);
                        SPJpushUtil.setCommunity(getContext(), 0);
                        SPJpushUtil.setCrm(getContext(), 0);
                        SPJpushUtil.setKnowledge(getContext(), 0);
                        SPJpushUtil.setMessage(getContext(), 0);
                        SPJpushUtil.setMyself(getContext(), 0);
                        SPJpushUtil.setPayment(getContext(), 0);
                        SPJpushUtil.setProposal(getContext(), 0);
                        SPJpushUtil.setScan(getContext(), 0);
                        SPJpushUtil.setShoot(getContext(), 0);
                        SPJpushUtil.setSignin(getContext(), 0);
                        SPJpushUtil.setTask(getContext(), 0);
                        SPJpushUtil.setTrain(getContext(), 0);

                        getPushCount(getActivity());
//                        pickerSelecect = selecect;
//                        positions.setText(pickersItemList.get(pickerSelecect).getShowConetnt());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("切换岗位错误" + ex);
                MyToast.showLong(getContext(), "切换岗位错误!");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getUserData() {
        showProgressDialog();

        //appBean = GsonUtil.getAppBean(SPUtil.getMobileJson(mActivity));


//        userBean = GsonUtil.getUserBean(SPUtil.getUserJson(mActivity));
//
//        if (userBean == null) {
//            MyToast.showShort(mActivity, "程序初始化出错,正在重启程序");
//            mActivity.finish();
//        }
//
//        userOrganizationBean = userBean.organization;
//
//        setUserData();
//        dismissProgressDialog();

        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = appBean.api_user + "/" + SPUtil.getUserId(mActivity) + "/" + "my.json";
        LogUtil.e("url--------------------" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                SPUtil.setUserJson(mActivity, strJson);//缓存用户信息
                LogUtil.e("getUserJson--------------------" + SPUtil.getUserJson(mActivity));
                userBean = GsonUtil.getUserBean(SPUtil.getUserJson(mActivity));
                userOrganizationBean = userBean.organization;


                setUserData();
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
                dismissProgressDialog();
            }
        });
    }


//    private void getUserData(){
//
//    }

    /**
     * 设置用户数据
     */
    private void setUserData() {
        appBean = GsonUtil.getAppBean(SPUtil.getMobileJson(mActivity));
        if (appBean.appid.equals("4661544566214097502")) {
            address_list.setVisibility(View.GONE);
        }

        userBean = GsonUtil.getUserBean(SPUtil.getUserJson(mActivity));
        userOrganizationBean = userBean.organization;

        LogUtil.e("头像==============" + userBean.avatar);
        bitmapUtils.display(avatar, SPUtil.getAvatar(mActivity));

        name.setText(userBean.name);

        if (userOrganizationBean != null) {
            company.setText(userOrganizationBean.company_name + "");//+""是为了防止userOrganizationBean为空
            //getOrganizationData();
        }

        if (userBean.organization.positions != null) {
            setPickers();

            positions.setText(userBean.organization.positions.get(userBean.organization.position_id));
        }

        department.setText(userBean.organization.dept_name);

        addinsBeen.clear();
        if (userBean.addins!=null) {//防止数据是null的
            addinsBeen.addAll(userBean.addins);
        }
        addinsAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(function);
    }

//    private void getOrganizationData() {
//
//
//        showProgressDialog();
//        HttpPostUtils httpPostUtil = new HttpPostUtils();
//        String url = "";
//        url = http_shuxinyun_url + userOrganizationBean.url;
//        LogUtil.e("公司架构userOrganizationBean url----===========" + url);
//        RequestParams params = new RequestParams(url);
//        httpPostUtil.get(mActivity, params);
//
//        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
//            @Override
//            public void onSuccess(String strJson) {
//                SPUtil.setUserOrganizationJson(mActivity, strJson);//缓存公司架构信息
//                //LogUtil.e("我的页面json");
////                LogUtil.e("公司架构==========="+SPUtil.getUserOrganizationJson(mActivity));
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                SPUtil.setUserOrganizationJson(mActivity, "");
//                LogUtil.e("我的页面json-----错误" + ex);
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                dismissProgressDialog();
//            }
//        });
//
//    }


    /**
     * 设置切换岗位数据源
     */
    private void setPickers() {
        //这里先静态设置
//        for (int i = 0; i < 5; i++) {
//            pickersCacheList.add(new Pickers("职位" + i, (i + 0) + ""));
//            pickersItemList.add(new Pickers("职位" + i, (i + 0) + ""));
//        }
        pickersCacheList.clear();
        pickersItemList.clear();
        Iterator iter = userOrganizationBean.positions.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
//            pickersCacheList.add(new Pickers(value, key));
//            pickersItemList.add(new Pickers(value, key));
            pikersKey.add(key);
            pickersCacheList.add(new Pickers(value, i + ""));
            pickersItemList.add(new Pickers(value, i + ""));
            i++;
        }
        iter = null;

    }


    /**
     * @param view
     */
    @Event(value = {R.id.title_back
            , R.id.mine_zdlf_address_list
            /*, R.id.mine_zdlf_personnel_file*/
            , R.id.mine_zdlf_reset_password
            , R.id.mine_zdlf_logout
            , R.id.mine_zdlf_icon
            , R.id.mine_zdlf_positions})
    private void approveChangeOnClick(View view) {
        Intent intent = new Intent(mActivity, EmptyActivity.class);
        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;

            case R.id.mine_zdlf_address_list://通讯录
                intent.putExtra("fragment_name", "addressList");
                startActivity(intent);
                break;

            case R.id.mine_zdlf_reset_password://重置密码

                if (!SPUtil.getResetPasswordUrl(mActivity).equals("")) {
                    intent.putExtra("fragment_name", "resetting_password");
                    startActivity(intent);
                } else {
                    MyToast.showLong(mActivity, "该用户没有重置密码权限!");
                }

                break;

            case R.id.mine_zdlf_logout://退出登录
                new AlertDialog.Builder(mActivity).setTitle("友情提示").setMessage("确定要退出登录吗?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                logoutOnservice();//从服务器注销

                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();

                break;

            case R.id.mine_zdlf_icon://更改头像
                //PhotoOrVideoUtils.doPhoto(mActivity, this, view);
                doPhoto(this, avatar);
                break;

            case R.id.mine_zdlf_positions://切换岗位
                //LogUtil.e("点击select¥¥¥"+pickerSelecect+"::getShowConetnt:"+pickersList.get(pickerSelecect).getShowConetnt());
                LogUtil.e("点击select¥¥¥" + pickerSelecect + "::getShowConetnt:" + pickerSelecect);
                pickersCacheList.clear();
                pickersCacheList.addAll(pickersItemList);
                pickerPopupWindow.setPickerScrollViewPopupWindow(mActivity, pickersCacheList, pickerSelecect, positions);
                break;

        }

    }

    /**
     * 告诉服务器__注销登录
     */
    private void logoutOnservice() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(SPUtil.getLogoutApi(mActivity));
        //params.setMultipart(true);
        httpPostUtils.post(mActivity, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("logoutOnservice---注销onSuccess" + strJson);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(strJson);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {


                        CloudPushService pushService = PushServiceFactory.getCloudPushService();
                        pushService.unbindAccount(new CommonCallback() {
                            @Override
                            public void onSuccess(String s) {
                                LogUtil.e("推送解绑状态-----成功===" + s);
                            }

                            @Override
                            public void onFailed(String s, String s1) {
                                LogUtil.e("推送解绑状态-----失败===" + s + "::::::" + s1);
                            }
                        });
                        SPUtil.setUserUUID(getActivity(), "");
                        SPUtil.setUserId(getContext(), "");
                        SPUtil.setToken(getContext(), "");
                        SPUtil.setAvatar(getContext(), "");


                        Intent i = new Intent(getActivity(), NewSplashActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        MyToast.showLong(mActivity, "注销失败!");
                        MyToast.showLong(mActivity, jsonObject.optString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(mActivity, "注销失败,请联系管理员");
                LogUtil.e("上传失败onError========UpDateAvatar" + ex);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("tag", "onActivityResult");
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                doPhoto();
            }

        } else if (requestCode == REQUEST_CODE_GET_PHOTO) {
            Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
            if (uri != null) {
                String imagePath = PhotoOrVideoUtils.getPath(mActivity, uri);
                upLoadAvatar(imagePath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片到服务器
     *
     * @param imagePath
     */
    private void upLoadAvatar(String imagePath) {
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
                        SPUtil.setAvatar(mActivity, jsonObject.optJSONObject("data").optString("src"));
                        UpDateAvatar();
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

//        HttpPostUtils httpPostUtils = new HttpPostUtils();
//        String url = http_shuxinyun_url;
//        RequestParams params = new RequestParams(SPUtil.getApiUpload(mActivity));
//        params.setMultipart(true);
//        File file = new File(imagePath);
//
//        params.addBodyParameter("avatar", file);
//        httpPostUtils.post(mActivity, params);
//
//        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
//            @Override
//            public void onSuccess(String strJson) {
//                LogUtil.e("上传成功onSuccess" + strJson);
//                try {
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    if (jsonObject.optInt("code") == 0) {
//                        SPUtil.setAvatar(mActivity, jsonObject.optJSONObject("data").optString("src"));
//                        UpDateAvatar();
//                    } else {
//                        MyToast.showLong(mActivity, "上传头像失败,请联系管理员");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                LogUtil.e("上传失败onError" + ex);
//                MyToast.showLong(mActivity, "上传头像失败,请联系管理员");
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });

    }

    /**
     * 将返回的头像地址更新到服务器
     */
    private void UpDateAvatar() {
        showProgressDialog();
        HttpPostUtils httpPostUtils = new HttpPostUtils();
        RequestParams params = new RequestParams(SPUtil.getApiAuth(mActivity) + "/avatar");
        //params.setMultipart(true);
        params.addBodyParameter("avatar", SPUtil.getAvatar(mActivity));
        httpPostUtils.post(mActivity, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("UpDateAvatar---上传成功onSuccess" + strJson);
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optBoolean("data")) {
                        bitmapUtils.display(avatar, SPUtil.getAvatar(mActivity));
                        MyToast.showLong(mActivity, "头像更新成功");
                        HttpPostUtils.getUserInfo(mActivity);//更新头像
                    } else {
                        MyToast.showLong(mActivity, "更新头像失败,请联系管理员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showLong(mActivity, "更新头像失败,请联系管理员");
                LogUtil.e("上传失败onError========UpDateAvatar" + ex);
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
     * 上传动作
     *
     * @param filepath 要上传的文件路径
     * @param number   要上传文件的对应的编号
     */
    public void upLoadFile(String filepath, String number, String ext) {
        LogUtil.e("传入的路径名==" + filepath);
        String io = MyBase64.file2String(filepath);
        dialog = ProgressDialog.show(getActivity(), "更新头像", "正在上传新头像", true, false);
        final RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));

        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));

        params.addBodyParameter("action", "submit");
        params.addBodyParameter("method", "set_user_avatar_asname");
        params.addBodyParameter("asname", "asname");
        params.addBodyParameter("name", "asname");

        params.addBodyParameter("avatar", io);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传结果为！！！！======" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.optBoolean("success", false);
                    if (!success) {
                        MyToast.showShort(getActivity(), "上传失败，请联系管理员");
                        LogUtil.e("上传失败，请联系管理员！！！！======");
                    } else {
                        MyToast.showShort(getActivity(), "上传成功！");
                        LogUtil.e("上传成功！！！！======");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("失败原因为！！！！======" + ex);
                MyToast.showShort(getActivity(), "上传失败，网络超时");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyBitmapUtils.getInstance(getActivity()).clearCache(userBean.avatar);
                bitmapUtils.display(avatar, userBean.avatar);
//                if (getActivity() instanceof MainActivity) {
//                    MainActivity a = (MainActivity) getActivity();
//                    a.setUserIcon();
//                }
                dialog.dismiss();
            }
        });

    }

    private PopupWindow photoWindow;
    public static int REQUEST_CODE_TAKE_PHOTO = 100;//拍照
    public static int REQUEST_CODE_GET_PHOTO = 200;//从相片中获取照片

    /**
     * 打开相册或者相机意图，只针对图片
     *
     * @param fragment 调用此方法的Fragment--当Fragment为空时，onActivityResult的返回类型为Activity中的；不为空，则为Fragment中的onActivityResult方法
     * @param v        调用此方法的View
     */
    public void doPhoto(final BaseFragment fragment, View v) {
        View contentView;
        photoWindow = null;
        if (photoWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(fragment.mActivity);
            contentView = mLayoutInflater.inflate(R.layout.pop_select_photo, null);
            photoWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView paizhao = (TextView) contentView.findViewById(R.id.paizhao);
            TextView xiangce = (TextView) contentView.findViewById(R.id.xiangce);
            TextView quxiao = (TextView) contentView.findViewById(R.id.quxiao);

            paizhao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开照相机的意图
                    PackageManager pm = mActivity.getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        MyToast.showLong(mActivity, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                        return;
                    }
                    takePhoto();
                    //MyToast.showShort(context, "点击了！");
//                    String state = Environment.getExternalStorageState();
//                    if (state.equals(Environment.MEDIA_MOUNTED)) {
//                        Intent i = new Intent();
//                        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                        fragment.startActivityForResult(i, REQUEST_CODE_TAKE_PHOTO);
//                        MyToast.showShort(getActivity(), "打开了照相机！");
//                    } else {
//                        Toast.makeText(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
//                    }
                    photoWindow.dismiss();
                }
            });
            xiangce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //打开相册的意图
                    Intent intent = new Intent();

                        /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                         /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                        /* 取得相片后返回本画面 */
                    fragment.startActivityForResult(intent, REQUEST_CODE_GET_PHOTO);
                    photoWindow.dismiss();
                }
            });
            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoWindow.dismiss();
                }
            });
        }

        ColorDrawable cd = new ColorDrawable(0x000000);
        photoWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);

        photoWindow.setOutsideTouchable(true);
        photoWindow.setFocusable(true);
        photoWindow.showAtLocation((View) v.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        photoWindow.update();
        photoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
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

    /**
     * 获取推送数量
     *
     * @param context
     */
    public void getPushCount(FragmentActivity context) {

        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = "http://api.shuxinyun.com/cache/users/" + SPUtil.getUserId(context) + "/pushitemcount.json";


        RequestParams params = new RequestParams(url);

        httpPostUtils.get(context, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                //TODO 防止后台给的数据是残疾的
                strJson = strJson.replace("\"\"", "0");
                LogUtil.e(strJson);
                BroadcastCallUtil.sendMessage2Activity(getContext(), strJson, GsonUtil.getPushBean(strJson));//发送消息,通知界面改数字

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("更新数据失败======" + ex);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
}

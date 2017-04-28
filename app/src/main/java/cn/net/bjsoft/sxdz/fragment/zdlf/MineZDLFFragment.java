package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.net.bjsoft.sxdz.activity.welcome.SplashActivity;
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
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;
import cn.net.bjsoft.sxdz.view.CircleImageView;
import cn.net.bjsoft.sxdz.view.WindowRecettingPasswordView;
import cn.net.bjsoft.sxdz.view.picker_scroll_view.Pickers;

import static cn.net.bjsoft.sxdz.utils.AddressUtils.http_shuxinyun_url;


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


        getUserData();
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

        RequestParams params = new RequestParams(appBean.api_auth + "/position");

        params.addBodyParameter("position_id", position);
        LogUtil.e("position================" + appBean.api_auth + "/position" + "::::" + position);

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
        //LogUtil.e("json" + mJson);
        appBean = GsonUtil.getAppBean(mJson);
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

//

    }

    /**
     * 设置用户数据
     */
    private void setUserData() {

        LogUtil.e("头像==============" + userBean.avatar);
        bitmapUtils.display(avatar, userBean.avatar);
        SPUtil.setAvatar(mActivity, userBean.avatar);//缓存头像地址
        name.setText(userBean.name);

        if (userOrganizationBean != null) {
            company.setText(userOrganizationBean.root_company_name + "");//+""是为了防止userOrganizationBean为空
            getOrganizationData();
        }

        setPickers();

        positions.setText(userBean.organization.positions.get(userBean.organization.position_id));
        department.setText(userBean.organization.dept_name);

        addinsBeen.clear();
        addinsBeen.addAll(userBean.addins);
        LogUtil.e("gangwei 功能=========" + addinsBeen.size() + "");
        addinsAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(function);
    }

    private void getOrganizationData() {

        showProgressDialog();
        HttpPostUtils httpPostUtil = new HttpPostUtils();
        String url = "";
        url = http_shuxinyun_url + userOrganizationBean.url;
        LogUtil.e("公司架构userOrganizationBean url----===========" + url);
        RequestParams params = new RequestParams(url);
        httpPostUtil.get(mActivity, params);

        httpPostUtil.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {
                SPUtil.setUserOrganizationJson(mActivity, strJson);//缓存公司架构信息
                //LogUtil.e("我的页面json");
//                LogUtil.e("公司架构==========="+SPUtil.getUserOrganizationJson(mActivity));
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
                dismissProgressDialog();
            }
        });

    }


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
                PhotoOrVideoUtils.doPhoto(mActivity, this, view);
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
                    if (code == 0){
                        SPUtil.setUserUUID(getActivity(), "");
                        SPUtil.setUserId(getContext(), "");
                        SPUtil.setToken(getContext(), "");
                        SPUtil.setAvatar(getContext(), "");
                        Intent i = new Intent(getActivity(), SplashActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }else {
                        MyToast.showLong(mActivity,"注销失败!");
                        MyToast.showLong(mActivity,jsonObject.optString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



//                try {
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    if (jsonObject.optBoolean("data")) {
//                        bitmapUtils.display(avatar, SPUtil.getAvatar(mActivity));
//                        MyToast.showLong(mActivity, "头像更新成功");
//                    } else {
//                        MyToast.showLong(mActivity, "更新头像失败,请联系管理员");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            String imagePath = PhotoOrVideoUtils.getPath(mActivity, uri);
            //upLoadFile(imagePath, "", "");
            upLoadAvatar(imagePath);

        }

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
}

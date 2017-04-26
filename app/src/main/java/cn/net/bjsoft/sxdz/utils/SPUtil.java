package cn.net.bjsoft.sxdz.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 优先共享数据
 * Created by zkagang on 2016/5/10.
 */
public class SPUtil {
    /**
     * 获取  --  USER_KEY_JSON = "user_json"缓存
     */
    public static String getApiAuth(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_API_AUTH, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_API_AUTH, "");
    }

    /**
     * 设置   --   USER_KEY_JSON = "user_json"缓存
     */
    public static void setApiAuth(Context context, String api_auth) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_API_AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_API_AUTH, api_auth);
        editor.commit();
    }


    /**
     * 获取  --  USER_KEY_API_UPLOAD = "api_upload"上传文件通用地址
     */
    public static String getApiUpload(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_API_UPLOAD, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_API_UPLOAD, "");
    }

    /**
     * 设置   --   USER_KEY_API_UPLOAD = "api_upload"上传文件通用地址
     */
    public static void setApiUpload(Context context, String api_upload) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_API_UPLOAD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_API_UPLOAD, api_upload);
        editor.commit();
    }

    /**
     * 获取  --  USER_KEY_JSON = "user_json"缓存
     */
    public static String getUserJson(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_USER_JSON, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_USER_JSON, "");
    }

    /**
     * 设置   --   USER_KEY_JSON = "user_json"缓存
     */
    public static void setUserJson(Context context, String user_json) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_USER_JSON, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_USER_JSON, user_json);
        editor.commit();
    }


    /**
     * 获取  --  USER_KEY_JSON = "user_json"缓存
     */
    public static String getUserOrganizationJson(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_USER_ORGANIZATION_JSON, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_USER_ORGANIZATION_JSON, "");
    }

    /**
     * 设置   --   USER_KEY_JSON = "user_json"缓存
     */
    public static void setUserOrganizationJson(Context context, String user_organization_json) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_USER_ORGANIZATION_JSON, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_USER_ORGANIZATION_JSON, user_organization_json);
        editor.commit();
    }




    /**
     * 获取  --  ismember值,
     */
    public static boolean getIsMember(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_ISMEMBER, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.USER_KEY_ISMEMBER, false);
    }

    /**
     * 设置   --   userid值,
     */
    public static void setIsMember(Context context, boolean ismember) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_ISMEMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.USER_KEY_ISMEMBER, ismember);
        editor.commit();
    }

    /**
     * 获取  --  userid值,
     */
    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_USERID, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_USERID, "");
    }

    /**
     * 设置   --   userid值,
     */
    public static void setUserId(Context context, String userid) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_USERID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_USERID, userid);
        editor.commit();
    }

    /**
     * 获取  --  token值,判断是否登录的唯一标识符
     */
    public static String getToken(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_TOKEN, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_TOKEN, "");
    }

    /**
     * 设置   --   获取token值,判断是否登录的唯一标识符
     */
    public static void setToken(Context context, String token) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_TOKEN, token);
        editor.commit();
    }

    /**
     * 获取  --  appid,判断是哪一个程序id在运行
     */
    public static String getAppid(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_APPID, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_APPID, "");
    }

    /**
     * 设置   --   获取appid值,判断是哪一个程序id在运行
     */
    public static void setAppid(Context context, String appid) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_APPID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_APPID, appid);
        editor.commit();
    }


    /**
     * 获取  --  secret,
     */
    public static String getSecret(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_SECRET, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_SECRET, "");
    }

    /**
     * 设置   --   获取secret值,
     */
    public static void setSecret(Context context, String secret) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_SECRET, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_SECRET, secret);
        editor.commit();
    }


    /**
     * 得到是否第一次打开程序
     */
    public static boolean getIsFirst(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        return preferences.getBoolean("isFirst", false);
    }

    /**
     * 获取是否第一次打开程序
     */
    public static void setIsFirst(Context context, boolean loginStatus) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirst", loginStatus);
        editor.commit();
    }

    /**
     * 得到登录状态
     */
    public static boolean getLoginStatus(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_LOGINSTATUS_FLAG, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.USER_KEY_LOGINSTATUS_FLAG, false);
    }

    /**
     * 保存登录状态
     */
    public static void setLoginStatus(Context context, boolean loginStatus) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_LOGINSTATUS_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.USER_KEY_LOGINSTATUS_FLAG, loginStatus);
        editor.commit();
    }

    /**
     * 得到用户名
     */
    public static String getLoginUserName(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_LOGIN_USERNAME_FLAG, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_LOGIN_USERNAME_FLAG, "");
    }

    /**
     * 保存用户名
     */
    public static void setLoginUserName(Context context, String loginUserName) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_LOGIN_USERNAME_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_LOGIN_USERNAME_FLAG, loginUserName);
        editor.commit();
    }

    /**
     * 得到用户密码
     */
    public static String getLoginPassword(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_LOGIN_PASSWORD_FLAG, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_LOGIN_PASSWORD_FLAG, "");
    }

    /**
     * 保存用户密码
     */
    public static void setLoginPassword(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_LOGIN_PASSWORD_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_LOGIN_PASSWORD_FLAG, loginPassword);
        editor.commit();
    }


    /**
     * 得到用户姓名
     */
    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_NAME, "");
    }

    /**
     * 保存用户姓名
     */
    public static void setUserName(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_NAME, loginPassword);
        editor.commit();
    }

    /**
     * 得到用户昵称
     */
    public static String getUserNiceName(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_NICK_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_NICK_NAME, "");
    }

    /**
     * 保存用户昵称
     */
    public static void setUserNiceName(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_NICK_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_NICK_NAME, loginPassword);
        editor.commit();
    }

    /**
     * 得到用户邮箱
     */
    public static String getUserEmail(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_EMAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_EMAIL, "");
    }

    /**
     * 保存用户邮箱
     */
    public static void setUserEmail(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_EMAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_EMAIL, loginPassword);
        editor.commit();
    }

    /**
     * 得到手机验证码短信模板编号
     */
    public static String getUserCode(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_CODE, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_CODE, "");
    }

    /**
     * 保存手机验证码短信模板编号
     */
    public static void setUserCode(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_CODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_CODE, loginPassword);
        editor.commit();
    }

    /**
     * 得到随机码
     */
    public static String getUserRandCode(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_RANDCODE, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_KEY_RANDCODE, "");
    }

    /**
     * 保存随机码
     */
    public static void setUserRandCode(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.USER_KEY_RANDCODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USER_KEY_RANDCODE, loginPassword);
        editor.commit();
    }

    /**
     * 得到uuid
     */
    public static String getUserUUID(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.UUID, Context.MODE_PRIVATE);
        return preferences.getString(Constants.UUID, "");
    }

    /**
     * 保存uuid
     */
    public static void setUserUUID(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.UUID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.UUID, loginPassword);
        editor.commit();
    }

    /**
     * 得到phoneuuid
     */
    public static String getUserPUUID(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.PHONE_UUID, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PHONE_UUID, "");
    }

    /**
     * 保存phoneuuid
     */
    public static void setUserPUUID(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.PHONE_UUID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PHONE_UUID, loginPassword);
        editor.commit();
    }

    /**
     * 得到电话
     */
    public static String getUserP(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.PHONE_PHONE, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PHONE_PHONE, "");
    }

    /**
     * 保存电话
     */
    public static void setUserP(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.PHONE_PHONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PHONE_PHONE, loginPassword);
        editor.commit();
    }

    /**
     * 得到纬度
     */
    public static String getLat(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.LATITUDE, Context.MODE_PRIVATE);
        return preferences.getString(Constants.LATITUDE, "0");
    }

    /**
     * 保存纬度
     */
    public static void setLat(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.LATITUDE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.LATITUDE, loginPassword);
        editor.commit();
    }

    /**
     * 得到经度
     */
    public static String getLong(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.LONGITUDE, Context.MODE_PRIVATE);
        return preferences.getString(Constants.LONGITUDE, "0");
    }

    /**
     * 保存经度
     */
    public static void setLong(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.LONGITUDE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.LONGITUDE, loginPassword);
        editor.commit();
    }

    /**
     * 得到详细地址
     */
    public static String getAddress(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(Constants.ADDRESS, Context.MODE_PRIVATE);
        return preferences.getString(Constants.ADDRESS, "0");
    }

    /**
     * 保存详细地址
     */
    public static void setAddress(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.ADDRESS, loginPassword);
        editor.commit();
    }

    /**
     * dept_name
     */
    public static String getDept(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("dept_name", Context.MODE_PRIVATE);
        return preferences.getString("dept_name", "");
    }

    /**
     * dept_name
     */
    public static void setDept(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("dept_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("dept_name", loginPassword);
        editor.commit();
    }

    /**
     * company_name
     */
    public static String getCompany(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("company_name", Context.MODE_PRIVATE);
        return preferences.getString("company_name", "");
    }

    /**
     * company_name
     */
    public static void setCompany(Context context, String loginPassword) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("company_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("company_name", loginPassword);
        editor.commit();
    }

    /**
     * 头像
     */
    public static String getAvatar(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("touxiang", Context.MODE_PRIVATE);
        return preferences.getString(Constants.AVATAR, "");
    }

    /**
     * 头像
     */
    public static void setAvatar(Context context, String avatar_url) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("touxiang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.AVATAR, avatar_url);
        editor.commit();
    }

}

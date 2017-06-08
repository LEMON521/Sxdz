package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.video.PhotoActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;

/**
 * Created by 靳宁 on 2017/2/14.
 */
@ContentView(R.layout.fragment_register_humen)
public class RegisterHumenFragment extends BaseFragment {
//    @ViewInject(R.id.register_humen_back)
//    private ImageView back;//返回

    @ViewInject(R.id.register_humen_editer_code)
    private EditText code;//身份证号
    @ViewInject(R.id.register_humen_editer_emile_code)
    private EditText emile;//电子邮箱
    @ViewInject(R.id.register_humen_editer_organization_code)
    private EditText organization;//所在机构


    @ViewInject(R.id.regiest_humen_ll_front)
    private LinearLayout ll_front;//营业执照照片
    @ViewInject(R.id.regiest_humen_ll_back)
    private LinearLayout ll_back;//营业执照照片

    @ViewInject(R.id.regiest_humen_iv_front)
    private ImageView front;//营业执照照片
    @ViewInject(R.id.regiest_humen_iv_front_delete)
    private ImageView front_delete;//营业执照照片
    @ViewInject(R.id.regiest_humen_iv_back)
    private ImageView back;//营业执照照片
    @ViewInject(R.id.regiest_humen_iv_back_delete)
    private ImageView back_delete;//营业执照照片


    @ViewInject(R.id.register_humen_btn_submit)
    private ImageView over;//完成按钮

    private String username = "";
    private String[] filePath = {"", ""};//图片绝对路径
    private String[] fileName = {"", ""};//图片名称,不包含路径名
    private Uri[] fileUri = {null, null};//图片uri
    private String[] fileUrl = {"", ""};//图片绝对路径
    private boolean[] isSet = {false, false};//图片是否设置的开关
    private String whitchImage = "front";//"front","back"---是哪个图片点击事件而想要获取照片的标识符

    @Override
    public void initData() {

        username = getArguments().getString("username");
        //ll_back.setVisibility(View.GONE);
    }

    @Event(value = {R.id.register_humen_back,
            R.id.regiest_humen_iv_front,
            R.id.regiest_humen_iv_front_delete,
            R.id.regiest_humen_iv_back,
            R.id.regiest_humen_iv_back_delete,
            R.id.register_humen_btn_submit})
    private void registerhumenOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_humen_back://返回
                getActivity().finish();
                break;

            case R.id.regiest_humen_iv_front://设置照片
                if (!isSet[0]) {//没有设置
                    //MyToast.showShort(getActivity(), "拍摄照片");
                    whitchImage = "front";
                    PhotoOrVideoUtils.doPhoto(mActivity, this, front);
//                    ll_back.setVisibility(View.VISIBLE);
                } else {//设置过图片，点击就是预览图片
                    Intent intent = new Intent(mActivity, PhotoActivity.class);
                    intent.putExtra("url", filePath[0]);
                    startActivity(intent);
                }
                break;

            case R.id.regiest_humen_iv_front_delete://删除照片
                front.setImageResource(R.drawable.common_photo);
                fileUri[0] = null;
                filePath[0] = "";
                isSet[0] = false;
                break;

            case R.id.regiest_humen_iv_back://设置背面照片
                if (!isSet[1]) {//没有设置
                    whitchImage = "back";
                    //MyToast.showShort(getActivity(), "拍摄照片");
                    PhotoOrVideoUtils.doPhoto(mActivity, this, back);
                } else {//设置过图片，点击就是预览图片
                    Intent intent = new Intent(mActivity, PhotoActivity.class);
                    intent.putExtra("url", filePath[1]);
                    startActivity(intent);
                }
                break;
            case R.id.regiest_humen_iv_back_delete://删除背面照片
//                if (!isSet[0]){//如果正面不是默认的话，
//                    ll_back.setVisibility(View.GONE);
//                }
                back.setImageResource(R.drawable.common_photo);
                fileUri[1] = null;
                filePath[1] = "";
                isSet[1] = false;
                break;
            case R.id.register_humen_btn_submit://访问网络--注册
                submit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            if (!uri.toString().equals("")) {
                if (whitchImage.equals("front")) {

                    fileUri[0] = uri;
                    LogUtil.e("获取到的uri为=====" + uri);

                    isSet[0] = true;
                    filePath[0] = PhotoOrVideoUtils.getPath(mActivity, fileUri[0]);
                    fileName[0] = filePath[0].substring(filePath[0].lastIndexOf("/") + 1);
                    upLoadImage(filePath[0]);

                }
                if (whitchImage.equals("back")) {
                    fileUri[1] = uri;
                    LogUtil.e("获取到的uri为=====" + uri);

                    isSet[1] = true;
                    filePath[1] = PhotoOrVideoUtils.getPath(mActivity, fileUri[1]);
                    fileName[1] = filePath[1].substring(filePath[1].lastIndexOf("/") + 1);
                    upLoadImage(filePath[1]);
                }
            } else {
                MyToast.showShort(mActivity, "选择图片出错，请重新选择图片");
            }
        } else {
            MyToast.showShort(mActivity, "没有选择图片，请重新选择图片");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片
     */
    private void upLoadImage(String imagePath) {
        showProgressDialog();
        RequestParams params = new RequestParams(SPUtil.getApiUpload(mActivity));
        params.setMultipart(true);
        File file = new File(imagePath);

        params.addBodyParameter("registerImage", file);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("上传成功onSuccess" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("code") == 0) {

                        if (whitchImage.equals("front")) {
                            front.setImageURI(fileUri[0]);//将获取到的图片设置到控件上
                            fileUrl[0] = jsonObject.optJSONObject("data").optString("src");
                        }
                        if (whitchImage.equals("back")) {
                            back.setImageURI(fileUri[1]);//将获取到的图片设置到控件上
                            fileUrl[1] = jsonObject.optJSONObject("data").optString("src");
                        }


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
                MyToast.showLong(mActivity, "上传身份证失败,请联系管理员");
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
     * 将页面的数据提交到服务器的方法
     */
    private void submit() {
        String emileStr = emile.getText().toString().trim();
        String organizationStr = organization.getText().toString().trim();
        if (TextUtils.isEmpty(emileStr)) {
            MyToast.showShort(getActivity(), "请输入邮箱信息");
            return;
        }

        if (TextUtils.isEmpty(organizationStr)) {
            MyToast.showShort(getActivity(), "请输入所在机构信息");
            return;
        }

        showProgressDialog();

        String url = GsonUtil.getAppBean(SPUtil.getMobileJson(mActivity)).login.registerapi;
        LogUtil.e("url$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("submit_id", "register");
        params.addBodyParameter("token", SPUtil.getToken(mActivity));
        params.addBodyParameter("appid", SPUtil.getAppid(mActivity));
        params.addBodyParameter("secret", SPUtil.getSecret(mActivity));

        StringBuilder sb = new StringBuilder();

        //sb.append("{\"data\":{");
        sb.append("{");

        sb.append("\"username\":\"");
        sb.append(username);
        sb.append("\",");

        sb.append("\"emile\":\"");
        sb.append(emileStr);
        sb.append("\",");

        sb.append("\"organization\":\"");
        sb.append(organizationStr);
        sb.append("\",");


        sb.append("\"files\":[");
        for (int i = 0; i < fileUrl.length; i++) {
            sb.append("{");

            sb.append("\"url\":\"");
            sb.append(fileUrl[i]);
            sb.append("\",");

            sb.append("\"name\":\"");
            sb.append(fileName[i]);
            sb.append("\"");

            sb.append("}");
            if (i != (fileUrl.length - 1)) {
                sb.append("},");
            }
        }
        sb.append("],");


        sb.append("\"code\":\"");//身份证号
        sb.append(code.getText().toString().trim());
        sb.append("\"");


        sb.append("}");

        params.addBodyParameter("data", sb.toString());

        LogUtil.e("params-------" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String strJson) {
                LogUtil.e("-----------------注册详情------注册获取消息----------------" + strJson);

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    if (jsonObject.optInt("code") == 0) {

                        MyToast.showShort(mActivity, "注册成功!");
                        mActivity.finish();
                    } else {
                        MyToast.showLong(mActivity, "注册失败,请联系管理员");
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
}

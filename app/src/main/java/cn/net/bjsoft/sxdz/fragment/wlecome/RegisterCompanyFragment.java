package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
@ContentView(R.layout.fragment_register_company)
public class RegisterCompanyFragment extends BaseFragment {
    @ViewInject(R.id.register_company_back)
    private ImageView back;//返回

    @ViewInject(R.id.register_company_editer_code)
    private EditText code;//营业执照号
    @ViewInject(R.id.register_company_editer_emile_code)
    private EditText emile;//电子邮箱
    @ViewInject(R.id.register_company_editer_organization_code)
    private EditText organization;//企业名称

    @ViewInject(R.id.regiest_company_iv_front)
    private ImageView front;//营业执照照片
    @ViewInject(R.id.regiest_company_iv_front_delete)
    private ImageView delete;//营业执照照片

    @ViewInject(R.id.register_company_btn_submit)
    private ImageView over;//完成按钮

    private String username = "";
    private String fileName = "";//图片绝对路径
    private String fileUrl = "";
    private String filePath = "";//图片绝对路径
    private Uri fileUri = null;//图片uri
    private boolean isSet = false;//图片是否设置的开关

    @Override
    public void initData() {
        username = getArguments().getString("username");
        //LogUtil.e("Fragment中@@@@@@@" + "getActivity()::" + getActivity() + "/n" + "getContext()::" + getContext());
    }


    @Event(value = {R.id.register_company_back,
            R.id.regiest_company_iv_front,
            R.id.regiest_company_iv_front_delete,
            R.id.register_company_btn_submit})
    private void registerCompanyOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_company_back://返回
                getActivity().finish();
                break;
            case R.id.regiest_company_iv_front://设置照片
                if (!isSet) {//没有设置
                    MyToast.showShort(getActivity(), "拍摄照片");
                    PhotoOrVideoUtils.doPhoto(mActivity, this, front);
                } else {//设置过图片，点击就是预览图片
                    Intent intent = new Intent(mActivity, PhotoActivity.class);
                    intent.putExtra("url", filePath);
                    startActivity(intent);
                }
                break;
            case R.id.regiest_company_iv_front_delete://删除照片
                front.setImageResource(R.drawable.common_photo);
                fileUri = null;
                filePath = "";
                isSet = false;
                break;
            case R.id.register_company_btn_submit://访问网络--注册
                submit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fileUri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (fileUri != null) {
            if (!fileUri.toString().equals("")) {

                filePath = PhotoOrVideoUtils.getPath(mActivity, fileUri);
                isSet = true;
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                upLoadImage(filePath);
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

                        front.setImageURI(fileUri);//将获取到的图片设置到控件上
                        fileUrl = jsonObject.optJSONObject("data").optString("src");


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

        sb.append("{");

        sb.append("\"url\":\"");
        sb.append(fileUrl);
        sb.append("\",");

        sb.append("\"name\":\"");
        sb.append(fileName);
        sb.append("\"");

        sb.append("}");


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

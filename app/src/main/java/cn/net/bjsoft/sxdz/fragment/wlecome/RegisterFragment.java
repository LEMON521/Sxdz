package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.login.RegisterActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by 靳宁 on 2017/2/14.
 */
@ContentView(R.layout.fragment_register)
public class RegisterFragment extends BaseFragment {
    @ViewInject(R.id.register_user_edit)
    private EditText editor_user;//用户名、手机号
    @ViewInject(R.id.register_editer_code)
    private EditText editor_code;//验证码

    @ViewInject(R.id.register_sendcode)
    private Button btn_code;//发送验证码按钮

    @ViewInject(R.id.register_rb_humen)
    private RadioButton rBtn_humen;//个人按钮
    @ViewInject(R.id.register_rb_company)
    private RadioButton rBtn_company;//企业按钮

    @ViewInject(R.id.register_txt_agreement)
    private TextView tv_agreement;//服务使用协议
    @ViewInject(R.id.register_txt_privacyClause)
    private TextView tv_privacy;//隐私条款

    @ViewInject(R.id.register_editer_summary)
    private EditText editor_summary;//个人功能简介

    @ViewInject(R.id.register_btn_next)
    private Button btn_next;//下一步


    private boolean submitLock = false;//验证码发送控制器
    private Activity context;
    /**
     * 验证码
     */
    private static final int RETRY_INTERVAL = 30;
    private int time = RETRY_INTERVAL;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    btn_code.setEnabled(true);
                    btn_code.setClickable(true);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void initData() {
        context = getActivity();
    }

    @Event(value = {R.id.register_back, R.id.register_sendcode, R.id.register_rb_humen,
            R.id.register_rb_company, R.id.register_txt_agreement,
            R.id.register_txt_privacyClause, R.id.register_btn_next,})
    private void rejisterOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                getActivity().finish();
                break;

            case R.id.register_sendcode://验证码

                if (editor_user.getText().toString().trim().equals("")) {
                    MyToast.showShort(getActivity(), "手机号码不能为空");
                } else if (isMobileNO(editor_user.getText().toString().trim())) {
                    sendCode();
                } else {
                    MyToast.showShort(getActivity(), "手机号码格式错误");
                }
                break;

            case R.id.register_rb_humen://个人按钮
                rBtn_company.setChecked(false);
                editor_summary.setHint("个人功能简介");
                break;

            case R.id.register_rb_company://企业按钮
                rBtn_humen.setChecked(false);
                editor_summary.setHint("企业功能简介");
                break;

            case R.id.register_txt_agreement://服务使用协议
                MyToast.showShort(getActivity(), "服务使用协议");
                break;

            case R.id.register_txt_privacyClause://隐私条款
                MyToast.showShort(getActivity(), "隐私条款");
                break;

            case R.id.register_btn_next://下一步
                if (editor_user.getText().toString().isEmpty() || editor_code.getText().toString().isEmpty()) {
                    MyToast.showShort(context, "手机号或验证码为空");
                } else {
                    //validateCode();//真正开发时用到的
                    if (editor_code.getText().toString().trim().equals("111111")) {
                        Intent intent = new Intent(getActivity(), RegisterActivity.class);
                        if (rBtn_humen.isChecked()) {
                            //个人注册
                            intent.putExtra("function", "register_humen");
                        } else if (rBtn_company.isChecked()) {
                            //企业注册
                            intent.putExtra("function", "register_company");
                        }
                        startActivity(intent);
                        //getActivity().finish();
                    }else {
                        MyToast.showShort(context, "验证码错误");
                    }
                }
                break;
        }
    }

    /**
     * 判断手机号码格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13\\d|15\\d|17[3678]|18\\d|14[57])\\d{8})|170[0125789]\\d{7}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 发送验证码
     */
    public void sendCode() {
        if (submitLock) {
            return;
        }
        showProgressDialog();
        submitLock = true;
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("action", AddressUtils.code_action);
        params.addBodyParameter("method", AddressUtils.code_method);
        params.addBodyParameter("phone", editor_user.getText().toString().trim());
        params.addBodyParameter("name", SPUtil.getUserName(getActivity()).isEmpty() ? editor_user.getText().toString().trim() : SPUtil.getUserName(getActivity()));
        params.addBodyParameter("pattern", /*Perference.getUserCode(this)*/"610");
        Log.e("tag", SPUtil.getUserPUUID(context) + "" +
                "==" + SPUtil.getUserRandCode(context) + "" +
                "==" + SPUtil.getUserUUID(context) + "" +
                "==" + AddressUtils.code_action + "" +
                "==" + AddressUtils.code_method + "" +
                "==" + editor_user.getText().toString().trim() + "" +
                "==" + (SPUtil.getUserName(getActivity()).isEmpty() ? editor_user.getText().toString().trim() : SPUtil.getUserName(getActivity())) + "" +
                "==" + SPUtil.getUserCode(getActivity()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("忘记密码=-==", result);

                submitLock = false;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean success = jsonObject.optBoolean("success");
                        if (success) {
                            btn_code.setEnabled(false);
                            btn_code.setClickable(false);
                            countDown();
                        } else {
                            MyToast.showShort(getActivity(), jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(getActivity(), "网络连接错误");
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
     * 验证验证码是否正确
     */
    public void validateCode() {
        if (submitLock) {
            return;
        }
        submitLock = true;
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("action", AddressUtils.code_action);
        params.addBodyParameter("method", AddressUtils.val_method);
        params.addBodyParameter("phone", editor_user.getText().toString());
        params.addBodyParameter("name", SPUtil.getUserName(getActivity()).isEmpty() ? editor_user.getText().toString() : SPUtil.getUserName(getActivity()));
        params.addBodyParameter("randcode", editor_code.getText().toString());

        Log.e("修改密码", SPUtil.getUserPUUID(getActivity()) + "" +
                "==" + SPUtil.getUserRandCode(getActivity()) + "" +
                "==" + SPUtil.getUserUUID(getActivity()) + "" +
                "==" + AddressUtils.code_action + "" +
                "==" + AddressUtils.val_method + "" +
                "==" + editor_user.getText().toString() + "" +
                "==" + (SPUtil.getUserName(getActivity()).isEmpty() ? editor_user.getText().toString() : SPUtil.getUserName(getActivity())) + "" +
                "==" + SPUtil.getUserCode(getActivity()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                submitLock = false;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean success = jsonObject.optBoolean("success");
                        if (success) {
                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                            if (rBtn_humen.isChecked()) {
                                //个人注册
                                intent.putExtra("function", "register_humen");
                            } else if (rBtn_company.isChecked()) {
                                //企业注册
                                intent.putExtra("function", "register_company");
                            }
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            dismissProgressDialog();
                            MyToast.showShort(getActivity(), jsonObject.optString("feedback"));
                            LogUtil.e("失败===" + result);
                        }
                    } else {
                        dismissProgressDialog();
                    }
                } catch (JSONException e) {
                    dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                submitLock = false;
                MyToast.showShort(context, "网络连接错误");
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

    //倒计时
    private void countDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (time-- > 0) {
                    String unReceive = getActivity().getResources()
                            .getString(R.string.smssdk_receive_msg, time);
                    updateTvUnreceive(unReceive);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                updateTvUnreceive(getResources().getString(R.string.sendyanzhnegma));
                handler.sendEmptyMessage(1);
                time = RETRY_INTERVAL;
            }
        }).start();
    }

    private void updateTvUnreceive(final String unReceive) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                btn_code.setText(Html.fromHtml(unReceive));
            }
        });
    }
}

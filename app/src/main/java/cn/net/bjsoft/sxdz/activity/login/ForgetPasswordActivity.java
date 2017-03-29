package cn.net.bjsoft.sxdz.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MDUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

public class ForgetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.user_edit)
    EditText userEdit;
    @ViewInject(R.id.yan_edit)
    EditText yanEdit;
    @ViewInject(R.id.pass_edit)
    EditText passEdit;
    @ViewInject(R.id.send)
    Button send;
    @ViewInject(R.id.revise)
    Button revise;

    private boolean submitLock = false;
    /**验证码*/
    private static final int RETRY_INTERVAL = 30;
    private int time = RETRY_INTERVAL;

    private boolean tag=false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    send.setEnabled(true);
                    send.setClickable(true);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        x.view().inject(this);

        yanEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //validateCode();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEdit.getText().toString().isEmpty()){
                    MyToast.showShort(context,"请填写手机号码");
                }else if(isMobileNO(userEdit.getText().toString())){
                    sendCode();
                }else{
                    MyToast.showShort(context,"手机号码格式错误");
                }
            }
        });
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEdit.getText().toString().isEmpty()||passEdit.getText().toString().isEmpty()){
                    MyToast.showShort(context,"手机号或密码为空");
                }else{
                    validateCode();
                }
            }
        });

    }

    /**
     * 发送验证码
     */
    public void sendCode(){
        if(submitLock){
            return;
        }
        showProgressDialog();
        submitLock=true;
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(this));
        params.addBodyParameter("single_code",SPUtil.getUserRandCode(this) );
        params.addBodyParameter("uuid", SPUtil.getUserUUID(this));
        params.addBodyParameter("action", AddressUtils.code_action);
        params.addBodyParameter("method", AddressUtils.code_method);
        params.addBodyParameter("phone", userEdit.getText().toString());
        params.addBodyParameter("name", SPUtil.getUserName(this).isEmpty()?userEdit.getText().toString():SPUtil.getUserName(this));
        params.addBodyParameter("pattern", /*Perference.getUserCode(this)*/"610");
        Log.e("tag",SPUtil.getUserPUUID(this)+"=="+SPUtil.getUserRandCode(this)+"=="+SPUtil.getUserUUID(this)+"=="+AddressUtils.code_action+"=="+AddressUtils.code_method+"=="+userEdit.getText().toString()+"=="+(SPUtil.getUserName(this).isEmpty()?userEdit.getText().toString():SPUtil.getUserName(this))+"=="+SPUtil.getUserCode(this));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("忘记密码=-==",result);
                dismissProgressDialog();
                submitLock = false;
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject!=null){
                        boolean success=jsonObject.optBoolean("success");
                        if(success){
                            send.setEnabled(false);
                            send.setClickable(false);
                            countDown();
                        }else{
                            MyToast.showShort(ForgetPasswordActivity.this,jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(ForgetPasswordActivity.this,"网络连接错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 判断手机号码格式
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern
                .compile("^((13\\d|15\\d|17[3678]|18\\d|14[57])\\d{8})|170[0125789]\\d{7}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    //倒计时
    private void countDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (time-- > 0) {
                    String unReceive = ForgetPasswordActivity.this.getResources()
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
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                send.setText(Html.fromHtml(unReceive));
            }
        });
    }

    /**
     * 验证验证码是否正确
     */
    public void validateCode(){
        if(submitLock){
            return;
        }
        submitLock=true;
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(this));
        params.addBodyParameter("single_code",SPUtil.getUserRandCode(this) );
        params.addBodyParameter("uuid", SPUtil.getUserUUID(this));
        params.addBodyParameter("action", AddressUtils.code_action);
        params.addBodyParameter("method", AddressUtils.val_method);
        params.addBodyParameter("phone", userEdit.getText().toString());
        params.addBodyParameter("name", SPUtil.getUserName(this).isEmpty()?userEdit.getText().toString():SPUtil.getUserName(this));
        params.addBodyParameter("randcode", yanEdit.getText().toString());

        Log.e("修改密码",SPUtil.getUserPUUID(this)+"=="+SPUtil.getUserRandCode(this)+"=="+SPUtil.getUserUUID(this)+"=="+AddressUtils.code_action+"=="+AddressUtils.val_method+"=="+userEdit.getText().toString()+"=="+(SPUtil.getUserName(this).isEmpty()?userEdit.getText().toString():SPUtil.getUserName(this))+"=="+SPUtil.getUserCode(this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                submitLock = false;
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject!=null){
                        boolean success=jsonObject.optBoolean("success");
                        if(success){
                            RevisePassword();
                        }else{
                            dismissProgressDialog();
                            MyToast.showShort(ForgetPasswordActivity.this,jsonObject.optString("feedback"));
                            LogUtil.e("失败==="+result);
                        }
                    }else{
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
                dismissProgressDialog();
                MyToast.showShort(ForgetPasswordActivity.this,"网络连接错误");
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
     * 修改密码
     */
    public void RevisePassword(){
        if(submitLock){
            return;
        }
        submitLock=true;
        RequestParams params = new RequestParams();
       params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(this));
//        params.addBodyParameter("single_code",Perference.getUserRandCode(this) );
        params.addBodyParameter("uuid", SPUtil.getUserUUID(this));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(this));
        params.addBodyParameter("action", AddressUtils.code_action);
        params.addBodyParameter("method", AddressUtils.rev_method);
        params.addBodyParameter("phone", userEdit.getText().toString());
        params.addBodyParameter("name", SPUtil.getUserName(this).isEmpty()?userEdit.getText().toString():SPUtil.getUserName(this));
        params.addBodyParameter("password", MDUtil.MD5(passEdit.getText().toString()));
       x.http().post(params, new Callback.CommonCallback<String>() {
           @Override
           public void onSuccess(String result) {
               dismissProgressDialog();
               submitLock = false;
               try {
                   JSONObject jsonObject=new JSONObject(result);
                   if(jsonObject!=null){
                       boolean success=jsonObject.optBoolean("success");
                       if(success){
                           LogUtil.e("修改密码成功！！！");
                           MyToast.showShort(ForgetPasswordActivity.this,"密码修改成功");
                           finish();
                       }else{
                           MyToast.showShort(ForgetPasswordActivity.this,jsonObject.optString("feedback"));
                           LogUtil.e("失败==="+result);
                       }
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onError(Throwable ex, boolean isOnCallback) {

           }

           @Override
           public void onCancelled(CancelledException cex) {

           }

           @Override
           public void onFinished() {

           }
       });

    }

}

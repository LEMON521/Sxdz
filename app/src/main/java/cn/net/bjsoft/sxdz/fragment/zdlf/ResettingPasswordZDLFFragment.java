package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.welcome.SplashActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MD5;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by Zrzc on 2017/4/11.
 */

@ContentView(R.layout.fragment_mine_zdlf_reset_password)
public class ResettingPasswordZDLFFragment extends BaseFragment {
    private InputMethodManager imm;

    @ViewInject(R.id.title_back)
    private ImageView back;//返回
    @ViewInject(R.id.title_title)
    private TextView title;//标题


    @ViewInject(R.id.mine_zdlf_reset_password_old)
    private EditText password_old;
    @ViewInject(R.id.mine_zdlf_reset_password_new)
    private EditText password_new;
    @ViewInject(R.id.mine_zdlf_reset_password_confirm)
    private EditText password_confirm;

    private String oldStr = "";
    private String newStr = "";
    private String confirmStr = "";

    @Override
    public void initData() {
        imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        back.setVisibility(View.VISIBLE);
        title.setText("修改密码");
    }


    @Event(value = {R.id.title_back, R.id.mine_zdlf_reset_password_submit})
    private void uploadOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
            case R.id.mine_zdlf_reset_password_submit:
                submit();
                break;
        }
    }

    /**
     * 获取输入框中的内容，返回长度与传进来的List的长度成正相关
     *
     * @return
     */
    public void submit() {

        oldStr = password_old.getText().toString().trim();
        newStr = password_new.getText().toString().trim();
        confirmStr = password_confirm.getText().toString().trim();


        if (oldStr.equals("")) {
            MyToast.showShort(mActivity, "原始密码不能为空");
            return;
        } else if (newStr.equals("")) {
            MyToast.showShort(mActivity, "新密码不能为空！");
            return;
        } else if (confirmStr.equals("")) {
            MyToast.showShort(mActivity, "确认新密码不能为空！");
            return;
        } else if (!newStr.equals(confirmStr)) {

            MyToast.showShort(mActivity, "两次输入的密码不一致！");
            password_confirm.setText("");
            return;
        } else {
            setUpdatePWD();
        }


    }

    public void setUpdatePWD() {
        final RequestParams params = new RequestParams(SPUtil.getResetPasswordUrl(mActivity));
        params.addBodyParameter("username", SPUtil.getLoginUserName(mActivity));
        params.addBodyParameter("password", MD5.getMessageDigest(oldStr.getBytes()));
        params.addBodyParameter("new_password", MD5.getMessageDigest(newStr.getBytes()));

        //LogUtil.e("params"+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("修改密码结果为！！！！======" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {
                        //SPUtil.setUserUUID(mActivity, jsonObject.optString("uuid", ""));
                        MyToast.showShort(getActivity(), "修改密码成功！请重新登录!");
                        SPUtil.setToken(mActivity, "");
                        Intent i = new Intent(getActivity(), SplashActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        MyToast.showShort(getActivity(), jsonObject.optString("msg", "密码修改失败"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("失败原因为！！！！======" + ex);
                MyToast.showShort(getActivity(), "修改失败，网络超时");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //imm.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
    }
}

package cn.net.bjsoft.sxdz.fragment.ylyd;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MD5;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.UrlUtil;

import static android.view.View.GONE;


/**
 * Homepage   ---- 报表页面
 * Created by zkagang on 2016/9/13.
 */
@ContentView(R.layout.window_entry_1)
public class YLYDResettingPasswordFragment extends BaseFragment {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title_title;

    @ViewInject(R.id.window_title)
    private TextView title;

    //栏目1
    @ViewInject(R.id.window_ll_entry_1)
    private LinearLayout entry_1;
    @ViewInject(R.id.window_tv_1)
    private TextView tv_1;
    @ViewInject(R.id.window_et_1)
    private EditText et_1;
    //栏目2
    @ViewInject(R.id.window_ll_entry_2)
    private LinearLayout entry_2;
    @ViewInject(R.id.window_tv_2)
    private TextView tv_2;
    @ViewInject(R.id.window_et_2)
    private EditText et_2;
    //栏目3
    @ViewInject(R.id.window_ll_entry_3)
    private LinearLayout entry_3;
    @ViewInject(R.id.window_tv_3)
    private TextView tv_3;
    @ViewInject(R.id.window_et_3)
    private EditText et_3;
    //栏目4
    @ViewInject(R.id.window_ll_entry_4)
    private LinearLayout entry_4;
    @ViewInject(R.id.window_ll_entry_4)
    private TextView tv_4;
    @ViewInject(R.id.window_ll_entry_4)
    private EditText et_4;

    @ViewInject(R.id.window_confirm)
    private TextView confirm;
    @ViewInject(R.id.window_cancle)
    private TextView cancle;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password = "";

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    private String oldPassword = "";

    @Override
    public void initData() {

        back.setVisibility(View.VISIBLE);
        title_title.setText("修改密码");

        title.setText("修改密码");
        tv_1.setText("原始密码");
        tv_2.setText("新 密 码");
        tv_3.setText("确认密码");
//        et_1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        et_2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        et_3.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_1.setHint("请输入原始密码");
        et_2.setHint("请输入新密码");
        et_3.setHint("请确认新密码");
        entry_4.setVisibility(GONE);
    }


    @Event(value = {R.id.title_back,R.id.window_confirm,R.id.window_cancle})
    private void uploadOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
            case R.id.window_confirm:
                submit();
                break;
            case R.id.window_cancle:
                getActivity().finish();
                break;
        }
    }

    /**
     * 获取输入框中的内容，返回长度与传进来的List的长度成正相关
     *
     * @return
     */
    public void submit() {

        String result_1 = et_1.getText().toString().trim();
        String result_2 = et_2.getText().toString().trim();
        String result_3 = et_3.getText().toString().trim();


        if (result_1.equals("")) {
            MyToast.showShort(mActivity, "原始密码不能为空");
            return;
        } else if (result_2.equals("")) {
            MyToast.showShort(mActivity, "新密码不能为空！");
            return;
        } else if (result_3.equals("")) {
            MyToast.showShort(mActivity,  "确认新密码不能为空！");
            return;
        } else if (!result_2.equals(result_3)) {

            MyToast.showShort(mActivity, "两次输入的密码不一致！");
            return;
        } else {
            setPassword(result_3);
            setOldPassword(result_1);

            setUpdatePWD();
        }


    }
    public void setUpdatePWD(){
        final RequestParams params = new RequestParams(UrlUtil.baseUrl);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("randcode", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));
        params.addBodyParameter("action", "submit");


        params.addBodyParameter("method", "edit_password");
        params.addBodyParameter("old_password", MD5.getMessageDigest(getOldPassword().getBytes()));
        params.addBodyParameter("password", MD5.getMessageDigest(getPassword().getBytes()));

        //LogUtil.e("params"+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("修改密码结果为！！！！======" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.optBoolean("success", false);
                    if (!success) {
                        MyToast.showShort(getActivity(), jsonObject.optString("feedback", "密码修改失败"));
                    } else {
                        SPUtil.setUserUUID(mActivity,jsonObject.optString("uuid", ""));
                        MyToast.showShort(getActivity(), "修改密码成功！");
                        mActivity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

            }
        });
    }
    /**
     * 将输入框置为空
     */
    public void clear() {
        et_1.setText("");
        et_2.setText("");
        et_3.setText("");
        et_4.setText("");
    }
}

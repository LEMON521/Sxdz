package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/2/21.
 */

public class WindowRecettingPasswordView extends LinearLayout /*implements View.OnClickListener*/ {

    private TextView title;

    //栏目1
    private LinearLayout entry_1;
    private TextView tv_1;
    private EditText et_1;
    //栏目2
    private LinearLayout entry_2;
    private TextView tv_2;
    private EditText et_2;
    //栏目3
    private LinearLayout entry_3;
    private TextView tv_3;
    private EditText et_3;
    //栏目4
    private LinearLayout entry_4;
    private TextView tv_4;
    private EditText et_4;

    private TextView confirm;
    private TextView cancle;

    public TextView getConfirm() {
        return confirm;
    }

    public void setConfirm(TextView confirm) {
        this.confirm = confirm;
    }

    public TextView getCancle() {
        return cancle;
    }

    public void setCancle(TextView cancle) {
        this.cancle = cancle;
    }

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
    private Context context;

    /**
     * 初始化必备参数
     *
     * @param context
     */
    public WindowRecettingPasswordView(Context context) {
        super(context);

        this.context = context;
        initView(context);

    }


    private void initView(Context context) {
        View.inflate(context, R.layout.window_entry, WindowRecettingPasswordView.this);
        title = (TextView) findViewById(R.id.window_title);
        entry_1 = (LinearLayout) findViewById(R.id.window_ll_entry_1);
        tv_1 = (TextView) findViewById(R.id.window_tv_1);
        et_1 = (EditText) findViewById(R.id.window_et_1);
        entry_2 = (LinearLayout) findViewById(R.id.window_ll_entry_2);
        tv_2 = (TextView) findViewById(R.id.window_tv_2);
        et_2 = (EditText) findViewById(R.id.window_et_2);
        entry_3 = (LinearLayout) findViewById(R.id.window_ll_entry_3);
        tv_3 = (TextView) findViewById(R.id.window_tv_3);
        et_3 = (EditText) findViewById(R.id.window_et_3);
        entry_4 = (LinearLayout) findViewById(R.id.window_ll_entry_4);
        tv_4 = (TextView) findViewById(R.id.window_tv_4);
        et_4 = (EditText) findViewById(R.id.window_et_4);
        confirm = (TextView) findViewById(R.id.window_confirm);
        cancle = (TextView) findViewById(R.id.window_cancle);

        title.setText("修改密码:");
        tv_1.setText("原始密码:");
        tv_2.setText("新 密 码:");
        tv_3.setText("确认密码:");
//        et_1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        et_2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        et_3.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_1.setHint("请输入原始密码");
        et_2.setHint("请输入新密码");
        et_3.setHint("请确认新密码");
        entry_4.setVisibility(GONE);

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
            MyToast.showShort(context, "原始密码不能为空");
            return;
        } else if (result_2.equals("")) {
            MyToast.showShort(context, "新密码不能为空！");
            return;
        } else if (result_3.equals("")) {
            MyToast.showShort(context,  "确认新密码不能为空！");
            return;
        } else if (!result_2.equals(result_3)) {

            MyToast.showShort(context, "两次输入的密码不一致！");
            return;
        } else {
            setPassword(result_3);
            setOldPassword(result_1);
        }


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

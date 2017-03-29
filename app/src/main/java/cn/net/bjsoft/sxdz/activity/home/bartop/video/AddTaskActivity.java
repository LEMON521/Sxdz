package cn.net.bjsoft.sxdz.activity.home.bartop.video;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

@ContentView(R.layout.activity_add_task)
public class AddTaskActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    @ViewInject(R.id.addtask_name)
    EditText name;
    @ViewInject(R.id.radioGroup)
    RadioGroup radioGroup;
    @ViewInject(R.id.addtask_btn)
    TextView btn;
    @ViewInject(R.id.addtask_back)
    ImageView back;

    private int RESULT_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioGroup.setOnCheckedChangeListener(this);
        btn.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.pic:
                RESULT_TAG = 1;
                break;
            case R.id.video:
                RESULT_TAG = 2;
                break;
            case R.id.audio:
                RESULT_TAG = 3;
                break;
        }
    }
//
//    @Event(value = {R.id.btn,R.id.addtask_back})
//    public void addTaskOnClick(View view) {
//        switch (view.getId()){
//            case R.id.addtask_back:
//                finish();
//                break;
//            case R.id.btn:
//                if(name.getText().toString().equals("")){
//                    MyToast.showShort(this,"请输入名称");
//                }else{
//                    upload();
//                }
//                break;
//        }
//    }

    /**
     * 提交
     */
    public void upload() {
        showProgressDialog();
        RequestParams params = new RequestParams("http://api.shuxin.net/Service/JsonData.aspx?");
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(this));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(this));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(this));
        params.addBodyParameter("action", "create");
        params.addBodyParameter("status", "1");
        params.addBodyParameter("js_mvc_name", "tab_drm_shoot");
        params.addBodyParameter("type", RESULT_TAG+"");
        params.addBodyParameter("name", name.getText().toString());
        params.addBodyParameter("user_id", SPUtil.getUserId(this));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("tag",result);
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean success = jsonObject.optBoolean("success");
                        if (success) {
                            setResult(102);
                            finish();
                        } else {
                            MyToast.showShort(AddTaskActivity.this, jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(AddTaskActivity.this, "网络连接错误");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addtask_back:
                finish();
                break;
            case R.id.addtask_btn:
                if(name.getText().toString().equals("")){
                    MyToast.showShort(this,"请输入名称");
                }else{
                    upload();
                }
                break;
        }
    }
}

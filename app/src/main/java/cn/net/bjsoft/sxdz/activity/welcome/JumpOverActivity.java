package cn.net.bjsoft.sxdz.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

@ContentView(R.layout.activity_jump_over)
public class JumpOverActivity extends BaseActivity {


    @ViewInject(R.id.img)
    ImageView img;
    @ViewInject(R.id.jump)
    TextView jump;

    private String json;

    private DatasBean datasBean;

    private int seconds;
    private boolean tag = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (seconds > 0) {
                seconds--;
                jump.setText(seconds + "s 跳过");
            } else if (seconds == 0) {
                seconds = -1;
                tag = false;

                if (!datasBean.data.authentication) {
                    Intent i3 = new Intent(JumpOverActivity.this, MainActivity.class);

                    i3.putExtra("json", json);
                    finish();
                    startActivity(i3);
                } else {
                    if (datasBean.data.user.logined) {
                        Intent intent = new Intent(JumpOverActivity.this, MainActivity.class);
                        //将返回的json传递过去，在下一个页面将必要的参数本地化
                        intent.putExtra("json", json);
                        // LogUtil.e("datasBean.data.loaders.size()" +datasBean.data.loaders.size());
                        finish();
                        startActivity(intent);
                    } else {
                        Intent i = new Intent(JumpOverActivity.this, LoginActivity.class);
                        i.putExtra("json", json);
                        finish();
                        startActivity(i);
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtil.e("JumpOverActivity::onCreat");

        json = getIntent().getStringExtra("json");
        // LogUtil.e("获取到的json"+json);
        datasBean = GsonUtil.getDatasBean(json);

        //seconds=initModel.getImgStr().get(0).getSeconds();
        //seconds = 3;//几秒之后跳过---以前是服务端传递过来的参数，现在取消了
        seconds = datasBean.data.loaders.size();

        img = (ImageView) findViewById(R.id.img);
        jump = (TextView) findViewById(R.id.jump);
        x.image().bind(img, datasBean.data.loaders.get(0).imgurl);
        jump.setText(seconds + "s 跳过");

        new Thread() {
            @Override
            public void run() {
                while (tag) {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        }.start();

    }

    @Event(type = View.OnClickListener.class, value = R.id.jump)
    public void onClick() {
        if (tag == true) {
            tag = false;
            if (!datasBean.data.authentication) {
                Intent i3 = new Intent(this, MainActivity.class);

                i3.putExtra("json", json);
                finish();
                startActivity(i3);
            } else if (datasBean.data.user.logined) {
                Intent i3 = new Intent(this, MainActivity.class);
                i3.putExtra("json", json);
                finish();
                startActivity(i3);
            } else if (!datasBean.data.authentication) {
                Intent i3 = new Intent(this, MainActivity.class);
                i3.putExtra("json", json);
                finish();
                startActivity(i3);
            } else if (!datasBean.data.user.logined && datasBean.data.authentication) {
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("json", json);
                //i.putExtra("zhuce",initModel.getLogin().getRegister_url());--注册的url已经取消
                finish();
                startActivity(i);


            } else {
                Intent i3 = new Intent(this, MainActivity.class);
                i3.putExtra("json", json);
                finish();
                startActivity(i3);

            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("JumpOverActivity==" + "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("JumpOverActivity==onDestroy");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("JumpOverActivity==onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        LogUtil.e("JumpOverActivity==onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("JumpOverActivity==onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("JumpOverActivity==onStop");
    }


}

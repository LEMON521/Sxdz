package cn.net.bjsoft.sxdz.activity.home.bartop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.dialog.ALiPushMessageInAppPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.receiver.ALiPushType3Receiver;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.function.InitFragmentUtil;

/**
 * 功能页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_function)
public class FunctionActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView back;
    @ViewInject(R.id.title_title)
    private TextView title;


    @ViewInject(R.id.function_content)
    private FrameLayout content;

    @ViewInject(R.id.function_bar)
    private LinearLayout bar;

    //扫一扫
    @ViewInject(R.id.function_scan)
    private RelativeLayout scan;
    @ViewInject(R.id.function_scan_icon)
    private ImageView scan_icon;
    @ViewInject(R.id.function_scan_text)
    private TextView scan_text;
    @ViewInject(R.id.function_scan_img)
    private ImageView scan_img;

    //拍照
    @ViewInject(R.id.funciton_photo)
    private RelativeLayout photo;
    @ViewInject(R.id.function_photo_icon)
    private ImageView photo_icon;
    @ViewInject(R.id.function_photo_text)
    private TextView photo_text;
    @ViewInject(R.id.function_photo_num)
    private TextView photo_num;
    @ViewInject(R.id.function_photo_img)
    private ImageView photo_img;

    //签到
    @ViewInject(R.id.function_sign)
    private RelativeLayout sign;
    @ViewInject(R.id.function_sign_icon)
    private ImageView sign_icon;
    @ViewInject(R.id.function_sign_text)
    private TextView sign_text;
    @ViewInject(R.id.function_sign_num)
    private TextView sign_num;
    @ViewInject(R.id.function_sign_img)
    private ImageView sign_img;

    //支付
    @ViewInject(R.id.function_pay)
    private RelativeLayout pay;
    @ViewInject(R.id.function_pay_icon)
    private ImageView pay_icon;
    @ViewInject(R.id.function_pay_text)
    private TextView pay_text;
    @ViewInject(R.id.function_pay_num)
    private TextView pay_num;
    @ViewInject(R.id.function_pay_img)
    private ImageView pay_img;


    private ArrayList<BaseFragment> mFragmentsList;
    private ArrayList<RelativeLayout> mBarList;
    private String mJson = "";
    private AppBean appBean;


    private HashMap<String, Integer> pushNum;
    private int mScan, mShoot, mSignin, mPayment;
    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();

    private ALiPushType3Receiver aLiPushType3Receiver = new ALiPushType3Receiver();

    private ALiPushMessageInAppPopupWindow showPushWindow;

    private String openTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentsList = new ArrayList<BaseFragment>();
        mBarList = new ArrayList<>();
        mJson = getIntent().getStringExtra("json");
        openTag = getIntent().getStringExtra("opentag");
        LogUtil.e("function接收到的opentag为=====" + openTag);
        appBean = GsonUtil.getAppBean(mJson);

        back.setVisibility(View.VISIBLE);

        initData();
        setView();
    }

    //TODO 因为Activity每次执行，不管是在前台后台，可见不可见，onStart是必经之路，所以将推送的数据在这里显示最合理
    @Override
    protected void onStart() {

        super.onStart();

        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.function"));

        registerReceiver(aLiPushType3Receiver, new IntentFilter("cn.net.bjsoft.sxdz.alipush.notify_type_3"));

        aLiPushType3Receiver.setOnData(new ALiPushType3Receiver.OnGetData() {
            @Override
            public void onDataCallBack(Bundle bundleData) {
                LogUtil.e("推送通知拿到数据==============" + bundleData);
                if (bundleData != null) {

                    showPushWindow = new ALiPushMessageInAppPopupWindow(FunctionActivity.this, bundleData, bar);

                    showPushWindow.showWindow();
                }
            }
        });
        setPushNumber();
    }

    public void setPushNumber() {
        int scanNum = SPJpushUtil.getScan(this);
        int shootNum = SPJpushUtil.getShoot(this);
        int signinNum = SPJpushUtil.getSignin(this);
        int paymentNum = SPJpushUtil.getPayment(this);
        //没有扫一扫

        //建议
        if (shootNum > 0) {
            photo_num.setText(shootNum + "");
            photo_num.setVisibility(View.VISIBLE);
        } else {
            photo_num.setVisibility(View.INVISIBLE);
        }
        //解惑报错
        if (signinNum > 0) {
            sign_num.setText(signinNum + "");
            sign_num.setVisibility(View.VISIBLE);
        } else {
            sign_num.setVisibility(View.INVISIBLE);
        }
        //帮助
        if (paymentNum > 0) {
            pay_num.setText(paymentNum + "");
            pay_num.setVisibility(View.VISIBLE);
        } else {
            pay_num.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mFragmentsList = InitFragmentUtil.getFunctionFragments(mJson);
    }

    /**
     * 显示数据
     */
    private void setView() {

        setFunctionVisibility();
        //如果没有发送消息 的任务，那么就让他显示默认的
        if (openTag.equals("") || openTag.equals("defult")) {
            int seclect = mFragmentsList.size() / 2 + mFragmentsList.size() % 2 - 1;
//            onClick(mBarList.get(seclect));
            onClick(mBarList.get(0));
        } else {
            //onClick(mBarList.get(openTag));
            for (int i = 0; i < mFragmentsList.size(); i++) {
//            MyToast.showShort(this,"是否为：："+(v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())));
                //字符串判定   一定要用equals！！！
                if (openTag.equals(mFragmentsList.get(i).getArguments().get("tag").toString())) {
                    onClick(mBarList.get(i));
                }
            }
        }
    }

    /**
     * 设置Community按钮的显示与否
     */
    private void setFunctionVisibility() {
        {
            scan.setVisibility(View.GONE);
            scan_img.setVisibility(View.GONE);

            photo.setVisibility(View.GONE);
            photo_img.setVisibility(View.GONE);

            sign.setVisibility(View.GONE);
            sign_img.setVisibility(View.GONE);

            pay.setVisibility(View.GONE);
            pay_img.setVisibility(View.GONE);

        }
        for (int i = 0; i < mFragmentsList.size(); i++) {

            if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((scan.getTag().toString()))) {
                scan.setVisibility(View.VISIBLE);
                scan_img.setVisibility(View.VISIBLE);
                mBarList.add(scan);
            } else if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((photo.getTag().toString()))) {
                photo.setVisibility(View.VISIBLE);
                photo_img.setVisibility(View.VISIBLE);
                mBarList.add(photo);
            } else if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((sign.getTag().toString()))) {
                sign.setVisibility(View.VISIBLE);
                sign_img.setVisibility(View.VISIBLE);
                mBarList.add(sign);
            } else if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals((pay.getTag().toString()))) {
                pay.setVisibility(View.VISIBLE);
                pay_img.setVisibility(View.VISIBLE);
                mBarList.add(pay);
            }
        }

        //如果只有一个页面，那么让底部栏隐藏掉
        if (mFragmentsList.size() == 1) {
            bar.setVisibility(View.GONE);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 点击事件
     *
     * @param v
     */
    @Event(type = View.OnClickListener.class, value = {R.id.function_scan, R.id.funciton_photo, R.id.function_sign, R.id.function_pay})
    private void onClick(View v) {

        for (int i = 0; i < mFragmentsList.size(); i++) {
//            MyToast.showShort(this,"是否为：："+(v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())));
            //字符串判定   一定要用equals！！！
            if (v.getTag().toString().equals(mFragmentsList.get(i).getArguments().get("tag").toString())) {

                if ((mFragmentsList.get(i).getArguments().get("tag").toString()).equals("scaning")) {
                    PackageManager pm = getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                        return;
                    }
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.function_content, mFragmentsList.get(i), "FUNCTION")
                        .commit();
            }
        }

        //MyToast.showShort(context, "点击了" + v.toString());
        setDefault();
        switch (v.getId()) {

            case R.id.function_scan:
                title.setText("扫一扫");
                scan_icon.setImageResource(R.drawable.saoyisao_s);
                scan_text.setTextColor(getResources().getColor(R.color.blue));
                break;
            case R.id.funciton_photo:
                title.setText("拍照");
                photo_icon.setImageResource(R.drawable.paizhao_s);
                photo_text.setTextColor(getResources().getColor(R.color.blue));
                photo_num.setText("");
                photo_num.setVisibility(View.INVISIBLE);
                break;
            case R.id.function_sign:
                title.setText("签到");
                sign_icon.setImageResource(R.drawable.qiandao_s);
                sign_text.setTextColor(getResources().getColor(R.color.blue));
                sign_num.setText("");
                sign_num.setVisibility(View.INVISIBLE);
                break;
            case R.id.function_pay:
                title.setText("支付");
                pay_icon.setImageResource(R.drawable.pay_s);
                pay_text.setTextColor(getResources().getColor(R.color.blue));
                pay_num.setText("");
                pay_num.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Event(value = {R.id.title_back})
    private void click(View view) {
        switch (view.getId()) {

            case R.id.title_back:
                finish();
                break;
        }
    }

    /**
     * 设置底部未选中的图片为默认颜色
     */
    private void setDefault() {
        scan_icon.setImageResource(R.drawable.saoyisao_n);
        scan_text.setTextColor(getResources().getColor(R.color.gray));

        photo_icon.setImageResource(R.drawable.paizhao_n);
        photo_text.setTextColor(getResources().getColor(R.color.gray));

        sign_icon.setImageResource(R.drawable.qiandao_n);
        sign_text.setTextColor(getResources().getColor(R.color.gray));

        pay_icon.setImageResource(R.drawable.pay_n);
        pay_text.setTextColor(getResources().getColor(R.color.gray));
    }


    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {
        /**
         * 接收广播
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            setPushNumber();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(aLiPushType3Receiver);
    }
}

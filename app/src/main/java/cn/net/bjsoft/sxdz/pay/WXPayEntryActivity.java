package cn.net.bjsoft.sxdz.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.app.MyApplication;
import cn.net.bjsoft.sxdz.pay.wxpay.Constants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    private MyApplication application;
    public static String orderNo;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        application = (MyApplication) getApplication();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            if (resp.errCode == 0)
            {

                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (resp.errCode == -2)
            {
                Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
            }
            else if (resp.errCode == -3)
            {
                Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "支付失败,errCode:" + resp.errCode, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
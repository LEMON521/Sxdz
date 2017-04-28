package cn.net.bjsoft.sxdz.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.mutils.utils.Log;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.net.bjsoft.sxdz.pay.alipay.SignUtils;
import cn.net.bjsoft.sxdz.pay.wxpay.Constants;
import cn.net.bjsoft.sxdz.pay.wxpay.MD5;
import cn.net.bjsoft.sxdz.pay.wxpay.Util;

/**
 * @author 作者: tongxiaoyun
 * @version 创建时间：2016年3月2日 上午11:27:15
 * @类说明 支付工具类
 */
public class PayUtils
{
    static PayUtils payUtils;
    static Context context;

    @SuppressWarnings("static-access")
    public static PayUtils newInstance(Context context)
    {
        if (payUtils == null)
        {
            payUtils = new PayUtils();
        }
        payUtils.context = context;
        return payUtils;
    }

    /**
     * 合作者ID
     */
    public static final String Alipay_PARTNER = "2088121864939898";
    /**
     * 官方网站
     */
    public static final String Alipay_SELLER = "zhangran@vane.travel";
    /**
     * 支付宝私钥
     */
    public static final String Alipay_RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALEaG+huRAcITZu21T3kU5Yt0IkkoubXDdELEDx7UeciVwLRVibT83CgmGkwjN6ysbcLV8PFLAq0m2FHu/nbc9p2CvmCS7UHTOjuxCD8zXaUElutMtjRBnGpJ3gmZtJf1RHX6C2FxQ1TlKJ8CioQZw5wUNKqP3J2YYXppaW/v8FJAgMBAAECgYBQLNa3ibuoR10DAmlVJJmmokUG4RwSTwdFPLHz/4wbjBNCBkzvNQK/WSTGciFD7DfB+xOXYBrJGexBxPzXZz7xxaMaJFDXtHy/JH6gN6n+XLsk2IW+sDWyfO+BNHrB2WsNNzgFDPrYx7kASWhy3v4dqG/tL7QMIesfdWjcSNjvAQJBANr68J5GAh+WViaj+OoxH7FAQRMvhGz1XSW29LYG2OnZ5Fp5Bm517EdkzenJEhg8E/A+oodZ4MDFgVg6fe/PAeECQQDPCsGHdJnp/u9527EMVf/VyYoYAqnW88woX7856XM3c3Zu60Es1F1HS0/ppXeLO5U0g7m9HwT2ByNKJxraz3xpAkEAtV0Wwo+LHKw/yBsQgq6S6BQmkYROcou5TQ49mTEMoIo1hfUsrXn/apYDsTg2Q1omzVii9eUrr50k5nTzbxWNIQJATh0KwyZU45nRlDI07CiOK4SsWz62AWld/2WQajMPyz3+0v5jSpyczMc3UTmijp9ePb/tFdNcNc6fm9yBH0jk8QJAHWX7bvjjaNDo7tVN16/H+vsKki3nNc8cjSauWs/wXjuSaGJmEmjzdKxkmuGlQFrOZFRY2NjMdDWQpYWnaB+JCA==";
    private static final int SDK_PAY_FLAG = 1;

    /**
     * call alipay sdk pay. 调用SDK支付
     * 
     */
    public void alipayPay(String orderId, String orderContent, String orderPrice, final Handler mHandler)
    {
        String orderInfo = getOrderInfo(orderId, orderContent, orderPrice);
        String sign = sign(orderInfo);
        try
        {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable()
        {

            @Override
            public void run()
            {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口
                String result = alipay.pay(payInfo,true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     * 
     */
    public String getOrderInfo(String subject, String body, String price)
    {
        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + Alipay_PARTNER + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Alipay_SELLER + "\"";

        subject.replace(":", "");
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + subject + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://weien.com01.org/notify_url.aspx" + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     * 
     * @param content
     *            待签名订单信息
     */
    public String sign(String content)
    {
        return SignUtils.sign(content, Alipay_RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     * 
     */
    public String getSignType()
    {
        return "sign_type=\"RSA\"";
    }

    /************************************** 微信支付 *************************************************************************/

    private String price; // 价格
    private String orderID; // 订单号
    private String subject; // 描述
    private PayReq req;
    private StringBuffer sb;
    private Map<String, String> resultunifiedorder;
    private IWXAPI msgApi;

    public void wxPay(String price, String orderID, String subject, String prepay_id)
    {
        this.price = price;
        this.orderID = orderID;
        this.subject = subject;
        req = new PayReq();
        sb = new StringBuffer();
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        if (!msgApi.isWXAppInstalled())
        {
            Toast.makeText(context, "对不起您还没安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        msgApi.registerApp(Constants.APP_ID);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        sb.append("prepay_id\n" + prepay_id + "\n\n");
        resultunifiedorder = new HashMap<String, String>();
        resultunifiedorder.put("prepay_id", prepay_id);
        genPayReq();
        sendPayReq();
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++)
        {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++)
        {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        // this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++)
        {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>>
    {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show(context, "提示", "正在获取预支付订单...");
        }

        @Override
        protected void onPostExecute(Map<String, String> result)
        {
            if (dialog != null)
            {
                dialog.dismiss();
            }

        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params)
        {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion", entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion", content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    public Map<String, String> decodeXml(String content)
    {

        try
        {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT)
            {

                String nodeName = parser.getName();
                switch (event)
                {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:

                    if ("xml".equals(nodeName) == false)
                    {
                        // 实例化student对象
                        xml.put(nodeName, parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                }
                event = parser.next();
            }

            return xml;
        }
        catch (Exception e)
        {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private String genNonceStr()
    {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp()
    {
        return System.currentTimeMillis() / 1000;
    }

    //
    private String genProductArgs()
    {
        StringBuffer xml = new StringBuffer();

        try
        {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            // packageParams.add(new BasicNameValuePair("body", subject));// 商品描述 字符串类123字节以下
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://huanqiu.com01.net:8999/wxpay_back_url.do"));
            packageParams.add(new BasicNameValuePair("out_trade_no", orderID.replace("-", "")));// 订单流水号
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(Double.parseDouble(price) * 1000))); // 价格
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return xmlstring;

        }
        catch (Exception e)
        {
            Log.e("======wxpay=======", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

    private void genPayReq()
    {
        // { "appid":"wxb4ba3c02aa476ea1", "noncestr":"870e542891110d3ebf4db175a87a9cf0", "package":"Sign=WXPay", "partnerid":"10000100",
        // "prepayid":"wx2016031517495376983eca0f0650150707", "timestamp":"1458035393", "sign":"BEBC64CD5056B89615ED229DF3D8E625" }

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        // req.prepayId = "wx2016031517495376983eca0f0650150707";
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        Log.e("orion = " + signParams.toString());
        Log.e("appId = " + req.appId);
        Log.e("partnerId = " + req.partnerId);
        Log.e("prepayId = " + req.prepayId);
        Log.e("packageValue = " + req.packageValue);
        Log.e("nonceStr = " + req.nonceStr);
        Log.e("timeStamp = " + req.timeStamp);
        Log.e("sign = " + req.sign);

        // req.appId = "wxb4ba3c02aa476ea1";
        // req.partnerId = "10000100";
        // req.packageValue = "Sign=WXPay";
        // req.extData = "app data"; // optional
        //
        // req.prepayId = "wx20160315194053127d072d0a0474281953";
        // req.nonceStr = "af77dc32b4b0d740acc7b6ec1b983eb7";
        // req.timeStamp = "1458042053";
        // req.sign = "D9B37D0A83B19C0EB67ED99C1A317C0B";

    }

    private void sendPayReq()
    {
        msgApi.registerApp(Constants.APP_ID);
        // msgApi.registerApp("wxb4ba3c02aa476ea1");
        msgApi.sendReq(req);
    }

    public void wxPay(String prepayId)
    {
        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        if (api.registerApp(Constants.APP_ID))
        {
            // Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
            PayReq request = new PayReq();
            request.appId = Constants.APP_ID;
            request.partnerId = Constants.MCH_ID;
            request.prepayId = prepayId;
            request.packageValue = "Sign=WXPay";
            request.nonceStr = genNonceStr();
            request.timeStamp = String.valueOf(genTimeStamp());

            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", request.appId));
            signParams.add(new BasicNameValuePair("noncestr", request.nonceStr));
            signParams.add(new BasicNameValuePair("package", request.packageValue));
            signParams.add(new BasicNameValuePair("partnerid", request.partnerId));
            signParams.add(new BasicNameValuePair("prepayid", request.prepayId));
            signParams.add(new BasicNameValuePair("timestamp", request.timeStamp));
            request.sign = genAppSign(signParams);

            Log.e("appId = " + request.appId);
            Log.e("partnerId = " + request.partnerId);
            Log.e("prepayId = " + request.prepayId);
            Log.e("packageValue = " + request.packageValue);
            Log.e("nonceStr = " + request.nonceStr);
            Log.e("timeStamp = " + request.timeStamp);
            Log.e("sign = " + request.sign);

            api.sendReq(request);
            // Toast.makeText(context, "正在调用", Toast.LENGTH_SHORT).show();
        }
    }
}

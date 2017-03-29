package cn.net.bjsoft.sxdz.fragment.bartop.function;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.welcome.LinkToActivity;
import cn.net.bjsoft.sxdz.adapter.PayExpandableListAdapter;
import cn.net.bjsoft.sxdz.bean.PayTaskBean;
import cn.net.bjsoft.sxdz.bean.PayTaskItemBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.pay.PayVUtils;
import cn.net.bjsoft.sxdz.pay.alipay.Result;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.HttpPostUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 支付页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.fragment_pay)
public class TopPayFragment extends BaseFragment implements View.OnClickListener {


    @ViewInject(R.id.pay_btn)
    TextView payBtn;
    @ViewInject(R.id.expandlistview)
    ExpandableListView expandlistview;
    @ViewInject(R.id.list)
    RelativeLayout list;
    @ViewInject(R.id.pay_back)
    ImageView back;

    private AppProgressDialog progressDialog;

    private View view;

    private PayExpandableListAdapter padapter;

    private ArrayList<PayTaskBean> payTaskBeanArrayList = new ArrayList<PayTaskBean>();

    private LinearLayout pay, weixinpay, alipay, yinlianpay;
    private ImageView weixin_btn, alipay_btn, back_btn;

    private int pay_style = 1;

    private SimpleDateFormat sdf;

    private double money_all = 0;

    private String order_number="";

    @Override
    public void initData() {
        /**
         * 注册广播
         */
        getActivity().registerReceiver(receiver, new IntentFilter("com.shuxin.payfragment"));

        sdf = new SimpleDateFormat("yyyyMMddhhmmss");


        payBtn.setOnClickListener(this);
        padapter = new PayExpandableListAdapter(getActivity(), payTaskBeanArrayList, this);
        pay = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.pop_paylayout, null);
        weixin_btn = (ImageView) pay.findViewById(R.id.weixin_btn);
        alipay_btn = (ImageView) pay.findViewById(R.id.alipay_btn);
        back_btn = (ImageView) pay.findViewById(R.id.back_btn);
        weixinpay = (LinearLayout) pay.findViewById(R.id.weixinpay);
        alipay = (LinearLayout) pay.findViewById(R.id.alipay);
        yinlianpay = (LinearLayout) pay.findViewById(R.id.yinlianpay);
        weixinpay.setOnClickListener(this);
        alipay.setOnClickListener(this);
        yinlianpay.setOnClickListener(this);
        back.setOnClickListener(this);
        expandlistview.addFooterView(pay, null, false);
        expandlistview.setAdapter(padapter);
        setINVISIBLE();
        getData();
    }

    public void getData() {
        showProgressDialog();
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("method", "load_order_data");
        params.addBodyParameter("moduleid", "drm");
        params.addBodyParameter("action", "load");
        params.addBodyParameter("status", "1");
        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));
        HttpPostUtil.getInstance().send(HttpRequest.HttpMethod.POST, AddressUtils.httpurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("tag", "video" + responseInfo.result);
                payTaskBeanArrayList.clear();
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject != null) {
                        boolean tag = jsonObject.optBoolean("success");
                        if (tag) {
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PayTaskBean p = new PayTaskBean();
                                    p.setMoney(jsonArray.optJSONObject(i).optString("money"));
                                    p.setId(jsonArray.optJSONObject(i).optString("id"));
                                    p.setName(jsonArray.optJSONObject(i).optString("name"));
                                    p.setNumber(jsonArray.optJSONObject(i).optString("orderno"));
                                    ArrayList<PayTaskItemBean> plist = new ArrayList<PayTaskItemBean>();
                                    if (jsonArray.optJSONObject(i).optJSONArray("children") != null && jsonArray.optJSONObject(i).optJSONArray("children").length() > 0) {
                                        for (int k = 0; k < jsonArray.optJSONObject(i).optJSONArray("children").length(); k++) {
                                            PayTaskItemBean pt = new PayTaskItemBean();
                                            pt.setId(jsonArray.optJSONObject(i).optJSONArray("children").optJSONObject(k).optString("id"));
                                            pt.setName(jsonArray.optJSONObject(i).optJSONArray("children").optJSONObject(k).optString("name"));
                                            pt.setMoney(jsonArray.optJSONObject(i).optJSONArray("children").optJSONObject(k).optString("money"));
                                            pt.setNum(jsonArray.optJSONObject(i).optJSONArray("children").optJSONObject(k).optString("count"));
                                            pt.setPrice(jsonArray.optJSONObject(i).optJSONArray("children").optJSONObject(k).optString("price"));
                                            pt.setUnit(jsonArray.optJSONObject(i).optJSONArray("children").optJSONObject(k).optString("unit"));
                                            plist.add(pt);
                                        }
                                        p.setPayTaskItemBeanArrayList(plist);
                                    }
                                    payTaskBeanArrayList.add(p);
                                }
                                padapter.refresh(payTaskBeanArrayList);
                                if(expandlistview!=null){
                                    expandlistview.setVisibility(View.VISIBLE);
                                }
                            } else {
                                setINVISIBLE();
                            }
                        } else {
                            setINVISIBLE();
                            MyToast.showShort(getActivity(), jsonObject.optString("feedback"));
                        }
                    }
                } catch (Exception e) {
                    setINVISIBLE();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                setINVISIBLE();
                dismissProgressDialog();
                MyToast.showShort(getActivity(), "网络连接异常");
            }

        });
        /*for (int i = 0; i < 5; i++) {
            PayTaskBean p = new PayTaskBean();
            p.setMoney("10000");
            p.setName("产品" + i);
            p.setNumber("2016926" + i);
            ArrayList<PayTaskItemBean> plist = new ArrayList<PayTaskItemBean>();
            for (int j = 0; j < 3; j++) {
                PayTaskItemBean pt = new PayTaskItemBean();
                pt.setName("电脑" + i + j);
                pt.setMoney("20" + i + j);
                pt.setNum(i + j + "");
                pt.setPrice("1" + i + j);
                plist.add(pt);
            }
            p.setPayTaskItemBeanArrayList(plist);
            payTaskBeanArrayList.add(p);
        }
        padapter.refresh(payTaskBeanArrayList);*/
    }

    public void setINVISIBLE(){
        if(expandlistview!=null){
            expandlistview.setVisibility(View.INVISIBLE);
        }
    }

    public void updataListData() {
        money_all = 0;
        order_number="";
        int k=1;
        for (int i = 0; i < payTaskBeanArrayList.size(); i++) {
            if (payTaskBeanArrayList.get(i).isCheck()) {
                money_all = money_all + Double.parseDouble(payTaskBeanArrayList.get(i).getMoney());
                if(k==1){
                    order_number=order_number+payTaskBeanArrayList.get(i).getNumber();
                    k++;
                }else{
                    order_number=order_number+"-"+payTaskBeanArrayList.get(i).getNumber();
                }
            }
        }
        Log.e("tag","订单号"+order_number);
    }

    public synchronized void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        progressDialog.show(getActivity());
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismissDialog();
        }
    }

    public synchronized AppProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        return progressDialog;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_back:
                getActivity().finish();
                break;
            case R.id.weixinpay:
                pay_style = 1;
                weixin_btn.setVisibility(View.VISIBLE);
                alipay_btn.setVisibility(View.GONE);
                back_btn.setVisibility(View.GONE);
                break;
            case R.id.alipay:
                pay_style = 2;
                weixin_btn.setVisibility(View.GONE);
                alipay_btn.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.GONE);
                break;
            case R.id.yinlianpay:
                pay_style = 3;
                weixin_btn.setVisibility(View.GONE);
                alipay_btn.setVisibility(View.GONE);
                back_btn.setVisibility(View.VISIBLE);
                break;
            case R.id.pay_btn:
                if (money_all == 0) {
                    MyToast.showShort(getActivity(), "请选择商品");
                    return;
                }
                MyToast.showShort(getActivity(), money_all + "");
                switch (pay_style) {
                    case 1:
                        wxPay("1");//money_all
                        break;
                    case 2:
                        LogUtils.e("点击支付宝支付了");
                        PayVUtils.newInstance(getActivity()).alipayPay(order_number, "测试商品", "0.01", mHandler);//"xj" + sdf.format(new Date()) +
                        break;
                    case 3:
                        unionPay("1");
                        break;
                }
                break;
        }
    }

    private void wxPay(final String price) {
        showProgressDialog();
        SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
        final String out_trade_no = order_number;//"xj" + sdf.format(new Date()) +
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams(); // 默认编码UTF-8
        params.addBodyParameter("out_trade_no", out_trade_no);
        params.addBodyParameter("remark", "舒心定制");
        params.addBodyParameter("fujia", "舒心定制官方支付");
        params.addBodyParameter("price", price + "0");
        HttpPostUtil.getInstance().send(HttpRequest.HttpMethod.POST, "http://www.shuxin.net:8002/WXpay.aspx?", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dismissProgressDialog();
                Log.e("tag","weixin="+responseInfo.result);
                try {
                    JSONObject jsonObject=new JSONObject(responseInfo.result);
                    if(jsonObject.optInt("success")==1){
                        String sign = jsonObject.optJSONObject("data").optString("sign");
                        String prepayid = jsonObject.optJSONObject("data").optString("prepayid");
                        PayVUtils.newInstance(getActivity()).wxPay(price + "0", out_trade_no, "唯恩充值", prepayid);
                    }else{
                        MyToast.showShort(getActivity(), "失败");
                    }
                } catch (JSONException e) {
                    MyToast.showShort(getActivity(), "失败");
                    e.printStackTrace();
                }
               /* String sign = object.getString("sign");
                String prepayid = object.getString("prepayid");
                PayVUtils.newInstance(getActivity()).wxPay(price + "00", out_trade_no, "唯恩充值", prepayid);*/
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dismissProgressDialog();
                MyToast.showShort(getActivity(), "网络连接异常");
            }

        });
    }

    private void unionPay(String price) {
        Date date = new Date();
        String url = "http://www.shuxin.net:8002/ToYinLianOrder.aspx?";//getResources().getString(R.string.service_host_address).concat(getResources().getString(R.string.ToYinLianOrder));
        url += ("orderId=" + order_number + "&");//xj" + sdf.format(date) +
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String string = sdf.format(date);
        url += ("txnTime=" + string + "&");
        url += ("txnAmt=" + price + "0");
        Intent intent = new Intent(getActivity(), LinkToActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", "银联支付");
        startActivity(intent);
    }


    @SuppressLint("SimpleDateFormat")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    String memo = resultObj.memo;
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtils.e("支付结果"+resultObj);
                            Toast.makeText(getActivity(), "支付失败:"+memo, Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }

        ;
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();

    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {
        /**
         *接收广播
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
            Log.e("tag","onresume");
        }
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
}

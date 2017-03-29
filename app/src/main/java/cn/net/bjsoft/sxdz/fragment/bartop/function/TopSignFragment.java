package cn.net.bjsoft.sxdz.fragment.bartop.function;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.function.SignHistoryActivity;
import cn.net.bjsoft.sxdz.activity.welcome.LinkToActivity;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.view.CircleImageView;
import cn.net.bjsoft.sxdz.view.RoundImageView;

/**
 * Created by Zrzc on 2017/1/10.
 */

@ContentView(R.layout.fragment_sign)
public class TopSignFragment extends BaseFragment {
    @ViewInject(R.id.title)
    TextView title;
    //    @ViewInject(R.id.titlebar)
//    RelativeLayout titlebar;
    @ViewInject(R.id.sign_back)
    ImageView back;
    @ViewInject(R.id.sign_user_icon)
    CircleImageView userIcon;
    @ViewInject(R.id.sign_name)
    TextView name;
    @ViewInject(R.id.sign_bumen)
    TextView bumen;
    @ViewInject(R.id.sign_time)
    TextView time;
    @ViewInject(R.id.sign_address)
    TextView address;
    @ViewInject(R.id.sign_history)
    LinearLayout sign_history;
    @ViewInject(R.id.history)
    TextView history;
    @ViewInject(R.id.sign_picture)
    RoundImageView picture;
    //    @ViewInject(R.id.history_back)
//    LinearLayout historyBack;
    @ViewInject(R.id.sing_past_text)
    TextView pastText;
    @ViewInject(R.id.sign_pic_icon)
    ImageView picIcon;
    @ViewInject(R.id.sign_btn)
    ImageView pastBtn;
    @ViewInject(R.id.sign_last_time)
    TextView oletime;
    @ViewInject(R.id.sign_last_address)
    TextView oldaddress;

    private BitmapUtils bitmapUtils;

    private String url_image = "";

    private String imgdata = "";

    private AppProgressDialog progressDialog;

    private String mJson = "";
    //private DatasBean mDatasBean;
    private FragmentActivity mActivity;


    @Override
    public void initData() {
        setData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showShort(getActivity(), "返回");
                getActivity().finish();
            }
        });
        sign_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showShort(getActivity(), "查看历史");
                Intent i = new Intent(getActivity(), SignHistoryActivity.class);
//                i.putExtra("url", CacheSerializableUtil.getlist(getActivity()).getToolBar().getSignurl());
                i.putExtra("url", "www.baidu.com");
                i.putExtra("title", "网页");
                startActivity(i);
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getActivity().getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                if (!permission) {
                    MyToast.showLong(getActivity(), "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                    return;
                }
                MyToast.showShort(getActivity(), "拍照片");
                takePhoto();
            }
        });
        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showShort(getActivity(), "签到");
                if (url_image.equals("")) {
                    MyToast.showShort(getActivity(), "请拍摄图片");
                } else {
                    try {
                        showProgressDialog();
                        imgdata = MyBase64.fileToString(url_image);
                        Log.e("tag", imgdata);
                        time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                        address.setText(SPUtil.getAddress(getActivity()));
                        upData();
                    } catch (Exception e) {
                        dismissProgressDialog();
                        MyToast.showShort(getActivity(), "转换出错");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setData() {
        bitmapUtils = new BitmapUtils(getActivity(), AddressUtils.img_cache_url);
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);
        bitmapUtils.display(userIcon, mDatasBean.data.user.avatar);
        name.setText(mDatasBean.data.user.name);
        bumen.setText(mDatasBean.data.user.name);//这里接收的参数现在未定
        time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
//      oldaddress.setText(InitModel.getInstance(getActivity()).getUserData().getAddress());
//      oletime.setText(InitModel.getInstance(getActivity()).getUserData().getSigntime());
        //----------------------注意
        if (mDatasBean.data.pushdata.sign_last != null) {
            if (mDatasBean.data.pushdata.sign_last.address != null) {
                if (!mDatasBean.data.pushdata.sign_last.address.equals("")) {
                    oldaddress.setText(mDatasBean.data.pushdata.sign_last.address);
                }
            } else {
                oldaddress.setText("未获取到信息");
            }
            if (mDatasBean.data.pushdata.sign_last.signtime != null) {
                if (!mDatasBean.data.pushdata.sign_last.signtime.equals("")) {
                    oletime.setText(mDatasBean.data.pushdata.sign_last.signtime);
                }
            } else {
                oletime.setText("未获取到信息");
            }
            if (SPUtil.getAddress(getActivity()) != null) {
                if (!SPUtil.getAddress(getActivity()).equals("")) {
                    address.setText(SPUtil.getAddress(getActivity()));
                }
            } else {
                address.setText("未获取到信息");
            }
        } else {
            oldaddress.setText("未获取到信息");
            oletime.setText("未获取到信息");
            address.setText("未获取到信息");

        }
    }

    @Event(type = View.OnClickListener.class, value = {R.id.sign_back, R.id.sign_history, R.id.sign_picture, R.id.sign_btn})
    public void onSignClick(View view) {
        switch (view.getId()) {
            case R.id.sign_back:
                //MyToast.showShort(mActivity, "返回");
                mActivity.finish();

                break;
            case R.id.sign_history:
                //MyToast.showShort(mActivity, "查看历史");
                Intent i = new Intent(getActivity(), LinkToActivity.class);
//                i.putExtra("url", CacheSerializableUtil.getlist(getActivity()).getToolBar().getSignurl());
                i.putExtra("url", "www.baidu.com");
                i.putExtra("title", "网页");
                startActivity(i);
                break;
            case R.id.sign_picture:
                PackageManager pm = mActivity.getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                if (!permission) {
                    MyToast.showLong(mActivity, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                    return;
                }
                // MyToast.showShort(mActivity, "拍照片");
                takePhoto();
                break;
            case R.id.sign_btn:
                //MyToast.showShort(mActivity, "签到");
                if (url_image.equals("")) {
                    MyToast.showShort(getActivity(), "请拍摄图片");
                } else {
                    try {
                        showProgressDialog();
                        imgdata = MyBase64.fileToString(url_image);
                        Log.e("tag", imgdata);
                        time.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                        address.setText(SPUtil.getAddress(getActivity()));
                        upData();
                    } catch (Exception e) {
                        dismissProgressDialog();
                        MyToast.showShort(getActivity(), "转换出错");
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 签到
     */
    public void upData() {
        RequestParams params = new RequestParams(AddressUtils.httpurl);
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("method", "auto_position");
        params.addBodyParameter("action", "submit");
        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));
        params.addBodyParameter("abs_x", SPUtil.getLong(getActivity()) + "");
        params.addBodyParameter("abs_y", SPUtil.getLat(getActivity()) + "");
        params.addBodyParameter("abs_z", "");
        params.addBodyParameter("address", SPUtil.getAddress(getActivity()));
        params.addBodyParameter("imgdata", imgdata);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dismissProgressDialog();
                Log.e("tag", "video" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean tag = jsonObject.optBoolean("success");
                        if (tag) {
                            MyToast.showShort(getActivity(), "签到成功");
//                            InitModel.getInstance(getActivity()).getUserData().setAddress(jsonObject.optJSONObject("data").optString("address"));
//                            InitModel.getInstance(getActivity()).getUserData().setSigntime(jsonObject.optJSONObject("data").optString("signtime"));
//                            oldaddress.setText(jsonObject.optJSONObject("data").optString("address"));
//                            oletime.setText(jsonObject.optJSONObject("data").optString("signtime"));
                        } else {
                            MyToast.showShort(getActivity(), jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                MyToast.showShort(getActivity(), "网络连接异常");
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
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获取到的图片路径
     */
    private String picPath;
    private Uri photoUri;
    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;

    private void takePhoto() {
//执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
/***
 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
 * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
 */
            ContentValues values = new ContentValues();
            photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
/**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(getActivity(), "内存卡不存在", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("tag", "onActivityResult");
        if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            doPhoto(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {
        Log.e("tag", "doPhoto");
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            if (Build.VERSION.SDK_INT < 14) {

                cursor.close();
            }
        }
        Log.e("tag", "imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            Log.e("tag", "最终选择的图片=" + picPath);
            url_image = picPath;
            //MyBitmapUtils.getInstance(getActivity()).display(picture,picPath);
            Bitmap bm = BitmapFactory.decodeFile(picPath);
            //toRoundCorner(bm, 60);
            //picture.setImageBitmap(toRoundCorner(bm, 60));
            MyBitmapUtils.getInstance(getActivity()).display(picture, picPath);
        } else {
            Toast.makeText(getActivity(), "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
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
}

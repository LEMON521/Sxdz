package cn.net.bjsoft.sxdz.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.video.PhotoActivity;
import cn.net.bjsoft.sxdz.bean.VideoTaskItem;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopPhotoFragment;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.HttpPostUtil;
import cn.net.bjsoft.sxdz.utils.MyBitmapUtils;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by zkagang on 2016/9/27.
 */
public class VideoItemAdapter extends BaseAdapter implements View.OnClickListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private PopupWindow popupWindow, popupWindow1;
    private Context context;
    private ArrayList<VideoTaskItem> videoTaskArrayList;
    private TopPhotoFragment vf;
    private AppProgressDialog progressDialog;

    // 多媒体设备
    private MediaRecorder mRecod;

    public VideoItemAdapter(Context context, ArrayList<VideoTaskItem> videoTaskArrayList, TopPhotoFragment vf) {
        this.context = context;
        this.videoTaskArrayList = videoTaskArrayList;
        this.vf = vf;
    }

    @Override
    public int getCount() {
        return videoTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoTaskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder tag = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_childtask, null);
            tag = new Holder();
            tag.name = (TextView) convertView.findViewById(R.id.name);
            tag.delect = (ImageView) convertView.findViewById(R.id.delete);
            tag.imgview = (ImageView) convertView.findViewById(R.id.imgview);
            convertView.setTag(tag);
        }
        Log.e("tag", videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(videoTaskArrayList.get(position).getName());
        if (videoTaskArrayList.get(position).getIfnull().equals("1")) {
            if (videoTaskArrayList.get(position).getTag().equals("1")) {
                MyBitmapUtils.getInstance(context).display(holder.imgview, videoTaskArrayList.get(position).getMediaurl());
            } else if (videoTaskArrayList.get(position).getTag().equals("2")) {
                holder.imgview.setBackgroundResource(R.drawable.common_av);
            } else if (videoTaskArrayList.get(position).getTag().equals("3")) {
                holder.imgview.setBackgroundResource(R.drawable.common_audio);
            }
        } else {
            //holder.imgview.setImageResource(R.drawable.common_add);
            if (videoTaskArrayList.get(position).getTag().equals("2")) {
                holder.imgview.setBackgroundResource(R.drawable.common_add);
            } else if (videoTaskArrayList.get(position).getTag().equals("3")) {
                holder.imgview.setBackgroundResource(R.drawable.common_add);
            } else if (videoTaskArrayList.get(position).getTag().equals("1")) {
                //holder.imgview.setBackgroundResource(R.drawable.common_photo);
                holder.imgview.setImageResource(R.drawable.common_photo);
            }
        }
        if (videoTaskArrayList.get(position).getStatus().equals("3") || videoTaskArrayList.get(position).getStatus().equals("5")) {
            holder.delect.setVisibility(View.GONE);
        } else {
            if (videoTaskArrayList.get(position).getIfnull().equals("0")) {
                holder.delect.setVisibility(View.GONE);
            } else {
                holder.delect.setVisibility(View.VISIBLE);
            }
        }

        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoTaskArrayList.get(position).getIsurl().equals("0")) {
                    if (videoTaskArrayList.get(position).getId().equals("")) {
                        videoTaskArrayList.remove(position);
                    } else {
                        videoTaskArrayList.get(position).setMediaurl("");
                        videoTaskArrayList.get(position).setIfnull("0");
                    }
                    refresh(videoTaskArrayList);
                } else {//走网络删除
                    //showProgressDialog();
                    //delete(position);
                    videoTaskArrayList.get(position).setMediaurl("");
                    videoTaskArrayList.get(position).setIfnull("0");
                    refresh(videoTaskArrayList);
                }
            }
        });


        //点击名称修改
        holder.name.setTag(position);
        holder.name.setOnClickListener(this);

        //点击图片拍摄
        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoTaskArrayList.get(position).getTag().equals("1")) {//图片
                    PackageManager pm = context.getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        MyToast.showShort(context, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                        return;
                    }
                    if (videoTaskArrayList.get(position).getIfnull().equals("0")) {//是空
                        showphoto(v, position);
                    } else {//不是空
                        Intent i = new Intent(context, PhotoActivity.class);
                        i.putExtra("url", videoTaskArrayList.get(position).getMediaurl());
                        context.startActivity(i);
                    }
                } else if (videoTaskArrayList.get(position).getTag().equals("2")) {//视频
                    PackageManager pm = context.getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        MyToast.showShort(context, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                        return;
                    }
                    if (videoTaskArrayList.get(position).getIfnull().equals("0")) {//是空
                        Intent intent = new Intent();
                        //指明action值，这个action值要和接收器的过滤器中的action值一致，这个时候才可以完成匹配
                        intent.setAction("com.shuxin.video");
                        intent.putExtra("groupid", videoTaskArrayList.get(position).getFid());
                        intent.putExtra("type", videoTaskArrayList.get(position).getTag());
                        intent.putExtra("id", position);
                        context.sendBroadcast(intent);
                        Intent intent2 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        vf.startActivityForResult(intent2, 3);
                    } else {//不是空

                    }
                } else if (videoTaskArrayList.get(position).getTag().equals("3")) {//音频

                    if (videoTaskArrayList.get(position).getIfnull().equals("0")) {//是空
                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            Intent i = new Intent();
                            //指明action值，这个action值要和接收器的过滤器中的action值一致，这个时候才可以完成匹配
                            i.setAction("com.shuxin.video");
                            i.putExtra("groupid", videoTaskArrayList.get(position).getFid());
                            i.putExtra("type", videoTaskArrayList.get(position).getTag());
                            i.putExtra("id", position);
                            context.sendBroadcast(i);

                            //Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                            try {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("audio/amr");
                                intent.setClassName("com.android.soundrecorder",
                                        "com.android.soundrecorder.SoundRecorder");
                                vf.startActivityForResult(intent, 4);
                            } catch (Exception e) {
                                MyToast.showShort(context, "录音功能不能使用");
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                            }

                        } else {
                            MyToast.showShort(context, "缺少内存设备");
                        }
                    } else {//不是空

                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag() + "");
        if (videoTaskArrayList.get(position).getStatus().equals("3") || videoTaskArrayList.get(position).getStatus().equals("5")) {
            return;
        } else {
            showpop(v, position);
        }
    }

    /**
     * 弹出修改名称的窗口
     */
    public void showpop(View v, final int i) {
        View contentView;
        popupWindow = null;
        if (popupWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            contentView = mLayoutInflater.inflate(R.layout.window_updata, null);
            final EditText name = (EditText) contentView.findViewById(R.id.name);
            name.setText(videoTaskArrayList.get(i).getName());
            TextView sure = (TextView) contentView.findViewById(R.id.sure);
            final TextView cancle = (TextView) contentView.findViewById(R.id.cancle);
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(videoTaskArrayList.get(i).getId().equals("")){
                        videoTaskArrayList.get(i).setName(name.getText().toString().trim());
                        refresh(videoTaskArrayList);
                    }else{
                        updataName(i,name.getText().toString().trim());
                    }*/
                    videoTaskArrayList.get(i).setName(name.getText().toString().trim());
                    refresh(videoTaskArrayList);
                    popupWindow.dismiss();
                }
            });
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.4f;
        ((Activity) context).getWindow().setAttributes(lp);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation((View) v.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });

    }

    private void showphoto(View v, final int i) {

        View contentView;
        popupWindow1 = null;
        if (popupWindow1 == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            contentView = mLayoutInflater.inflate(R.layout.pop_select_1, null);
            popupWindow1 = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView paizhao = (TextView) contentView.findViewById(R.id.paizhao);
            TextView xiangce = (TextView) contentView.findViewById(R.id.xiangce);
            TextView quxiao = (TextView) contentView.findViewById(R.id.quxiao);

            paizhao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //指明action值，这个action值要和接收器的过滤器中的action值一致，这个时候才可以完成匹配
                    intent.setAction("com.shuxin.video");
                    intent.putExtra("groupid", videoTaskArrayList.get(i).getFid());
                    intent.putExtra("type", videoTaskArrayList.get(i).getTag());
                    intent.putExtra("id", i);
                    context.sendBroadcast(intent);
                    takePhoto();
                    popupWindow1.dismiss();
                }
            });
            xiangce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //指明action值，这个action值要和接收器的过滤器中的action值一致，这个时候才可以完成匹配
                    intent.setAction("com.shuxin.video");
                    intent.putExtra("groupid", videoTaskArrayList.get(i).getFid());
                    intent.putExtra("type", videoTaskArrayList.get(i).getTag());
                    intent.putExtra("id", i);
                    context.sendBroadcast(intent);
                    pickPhoto();
                    popupWindow1.dismiss();
                }
            });
            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow1.dismiss();
                }
            });
        }

        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow1.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.4f;
        ((Activity) context).getWindow().setAttributes(lp);

        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setFocusable(true);
        popupWindow1.showAtLocation((View) v.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow1.update();
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });
    }


    /**
     * 修改任务名称
     */
    public void updataName(final int i, final String contant) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(context));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(context));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(context));
        params.addBodyParameter("js_mvc_name", "tab_drm_shoot");
        params.addBodyParameter("type", videoTaskArrayList.get(i).getTag());
        params.addBodyParameter("action", "update");
        params.addBodyParameter("name", contant);
        params.addBodyParameter("id", videoTaskArrayList.get(i).getId());
        params.addBodyParameter("user_id", SPUtil.getUserId(context));
        HttpPostUtil.getInstance().send(HttpRequest.HttpMethod.POST, "http://api.shuxin.net/Service/JsonData.aspx", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject != null) {
                        boolean tag = jsonObject.optBoolean("success");
                        if (tag) {
                            MyToast.showShort(context, "修改成功");
                            Intent intent = new Intent();
                            //指明action值，这个action值要和接收器的过滤器中的action值一致，这个时候才可以完成匹配
                            intent.setAction("com.shuxin.video");
                            context.sendBroadcast(intent);
                            /*videoTaskArrayList.get(i).setName(contant);
                            refresh(videoTaskArrayList);*/
                        } else {
                            MyToast.showShort(context, jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dismissProgressDialog();
                MyToast.showShort(context, "网络连接异常");
            }
        });
    }


    public static class Holder {
        public TextView name;
        public ImageView delect, imgview;
    }

    public void refresh(ArrayList<VideoTaskItem> videoTaskArrayList) {
        this.videoTaskArrayList = videoTaskArrayList;
        notifyDataSetChanged();
    }

    public synchronized void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        progressDialog.show(context);
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


    ////////////////////////////////////////////////////////////////////
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
            vf.photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            //photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, vf.photoUri);
            vf.startActivityForResult(intent, 1);
        } else {
            Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        vf.startActivityForResult(intent, 2);
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
     * 删除
     *
     * @param i
     */
    public void delete(final int i) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(context));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(context));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(context));
        params.addBodyParameter("action", "delete");
        params.addBodyParameter("js_mvc_name", "tab_drm_shoot");
        params.addBodyParameter("id", videoTaskArrayList.get(i).getId());
        HttpPostUtil.getInstance().send(HttpRequest.HttpMethod.POST, AddressUtils.httpurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject != null) {
                        boolean tag = jsonObject.optBoolean("success");
                        if (tag) {
                            /*videoTaskArrayList.get(i).setMediaurl("");
                            videoTaskArrayList.get(i).setIfnull("0");
                            videoTaskArrayList.get(i).setIsurl("1");
                            refresh(videoTaskArrayList);*/
                            Intent intent = new Intent();
                            //指明action值，这个action值要和接收器的过滤器中的action值一致，这个时候才可以完成匹配
                            intent.setAction("com.shuxin.video");
                            context.sendBroadcast(intent);
                        } else {
                            MyToast.showShort(context, jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dismissProgressDialog();
                MyToast.showShort(context, "网络连接异常");
            }

        });
    }
}

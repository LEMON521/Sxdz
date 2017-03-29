package cn.net.bjsoft.sxdz.fragment.bartop.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.video.AddTaskActivity;
import cn.net.bjsoft.sxdz.adapter.VideoAdapter;
import cn.net.bjsoft.sxdz.bean.VideoTask;
import cn.net.bjsoft.sxdz.bean.VideoTaskItem;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.model.VideoModel;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 拍照
 * Created by Zrzc on 2017/1/10.
 */

@ContentView(R.layout.fragment_video)
public class TopPhotoFragment extends BaseFragment {

    @ViewInject(R.id.listview)
    ListView listview;
    @ViewInject(R.id.function_photo_add)
    ImageView add;
    private View view;
    private VideoModel videoModel;

    private VideoAdapter videoAdapter;

    private int REQUEST_ADD=101;

    private SimpleDateFormat sdf;

    private AppProgressDialog progressDialog;

    private int tagid;
    private String fid,type;
    @Override
    public void initData() {
        getActivity().registerReceiver(receiver, new IntentFilter("com.shuxin.video"));
        sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        videoModel = VideoModel.getInstance(getActivity());
        videoAdapter = new VideoAdapter(getActivity(), videoModel.getVideoTaskArrayList(), this);
        listview.setAdapter(videoAdapter);
        getData();
    }

    @Event(value = {R.id.function_photo_add,R.id.function_photo_back})
    private void topPhotoOnclick(View view){
        switch (view.getId()){
            case R.id.function_photo_add:
                Intent intent=new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(intent,REQUEST_ADD);
                break;
            case R.id.function_photo_back:
                mActivity.finish();
                break;
        }
    }


    /**
     * 初始化
     */
    public void getData() {
        //showProgressDialog();
        RequestParams params = new RequestParams("http://api.shuxin.net/Service/JsonData.aspx");
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(getActivity()));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(getActivity()));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(getActivity()));
        params.addBodyParameter("method", "load_shoot_data");
        params.addBodyParameter("moduleid", "drm");
        params.addBodyParameter("action", "load");
        params.addBodyParameter("user_id", SPUtil.getUserId(getActivity()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dismissProgressDialog();
                Log.e("获取到的数据", "拍照json" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean tag = videoModel.parseJsonObject(jsonObject);
                        if (tag) {
                            videoAdapter.refresh(videoModel.getVideoTaskArrayList());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException", "异常了");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(getActivity(),"网络连接异常");
                videoAdapter.refresh(new ArrayList<VideoTask>());
                Log.e("拍照json", "onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("拍照json", "onCancelled");
            }

            @Override
            public void onFinished() {
                Log.e("拍照json", "onFinished");
                dismissProgressDialog();

            }
        });


    }


    public void onback(View view) {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {

        getActivity().unregisterReceiver(receiver);
        super.onDestroyView();
    }

    @Event(R.id.add)
    public void onClick() {
        Intent intent=new Intent(getActivity(), AddTaskActivity.class);
        startActivityForResult(intent,REQUEST_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("tag","11111111");
        if(requestCode==REQUEST_ADD&&resultCode==102){
            getData();
        }else if(requestCode==1){
            doPhoto(requestCode, data);
        }else if(requestCode==2){
            doPhoto(requestCode, data);
        }else if(requestCode==3){
            if(data!=null){
                Uri uriRecorder = data.getData();
                if(uriRecorder==null){
                    return;
                }
                Cursor mCusor = getActivity().getContentResolver().query(uriRecorder, null, null, null, null);
                if (mCusor.moveToNext()) {
                    // _data：文件的绝对路径 ，_display_name：文件名
                    strRecorderPath = mCusor.getString(mCusor.getColumnIndex("_data"));
                    updataVideo();
                }
            }
        }else if(requestCode==4){
            /*if(data!=null){
                Uri uriRecorder = data.getData();
                if(uriRecorder==null){
                    return;
                }
                Uri audioPath = data.getData();

                strRecorderPath=audioPath.toString();
                updataAudio();
            }*/
            if(data==null){
                return;
            }
            Uri uriRecorder = data.getData();
            if(uriRecorder==null){
                return;
            }
            try {
                AssetFileDescriptor videoAsset = getActivity().getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream fis = videoAsset.createInputStream();
                //图片存放地址
                String fileName = Environment.getExternalStorageDirectory()+"/"+System.currentTimeMillis()+".amr";//saveDir+"/" +System.currentTimeMillis()+".arm";
                Log.e("tag",fileName);
                FileOutputStream fos = new FileOutputStream(fileName);

                byte[] buf = new byte[1024];
                int len;
                while ((len = fis.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                fis.close();
                fos.close();
                Log.e("tag","成功了");
                strRecorderPath=fileName;
                updataAudio();
            } catch (IOException io_e) {
                Log.e("tag",io_e.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 视频路径
     */
    private String strRecorderPath;
    /**
     * 获取到的图片路径
     */
    private String picPath;
    public static Uri photoUri;
    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {
        if(requestCode == 2 ) //从相册取图片，有些手机有异常情况，请注意
        {
            if(data == null)
            {
                MyToast.showShort(getActivity(), "选择图片文件出错");
                return;
            }
            photoUri = data.getData();
            if(photoUri == null )
            {
                MyToast.showShort(getActivity(), "选择图片文件出错");
                return;
            }
        }

        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            if(cursor.getCount()==0){
                return;
            }
            picPath = cursor.getString(columnIndex);
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
        if(BitmapFactory.decodeFile(picPath)==null){
            return;
        }
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            Log.e("tag", "最终选择的图片=" + picPath);
            Log.e("tag", fid+"=fid");
            for(int i=0;i<videoModel.getVideoTaskArrayList().size();i++){
                Log.e("tag", videoModel.getVideoTaskArrayList().get(i).getId()+"=id");
                if(videoModel.getVideoTaskArrayList().get(i).getId().equals(fid+"")){
                    Log.e("tag", "走到这里了---------");
                    videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(tagid).setMediaurl(picPath);
                    videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(tagid).setIfnull("1");
                    if(videoModel.getVideoTaskArrayList().get(i).getCount().equals("0")||Integer.parseInt(videoModel.getVideoTaskArrayList().get(i).getCount())>videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()){
                        VideoTaskItem vti=new VideoTaskItem();
                        vti.setName("未命名");
                        vti.setId("");
                        vti.setMediaurl("");
                        vti.setIsurl("0");
                        vti.setIsjia("1");
                        vti.setFid(videoModel.getVideoTaskArrayList().get(i).getId());
                        vti.setStatus(videoModel.getVideoTaskArrayList().get(i).getStatus());
                        vti.setTag(videoModel.getVideoTaskArrayList().get(i).getType());
                        vti.setIfnull("0");
                        Log.e("tag","000000000000000");
                        if(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()-1).getIsjia().equals("1")&&videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()-1).getMediaurl().equals("")){
                            Log.e("tag","11111111111");
                        }else{
                            Log.e("tag","22222222222222");
                            videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().add(vti);
                        }
                        Log.e("tag","333333333333333");
                    }
                    Log.e("tag","44444444444");
                    videoAdapter.refresh(videoModel.getVideoTaskArrayList());
                }
                Log.e("tag", "走到这里了=========");
            }

        } else {
            Toast.makeText(getActivity(), "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 拍摄视频后刷新adapter
     */
    private void updataVideo(){
        for(int i=0;i<videoModel.getVideoTaskArrayList().size();i++){
            Log.e("tag", videoModel.getVideoTaskArrayList().get(i).getId()+"=id");
            if(videoModel.getVideoTaskArrayList().get(i).getId().equals(fid+"")){
                Log.e("tag", "走到这里了");
                videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(tagid).setMediaurl(strRecorderPath);
                videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(tagid).setIfnull("1");
                if(videoModel.getVideoTaskArrayList().get(i).getCount().equals("0")||Integer.parseInt(videoModel.getVideoTaskArrayList().get(i).getCount())>videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()){
                    VideoTaskItem vti=new VideoTaskItem();
                    vti.setName("未命名");
                    vti.setId("");
                    vti.setMediaurl("");
                    vti.setIsurl("0");
                    vti.setIsjia("1");
                    vti.setFid(videoModel.getVideoTaskArrayList().get(i).getId());
                    vti.setStatus(videoModel.getVideoTaskArrayList().get(i).getStatus());
                    vti.setTag(videoModel.getVideoTaskArrayList().get(i).getType());
                    vti.setIfnull("0");
                    if(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()-1).getIsjia().equals("1")&&videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()-1).getMediaurl().equals("")){

                    }else{
                        videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().add(vti);
                    }
                }
                videoAdapter.refresh(videoModel.getVideoTaskArrayList());
            }
        }
    }

    /**
     * 录音后刷新adapter
     */
    private void updataAudio(){
        for(int i=0;i<videoModel.getVideoTaskArrayList().size();i++){
            Log.e("tag", videoModel.getVideoTaskArrayList().get(i).getId()+"=id");
            if(videoModel.getVideoTaskArrayList().get(i).getId().equals(fid+"")){
                Log.e("tag", "走到这里了");
                videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(tagid).setMediaurl(strRecorderPath);
                videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(tagid).setIfnull("1");
                if(videoModel.getVideoTaskArrayList().get(i).getCount().equals("0")||Integer.parseInt(videoModel.getVideoTaskArrayList().get(i).getCount())>videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()){
                    VideoTaskItem vti=new VideoTaskItem();
                    vti.setName("未命名");
                    vti.setId("");
                    vti.setMediaurl("");
                    vti.setIsurl("0");
                    vti.setIsjia("1");
                    vti.setFid(videoModel.getVideoTaskArrayList().get(i).getId());
                    vti.setStatus(videoModel.getVideoTaskArrayList().get(i).getStatus());
                    vti.setTag(videoModel.getVideoTaskArrayList().get(i).getType());
                    vti.setIfnull("0");
                    if(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()-1).getIsjia().equals("1")&&videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().get(videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().size()-1).getMediaurl().equals("")){

                    }else{
                        videoModel.getVideoTaskArrayList().get(i).getVideoTaskItemArrayList().add(vti);
                    }
                }
                videoAdapter.refresh(videoModel.getVideoTaskArrayList());
            }
        }
    }


    public synchronized void showProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new AppProgressDialog();
        }
        progressDialog.show(getActivity());
    }

    public void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismissDialog();
        }
    }

    public synchronized AppProgressDialog getProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new AppProgressDialog();
        }
        return progressDialog;
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
            fid=intent.getStringExtra("groupid");
            if(fid!=null){
                tagid=intent.getIntExtra("id",-1);
                type=intent.getStringExtra("type");
            }else{
                //通知页面刷新
                showProgressDialog();
                getData();
            }
        }
    }
}

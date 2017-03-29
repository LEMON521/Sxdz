package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.UpVideoBean;
import cn.net.bjsoft.sxdz.bean.VideoTask;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopPhotoFragment;
import cn.net.bjsoft.sxdz.utils.Constants;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyBase64;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by zkagang on 2016/9/27.
 */
public class VideoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<VideoTask> videoTaskArrayList;
    private TopPhotoFragment vf;
    private AppProgressDialog progressDialog;
    public VideoAdapter(Context context, ArrayList<VideoTask> videoTaskArrayList, TopPhotoFragment vf){
        this.context=context;
        this.videoTaskArrayList=videoTaskArrayList;
        this.vf=vf;
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
                    R.layout.item_task, null);
            tag = new Holder();
            tag.name= (TextView) convertView.findViewById(R.id.taskname);
            tag.taskstate= (TextView) convertView.findViewById(R.id.taskstate);
            tag.tijiao= (TextView) convertView.findViewById(R.id.tijiao);
            tag.delete= (TextView) convertView.findViewById(R.id.delete);
            tag.gridview= (GridView) convertView.findViewById(R.id.gridview);
            convertView.setTag(tag);
        }

        // 设置数据
        Holder holder = (Holder) convertView.getTag();
        holder.name.setText(videoTaskArrayList.get(position).getName());
        switch(videoTaskArrayList.get(position).getStatus()){
            case "1":
                holder.taskstate.setText("新建");
                holder.tijiao.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                break;
            case "2":
                holder.taskstate.setText("待拍");
                holder.tijiao.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.GONE);
                break;
            case "3":
                holder.taskstate.setText("待审核");
                holder.tijiao.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                break;
            case "4":
                holder.taskstate.setText("重拍");
                holder.tijiao.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.GONE);
                break;
            case "5":
                holder.taskstate.setText("完成");
                holder.tijiao.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                break;
        }
        VideoItemAdapter videoItemAdapter=new VideoItemAdapter(context,videoTaskArrayList.get(position).getVideoTaskItemArrayList(),vf);
        holder.gridview.setAdapter(videoItemAdapter);

        holder.tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                upData(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                delete(position);
            }
        });

        return convertView;
    }

    public static class Holder {
        public TextView name,taskstate,tijiao,delete;
        public GridView gridview;
    }

    public void refresh(ArrayList<VideoTask> videoTaskArrayList){
        this.videoTaskArrayList=videoTaskArrayList;
        notifyDataSetChanged();
    }

    public String getJsonStr(int tag){
        ArrayList<UpVideoBean> list=new ArrayList<UpVideoBean>();
        for(int i=0;i<videoTaskArrayList.get(tag).getVideoTaskItemArrayList().size();i++){
            UpVideoBean v=new UpVideoBean();
            v.setId(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getId());
            v.setName(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getName());
            if(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getMediaurl().equals("")&&videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getId().equals("")){
                v.setBlobdata("");
            }else{
                try {
                    if(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getIsurl().equals("1")&&videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getIsjia().equals("0")){
                        v.setBlobdata("");
                    }else{
                        if(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getMediaurl().equals("")){
                            v.setBlobdata("");
                        }else{
                            v.setBlobdata(MyBase64.fileToString(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getMediaurl()));
                        }
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    Log.e("tag","异常了");
                    e.printStackTrace();
                }
            }
            if(videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getId().equals("")&&videoTaskArrayList.get(tag).getVideoTaskItemArrayList().get(i).getMediaurl().equals("")){

            }else{
                list.add(v);
            }
        }
        return GsonUtil.getGson().toJson(list);
    }

    public void upData(int i){
        RequestParams params = new RequestParams("http://api.shuxin.net/Service/JsonData.aspx?");
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(context));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(context));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(context));
        params.addBodyParameter("method", "save_shoot_data");
        params.addBodyParameter("action", "submit");
        params.addBodyParameter("moduleid", "drm");
        params.addBodyParameter("shoot_id", videoTaskArrayList.get(i).getId());
        params.addBodyParameter("children", getJsonStr(i));
        Log.e("tag", "up" + getJsonStr(i));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("tag", "req" + result);
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean tag = jsonObject.optBoolean("success");
                        if (tag) {
                            MyToast.showShort(context, "提交成功");
                            Intent intent = new Intent();
                            intent.setAction("com.shuxin.video");
                            context.sendBroadcast(intent);
                        } else {
                            MyToast.showShort(context, "失败原因："+jsonObject.optString("feedback"));
                        }
                    }
                } catch (JSONException e) {
                    MyToast.showShort(context, "异常");
                    dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(context, "网络连接异常");
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

    /**
     * 删除
     * @param i
     */
    public void delete(final int i){
        RequestParams params = new RequestParams("http://api.shuxin.net/Service/JsonData.aspx");
        params.addBodyParameter("client_name", Constants.app_name);
        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(context));
        params.addBodyParameter("single_code", SPUtil.getUserRandCode(context));
        params.addBodyParameter("uuid", SPUtil.getUserUUID(context));
        params.addBodyParameter("action", "delete");
        params.addBodyParameter("js_mvc_name", "tab_drm_shoot");
        params.addBodyParameter("id", videoTaskArrayList.get(i).getId());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        boolean tag = jsonObject.optBoolean("success");
                        if (tag) {
                            Intent intent = new Intent();
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
            public void onError(Throwable ex, boolean isOnCallback) {
                MyToast.showShort(context, "网络连接异常");
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
}

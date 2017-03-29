package cn.net.bjsoft.sxdz.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.bean.VideoTask;
import cn.net.bjsoft.sxdz.bean.VideoTaskItem;

/**
 * Created by zkagang on 2016/9/18.
 */
public class VideoModel implements Serializable {
    private static VideoModel instance;
    private Context context;
    private ArrayList<VideoTask> videoTaskArrayList=new ArrayList<VideoTask>();



    public VideoModel(Context context) {
        this.context = context;
    }

    public static VideoModel getInstance(Context context) {
        if(instance == null)
            instance = new VideoModel(context.getApplicationContext());
        return instance;
    }

    public boolean parseJsonObject(JSONObject j) {
        clearData();
        try {
            JSONArray jsonArray=j.optJSONArray("data");
            if(jsonArray!=null&&jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++){
                    VideoTask vt=new VideoTask();
                    vt.setId(jsonArray.optJSONObject(i).optString("id"));
                    vt.setName(jsonArray.optJSONObject(i).optString("name"));
                    vt.setCount(jsonArray.optJSONObject(i).optString("count"));
                    vt.setStatus(jsonArray.optJSONObject(i).optString("status"));
                    vt.setType(jsonArray.optJSONObject(i).optString("type"));
                    JSONArray ja=jsonArray.optJSONObject(i).optJSONArray("children");
                    if(ja!=null&&ja.length()>0){
                        vt.setTag("1");
                        for(int k=0;k<ja.length();k++){
                            VideoTaskItem vti=new VideoTaskItem();
                            vti.setName(ja.optJSONObject(k).optString("name"));
                            vti.setId(ja.optJSONObject(k).optString("id"));
                            vti.setMediaurl(ja.optJSONObject(k).optString("mediaurl"));
                            if(!ja.optJSONObject(k).optString("mediaurl").equals("")){
                                vti.setIsurl("1");
                            }else{
                                vti.setIsurl("0");
                            }
                            vti.setStatus(jsonArray.optJSONObject(i).optString("status"));
                            vti.setFid(jsonArray.optJSONObject(i).optString("id"));
                            vti.setTag(jsonArray.optJSONObject(i).optString("type"));
                            if(ja.optJSONObject(k).optString("mediaurl").equals("")){
                                vti.setIfnull("0");
                            }else{
                                vti.setIfnull("1");
                            }
                            vt.getVideoTaskItemArrayList().add(vti);
                        }
                        if(jsonArray.optJSONObject(i).optString("count").equals("0")|| Integer.parseInt(jsonArray.optJSONObject(i).optString("count"))<ja.length()){
                           if(!jsonArray.optJSONObject(i).optString("status").equals("3")&&!jsonArray.optJSONObject(i).optString("status").equals("5")){
                               VideoTaskItem vti=new VideoTaskItem();
                               vti.setName("未命名");
                               vti.setId("");
                               vti.setMediaurl("");
                               vti.setIsurl("0");
                               vti.setIsjia("1");
                               vti.setFid(jsonArray.optJSONObject(i).optString("id"));
                               vti.setStatus(jsonArray.optJSONObject(i).optString("status"));
                               vti.setTag(jsonArray.optJSONObject(i).optString("type"));
                               vti.setIfnull("0");
                               vt.getVideoTaskItemArrayList().add(vti);
                           }
                        }
                    }else{
                        if(!jsonArray.optJSONObject(i).optString("status").equals("3")&&!jsonArray.optJSONObject(i).optString("status").equals("5")) {
                            VideoTaskItem vti=new VideoTaskItem();
                            vti.setName("未命名");
                            vti.setId("");
                            vti.setIsjia("1");
                            vti.setMediaurl("");
                            vti.setIsurl("0");
                            vti.setFid(jsonArray.optJSONObject(i).optString("id"));
                            vti.setStatus(jsonArray.optJSONObject(i).optString("status"));
                            vti.setTag(jsonArray.optJSONObject(i).optString("type"));
                            vti.setIfnull("0");
                            vt.setTag("0");
                            vt.getVideoTaskItemArrayList().add(vti);
                        }
                    }
                    videoTaskArrayList.add(vt);
                }
                return true;
            }else{
                return false;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public ArrayList<VideoTask> getVideoTaskArrayList() {
        return videoTaskArrayList;
    }


    private void clearData() {
        videoTaskArrayList.clear();
    }
}

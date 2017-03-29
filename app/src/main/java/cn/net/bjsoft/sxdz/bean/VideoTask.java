package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zkagang on 2016/9/27.
 */
public class VideoTask implements Serializable {
    private String id;
    private String name;
    private String type;
    private String count;
    private String status;
    private String tag;//判断是否有child
    private ArrayList<VideoTaskItem> videoTaskItemArrayList=new ArrayList<VideoTaskItem>();

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<VideoTaskItem> getVideoTaskItemArrayList() {
        return videoTaskItemArrayList;
    }

    public void setVideoTaskItemArrayList(ArrayList<VideoTaskItem> videoTaskItemArrayList) {
        this.videoTaskItemArrayList = videoTaskItemArrayList;
    }
}

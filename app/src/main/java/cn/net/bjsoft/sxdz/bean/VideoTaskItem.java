package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;

/**
 * Created by zkagang on 2016/9/27.
 */
public class VideoTaskItem implements Serializable {
    private String id;
    private String name;
    private String mediaurl;
    private String tag;//判断是1图片还是2视频还是3音频
    private String ifnull;//判断是否是空的
    private String fid;

    private String isurl;//判断最开始有没有url地址

    private String isjia="0";

    public String getIsjia() {
        return isjia;
    }

    public void setIsjia(String isjia) {
        this.isjia = isjia;
    }

    public String getIsurl() {
        return isurl;
    }

    public void setIsurl(String isurl) {
        this.isurl = isurl;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIfnull() {
        return ifnull;
    }

    public void setIfnull(String ifnull) {
        this.ifnull = ifnull;
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

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }
}

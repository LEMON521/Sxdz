package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;

/**
 * Created by zkagang on 2016/10/5.
 */
public class UpVideoBean implements Serializable {

    private String blobdata;
    private String name;
    private String id;

    public String getBlobdata() {
        return blobdata;
    }

    public void setBlobdata(String blobdata) {
        this.blobdata = blobdata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

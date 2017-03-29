package cn.net.bjsoft.sxdz.bean.community;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/1/13.
 */

public class DisabuseBean implements Serializable {
    /**
     * 提问者
     */
    public String avatarUrl;
    public String name;
    public String dis;
    public String time;


    public String happenTime;
    public int state;//0已回复,1未回复

    /**
     * 回复者
     */
    public String reAvatarUrl;
    public String reName;
    public String reDis;
    public String reTime;

}

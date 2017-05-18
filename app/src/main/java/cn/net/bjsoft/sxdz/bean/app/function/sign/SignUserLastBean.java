package cn.net.bjsoft.sxdz.bean.app.function.sign;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/5/18.
 */

public class SignUserLastBean implements Serializable {
    // {"code":0,
    // "data":{
    //      "id":"5267675506130298631",
    //      "address":"北京市朝阳区朝阳路辅路靠近朝阳区管庄中医医院",
    //      "ctime":"2017-05-18 16:21:41",
    //      "img_url":"http://api.shuxinyun.com/files/temp/20170518_042137/131395691074911883.jpg"
    // },
    // "msg":"select id,address,ctime,img_url from shuxin_signin  where (userid = 12341) order by ctime desc"}

    public String code = "";
    public SignUserLastDataBean data;
}

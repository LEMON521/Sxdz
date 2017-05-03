package cn.net.bjsoft.sxdz.utils;


import android.content.Context;

/**
 * url拼接工具
 * Created by zkagang on 2016/5/19.
 */
public class UrlUtil {

    public final static String init_url = "http://api.shuxinyun.com/apps/4895081593118139245/mobile.json";

    public final static String apps_url = "http://api.shuxinyun.com/apps/";

    public final static String baseUrl = "http://api.shuxin.net/service/jsondata.aspx?";
    public final static String scanUrl = "http://www.shuxin.net/wap/app/modules/rcjg/view/form/proving.html?";
    public final static String getUrl(String url, Context context){
        String newUrl="";
        if(url.contains("?")){
            newUrl=url+getParameter(context);
        }else{
            newUrl=url+"?"+getParameter(context);
        }
        return newUrl;
    }

    public final static String getParameter(Context context){
        return "&client_name="+ Constants.app_name+"&phone_uuid="+ SPUtil.getUserPUUID(context)+"&randcode="+SPUtil.getUserRandCode(context)+"&uuid="+SPUtil.getUserUUID(context);
    }
    /**
     * 包含http://api.shuxin.net/service/jsondata.aspx?
     * appname
     * phone_uuid
     * randcode
     * uuid
     * user_id
     *
     * @param context
     * @return
     */
    public final static String getBaseUrl(Context context){

        return getUrl("http://api.shuxin.net/service/jsondata.aspx?",context);
    }
}

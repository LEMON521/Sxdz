package cn.net.bjsoft.sxdz.utils;

import android.os.Environment;

/**
 * 地址
 * Created by zkagang on 2016/9/14.
 */
public class AddressUtils {

    /*
   * 网络地址
   */
    public static String http_shuxinyun_url="http://api.shuxinyun.com/";

    /*
     * 网络地址
     */
    public static String httpurl="http://api.shuxin.net/service/jsondata.aspx?";

    public static String http_head="http://www.shuxin.net";

    /**
     * 扫一扫请求结果地址
     */
    public static String url_scan_result = "http://biip.shuxin.net/wap/index.html?";

    /*
     * 搜索网络地址
     */
    public static String search_url="http://api.shuxin.net/service/jsondata.aspx?";

    /*
     * 图片缓存地址
     */
    public static final String img_cache_url= Environment.getExternalStorageDirectory().getAbsolutePath()+"/shuxin/imgview/";

    /*
    url附件内容
     */


    /*
    登录
     */
    public static final String login_action="load";
    public static final String login_method="login";

    /*
   验证码
    */
    public static final String code_action="submit";
    public static final String code_method="send_rand_code";
    public static final String val_method="check_rand_code";
    public static final String rev_method="edit_password";

    /*
    共有属性
     */
    public static final String client_name="submit";
    public static final String phone_uuid="send_rand_code";
    public static final String single_code="check_rand_code";
    public static final String uuid="edit_password";

    /*
    初始化数据保存文件
     */
    public static final String init_txt="init.txt";

    /**
     * 总数据
     */
    public static final String initData_txt="initData.txt";

    /**
     * 新闻数据
     */
    public static final String initNewsData_txt="initNewsData.txt";

    /*
    自动定位
     */
    public static final String position_action="submit";
    public static final String position_method="auto_position";


}

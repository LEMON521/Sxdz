package cn.net.bjsoft.sxdz.bean.zdlf.work;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/16.
 */

public class WorkBean implements Serializable {
    public String code = "";
    public WorkDataDao data;

    /**
     * 工作页面的轮播图集合和功能集合
     */
    public class WorkDataDao implements Serializable {

        /**
         * 功能集合
         */
        public ArrayList<FunctionListDao> function_list;
        /**
         * 轮播图集合
         */
        public ArrayList<ScrollListDao> scroll_list;


    }

    public class FunctionListDao implements Serializable {
        public String url = "";//将要打开的地址
        public String image_url = "";//图片地址
        public String name = "";//名称
        public int push_count = 0;//推送数量
        public String type = "";//模块类型
        public String tag = "";//唯一标识符
        public String type_icon = "";//唯一标识符type_icon
        public String type_url = "";
        public String id = "";

    }

    public class ScrollListDao implements Serializable {
        public String id ="";
        public String image_url;//图片地址
        public String file_url;//附件地址
        public String file_text;//附件提示信息
        public String url;//附件提示信息
    }

}

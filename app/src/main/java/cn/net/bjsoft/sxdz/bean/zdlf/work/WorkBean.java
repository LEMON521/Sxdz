package cn.net.bjsoft.sxdz.bean.zdlf.work;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/3/16.
 */

public class WorkBean implements Serializable {
    public boolean result = false;
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
        public String push_count = "";//推送数量
        public String type = "";//模块类型

    }

    public class ScrollListDao implements Serializable {
        public String image_url;//图片地址
        public String file_url;//附件地址
        public String file_text;//附件提示信息
    }

}

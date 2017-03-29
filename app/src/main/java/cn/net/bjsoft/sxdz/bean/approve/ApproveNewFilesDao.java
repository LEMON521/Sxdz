package cn.net.bjsoft.sxdz.bean.approve;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/2/21.
 */

public class ApproveNewFilesDao implements Serializable {
    public String url;//文件路径
    public String uri;
    public String name;//文件名称
    /**
     * 1，添加类型
     * 2，Word类型
     * 3，xecl类型
     * 4，ppt类型
     * 5，图片类型
     * 6，其他类型
     */
    public int tag;//文件类型


}

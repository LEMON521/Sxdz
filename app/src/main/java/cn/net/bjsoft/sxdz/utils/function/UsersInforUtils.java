package cn.net.bjsoft.sxdz.utils.function;

import android.content.Context;

import org.xutils.common.util.LogUtil;

import java.util.HashMap;

import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersAllBean;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersSingleBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by Zrzc on 2017/5/4.
 */

public class UsersInforUtils {
    private static UsersInforUtils inforUtils = null;
    private UsersAllBean usersAllBean;
    private  HashMap<String, UsersSingleBean> usersMap;


    private UsersInforUtils(Context context) {
        usersAllBean = GsonUtil.getUsersAllBean(SPUtil.getUsersAll(context));
        LogUtil.e(SPUtil.getUsersAll(context));
        if (usersMap == null) {
            usersMap = new HashMap<>();
        }

        if (usersAllBean!=null) {
            for (int i = 0; i < usersAllBean.users_all.size(); i++) {
                usersMap.put(usersAllBean.users_all.get(i).id, usersAllBean.users_all.get(i));
            }
        }else {
            MyToast.showShort(context,"没有联系人信息!");
        }

    }

    public static UsersInforUtils getInstance(Context context) {
        System.out.println("-->懒汉式单例模式开始调用公有方法返回实例");
        if (inforUtils == null) {
            System.out.println("-->懒汉式构造函数的实例当前并没有被创建");
            inforUtils = new UsersInforUtils(context);
        } else {
            System.out.println("-->懒汉式构造函数的实例已经被创建");
        }
        System.out.println("-->方法调用结束，返回单例");
        return inforUtils;

    }

    public UsersSingleBean getUserInfo(String id) {

        return usersMap.get(id)==null?new UsersSingleBean():usersMap.get(id);
    }
}

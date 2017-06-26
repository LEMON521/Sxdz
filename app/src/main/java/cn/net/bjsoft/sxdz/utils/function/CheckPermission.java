package cn.net.bjsoft.sxdz.utils.function;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by 靳宁宁 on 2017/4/19.
 */

public class CheckPermission {

    /**
     * 检查拨打电话权限
     * </P>
     * activity和fragmentActivity任选其一
     * </P>
     * @param activity Activity或其子类
     * @param fragmentActivity FragmentActivity或其子类
     * @return
     */
    public static boolean checkCallPhone(Activity activity, FragmentActivity fragmentActivity) {
        PackageManager pm;
        if (activity == null) {
            pm = fragmentActivity.getPackageManager();
        } else if (fragmentActivity == null) {
            pm = activity.getPackageManager();
        } else {
            return false;
        }
        return (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CALL_PHONE", "cn.net.bjsoft.sxdz"));

    }

}

package cn.net.bjsoft.sxdz.bean.app.function.sign;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/18.
 */

public class SignUserBean implements Serializable {
    public String code="";
    public ArrayList<SignUserDataBean> data;
}

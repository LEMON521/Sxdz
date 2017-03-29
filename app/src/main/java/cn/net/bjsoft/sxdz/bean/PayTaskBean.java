package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 总订单
 * Created by zkagang on 2016/9/26.
 */
public class PayTaskBean implements Serializable {
    private String id;
    private String name;
    private String number;
    private String money;
    private boolean check=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private ArrayList<PayTaskItemBean> payTaskItemBeanArrayList=new ArrayList<PayTaskItemBean>();

    public ArrayList<PayTaskItemBean> getPayTaskItemBeanArrayList() {
        return payTaskItemBeanArrayList;
    }

    public void setPayTaskItemBeanArrayList(ArrayList<PayTaskItemBean> payTaskItemBeanArrayList) {
        this.payTaskItemBeanArrayList = payTaskItemBeanArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "PayTaskBean{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", money='" + money + '\'' +
                ", check=" + check +
                ", payTaskItemBeanArrayList=" + payTaskItemBeanArrayList +
                '}';
    }
}

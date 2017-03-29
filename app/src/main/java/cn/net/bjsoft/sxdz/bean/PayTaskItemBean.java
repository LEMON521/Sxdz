package cn.net.bjsoft.sxdz.bean;

import java.io.Serializable;

/**
 * Created by zkagang on 2016/9/26.
 */
public class PayTaskItemBean implements Serializable {
    private String id;
    private String name;
    private String price;
    private String money;
    private String num;
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "PayTaskItemBean{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", money='" + money + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}

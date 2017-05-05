package cn.net.bjsoft.sxdz.bean.app.push_json_bean;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/5/5.
 */

public class PostJsonDataBean implements Serializable {
    private StringBuilder stringBuilder;
    public String task_id = "";

    @Override
    public String toString() {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("data:");
        stringBuilder.append("{");
        stringBuilder.append("\"task_id\":" + "\"" + task_id + "\"");//最后一个元素不要有,号
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
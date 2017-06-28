package cn.net.bjsoft.sxdz.bean.app.push_json_bean;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/5/5.
 */

public class PostJsonDataBean implements Serializable {
    private StringBuilder stringBuilder;
    public String task_id = "";
    public String source_id = "";

    public String task_type = "";
    public String task_priority = "";
    public String start_time = "";
    public String limit_time = "";

    @Override
    public String toString() {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("\"data\":");
        stringBuilder.append("{");
        stringBuilder.append("\"source_id\":" + "\"" + source_id + "\",");//最后一个元素不要有,号
        stringBuilder.append("\"task_id\":" + "\"" + task_id + "\",");
        stringBuilder.append("\"task_type\":" + "\"" + task_type + "\",");
        stringBuilder.append("\"task_priority\":" + "\"" + task_priority + "\",");
        stringBuilder.append("\"start_time\":" + "\"" + start_time + "\",");
        stringBuilder.append("\"limit_time\":" + "\"" + limit_time + "\"");//最后一个元素不要有,号
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
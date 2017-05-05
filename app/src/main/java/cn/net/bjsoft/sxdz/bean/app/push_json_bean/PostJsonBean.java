package cn.net.bjsoft.sxdz.bean.app.push_json_bean;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/5/5.
 */

public class PostJsonBean implements Serializable {
    private StringBuilder sb;
    public String start ="";
    public String limit = "";
    public String[] condition = {};
    public PostJsonDataBean data = new PostJsonDataBean();


    @Override
    public String toString() {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.delete(0, sb.length());
        sb.append("{");
        sb.append("\"start\":" + start + ",");
        sb.append("\"limit\":" + limit + ",");
        sb.append("\"condition\":[");
        for (int i = 0; i < condition.length; i++) {
            if (i == condition.length) {
                sb.append(condition[i] + ",");//最后一个元素不要有,号
            } else {
                sb.append(condition[i] + ",");
            }
        }
        sb.append("],");
        sb.append(data.toString());
        sb.append("}");

        return sb.toString();
    }
}

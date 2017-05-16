package cn.net.bjsoft.sxdz.bean.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class HomepageBean implements Serializable {
    private StringBuilder sb;


    public String icon = "";
    public String icon_default = "";
    public String icon_position = "";
    public String icon_selected = "";
    public String linkto = "";
    public int member = 0;
    public int selected = 0;
    public String tag = "";
    public String text = "";
    public String title = "";
    public String url = "";
    public int visitor = 0;
    public HashMap<String, String> tag_params;

    @Override
    public String toString() {
        if (sb == null) {
            sb = new StringBuilder();
        } else if (sb.length() > 0) {
            sb.setLength(0);
        }

        sb.append("{");

        sb.append("\"icon\":\"");
        sb.append(icon);
        sb.append("\",");

        sb.append("\"icon_default\":\"");
        sb.append(icon_default);
        sb.append("\",");

        sb.append("\"icon_position\":\"");
        sb.append(icon_position);
        sb.append("\",");

        sb.append("\"icon_selected\":\"");
        sb.append(icon_selected);
        sb.append("\",");

        sb.append("\"linkto\":\"");
        sb.append(linkto);
        sb.append("\",");

        sb.append("\"member\":");
        sb.append(member);
        sb.append(",");

        sb.append("\"selected\":");
        sb.append(selected);
        sb.append(",");

        sb.append("\"tag\":\"");
        sb.append(tag);
        sb.append("\",");

        sb.append("\"text\":\"");
        sb.append(text);
        sb.append("\",");

        sb.append("\"title\":\"");
        sb.append(title);
        sb.append("\",");

        sb.append("\"url\":\"");
        sb.append(url);
        sb.append("\",");

        sb.append("\"visitor\":");
        sb.append(visitor);
        sb.append("");

        if (tag_params != null) {
            sb.append(",{");

            Iterator iterator = tag_params.entrySet().iterator();
            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                sb.append("\"");
                sb.append(key);
                sb.append("\":\"");
                sb.append(value);
                sb.append("\"");
                if (iterator.hasNext()) {
                    sb.append(",");
                }

            }

            sb.append("}");
        }


        sb.append("}");

        return sb.toString();
    }
}

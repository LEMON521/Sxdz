package cn.net.bjsoft.sxdz.bean.app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/4/19.
 */

public class ToolbarBean implements Serializable {
    public boolean approve = false;
    public boolean bug = false;
    public boolean community = false;
    public boolean crm = false;
    public boolean knowledge = false;
    public boolean message = false;
    public boolean myself = false;
    public ArrayList<Object> payment;
    public boolean proposal = false;
    public boolean scan = false;
    public String scan_to = "";
    public boolean search_by_voice = false;
    public boolean search_by_word = false;
    public boolean search_to = false;
    public boolean shoot = false;
    public boolean signin = false;
    public boolean task = false;
    public boolean train = false;
}

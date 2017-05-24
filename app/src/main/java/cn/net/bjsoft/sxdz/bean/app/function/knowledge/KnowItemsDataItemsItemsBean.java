package cn.net.bjsoft.sxdz.bean.app.function.knowledge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/23.
 */

public class KnowItemsDataItemsItemsBean implements Serializable {

    public String  content="";
    public String  ctime="";
    public String  id="";
    public String  know_id="";
    public String  reply_id="";
    public String  sort_index="";
    public String  title="";
    public String  userid="";

    public ArrayList<KnowItemsDataItemsItemsBean> items;


    public ArrayList<KnowItemsDataItemsReplayBean> knowledge_item;
}

package cn.net.bjsoft.sxdz.bean.app.function.knowledge;

import java.io.Serializable;
import java.util.ArrayList;

import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;

/**
 * Created by Zrzc on 2017/5/23.
 */

public class KnowItemsDataItemsReplayBean implements Serializable {

    public String avatar_url = "";//回复人的头像地址
    public String comment_text = "";//回复内容
    public String name = "";//回复人的名字
    public String time = "";//回复时间.时间戳
    public String reply_to = "";//向谁回复
    public ArrayList<KnowLedgeItemBean.ReplyListDao> reply_list;//回复列表信息
}

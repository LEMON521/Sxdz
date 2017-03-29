package cn.net.bjsoft.sxdz.bean.approve;

import java.io.Serializable;

/**
 * Created by Zrzc on 2017/2/21.
 */

public class
ApproveAgreementDao extends ApproveNewDao implements Serializable{
        public String num = "";//合同编号
        public String date = "";//签约日期
        public String first = "";//甲方名称
        public String first_leading = "";//甲方负责人
        public String second = "";//乙方名称
        public String second_leading = "";//乙方负责人

}

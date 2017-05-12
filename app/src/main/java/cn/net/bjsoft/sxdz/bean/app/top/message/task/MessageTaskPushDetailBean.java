package cn.net.bjsoft.sxdz.bean.app.top.message.task;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zrzc on 2017/5/12.
 */

public class MessageTaskPushDetailBean implements Serializable {

    public String id = "";
    public boolean submit = false;
    public ArrayList<MessageTaskDetailDataUsersBean> users;
    public ArrayList<MessageTaskDetailDataUsersPlanBean> plan;
    public ArrayList<MessageTaskDetailDataFilesBean> files;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"task_id\":\"");
        sb.append(id);
        sb.append("\",");

        sb.append("\"submit\":");
        sb.append(submit);
        sb.append(",");

        sb.append("\"data\":");
        sb.append("{");

        if (plan != null) {
            sb.append("\"plan\":[");
            for (int i = 0; i < plan.size(); i++) {
                sb.append("{");

                sb.append("\"id\":\"");
                sb.append(plan.get(i).id);
                sb.append("\",");

                sb.append("\"title\":\"");
                sb.append(plan.get(i).title);
                sb.append("\",");

                sb.append("\"content\":\"");
                sb.append(plan.get(i).content);
                sb.append("\",");

                sb.append("\"starttime\":\"");
                sb.append(plan.get(i).starttime);
                sb.append("\",");

                sb.append("\"endtime\":\"");
                sb.append(plan.get(i).endtime);
                sb.append("\",");

                sb.append("\"finished\":");
                sb.append(plan.get(i).finished);
                sb.append("");

                if (i == plan.size() - 1) {
                    sb.append("}");
                } else {
                    sb.append("},");
                }

            }


            sb.append("],");
        }


        if (files != null) {
            sb.append("\"files\":[");
            for (int i = 0; i < files.size(); i++) {
                sb.append("{");

                sb.append("\"id\":\"");
                sb.append(files.get(i).id);
                sb.append("\",");

                sb.append("\"title\":\"");
                sb.append(files.get(i).title);
                sb.append("\",");

                sb.append("\"ctime\":\"");
                sb.append(files.get(i).ctime);
                sb.append("\",");

                sb.append("\"url\":\"");
                sb.append(files.get(i).url);
                sb.append("\"");

                if (i == files.size() - 1) {
                    sb.append("}");
                } else {
                    sb.append("},");
                }

            }

            sb.append("],");
        }


        if (users != null) {
            sb.append("\"users\":[");
            for (int i = 0; i < users.size(); i++) {
                sb.append("{");

                sb.append("\"userid\":\"");
                sb.append(users.get(i).userid);
                sb.append("\",");

                sb.append("\"passed\":");
                sb.append(users.get(i).passed);
                sb.append(",");

                sb.append("\"evaluate\":\"");
                sb.append(users.get(i).evaluate);
                sb.append("\"");

                if (i == users.size() - 1) {
                    sb.append("}");
                } else {
                    sb.append("},");
                }

            }

            sb.append("]");
        }


        sb.append("}");


        sb.append("}");
//
//        "task_id": "4841696800672383172",//任务编号
//                "submit": true,//true就是任务发起人对任何执行人进行任务核查，当为true下面users起效 ，否则是任务执行人操作
//                "data": {
//            "plan": [{
//                id:序号，//事项编号
//                title:"",//事项标题
//                        content:"",//事项内容
//                        starttime:"",//计划开始时间
//                        endtime:"",//计划完成时间
//                        finished:true//是否完成
//            }],
//            "files": [{
//                id:"",//附件编号，新上传为空
//                        title:"",//附件名称
//                        ctime:"",//创建时间
//                        url:""//文件地址
//            }],
//            "users": [
//            {
//                "userid": "11006"  //任务执行人编号
//                "passed": true,//true任务完成，false没有完成，重新做
//                    "evaluate": "批示意见",
//            }
//  ]}

        return sb.toString();
    }
}

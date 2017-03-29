package cn.net.bjsoft.sxdz.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class KnowledgeReplyPopupWindow {
    private static PopupWindow mReplayPopupWindow;
    private static BaseFragment fragment;
    private static FragmentActivity activity;
    private static BaseAdapter adapter;

    public static FragmentActivity getActivity() {
        return activity;
    }

    public static void setActivity(FragmentActivity activity) {
        KnowledgeReplyPopupWindow.activity = activity;
    }

    public static BaseFragment getFragment() {
        return fragment;
    }

    public static void setFragment(BaseFragment fragment) {
        KnowledgeReplyPopupWindow.fragment = fragment;
    }

    public static BaseAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(BaseAdapter adapter) {
        KnowledgeReplyPopupWindow.adapter = adapter;
    }


    public static void setmReplayPopupWindow(FragmentActivity activity
            , BaseFragment fragment
            , View view
            , final BaseAdapter adapterParent
            , final BaseAdapter adapterChildren
            , final ArrayList<KnowLedgeItemBean.ReplyListDao> list
            , final KnowLedgeItemBean.ReplyListDao replyListDao) {

        setActivity(activity);
        setFragment(fragment);
        setAdapter(adapter);
        TextView send;
        TextView name;
        final EditText edit;
        View contentView;
        mReplayPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(activity);
        contentView = mLayoutInflater.inflate(R.layout.pop_knowledge_reply, null);
        mReplayPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        send = (TextView) contentView.findViewById(R.id.pop_knowledge_send);
        name = (TextView) contentView.findViewById(R.id.pop_knowledge_name);
        edit = (EditText) contentView.findViewById(R.id.pop_knowledge_edit);

        final String replyName;
        if (replyListDao != null) {
            replyName = replyListDao.name;
        } else {
            replyName = "楼主";
        }
        name.setText("回复 " + replyName + " ");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KnowLedgeItemBean bean = new KnowLedgeItemBean();
                KnowLedgeItemBean.ReplyListDao dao = bean.new ReplyListDao();
                dao.name = "张三";
                dao.avatar_url = "http://www.shuxin.net/api/app_json/wlh.jpg";

                //dao.description = edit.getText().toString().split(replyName + " ")[1];
                dao.comment_text = edit.getText().toString().trim();
                if (dao.comment_text.equals("")){
                    MyToast.showShort(getActivity(),"请输入回复内容!");
                    return;
                }

                dao.reply_to = replyName;
                dao.time = System.currentTimeMillis() + "";

                if (replyListDao == null) {//如果为空,那么久创建一个新的空的评论列表
                    dao.reply_list = new ArrayList<KnowLedgeItemBean.ReplyListDao>();
                }
                list.add(dao);
                if (adapterParent != null)
                    adapterParent.notifyDataSetChanged();
                if (adapterChildren != null)
                    adapterChildren.notifyDataSetChanged();

                /**
                 * 在这里,还应该把数据添加到服务器上
                 */

                mReplayPopupWindow.dismiss();

                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


        ColorDrawable cd = new ColorDrawable(0x000000);
        mReplayPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);

        mReplayPopupWindow.setOutsideTouchable(true);
        mReplayPopupWindow.setFocusable(true);
        //下面两句话是将底部的popupWindow顶到软键盘上面去
        mReplayPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mReplayPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        mReplayPopupWindow.showAtLocation((View) view.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mReplayPopupWindow.update();
        mReplayPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

}

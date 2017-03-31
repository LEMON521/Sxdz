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
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.knowledge.KnowLedgeItemBean;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class KnowledgeReplyPopupWindow_1 implements View.OnClickListener {
    private PopupWindow mReplayPopupWindow;
    private FragmentActivity activity;
    private View view;

    private InputMethodManager imm;

    private TextView send;
    private TextView name;
    private EditText edit;

    private String replyTo;

    // 数据接口
    OnGetData mOnGetData;

    public KnowledgeReplyPopupWindow_1(FragmentActivity activity
            , View view) {
        init(activity, view, "");
    }

    public KnowledgeReplyPopupWindow_1(FragmentActivity activity
            , View view
            , String replyTo) {
        init(activity, view, replyTo);
    }

    private void init(FragmentActivity activity
            , View view
            , String replyTo) {
        this.activity = activity;
        this.view = view;
        this.replyTo = replyTo;
        //软键盘管理器
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        setmReplayPopupWindow();
    }


    public void setmReplayPopupWindow() {
        //弹出软键盘
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        View contentView;
        mReplayPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(activity);
        contentView = mLayoutInflater.inflate(R.layout.pop_knowledge_reply, null);
        mReplayPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        send = (TextView) contentView.findViewById(R.id.pop_knowledge_send);
        name = (TextView) contentView.findViewById(R.id.pop_knowledge_name);
        edit = (EditText) contentView.findViewById(R.id.pop_knowledge_edit);

        name.setText("回复: " + replyTo);

        send.setOnClickListener(this);

        ColorDrawable cd = new ColorDrawable(0x000000);
        mReplayPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);

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
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }

    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(KnowLedgeItemBean.ReplyListDao replyListDao);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        mOnGetData = sd;
    }


    @Override
    public void onClick(View v) {
        KnowLedgeItemBean bean = new KnowLedgeItemBean();
        KnowLedgeItemBean.ReplyListDao dao = bean.new ReplyListDao();


        //dao.description = edit.getText().toString().split(replyName + " ")[1];
        dao.comment_text = edit.getText().toString().trim();
        if (dao.comment_text.equals("")) {
            MyToast.showShort(activity, "请输入回复内容!");
            return;
        }

        dao.name = SPUtil.getUserName(activity);
        dao.avatar_url = SPUtil.getAvatar(activity);
        dao.reply_to = replyTo;
        dao.time = System.currentTimeMillis() + "";


        /**
         * 在这里,还应该把数据添加到服务器上
         */

        mOnGetData.onDataCallBack(dao);

        mReplayPopupWindow.dismiss();

        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

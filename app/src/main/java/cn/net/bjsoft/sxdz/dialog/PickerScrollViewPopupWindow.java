package cn.net.bjsoft.sxdz.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.view.picker_scroll_view.PickerScrollView;
import cn.net.bjsoft.sxdz.view.picker_scroll_view.Pickers;

/**
 * Created by Zrzc on 2017/4/10.
 */

public class PickerScrollViewPopupWindow implements View.OnClickListener {
    private FragmentActivity mActivity;
    private View view;
    private ArrayList<String> itemsDataList;

    private PopupWindow mPickerScrollViewPopupWindow;

    private TextView cancel,submit;
    private PickerScrollView picker;

    private List<Pickers> list; // 滚动选择器数据
    private String[] id;
    private String[] name;

    private String selete = "";


    // 数据接口
    OnGetData mOnGetData;

    public PickerScrollViewPopupWindow() {

        //setPickerScrollViewPopupWindow();
        //InitUI();


    }
    private void initData(){
        list = new ArrayList<Pickers>();
        id = new String[] { "1", "2", "3", "4", "5", "6" };
        name = new String[] { "中国银行", "农业银行", "招商银行", "工商银行", "建设银行", "民生银行" };
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], id[i]));
        }

    }

    public void setPickerScrollViewPopupWindow(FragmentActivity activity
            , ArrayList<String> itemsDataList
            , View view) {

        this.mActivity = activity;
        this.view = view;
        //this.cacheItemsDataList = cacheItemsDataList;
        this.itemsDataList = itemsDataList;

        initData();

        View contentView;
        mPickerScrollViewPopupWindow = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        contentView = mLayoutInflater.inflate(R.layout.pop_picker_scroll_view, null);
        mPickerScrollViewPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cancel = (TextView) contentView.findViewById(R.id.pop_picker_cancel);
        submit = (TextView) contentView.findViewById(R.id.pop_picker_submit);
        picker = (PickerScrollView) contentView.findViewById(R.id.pop_picker_pick);

        // 设置数据，默认选择第一条
        picker.setData(list);
        picker.setSelected(0);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        // 滚动选择器选中事件
        picker.setOnSelectListener(new PickerScrollView.onSelectListener() {

            @Override
            public void onSelect(Pickers pickers) {
                LogUtil.e("选择：" + pickers.getShowId() + "--银行："
                        + pickers.getShowConetnt());
                selete = "";
                selete = "选择：" + pickers.getShowId() + "--银行："
                        + pickers.getShowConetnt();
            }
        });


        ColorDrawable cd = new ColorDrawable(0x000000);
        mPickerScrollViewPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);

        mPickerScrollViewPopupWindow.setOutsideTouchable(true);
        mPickerScrollViewPopupWindow.setFocusable(true);
        //下面两句话是将底部的popupWindow顶到软键盘上面去
//        mPickerScrollViewPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        mPickerScrollViewPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////
        mPickerScrollViewPopupWindow.showAtLocation((View) view.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPickerScrollViewPopupWindow.update();
        mPickerScrollViewPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }





    // 数据接口抽象方法
    public interface OnGetData {
        //abstract ArrayList<KnowledgeBean.ItemsDataDao> cacheItemsDataList();

        abstract void onDataCallBack(String string);
    }

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData string) {
        mOnGetData = string;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_picker_cancel:
                mOnGetData.onDataCallBack("");
                break;

            case R.id.pop_picker_submit:
                mOnGetData.onDataCallBack(selete);
                break;


        }
        mPickerScrollViewPopupWindow.dismiss();
    }
}

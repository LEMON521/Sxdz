package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/2/21.
 */

public class WindowEntryView extends LinearLayout /*implements View.OnClickListener*/ {

    private TextView title;

    //栏目1
    private LinearLayout entry_1;
    private TextView tv_1;
    private EditText et_1;
    //栏目2
    private LinearLayout entry_2;
    private TextView tv_2;
    private EditText et_2;
    //栏目3
    private LinearLayout entry_3;
    private TextView tv_3;
    private EditText et_3;
    //栏目4
    private LinearLayout entry_4;
    private TextView tv_4;
    private EditText et_4;

    private TextView confirm;
    private TextView cancle;

    public TextView getConfirm() {
        return confirm;
    }

    public void setConfirm(TextView confirm) {
        this.confirm = confirm;
    }

    public TextView getCancle() {
        return cancle;
    }

    public void setCancle(TextView cancle) {
        this.cancle = cancle;
    }

    private ArrayList<String> titles;

    private ArrayList<String> result;
    private Context context;
    /**
     * 初始化必备参数
     *
     * @param context
     * @param titles    {总标题
     *                ,栏目1标题,栏目1提示
     *                ,栏目2标题,栏目2提示
     *                ,栏目3标题,栏目3提示
     *                ,栏目4标题,栏目4提示}
     *                其中,如果如果不指示栏目标题，则该栏目就将不显示，如果只是栏目标题而不指示提示，则不显示提示
     *                如果不想只是提示信息，那么请传递  ""  字符串
     */
    public WindowEntryView(Context context, ArrayList<String> titles) {
        super(context);
        this.titles = titles;
        this.context = context;
        initView(context, titles);

    }
//
//    private WindowEntryView(Context context) {
//        super(context);
//        initView(context);
//    }
//
//    private WindowEntryView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView(context);
//    }
//
//    private WindowEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView(context);
//    }

    private void initView(Context context, ArrayList<String> strs) {
        View.inflate(context, R.layout.window_entry, WindowEntryView.this);
        title = (TextView) findViewById(R.id.window_title);
        entry_1 = (LinearLayout) findViewById(R.id.window_ll_entry_1);
        tv_1 = (TextView) findViewById(R.id.window_tv_1);
        et_1 = (EditText) findViewById(R.id.window_et_1);
        entry_2 = (LinearLayout) findViewById(R.id.window_ll_entry_2);
        tv_2 = (TextView) findViewById(R.id.window_tv_2);
        et_2 = (EditText) findViewById(R.id.window_et_2);
        entry_3 = (LinearLayout) findViewById(R.id.window_ll_entry_3);
        tv_3 = (TextView) findViewById(R.id.window_tv_3);
        et_3 = (EditText) findViewById(R.id.window_et_3);
        entry_4 = (LinearLayout) findViewById(R.id.window_ll_entry_4);
        tv_4 = (TextView) findViewById(R.id.window_tv_4);
        et_4 = (EditText) findViewById(R.id.window_et_4);
        confirm = (TextView) findViewById(R.id.window_confirm);
        cancle = (TextView) findViewById(R.id.window_cancle);


        if (strs ==null){//为null的时候

        }

        if (strs.size()==0){//长度为0的时候

        }
        entry_1.setVisibility(GONE);
        entry_2.setVisibility(GONE);
        entry_3.setVisibility(GONE);
        entry_4.setVisibility(GONE);
        for (int i = 0;i<strs.size();i++){//精益求精的算法

            switch (i){
                case 0:
                    title.setText(strs.get(i));//等同于title.setText(strs.get(0))
                    break;
                case 1:
                    entry_1.setVisibility(VISIBLE);
                    tv_1.setText(strs.get(i));//等同于tv_1.setText(strs.get(1))
                    break;
                case 2:

                    et_1.setHint(strs.get(i));//等同于et_1.setHint(strs.get(2))
                    break;
                case 3:
                    if (strs.get(i).equals("开始时间")){
                        et_2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    if (strs.get(i).equals("单价")){
                        et_2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    entry_2.setVisibility(VISIBLE);
                    tv_2.setText(strs.get(i));
                    break;
                case 4:
                    et_2.setHint(strs.get(i));
                    break;
                case 5:
                    if (strs.get(i).equals("结束时间")){
                        et_3.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    if (strs.get(i).equals("数量")){
                        et_3.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    entry_3.setVisibility(VISIBLE);
                    tv_3.setText(strs.get(i));
                    break;
                case 6:
                    et_3.setHint(strs.get(i));
                    break;
                case 7:
                    if (strs.get(i).equals("金额")||strs.get(i).equals("请假天数")){
                        et_4.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    entry_4.setVisibility(VISIBLE);
                    tv_4.setText(strs.get(i));
                    break;
                case 8:

                    et_4.setHint(strs.get(i));
                    break;
            }
        }


//
//
//        if (strs.size()>0){
//            title.setText(strs.get(0));
//        }
//        if (strs.size()>0&&strs.size()<2){
//
//            entry_1.setVisibility(GONE);
//            entry_2.setVisibility(GONE);
//            entry_3.setVisibility(GONE);
//            entry_4.setVisibility(GONE);
//        }
//
//        if (strs.size()>1&&strs.size()<4){
//            tv_1.setText(strs.get(1));
//            if (strs.size()<3){
//                et_1.setHint("");
//            }else {
//                et_1.setHint(strs.get(2));
//            }
//            entry_2.setVisibility(GONE);
//            entry_3.setVisibility(GONE);
//            entry_4.setVisibility(GONE);
//        }
//        if (strs.size()>3&&strs.size()<6){
//            tv_1.setText(strs.get(1));
//            et_1.setHint(strs.get(2));
//            tv_2.setText(strs.get(3));
//            if (strs.size()<5){
//                et_2.setHint("");
//            }else {
//                et_2.setHint(strs.get(4));
//            }
//            entry_3.setVisibility(GONE);
//            entry_4.setVisibility(GONE);
//        }
//
//        if (strs.size()>5&&strs.size()<8){
//            tv_1.setText(strs.get(1));
//            et_1.setHint(strs.get(2));
//            tv_2.setText(strs.get(3));
//            et_2.setHint(strs.get(4));
//            tv_3.setText(strs.get(5));
//            if (strs.size()<7){
//                et_3.setHint("");
//            }else {
//                et_3.setHint(strs.get(6));
//            }
//            entry_4.setVisibility(GONE);
//        }
//
//        if (strs.size()>7&&strs.size()<10){
//            tv_1.setText(strs.get(1));
//            et_1.setHint(strs.get(2));
//            tv_2.setText(strs.get(3));
//            et_2.setHint(strs.get(4));
//            tv_3.setText(strs.get(5));
//            et_3.setHint(strs.get(6));
//            tv_4.setText(strs.get(7));
//            if (strs.size()<9){
//                et_4.setHint("");
//            }else {
//                et_4.setHint(strs.get(8));
//            }
//        }
//
//        if (strs.size()>9){
//
//        }

//        confirm.setOnClickListener(this);
//        cancle.setOnClickListener(this);
    }


//    @Override
//    public void onClick(View v) {
//        result = new ArrayList<>();
//        switch (v.getId()){
//            case R.id.window_confirm:
//                result.add(et_1.getText().toString().trim());
//                result.add(et_2.getText().toString().trim());
//                result.add(et_3.getText().toString().trim());
//                result.add(et_4.getText().toString().trim());
//                break;
//
//            case R.id.window_cancle:
//
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 获取输入框中的内容，返回长度与传进来的List的长度成正相关
     * @return
     */
    public ArrayList<String> submit(){
        result = new ArrayList<>();
        String result_1 = et_1.getText().toString().trim();
        String result_2 = et_2.getText().toString().trim();
        String result_3 = et_3.getText().toString().trim();
        String result_4 = et_4.getText().toString().trim();
        if (entry_1.getVisibility() == VISIBLE){
            if (result_1.equals("")){
                MyToast.showShort(context,tv_1.getText().toString()+"信息不能为空！");
            }else {
                result.add(result_1);
            }
        }

        if (entry_2.getVisibility() == VISIBLE){
            if (result_2.equals("")){
                MyToast.showShort(context,tv_2.getText().toString()+"信息不能为空！");
            }else {
                result.add(result_2);
            }
        }

        if (entry_3.getVisibility() == VISIBLE){
            if (result_3.equals("")){
                MyToast.showShort(context,tv_3.getText().toString()+"信息不能为空！");
            }else {
                result.add(result_3);
            }
        }

        if (entry_4.getVisibility() == VISIBLE){
            if (result_4.equals("")){
                MyToast.showShort(context,tv_4.getText().toString()+"信息不能为空！");
            }else {
                result.add(result_4);
            }
        }
        return result;
    }

    /**
     * 将输入框置为空
     */
    public void clear(){
        et_1.setText("");
        et_2.setText("");
        et_3.setText("");
        et_4.setText("");
    }
}

package cn.net.bjsoft.sxdz.fragment.bartop.search;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.search.SearchResultActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Zrzc on 2017/1/17.
 */
@ContentView(R.layout.fragment_search_text)
public class TopSearchTextFragment extends BaseFragment {
    @ViewInject(R.id.search_back)
    private ImageView back;

    @ViewInject(R.id.search_go)
    private TextView title;


    @ViewInject(R.id.search_ll)
    private LinearLayout search_ll;
    @ViewInject(R.id.search_edittext)
    private EditText inText;

//    @ViewInject(R.id.empty_text)
//    private TextView text;

    private String content = "";

    //震动管理器
    private Vibrator vibrator;

    @Override
    public void initData() {

        content = getArguments().get("content").toString();

        //获取到震动服务
        vibrator = (Vibrator) mActivity.getSystemService(VIBRATOR_SERVICE);


    }

    @Override
    public void onResume() {
        super.onResume();
        initEditor();
        inText.setText(content);
    }

    /**
     * 输入框相关的初始化
     */
    private void initEditor() {
        //实时查询(在我们输入的时候,动态的进行查询)--所以我们要将事件添加到输入框改变的时候
        inText.addTextChangedListener(new TextWatcher() {

            //输入框改变之前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //输入框改变的时候
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e(TAG,"s:"+s+"\nstart:"+start+"\nbefore:"+before+"\ncount:"+count);
                //queryAddress(s.toString().trim());
                MyToast.showShort(mActivity,inText.getText().toString());
            }

            //输入框改变之后的时候
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Event(value = {R.id.search_back, R.id.search_edittext, R.id.search_go})
    private void searchOnclick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                getActivity().finish();

                break;
            case R.id.search_edittext:
                //MyToast.showShort(mActivity, "下拉缓冲");

                break;
            case R.id.search_go:


                if (!(inText.getText().toString().equals(""))) {
                    LogUtil.e("长度为"+inText.getText().toString().length());
                   // MyToast.showShort(mActivity, inText.getText().toString().length());
                    searchGo(inText.getText().toString().trim());
                } else {
                    //频率:     震动,停止,震动,停止,震动,停止
                    long[] pattern = {500,500,0,0,0,0};
                    vibrator.vibrate(pattern,-1);
                    MyToast.showShort(mActivity, "请输入搜索内容");

                    //TODO 为控件添加动画
                    Animation etAnimation = AnimationUtils.loadAnimation(mActivity,R.anim.shake_x);
                    search_ll.startAnimation(etAnimation);
                }
                break;
        }
    }

    /**
     * 搜索全局的方法
     */
    private void searchGo(String contant) {

        //如果输入法在窗口上已经显示，则隐藏，反之则显示
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        //MyToast.showShort(mActivity, "搜索全局");
        Intent intent = new Intent(mActivity, SearchResultActivity.class);
    }



}

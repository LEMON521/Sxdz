package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;

/**
 * Created by 靳宁宁 on 2017/2/6.
 */

public class BottomIconView extends LinearLayout {

    private Context context;
    private AttributeSet attrs;
    private int defStyleAttr;
    private View view;
    private String tag;

    private String selectIcon;
    private String defaultIcon;

    private ImageOptions mImageOptions;

//    @ViewInject(R.id.view_bottom_vertical)
    private LinearLayout vertical;//竖直
//    @ViewInject(R.id.view_bottom_vertical_fram_top)
    private FrameLayout topFram;//竖直
//    @ViewInject(R.id.view_bottom_vertical_text_top)
    private TextView topText;
//    @ViewInject(R.id.view_bottom_vertical)
    private ImageView topIv;
//    @ViewInject(R.id.view_bottom_vertical_fram_bottom)
    private FrameLayout bottomFram;//竖直
//    @ViewInject(R.id.view_bottom_vertical_text_bottom)
    private TextView bottomText;
//    @ViewInject(R.id.view_bottom_vertical)
    private ImageView bottomIv;




//    @ViewInject(R.id.view_bottom_horizontal)
    private LinearLayout horizontal;//水平
//    @ViewInject(R.id.view_bottom_horizontal_fram_left)
    private FrameLayout leftFram;//竖直
//    @ViewInject(R.id.view_bottom_horizontal_text_left)
    private TextView leftText;
//    @ViewInject(R.id.view_bottom_horizontal_iv_left)
    private ImageView leftIv;
//    @ViewInject(R.id.view_bottom_horizontal_fram_right)
    private FrameLayout rightFram;//竖直
//    @ViewInject(R.id.view_bottom_horizontal_text_right)
    private TextView rightText;
//    @ViewInject(R.id.view_bottom_horizontal_iv_right)
    private ImageView rightIv;

    public BottomIconView(Context context) {
        super(context);
        this.context = context;
        findView();
        initData();
    }

    public BottomIconView(Context context,String tag) {
        super(context);
        this.context = context;
        setTag(tag);
        findView();
        initData();
    }

    public BottomIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        findView();
        initData();


    }

    public BottomIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        //view = LayoutInflater.from(context).inflate(R.layout.view_bottom_icon, null);
        findView();
        initData();
    }


//
//    /**
//     * @param mode          显示方式
//     * @param topText       顶部文字
//     * @param topSelect     顶部选中图片地址
//     * @param topDefault    顶部非选中图片地址
//     * @param bottomText    底部文字
//     * @param bottomSelect  底部选中图片地址
//     * @param bottomDefault 底部非选中图片地址
//     * @param leftText      左部文字
//     * @param leftSelect    左部选中图片地址
//     * @param leftDefault   左部选中图片地址
//     * @param rightText     右部文字
//     * @param rightSelect   右部选中图片地址
//     * @param rightDefault  右部选中图片地址
//     */
//    public void setModeView(String mode,
//                            String topText, String topSelect, String topDefault,
//                            String bottomText, String bottomSelect, String bottomDefault,
//                            String leftText, String leftSelect, String leftDefault,
//                            String rightText, String rightSelect, String rightDefault) {
//
//
//
//    }

    private void findView(){
        view = LayoutInflater.from(context).inflate(R.layout.view_bottom_icon,this,true);

        vertical = (LinearLayout) view.findViewById(R.id.view_bottom_vertical);
        topFram = (FrameLayout) view.findViewById(R.id.view_bottom_vertical_fram_top);
        topText = (TextView) view.findViewById(R.id.view_bottom_vertical_text_top);
        topIv = (ImageView) view.findViewById(R.id.view_bottom_vertical_iv_top);
        bottomFram = (FrameLayout) view.findViewById(R.id.view_bottom_vertical_fram_bottom);
        bottomText = (TextView) view.findViewById(R.id.view_bottom_vertical_text_bottom);
        bottomIv = (ImageView) view.findViewById(R.id.view_bottom_vertical_iv_bottom);

        horizontal = (LinearLayout) view.findViewById(R.id.view_bottom_horizontal);
        leftFram = (FrameLayout) view.findViewById(R.id.view_bottom_horizontal_fram_left);
        leftText = (TextView) view.findViewById(R.id.view_bottom_horizontal_text_left);
        leftIv = (ImageView) view.findViewById(R.id.view_bottom_horizontal_iv_left);
        rightFram = (FrameLayout) view.findViewById(R.id.view_bottom_horizontal_fram_right);
        rightText = (TextView) view.findViewById(R.id.view_bottom_horizontal_text_right);
        rightIv = (ImageView) view.findViewById(R.id.view_bottom_horizontal_iv_right);

    }

    private void initData(){
        vertical.setVisibility(GONE);
        topFram.setVisibility(GONE);
        topText.setVisibility(GONE);
        topIv.setVisibility(GONE);
        bottomFram.setVisibility(GONE);
        bottomText.setVisibility(GONE);
        bottomIv.setVisibility(GONE);

        horizontal.setVisibility(GONE);
        leftFram.setVisibility(GONE);
        leftText.setVisibility(GONE);
        leftIv.setVisibility(GONE);
        rightFram.setVisibility(GONE);
        rightText.setVisibility(GONE);
        rightIv.setVisibility(GONE);

        mImageOptions = new ImageOptions.Builder().setUseMemCache(true).build();
    }

    /**
     * 未点击的时候显示样式
     */
    public void setDefaultShow(){
        x.image().bind(topIv,defaultIcon,mImageOptions);
        x.image().bind(bottomIv,defaultIcon,mImageOptions);
        x.image().bind(leftIv,defaultIcon,mImageOptions);
        x.image().bind(rightIv,defaultIcon,mImageOptions);

        topText.setTextColor(Color.rgb(105,105,105));
        bottomText.setTextColor(Color.rgb(105,105,105));
        leftText.setTextColor(Color.rgb(105,105,105));
        rightText.setTextColor(Color.rgb(105,105,105));
    }

    /**
     * 点击的时候显示的样式
     */
    public void setClickShow(){
        LogUtil.e("之前点击了图片！！！！"+defaultIcon);
        x.image().bind(topIv,selectIcon,mImageOptions);
        x.image().bind(bottomIv,selectIcon,mImageOptions);
        x.image().bind(leftIv,selectIcon,mImageOptions);
        x.image().bind(rightIv,selectIcon,mImageOptions);

        topText.setTextColor(Color.rgb(0,93,209));
        bottomText.setTextColor(Color.rgb(0,93,209));
        leftText.setTextColor(Color.rgb(0,93,209));
        rightText.setTextColor(Color.rgb(0,93,209));
        LogUtil.e("之后点击了图片！！！！"+selectIcon);

    }

    public void setModeView(String mode, String text, String selectIcon, String defaultIcon) {
        this.selectIcon = selectIcon;
        this.defaultIcon = defaultIcon;


        //上下排列
        if (mode.equals("bottom")||mode.equals("top")) {
            vertical.setVisibility(VISIBLE);
            horizontal.setVisibility(GONE);

            //图片在上
            if (mode.equals("top")){
                if(defaultIcon.equals("")){
                    topFram.setVisibility(GONE);
                }else {
                    topFram.setVisibility(VISIBLE);
                    topText.setVisibility(GONE);
                    topIv.setVisibility(VISIBLE);
                    x.image().bind(topIv,selectIcon,mImageOptions);
                    x.image().bind(topIv,defaultIcon,mImageOptions);
                }
                bottomFram.setVisibility(VISIBLE);
                bottomIv.setVisibility(GONE);
                bottomText.setVisibility(VISIBLE);
                bottomText.setText(text);
            }

            //图片在下
            if (mode.equals("bottom")){
                if(defaultIcon.equals("")){
                    bottomFram.setVisibility(GONE);
                }else {
                    bottomFram.setVisibility(VISIBLE);
                    bottomText.setVisibility(GONE);
                    bottomIv.setVisibility(VISIBLE);
                    x.image().bind(bottomIv,selectIcon,mImageOptions);
                    x.image().bind(bottomIv,defaultIcon,mImageOptions);
                }
                topFram.setVisibility(VISIBLE);
                topIv.setVisibility(GONE);
                topText.setVisibility(VISIBLE);
                topText.setText(text);
            }
        }

        //左右排列

        if (mode.equals("left")||mode.equals("right")) {
            horizontal.setVisibility(VISIBLE);
            vertical.setVisibility(GONE);

            //图片在左
            if (mode.equals("left")){
                if(defaultIcon.equals("")){
                    leftFram.setVisibility(GONE);
                }else {
                    leftFram.setVisibility(VISIBLE);
                    leftText.setVisibility(GONE);
                    leftIv.setVisibility(VISIBLE);
                    x.image().bind(leftIv,selectIcon,mImageOptions);
                    x.image().bind(leftIv,defaultIcon,mImageOptions);
                }
                rightFram.setVisibility(VISIBLE);
                rightIv.setVisibility(GONE);
                rightText.setVisibility(VISIBLE);
                rightText.setText(text);
            }

            //图片在右
            if (mode.equals("right")){
                if(defaultIcon.equals("")){
                    rightFram.setVisibility(GONE);
                }else {
                    rightFram.setVisibility(VISIBLE);
                    rightText.setVisibility(GONE);
                    rightIv.setVisibility(VISIBLE);
                    x.image().bind(rightIv,selectIcon,mImageOptions);
                    x.image().bind(rightIv,defaultIcon,mImageOptions);
                }
                leftFram.setVisibility(VISIBLE);
                leftIv.setVisibility(GONE);
                leftText.setVisibility(VISIBLE);
                leftText.setText(text);
            }
        }
    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        LogUtil.e("之前点击了图片！！！！");
//        super.setOnClickListener(l);
//        LogUtil.e("之后点击了图片！！！！");
//        setDefaultShow();
//        setClickShow();
//    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}

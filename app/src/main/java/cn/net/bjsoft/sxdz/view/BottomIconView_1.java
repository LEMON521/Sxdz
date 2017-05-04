package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.net.bjsoft.sxdz.R;

/**
 * Created by 靳宁宁 on 2017/2/6.
 */

public class BottomIconView_1 extends LinearLayout {

    private Context context;
    private AttributeSet attrs;
    private int defStyleAttr;
    private View view;
    private String tag = "";

    private TextView num;

    private String mode = "";
    private String selectIcon = "";
    private String defaultIcon = "";

    private ImageOptions mImageOptions;

    private LinearLayout vertical_1;//竖直
    private TextView topText;
    private ImageView topIv;
    private LinearLayout vertical_2;//竖直
    private TextView bottomText;
    private ImageView bottomIv;


    private LinearLayout horizontal_1;//水平
    private TextView leftText;
    private ImageView leftIv;
    private LinearLayout horizontal_2;//水平
    private TextView rightText;
    private ImageView rightIv;

    public BottomIconView_1(Context context) {
        super(context);
        this.context = context;
        findView();
        initData();
    }

//    public BottomIconView_1(Context context,String mode,String tag) {
//        super(context);
//        this.context = context;
//        findView();
//        initData();
//    }


    public BottomIconView_1(Context context, String tag) {
        super(context);
        this.context = context;
        setTag(tag);
        findView();
        initData();
    }

    public BottomIconView_1(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        findView();
        initData();
    }

    public BottomIconView_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        //view = LayoutInflater.from(context).inflate(R.layout.view_bottom_icon, null);
        findView();
        initData();
    }

    private void findView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_bottom_icon_1, this, true);

        num = (TextView) view.findViewById(R.id.view_bottom_num);

        vertical_1 = (LinearLayout) view.findViewById(R.id.view_bottom_vertical_tv_iv);
        topText = (TextView) view.findViewById(R.id.view_bottom_vertical_text_top);
        bottomIv = (ImageView) view.findViewById(R.id.view_bottom_vertical_iv_bottom);
        vertical_2 = (LinearLayout) view.findViewById(R.id.view_bottom_vertical_iv_tv);
        bottomText = (TextView) view.findViewById(R.id.view_bottom_vertical_text_bottom);
        topIv = (ImageView) view.findViewById(R.id.view_bottom_vertical_iv_top);

        horizontal_1 = (LinearLayout) view.findViewById(R.id.view_bottom_horizontal_tv_iv);
        leftText = (TextView) view.findViewById(R.id.view_bottom_horizontal_text_left);
        rightIv = (ImageView) view.findViewById(R.id.view_bottom_horizontal_iv_right);
        horizontal_2 = (LinearLayout) view.findViewById(R.id.view_bottom_horizontal_iv_tv);
        rightText = (TextView) view.findViewById(R.id.view_bottom_horizontal_text_right);
        leftIv = (ImageView) view.findViewById(R.id.view_bottom_horizontal_iv_left);

    }

    private void initData() {
        vertical_1.setVisibility(GONE);
        topText.setVisibility(GONE);
        topIv.setVisibility(GONE);
        vertical_2.setVisibility(GONE);
        bottomText.setVisibility(GONE);
        bottomIv.setVisibility(GONE);

        horizontal_1.setVisibility(GONE);
        leftText.setVisibility(GONE);
        leftIv.setVisibility(GONE);
        horizontal_2.setVisibility(GONE);
        rightText.setVisibility(GONE);
        rightIv.setVisibility(GONE);

        //       x.image().clearCacheFiles();
        //       x.image().clearMemCache();
        mImageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.application_zdlf_loding) //以资源id设置加载失败的动画
                .setLoadingDrawableId(R.mipmap.application_zdlf_loding)
                .setUseMemCache(true)
                .build();

    }

    /**
     * 未点击的时候显示样式
     * 在setModeView方法之后调用
     */
    public void setDefaultShow() {
        //LogUtil.e(mode+"设置默认图标，地址为===" + defaultIcon);
        setImage(defaultIcon);
        topText.setTextColor(Color.rgb(105, 105, 105));//rgb:#696969
        bottomText.setTextColor(Color.rgb(105, 105, 105));
        leftText.setTextColor(Color.rgb(105, 105, 105));
        rightText.setTextColor(Color.rgb(105, 105, 105));
    }

    /**
     * 点击的时候显示的样式
     * 在setModeView方法之后调用
     */
    public void setClickShow() {
        //LogUtil.e("设置显示图标，地址为===" + selectIcon);
        setImage(selectIcon);
        topText.setTextColor(Color.rgb(0, 93, 209));//rgb:#005dd1
        bottomText.setTextColor(Color.rgb(0, 93, 209));
        leftText.setTextColor(Color.rgb(0, 93, 209));
        rightText.setTextColor(Color.rgb(0, 93, 209));

    }

    public void setModeView(String mode, String text, String selectIcon, String defaultIcon) {
        this.selectIcon = selectIcon;
        this.defaultIcon = defaultIcon;
        this.mode = mode;

        //没有指定排列方式
        if (mode.equals("")) {
            vertical_2.setVisibility(VISIBLE);
            vertical_1.setVisibility(GONE);
            horizontal_1.setVisibility(GONE);
            horizontal_2.setVisibility(GONE);
            if (defaultIcon.equals("")) {
                topIv.setVisibility(GONE);
            } else {
                topIv.setVisibility(VISIBLE);
                x.image().bind(topIv, selectIcon, mImageOptions);
                x.image().bind(topIv, defaultIcon, mImageOptions);
            }
            bottomText.setVisibility(VISIBLE);
            bottomText.setText(text);
        }

        //上下排列
        //图片在上
        if (mode.equals("top")) {
            vertical_2.setVisibility(VISIBLE);
            vertical_1.setVisibility(GONE);
            horizontal_1.setVisibility(GONE);
            horizontal_2.setVisibility(GONE);
            if (defaultIcon.equals("")) {
                topIv.setVisibility(GONE);
            } else {
                topIv.setVisibility(VISIBLE);
                x.image().bind(topIv, selectIcon, mImageOptions);
                x.image().bind(topIv, defaultIcon, mImageOptions);
            }
            bottomText.setVisibility(VISIBLE);
            bottomText.setText(text);
        }

        //图片在下
        if (mode.equals("bottom")) {
            vertical_1.setVisibility(VISIBLE);
            vertical_2.setVisibility(GONE);
            horizontal_1.setVisibility(GONE);
            horizontal_2.setVisibility(GONE);
            if (defaultIcon.equals("")) {
                bottomIv.setVisibility(GONE);
            } else {
                bottomIv.setVisibility(VISIBLE);
                x.image().bind(bottomIv, selectIcon, mImageOptions);
                x.image().bind(bottomIv, defaultIcon, mImageOptions);
            }
            topText.setVisibility(VISIBLE);
            topText.setText(text);
        }


        //左右排列

        //图片在左
        if (mode.equals("left")) {
            vertical_1.setVisibility(GONE);
            vertical_2.setVisibility(GONE);
            horizontal_1.setVisibility(GONE);
            horizontal_2.setVisibility(VISIBLE);
            if (defaultIcon.equals("")) {
                leftIv.setVisibility(GONE);
            } else {
                leftIv.setVisibility(VISIBLE);
                x.image().bind(leftIv, selectIcon, mImageOptions);
                x.image().bind(leftIv, defaultIcon, mImageOptions);
            }
            rightText.setVisibility(VISIBLE);
            rightText.setText(text);
        }

        //图片在右
        if (mode.equals("right")) {
            vertical_1.setVisibility(GONE);
            vertical_2.setVisibility(GONE);
            horizontal_2.setVisibility(GONE);
            horizontal_1.setVisibility(VISIBLE);
            if (defaultIcon.equals("")) {
                rightIv.setVisibility(GONE);
            } else {
                rightIv.setVisibility(VISIBLE);
                x.image().bind(rightIv, selectIcon, mImageOptions);
                x.image().bind(rightIv, defaultIcon, mImageOptions);
            }
            leftText.setVisibility(VISIBLE);
            leftText.setText(text);
        }

    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 设置图片，此方法在setModeView方法之后调用
     *
     * @param url 网络图片地址
     */
    public void setImage(String url) {
        if (mode.equals("")) {
            mode = "top";
        }
        if (mode.equals("top")) {
            x.image().bind(topIv, url, mImageOptions);
        }
        if (mode.equals("bottom")) {
            x.image().bind(bottomIv, url, mImageOptions);
        }
        if (mode.equals("left")) {
            x.image().bind(leftIv, url, mImageOptions);
        }
        if (mode.equals("right")) {
            x.image().bind(rightIv, url, mImageOptions);
        }

    }

    public void setPushCountNum(int number){
        if (number>0) {
            num.setVisibility(VISIBLE);
            num.setText(number+"");
        }else {
            num.setVisibility(INVISIBLE);
        }

    }
}

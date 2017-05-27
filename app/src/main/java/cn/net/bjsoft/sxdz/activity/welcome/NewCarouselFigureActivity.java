package cn.net.bjsoft.sxdz.activity.welcome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.MainActivity;
import cn.net.bjsoft.sxdz.activity.login.LoginActivity;
import cn.net.bjsoft.sxdz.adapter.CarouselPagerAdapter;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.LoadersBean;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;

/**
 * 轮播图展示
 */
public class NewCarouselFigureActivity extends BaseActivity {

    @ViewInject(R.id.viewpager)
    ViewPager viewpager;
    @ViewInject(R.id.taste)
    Button taste;
    @ViewInject(R.id.dotImg)
    ImageView dotImg;

    /*
    轮播图
     */

    private ArrayList<LoadersBean> imgStr = new ArrayList<LoadersBean>();

    /*
    tab
     */
    private AppBean datasBean;
    private String json;
    //private ArrayList<HomepageBean> tablist = new ArrayList<HomepageBean>();

    private CarouselPagerAdapter myViewPagerAdapter;

    private String zhuce;

    private boolean /*islogin,*/ authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_figure);
        x.view().inject(this);
        json = getIntent().getStringExtra("json");
        datasBean = GsonUtil.getAppBean(json);
        if (getIntent() != null) {
            imgStr = datasBean.loaders;
//            tablist = datasBean.homepage;
//            islogin = datasBean.user.logined;
            authentication = datasBean.authentication;
        }
        if (!imgStr.get(0).jumpto.isEmpty()) {
            taste.setText(imgStr.get(0).jumpto);
            taste.setVisibility(View.VISIBLE);
        } else {
            taste.setVisibility(View.GONE);
        }
        setLunbo();
    }


    /**
     * 轮播图
     */
    public void setLunbo() {
        myViewPagerAdapter = new CarouselPagerAdapter(this, imgStr);
        viewpager.setAdapter(myViewPagerAdapter);
        int i = 0;
        if (imgStr.size() != 0 && imgStr != null) {
            i = imgStr.size();
        } else {
            i = 0;
        }
        if (i > 0) {
            setDotView(0);
            viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float f, int j) {
                }

                @Override
                public void onPageSelected(int position) {
                    setDotView(position);
                    if (!imgStr.get(position).jumpto.isEmpty()) {
                        taste.setText(imgStr.get(position).jumpto);
                        taste.setVisibility(View.VISIBLE);
                    } else {
                        taste.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
        }
    }

    /**
     * 设置位点
     *
     * @param position
     */
    private void setDotView(int position) {
        int count = 0;
        if (imgStr != null & imgStr.size() != 0) {
            count = imgStr.size();
        } else {
            count = 0;
        }


        Bitmap src1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.page_on);
        Bitmap src2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.page_off);

        int w = src1.getWidth();
        int h = src1.getHeight();

        if (count > 0) {
            int s = 10;
            Bitmap newb = Bitmap.createBitmap((w + s) * count, h, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(newb);

            for (int i = 0; i < count; i++) {
                if (i == position) {
                    cv.drawBitmap(src1, (w + s) * i, 0, null);
                } else {
                    cv.drawBitmap(src2, (w + s) * i, 0, null);
                }
            }
            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
            dotImg.setImageBitmap(newb);
        }
    }

    @Event(value = R.id.taste)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.taste:
                if (!TextUtils.isEmpty(SPUtil.getToken(this))) {
                    Intent i3 = new Intent(this, NewInitInfoActivity.class);
                    i3.putExtra("json", json);
                    startActivity(i3);
                } else {
                    if (authentication) {
                        Intent i = new Intent(this, LoginActivity.class);
                        i.putExtra("json", json);
                        startActivity(i);
                    } else {
                        Intent i3 = new Intent(this, NewInitInfoActivity.class);
                        i3.putExtra("json", json);
                        startActivity(i3);
                    }
                }
                finish();
                break;
        }
    }
}

package cn.net.bjsoft.sxdz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.welcome.LinkToActivity;
import cn.net.bjsoft.sxdz.bean.DatasBean;

/**
 * 轮播图
 * Created by zkagang on 2016/9/14.
 */
public class CarouselPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<DatasBean.LoadersDao> imgStr=new ArrayList<DatasBean.LoadersDao>();//如果图片是从网上获取
    private ArrayList<View> mViewArrayList = new ArrayList<View>();

    public CarouselPagerAdapter(Context mContext, ArrayList<DatasBean.LoadersDao> imgStr) {
        this.mContext = mContext;
        if (imgStr!=null&&imgStr.size()!=0){
            for (int i = 0; i < imgStr.size(); i++) {
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View itemview = inflater.inflate(R.layout.viewpager_item, null);
                ImageView img= (ImageView) itemview.findViewById(R.id.item_img);
                x.image().bind(img,imgStr.get(i).imgurl);
                mViewArrayList.add(itemview);
                this.imgStr=imgStr;
            }

        }

    }


    @Override
    public int getCount() {
        if (imgStr.size()!=0&&imgStr!=null){
            return imgStr.size();
        }else{
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mViewArrayList.get(position);
        itemView.setOnClickListener(new onClickListenerImpl());
        itemView.setTag(position);
        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        ((ViewPager) container).removeView((View) object);
    }

    private class onClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                View itemView = (View) mViewArrayList.get(Integer.parseInt(v.getTag() + ""));
                if (itemView != null) {
                    String urlpath = imgStr.get(Integer.parseInt(v.getTag() + "")).linkto;
                    if(urlpath.isEmpty()){

                    }else{
                        Intent i=new Intent(mContext, LinkToActivity.class);
                        i.putExtra("url",urlpath);
                        i.putExtra("title","网页");
                        mContext.startActivity(i);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}

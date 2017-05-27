package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import cn.net.bjsoft.sxdz.R;

/**
 * 轮播图
 */
public class RollViewPager extends ViewPager {
    private List<String> titleList;
    private TextView top_news_title;
    private List<String> imgUrlList;
    private List<String> linktoUrlList;
    private List<ImageView> dotList;
    //private BitmapUtils bitmapUtils;
    private MyPagerAdapter myPagerAdapter;

    //需要维护的页面指向的索引值
    private int currentPosition = 0;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //接受消息，viewpager按照自己维护好的currentPosition做页面跳转
            RollViewPager.this.setCurrentItem(currentPosition);
            //一直维护3秒跳跃一次的操作
            startRoll();
        }

        ;
    };
    private int downX;
    private int downY;
    private OnViewClickListener clickListener;

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                //夫控件不拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                //如果Y轴移动的比X轴移动的绝对距离多，要去下拉刷新，两个子控件一块移动，夫控件拦截事件
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
                    //夫控件拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //X轴Y轴移动的多，
                    //1,由右边向左边移动,moveX-downX<0
                    if (moveX - downX < 0 && getCurrentItem() == getAdapter().getCount() - 1) {
                        //跳转整个模块，夫控件要去拦截事件
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (moveX - downX < 0 && getCurrentItem() < getAdapter().getCount() - 1) {
                        //滑动的是轮播图片，夫控件不能去拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else if (moveX - downX > 0 && getCurrentItem() == 0) {
                        //2,有左边向右边移动,moveX-downX>0
                        //如果在第一个点上面由左边向右边移动，就需要去移动整个模块，夫控件拦截事件
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (moveX - downX > 0 && getCurrentItem() > 0) {
                        //滑动轮播图片，不能去拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public interface OnViewClickListener {
        //业务逻辑对应操作
        public void viewClickListener(String title,String url,String linktoUrl);
    }

    //界面移除出界面时候调用的方法
    protected void onDetachedFromWindow() {
        //取消handler中维护任务
        handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    ;


    //点所在的集合传递进去，由当前类单独管理
    public RollViewPager(Context context, final List<ImageView> dotList, OnViewClickListener clickListener) {
        super(context);
        this.clickListener = clickListener;
        //bitmapUtils = new BitmapUtils(context);
        this.dotList = dotList;
        this.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                //选中界面的时候去修改配套文字
                top_news_title.setText(titleList.get(arg0));

                for (int i = 0; i < dotList.size(); i++) {
                    if (i == arg0) {
                        //变红
                        dotList.get(arg0).setImageResource(R.drawable.page_on);
                    } else {
                        //变白
                        dotList.get(i).setImageResource(R.drawable.page_off);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    //titleList图片的配套文字，top_news_title显示图片的控件
    public void initTitle(List<String> titleList, TextView top_news_title) {
        if (top_news_title != null && titleList != null && titleList.size() > 0) {
            top_news_title.setText(titleList.get(0));
        }
        this.titleList = titleList;
        this.top_news_title = top_news_title;
    }

    //图片的链接地址所在集合
    public void initImgUrl(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    //点击链接地址
    public void initLinktoUrl(List<String> linktoUrlList){
        this.linktoUrlList = linktoUrlList;
    }

    class RunnableTask implements Runnable {
        @Override
        public void run() {
            //3秒后要去执行的代码
            currentPosition = (currentPosition + 1) % imgUrlList.size();
            //发送消息,等同于发送了一个空消息
            handler.obtainMessage().sendToTarget();
        }
    }

    public void startRoll() {
        //设置数据适配器
        if (myPagerAdapter == null) {
            myPagerAdapter = new MyPagerAdapter();
            this.setAdapter(myPagerAdapter);
        } else {
            myPagerAdapter.notifyDataSetChanged();
        }
        //viewpager滚动起来,
        //按照一定的时间间隔让viewpager滚动起来，handler去发送定时的消息
        handler.postDelayed(new RunnableTask(), 3000);
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imgUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = View.inflate(getContext(), R.layout.viewpager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
            //网络图片放置到本地操作(三级缓存(内存，文件，网络))  BitmapUtil---->异步下载图片
            //map<链接地址,Bitmap>，内存溢出，LRUCache算法(最近最少使用算法)
            //文件（链接地址作为图片文件的名称，将文件中获取出来的图片再一次的放置到内存中去）
            //放置获取图片的地方，对应图片的下载地址
            //bitmapUtils.display(imageView, imgUrlList.get(position));
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setFailureDrawableId(R.drawable.http_work_loading) //以资源id设置加载失败的动画
                    .setLoadingDrawableId(R.drawable.http_work_loading).build();

            x.image().bind(imageView, imgUrlList.get(position),imageOptions);
            //给viewpager添加一个放置了图片的view对象操作
            container.addView(view);
            //viewpager和内部view事件的一个交互过程
            //1，如果获取按下事件，就将其传递到内部控件中去，内部控件响应action_down事件,
            //2,如果手势不去滑动，就抬起Action_up，viewpager优先获取，传递给内部的view，作用在view上

            //3,如果手势滑动actiont_move，过后抬起action_up，滑动的事件优先传递给viewpager，然后传递给，内部view去做响应，
            //如果滑动达到一定距离的是，view就不再去响应滑动事件(action_move)了,转而响应action_cancel,后续的事件就不再响应了

            //4，在view触发了action_cancel，后续(action_move,action_up)的事件都由viewpager再一次捕获响应。
            view.setOnTouchListener(new OnTouchListener() {
                private int downX;
                private long downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //当前控件要去响应事件
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //手指点中，不能滑动，handler不去维护任务达到不去滑动的目的
                            handler.removeCallbacksAndMessages(null);
                            downTime = System.currentTimeMillis();
                            downX = (int) event.getX();
                            break;
                        //如果有做滑动操作，最后view不会触发ACTION_UP操作，而会去触发ACTION_CANCEL
                        case MotionEvent.ACTION_UP:
                            startRoll();
                            int upX = (int) event.getX();

                            long upTime = System.currentTimeMillis();
                            if (upX == downX && upTime - downTime < 500) {
                                //点击轮播图后的业务逻辑，
                                //回调(1，定义一个接口2，有未实现的方法(未知的后续业务逻辑)3，获取实现了接口的类生成的对象，4，调用一下)
//							Toast.makeText(getContext(),position+"", 0).show();
                                if (clickListener != null) {
                                    clickListener.viewClickListener(titleList.get(position),imgUrlList.get(position),linktoUrlList.get(position));
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            startRoll();
                            break;
                    }
                    return true;
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

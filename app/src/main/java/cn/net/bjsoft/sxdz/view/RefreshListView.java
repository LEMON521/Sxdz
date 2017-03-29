package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.bjsoft.sxdz.R;


public class RefreshListView extends ListView implements OnScrollListener {
	private LinearLayout refresh_header_root;
	private LinearLayout refresh_header_view;
	private ProgressBar refresh_header_progressbar;
	private ImageView refresh_header_imageview;
	private TextView refresh_header_text;
	private TextView refresh_header_time;
	private int headerHeight;
	private View customerView;
	private RotateAnimation animationUp;
	private RotateAnimation animationDown;
	private int downY = -1;
	private int mFirstVisibleItem = -1;
	
	//默认情况下是下拉刷新状态
	private int currentOption = PULL_REFRESH;
	private OnRefreshListener onRefreshListener;
	
	//下拉刷新
	private static final int PULL_REFRESH = 0;
	//释放刷新
	private static final int RELEASE_REFRESH = 1;
	//正在刷新
	private static final int IS_REFRESH = 2;
	
//	private Handler handler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			onRefreshFinish();
//		};
//	};
	private int footerHeight;
	private boolean isload = false;
	private View viewFooter;
	
	public RefreshListView(Context context) {
		super(context);
		//初始化刷新头
		initHeader();
		initFooter();
		this.setOnScrollListener(this);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader();
		initFooter();
		this.setOnScrollListener(this);
	}
	
	private void initFooter() {
		viewFooter = View.inflate(getContext(), R.layout.refresh_footer, null);
		//隐藏掉
		viewFooter.measure(0, 0);
		footerHeight = viewFooter.getMeasuredHeight();
		viewFooter.setPadding(0, -footerHeight, 0, 0);
		//给指定的Listview去添加一个底
		this.addFooterView(viewFooter);
	}

	public interface OnRefreshListener{
		//下拉刷新方法
		public void pullDownRefresh();
		//上拉加载方法
		public void pullUpLoadMore();
	}
	//接受对象的操作，对象需要去调用方法
	public void setOnRefreshListener(OnRefreshListener onRefreshListener){
		this.onRefreshListener  = onRefreshListener;
	}
	
	public void onRefreshFinish(){
		//刷新完成 后的操作
		if(IS_REFRESH == currentOption){
			//刷新完成后需要去隐藏正在刷新的进度条，箭头需要去显示，拖拽出来的刷新头给隐藏掉
			currentOption = PULL_REFRESH;
			//占有一定位置，让箭头显示在中心位置
			refresh_header_progressbar.setVisibility(View.INVISIBLE);
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_view.setPadding(0, -headerHeight, 0, 0);
		}
		//下拉加载完成处理
		if(isload){
			isload = false;
			viewFooter.setPadding(0, -footerHeight, 0, 0);
		}
	}
	private void initHeader() {
		View viewHeader = View.inflate(getContext(), R.layout.refresh_header, null);
		
		refresh_header_root = (LinearLayout) viewHeader.findViewById(R.id.refresh_header_root);

		//刷新头
		refresh_header_view = (LinearLayout) viewHeader.findViewById(R.id.refresh_header_view);
		
		//获取进度条
		refresh_header_progressbar = (ProgressBar) viewHeader.findViewById(R.id.refresh_header_progressbar);
		refresh_header_imageview = (ImageView) viewHeader.findViewById(R.id.refresh_header_imageview);
		
		refresh_header_text = (TextView) viewHeader.findViewById(R.id.refresh_header_text);
		refresh_header_time = (TextView) viewHeader.findViewById(R.id.refresh_header_time);
		
		//测量刷新头
		refresh_header_view.measure(0, 0);
		//获取测量过后的高度
		headerHeight = refresh_header_view.getMeasuredHeight();
		//隐藏刷新头
		refresh_header_view.setPadding(0, -headerHeight, 0, 0);
		//添加头的操作
		this.addHeaderView(viewHeader);
		//初始动画操作
		initAnimation();
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//记录手指点下y坐标
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(downY == -1){
				//无限接近点下downY的值
				downY = (int) ev.getY();
			}
			
			int moveY = (int) ev.getY();
			int padding = -headerHeight + moveY-downY;
			
			//获取listView的Y轴在屏幕上的左上角高度
			int[] listViewLocation = new int[2];
			//获取Listview在屏幕上左上角的坐标
			this.getLocationOnScreen(listViewLocation);
			int listViewLocationY = listViewLocation[1];
			
			//获取轮播图对应在Y轴上的左上角高度
//			int[] customerViewLocation = new int[2];
//			customerView.getLocationOnScreen(customerViewLocation);
//			int customerViewLocationY = customerViewLocation[1];
			
//			if(listViewLocationY>customerViewLocationY){
//				break;
//			}
			//如果正在刷新的时候所做操作不响应
			if(IS_REFRESH == currentOption){
				break;
			}
			if(padding > -headerHeight && mFirstVisibleItem == 0){
				//在第一个条目上面向下拖拽
				if(padding>0 && currentOption == PULL_REFRESH){
					//完整的拖拽出来
					currentOption = RELEASE_REFRESH;
					setCurrentOption();
				}
				
				if(padding<0 && currentOption == RELEASE_REFRESH){
					//没有完整的拖拽脱出
					currentOption = PULL_REFRESH;
					setCurrentOption();
				}
				refresh_header_view.setPadding(0, padding, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -1;
			if(currentOption == PULL_REFRESH){
				refresh_header_view.setPadding(0, -headerHeight, 0, 0);
			}
			if(currentOption == RELEASE_REFRESH){
				refresh_header_view.setPadding(0, 0	, 0, 0);
				currentOption = IS_REFRESH;
				setCurrentOption();
				//刷新业务逻辑调用的地方，回调(定义一个接口，在接口中定义一个未实现的方法，谁用谁实现，合适的地方去调用当前方法)
				if(onRefreshListener!=null){
					//刷新业务逻辑处理
					onRefreshListener.pullDownRefresh();
				}
//				handler.sendEmptyMessageDelayed(0,1000);
			}
			
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void setCurrentOption(){
		switch (currentOption) {
		case RELEASE_REFRESH:
			refresh_header_text.setText("释放刷新");
			refresh_header_imageview.startAnimation(animationUp);
			break;
		case PULL_REFRESH:
			refresh_header_text.setText("下拉刷新");
			refresh_header_imageview.startAnimation(animationDown);
			break;
		case IS_REFRESH:
			refresh_header_text.setText("正在刷新");
			refresh_header_progressbar.setVisibility(View.VISIBLE);
			refresh_header_imageview.clearAnimation();
			refresh_header_imageview.setVisibility(View.GONE);
			refresh_header_time.setText(getDataTime());
			break;
		}
	}
	
	public String getDataTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void initAnimation() {
		animationUp = new RotateAnimation(
				0, -180, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animationUp.setDuration(500);
		animationUp.setFillAfter(true);
		
		animationDown = new RotateAnimation(
				-180,-360,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animationDown.setDuration(500);
		animationDown.setFillAfter(true);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		OnScrollListener.SCROLL_STATE_FLING  飞速滚动
//		OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 一直触摸着在滚动
//		OnScrollListener.SCROLL_STATE_IDLE  滚动状态发生改变的
		if(scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL){
			//监听滑动状态的方法
			if(getLastVisiblePosition() == getAdapter().getCount()-1 && !isload  ){
				//看见的是最后一个条目的时候
				//加载操作
				isload = true;
				//显示正在加载的脚
				viewFooter.setPadding(0, 0, 0, 0);
				//加载的业务逻辑，(回调，创建一个接口，接口内定义一个未实现的方法，谁用谁实现，在合适的地方调用当前方法)
				if(onRefreshListener!=null){
					onRefreshListener.pullUpLoadMore();
				}
				
//				handler.sendEmptyMessageDelayed(0, 1000);
			}
		}
	}

	//
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem  = firstVisibleItem;
	}
}

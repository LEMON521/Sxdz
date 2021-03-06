package cn.net.bjsoft.sxdz.activity.home;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.BitmapUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.CommunityActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.FunctionActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.MessageActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.UserActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.search.SearchResultActivity;
import cn.net.bjsoft.sxdz.activity.home.bartop.search.SpeechSearchActivity;
import cn.net.bjsoft.sxdz.app_utils.HttpPostUtils;
import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.HomepageBean;
import cn.net.bjsoft.sxdz.bean.app.ToolbarBean;
import cn.net.bjsoft.sxdz.bean.app.user.users_all.UsersSingleBean;
import cn.net.bjsoft.sxdz.dialog.ALiPushMessageInAppPopupWindow;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.receiver.ALiPushType3Receiver;
import cn.net.bjsoft.sxdz.utils.AddressUtils;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.SPJpushUtil;
import cn.net.bjsoft.sxdz.utils.SPUtil;
import cn.net.bjsoft.sxdz.utils.function.ALiPushCountUtils;
import cn.net.bjsoft.sxdz.utils.function.InitFragmentUtil;
import cn.net.bjsoft.sxdz.utils.function.UsersInforUtils;
import cn.net.bjsoft.sxdz.utils.function.WidgetUtils;
import cn.net.bjsoft.sxdz.view.BottomIconView;
import cn.net.bjsoft.sxdz.view.BottomIconView_2;
import cn.net.bjsoft.sxdz.view.CircleImageView;

/**
 * Created by 靳宁宁 on 2017/1/5.
 */

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.title_title)
    private TextView title;


    @ViewInject(R.id.main_content)
    private LinearLayout main_content;

    @ViewInject(R.id.main_show_hide)
    private ImageView showOrHide;

    @ViewInject(R.id.main_top_ll)
    private LinearLayout mTopBar_ll;

    @ViewInject(R.id.main_top_ll_ll)
    private LinearLayout mTopBar_ll_ll;

    //顶部栏
    @ViewInject(R.id.community)
    private FrameLayout community;
    @ViewInject(R.id.community_img)
    private ImageView community_img;
    @ViewInject(R.id.community_num)
    private TextView community_num;

    @ViewInject(R.id.function)
    private FrameLayout function;
    @ViewInject(R.id.function_img)
    private ImageView function_img;
    @ViewInject(R.id.function_num)
    private TextView function_num;

    @ViewInject(R.id.message)
    private FrameLayout message;
    @ViewInject(R.id.message_img)
    private ImageView message_img;
    @ViewInject(R.id.message_num)
    private TextView message_num;


    @ViewInject(R.id.user)
    private FrameLayout user;
    @ViewInject(R.id.sign_user_icon)
    private CircleImageView user_icon;
    //private RoundImageView user_icon;
    @ViewInject(R.id.user_num)
    private TextView user_num;

    @ViewInject(R.id.search)
    private RelativeLayout search;
    @ViewInject(R.id.search_edittext)
    private EditText search_edittext;
    @ViewInject(R.id.search_edittext)
    private EditText search_speech;


    //*************************************************************************//
    @ViewInject(R.id.main_botton_ll)
    private LinearLayout mBottonBar_ll;
    //底部栏图片
    @ViewInject(R.id.main_botton_scan)
    private ImageView ivScan;
    @ViewInject(R.id.main_botton_upload)
    private ImageView ivUpload;
    @ViewInject(R.id.main_botton_article)
    private ImageView ivArticle;
    @ViewInject(R.id.main_botton_form)
    private ImageView ivForm;
    @ViewInject(R.id.main_botton_mine)
    private ImageView ivMine;


    @ViewInject(R.id.llll)
    private LinearLayout llll;
    @ViewInject(R.id.main_botton_ll_ll)
    private LinearLayout mBottonBar_ll_ll;
    public BottomIconView_2 bottomView;
    public BottomIconView scanView;
    public BottomIconView uploadView;
    public BottomIconView articleView;
    public BottomIconView formView;
    public BottomIconView mineView;
    //************************************************************************//

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private boolean submitLock = false;
    private int time = 2;//默认自动定位时间

    private ArrayList<ImageView> mBottonIVList;
    private ArrayList<BottomIconView_2> mBottomIconViewList;
    private ArrayList<BaseFragment> mBottonFragmentList;

    private MainActivity mActivity = null;
    private static MainActivity mainActivity;

    private String mJson = "";
    //private DatasBean mDatasBean = null;
    private AppBean appBean;
    private ToolbarBean toolbarBean;
    private ArrayList<HomepageBean> homepageBean;

    //private DatasBean.ToolbarDao toolbarBean = null;
    //private ArrayList<DatasBean.HomepageDao> homepageBean = null;
    private ArrayList<ImageView> mImgeView;

    private BitmapUtils bitmapUtils;
    private ImageOptions mImageOptions;
    private Animation toolUpAnimation;
    private Animation toolDownAnimation;
    private TranslateAnimation mShowAnimation;
    private TranslateAnimation mHideAnimation;
    private Animation mUpAnimation;
    private Animation mDownAnimation;
    private ObjectAnimator mUpAnimator;
    private ObjectAnimator mDownAnimator;


    private boolean mTopBarIsShow = true;//顶部栏显示与否的标记，防止重复动画--true,显示
    private String communityTag = "";
    private String functionTag = "";
    private String messageTag = "";


    /**
     * 广播
     */
    private MyReceiver receiver = new MyReceiver();

    private ALiPushType3Receiver aLiPushType3Receiver = new ALiPushType3Receiver();

    private ALiPushMessageInAppPopupWindow showPushWindow;

    private ALiPushBottomBarReceiver bottomBarReceiver = new ALiPushBottomBarReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GPSUtils.getAddress(this);
        // x.view().inject(this);


        mActivity = this;
        mainActivity = this;
        mJson = getIntent().getStringExtra("json");
        LogUtil.e("=====mJson@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=====" + mJson);
        //mDatasBean = GsonUtil.getDatasBean(mJson);

        showProgressDialog();
        appBean = GsonUtil.getAppBean(mJson);
        toolbarBean = appBean.toolbar;
        homepageBean = appBean.homepage;

        mImgeView = new ArrayList<>();
        mImageOptions = new ImageOptions.Builder()
               /*.setSize(0, 0)*/
                /*.setCircular(true)*/
                .setUseMemCache(true)
                /*.setCrop(true)*/
                .build();
        bitmapUtils = new BitmapUtils(this, AddressUtils.img_cache_url);
        bitmapUtils.configDefaultLoadingImage(R.drawable.get_back_passwoed);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.get_back_passwoed);


//        mImageOptions = new ImageOptions.Builder().setCircular(true).setUseMemCache(true).build();


        //初始化顶部栏动画
        initAnimation();
        initTopBar();
        initBottomBar();
        //initMapLocation(time);

        UsersInforUtils inforUtils = UsersInforUtils.getInstance(this);
        UsersSingleBean bean = inforUtils.getUserInfo("10001");
        LogUtil.e("联系人信息--------------------" + bean.nickname + "::" + bean.avatar);

        dismissProgressDialog();
    }

    //TODO 因为Activity每次执行，不管是在前台后台，可见不可见，onStart是必经之路，所以将推送的数据在这里显示最合理
    @Override
    protected void onStart() {
        super.onStart();


        LogUtil.e("main==onStart");
        setUserIcon();
        getPushCount(this);


        setBottomBarNum(this);
        setPushNumber();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化--顶部--的标签栏
     */
    private void initTopBar() {


        ToolbarBean toolBar = appBean.toolbar;

        if (toolBar == null) {
            return;
        }
        //如果都为false，则让顶部栏隐藏
        if (!(toolBar.train || toolBar.knowledge || toolBar.proposal || toolBar.bug ||
                toolBar.scan || toolBar.shoot || toolBar.signin /*|| toolBar.payment.size() > 0*/ ||
                toolBar.search_by_word || toolBar.search_by_voice ||
                toolBar.message || toolBar.task || toolBar.crm || toolBar.approve ||
                toolBar.myself)) {
            mTopBar_ll.setVisibility(View.GONE);
        } else {
            mTopBar_ll.setVisibility(View.VISIBLE);
        }

        //社区
        {
            if (!(toolBar.train || toolBar.knowledge || toolBar.proposal || toolBar.bug || toolBar.community)) {
                community.setVisibility(View.GONE);
            } else {
                community.setVisibility(View.VISIBLE);
            }
        }
        //功能
        {
            if (!(toolBar.scan || toolBar.shoot || toolBar.signin || (toolBar.payment != null && toolBar.payment.size() > 0))) {
                function.setVisibility(View.GONE);
            } else {
                function.setVisibility(View.VISIBLE);
            }
        }
        //搜索
        {
            if (!(toolBar.search_by_word || toolBar.search_by_voice)) {
                search.setVisibility(View.INVISIBLE);
            } else {
                search.setVisibility(View.VISIBLE);
            }
        }
        //消息
        {
            if (!(toolBar.message || toolBar.task || toolBar.crm || toolBar.approve)) {
                message.setVisibility(View.GONE);
            } else {
                message.setVisibility(View.VISIBLE);
            }
        }
        //用户
        {
            if (!(toolBar.myself)) {
                user.setVisibility(View.GONE);
            } else {
                user.setVisibility(View.VISIBLE);
                setUserIcon();
            }
        }
        LogUtil.e("mTopBar_ll=============" + mTopBar_ll.getHeight());
//        mTopBar_ll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent ev) {
//                switch (ev.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        //记录手指点下y坐标
//                        downY = (int) ev.getY();
//                        //LogUtil.e("点击的位置=="+downY);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (downY == -1) {
//                            //无限接近点下downY的值
//                            downY = (int) ev.getY();
//                        }
//                        moveY = (int) ev.getY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        upY = (int) ev.getY();
//                        // LogUtil.e("抬起的位置=="+ev.getY());
//                        if (downY < 200) {
//                            //下划
//                            if (upY - downY > 0) {
//                                if (mTopBarIsShow) {
//                                    mTopBarIsShow = true;
//                                } else {
//                                     LogUtil.e("显示了"+(upY-downY));
//                                    //mTopBar_ll.setAnimation(mShowAnimation);
//                                    mTopBar_ll.startAnimation(mDownAnimation);
//                                    // MyToast.showShort(MainActivity.this,"显示了"+(mTopBar_ll.getAnimation()));
//                                    mTopBar_ll.setVisibility(View.VISIBLE);
//                                    main_content.setAnimation(mDownAnimation);
//
//                                    mTopBarIsShow = true;
//                                }
//
//                            } else if (upY - downY < 0) {
//                                if (mTopBarIsShow) {
//                                      LogUtil.e("隐藏了"+(upY-downY));
//                                    //mTopBar_ll.setAnimation(mHideAnimation);
//                                    mTopBar_ll.startAnimation(mUpAnimation);
//                                    // MyToast.showShort(MainActivity.this,"隐藏了"+(mTopBar_ll.getAnimation()));
//                                    mTopBar_ll.setVisibility(View.GONE);
//                                    main_content.setAnimation(mUpAnimation);
//                                    mTopBarIsShow = false;
//                                } else {
//                                    mTopBarIsShow = false;
//                                }
//                            }
//                        }
//                        downY = -1;
//                        moveY = -1;
//                        break;
//                }
//                return true;
//            }
//        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化底部的标签栏
     */
    private void initBottomBar() {
        /**
         * ********************************添加Fragment开始*********************************************
         * TODO 向底部栏添加关联的Fragment
         */
//        mBottonFragmentList = new ArrayList<>();
//        for (int homepageNum = 0;homepageNum<homepageBean.size();homepageNum++){
        mBottonFragmentList = new ArrayList<>();
        mBottonFragmentList = InitFragmentUtil.getBottonFragments(mJson);
        for (int i = 0; i < mBottonFragmentList.size(); i++) {
            LogUtil.e("每个Fragment为----" + mBottonFragmentList.get(i).toString() + "--tag::" + mBottonFragmentList.get(i).getArguments().get("tag"));
        }
//        LogUtil.e("个数为"+mBottonFragmentList.size());
//        mBottonFragmentList.addAll(InitFragmentUtil.getBottonFragments(mJson));
        //setBottonItemVisibility();
//        }
        //********************************添加Fragment结束*********************************************
        /**
         * ********************************添加ImageView开始*********************************************
         * TODO 向底部栏添加关联的ImageView
         *
         * 根据fragment 的tag对ImageView进行控制
         */
//        homepageBean = appBean.homepage;
//        mBottonIVList = new ArrayList<>();
        mBottomIconViewList = new ArrayList<>();
        for (int homepageNum = 0; homepageNum < mBottonFragmentList.size(); homepageNum++) {
            if (mBottonFragmentList.size() == 1) {
                mBottonBar_ll_ll.setVisibility(View.GONE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, mBottonFragmentList.get(homepageNum), mBottonFragmentList.get(homepageNum).getArguments().get("tag").toString())
                        .commit();
                LogUtil.e("tag为=====" + mBottonFragmentList.get(homepageNum).getArguments().get("tag"));
                return;
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            params.setMargins(0, 2, 0, 2);
            //给View绑定Tag的时候用创建Fragment时指定的Tag，这样就避免点击图标而导致找不到与Fragment相同的tag了
            bottomView = new BottomIconView_2(this, mBottonFragmentList.get(homepageNum).getArguments().getString("tag"));
            bottomView.setModeView(homepageBean.get(homepageNum).icon_position, homepageBean.get(homepageNum).text, homepageBean.get(homepageNum).icon_selected, homepageBean.get(homepageNum).icon_default, homepageBean.get(homepageNum).icon);
            LogUtil.e("添加了新图标" + bottomView.toString());
            bottomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomIconView_2 view = (BottomIconView_2) v;
                    //view.setClickShow();
                    onBottomIconClick(view);
                    LogUtil.e("点击了新图标" + view.toString());
                }
            });
            //bottomView.setLayoutParams(params);
            mBottomIconViewList.add(bottomView);
            mBottonBar_ll_ll.addView(bottomView, params);
            //llll.addView(bottomView,params);
        }

        LogUtil.e("$$缓存了新图标$$");
        synchronized (this) {
            for (int homepageNum = 0; homepageNum < mBottomIconViewList.size(); homepageNum++) {
                mBottomIconViewList.get(homepageNum).setClickShow();
                mBottomIconViewList.get(homepageNum).setDefaultShow();
            }
        }
        LogUtil.e("￥￥缓存了新图标￥￥");

        //********************************添加ImageView结束*********************************************
        //设置默认显示页面
        //int seclect = homepageBean.size() / 2 + homepageBean.size() % 2 - 1;
//        x.image().bind(mBottonIVList.get(seclect), homepageBean.get(seclect).icon_selected,mImageOptions);

        for (int seclect = 0; seclect < homepageBean.size(); seclect++) {
            //根据每个页面中selected的状态来判断
            if (homepageBean.get(seclect).selected == 1) {

                //极端情况，如果默认选择的是打开摄像机的页面，那么就进行判断
                if (mBottonFragmentList.size() > 0 && ((mBottonFragmentList.get(seclect).getArguments().get("tag").toString()).equals("scaning"))) {
                    PackageManager pm = getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    //如果权限没打开
                    if (!permission) {
                        if (homepageBean.size() > 1) {//如果程序后台配置了两个及其两个以上的页面，则默认打开第二个页面
                            MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限再重新启动程序！");
                            //onBottomIconClick(mBottomIconViewList.get(1));
                            continue;//TODO 结束当前循环，进行下一次循环
                        } else {
                            MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限再重新启动程序！");
                        }
                    } else {//如果打开了权限
                        onBottomIconClick(mBottomIconViewList.get(0));
                    }
                } else {
                    LogUtil.e("设置默认页面" + mBottomIconViewList.get(seclect).toString());
                    onBottomIconClick(mBottomIconViewList.get(seclect));
                }
                return;
            }
            //如果没有指定显示默认的状态，则默认选择第一个
            if (((seclect + "").equals((homepageBean.size() - 1) + "")) && (homepageBean.get(seclect).selected == 0)) {

                if (mBottonFragmentList.size() > 0 && ((mBottonFragmentList.get(0).getArguments().get("tag").toString()).equals("scaning"))) {
                    //LogUtil.e("没有默认页面");
                    PackageManager pm = getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        if (homepageBean.size() > 1) {
                            MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限再重新启动程序！");
                            onBottomIconClick(mBottomIconViewList.get(1));
                        } else {
                            MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限再重新启动程序！");
                        }
                    } else {
                        //LogUtil.e("设置默认页面"+mBottomIconViewList.get(0).toString());
                        onBottomIconClick(mBottomIconViewList.get(0));
                    }
                } else {
                    onBottomIconClick(mBottomIconViewList.get(0));
                    LogUtil.e("默认页面为：：：：" + mBottonFragmentList.get(0).getArguments().get("url"));
                }
            }
        }


    }

    /**
     * 点击底部图标的事件
     *
     * @param v 被点击的图标
     */
    public void onBottomIconClick(BottomIconView_2 v) {
        //LogUtil.e("BottomIconView_1的TAG==="+v.getTag().toString());
        for (int i = 0; i < mBottonFragmentList.size(); i++) {
//            MyToast.showShort(this,"是否为：："+(v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())));
            //字符串判定   一定要用equals！！！
            //LogUtil.e("替换Fragment的TAG==="+mBottonFragmentList.get(i).getArguments().get("tag").toString());
            if (v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())) {
                if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals("scaning")) {
                    PackageManager pm = getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                        return;
                    }
                }

                LogUtil.e("底部栏Tag为=====" + mBottonFragmentList.get(i).getArguments().get("tag").toString());

                String tag = mBottonFragmentList.get(i).getArguments().get("tag").toString();
                String url = mBottonFragmentList.get(i).getArguments().get("url").toString();
                if (tag.equals("communication_zdlf")) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, WebActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("title", "通讯");
                    startActivity(intent);
                    return;//这句话的效果在于,结束当前方法体,避免底部图标被点击之后的改变状态
                } else {
                    title.setText(homepageBean.get(i).text);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, mBottonFragmentList.get(i), mBottonFragmentList.get(i).getArguments().get("tag").toString())
                            .commit();
                }
            }
        }
        //先设置成默认的图标以及颜色
        for (int homepageNum = 0; homepageNum < mBottomIconViewList.size(); homepageNum++) {
            mBottomIconViewList.get(homepageNum).setDefaultShow();
        }
        v.setClickShow();//设置成点击后的图标以及颜色
        //LogUtil.e("点击定位位置"+GPSUtils.getAddress(this));
    }

    private void setBottonItemVisibility() {
        {
//            if (mBottonFragmentList.size()==0){
//                mBottonBar_ll_ll.setVisibility(View.GONE);
//                return;
//            }
            ivScan.setVisibility(View.GONE);
            ivUpload.setVisibility(View.GONE);
            ivArticle.setVisibility(View.GONE);
            ivForm.setVisibility(View.GONE);
            ivMine.setVisibility(View.GONE);
        }
        for (int i = 0; i < mBottonFragmentList.size(); i++) {
            if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals("logo")) {
                mBottonBar_ll_ll.setVisibility(View.GONE);
            } else if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals((ivScan.getTag().toString()))) {
                ivScan.setVisibility(View.VISIBLE);
                mImgeView.add(ivScan);
            } else if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals((ivUpload.getTag().toString()))) {
                ivUpload.setVisibility(View.VISIBLE);
                mImgeView.add(ivUpload);
            } else if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals((ivArticle.getTag().toString()))) {
                ivArticle.setVisibility(View.VISIBLE);
                mImgeView.add(ivArticle);
            } else if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals((ivForm.getTag().toString()))) {
                ivForm.setVisibility(View.VISIBLE);
                mImgeView.add(ivForm);
            } else if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals((ivMine.getTag().toString()))) {
                ivMine.setVisibility(View.VISIBLE);
                mImgeView.add(ivMine);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    @Event(value = {R.id.main_show_hide})
    private void onAnimationClick(View v) {

        int hight = WidgetUtils.getWidthHigh(1, mTopBar_ll)[0];
        LogUtil.e("------------------获取的宽高" + WidgetUtils.getWidthHigh(1, mTopBar_ll)[0]);
        switch (v.getId()) {
            case R.id.main_show_hide:
                if (mTopBarIsShow) {//点击之后隐藏
//                    mTopBar_ll.setAnimation(mHideAnimation);
                    mTopBar_ll_ll.setVisibility(View.GONE);
//                    mTopBar_ll.clearAnimation();
//                    ObjectAnimator.ofFloat(showOrHide, "TranslationY",-20)
//                            .setDuration(500).start();
//                    ObjectAnimator.ofFloat(mTopBar_ll, "TranslationY", -hight)
//                            .setDuration(500).start();
//                    ObjectAnimator.ofFloat(main_content, "TranslationY", -WidgetUtils.getWidthHigh(1, mTopBar_ll)[0])
//                            .setDuration(500).start();

                    LogUtil.e("第一种方法获取的宽高" + WidgetUtils.getWidthHigh(1, mTopBar_ll_ll)[0]);
                    LogUtil.e("第二种方法获取的宽高" + WidgetUtils.getWidthHigh(1, mTopBar_ll)[0]);
                    LogUtil.e("第三种方法获取的宽高" + WidgetUtils.getWidthHigh(3, mTopBar_ll_ll)[0]);
                    showOrHide.setImageResource(R.drawable.zhuijia);
//                    mTopBar_ll.startAnimation(mUpAnimation);
//                    main_content.startAnimation(mUpAnimation);
//                    showOrHide.startAnimation(mUpAnimation);
                    mTopBarIsShow = false;
                } else {//点击之后显示
                    mTopBar_ll_ll.setVisibility(View.VISIBLE);
//                    mTopBar_ll.setAnimation(mShowAnimation);
//                    mTopBar_ll.clearAnimation();
//                    ObjectAnimator.ofFloat(mTopBar_ll, "TranslationY", 0)
//                            .setDuration(500).start();
//                    ObjectAnimator.ofFloat(main_content, "TranslationY", 0)
//                            .setDuration(500).start();
//                    ObjectAnimator.ofFloat(showOrHide, "TranslationY",0)
//                            .setDuration(500).start();
//                    mTopBar_ll.startAnimation(mDownAnimation);
//                    main_content.startAnimation(mDownAnimation);
//                    showOrHide.startAnimation(mDownAnimation);
                    showOrHide.setImageResource(R.drawable.shang);
                    LogUtil.e("第一种方法获取的宽高" + WidgetUtils.getWidthHigh(1, mTopBar_ll_ll)[0]);
                    LogUtil.e("第二种方法获取的宽高" + WidgetUtils.getWidthHigh(1, mTopBar_ll)[0]);
                    LogUtil.e("第三种方法获取的宽高" + WidgetUtils.getWidthHigh(3, mTopBar_ll_ll)[0]);
                    mTopBarIsShow = true;
                }
                break;
        }
    }

    @Event(value = {R.id.community, R.id.function, R.id.message, R.id.user, R.id.search_edittext, R.id.search_speech})
    private void onTopClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {

            case R.id.community:

                intent.setClass(context, CommunityActivity.class);
                intent.putExtra("opentag", communityTag);
                //pushNum.replace("",0);
                //app.reFreshPushNumList("Community", 0);
                break;
            case R.id.function:

                intent.setClass(context, FunctionActivity.class);
                intent.putExtra("opentag", functionTag);
                //app.reFreshPushNumList("Function", 0);
                break;
            case R.id.message:

                intent.setClass(context, MessageActivity.class);
                intent.putExtra("opentag", messageTag);
                //app.reFreshPushNumList("Message", 0);
                break;
            case R.id.user:

                intent.setClass(context, UserActivity.class);
                //app.reFreshPushNumList("User", 0);
                break;
            case R.id.search_edittext:

                //MyToast.showShort(MainActivity.this, "文字搜索");
                intent.putExtra("searchType", "text");
                intent.setClass(context, SearchResultActivity.class);
                break;
            case R.id.search_speech:

                //MyToast.showShort(MainActivity.this, "语音搜索");
                intent.putExtra("searchType", "speech");
                intent.setClass(context, SpeechSearchActivity.class);
                break;
        }
        intent.putExtra("json", mJson);
        startActivity(intent);
    }

    @Event(value = {R.id.main_botton_scan, R.id.main_botton_upload, R.id.main_botton_article, R.id.main_botton_form, R.id.main_botton_mine,})
    private void onBottonClick(View v) {
        for (int i = 0; i < mBottonFragmentList.size(); i++) {
//            MyToast.showShort(this,"是否为：："+(v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())));
            //字符串判定   一定要用equals！！！
            if (v.getTag().toString().equals(mBottonFragmentList.get(i).getArguments().get("tag").toString())) {


                if ((mBottonFragmentList.get(i).getArguments().get("tag").toString()).equals("scaning")) {
                    PackageManager pm = getPackageManager();
                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CAMERA", "cn.net.bjsoft.sxdz"));
                    if (!permission) {
                        MyToast.showLong(this, "没有拍摄权限,请在移动设备设置中添加拍摄权限");
                        return;

                    }
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, mBottonFragmentList.get(i), mBottonFragmentList.get(i).getArguments().get("tag").toString())
                        .commit();
            }
        }


        setImageViewDefult();
        switch (v.getId()) {
            case R.id.main_botton_scan:
                ivScan.setImageResource(R.drawable.tab_scan_s);
                break;
            case R.id.main_botton_upload:
                ivUpload.setImageResource(R.drawable.tab_upload_s);
                break;
            case R.id.main_botton_article:
                ivArticle.setImageResource(R.drawable.tab_news_s);
                break;
            case R.id.main_botton_form:
                ivForm.setImageResource(R.drawable.tab_form_s);
                break;
            case R.id.main_botton_mine:
                ivMine.setImageResource(R.drawable.tab_me_s);
                break;
        }
    }

    /**
     * 设置未选中状态
     */
    private void setImageViewDefult() {
        ivScan.setImageResource(R.drawable.tab_scan);
        ivUpload.setImageResource(R.drawable.tab_upload);
        ivArticle.setImageResource(R.drawable.tab_news);
        ivForm.setImageResource(R.drawable.tab_form);
        ivMine.setImageResource(R.drawable.tab_me);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        LogUtil.e("main====" + "onRestart");

    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("main==onResume");
        /**
         * 注册广播
         */
        registerReceiver(receiver, new IntentFilter("cn.net.bjsoft.sxdz.main"));
        /**
         * 注册广播
         */

        registerReceiver(aLiPushType3Receiver, new IntentFilter("cn.net.bjsoft.sxdz.alipush.notify_type_3"));

        aLiPushType3Receiver.setOnData(new ALiPushType3Receiver.OnGetData() {
            @Override
            public void onDataCallBack(Bundle bundleData) {
                LogUtil.e("推送通知拿到数据==============" + bundleData);
                if (bundleData != null) {

                    showPushWindow = new ALiPushMessageInAppPopupWindow(MainActivity.this, bundleData, mBottonBar_ll_ll);

                    showPushWindow.showWindow();
                }
            }
        });

        registerReceiver(bottomBarReceiver, new IntentFilter("cn.net.bjsoft.sxdz.main.bottombar"));


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(aLiPushType3Receiver);
        LogUtil.e("main==onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
//        //在后台时，不让他接收广播
//        unregisterReceiver(receiver);
//        unregisterReceiver(aLiPushType3Receiver);

        LogUtil.e("main==onStop");
    }

    /**
     * 监听退出
     */
    private long exitTime = 0;//记录按下的时间戳

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    MyToast.showShort(this, "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                    return false;
                } else {
                    finish();
                    System.exit(0);
                }
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
        //return false;
    }


//    private int downY = -1;
//    private int moveY = -1;
//    private int upY = -1;

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //记录手指点下y坐标
//                downY = (int) ev.getY();
//                // LogUtil.e("点击的位置=="+downY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (downY == -1) {
//                    //无限接近点下downY的值
//                    downY = (int) ev.getY();
//                }
//                moveY = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                upY = (int) ev.getY();
//                // LogUtil.e("抬起的位置=="+ev.getY());
//                if (downY < 200) {
//                    //下划
//                    if (upY - downY > 0) {
//                        if (mTopBarIsShow) {
//                            mTopBarIsShow = true;
//                        } else {
//                              LogUtil.e("显示了"+(upY-downY));
//                            //mTopBar_ll.setAnimation(mShowAnimation);
//                            mTopBar_ll.startAnimation(mDownAnimation);
//                            //MyToast.showShort(MainActivity.this,"显示了"+(mTopBar_ll.getAnimation()));
//                            //mTopBar_ll.setVisibility(View.VISIBLE);
//                            main_content.setAnimation(mDownAnimation);
//                            mTopBarIsShow = true;
//                        }
//                    } else if (upY - downY < 0) {
//                        if (mTopBarIsShow) {
//                            LogUtil.e("隐藏了"+(upY-downY));
//                            //mTopBar_ll.setAnimation(mHideAnimation);
//                            mTopBar_ll.startAnimation(mUpAnimation);
//                            main_content.setAnimation(mUpAnimation);
//                            // MyToast.showShort(MainActivity.this,"隐藏了"+(mTopBar_ll.getAnimation()));
//                            //mTopBar_ll.setVisibility(View.GONE);
//                            mTopBarIsShow = false;
//                        } else {
//                            mTopBarIsShow = false;
//                        }
//
//                    }
//                }
//                downY = -1;
//                moveY = -1;
//                break;
//        }
//        //return super.onTouchEvent(ev);
//        return false;
//    }

    /**
     * 初始化顶部栏动画
     */
    private void initAnimation() {
        // 从自已-1倍的位置移到自己原来的位置
        mShowAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f);


        mHideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mShowAnimation.setDuration(500);
        mHideAnimation.setDuration(500);


        mUpAnimation = AnimationUtils.loadAnimation(this, R.anim.main_up);
        mUpAnimation.setFillAfter(false);
        mUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                mTopBar_ll_ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTopBar_ll_ll.setVisibility(View.GONE);
//                mTopBar_ll.clearAnimation();
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
//                params.setMargins(0,-85,0,0);
//                main_content.setLayoutParams(params);
//                mTopBar_ll.setLayoutParams(params);
//                main_show_hide_ll.setLayoutParams(params);
//                main_content.layout(0,0-85,mActivity.getWindowManager().getDefaultDisplay().getWidth(),mActivity.getWindowManager().getDefaultDisplay().getHeight()-85);
//                mTopBar_ll.layout(0,0-85,mActivity.getWindowManager().getDefaultDisplay().getWidth(),mActivity.getWindowManager().getDefaultDisplay().getHeight()-85);
//                main_show_hide_ll.layout(0,0-85,mActivity.getWindowManager().getDefaultDisplay().getWidth(),mActivity.getWindowManager().getDefaultDisplay().getHeight()-85);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mDownAnimation = AnimationUtils.loadAnimation(this, R.anim.main_down);
        mDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTopBar_ll_ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                mTopBar_ll.clearAnimation();
//                mTopBar_ll.setVisibility(View.VISIBLE);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
//                params.setMargins(0,0,0,0);
//                main_content.setLayoutParams(params);
//                mTopBar_ll.setLayoutParams(params);
//                main_show_hide_ll.setLayoutParams(params);
//                main_content.layout(0,0,mActivity.getWindowManager().getDefaultDisplay().getWidth(),mActivity.getWindowManager().getDefaultDisplay().getHeight()-85);
//                mTopBar_ll.layout(0,0,mActivity.getWindowManager().getDefaultDisplay().getWidth(),mActivity.getWindowManager().getDefaultDisplay().getHeight()-85);
//                main_show_hide_ll.layout(0,0,mActivity.getWindowManager().getDefaultDisplay().getWidth(),mActivity.getWindowManager().getDefaultDisplay().getHeight()-85);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mDownAnimation.setFillAfter(false);


        //属性动画

//        //上划动画
//        toolUpAnimation = AnimationUtils.loadAnimation(this,
//                R.anim.toolbar_up);
//        toolUpAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mTopBar_ll.setY(-100f);
//            }
//        });
//        //下划动画
//        toolDownAnimation = AnimationUtils.loadAnimation(this,
//                R.anim.toolbar_up);
//        toolDownAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mTopBar_ll.setY(0);
//            }
//        });
    }

    /**
     * 暴露方法，方便其他的页面设置头像
     */
    public void setUserIcon() {
//        if (appBean.user.logined) {
////            MyBitmapUtils.getInstance(this).clearCache(appBean.user.avatar);
////            MyBitmapUtils.getInstance(this).display(user_icon, appBean.user.avatar);
//            bitmapUtils.display(user_icon, appBean.user.avatar);
//        }
    }


    /**
     * 设置用来显示推送数据TextView的数值
     */
    public void setPushNumber() {
        if (!toolbarBean.train) {
            SPJpushUtil.setTrain(this, 0);
        }
        if (!toolbarBean.knowledge) {
            SPJpushUtil.setKnowledge(this, 0);
        }
        if (!toolbarBean.proposal) {
            SPJpushUtil.setProposal(this, 0);
        }
        if (!toolbarBean.bug) {
            SPJpushUtil.setBug(this, 0);
        }
        if (!toolbarBean.community) {
            SPJpushUtil.setCommunity(this, 0);
        }
        if (!toolbarBean.scan) {
            SPJpushUtil.setScan(this, 0);
        }
        if (!toolbarBean.shoot) {
            SPJpushUtil.setShoot(this, 0);
        }
        if (!toolbarBean.signin) {
            SPJpushUtil.setSignin(this, 0);
        }

        LogUtil.e("===================payment======================" + SPJpushUtil.getPayment(this));
        if (toolbarBean.payment != null) {
            LogUtil.e("=======里面============payment======================" + SPJpushUtil.getPayment(this));
            if (!(toolbarBean.payment.size() > 0)) {
                SPJpushUtil.setPayment(this, 0);
                LogUtil.e("=======里面============payment======================" + SPJpushUtil.getPayment(this));
            }
        } else {
            SPJpushUtil.setPayment(this, 0);
        }
        if (!toolbarBean.message) {
            SPJpushUtil.setMessage(this, 0);
        }
        if (!toolbarBean.task) {
            SPJpushUtil.setTask(this, 0);
        }
        if (!toolbarBean.crm) {
            SPJpushUtil.setCrm(this, 0);
        }
        if (!toolbarBean.approve) {
            SPJpushUtil.setApprove(this, 0);
        }
        if (!toolbarBean.myself) {
            SPJpushUtil.setMyself(this, 0);
        }

        //社区
        int communityNum = 0;
        int functionNum = 0;
        int messageNum = 0;
        int userNum = 0;

        communityNum = SPJpushUtil.getTrain(MainActivity.this)
                + SPJpushUtil.getKnowledge(MainActivity.this)
                + SPJpushUtil.getProposal(MainActivity.this)
                + SPJpushUtil.getBug(MainActivity.this)
                + SPJpushUtil.getCommunity(MainActivity.this);


        if (communityNum > 0) {
            community_num.setVisibility(View.VISIBLE);
            if (SPJpushUtil.getCommunity(MainActivity.this) > 0) {
                community_img.setImageResource(R.drawable.nav_live);
                community_num.setText(SPJpushUtil.getCommunity(MainActivity.this) + "");
                communityTag = "community";
            } else if (SPJpushUtil.getBug(MainActivity.this) > 0) {
                community_img.setImageResource(R.drawable.nav_bug);
                community_num.setText(SPJpushUtil.getBug(MainActivity.this) + "");
                communityTag = "disabuse";
            } else if (SPJpushUtil.getProposal(MainActivity.this) > 0) {
                community_img.setImageResource(R.drawable.nav_advise);
                community_num.setText(SPJpushUtil.getProposal(MainActivity.this) + "");
                communityTag = "proposal";
            } else if (SPJpushUtil.getKnowledge(MainActivity.this) > 0) {
                community_img.setImageResource(R.drawable.nav_help);
                community_num.setText(SPJpushUtil.getKnowledge(MainActivity.this) + "");
                communityTag = "help";
            } else if (SPJpushUtil.getTrain(MainActivity.this) > 0) {
                community_img.setImageResource(R.drawable.nav_live);
                community_num.setText(SPJpushUtil.getTrain(MainActivity.this) + "");
                communityTag = "live";
            }
            LogUtil.e("主页面设置的communityTag为：：：" + communityTag);
        } else {
            //community_img.setImageResource(R.drawable.tab_help_s);
            if (toolbarBean.train) {
                community_img.setImageResource(R.drawable.nav_live);
            } else if (toolbarBean.knowledge) {
                community_img.setImageResource(R.drawable.nav_help);
            } else if (toolbarBean.proposal) {
                community_img.setImageResource(R.drawable.nav_advise);
            } else if (toolbarBean.bug) {//
                community_img.setImageResource(R.drawable.nav_bug);
            } else if (toolbarBean.community) {//社区图标未找到
                community_img.setImageResource(R.drawable.nav_live);
            }
            community_num.setVisibility(View.INVISIBLE);
            communityTag = "defult";
        }

        //功能
        functionNum = SPJpushUtil.getScan(MainActivity.this)
                + SPJpushUtil.getShoot(MainActivity.this)
                + SPJpushUtil.getSignin(MainActivity.this)
                + SPJpushUtil.getPayment(MainActivity.this);
        if (functionNum > 0) {
            function_num.setVisibility(View.VISIBLE);
            if (SPJpushUtil.getPayment(MainActivity.this) > 0) {
                function_img.setImageResource(R.drawable.nav_pay);
                function_num.setText(SPJpushUtil.getPayment(MainActivity.this) + "");
                functionTag = "pay";
                LogUtil.e("=========设置==========payment======================" + SPJpushUtil.getPayment(this));

            } else if (SPJpushUtil.getSignin(MainActivity.this) > 0) {
                function_img.setImageResource(R.drawable.nav_arrive);
                function_num.setText(SPJpushUtil.getSignin(MainActivity.this) + "");
                functionTag = "sign";
            } else if (SPJpushUtil.getShoot(MainActivity.this) > 0) {
                function_img.setImageResource(R.drawable.nav_photo);
                function_num.setText(SPJpushUtil.getShoot(MainActivity.this) + "");
                functionTag = "photo";
            } else if (SPJpushUtil.getScan(MainActivity.this) > 0) {
                function_img.setImageResource(R.drawable.nav_sao);
                function_num.setText(SPJpushUtil.getScan(MainActivity.this) + "");
                functionTag = "scaning";
            }
            LogUtil.e("主页面设置的functionTag为：：：" + functionTag);
        } else {
            if (toolbarBean.scan) {
                function_img.setImageResource(R.drawable.nav_sao);
            } else if (toolbarBean.shoot) {
                function_img.setImageResource(R.drawable.nav_photo);
            } else if (toolbarBean.signin) {
                function_img.setImageResource(R.drawable.nav_arrive);
            }
//            else if (toolbarBean.payment.size() > 0) {
//                function_img.setImageResource(R.drawable.nav_pay);
//            }
            function_num.setVisibility(View.INVISIBLE);
            functionTag = "defult";
        }
        //消息
        messageNum = SPJpushUtil.getMessage(MainActivity.this)
                + SPJpushUtil.getTask(MainActivity.this)
                + SPJpushUtil.getCrm(MainActivity.this)
                + SPJpushUtil.getApprove(MainActivity.this);


        if (messageNum > 0) {
            message_num.setVisibility(View.VISIBLE);
            if (SPJpushUtil.getApprove(MainActivity.this) > 0) {
                message_img.setImageResource(R.drawable.nav_shenpi);
                message_num.setText(SPJpushUtil.getApprove(MainActivity.this) + "");
                messageTag = "approve";
            } else if (SPJpushUtil.getCrm(MainActivity.this) > 0) {
                message_img.setImageResource(R.drawable.clientele_s);
                message_num.setText(SPJpushUtil.getCrm(MainActivity.this) + "");
                messageTag = "client";
            } else if (SPJpushUtil.getTask(MainActivity.this) > 0) {
                message_img.setImageResource(R.drawable.nav_renwu);
                message_num.setText(SPJpushUtil.getTask(MainActivity.this) + "");
                messageTag = "task";
            } else if (SPJpushUtil.getMessage(MainActivity.this) > 0) {
                message_img.setImageResource(R.drawable.nav_mag);
                message_num.setText(SPJpushUtil.getMessage(MainActivity.this) + "");
                messageTag = "message";
            }
            LogUtil.e("主页面设置的messageTag为：：：" + messageTag);
        } else {
            if (toolbarBean.message) {
                message_img.setImageResource(R.drawable.nav_mag);
            } else if (toolbarBean.task) {
                message_img.setImageResource(R.drawable.nav_renwu);
            } else if (toolbarBean.crm) {
                message_img.setImageResource(R.drawable.clientele_s);
            } else if (toolbarBean.approve) {
                message_img.setImageResource(R.drawable.nav_shenpi);
            }

            message_num.setVisibility(View.INVISIBLE);
            messageTag = "defult";
        }

        //用户
        userNum = SPJpushUtil.getMyself(MainActivity.this);
        if (userNum > 0) {
            user_num.setText(userNum + "");
            user_num.setVisibility(View.VISIBLE);
        } else {
            user_num.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 获取用来显示推送数据的TextView
     *
     * @return
     */
    public ArrayList<TextView> getPushNumber() {
        ArrayList<TextView> list = new ArrayList<>();
        list.add(community_num);
        list.add(function_num);
        list.add(message_num);
        list.add(user_num);
        return list;
    }


    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {
        /**
         * 接收广播
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            setPushNumber();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("main==onDestroy");


        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }

    }

    /**
     * 初始化定位
     */
    private void initMapLocation(int time) {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        SPUtil.setLat(MainActivity.this, amapLocation.getLatitude() + "");
                        SPUtil.setLong(MainActivity.this, amapLocation.getLongitude() + "");
                        SPUtil.setAddress(MainActivity.this, amapLocation.getAddress());
                        //T.showShort(MainActivity.this, amapLocation.getAddress());
                        sendLocation(amapLocation);
                        LogUtil.e("定位地址为" + amapLocation.getAddress());
                        LogUtil.e("纬度" + amapLocation.getLatitude());
                        LogUtil.e("经度" + amapLocation.getLongitude());
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtil.e("AmapErrorlocation Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(time * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 自动定位，发送位置信息
     */
    public void sendLocation(AMapLocation amapLocation) {
//        if (submitLock) {
//            return;
//        }
//        submitLock = true;
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("client_name", Constants.app_name);
//        params.addBodyParameter("phone_uuid", SPUtil.getUserPUUID(this));
//        params.addBodyParameter("single_code", SPUtil.getUserRandCode(this));
//        params.addBodyParameter("uuid", SPUtil.getUserUUID(this));
//        params.addBodyParameter("action", "submit");
//        params.addBodyParameter("method", "auto_position");
//        params.addBodyParameter("user_id", SPUtil.getUserId(this));
//        params.addBodyParameter("abs_x", amapLocation.getLongitude() + "");
//        params.addBodyParameter("abs_y", amapLocation.getLatitude() + "");
//        params.addBodyParameter("abs_z", "");
//        params.addBodyParameter("address", amapLocation.getAddress());
//
//        HttpPostUtil.getInstance().send(HttpRequest.HttpMethod.POST, AddressUtils.httpurl, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                submitLock = false;
//                try {
//                    JSONObject jsonObject = new JSONObject(responseInfo.result);
//                    if (jsonObject != null) {
//                        boolean success = jsonObject.optBoolean("success");
//                        if (success) {
//                            LogUtil.e("发送位置成功");
//                        } else {
//                            LogUtil.e("11" + jsonObject.optString("feedback"));
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                submitLock = false;
//            }
//
//        });
    }


    /**
     * 获取到MainActivity的实例---静态方法
     *
     * @return
     */
    public static MainActivity getMainActivity() {
        return mainActivity;
    }


    /**
     * 获取推送数量
     *
     * @param context
     */
    public void getPushCount(FragmentActivity context) {

        HttpPostUtils httpPostUtils = new HttpPostUtils();

        String url = "http://api.shuxinyun.com/cache/users/" + SPUtil.getUserId(context) + "/pushitemcount.json";

        LogUtil.e("推送数据url----------------" + url);

        RequestParams params = new RequestParams(url);

        httpPostUtils.get(context, params);

        httpPostUtils.OnCallBack(new HttpPostUtils.OnSetData() {
            @Override
            public void onSuccess(String strJson) {

                //TODO 防止后台给的数据是残疾的
                strJson = strJson.replace("\"\"", "0");
                LogUtil.e(strJson);

                ALiPushCountUtils.setPushCount(MainActivity.this, strJson);
                LogUtil.e("==========开始发送数据============" + strJson);
                //BroadcastCallUtil.sendMessage2Activity(MainActivity.this, strJson, GsonUtil.getPushBean(strJson));//发送消息,通知界面改数字
                LogUtil.e("==============结束发送数据=========");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("==========开始发送数据======错误======" + ex);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 底部栏推送消息接收器
     */
    private class ALiPushBottomBarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setBottomBarNum(context);
        }
    }

    /**
     * 设置底部栏推送数据
     *
     * @param context
     */
    private void setBottomBarNum(Context context) {
        if (mBottomIconViewList != null && mBottomIconViewList.size() > 0) {
            for (int i = 0; i < mBottomIconViewList.size(); i++) {
                if (mBottomIconViewList.get(i).getTag().equals("home_zdlf")) {
                    mBottomIconViewList.get(i).setPushCountNum(SPJpushUtil.getHome_zdlf(context));
                } else if (mBottomIconViewList.get(i).getTag().equals("work_items")) {
                    mBottomIconViewList.get(i).setPushCountNum(SPJpushUtil.getWork_items(context));
                } else if (mBottomIconViewList.get(i).getTag().equals("knowledge_zdlf")) {
                    mBottomIconViewList.get(i).setPushCountNum(SPJpushUtil.getKnowledge_zdlf(context));
                } else if (mBottomIconViewList.get(i).getTag().equals("communication_zdlf")) {
                    mBottomIconViewList.get(i).setPushCountNum(SPJpushUtil.getCommunication_zdlf(context));
                } else if (mBottomIconViewList.get(i).getTag().equals("mine_zdlf")) {
                    mBottomIconViewList.get(i).setPushCountNum(SPJpushUtil.getMine_zdlf(context));
                }
            }
        }
    }
}

package cn.net.bjsoft.sxdz.utils.function;

import android.os.Bundle;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.bean.app.AppBean;
import cn.net.bjsoft.sxdz.bean.app.HomepageBean;
import cn.net.bjsoft.sxdz.bean.app.ToolbarBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.fragment.WebViewFragment;
import cn.net.bjsoft.sxdz.fragment.barbotton.BottonMineFragment;
import cn.net.bjsoft.sxdz.fragment.barbotton.BottonScanFragment;
import cn.net.bjsoft.sxdz.fragment.barbotton.BottonUploadFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.community.TopCommunityFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.community.TopHelpNewFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.community.TopLiveFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopPayFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopPhotoFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopScanFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.function.TopSignFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopApproveFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopClientFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopMessageFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.message.TopTaskFragment;
import cn.net.bjsoft.sxdz.fragment.bartop.user.TopUserFragment;
import cn.net.bjsoft.sxdz.fragment.ylyd.BottonFormYuLongYaDongFragment;
import cn.net.bjsoft.sxdz.fragment.ylyd.BottonMineYuLongYaDongFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.KnowledgeZDLFFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.MineAddressListFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.MineZDLFFragment;
import cn.net.bjsoft.sxdz.fragment.zdlf.WorkFragment;
import cn.net.bjsoft.sxdz.utils.GsonUtil;

/**
 * Created by Zrzc on 2017/1/6.
 */

public class InitFragmentUtil {
    //    private static DatasBean mAppBeanBean;
//    private static DatasBean.DataDao mAppBean;
    private static AppBean mAppBean;
    private static ArrayList<BaseFragment> mFragmentlist;
    private static BaseFragment fragment;
    private static ArrayList<HomepageBean> mBottonList;
    private static String mJson;

    /**
     * 获取底部栏模块的初始化 Fragment
     *
     * @param json
     * @return
     */
    public static ArrayList<BaseFragment> getBottonFragments(String json) {
        mJson = json;
        mAppBean = GsonUtil.getAppBean(mJson);
        if (mAppBean.homepage != null) {
            mFragmentlist = null;
            mFragmentlist = new ArrayList<>();
            mBottonList = mAppBean.homepage;
            //向homepageList添加Fragment
            Bundle bundle = null;
            if (mBottonList.size() != 0) {
                for (int i = 0; i < mBottonList.size(); i++) {
                    if (fragment != null) {
                        fragment = null;
                        bundle = null;
                    }
                    bundle = new Bundle();
//                if (datasBean.data.authentication) {

                    if ((!mBottonList.get(i).tag.equals(""))/*||homepageList.get(i).linkto != null*/) {
                        if (mBottonList.get(i).tag.equals("message")) {
                            fragment = new TopMessageFragment();
                        } else if (mBottonList.get(i).tag.equals("communication")) {//通讯录
                            fragment = new MineAddressListFragment();
                        } else if (mBottonList.get(i).tag.equals("workflow")) {
                            fragment = new TopApproveFragment();
                        } else if (mBottonList.get(i).tag.equals("signin")) {
                            fragment = new TopSignFragment();
                        } else if (mBottonList.get(i).tag.equals("scaning")) {
                            fragment = new BottonScanFragment();
                        } else if (mBottonList.get(i).tag.equals("work_items")) {
                            fragment = new WorkFragment();//中电联发的工作界面
                        } else if (mBottonList.get(i).tag.equals("mine_zdlf")) {
                            fragment = new MineZDLFFragment();//中电联发的我界面
                        } else if (mBottonList.get(i).tag.equals("knowledge_zdlf")) {
                            fragment = new KnowledgeZDLFFragment();//中电联发的z知识界面
                            //fragment = new BottonNewsFragment_new();
                        } else if (mBottonList.get(i).tag.equals("upload")) {
                            fragment = new BottonUploadFragment();//上传
                        } else if (mBottonList.get(i).tag.equals("article")) {
                            //fragment = new BottonNewsFragment();//新闻页面
                            fragment = new TopHelpNewFragment();//新闻页面
                            bundle.putString("type", "news");
                        } else if (mBottonList.get(i).tag.equals("form")) {
                            fragment = new WebViewFragment();
                        } else if (mBottonList.get(i).tag.equals("workflow")) {
                            fragment = new TopApproveFragment();//
                        } else if (mBottonList.get(i).tag.equals("task")) {
                            fragment = new TopTaskFragment();//
                        } else if (mBottonList.get(i).tag.equals("form_ylyd")) {
                            fragment = new BottonFormYuLongYaDongFragment();
                        } else if (mBottonList.get(i).tag.equals("mine_ylyd")) {
                            fragment = new BottonMineYuLongYaDongFragment();
                        } else if (mBottonList.get(i).tag.equals("mine_rcjg")) {
                            fragment = new BottonMineFragment();
                        } else if (mBottonList.get(i).tag.equals("mine")) {
//                            if (mAppBean.user.logined) {
                            //fragment = new BottonMineFragment();
                            fragment = new TopUserFragment();
                            ArrayList<String> list = new ArrayList<String>();
//                                list.add(mAppBean.user.avatar);
//                                list.add(mAppBean.user.name);
//                                list.add(mAppBean.user.email);
//                                list.add(mAppBean.user.birthday);
//                                list.add(mAppBean.user.phone);
//                                bundle.putStringArrayList("detail", list);
//                            } else {
//                                fragment = new LoginFragment();
//                            }
                        } else {
                            fragment = new WebViewFragment();
                        }
                    } else {
                        fragment = new WebViewFragment();
                        LogUtil.e("fragmentl&&&&&&&&&&&" + fragment.toString());
                    }
                    if (mBottonList.get(i).tag.equals("")) {
                        if (mBottonList.get(i).url.equals("")) {
                            bundle.putString("tag", "empty");
                        } else {
                            bundle.putString("tag", "url");
                        }
                    } else {
                        bundle.putString("tag", mBottonList.get(i).tag);
                    }
                    bundle.putString("tag", mBottonList.get(i).tag);
                    bundle.putString("linkto", mBottonList.get(i).linkto);
                    bundle.putString("url", mBottonList.get(i).url);
                    bundle.putString("text", mBottonList.get(i).title);
                    bundle.putString("json", mJson);
                    fragment.setArguments(bundle);
                    mFragmentlist.add(fragment);
                }
            } else {

                fragment = new WebViewFragment();
                // LogUtil.e("创建了" + mBottonList.get(i).linkto);
                bundle = new Bundle();
                bundle.putString("text", "");
                bundle.putString("url", "");
                bundle.putString("tag", "empty");
                bundle.putString("json", mJson);
                fragment.setArguments(bundle);

                //fragment.setTa
                mFragmentlist.add(fragment);
            }
            return mFragmentlist;
        } else {
            return null;
        }
    }

    /**
     * 获取社区模块的初始化 Fragment
     *
     * @param json
     * @return
     */

    private static ToolbarBean mToolBarBean;

    public static ArrayList<BaseFragment> getCommunityFragments(String json) {
        mJson = json;
        mAppBean = GsonUtil.getAppBean(mJson);
        mToolBarBean = mAppBean.toolbar;

        mFragmentlist = null;
        mFragmentlist = new ArrayList<>();

        if (mToolBarBean.train) {
            addFragment2List("live", new TopLiveFragment());
        }
        if (mToolBarBean.knowledge) {
//            addFragment2List("help", new TopHelpFragment());//旧的--已弃用
            BaseFragment baseFragment = new TopHelpNewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "help");
            baseFragment.setArguments(bundle);
            addFragment2List("help", baseFragment);

        }
        if (mToolBarBean.proposal) {//建议
            BaseFragment baseFragment = new TopHelpNewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "proposal");
            baseFragment.setArguments(bundle);
            addFragment2List("proposal", baseFragment);
        }
        if (mToolBarBean.bug) {//报错
            BaseFragment baseFragment = new TopHelpNewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "disabuse");
            baseFragment.setArguments(bundle);
            addFragment2List("disabuse", baseFragment);
        }
        //社区模块，暂时没有后台数据控制
        if (mToolBarBean.community) {
            addFragment2List("community", new TopCommunityFragment());
        }

        return mFragmentlist;
    }


    /**
     * 获取功能模块的初始化 Fragment
     *
     * @param json
     * @return
     */
    public static ArrayList<BaseFragment> getFunctionFragments(String json) {
        mJson = json;
        mAppBean = GsonUtil.getAppBean(mJson);
        mToolBarBean = mAppBean.toolbar;

        mFragmentlist = null;
        mFragmentlist = new ArrayList<>();

        if (mToolBarBean.scan) {
            addFragment2List("scaning", new TopScanFragment());
        }
        if (mToolBarBean.shoot) {
            addFragment2List("photo", new TopPhotoFragment());
        }
        if (mToolBarBean.signin) {
            addFragment2List("sign", new TopSignFragment());
        }
        if (mToolBarBean.payment.size() > 0) {
            addFragment2List("pay", new TopPayFragment());
        }

        return mFragmentlist;
    }

    /**
     * 获取--消息--模块的初始化 Fragment
     *
     * @param json
     * @return
     */
    public static ArrayList<BaseFragment> getMessageFragments(String json) {
        mJson = json;
        mAppBean = GsonUtil.getAppBean(mJson);
        mToolBarBean = mAppBean.toolbar;

        mFragmentlist = null;
        mFragmentlist = new ArrayList<>();

        if (mToolBarBean.message) {
            addFragment2List("message", new TopMessageFragment());
        }
        if (mToolBarBean.task) {
            addFragment2List("task", new TopTaskFragment());
        }
        if (mToolBarBean.crm) {
            addFragment2List("client", new TopClientFragment());
        }
        if (mToolBarBean.approve) {
            addFragment2List("approve", new TopApproveFragment());
        }

        return mFragmentlist;
    }


    /**
     * 把Fragment添加到列表中
     *
     * @param tag
     * @param itemFragment
     */
    private static void addFragment2List(String tag, BaseFragment itemFragment) {
        Bundle bundle = null;
        if (bundle != null) {
            bundle = null;
        }
        bundle = new Bundle();
        bundle.putString("tag", tag);

        bundle.putString("json", mJson);
        itemFragment.setArguments(bundle);
        mFragmentlist.add(itemFragment);
    }
}

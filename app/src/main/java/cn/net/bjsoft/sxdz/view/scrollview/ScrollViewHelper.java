package cn.net.bjsoft.sxdz.view.scrollview;

/**
 * Created by Zrzc on 2017/3/16.
 */





public class ScrollViewHelper {

//    //初始化轮播图-----------------------------开始--------------------------
//    private void setRollViewPage() {
//        //判断当前的轮播图中是否有数据，有数据做后续操作
//        if (scroll_list.size() > 0) {
//
//            //通过顶部轮播图的数据填充界面
//            RollViewPager rollViewPager = new RollViewPager(mActivity, viewList,
//                    new RollViewPager.OnViewClickListener() {
//                        @Override
//                        public void viewClickListener(String imgUrl) {
//                            //在当前的方法中要去获取图片的链接地址
//                            MyToast.showShort(mActivity, imgUrl);
//                        }
//                    });
//
//            titleList.clear();
//            imgUrlList.clear();
//            for (int i = 0; i < scroll_list.size(); i++) {
//                titleList.add(scroll_list.get(i).file_text);
//                imgUrlList.add(scroll_list.get(i).image_url);
//
//            }
//            //点
//            initDot();
//            //文字
//            rollViewPager.initTitle(titleList, top_title);
//            //图片
//            rollViewPager.initImgUrl(imgUrlList);
//            rollViewPager.startRoll();
//            //底部一般列表页的数据填充界面
//
//            //先将轮播图所在的线性布局内部内容全部移除掉
//            top_viewpager.removeAllViews();
//            //将轮播图对应的对象添加到线性布局中去
//            top_viewpager.addView(rollViewPager);
//
////			if(lv_item_news.getHeaderViewsCount()<1){
////				lv_item_news.addHeaderView(layout_roll_view);
////			}
//        } else {
//            top_viewpager.setVisibility(View.GONE);
//        }
//
//    }
//
//    private void initDot() {
//        //dots_ll添加点的线性布局
//        dots_ll.removeAllViews();
//        //安放点（view）对象的集合 viewList
//        viewList.clear();
//
//        for (int i = 0; i < imgUrlList.size(); i++) {
//            ImageView view = new ImageView(mActivity);
////            View view = View.inflate(getContext(), R.layout.viewpager_item_dot, null);
////            ImageView imageView = (ImageView) view.findViewById(R.id.item_img_dot);
//            if (i == 0) {
//                //构建第一个点
//                view.setImageResource(R.drawable.page_on);
//            } else {
//                //后续的点
//                view.setImageResource(R.drawable.page_off);
//            }
//            //将点放置到其所在的线性布局中
//
//            //将宽高定义在夫控件上面，作用在子控件上面
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            //控件间距
//            layoutParams.setMargins(0, 0, 6, 0);
//            dots_ll.addView(view, layoutParams);
//
//            viewList.add(view);
//        }
//    }

    //初始化轮播图结束-------------------------------------------------------
}

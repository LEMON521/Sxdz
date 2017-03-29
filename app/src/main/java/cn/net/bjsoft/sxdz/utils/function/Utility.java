package cn.net.bjsoft.sxdz.utils.function;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by 靳宁 on 2017/2/22.
 */

public class Utility {

    /**
     * 设置ListView高度
     *
     * @param listView 将要设置高度的ListView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = getListViewHeightBasedOnChildren(listView);
        listView.setLayoutParams(params);
    }

    /**
     * 根据ListView中的条目数获取ListView的高度
     *
     * @param listView 将要计算高度的ListView
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        int heights = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        return heights;
    }

    /**
     * 根据GridView中的条目数获取GridView的高度
     *
     * @param col      每行的个数
     * @param gridView 将要计算高度的GridView
     * @return
     */
    public static int getGridViewHeightBasedOnChildren(GridView gridView, int col) {
        ListAdapter listAdapter = gridView.getAdapter();
        //col = 4;//每行的个数
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            if (listAdapter.getCount() > col) {//第二行的时候才加上间距
                totalHeight += gridView.getVerticalSpacing();//行间距
            }
        }
        //int heights = totalHeight + (gridView.getVerticalSpacing() * (listAdapter.getCount()/col+1));
        return totalHeight;
    }

    /**
     * 设置GridView高度
     *
     * @param gridView 将要设置高度的GridView
     * @param col      GridView每行的条目数
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int col) {
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = getGridViewHeightBasedOnChildren(gridView, col);
        gridView.setLayoutParams(params);
    }
}

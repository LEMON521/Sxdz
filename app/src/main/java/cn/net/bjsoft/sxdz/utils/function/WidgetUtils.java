package cn.net.bjsoft.sxdz.utils.function;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Zrzc on 2017/2/17.
 */

public class WidgetUtils {


    /**
     * 获取View的宽高 效率==3>2>1 消耗内存：1>2>3
     * @param whitch 用哪一种方法
     * @param view 将要获取宽高的控件
     * @return 控件的宽高{宽,高}
     */
    public static int[] getWidthHigh(int whitch, View view) {

        int[] result = {0,0};
        switch (whitch){
            case 1:
                result = getWidthHigh1(view);
               break;
            case 2:
                result =  getWidthHigh2(view);
                break;
            case 3:
                result = getWidthHigh3(view);
                break;
        }
        return result;
    }

    public static int[] getWidthHigh1(View view) {

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return new int[]{width, height};
    }

    public static int[] getWidthHigh2(final View view) {
        final int[] result = {0,0};
        int height = 0;
        int width = 0;
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int height = view.getMeasuredHeight();
                int width = view.getMeasuredWidth();
                result[0] = height;
                result[1] = width;
                return true;
            }
        });

        return result;
    }

    public static int[] getWidthHigh3(final View view) {
        final int[] result = {0,0};
        int height = 0;
        int width = 0;
        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = view.getMeasuredHeight();
                int width = view.getMeasuredWidth();
                result[0] = height;
                result[1] = width;
            }
        });
        return result;
    }
}

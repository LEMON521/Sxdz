package cn.net.bjsoft.sxdz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Zrzc on 2017/3/21.
 */

public class ChildrenListView extends ListView {

    public  ChildrenListView  (Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public  ChildrenListView  (Context context) {

        super(context);

    }

    public  ChildrenListView  (Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }

    @Override

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}

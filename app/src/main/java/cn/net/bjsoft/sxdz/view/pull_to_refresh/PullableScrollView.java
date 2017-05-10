package cn.net.bjsoft.sxdz.view.pull_to_refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Zrzc on 2017/5/10.
 */

public class PullableScrollView extends ScrollView implements Pullable {


    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //    scrollView.fullScroll(ScrollView.FOCUS_DOWN);滚动到底部
//scrollView.fullScroll(ScrollView.FOCUS_UP);滚动到顶部
    @Override
    public boolean canPullDown() {
        if (this.fullScroll(ScrollView.FOCUS_UP))
            return true;
        else return false;
    }

    @Override
    public boolean canPullUp() {
        if (this.fullScroll(ScrollView.FOCUS_DOWN))
            return true;
        else return false;
    }
}

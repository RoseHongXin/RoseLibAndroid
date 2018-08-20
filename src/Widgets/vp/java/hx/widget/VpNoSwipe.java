package hx.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rose on 9/27/2016.
 */
public class VpNoSwipe extends ViewPager {

    boolean mSwipeScrollEnabled = false;
    boolean mSwitchAnimationEnabled = false;

    public void setSwipeScrollEnabled(boolean enabled){
        this.mSwipeScrollEnabled = enabled;
    }

    public void setSwitchAnimationEnabled(boolean enabled) {
        this.mSwitchAnimationEnabled = enabled;
    }

    public VpNoSwipe(Context context) {
        super(context);
    }

    public VpNoSwipe(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mSwipeScrollEnabled && super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mSwipeScrollEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
//        super.setCurrentItem(item, false);
        super.setCurrentItem(item, smoothScroll);
    }

    //去除页面切换时的滑动翻页效果
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, mSwitchAnimationEnabled);
    }
}

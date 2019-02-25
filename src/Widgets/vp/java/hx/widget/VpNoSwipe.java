package hx.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrHandler2;
import in.srain.cube.views.ptr.util.PtrCLog;

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
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return mSwipeScrollEnabled && super.onInterceptTouchEvent(ev);
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        if (!isEnabled() || mContent == null || mHeaderView == null) {return super.onInterceptTouchEvent(e);}
//        int action = e.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mHasSendCancelEvent = false;
//                mPtrIndicator.onPressDown(e.getX(), e.getY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mLastMoveEvent = e;
//                mPtrIndicator.onMove(e.getX(), e.getY());
//                float offsetX = mPtrIndicator.getOffsetX();
//                float offsetY = mPtrIndicator.getOffsetY();
//
//                boolean moveDown = offsetY > 0;
//                boolean moveUp = !moveDown;
//                boolean canMoveUp = mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition(); // if the header is showing
//                boolean canMoveDown = mFooterView != null && !mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition(); // if the footer is showing
//                boolean canHeaderMoveDown = mPtrHandler != null && mPtrHandler.checkCanDoRefresh(this, mContent, mHeaderView) && (mMode.ordinal() & 1) > 0;
//                boolean canFooterMoveUp = mPtrHandler != null && mFooterView != null // The footer view could be null, so need double check
//                        && mPtrHandler instanceof PtrHandler2 && ((PtrHandler2) mPtrHandler).checkCanDoLoadMore(this, mContent, mFooterView) && (mMode.ordinal() & 2) > 0;
//                if (!canMoveUp && !canMoveDown && ((moveDown && !canHeaderMoveDown) || (moveUp && !canFooterMoveUp))) {
//                    if (DEBUG) { PtrCLog.v(LOG_TAG, "onInterceptTouchEvent: block this ACTION_MOVE"); }
//                    return super.onInterceptTouchEvent(e);
////                    super.onInterceptTouchEvent(e);
////                    return false;
//                }
//                if(Math.abs(offsetY) > mPagingTouchSlop){
//                    if (DEBUG) { PtrCLog.v(LOG_TAG, "onInterceptTouchEvent: ACTION_MOVE dispatch onTouchEvent."); }
//                    return true;
//                }
//        }
//        return super.onInterceptTouchEvent(e);
//    }

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

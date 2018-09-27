package hx.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import hx.kit.log.Log4Android;

/**
 * Created by RoseHongXin on 2018/4/3 0003.
 */

public class TouchSwipe2LeftLayout extends LinearLayout{

    private static final int SCROLL_DURATION = 480;

    private float mDownX, mDownY;
    private float mMoveX;
    private Scroller mScroller;
    private boolean mAnchorShow = false;
    private int mTouchSlop;
    private int mClickSlop;

    private View _v_anchor;
    private View _v_anchorLeft;

    private int mAnchorWidth;
    private int mAnchorShowThreshold;
    private int mAnchorHideThreshold;


    public TouchSwipe2LeftLayout(Context context) {
        this(context, null);
    }

    public TouchSwipe2LeftLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchSwipe2LeftLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mClickSlop = mTouchSlop / 4;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        _v_anchor = getChildAt(1);
        if(_v_anchor == null) return;
        _v_anchorLeft = getChildAt(0);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) _v_anchor.getLayoutParams();
        int width = 0;
        if(_v_anchor instanceof ViewGroup){
            View child;
            for(int i = 0; i < ((ViewGroup) _v_anchor).getChildCount(); i++) {
                child = ((ViewGroup) _v_anchor).getChildAt(i);
                width += calculateWidth(child);
            }
        }else {
            width = calculateWidth(_v_anchor);
        }
        layoutParams.width = (int) (width);
        _v_anchor.setLayoutParams(layoutParams);
    }

    private int calculateWidth(View _v_){
        int width = 0;
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) _v_.getLayoutParams();
        if(_v_ instanceof TextView) width += (int) ((TextView)_v_).getPaint().measureText(((TextView) _v_).getText().toString());
        else if(_v_ instanceof ImageView) width += ((ImageView) _v_).getDrawable().getIntrinsicWidth();
        width += _v_.getPaddingLeft() + _v_.getPaddingRight();
        layoutParams.width = width;
        _v_.setLayoutParams(layoutParams);
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mAnchorWidth = _v_anchor.getMeasuredWidth();
        mAnchorShowThreshold = (int) (mAnchorWidth * 0.2f);
        mAnchorHideThreshold = mAnchorShowThreshold / 2;
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mDownX = ev.getRawX();
//                mDownY = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mMoveX = ev.getRawX();
//                if (Math.abs(mMoveX - mDownX) > mTouchSlop) {
//                    int moved = (int) (mMoveX - mDownX);
//                    if (mAnchorShow) {
//                        moved -= mAnchorWidth;
//                    }
//                    scrollTo(-moved, 0);
//                    if (getScrollX() <= 0) {
//                        scrollTo(0, 0);
//                    } else if (getScrollX() >= mAnchorWidth) {
//                        scrollTo(mAnchorWidth, 0);
//                    }
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                float x = ev.getRawX();
//                float y = ev.getRawY();
//                float xDelta = x - mDownX;
//                if (xDelta < 0 && getScrollX() > mAnchorShowThreshold) {
//                    smoothScrollTo(mAnchorWidth, 0);
//                    mAnchorShow = true;
//                } else{
//                    reset();
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
//            case MotionEvent.ACTION_MOVE:
//                mMoveX = ev.getRawX();
//                if (Math.abs(mMoveX - mDownX) > mTouchSlop) {
////                    getParent().requestDisallowInterceptTouchEvent(true);
//                    requestDisallowInterceptTouchEvent(true);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
////                if(Math.abs(ev.getRawY() - mDownY) < mClickSlop && Math.abs(ev.getRawX() - mDownX) < mClickSlop) {
//                if(ev.getRawY() == mDownY && ev.getRawX() == mDownX) {
//                    if(mAnchorShow && touchInAnchorBound(ev)) _v_anchor.callOnClick();
//                    else{
//                        callOnClick();
//                    }
//                    reset();
//                }
//                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                if (Math.abs(mMoveX - mDownX) > mTouchSlop) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
                    requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScroller.isFinished()){return false;}
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) ev.getRawX();
                int moved = (int) (mMoveX - mDownX);
                if (mAnchorShow) {
                    moved -= mAnchorWidth;
                }
                scrollTo(-moved, 0);
                if (getScrollX() <= 0) {
                    scrollTo(0, 0);
                } else if (getScrollX() >= mAnchorWidth) {
                    scrollTo(mAnchorWidth, 0);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float x = ev.getRawX();
                float y = ev.getRawY();
                float xDelta = x - mDownX;
                if (xDelta < 0 && getScrollX() > mAnchorShowThreshold) {
                    smoothScrollTo(mAnchorWidth, 0);
                    mAnchorShow = true;
                    return true;
                } else if(xDelta != 0){
                    reset();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
//        return true;
    }

    public boolean touchInAnchorBound(MotionEvent ev) {
        int[] location = new int[2];
        _v_anchor.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + _v_anchor.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + _v_anchor.getHeight())) {
            return false;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, SCROLL_DURATION);
        postInvalidate();
    }

    public void reset(){
        smoothScrollTo(0,0);
        mAnchorShow = false;
    }
}

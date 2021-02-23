package hx.view.softtouch;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by RoseHongXin on 2017/6/23 0023.
 */

public class SoftTouchDelegate{

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final int LEFT = 0x11;
    public static final int RIGHT = 0x22;
    public static final int UP = 0x33;
    public static final int DOWN = 0x44;

    private static final float DEFAULT_TOUCH_RATIO = 0.75f;
    private static final int TOUCH_THRESHOLD_DISABLE = -1;

    public Rect mRectOriginal;
    private int mOrientation;
    private TouchListener mTouchListener;
    private float mTouchRatio = DEFAULT_TOUCH_RATIO;
    private int mTouchThreshold = TOUCH_THRESHOLD_DISABLE;

    public ViewGroup _vg_;
    public TouchStateImpl mTouchStateImpl;
    private float mLastX;
    private float mLastY;

    public SoftTouchDelegate(ViewGroup _vg_) {
        this._vg_ = _vg_;
        mTouchStateImpl = new TouchStateImpl();
//        mVelocityTracker = VelocityTracker.obtain();
        mRectOriginal = new Rect();
        _vg_.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            mRectOriginal.left = _vg_.getLeft();
            mRectOriginal.top = _vg_.getTop();
            mRectOriginal.right = _vg_.getRight();
            mRectOriginal.bottom = _vg_.getBottom();
        });
    }

    public SoftTouchDelegate rect(Rect rect){
        this.mRectOriginal = rect;
        return this;
    }
    public SoftTouchDelegate listener(TouchListener listener){
        this.mTouchListener = listener;
        return this;
    }
    public SoftTouchDelegate direction(int orientation){
        this.mOrientation = orientation;
        return this;
    }
    public SoftTouchDelegate touchRatio(float ratio){
        this.mTouchRatio = ratio;
        return this;
    }
    public SoftTouchDelegate touchThreshold(int threshold){
        this.mTouchThreshold = threshold;
        return this;
    }

    public void onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = event.getX();
            mLastY = event.getY();
//            mTouchListener.beforeTouchMove();
        } else if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            int actionIndex = event.getActionIndex();
            mLastX = event.getX(actionIndex);
            mLastY = event.getY(actionIndex);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float x = event.getX();
                int yDelta = (int) ((y - mLastY) * mTouchRatio + 0.5f);
                int xDelta = (int) ((x - mLastX) * mTouchRatio + 0.5f);
//                if (mOrientation == RecyclerView.HORIZONTAL && (mTouchThreshold == TOUCH_THRESHOLD_DISABLE || Math.abs(xDelta) > mTouchThreshold) && mTouchStateImpl.horizontalOffset(xDelta)) {
                if (mOrientation == HORIZONTAL && mTouchStateImpl.horizontalOffset(xDelta)) {
                    mTouchListener.beforeTouchMove();
                    mTouchListener.horizontalTouch(mLastX, x, xDelta);
                } else if (mOrientation == VERTICAL && mTouchStateImpl.verticalOffset(yDelta)) {
                    mTouchListener.beforeTouchMove();
                    mTouchListener.verticalTouch(mLastY, y, yDelta);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchStateImpl.isHorizontalDrag()) {
                    mTouchListener.horizontalRelease(mTouchStateImpl.leftDrag() ? LEFT : RIGHT);
                } else if (mTouchStateImpl.isVerticalDrag()) {
                    mTouchListener.verticalRelease(mTouchStateImpl.topDrag() ? UP : DOWN);
                }else {
                    _vg_.performClick();
                }
                mTouchStateImpl.idle();
                break;
        }
    }

    public static class TouchListener {
        public void beforeTouchMove(){}
        public void horizontalTouch(float from, float to, int delta){}
        public void verticalTouch(float from, float to, int delta){}
        public void horizontalRelease(int direction){}
        public void verticalRelease(int direction){}
    }

}

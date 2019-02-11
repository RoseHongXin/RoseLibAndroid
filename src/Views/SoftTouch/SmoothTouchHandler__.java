package hx.view.smoothtouch;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import hx.view.softtouch.TouchStateImpl;
import hx.view.softtouch.SmoothTouchConst;

/**
 * Created by RoseHongXin on 2018/3/13 0013.
 */

public class SmoothTouchHandler__ {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private final int INVALID_POINTER_ID = -1;

    private float mDragRatio = SmoothTouchConst.DEFAULT_DRAG_RATIO;
    private float mMaximumTouchVelocity;

    private Rect mRectOriginalPosition;
    private float mLastX;
    private float mLastY;
    private float mInitDownX;
    private float mInitDownY;
    private int mActivePointerId;
    private boolean mTouchFling = false;

    private TranslateAnimation mReleaseAnim;
    private TouchStateImpl mTouchStateImpl;
    private VelocityTracker mVelocityTracker;

    private ViewGroup _vg;
    private int mOrientation;

    private SmoothTouchHandler__(@NonNull ViewGroup _vg, int orientation){
        this._vg = _vg;
        this.mOrientation = orientation;
        if(_vg instanceof RecyclerView) {}
        else if(_vg instanceof AbsListView){}
        else if(_vg instanceof HorizontalScrollView) {
            this.mOrientation = LinearLayoutCompat.HORIZONTAL;
        }
        else if(_vg instanceof ScrollView) {
            this.mOrientation = LinearLayoutCompat.VERTICAL;
        }
        else throw new IllegalArgumentException("View must be a RecyclerView or ScrollView or AbsListView.");
        final int[] globalLayoutCallbackCount = {0};
        _vg.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(globalLayoutCallbackCount[0]++ == 0) return;
            init();
            addTouchListener();
        });
    }
    public static SmoothTouchHandler__ handle(@NonNull ViewGroup _vg){
        return handle(_vg, LinearLayoutCompat.VERTICAL);
    }
    public static SmoothTouchHandler__ handle(@NonNull ViewGroup _vg, int orientation){
        return new SmoothTouchHandler__(_vg, orientation);
    }

    private void init(){
//        mTouchSlop = ViewConfiguration.get(_vg.getContext()).getScaledTouchSlop();
        mTouchStateImpl = new TouchStateImpl();
        mRectOriginalPosition = new Rect();
        mRectOriginalPosition.left = _vg.getLeft();
        mRectOriginalPosition.top = _vg.getTop();
        mRectOriginalPosition.right = _vg.getRight();
        mRectOriginalPosition.bottom = _vg.getBottom();
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumTouchVelocity = ViewConfiguration.get(_vg.getContext()).getScaledMaximumFlingVelocity();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener() {
        _vg.setOnTouchListener(((v, event) -> {
            if(mReleaseAnim != null && !mReleaseAnim.hasEnded()) return true;
            int actionIndex = MotionEventCompat.getActionIndex(event);
            mVelocityTracker.addMovement(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mInitDownX = mLastX = event.getX();
                    mInitDownY = mLastY = event.getY();
                    mActivePointerId = event.getPointerId(0);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mInitDownX = mLastX = event.getX(actionIndex);
                    mInitDownY = mLastY = event.getY(actionIndex);
                    mActivePointerId = event.getPointerId(actionIndex);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float y = event.getY();
                    float x = event.getX();
                    int yDelta = (int) ((y - mLastY) * mDragRatio + 0.5f);
                    int xDelta = (int) ((x - mLastX) * mDragRatio + 0.5f);
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumTouchVelocity);
                    if (mOrientation == LinearLayoutCompat.HORIZONTAL && mTouchStateImpl.horizontalOffset(xDelta)) {
                        float xVel = mVelocityTracker.getXVelocity(mActivePointerId);
                        if(Math.abs(xVel) > SmoothTouchConst.MAXIMUM_HORIZONTAL_TOUCH_VELOCITY || Math.abs(xDelta) > SmoothTouchConst.MAXIMUM_HORIZONTAL_TOUCH_DELTA) {
                            mTouchFling = true;
//                            return true;
                        }
//                        _vg.layout(_vg.getLeft() + xDelta, mRectOriginal.top, _vg.getRight() + xDelta, mRectOriginal.bottom);
                        ViewCompat.offsetLeftAndRight(_vg, xDelta);
                    } else if (mOrientation == LinearLayoutCompat.VERTICAL && mTouchStateImpl.verticalOffset(yDelta)) {
                        float yVel = mVelocityTracker.getYVelocity(mActivePointerId);
                        if(Math.abs(yVel) > SmoothTouchConst.MAXIMUM_VERTICAL_TOUCH_VELOCITY || Math.abs(yDelta) > SmoothTouchConst.MAXIMUM_VERTICAL_TOUCH_DELTA) {
                            mTouchFling = true;
//                            return true;
                        }
//                        _vg.layout(mRectOriginal.left, _vg.getTop() + yDelta, mRectOriginal.right, _vg.getBottom() + yDelta);
                        ViewCompat.offsetTopAndBottom(_vg, yDelta);
                    }
                    mLastX = x;
                    mLastY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTouchStateImpl.isVerticalDrag()) {
                        yDelta = (int) ((mLastY - mInitDownY) * mDragRatio + 0.5f);
                        mReleaseAnim = new TranslateAnimation(0, 0, yDelta, 0);
                        startAnim();
                    } else if (mTouchStateImpl.isHorizontalDrag()) {
                        xDelta = (int) ((event.getX() - mInitDownX) * mDragRatio + 0.5f);
                        mReleaseAnim = new TranslateAnimation(xDelta, 0, 0, 0);
                        startAnim();
                    } else {
                        mTouchStateImpl.idle();
                    }
                    mVelocityTracker.clear();
                    mActivePointerId = INVALID_POINTER_ID;
                    if(mTouchFling){
                        mTouchFling = false;
                        return true;
                    }
                    break;
            }
            return false;
        }));
    }

    private void startAnim(){
        mReleaseAnim.setDuration(SmoothTouchConst.ANIMATION_DURATION);
        mReleaseAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                mTouchStateImpl.idle();
                mReleaseAnim = null;
            }
            @Override public void onAnimationRepeat(Animation animation) {}
        });
        mTouchStateImpl.release();
        _vg.layout(mRectOriginalPosition.left, mRectOriginalPosition.top, mRectOriginalPosition.right, mRectOriginalPosition.bottom);
        _vg.startAnimation(mReleaseAnim);
    }
}

package hx.view.softtouch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import hx.kit.log.Log4Android;

/**
 * Created by RoseHongXin on 2017/6/23 0023.
 */

public class SmoothTouchDelegate {

    public static final int EVENT_CONSUME = 1;
    public static final int EVENT_IGNORE = 2;

    private static final int ANIMATION_DURATION = 800;
    private static final float DEFAULT_DRAG_RATIO = 0.65f;
    private static final int TOUCH_VELOCITY_PIXEL = 500;
    private static final float MAXIMUM_VERTICAL_TOUCH_VELOCITY = 10000.0f;
    private static final float MAXIMUM_HORIZONTAL_TOUCH_VELOCITY = 7000.0f;
    private static final int OFFSET_PIXEL = 10;
    private static final int MAXIMUM_VERTICAL_TOUCH_DELTA = 300;
    private static final int MAXIMUM_HORIZONTAL_TOUCH_DELTA = 180;

    private float mMaximumTouchVelocity;
    private float mTouchSlop;
    private float mMinVelocityFling;

    private Rect mRectOriginalPosition;
    private float mLastX;
    private float mLastY;
    private int mActivePointerId;

    private int mOrientation;

    private TouchStateImpl mTouchStateImpl;
    private VelocityTracker mVelocityTracker;
    private AnimatorSet mReleaseAnims;

    private ViewGroup _vg_;

    public SmoothTouchDelegate(Context context, ViewGroup _vg_) {
        this._vg_ = _vg_;
        mTouchStateImpl = new TouchStateImpl();
        mRectOriginalPosition = new Rect();
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumTouchVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMinVelocityFling = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        ViewTreeObserver.OnGlobalFocusChangeListener globalFocusChangeListener = (oldFocus, newFocus) -> {
            mRectOriginalPosition.left = _vg_.getLeft();
            mRectOriginalPosition.top = _vg_.getTop();
            mRectOriginalPosition.right = _vg_.getRight();
            mRectOriginalPosition.bottom = _vg_.getBottom();
        };
        _vg_.getViewTreeObserver().addOnGlobalFocusChangeListener(globalFocusChangeListener);
    }

    public void smooth(int orientation){
        this.mOrientation = orientation;
    }

    public void interceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = event.getX();
            mLastY = event.getY();
            Log4Android.d(this, "*************" + mLastX);
            mActivePointerId = event.getPointerId(0);
            if(mReleaseAnims != null && !mReleaseAnims.isRunning()) {
                Log4Android.v(this, "====================");
                mReleaseAnims.cancel();
            }
        } else if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            int actionIndex = event.getActionIndex();
            mLastX = event.getX(actionIndex);
            mLastY = event.getY(actionIndex);
            mActivePointerId = event.getPointerId(actionIndex);
            if(mReleaseAnims != null && !mReleaseAnims.isRunning()){
                mReleaseAnims.cancel();
            }
        }
    }

    public int onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float x = event.getX();
                int yDelta = (int) ((y - mLastY) * DEFAULT_DRAG_RATIO + 0.5f);
                int xDelta = (int) ((x - mLastX) * DEFAULT_DRAG_RATIO + 0.5f);
                Log4Android.i(this, x + "-----------" + xDelta + "====" + (mReleaseAnims != null && mReleaseAnims.isRunning()));
                mVelocityTracker.computeCurrentVelocity(TOUCH_VELOCITY_PIXEL);
                if (mOrientation == RecyclerView.HORIZONTAL) {
                    float xVel = mVelocityTracker.getXVelocity(mActivePointerId);
//                    if(!_vg_.canScrollHorizontally(xDelta) || Math.abs(xVel) > mMaximumTouchVelocity) {
//                        mLastX = x;
//                        break;
//                    }
                    mTouchStateImpl.horizontalOffset(xDelta);
                    ViewCompat.offsetLeftAndRight(_vg_, xDelta);
                    return EVENT_CONSUME;
                } else if (mOrientation == RecyclerView.VERTICAL) {
                    float yVel = mVelocityTracker.getYVelocity(mActivePointerId);
                    if(!_vg_.canScrollVertically(yDelta) || Math.abs(yVel) > MAXIMUM_VERTICAL_TOUCH_VELOCITY) break;
                     mTouchStateImpl.verticalOffset(yDelta);
                    ViewCompat.offsetTopAndBottom(_vg_, yDelta);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchStateImpl.isVerticalDrag()) {
                    startVerticalAnim();
                } else if (mTouchStateImpl.isHorizontalDrag()) {
                    startHorizontalAnim();
                } else {
                    mTouchStateImpl.idle();
                }
                mVelocityTracker.clear();
                break;
        }
        return EVENT_IGNORE;
    }

    private void startHorizontalAnim(){
        if(!mTouchStateImpl.isHorizontalDrag()){ return; }
        ObjectAnimator leftAnim = ObjectAnimator.ofInt(_vg_, "left", _vg_.getLeft(), mRectOriginalPosition.left);
        ObjectAnimator rightAnim = ObjectAnimator.ofInt(_vg_, "right", _vg_.getRight(), mRectOriginalPosition.right);
        mReleaseAnims = new AnimatorSet();
        mReleaseAnims.setInterpolator(new LinearInterpolator());
        mReleaseAnims.setDuration(ANIMATION_DURATION);
        mReleaseAnims.playTogether(leftAnim, rightAnim);
        mReleaseAnims.start();
//
//        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
//        if(mTouchStateImpl.leftDrag()) mOriginalMargin = layoutParams.leftMargin - mOriginalLayoutParams.left;
//        else if(mTouchStateImpl.rightDrag()) mOriginalMargin = layoutParams.rightMargin - mOriginalLayoutParams.right;
//        Animation animation = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                super.applyTransformation(interpolatedTime, t);
//                MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
//                if(mTouchStateImpl.leftDrag()) params.leftMargin = (int) (mOriginalMargin * (1f - interpolatedTime) + mOriginalLayoutParams.left);
//                else if(mTouchStateImpl.rightDrag()) params.rightMargin = (int) (mOriginalMargin * (1f - interpolatedTime) + mOriginalLayoutParams.right);
//                Log4Android.v(this, "----------" + params.leftMargin + "\t" + params.rightMargin);
//                setLayoutParams(params);
//            }
//        };
//        animation.setInterpolator(new AccelerateDecelerateInterpolator());
//        animation.setDuration(ANIMATION_DURATION);
//        startAnimation(animation);
    }
    private void startVerticalAnim(){
        ObjectAnimator topAnim = ObjectAnimator.ofInt(_vg_, "top", _vg_.getTop(), mRectOriginalPosition.top);
        ObjectAnimator bottomAnim = ObjectAnimator.ofInt(_vg_, "bottom", _vg_.getBottom(), mRectOriginalPosition.bottom);
        mReleaseAnims = new AnimatorSet();
        mReleaseAnims.setInterpolator(new AccelerateDecelerateInterpolator());
        mReleaseAnims.setDuration(ANIMATION_DURATION);
        mReleaseAnims.playTogether(topAnim, bottomAnim);
        mReleaseAnims.start();
    }
}

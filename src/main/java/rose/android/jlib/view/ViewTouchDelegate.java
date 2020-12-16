package rose.android.jlib.view;



import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.core.view.ViewCompat;

/**
 * Created by RoseHongXin on 2017/6/23 0023.
 */

class ViewTouchDelegate extends SoftTouchDelegate{

    private static final int ANIMATION_DURATION = 1000;
    private static final int ANIMATION_DELAY = 50;

    private AnimatorSet mReleaseAnims;
    private boolean mAnimRunning = false;
    private Animator.AnimatorListener mAnimListener = new Animator.AnimatorListener() {
        @Override public void onAnimationStart(Animator animation) {
            mAnimRunning = true;
        }
        @Override public void onAnimationEnd(Animator animation) {
            mAnimRunning = false;
            mTouchStateImpl.idle();
        }
        @Override public void onAnimationCancel(Animator animation) {
            mAnimRunning = false;
            mTouchStateImpl.idle();

        }
        @Override public void onAnimationRepeat(Animator animation) {}
    };
    private TouchListener mTouchListener = new TouchListener() {
        @Override
        public void beforeTouchMove() {
            if(mAnimRunning) {
                mReleaseAnims.cancel();
            }
        }
        @Override
        public void horizontalTouch(float from, float to, int delta) {
            _vg_.post(() -> ViewCompat.offsetLeftAndRight(_vg_, delta));
        }
        @Override
        public void verticalTouch(float from, float to, int delta) {
            _vg_.post(() -> ViewCompat.offsetTopAndBottom(_vg_, delta));
        }
        @Override
        public void horizontalRelease(int direction) {
            startHorizontalAnim();
        }
        @Override
        public void verticalRelease(int direction) {
            startVerticalAnim();
        }
    };

    ViewTouchDelegate(ViewGroup _vg_) {
        super(_vg_);
        listener(mTouchListener);
    }

    private void startHorizontalAnim(){
        if(!mTouchStateImpl.isHorizontalDrag()){ return; }
        ObjectAnimator leftAnim = ObjectAnimator.ofInt(_vg_, "left", _vg_.getLeft(), mRectOriginal.left);
        ObjectAnimator rightAnim = ObjectAnimator.ofInt(_vg_, "right", _vg_.getRight(), mRectOriginal.right);
        mReleaseAnims = new AnimatorSet();
        mReleaseAnims.playTogether(leftAnim, rightAnim);
        mReleaseAnims.setInterpolator(new BounceInterpolator());
        start();
    }
    private void startVerticalAnim(){
        if(!mTouchStateImpl.isVerticalDrag()){ return; }
        ObjectAnimator topAnim = ObjectAnimator.ofInt(_vg_, "top", _vg_.getTop(), mRectOriginal.top);
        ObjectAnimator bottomAnim = ObjectAnimator.ofInt(_vg_, "bottom", _vg_.getBottom(), mRectOriginal.bottom);
        mReleaseAnims = new AnimatorSet();
        mReleaseAnims.playTogether(topAnim, bottomAnim);
        mReleaseAnims.setInterpolator(new BounceInterpolator());
        start();
    }

    private void start(){
        mReleaseAnims.setInterpolator(new DecelerateInterpolator());
        mReleaseAnims.setDuration(ANIMATION_DURATION);
//        mReleaseAnims.setStartDelay(ANIMATION_DELAY);
        mReleaseAnims.addListener(mAnimListener);
        mReleaseAnims.start();
    }
}

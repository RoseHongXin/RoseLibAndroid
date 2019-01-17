package hx.view.softtouch;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by RoseHongXin on 2017/6/23 0023.
 */

public class SoftScrollView extends NestedScrollView {

    private ViewTouchDelegate mSoftTouchDelegate;

    public SoftScrollView(Context context) {
        this(context, null);
    }

    public SoftScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSoftTouchDelegate = new ViewTouchDelegate(this);
        mSoftTouchDelegate.direction(ViewTouchDelegate.VERTICAL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
         mSoftTouchDelegate.onInterceptTouchEvent(event);
         return super.onInterceptTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSoftTouchDelegate.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

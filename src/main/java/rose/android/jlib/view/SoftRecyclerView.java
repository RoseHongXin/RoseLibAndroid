package rose.android.jlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by RoseHongXin on 2017/6/23 0023.
 */

public class SoftRecyclerView extends RecyclerView {

    private ViewTouchDelegate mSoftTouchDelegate;

    public SoftRecyclerView(Context context) {
        this(context, null);
    }

    public SoftRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSoftTouchDelegate = new ViewTouchDelegate(this);
        LayoutManager.Properties properties = RecyclerView.LayoutManager.getProperties(context, attrs, defStyle, 0);
        direction(properties.orientation == RecyclerView.HORIZONTAL ? ViewTouchDelegate.HORIZONTAL : ViewTouchDelegate.VERTICAL);
    }

    public void direction(int orientation){
        mSoftTouchDelegate.direction(orientation);
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

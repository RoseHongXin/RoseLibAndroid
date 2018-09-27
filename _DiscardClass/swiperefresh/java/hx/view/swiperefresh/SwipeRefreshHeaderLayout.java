package hx.view.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author hujian
 * @version v1.0
 * @Title
 * @Description:
 * @Date 2017/1/16 0016 下午 2:10
 */
public class SwipeRefreshHeaderLayout extends RelativeLayout implements SwipeRefreshTrigger, SwipeTrigger {

    public SwipeRefreshHeaderLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {
    }
}

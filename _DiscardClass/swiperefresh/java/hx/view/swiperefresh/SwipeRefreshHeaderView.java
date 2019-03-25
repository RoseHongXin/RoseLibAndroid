package hx.view.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import hx.lib.R;


/**
 * Created by Aspsine on 2015/9/9.
 */
public class SwipeRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private ImageView _sr_iv_arrow;

    private ImageView _sr_iv_success;

    private TextView _sr_tv_refresh;

    private ProgressBar _sr_pb;

    private int mHeaderHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public SwipeRefreshHeaderView(Context context) {
        this(context, null);
    }

    public SwipeRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.swipe_refresh_header_height);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.am_rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.am_rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        _sr_tv_refresh = (TextView) findViewById(R.id._sr_tv_refresh);
        _sr_iv_arrow = (ImageView) findViewById(R.id._sr_iv_arrow);
        _sr_iv_success = (ImageView) findViewById(R.id._sr_iv_success);
        _sr_pb = (ProgressBar) findViewById(R.id._sr_pb);
    }

    @Override
    public void onRefresh() {
        _sr_iv_success.setVisibility(GONE);
        _sr_iv_arrow.clearAnimation();
        _sr_iv_arrow.setVisibility(GONE);
        _sr_pb.setVisibility(VISIBLE);
        _sr_tv_refresh.setText(getContext().getString(R.string.swipe_refresh_refreshing));
    }

    @Override
    public void onPrepare() {
        Log.d("RefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            _sr_iv_arrow.setVisibility(VISIBLE);
            _sr_pb.setVisibility(GONE);
            _sr_iv_success.setVisibility(GONE);
            if (y > mHeaderHeight) {
                _sr_tv_refresh.setText(getContext().getString(R.string.swipe_refresh_release_to_refresh));
                if (!rotated) {
                    _sr_iv_arrow.clearAnimation();
                    _sr_iv_arrow.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    _sr_iv_arrow.clearAnimation();
                    _sr_iv_arrow.startAnimation(rotateDown);
                    rotated = false;
                }

                _sr_tv_refresh.setText(getContext().getString(R.string.swipe_refresh_pull_down_to_refresh));
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        _sr_iv_success.setVisibility(VISIBLE);
        _sr_iv_arrow.clearAnimation();
        _sr_iv_arrow.setVisibility(GONE);
        _sr_pb.setVisibility(GONE);
        _sr_tv_refresh.setText(getContext().getString(R.string.swipe_refresh_refresh_complete));
    }

    @Override
    public void onReset() {
        rotated = false;
        _sr_iv_success.setVisibility(GONE);
        _sr_iv_arrow.clearAnimation();
        _sr_iv_arrow.setVisibility(GONE);
        _sr_pb.setVisibility(GONE);
    }

}

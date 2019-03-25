package hx.view.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import hx.lib.R;


/**
 * Created by Aspsine on 2015/9/2.
 */
public class SwipeLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView _sr_tv_loadmore;
    private ImageView _sr_iv_success;
    private ProgressBar _sr_pb;

    private int mFooterHeight;

    public SwipeLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public SwipeLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.swipe_refresh_footer_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        _sr_tv_loadmore = (TextView) findViewById(R.id._sr_tv_loadmore);
        _sr_iv_success = (ImageView) findViewById(R.id._sr_iv_success);
        _sr_pb = (ProgressBar) findViewById(R.id._sr_pb);
    }

    @Override
    public void onPrepare() {
        _sr_iv_success.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            _sr_iv_success.setVisibility(GONE);
            _sr_pb.setVisibility(GONE);
            if (-y >= mFooterHeight) {
                _sr_tv_loadmore.setText(getContext().getString(R.string.swipe_refresh_release_to_load_more));
            } else {
                _sr_tv_loadmore.setText(getContext().getString(R.string.swipe_refresh_pull_up_to_load_more));
            }
        }
    }

    @Override
    public void onLoadMore() {
        _sr_tv_loadmore.setText(getContext().getString(R.string.swipe_refresh_load_more_loading));
        _sr_pb.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        _sr_pb.setVisibility(GONE);
        _sr_iv_success.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        _sr_iv_success.setVisibility(GONE);
    }
}

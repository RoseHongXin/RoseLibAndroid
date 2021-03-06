package in.srain.cube.views.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;

import rose.android.jlib.R;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrCLog;

/**
 * This layout view for "Pull to Refresh(Ptr)" support all of the view, you can contain everything you want.
 * support: pull to refresh / release to refresh / auto refresh / keep header view while refreshing / hide header view while refreshing
 * It defines {@link in.srain.cube.views.ptr.PtrUIHandler}, which allows you customize the UI easily.
 */
public class PtrFrameLayout extends ViewGroup {

    public enum Mode {
        NONE, REFRESH, LOAD_MORE, BOTH
    }

    private byte mStatus = PTR_STATUS_INIT;
    // status enum
    public final static byte PTR_STATUS_INIT = 1;
    public final static byte PTR_STATUS_PREPARE = 2;
    public final static byte PTR_STATUS_LOADING = 3;
    public final static byte PTR_STATUS_COMPLETE = 4;
    private static final boolean DEBUG_LAYOUT = false;
    public static boolean DEBUG = false;
    private static int ID = 1;
    protected final String LOG_TAG = "ptr-frame-" + ++ID;
    // auto refresh status
    private static byte FLAG_AUTO_REFRESH_AT_ONCE = 0x01;
    private static byte FLAG_AUTO_REFRESH_BUT_LATER = 0x01 << 1;
    private static byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 0x01 << 2;
    private static byte FLAG_PIN_CONTENT = 0x01 << 3;
    private static byte MASK_AUTO_REFRESH = 0x03;
    protected View mContent;
    // optional config for define header and target in xml file
    private int mHeaderId = 0;
    private int mContainerId = 0;
    private int mFooterId = 0;
    // config
    private Mode mMode = Mode.BOTH;
    protected int mTxtColor;

    // the time to back to refreshing position when release
    private int mDurationToBackHeader = 200;
    private int mDurationToBackFooter = 200;
    // the time to close header/footer when refresh completed
    private int mDurationToCloseHeader = 1000;
    private int mDurationToCloseFooter = 1000;

    private boolean mKeepHeaderWhenRefresh = true;
    private boolean mPullToRefresh = false;
    private boolean mForceBackWhenComplete = false;
    private View _l_header;
    private View _l_footer;
    private PtrUIHandlerHolder mPtrUIHandlerHolder = PtrUIHandlerHolder.create();
    private PtrHandler mPtrHandler;

    // working parameters
    private ScrollChecker mScrollChecker;
    private int mPagingTouchSlop;
    private int mHeaderHeight;
    private int mFooterHeight;
    private VelocityTracker mVelocityTracker;
    private int mPagingTouchVelocity;

    private boolean mDisableWhenHorizontalMove = false;
    private int mFlag = 0x00;

    // disable when detect moving horizontally
    private boolean mPreventForHorizontal = false;

    private MotionEvent mLastMoveEvent;

    private PtrUIHandlerHook mRefreshCompleteHook;

    private int mLoadingMinTime = 500;
    private long mLoadingStartTime = 0;
    private PtrIndicator mPtrIndicator;
    private boolean mHasSendCancelEvent = false;
    private Runnable mPerformRefreshCompleteDelay = () -> performRefreshComplete();

    public PtrFrameLayout(Context context) {
        this(context, null);
    }

    public PtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPtrIndicator = new PtrIndicator();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PtrLayout, 0, 0);
        if (arr != null) {
            mHeaderId = arr.getResourceId(R.styleable.PtrLayout_ptr_header, mHeaderId);
            mContainerId = arr.getResourceId(R.styleable.PtrLayout_ptr_content, mContainerId);
            mFooterId = arr.getResourceId(R.styleable.PtrLayout_ptr_footer, mFooterId);

            mPtrIndicator.setResistanceHeader(arr.getFloat(R.styleable.PtrLayout_ptr_resistance, mPtrIndicator.getResistanceHeader()));
            mPtrIndicator.setResistanceFooter(arr.getFloat(R.styleable.PtrLayout_ptr_resistance, mPtrIndicator.getResistanceFooter()));
            mPtrIndicator.setResistanceHeader(arr.getFloat(R.styleable.PtrLayout_ptr_resistance_header, mPtrIndicator.getResistanceHeader()));
            mPtrIndicator.setResistanceFooter(arr.getFloat(R.styleable.PtrLayout_ptr_resistance_footer, mPtrIndicator.getResistanceFooter()));

            mDurationToBackHeader = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_back_refresh, mDurationToCloseHeader);
            mDurationToBackFooter = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_back_refresh, mDurationToCloseHeader);
            mDurationToBackHeader = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_back_header, mDurationToCloseHeader);
            mDurationToBackFooter = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_back_footer, mDurationToCloseHeader);

            mDurationToCloseHeader = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_close_either, mDurationToCloseHeader);
            mDurationToCloseFooter = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_close_either, mDurationToCloseFooter);
            mDurationToCloseHeader = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_close_header, mDurationToCloseHeader);
            mDurationToCloseFooter = arr.getInt(R.styleable.PtrLayout_ptr_duration_to_close_footer, mDurationToCloseFooter);

            float ratio = mPtrIndicator.getRatioOfHeaderToHeightRefresh();
            ratio = arr.getFloat(R.styleable.PtrLayout_ptr_ratio_of_header_height_to_refresh, ratio);
            mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);

            mKeepHeaderWhenRefresh = arr.getBoolean(R.styleable.PtrLayout_ptr_keep_header_when_refresh, mKeepHeaderWhenRefresh);
            mPullToRefresh = arr.getBoolean(R.styleable.PtrLayout_ptr_pull_to_fresh, mPullToRefresh);
            mMode = getModeFromIndex(arr.getInt(R.styleable.PtrLayout_ptr_mode, 4));

            mTxtColor = arr.getColor(R.styleable.PtrLayout_ptr_txt_color, 0);

            arr.recycle();
        }
        mScrollChecker = new ScrollChecker();
        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchVelocity = conf.getScaledMinimumFlingVelocity();
//        mPagingTouchSlop = conf.getScaledTouchSlop();
        mPagingTouchSlop = conf.getScaledTouchSlop() / 2;
        mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.computeCurrentVelocity(1000);
    }

    private Mode getModeFromIndex(int index) {
        switch (index) {
            case 0:
                return Mode.NONE;
            case 1:
                return Mode.REFRESH;
            case 2:
                return Mode.LOAD_MORE;
            case 3:
                return Mode.BOTH;
            default:
                return Mode.BOTH;
        }
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount > 3) {
            throw new IllegalStateException("PtrFrameLayout only can host 3 elements");
        } else if (childCount == 3) {
            if (mHeaderId != 0 && _l_header == null) {
                _l_header = findViewById(mHeaderId);
            }
            if (mContainerId != 0 && mContent == null) {
                mContent = findViewById(mContainerId);
            }
            if (mFooterId != 0 && _l_footer == null) {
                _l_footer = findViewById(mFooterId);
            }
            // not specify header or target or footer
            if (mContent == null || _l_header == null || _l_footer == null) {
                final View child1 = getChildAt(0);
                final View child2 = getChildAt(1);
                final View child3 = getChildAt(2);
                // all are not specified
                if (mContent == null && _l_header == null && _l_footer == null) {
                    _l_header = child1;
                    mContent = child2;
                    _l_footer = child3;
                }
                // only some are specified
                else {
                    ArrayList<View> view = new ArrayList<View>(3) {{
                        add(child1);
                        add(child2);
                        add(child3);
                    }};
                    if (_l_header != null) {
                        view.remove(_l_header);
                    }
                    if (mContent != null) {
                        view.remove(mContent);
                    }
                    if (_l_footer != null) {
                        view.remove(_l_footer);
                    }
                    if (_l_header == null && view.size() > 0) {
                        _l_header = view.get(0);
                        view.remove(0);
                    }
                    if (mContent == null && view.size() > 0) {
                        mContent = view.get(0);
                        view.remove(0);
                    }
                    if (_l_footer == null && view.size() > 0) {
                        _l_footer = view.get(0);
                        view.remove(0);
                    }
                }
            }
        } else if (childCount == 2) { // ignore the footer by default
            if (mHeaderId != 0 && _l_header == null) {
                _l_header = findViewById(mHeaderId);
            }
            if (mContainerId != 0 && mContent == null) {
                mContent = findViewById(mContainerId);
            }

            // not specify header or target
            if (mContent == null || _l_header == null) {

                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                if (child1 instanceof PtrUIHandler) {
                    _l_header = child1;
                    mContent = child2;
                } else if (child2 instanceof PtrUIHandler) {
                    _l_header = child2;
                    mContent = child1;
                } else {
                    // both are not specified
                    if (mContent == null && _l_header == null) {
                        _l_header = child1;
                        mContent = child2;
                    }
                    // only one is specified
                    else {
                        if (_l_header == null) {
                            _l_header = mContent == child1 ? child2 : child1;
                        } else {
                            mContent = _l_header == child1 ? child2 : child1;
                        }
                    }
                }
            }
        } else if (childCount == 1) {
            mContent = getChildAt(0);
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The target view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
            mContent = errorView;
            addView(mContent);
        }
        if (_l_header != null) {
            _l_header.bringToFront();
        }
        if (_l_footer != null) {
            _l_footer.bringToFront();
        }
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mScrollChecker != null) {
            mScrollChecker.destroy();
        }

        if (mPerformRefreshCompleteDelay != null) {
            removeCallbacks(mPerformRefreshCompleteDelay);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (DEBUG && DEBUG_LAYOUT) {
            PtrCLog.d(LOG_TAG, "onMeasure frame: width: %s, height: %s, padding: %s %s %s %s", getMeasuredWidth(), getMeasuredHeight(), getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());
        }

        if (_l_header != null) {
            measureChildWithMargins(_l_header, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) _l_header.getLayoutParams();
            mHeaderHeight = _l_header.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            mPtrIndicator.setHeaderHeight(mHeaderHeight);
        }

        if (_l_footer != null) {
            measureChildWithMargins(_l_footer, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) _l_footer.getLayoutParams();
            mFooterHeight = _l_footer.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            mPtrIndicator.setFooterHeight(mFooterHeight);
        }

        if (mContent != null) {
            measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
            ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
            if (DEBUG && DEBUG_LAYOUT) {
                PtrCLog.d(LOG_TAG, "onMeasure target, width: %s, height: %s, margin: %s %s %s %s", getMeasuredWidth(), getMeasuredHeight(), lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
                PtrCLog.d(LOG_TAG, "onMeasure, currentPos: %s, lastPos: %s, top: %s", mPtrIndicator.getCurrentPosY(), mPtrIndicator.getLastPosY(), mContent.getTop());
            }
            // if the layout's height is wrap_content, set the layout' height to be the same with the target's height
            if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                super.setMeasuredDimension(getMeasuredWidth(), mContent.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }
    }

    private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        layoutChildren();
    }

    private void layoutChildren() {
        // because the header and footer can not show at the same time, so when header has a offset, the footer's offset should be 0, vice versa..
        int offsetHeaderY;
        int offsetFooterY;
        if (mPtrIndicator.isHeader()) {
            offsetHeaderY = mPtrIndicator.getCurrentPosY();
            offsetFooterY = 0;
        } else {
            offsetHeaderY = 0;
            offsetFooterY = mPtrIndicator.getCurrentPosY();
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int contentBottom = 0;

        if (DEBUG && DEBUG_LAYOUT) {
            PtrCLog.d(LOG_TAG, "onLayout offset: %s %s %s %s", offsetHeaderY, offsetFooterY, isPinContent(), mPtrIndicator.isHeader());
        }

        if (_l_header != null) {
            MarginLayoutParams lp = (MarginLayoutParams) _l_header.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + offsetHeaderY - mHeaderHeight;
            final int right = left + _l_header.getMeasuredWidth();
            final int bottom = top + _l_header.getMeasuredHeight();
            _l_header.layout(left, top, right, bottom);
            if (DEBUG && DEBUG_LAYOUT) {
                PtrCLog.d(LOG_TAG, "onLayout header: %s %s %s %s %s", left, top, right, bottom, _l_header.getMeasuredHeight());
            }
        }
        if (mContent != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
            int left;
            int top;
            int right;
            int bottom;
            if (mPtrIndicator.isHeader()) {
                left = paddingLeft + lp.leftMargin;
                top = paddingTop + lp.topMargin + (isPinContent() ? 0 : offsetHeaderY);
                right = left + mContent.getMeasuredWidth();
                bottom = top + mContent.getMeasuredHeight();
            } else {
                left = paddingLeft + lp.leftMargin;
                top = paddingTop + lp.topMargin - (isPinContent() ? 0 : offsetFooterY);
                right = left + mContent.getMeasuredWidth();
                bottom = top + mContent.getMeasuredHeight();
            }
            contentBottom = bottom;
            if (DEBUG && DEBUG_LAYOUT) {
                PtrCLog.d(LOG_TAG, "onLayout target: %s %s %s %s %s", left, top, right, bottom, mContent.getMeasuredHeight());
            }
            mContent.layout(left, top, right, bottom);
        }
        if (_l_footer != null) {
            MarginLayoutParams lp = (MarginLayoutParams) _l_footer.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + contentBottom - (isPinContent() ? offsetFooterY : 0);
            final int right = left + _l_footer.getMeasuredWidth();
            final int bottom = top + _l_footer.getMeasuredHeight();
            _l_footer.layout(left, top, right, bottom);
            if (DEBUG && DEBUG_LAYOUT) {
                PtrCLog.d(LOG_TAG, "onLayout footer: %s %s %s %s %s", left, top, right, bottom, _l_footer.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!isEnabled() || mContent == null || _l_header == null || mPtrHandler == null) {return super.onInterceptTouchEvent(e);}
        int action = e.getAction();
        mVelocityTracker.addMovement(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(isRefreshing()){
                    if (DEBUG) { PtrCLog.v(LOG_TAG, "onInterceptTouchEvent: block this ACTION_DOWN"); }
                    return false;
                }
                mHasSendCancelEvent = false;
                mPtrIndicator.onPressDown(e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = e;
                mPtrIndicator.onMove(e.getX(), e.getY());
                float offsetX = mPtrIndicator.getOffsetX();
                float offsetY = -mPtrIndicator.getOffsetY();        //mPtrIndicator 计算的是结束坐标减去开始坐标

                boolean headerShowing = mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition();
                boolean footerShowing = _l_footer != null && !mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition();
                boolean canHeaderMoveDown = mPtrHandler.checkCanDoRefresh(this, mContent, _l_header) && (mMode.ordinal() & 1) > 0;
                boolean canFooterMoveUp = mPtrHandler instanceof PtrHandler2 && ((PtrHandler2) mPtrHandler).checkCanDoLoadMore(this, mContent, _l_footer) && (mMode.ordinal() & 2) > 0;

                if(((headerShowing || footerShowing) || (offsetY > 0 && canFooterMoveUp) || (offsetY < 0 && canHeaderMoveDown))) {
                    if(Math.abs(offsetY) > mPagingTouchSlop) {
                        if (DEBUG) { PtrCLog.v(LOG_TAG, "onInterceptTouchEvent: ACTION_MOVE dispatch onTouchEvent."); }
                        return true;
                    }else{
                        if (DEBUG) { PtrCLog.v(LOG_TAG, "onInterceptTouchEvent: ACTION_MOVE touch fling."); }
                    }
                }
                if (DEBUG) { PtrCLog.v(LOG_TAG, "onInterceptTouchEvent: block this ACTION_MOVE"); }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPtrIndicator.onRelease();
                if (mPtrIndicator.hasLeftStartPosition()) {
                    if (DEBUG) {PtrCLog.d(LOG_TAG, "call onRelease when user release");}
                    onRelease(false);
                    if (mPtrIndicator.hasMovedAfterPressedDown()) {
                        sendCancelEvent();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mHasSendCancelEvent = false;
                mPtrIndicator.onPressDown(e.getX(), e.getY());
                if (!mForceBackWhenComplete) {
                    mScrollChecker.abortIfWorking();
                } else {
                    // when footer is showing and status is completed, do not abort scroller.
                    boolean isFooter = !mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition(); // if the footer is showing
                    if (!isFooter || mStatus != PTR_STATUS_COMPLETE) {
                        mScrollChecker.abortIfWorking();
                    }
                }
                mPreventForHorizontal = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = e;
                mPtrIndicator.onMove(e.getX(), e.getY());
                float offsetX = mPtrIndicator.getOffsetX();
                float offsetY = mPtrIndicator.getOffsetY();

                boolean headerShowing = mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition();
                boolean footerShowing = _l_footer != null && !mPtrIndicator.isHeader() && mPtrIndicator.hasLeftStartPosition();
                boolean canHeaderMoveDown = mPtrHandler.checkCanDoRefresh(this, mContent, _l_header) && (mMode.ordinal() & 1) > 0;
                boolean canFooterMoveUp = mPtrHandler instanceof PtrHandler2 && ((PtrHandler2) mPtrHandler).checkCanDoLoadMore(this, mContent, _l_footer) && (mMode.ordinal() & 2) > 0;
                boolean moveDown = offsetY > 0;
                boolean moveUp = !moveDown;

                if (DEBUG) {
                    PtrCLog.v(LOG_TAG, "onTouchEvent ACTION_MOVE: offsetY:%s, currentPos: %s, moveUp: %s, canMoveUp: %s, moveDown: %s: canMoveDown: %s canHeaderMoveDown: %s canFooterMoveUp: %s",
                            offsetY, mPtrIndicator.getCurrentPosY(), moveUp, headerShowing, moveDown, footerShowing, canHeaderMoveDown, canFooterMoveUp);
                }
                if (!headerShowing && !footerShowing) {
                    if ((moveDown && !canHeaderMoveDown) || (moveUp && !canFooterMoveUp)) {
                        if(DEBUG){ PtrCLog.i(LOG_TAG, "onTouchEvent: ignore this ACTION_MOVE."); }
                        super.onTouchEvent(e);
                        return false;
                    }
                    if (moveDown) {
                        moveHeaderPos(offsetY);
                    }else {
                        moveFooterPos(offsetY);
                    }
                    return true;
                }
                if (headerShowing) {
                    moveHeaderPos(offsetY);
                    return true;
                }else{
                    // When status is completed, disable pull up
                    if (mForceBackWhenComplete && mStatus == PTR_STATUS_COMPLETE) {
                        return super.onTouchEvent(e);
                    } else {
                        moveFooterPos(offsetY);
                        return true;
                    }
                }
        }
        return super.onTouchEvent(e);
    }

    private void moveFooterPos(float deltaY) {
        mPtrIndicator.setIsHeader(false);
        // to keep the consistence with refresh, need to converse the deltaY
        movePos(-deltaY);
    }

    private void moveHeaderPos(float deltaY) {
        mPtrIndicator.setIsHeader(true);
        movePos(deltaY);
    }

    /**
     * if deltaY > 0, move the target down
     *
     * @param deltaY
     */
    private void movePos(float deltaY) {
        // has reached the top
        if ((deltaY < 0 && mPtrIndicator.isInStartPosition())) {
            if (DEBUG) {
                PtrCLog.e(LOG_TAG, String.format("has reached the top"));
            }
            return;
        }

        int to = mPtrIndicator.getCurrentPosY() + (int) deltaY;

        // over top
        if (mPtrIndicator.willOverTop(to)) {
            if (DEBUG) {
                PtrCLog.e(LOG_TAG, String.format("over top"));
            }
            to = PtrIndicator.POS_START;
        }
        mPtrIndicator.setCurrentPos(to);
        int change = to - mPtrIndicator.getLastPosY();
        updatePos(mPtrIndicator.isHeader() ? change : -change);
    }

    private void updatePos(int change) {
        if (change == 0) {
            return;
        }

        boolean isUnderTouch = mPtrIndicator.isUnderTouch();

        // once moved, cancel event will be sent to child
        if (isUnderTouch && !mHasSendCancelEvent && mPtrIndicator.hasMovedAfterPressedDown()) {
            mHasSendCancelEvent = true;
            sendCancelEvent();
        }

        // leave initiated position or just refresh complete
        if ((mPtrIndicator.hasJustLeftStartPosition() && mStatus == PTR_STATUS_INIT) ||
                (mPtrIndicator.goDownCrossFinishPosition() && mStatus == PTR_STATUS_COMPLETE && isEnabledNextPtrAtOnce())) {

            mStatus = PTR_STATUS_PREPARE;
            mPtrUIHandlerHolder.onUIRefreshPrepare(this);
            if (DEBUG) {
                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
            }
        }

        // back to initiated position
        if (mPtrIndicator.hasJustBackToStartPosition()) {
            tryToNotifyReset();

            // recover event to children
            if (isUnderTouch) {
                sendDownEvent();
            }
        }

        // Pull to Refresh
        if (mStatus == PTR_STATUS_PREPARE) {
            // reach fresh height while moving from top to bottom
            if (isUnderTouch && !isAutoRefresh() && mPullToRefresh
                    && mPtrIndicator.crossRefreshLineFromTopToBottom()) {
                tryToPerformRefresh();
            }
            // reach header height while auto refresh
            if (performAutoRefreshButLater() && mPtrIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
                tryToPerformRefresh();
            }
        }

        if (DEBUG) {
            PtrCLog.v(LOG_TAG, "updatePos: change: %s, current: %s last: %s, top: %s, headerHeight: %s",
                    change, mPtrIndicator.getCurrentPosY(), mPtrIndicator.getLastPosY(), mContent.getTop(), mHeaderHeight);
        }

        if (mPtrIndicator.isHeader()) {
            _l_header.offsetTopAndBottom(change);
        } else {
            _l_footer.offsetTopAndBottom(change);
        }
        if (!isPinContent()) {
            mContent.offsetTopAndBottom(change);
        }
        invalidate();

        if (mPtrUIHandlerHolder.hasHandler()) {
            mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, mStatus, mPtrIndicator);
        }
        onPositionChange(isUnderTouch, mStatus, mPtrIndicator);
    }

    private void onPositionChange(boolean isUnderTouch, byte mStatus, PtrIndicator mPtrIndicator) {

    }

    @SuppressWarnings("unused")
    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public int getFooterHeight() {
        return mFooterHeight;
    }

    private void onRelease(boolean stayForLoading) {

        tryToPerformRefresh();

        if (mStatus == PTR_STATUS_LOADING) {
            // keep header for fresh
            if (mKeepHeaderWhenRefresh) {
                // scroll header back
                if (mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && !stayForLoading) {
                    if (mPtrIndicator.isHeader()) {
                        mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToKeepHeaderWhileLoading(), mDurationToBackHeader);
                    } else {
                        mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToKeepHeaderWhileLoading(), mDurationToBackFooter);
                    }
                } else {
                    // do nothing
                }
            } else {
                tryScrollBackToTopWhileLoading();
            }
        } else {
            mPtrIndicator.onRelease();
            if (mStatus == PTR_STATUS_COMPLETE) {
                notifyUIRefreshComplete(false);
            } else {
                tryScrollBackToTopAbortRefresh();
            }
        }
    }

    /**
     * please DO REMEMBER resume the hook
     *
     * @param hook
     */

    public void setRefreshCompleteHook(PtrUIHandlerHook hook) {
        mRefreshCompleteHook = hook;
        hook.setResumeAction(new Runnable() {
            @Override
            public void run() {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "mRefreshCompleteHook resume.");
                }
                notifyUIRefreshComplete(true);
            }
        });
    }

    /**
     * Scroll back to to if is not under touch
     * When forceback is true and footer is completed, scroll back either
     */
    private void tryScrollBackToTop() {
        if (!mPtrIndicator.isUnderTouch() && mPtrIndicator.hasLeftStartPosition()) {
            mScrollChecker.tryToScrollTo(PtrIndicator.POS_START, mPtrIndicator.isHeader() ? mDurationToCloseHeader : mDurationToCloseFooter);
            return;
        }

        if (mForceBackWhenComplete && !mPtrIndicator.isHeader() && mStatus == PTR_STATUS_COMPLETE) {
            mScrollChecker.tryToScrollTo(PtrIndicator.POS_START, mDurationToCloseFooter);
        }
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopWhileLoading() {
        tryScrollBackToTop();
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopAfterComplete() {
        tryScrollBackToTop();
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopAbortRefresh() {
        tryScrollBackToTop();
    }

    private boolean tryToPerformRefresh() {
        if (mStatus != PTR_STATUS_PREPARE) {
            return false;
        }

        //
        if ((mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoRefresh()) || mPtrIndicator.isOverOffsetToRefresh()) {
            mStatus = PTR_STATUS_LOADING;
            performRefresh();
        }
        return false;
    }

    private void performRefresh() {
        mLoadingStartTime = System.currentTimeMillis();
        if (mPtrUIHandlerHolder.hasHandler()) {
            mPtrUIHandlerHolder.onUIRefreshBegin(this);
            if (DEBUG) {
                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshBegin");
            }
        }
        if (mPtrHandler != null) {
            if (mPtrIndicator.isHeader()) {
                mPtrHandler.onRefreshBegin(this);
            } else {
                if (mPtrHandler instanceof PtrHandler2) {
                    ((PtrHandler2) mPtrHandler).onLoadMoreBegin(this);
                }
            }
        }
    }

    /**
     * If at the top and not in loading, reset
     */
    private boolean tryToNotifyReset() {
        if ((mStatus == PTR_STATUS_COMPLETE || mStatus == PTR_STATUS_PREPARE) && mPtrIndicator.isInStartPosition()) {
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIReset(this);
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIReset");
                }
            }
            mStatus = PTR_STATUS_INIT;
            clearFlag();
            return true;
        }
        return false;
    }

    protected void onPtrScrollAbort() {
        if (mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "call onRelease after scroll abort");
            }
            onRelease(true);
        }
    }

    protected void onPtrScrollFinish() {
        if (mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "call onRelease after scroll finish");
            }
            onRelease(true);
        }
    }

    /**
     * Detect whether is refreshing.
     *
     * @return
     */
    public boolean isRefreshing() {
        return mStatus == PTR_STATUS_LOADING;
    }

    /**
     * Call this when data is loaded.
     * The UI will perform complete at once or after a delay, depends on the time elapsed is greater then {@link #mLoadingMinTime} or not.
     */
    final public void refreshComplete() {
        if (DEBUG) {
            PtrCLog.i(LOG_TAG, "refreshComplete");
        }

        if (mRefreshCompleteHook != null) {
            mRefreshCompleteHook.reset();
        }

        int delay = (int) (mLoadingMinTime - (System.currentTimeMillis() - mLoadingStartTime));
        if (delay <= 0) {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "performRefreshComplete at once");
            }

            //////////////////////////////////////////////////
            //自己加的代码, 解决当当前页面被其他页面覆盖时, autoRefresh后, header无法回弹的问题
            mPtrIndicator.onRelease();
//            if(DEBUG){ PtrCLog.d(LOG_TAG, "--------------------refresh complete onRelease()");}
            //////////////////////////////////////////////////

            performRefreshComplete();
        } else {
            postDelayed(mPerformRefreshCompleteDelay, delay);
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "performRefreshComplete after delay: %s", delay);
            }
        }
    }

    /**
     * Do refresh complete work when time elapsed is greater than {@link #mLoadingMinTime}
     */
    private void performRefreshComplete() {
        mStatus = PTR_STATUS_COMPLETE;

        // if is auto refresh do nothing, wait scroller stop
        if (mScrollChecker.mIsRunning && isAutoRefresh()) {
            // do nothing
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "performRefreshComplete do nothing, scrolling: %s, auto refresh: %s", mScrollChecker.mIsRunning, mFlag);
            }
            return;
        }

        notifyUIRefreshComplete(false);
    }

    /**
     * Do real refresh work. If there is a hook, execute the hook first.
     *
     * @param ignoreHook
     */
    private void notifyUIRefreshComplete(boolean ignoreHook) {
        /**
         * After hook operation is done, {@link #notifyUIRefreshComplete} will be call in resume action to ignore hook.
         */
        if (mPtrIndicator.hasLeftStartPosition() && !ignoreHook && mRefreshCompleteHook != null) {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "notifyUIRefreshComplete mRefreshCompleteHook run.");
            }

            mRefreshCompleteHook.takeOver();
            return;
        }
        if (mPtrUIHandlerHolder.hasHandler()) {
            if (DEBUG) {
                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshComplete");
            }
            mPtrUIHandlerHolder.onUIRefreshComplete(this, mPtrIndicator.isHeader());
        }
        mPtrIndicator.onUIRefreshComplete();
//        tryScrollBackToTopAfterComplete();
        tryScrollBackToTop();
        tryToNotifyReset();
    }

    public void justShowRefreshHint(){
        mLoadingStartTime = System.currentTimeMillis();
        mStatus = PTR_STATUS_LOADING;
        mFlag |= FLAG_AUTO_REFRESH_AT_ONCE;
        if (mPtrUIHandlerHolder.hasHandler()) {
            mPtrUIHandlerHolder.onUIRefreshBegin(this);
            if (DEBUG) { PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshBegin, mFlag %s (justShowRefreshHint)", mFlag); }
        }
        mPtrIndicator.setIsHeader(true);
        mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToRefresh(), mDurationToCloseHeader);
        mStatus = PTR_STATUS_LOADING;
    }

    public void autoRefresh() {
        autoRefresh(true, true);
    }

    public void autoRefresh(boolean atOnce) {
        autoRefresh(atOnce, true);
    }

    private void clearFlag() {
        // remove auto fresh flag
        mFlag = mFlag & ~MASK_AUTO_REFRESH;
    }

    public void autoLoadMore() {
        autoRefresh(true, false);
    }

    public void autoLoadMore(boolean atOnce) {
        autoRefresh(atOnce, false);
    }

    public void autoRefresh(boolean atOnce, boolean isHeader) {

        if (mStatus != PTR_STATUS_INIT) {
            return;
        }

        mFlag |= atOnce ? FLAG_AUTO_REFRESH_AT_ONCE : FLAG_AUTO_REFRESH_BUT_LATER;

        mStatus = PTR_STATUS_PREPARE;
        if (mPtrUIHandlerHolder.hasHandler()) {
            mPtrUIHandlerHolder.onUIRefreshPrepare(this);
            if (DEBUG) {
                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
            }
        }
        mPtrIndicator.setIsHeader(isHeader);

        mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToRefresh(), isHeader ? mDurationToCloseHeader : mDurationToCloseFooter);
        if (atOnce) {
            mStatus = PTR_STATUS_LOADING;
            performRefresh();
        }
    }

    public boolean isAutoRefresh() {
        return (mFlag & MASK_AUTO_REFRESH) > 0;
    }

    private boolean performAutoRefreshButLater() {
        return (mFlag & MASK_AUTO_REFRESH) == FLAG_AUTO_REFRESH_BUT_LATER;
    }

    public boolean isEnabledNextPtrAtOnce() {
        return (mFlag & FLAG_ENABLE_NEXT_PTR_AT_ONCE) > 0;
    }

    /**
     * If @param enable has been set to true. The user can perform next PTR at once.
     *
     * @param enable
     */
    public void setEnabledNextPtrAtOnce(boolean enable) {
        if (enable) {
            mFlag = mFlag | FLAG_ENABLE_NEXT_PTR_AT_ONCE;
        } else {
            mFlag = mFlag & ~FLAG_ENABLE_NEXT_PTR_AT_ONCE;
        }
    }

    public boolean isPinContent() {
        return (mFlag & FLAG_PIN_CONTENT) > 0;
    }

    /**
     * The target view will now move when pinContent set to true.
     *
     * @param pinContent
     */
    public void setPinContent(boolean pinContent) {
        if (pinContent) {
            mFlag = mFlag | FLAG_PIN_CONTENT;
        } else {
            mFlag = mFlag & ~FLAG_PIN_CONTENT;
        }
    }

    /**
     * It's useful when working with viewpager.
     *
     * @param disable
     */
    public void disableWhenHorizontalMove(boolean disable) {
        mDisableWhenHorizontalMove = disable;
    }

    /**
     * loading will last at least for so long
     *
     * @param time
     */
    public void setLoadingMinTime(int time) {
        mLoadingMinTime = time;
    }

    /**
     * Not necessary any longer. Once moved, cancel event will be sent to child.
     *
     * @param yes
     */
    @Deprecated
    public void setInterceptEventWhileWorking(boolean yes) {
    }

    @SuppressWarnings({"unused"})
    public View getContentView() {
        return mContent;
    }


    public void setPtrHandler(PtrHandler ptrHandler) {
        mPtrHandler = ptrHandler;
    }

    public void addPtrUIHandler(PtrUIHandler ptrUIHandler) {
        PtrUIHandlerHolder.addHandler(mPtrUIHandlerHolder, ptrUIHandler);
    }

    @SuppressWarnings({"unused"})
    public void removePtrUIHandler(PtrUIHandler ptrUIHandler) {
        mPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(mPtrUIHandlerHolder, ptrUIHandler);
    }

    public void setPtrIndicator(PtrIndicator slider) {
        if (mPtrIndicator != null && mPtrIndicator != slider) {
            slider.convertFrom(mPtrIndicator);
        }
        mPtrIndicator = slider;
    }

    public void setMode(Mode mode) {
        mMode = mode;
    }

    public Mode getMode() {
        return mMode;
    }

    @SuppressWarnings({"unused"})
    public float getResistanceHeader() {
        return mPtrIndicator.getResistanceHeader();
    }

    @SuppressWarnings({"unused"})
    public float getResistanceFooter() {
        return mPtrIndicator.getResistanceFooter();
    }

    public void setResistance(float resistance) {
        setResistanceHeader(resistance);
        setResistanceFooter(resistance);
    }

    public void setResistanceHeader(float resistance) {
        mPtrIndicator.setResistanceHeader(resistance);
    }

    public void setResistanceFooter(float resistance) {
        mPtrIndicator.setResistanceFooter(resistance);
    }

    @SuppressWarnings({"unused"})
    public float getDurationToClose() {
        return mDurationToCloseHeader;
    }


    /**
     * The duration to return back to the refresh position
     *
     * @param duration
     */
    public void setDurationToBack(int duration) {
        setDurationToBackHeader(duration);
        setDurationToBackFooter(duration);
    }

    public int getDurationToBackHeader() {
        return mDurationToBackHeader;
    }

    public void setDurationToBackHeader(int mDurationToBackHeader) {
        this.mDurationToBackHeader = mDurationToBackHeader;
    }

    public int getDurationToBackFooter() {
        return mDurationToBackFooter;
    }

    public void setDurationToBackFooter(int mDurationToBackFooter) {
        this.mDurationToBackFooter = mDurationToBackFooter;
    }

    /**
     * The duration to return back to the start position
     *
     * @param duration
     */
    public void setDurationToClose(int duration) {
        setDurationToCloseHeader(duration);
        setDurationToCloseFooter(duration);
    }

    public void setDurationToCloseHeader(int duration) {
        mDurationToCloseHeader = duration;
    }

    public void setDurationToCloseFooter(int duration) {
        mDurationToCloseFooter = duration;
    }

    @SuppressWarnings({"unused"})
    public long getDurationToCloseHeader() {
        return mDurationToCloseHeader;
    }

    @SuppressWarnings({"unused"})
    public long getDurationToCloseFooter() {
        return mDurationToCloseFooter;
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
    }

    public int getOffsetToRefresh() {
        return mPtrIndicator.getOffsetToRefresh();
    }

    @SuppressWarnings({"unused"})
    public void setOffsetToRefresh(int offset) {
        mPtrIndicator.setOffsetToRefresh(offset);
    }

    @SuppressWarnings({"unused"})
    public float getRatioOfHeaderToHeightRefresh() {
        return mPtrIndicator.getRatioOfHeaderToHeightRefresh();
    }

    @SuppressWarnings({"unused"})
    public int getOffsetToKeepHeaderWhileLoading() {
        return mPtrIndicator.getOffsetToKeepHeaderWhileLoading();
    }

    @SuppressWarnings({"unused"})
    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mPtrIndicator.setOffsetToKeepHeaderWhileLoading(offset);
    }

    @SuppressWarnings({"unused"})
    public boolean isKeepHeaderWhenRefresh() {
        return mKeepHeaderWhenRefresh;
    }

    public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
        mKeepHeaderWhenRefresh = keepOrNot;
    }

    @SuppressWarnings({"unused"})
    public boolean isForceBackWhenComplete() {
        return mForceBackWhenComplete;
    }

    public void setForceBackWhenComplete(boolean mForceBackWhenComplete) {
        this.mForceBackWhenComplete = mForceBackWhenComplete;
    }

    public boolean isPullToRefresh() {
        return mPullToRefresh;
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        mPullToRefresh = pullToRefresh;
    }

    @SuppressWarnings({"unused"})
    public View getHeaderView() {
        return _l_header;
    }

    public void setHeaderView(View header) {
        if (_l_header != null && header != null && _l_header != header) {
            removeView(_l_header);
        }
        ViewGroup.LayoutParams lp = header.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(-1, -2);
            header.setLayoutParams(lp);
        }
        _l_header = header;
        addView(header);
    }

    public void setFooterView(View footer) {
        if (_l_footer != null && footer != null && _l_footer != footer) {
            removeView(_l_footer);
        }
        ViewGroup.LayoutParams lp = footer.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(-1, -2);
            footer.setLayoutParams(lp);
        }
        _l_footer = footer;
        addView(footer);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private void sendCancelEvent() {
        if (DEBUG) {
            PtrCLog.d(LOG_TAG, "send cancel event");
        }
        // The ScrollChecker will update position and lead to send cancel event when mLastMoveEvent is null.
        // fix #104, #80, #92
        if (mLastMoveEvent == null) {
            return;
        }
        MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        onInterceptTouchEvent(e);
    }

    private void sendDownEvent() {
        if (DEBUG) {
            PtrCLog.d(LOG_TAG, "send down event");
        }
        if (mLastMoveEvent == null) {
            return;
        }
        final MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
        onInterceptTouchEvent(e);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    class ScrollChecker implements Runnable {

        private int mLastFlingY;
        private Scroller mScroller;
        private boolean mIsRunning = false;
        private int mStart;
        private int mTo;

        public ScrollChecker() {
            mScroller = new Scroller(getContext());
        }

        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int curY = mScroller.getCurrY();
            int deltaY = curY - mLastFlingY;
            if (DEBUG) {
                if (deltaY != 0) {
                    PtrCLog.v(LOG_TAG,
                            "scroll: %s, start: %s, to: %s, currentPos: %s, current :%s, last: %s, delta: %s",
                            finish, mStart, mTo, mPtrIndicator.getCurrentPosY(), curY, mLastFlingY, deltaY);
                }
            }
            if (!finish) {
                mLastFlingY = curY;
                if (mPtrIndicator.isHeader()) {
                    moveHeaderPos(deltaY);
                } else {
                    moveFooterPos(-deltaY);
                }
                post(this);
            } else {
                finish();
            }
        }

        public boolean isRunning() {
            return mScroller.isFinished();
        }

        private void finish() {
            if (DEBUG) {
                PtrCLog.v(LOG_TAG, "finish, currentPos:%s", mPtrIndicator.getCurrentPosY());
            }
            reset();
            onPtrScrollFinish();
        }

        private void reset() {
            mIsRunning = false;
            mLastFlingY = 0;
            removeCallbacks(this);
        }

        private void destroy() {
            reset();
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
        }

        public void abortIfWorking() {
            if (mIsRunning) {
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                onPtrScrollAbort();
                reset();
            }
        }

        public void tryToScrollTo(int to, int duration) {
            if (mPtrIndicator.isAlreadyHere(to)) {
                return;
            }
            mStart = mPtrIndicator.getCurrentPosY();
            mTo = to;
            int distance = to - mStart;
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "tryToScrollTo: start: %s, distance:%s, to:%s", mStart, distance, to);
            }
            removeCallbacks(this);

            mLastFlingY = 0;

            // fix #47: Scroller should be reused, https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/47
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            if (duration > 0) {
                mScroller.startScroll(0, 0, 0, distance, duration);
                post(this);
                mIsRunning = true;
            } else {
                if (mPtrIndicator.isHeader()) {
                    moveHeaderPos(distance);
                } else {
                    moveFooterPos(-distance);
                }
                mIsRunning = false;
            }
        }
    }
}

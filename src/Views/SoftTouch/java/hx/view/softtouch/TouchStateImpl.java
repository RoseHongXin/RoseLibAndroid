package hx.view.softtouch;

/**
 * Created by RoseHongXin on 2017/6/27 0027.
 */

public class TouchStateImpl {

    private final int STATE_IDLE = 0x1;
    private final int STATE_TOP_DRAG = 0x2;
    private final int STATE_BOTTOM_DRAG = 0x3;
    private final int STATE_LEFT_DRAG = 0x4;
    private final int STATE_RIGHT_DRAG = 0x5;
    private final int STATE_RELEASE = 0x6;

    private int state = STATE_IDLE;

    public boolean isVerticalDrag(){
        return topDrag() || bottomDrag();
    }
    public boolean isHorizontalDrag(){
        return leftDrag() || rightDrag();
    }

    public boolean isIdle(){
        return state == STATE_IDLE;
    }
    public void idle(){
        state = STATE_IDLE;
    }

    public boolean topDrag(){
        return state == STATE_TOP_DRAG;
    }
    public boolean bottomDrag(){
        return state == STATE_BOTTOM_DRAG;
    }
    public boolean leftDrag(){
        return state == STATE_LEFT_DRAG;
    }
    public boolean rightDrag(){
        return state == STATE_RIGHT_DRAG;
    }

    public boolean isRelease(){
        return state == STATE_RELEASE;
    }
    public void release(){
        state = STATE_RELEASE;
    }

    public boolean verticalOffset(int yDelta){
        return topOffset(yDelta) || bottomOffset(yDelta);
    }
    public boolean horizontalOffset(int xDelta){
        return leftOffset(xDelta) || rightOffset(xDelta);
    }

    private boolean topOffset(int yDelta) {
        if (yDelta > 0) {
            state = STATE_TOP_DRAG;
            return true;
        }
        return false;
    }

    private boolean bottomOffset(int yDelta) {
        if (yDelta < 0) {
            state = STATE_BOTTOM_DRAG;
            return true;
        }
        return false;
    }

    private boolean leftOffset(int xDelta) {
        if (xDelta > 0) {
            state = STATE_LEFT_DRAG;
            return true;
        }
        return false;
    }

    private boolean rightOffset(int xDelta) {
        if (xDelta < 0) {
            state = STATE_RIGHT_DRAG;
            return true;
        }
        return false;
    }

}

//        View lastItemView = mLayoutManager.getChildAt(mLayoutManager.getChildCount() - 1);
//        if (lastItemView != null && mLayoutManager.getPosition(lastItemView) == mLayoutManager.getItemCount() - 1 && lastItemView.getBottom() == mRectOriginal.bottom)
//            return true;
//        else {
//            hx.view.smoothtouch(yDelta);
//            return false;
//        }


//        View firstItemView = mLayoutManager.getChildAt(0);
//        if (firstItemView != null && mLayoutManager.getPosition(firstItemView) == 0 && firstItemView.getTop() == mRectOriginal.top)
//            return true;
//        else {
//            hx.view.smoothtouch(yDelta);
//            return false;
//        }

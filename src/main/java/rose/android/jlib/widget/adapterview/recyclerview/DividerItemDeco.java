package rose.android.jlib.widget.adapterview.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by RoseHongXin on 2019/11/15 0015.
 */

public class DividerItemDeco extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider, android.R.attr.colorEdgeEffect};

    private RecyclerView.LayoutManager mLayoutMgr;

    private int mLineWidth = 1;
    private Rect mOutlineRect;
    private Path mOutlinePath;
    private Paint mPaint;

    private boolean mMenuMode = false;
    private int mOrientation;
    private Context mCtx;

    public DividerItemDeco(Context context, RecyclerView.LayoutManager layoutMgr) {
        this.mCtx = context;
        if(layoutMgr instanceof GridLayoutManager){
            mMenuMode = true;
        }else if(layoutMgr instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutMgr).getOrientation();
            orientation(orientation);
        }
        mLayoutMgr = layoutMgr;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        int color = context.getResources().getColor(android.R.color.darker_gray);
        setColor(color);
        a.recycle();

        mOutlineRect = new Rect();
        mOutlinePath = new Path();
    }

    public void orientation(int orientation) {
        mOrientation = orientation;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    public void line(int width) {
        if(mPaint == null) initPaint();
        mPaint.setStrokeWidth(width);
        mLineWidth = width;
    }
    public void setColor(@ColorInt int color) {
        if(mPaint == null) initPaint();
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(mLayoutMgr instanceof GridLayoutManager){
            if(mMenuMode) { drawMenuGrid(c, parent); }
            else {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    parent.getDecoratedBoundsWithMargins(view, mOutlineRect);
                    c.drawRect(mOutlineRect, mPaint);
                }
            }
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(view, mOutlineRect);
            if(mOrientation == HORIZONTAL){
                c.drawRect(mOutlineRect, mPaint);
            }else{
                c.drawRect(mOutlineRect, mPaint);
            }
        }
    }

    private void drawMenuGrid(@NonNull Canvas c, @NonNull RecyclerView parent){
        GridLayoutManager manager = (GridLayoutManager) mLayoutMgr;
        int span = manager.getSpanCount();
        int rows = parent.getChildCount() / span - 1;
        int columns = span - 1;
        int stroke = mLineWidth;
        //为了Item大小均匀，将设定分割线平均分给左右两边Item各一半
        int offset = stroke / 2;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(view, mOutlineRect);
            int position = parent.getChildAdapterPosition(view);
            int row = position / span;
            int column = position % span;
            mOutlinePath.reset();
            if(row == 0) {
                if(column == 0) {
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                }else if(column == columns){
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                }else{
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                }
            }else if(row == rows) {
                if(column == 0) {
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                }else if(column == columns){
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                }else{
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                }
            }else {
                if(column == 0) {
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.bottom);
                }else if(column == columns){
                    mOutlinePath.moveTo(mOutlineRect.right, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                }else{
                    mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                    mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                    mOutlinePath.lineTo(mOutlineRect.left, mOutlineRect.bottom);
                }
            }
            c.drawPath(mOutlinePath, mPaint);
        }
    }

//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        if(mMenuMode && mLayoutMgr instanceof GridLayoutManager){
//            GridLayoutManager manager = (GridLayoutManager) mLayoutMgr;
//            int childSize = parent.getChildCount();
//            int span = manager.getSpanCount();
//            int stroke = mLineWidth;
//            //为了Item大小均匀，将设定分割线平均分给左右两边Item各一半
//            int offset = stroke / 2;
//            int childPosition = parent.getChildAdapterPosition(view);
//            //第一排，顶部不画
//            if (childPosition  < span) {
//                //最左边的，左边不画
//                if (childPosition  % span == 0) {
//                    outRect.set(0, 0, offset, 0);
//                    //最右边，右边不画
//                } else if (childPosition  % span == span - 1) {
//                    outRect.set(offset, 0, 0, 0);
//                } else {
//                    outRect.set(offset, 0, offset, 0);
//                }
//            } else {
//                //上下的分割线，就从第二排开始，每个区域的顶部直接添加设定大小，不用再均分了
//                if (childPosition  % span == 0) {
//                    outRect.set(0, stroke, offset, 0);
//                } else if (childPosition  % span == span - 1) {
//                    outRect.set(offset, stroke, 0, 0);
//                } else {
//                    outRect.set(offset, stroke, offset, 0);
//                }
//            }
//        }else{
//            if(mOrientation == HORIZONTAL){
//
//            }else if(mOrientation == VERTICAL){
//
//            }
//        }
//
//    }
}

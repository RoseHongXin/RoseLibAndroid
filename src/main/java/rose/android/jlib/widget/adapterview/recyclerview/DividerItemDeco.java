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

    private final RecyclerView.LayoutManager mLayoutMgr;

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
            if(mMenuMode) {
                GridLayoutManager mgr = (GridLayoutManager) mLayoutMgr;
                int columns = mgr.getSpanCount();
                int rows = (int)Math.ceil(((float)parent.getChildCount() / (float) columns));
                int cnt = parent.getChildCount();
                for (int i = 0; i < cnt; i++) {
                    View view = parent.getChildAt(i);
                    parent.getDecoratedBoundsWithMargins(view, mOutlineRect);
                    int position = parent.getChildAdapterPosition(view) + 1;
                    mOutlinePath.reset();
                    int col = position % columns;
                    int row = (int)Math.ceil(position / (double)columns);
                    boolean drawRight = col % columns > 0;
                    boolean drawBottom = row < rows;
                    if(drawRight && drawBottom){
                        mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.bottom);
                        mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                        mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.top);
                    }else if(drawRight){
                        mOutlinePath.moveTo(mOutlineRect.right, mOutlineRect.top);
                        mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                    }else if(drawBottom){
                        mOutlinePath.moveTo(mOutlineRect.left, mOutlineRect.bottom);
                        mOutlinePath.lineTo(mOutlineRect.right, mOutlineRect.bottom);
                    }
                    c.drawPath(mOutlinePath, mPaint);
                }
            } else {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    parent.getDecoratedBoundsWithMargins(view, mOutlineRect);
                    c.drawRect(mOutlineRect, mPaint);
                }
            }
        }else if(mLayoutMgr instanceof LinearLayoutManager){
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(view, mOutlineRect);
                if(mOrientation == HORIZONTAL){
                    c.drawLine(mOutlineRect.right, mOutlineRect.top, mOutlineRect.right, mOutlineRect.bottom, mPaint);
                }else{
                    c.drawLine(mOutlineRect.left, mOutlineRect.bottom, mOutlineRect.right, mOutlineRect.bottom, mPaint);
                }
            }
        }
    }
}

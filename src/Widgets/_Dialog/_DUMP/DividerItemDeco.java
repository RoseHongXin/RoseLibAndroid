package hx.widget.adapterview.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by RoseHongXin on 2019/11/15 0015.
 */

public class DividerItemDeco extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    public static final int BOTH = 2;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider, android.R.attr.colorEdgeEffect};

    private Drawable mDivider;
    private RecyclerView.LayoutManager mLayoutMgr;

    private int mOrientation;
    private Context mCtx;

    private final Rect mBounds = new Rect();

    public DividerItemDeco(Context context, RecyclerView.LayoutManager layoutMgr) {
        this.mCtx = context;
        if(layoutMgr instanceof GridLayoutManager){
            setOrientation(BOTH);
        }else if(layoutMgr instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutMgr).getOrientation();
            setOrientation(orientation);
        }
        mLayoutMgr = layoutMgr;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        int color = a.getColor(1, context.getResources().getColor(android.R.color.darker_gray));
//        mDivider = a.getDrawable(0);
        setColor(color);
        a.recycle();
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            orientation = BOTH;
        }
        mOrientation = orientation;
    }


    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }
    public void setColor(@ColorInt int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        Bitmap bitmap = Bitmap.createBitmap(100, 2, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, 100, 2);
        drawable.draw(canvas);
        byte[] chunk = bitmap.getNinePatchChunk();
        boolean valid = NinePatch.isNinePatchChunk(chunk);
        NinePatch ninePatch  = new NinePatch(bitmap, chunk);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(mCtx.getResources(), ninePatch);
        NinePatchDrawable.
        mDivider = ninePatchDrawable;
    }

    @Nullable
    public Drawable getDrawable() {
        return mDivider;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(mOrientation == BOTH){
//            drawGrid(c, parent);
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        }else{
            if (mOrientation == VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
    }

    private void drawGrid(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        int top;
        int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(left, top, right, bottom);
        } else {
            left = 0;
            right = parent.getWidth();
            top = 0;
            bottom = parent.getHeight();
        }
        int spanCount = ((GridLayoutManager)mLayoutMgr).getSpanCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
//            parent.getDecoratedBoundsWithMargins(child, mBounds);
            mLayoutMgr.getDecoratedBoundsWithMargins(child, mBounds);
            bottom = mBounds.bottom + Math.round(child.getTranslationY());
            top = bottom - mDivider.getIntrinsicHeight();
            right = mBounds.right + Math.round(child.getTranslationX());
            left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if(mOrientation == BOTH){
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }else{
            if (mOrientation == VERTICAL) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }
}

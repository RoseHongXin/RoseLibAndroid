package hx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import hx.lib.R;

/**
 * Created by rose on 17-09-12.
 *
 */

public class TabItem extends FrameLayout {


    public TabItem(Context context) {
        super(context);
        init(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs){
        View layout = inflate(ctx, R.layout.l_tabitem, null);
        addView(layout);
        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.TabItem);
        ta.recycle();
    }
    
}

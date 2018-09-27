package hx.view.header;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rose on 16-8-25.
 *
 *
 */
public class IvHeader extends CircleImageView {

    //final int WIDTH = 96;
    //final int HEIGHT = 96;
    final int DEFAULT = 42;
    final int STEP = 10;
    int SIZE;

    public IvHeader(Context context) {
        super(context);
        init(context);
    }

    public IvHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IvHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context ctx){
        /*DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int density = metrics.densityDpi;
        if(density == DisplayMetrics.DENSITY_LOW) SIZE = DEFAULT - STEP * 2;
        else if(density == DisplayMetrics.DENSITY_MEDIUM) SIZE = DEFAULT - STEP;
        else if(density == DisplayMetrics.DENSITY_HIGH) SIZE = DEFAULT;
        else if(density == DisplayMetrics.DENSITY_XHIGH) SIZE = DEFAULT + STEP;
        else if(density == DisplayMetrics.DENSITY_XXHIGH) SIZE = DEFAULT + STEP * 2;
        else if(density == DisplayMetrics.DENSITY_XXXHIGH) SIZE = DEFAULT + STEP * 3;
        else SIZE = DEFAULT;*/

        SIZE = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT, ctx.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        //int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        //int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定

        if(widthMode != MeasureSpec.EXACTLY) widthMeasureSpec = MeasureSpec.makeMeasureSpec(SIZE, MeasureSpec.EXACTLY);
        if(heightMode != MeasureSpec.EXACTLY) heightMeasureSpec = MeasureSpec.makeMeasureSpec(SIZE, MeasureSpec.EXACTLY);

        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        //setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
}

package hx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import hx.lib.R;

/**
 * Created by rose on 16-8-11.
 */

public class LinearItem extends FrameLayout {

    final static private int NOTHING = -1;

    public TextView _tv_itemLeft;
    public TextView _tv_itemRight;

    @DrawableRes int iconLeft, iconRight;
    @ColorInt int colorTextLeft, colorTextRight;
    int sizeIconLeft, sizeIconRight;
    int sizeTextLeft, sizeTextRight;
    String textLeft, textRight;
    int gapLeft, gapRight, paddingHorizontal, paddingVertical;
    boolean clickRectRight;


    public void paddingHorizontal(int paddingHorizontal) {
        this.paddingHorizontal = paddingHorizontal;
        //因为有drawableRight在,paddingHorizontal的右边,就不设值了.
        _tv_itemRight.setPadding(paddingHorizontal, _tv_itemRight.getPaddingTop(), _tv_itemRight.getPaddingRight(), _tv_itemRight.getPaddingBottom());
    }
    public void paddingVertical(int paddingVertical) {
        this.paddingVertical = paddingVertical;
        _tv_itemRight.setPadding(_tv_itemRight.getPaddingLeft(), paddingVertical, _tv_itemRight.getPaddingRight(), paddingVertical);
    }
    public void gapLeft(int gap) {
        this.gapLeft = gap;
        _tv_itemLeft.setCompoundDrawablePadding(gap);
    }
    public void gapRight(int gap) {
        this.gapRight = gap;
        _tv_itemRight.setCompoundDrawablePadding(gap);
    }
    public void iconLeft(@DrawableRes int icon) {
        this.iconLeft = icon;
        _tv_itemLeft.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
    }
    public void iconRight(@DrawableRes int icon) {
        this.iconRight = icon;
        _tv_itemRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
    }
    public int sizeIconLeft() {
        return sizeIconLeft;
    }
    public void sizeIconLeft(int size) {
        this.sizeIconLeft = size;
//        ViewGroup.LayoutParams params = _iv_itemLeft.getLayoutParams();
//        params.width = size;
//        params.height = size;
//        _iv_itemLeft.setLayoutParams(params);
    }
    public int sizeIconRight() {
        return sizeIconRight;
    }
    public void sizeIconRight(int size) {
        this.sizeIconRight = size;
//        ViewGroup.LayoutParams params = _iv_itemRight.getLayoutParams();
//        params.width = size;
//        params.height = size;
//        _iv_itemRight.setLayoutParams(params);
    }

    public void colorTextLeft(@ColorInt int color) {
        this.colorTextLeft = color;
        _tv_itemLeft.setTextColor(color);
    }
    public void colorTextRight(@ColorInt int color) {
        this.colorTextRight = color;
        _tv_itemRight.setTextColor(color);
    }
    public String textLeft() {
        return textLeft;
    }
    public void textLeft(String text) {
        this.textLeft = text;
        _tv_itemLeft.setText(text);
    }
    public String textRight() {
        return textRight;
    }
    public void textRight(String text) {
        this.textRight = text;
        _tv_itemRight.setText(text);
    }

    public void sizeTextLeft(int size) {
        this.sizeTextLeft = size;
        _tv_itemLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
    public void sizeTextRight(int size) {
        this.sizeTextRight = size;
        _tv_itemRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void text(String text){
        textLeft(text);
    }
    public void icon(@DrawableRes int drawableRes){
        iconLeft(drawableRes);
    }

    public LinearItem(Context context) {
        super(context);
        init(context, null);
    }

    public LinearItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinearItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs){
        LayoutInflater.from(ctx).inflate(R.layout.l_linearitem, this, true);
        _tv_itemLeft = (TextView)findViewById(R.id._tv_lineItemLeft);
        _tv_itemRight = (TextView)findViewById(R.id._tv_lineItemRight);

        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.LinearItem);

        gapLeft = ta.getDimensionPixelSize(R.styleable.LinearItem_gapLeft, NOTHING); if(gapLeft != NOTHING) gapLeft(gapLeft);
        gapRight = ta.getDimensionPixelSize(R.styleable.LinearItem_gapRight, NOTHING); if(gapRight != NOTHING) gapRight(gapRight);
        paddingHorizontal = ta.getDimensionPixelSize(R.styleable.LinearItem_paddingHorizontal, NOTHING); if(paddingHorizontal != NOTHING) paddingHorizontal(paddingHorizontal);
        paddingVertical = ta.getDimensionPixelSize(R.styleable.LinearItem_paddingVertical, NOTHING); if(paddingVertical != NOTHING) paddingVertical(paddingVertical);

        iconLeft = ta.getResourceId(R.styleable.LinearItem_iconLeft, NOTHING); if(iconLeft != NOTHING) iconLeft(iconLeft);
        iconRight = ta.getResourceId(R.styleable.LinearItem_iconRight, NOTHING); if(iconRight != NOTHING) iconRight(iconRight);
        sizeIconLeft = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeIconLeft, NOTHING); if(sizeIconLeft != NOTHING) sizeIconLeft(sizeIconLeft);
        sizeIconRight = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeIconRight, NOTHING); if(sizeIconRight != NOTHING) sizeIconRight(sizeIconRight);

        textLeft = ta.getString(R.styleable.LinearItem_textLeft); if(!TextUtils.isEmpty(textLeft)) textLeft(textLeft);
        textRight = ta.getString(R.styleable.LinearItem_textRight); if(!TextUtils.isEmpty(textRight)) textRight(textRight);
        colorTextLeft = ta.getColor(R.styleable.LinearItem_colorTextLeft, NOTHING); if(colorTextLeft != NOTHING) colorTextLeft(colorTextLeft);
        colorTextRight = ta.getColor(R.styleable.LinearItem_colorTextRight, NOTHING); if(colorTextRight != NOTHING) colorTextRight(colorTextRight);
        sizeTextLeft = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeTextLeft, NOTHING); if(sizeTextLeft != NOTHING) sizeTextLeft(sizeTextLeft);
        sizeTextRight = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeTextRight, NOTHING); if(sizeTextRight != NOTHING) sizeTextRight(sizeTextRight);

        clickRectRight = ta.getBoolean(R.styleable.LinearItem_clickRectRight, false);

        ta.recycle();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        Drawable[] drawables = _tv_itemRight.getCompoundDrawables();
        if(clickRectRight && (!TextUtils.isEmpty(_tv_itemRight.getText()) || drawables[2] != null)){
            _tv_itemRight.setClickable(true);
            _tv_itemRight.setOnClickListener(v -> {
                if(l != null) l.onClick(LinearItem.this);
            });
        }else{
            super.setOnClickListener(l);
        }
    }
}

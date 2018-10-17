package hx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hx.lib.R;

/**
 * Created by rose on 16-8-11.
 */

public class LinearItem extends FrameLayout {

    final static private int NOTHING_SET = -1;

    private ImageView _iv_itemLeft;
    private TextView _tv_itemLeft;
    private ImageView _iv_itemRight;
    private TextView _tv_itemRight;

    @DrawableRes int iconLeft, iconRight;
    @ColorInt int textColorLeft, textColorRight;
    int iconSizeLeft, iconSizeRight;
    int textSizeLeft, textSizeRight;
    String textLeft, textRight;
    int gapLeft, gapRight, paddingHorizontal, paddingVertical;

    public ImageView _iv_left() {
        return _iv_itemLeft;
    }
    public ImageView _iv_right() {
        return _iv_itemRight;
    }
    public TextView _tv_left() {
        return _tv_itemLeft;
    }
    public TextView _tv_right() {
        return _tv_itemRight;
    }

    public int getPaddingHorizontal() {
        return paddingHorizontal;
    }
    public void setPaddingHorizontal(int paddingHorizontal) {
        this.paddingHorizontal = paddingHorizontal;
        setPadding(paddingHorizontal, getPaddingTop(), paddingHorizontal, getPaddingBottom());
    }
    public int getPaddingVertical() {
        return paddingVertical;
    }
    public void setPaddingVertical(int paddingVertical) {
        this.paddingVertical = paddingVertical;
        setPadding(getPaddingLeft(), paddingVertical, getPaddingRight(), paddingVertical);
    }
    public int getGapLeft() {
        return gapLeft;
    }
    public void setGapLeft(int gapLeft) {
        this.gapLeft = gapLeft;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _iv_itemLeft.getLayoutParams();
        params.rightMargin = gapLeft;
        _iv_itemLeft.setLayoutParams(params);
    }
    public int getGapRight() {
        return gapRight;
    }
    public void setGapRight(int gapRight) {
        this.gapRight = gapRight;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _tv_itemRight.getLayoutParams();
        params.rightMargin = gapRight;
        _tv_itemRight.setLayoutParams(params);
    }
    @DrawableRes public int getIconLeft() {
        return iconLeft;
    }
    public void setIconLeft(@DrawableRes int iconLeft) {
        this.iconLeft = iconLeft;
        _iv_itemLeft.setVisibility(VISIBLE);
        _iv_itemLeft.setImageResource(iconLeft);
    }
    @DrawableRes public int getIconRight() {
        return iconRight;
    }
    public void setIconRight(@DrawableRes int iconRight) {
        this.iconRight = iconRight;
        _iv_itemRight.setVisibility(VISIBLE);
        _iv_itemRight.setImageResource(iconRight);
    }
    public int getIconSizeLeft() {
        return iconSizeLeft;
    }
    public void setIconSizeLeft(int iconSizeLeft) {
        this.iconSizeLeft = iconSizeLeft;
        ViewGroup.LayoutParams params = _iv_itemLeft.getLayoutParams();
        params.width = iconSizeLeft;
        params.height = iconSizeLeft;
        _iv_itemLeft.setLayoutParams(params);
    }
    public int getIconSizeRight() {
        return iconSizeRight;
    }
    public void setIconSizeRight(int iconSizeRight) {
        this.iconSizeRight = iconSizeRight;
        ViewGroup.LayoutParams params = _iv_itemRight.getLayoutParams();
        params.width = iconSizeRight;
        params.height = iconSizeRight;
        _iv_itemRight.setLayoutParams(params);
    }

    public int getTextColorLeft() {
        return textColorLeft;
    }
    public void setTextColorLeft(int textColorLeft) {
        this.textColorLeft = textColorLeft;
        _tv_itemLeft.setTextColor(textColorLeft);
    }
    public int getTextColorRight() {
        return textColorRight;
    }
    public void setTextColorRight(int textColorRight) {
        this.textColorRight = textColorRight;
        _tv_itemRight.setTextColor(textColorRight);
    }
    public String getTextLeft() {
        return textLeft;
    }
    public void setTextLeft(String textLeft) {
        this.textLeft = textLeft;
        _tv_itemLeft.setVisibility(VISIBLE);
        _tv_itemLeft.setText(textLeft);
    }
    public String getTextRight() {
        return textRight;
    }
    public void setTextRight(String textRight) {
        this.textRight = textRight;
        _tv_itemRight.setVisibility(VISIBLE);
        _tv_itemRight.setText(textRight);
    }
    public int getTextSizeLeft() {
        return textSizeLeft;
    }
    public void setTextSizeLeft(int textSizeLeft) {
        this.textSizeLeft = textSizeLeft;
        _tv_itemLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeLeft);
    }
    public int getTextSizeRight() {
        return textSizeRight;
    }
    public void setTextSizeRight(int textSizeRight) {
        this.textSizeRight = textSizeRight;
        _tv_itemRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeRight);
    }

    public void setText(String text){
        setTextLeft(text);
    }
    public void setIcon(@DrawableRes int drawableRes){
        setIconLeft(drawableRes);
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
        LayoutInflater.from(ctx).inflate(R.layout.l_lineitem, this, true);
        _iv_itemLeft = (ImageView)findViewById(R.id._iv_lineItemLeft);
        _tv_itemLeft = (TextView)findViewById(R.id._tv_lineItemLeft);
        _iv_itemRight = (ImageView)findViewById(R.id._iv_lineItemRight);
        _tv_itemRight = (TextView)findViewById(R.id._tv_lineItemRight);

        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.LinearItem);

        gapLeft = ta.getDimensionPixelSize(R.styleable.LinearItem_gapLeft, NOTHING_SET); if(gapLeft != NOTHING_SET) setGapLeft(gapLeft);
        gapRight = ta.getDimensionPixelSize(R.styleable.LinearItem_gapRight, NOTHING_SET); if(gapRight != NOTHING_SET) setGapRight(gapRight);

        paddingHorizontal = ta.getDimensionPixelSize(R.styleable.LinearItem_paddingHorizontal, NOTHING_SET); if(paddingHorizontal != NOTHING_SET) setPaddingHorizontal(paddingHorizontal);
        paddingVertical = ta.getDimensionPixelSize(R.styleable.LinearItem_paddingVertical, NOTHING_SET); if(paddingVertical != NOTHING_SET) setPaddingVertical(paddingVertical);

        iconLeft = ta.getResourceId(R.styleable.LinearItem_iconLeft, NOTHING_SET); if(iconLeft != NOTHING_SET) setIconLeft(iconLeft);
        iconRight = ta.getResourceId(R.styleable.LinearItem_iconRight, NOTHING_SET); if(iconRight != NOTHING_SET) setIconRight(iconRight);
        iconSizeLeft = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeIconLeft, NOTHING_SET); if(iconSizeLeft != NOTHING_SET) setIconSizeLeft(iconSizeLeft);
        iconSizeRight = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeIconRight, NOTHING_SET); if(iconSizeRight != NOTHING_SET) setIconSizeRight(iconSizeRight);


        textLeft = ta.getString(R.styleable.LinearItem_textLeft); if(!TextUtils.isEmpty(textLeft)) setTextLeft(textLeft);
        textRight = ta.getString(R.styleable.LinearItem_textRight); if(!TextUtils.isEmpty(textRight)) setTextRight(textRight);

        textColorLeft = ta.getColor(R.styleable.LinearItem_colorTextLeft, NOTHING_SET); if(textColorLeft != NOTHING_SET) setTextColorLeft(textColorLeft);
        textColorRight = ta.getColor(R.styleable.LinearItem_colorTextRight, NOTHING_SET); if(textColorRight != NOTHING_SET) setTextColorRight(textColorRight);
        textSizeLeft = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeTextLeft, NOTHING_SET); if(textSizeLeft != NOTHING_SET) setTextSizeLeft(textSizeLeft);
        textSizeRight = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeTextRight, NOTHING_SET); if(textSizeRight != NOTHING_SET) setTextSizeRight(textSizeRight);

        ta.recycle();
    }

}

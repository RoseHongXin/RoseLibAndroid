package hx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    @DrawableRes private int iconL, iconR;
    @ColorInt private int colorL, colorR;
    private int iconSizeL, iconSizeR;
    private int textSizeL, textSizeR;
    private String textL, textR;
    private String hintL, hintR;
    private int gapL, gapR, paddingH, paddingV;

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

    public void paddingH(int padding) {
        this.paddingH = padding;
        setPadding(padding, getPaddingTop(), padding, getPaddingBottom());
    }
    public void paddingV(int padding) {
        this.paddingV = padding;
        setPadding(getPaddingLeft(), padding, getPaddingRight(), padding);
    }
    public void gapL(int gap) {
        this.gapL = gap;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _iv_itemLeft.getLayoutParams();
        params.rightMargin = gap;
        _iv_itemLeft.setLayoutParams(params);
    }
    public void gapR(int gap) {
        this.gapR = gap;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _tv_itemRight.getLayoutParams();
        params.rightMargin = gap;
        _tv_itemRight.setLayoutParams(params);
    }
    public void iconL(@DrawableRes int icon) {
        this.iconL = icon;
        if(_iv_itemLeft.getVisibility() != VISIBLE) _iv_itemLeft.setVisibility(VISIBLE);
        _iv_itemLeft.setImageResource(icon);
    }
    public void iconR(@DrawableRes int icon) {
        this.iconR = icon;
        if(_iv_itemRight.getVisibility() != VISIBLE) _iv_itemRight.setVisibility(VISIBLE);
        _iv_itemRight.setImageResource(icon);
    }
    public void iconSizeL(int size) {
        this.iconSizeL = size;
        ViewGroup.LayoutParams params = _iv_itemLeft.getLayoutParams();
        params.width = size;
        params.height = size;
        _iv_itemLeft.setLayoutParams(params);
    }
    public void iconSizeR(int size) {
        this.iconSizeR = size;
        ViewGroup.LayoutParams params = _iv_itemRight.getLayoutParams();
        params.width = size;
        params.height = size;
        _iv_itemRight.setLayoutParams(params);
    }

    public void colorL(int color) {
        this.colorL = color;
        _tv_itemLeft.setTextColor(color);
    }
    public void colorR(int color) {
        this.colorR = color;
        _tv_itemRight.setTextColor(color);
    }
    public void textSizeL(int size) {
        this.textSizeL = size;
        _tv_itemLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
    public void textSizeR(int size) {
        this.textSizeR = size;
        _tv_itemRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public String textL() {
        return textL;
    }
    public void textL(Object text) {
        if(text instanceof String){
            textL = (String) text;
        }else if(text instanceof Integer){
            textL = getResources().getString((int)text);
        }
        if(_tv_itemLeft.getVisibility() != VISIBLE) _tv_itemLeft.setVisibility(VISIBLE);
        _tv_itemLeft.setText(textL);
    }
    public String textR() {
        return textR;
    }
    public void textR(Object text) {
        if(text instanceof String){
            textR = (String) text;
        }else if(text instanceof Integer){
            textR = getResources().getString((int)text);
        }
        if(_tv_itemRight.getVisibility() != VISIBLE) _tv_itemRight.setVisibility(VISIBLE);
        _tv_itemRight.setText(textR);
    }
    public void text(Object text){
        textR(text);
    }
    public String text(){
        return textR();
    }
    public void hintL(Object text) {
        if(text instanceof String){
            hintL = (String) text;
        }else if(text instanceof Integer){
            hintL = getResources().getString((int)text);
        }
        if(_tv_itemLeft.getVisibility() != VISIBLE) _tv_itemLeft.setVisibility(VISIBLE);
        _tv_itemLeft.setHint(hintL);
    }
    public void hintR(Object text) {
        if(text instanceof String){
            hintR = (String) text;
        }else if(text instanceof Integer){
            hintR = getResources().getString((int)text);
        }
        if(_tv_itemRight.getVisibility() != VISIBLE) _tv_itemRight.setVisibility(VISIBLE);
        _tv_itemRight.setHint(hintR);
    }

    public void icon(@DrawableRes int drawableRes){
        iconR(drawableRes);
    }
    public void color(@ColorInt int color) {
        colorL(color);
        colorR(color);
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
        _iv_itemLeft = (ImageView)findViewById(R.id._iv_linearItemLeft);
        _tv_itemLeft = (TextView)findViewById(R.id._tv_linearItemLeft);
        _iv_itemRight = (ImageView)findViewById(R.id._iv_linearItemRight);
        _tv_itemRight = (TextView)findViewById(R.id._tv_linearItemRight);

        _iv_itemLeft.setVisibility(GONE);
        _tv_itemLeft.setVisibility(GONE);
        _iv_itemRight.setVisibility(GONE);
        _tv_itemRight.setVisibility(GONE);

        _tv_itemLeft.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                if(_tv_itemLeft.getVisibility() != VISIBLE) _tv_itemLeft.setVisibility(VISIBLE);
            }
        });
        _tv_itemRight.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                if(_tv_itemRight.getVisibility() != VISIBLE) _tv_itemRight.setVisibility(VISIBLE);
            }
        });

        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.LinearItem);

        gapL = ta.getDimensionPixelSize(R.styleable.LinearItem_gapLeft, NOTHING_SET); if(gapL != NOTHING_SET) gapL(gapL);
        gapR = ta.getDimensionPixelSize(R.styleable.LinearItem_gapRight, NOTHING_SET); if(gapR != NOTHING_SET) gapR(gapR);

        paddingH = ta.getDimensionPixelSize(R.styleable.LinearItem_paddingHorizontal, NOTHING_SET); if(paddingH != NOTHING_SET) paddingH(paddingH);
        paddingV = ta.getDimensionPixelSize(R.styleable.LinearItem_paddingVertical, NOTHING_SET); if(paddingV != NOTHING_SET) paddingV(paddingV);

        iconL = ta.getResourceId(R.styleable.LinearItem_iconLeft, NOTHING_SET); if(iconL != NOTHING_SET) iconL(iconL);
        iconR = ta.getResourceId(R.styleable.LinearItem_iconRight, NOTHING_SET); if(iconR != NOTHING_SET) iconR(iconR);
        iconSizeL = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeIconLeft, NOTHING_SET); if(iconSizeL != NOTHING_SET) iconSizeL(iconSizeL);
        iconSizeR = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeIconRight, NOTHING_SET); if(iconSizeR != NOTHING_SET) iconSizeR(iconSizeR);

        textL = ta.getString(R.styleable.LinearItem_textLeft); if(!TextUtils.isEmpty(textL)) textL(textL);
        textR = ta.getString(R.styleable.LinearItem_textRight); if(!TextUtils.isEmpty(textR)) textR(textR);
        hintL = ta.getString(R.styleable.LinearItem_hintLeft); if(!TextUtils.isEmpty(hintL)) hintL(hintL);
        hintR = ta.getString(R.styleable.LinearItem_hintRight); if(!TextUtils.isEmpty(hintR)) hintR(hintR);

        colorL = ta.getColor(R.styleable.LinearItem_colorTextLeft, getResources().getColor(R.color.textColorPrimary)); colorL(colorL);
        colorR = ta.getColor(R.styleable.LinearItem_colorTextRight, getResources().getColor(R.color.textColorPrimary)); colorR(colorR);
        textSizeL = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeTextLeft, NOTHING_SET); if(textSizeL != NOTHING_SET) textSizeL(textSizeL);
        textSizeR = ta.getDimensionPixelSize(R.styleable.LinearItem_sizeTextRight, NOTHING_SET); if(textSizeR != NOTHING_SET) textSizeR(textSizeR);

        ta.recycle();
    }

}

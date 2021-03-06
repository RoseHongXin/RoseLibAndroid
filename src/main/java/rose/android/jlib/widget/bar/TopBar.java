package rose.android.jlib.widget.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import rose.android.jlib.R;

/**
 * Created by RoseHongXin on 2018/1/26 0026.
 */

@Deprecated
public class TopBar extends AppBarLayout implements ITopBarOpt{

    public TopBarHelper mTopBarHelper;
    public Toolbar _tb_;
    public TextView _tv_tbTitle;
    public ImageView _iv_tbRight;
    public TextView _tv_tbRight;
    public @DrawableRes
    int mNavIcon;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.l_topbar_tb, this, true);
        mTopBarHelper = TopBarHelper.obtain(this);
        _tb_ = mTopBarHelper._tb_;
        _tv_tbTitle = mTopBarHelper._tv_tbTitle;
        _iv_tbRight = mTopBarHelper._iv_tbRight;
        _tv_tbRight = mTopBarHelper._tv_tbRight;
        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
            String title = ta.getString(R.styleable.TopBar_tb_title);
            int navDrawable = ta.getResourceId(R.styleable.TopBar_tb_navigation, 0);
            String text = ta.getString(R.styleable.TopBar_tb_text);
            int icon = ta.getResourceId(R.styleable.TopBar_tb_icon, 0);
            int color = ta.getColor(R.styleable.TopBar_tb_color, context.getResources().getColor(R.color.tb_txt));
            ta.recycle();
            title(title);
            navigation(navDrawable);
            text(text);
            icon(icon);
            color(color);
        }
    }

    @Override
    public void title(int strRes) {
        mTopBarHelper.title(strRes);
    }

    @Override
    public void title(String title) {
        mTopBarHelper.title(title);
    }

    @Override
    public void color(@ColorInt int color) {
        mTopBarHelper.color(color);
    }

    @Override
    public String title() {
        return mTopBarHelper.title();
    }

    @Override
    public void icon(@DrawableRes int iconRes, OnClickListener listener){
        mTopBarHelper.icon(iconRes, listener);
    }

    @Override
    public void icon(int iconRes) {
        mTopBarHelper.icon(iconRes);
    }

    public void text(String text, OnClickListener listener){
        mTopBarHelper.text(text, listener);
    }
    public void text(@StringRes int strRes, OnClickListener listener){
        mTopBarHelper.text(strRes, listener);
    }

    @Override
    public void text(String text) {
        mTopBarHelper.text(text);
    }

    @Override
    public void text(int strRes) {
        mTopBarHelper.text(strRes);
    }

    @Override
    public void right(OnClickListener listener) {
        mTopBarHelper.right(listener);
    }

    @Override
    public void navigation(AppCompatActivity act) {
        mTopBarHelper.navigation(act);
    }

    @Override
    public void navigation(int iconRes) {
        mNavIcon = iconRes;
        mTopBarHelper.navigation(iconRes);
    }

    @Override
    public void navigation(OnClickListener listener) {
        mTopBarHelper.navigation(listener);
    }

    @Override
    public void navigation(int iconRes, OnClickListener listener) {
        mTopBarHelper.navigation(iconRes);
        mTopBarHelper.navigation(listener);
    }
}

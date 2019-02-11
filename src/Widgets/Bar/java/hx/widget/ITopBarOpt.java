package hx.widget;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by RoseHongXin on 2018/3/14 0014.
 */

public interface ITopBarOpt {
    void title(@StringRes int strRes);
    void title(String title);
    void color(@ColorInt int colorRes);
    String title();
    void icon(@DrawableRes int iconRes, View.OnClickListener listener);
    void icon(@DrawableRes int iconRes);
    void text(String text, View.OnClickListener listener);
    void text(@StringRes int strRes, View.OnClickListener listener);
    void text(String text);
    void text(@StringRes int strRes);
    void right(View.OnClickListener listener);
    void navigation(AppCompatActivity act);
    void navigation(@DrawableRes int iconRes);
    void navigation(View.OnClickListener listener);
    void navigation(@DrawableRes int iconRes, View.OnClickListener listener);

}

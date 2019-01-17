package hx.kit.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;


/**
 * Created by RoseHongXin on 2017/9/15 0015.
 */

public class ViewKit {

    public static void hideInputMgr(@NonNull View... _vs) {
        Context ctx = _vs[0].getContext();
        InputMethodManager inputMgr = ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMgr == null) return;
        for (View _v : _vs) {
            inputMgr.hideSoftInputFromWindow(_v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            _v.clearFocus();
        }
    }

    public static void showInputMgr(@NonNull View _v) {
        _v.requestFocus();
        InputMethodManager inputMgr = ((InputMethodManager) _v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMgr == null) return;
        _v.postDelayed(() -> {
//        inputMgr.showSoftInputFromInputMethod(_v.getWindowToken(), InputMethodManager.SHOW_FORCED);
//        inputMgr.showSoftInput(_v, InputMethodManager.SHOW_FORCED);
            inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }, 100);
    }


    public static void tlTabFixWidth(TabLayout tabLayout) {
        tabLayout.post(() -> {
            try {
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                int dp10 = 24;
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);
                    TextView mTextView = (TextView) mTextViewField.get(tabView);
                    tabView.setPadding(0, 0, 0, 0);
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
//                        params.leftMargin = dp10;
//                        params.rightMargin = dp10;
                    tabView.setLayoutParams(params);
                    tabView.invalidate();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }
}

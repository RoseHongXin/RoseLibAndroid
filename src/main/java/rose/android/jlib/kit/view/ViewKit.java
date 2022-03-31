package rose.android.jlib.kit.view;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.google.android.material.tabs.TabLayout;
import rose.android.jlib.R;
import rose.android.jlib.kit.data.Formater;

import java.lang.reflect.Field;


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
    public static void deco(TextView _tv, Object head, Object tail) {
        deco(_tv, head, tail, null);
    }
    //tailDeco: String/Int, 表示textColor,  Float表示textSize
    public static void deco(TextView _tv, Object head, Object tail, Object tailDeco){
        String headText = "";
        String tailText = "";
        if(head instanceof String){ headText = (String)head;
        }else if(head instanceof Integer){ headText = _tv.getResources().getString((int)head); }
        if(tail instanceof String){ tailText = (String)tail;
        }else if(tail instanceof Integer){ tailText = _tv.getResources().getString((int)tail); }
        String text = headText + tailText;
        int count = text.length();
        Spannable spannable = new SpannableString(text);
        if(tailDeco instanceof String){
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor((String)tailDeco)), headText.length(), count, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }else if(tailDeco instanceof Integer){
            spannable.setSpan(new ForegroundColorSpan((int)tailDeco), headText.length(), count, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }else if(tailDeco instanceof Float){
            spannable.setSpan(new RelativeSizeSpan((float)tailDeco), headText.length(), count, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        _tv.setText(spannable);
    }
    public static void selectCheckToast(Context ctx, @StringRes int name){
        Toast.makeText(ctx, String.format(ctx.getString(R.string.HX_selectHintF), ctx.getString(name)), Toast.LENGTH_SHORT).show();
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

    public static boolean inputCheck(EditText... _et_s){
        for(EditText _et_ : _et_s) {
            String value = _et_.getText() == null ? "" : _et_.getText().toString();
            if (TextUtils.isEmpty(value)) {
                if (_et_.getHint() != null) Toast.makeText(_et_.getContext(), _et_.getHint().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
            if(_et_.getInputType() == InputType.TYPE_CLASS_PHONE){
                String phone = _et_.getText().toString();
                if(!Formater.isMobile(phone)){
                    Toast.makeText(_et_.getContext(), R.string.HX_phoneInputWrongToast, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else if(_et_.getInputType() == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS){

            }
        }
        return true;
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

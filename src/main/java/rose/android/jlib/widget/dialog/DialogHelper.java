package rose.android.jlib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import rose.android.jlib.R;

/**
 * Created by RoseHongXin on 2017/5/17 0017.
 */

public class DialogHelper {

    public static void NoPadding(Dialog dialog){
        Conf(dialog, Gravity.BOTTOM, 0, 0, 0, 0);
    }
    public static void NoPadding(Dialog dialog, int gravity){
        Conf(dialog, gravity, 0, 0, 0, 0);
    }

    public static void Padding(Dialog dialog, int gravity){
        Conf(dialog, gravity, 24, 24, 24, 24);
    }
    public static void Padding(Dialog dialog){
        Conf(dialog, Gravity.CENTER, 24, 24, 24, 24);
    }

    public static void Padding(Dialog dialog, int paddingHorizontal, int paddingVertical){
        Conf(dialog, Gravity.CENTER, paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }
    public static void Padding(Dialog dialog, int gravity, int paddingHorizontal, int paddingVertical){
        Conf(dialog, gravity, paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }

    private static void Conf(Dialog dialog, int gravity, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(gravity);
            window.getDecorView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            WindowManager.LayoutParams lp = window.getAttributes();
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            lp.width = metrics.widthPixels;
//            lp.height = metrics.heightPixels / 3;
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    public static AlertDialog BtnCenter(AlertDialog.Builder builder){
        return BtnCenter(builder, null);
    }
    public static AlertDialog BtnCenter(AlertDialog.Builder builder, DialogInterface.OnShowListener onShowListener){
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button positiveBt = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            LinearLayout.LayoutParams layoutParams  = (LinearLayout.LayoutParams) positiveBt.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.CENTER;
            positiveBt.setLayoutParams(layoutParams);
            positiveBt.setTextColor(positiveBt.getResources().getColor(R.color.colorAccent));
            if(onShowListener != null) onShowListener.onShow(dialog);
        });
        return dialog;
    }
    public static AlertDialog BtnFlat(AlertDialog dialog, AlertDialog.OnShowListener onShowListener){
        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
            Button _bt_positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            View _v_container = (View) _bt_positive.getParent();
            int width = _v_container.getMeasuredWidth() / 2 - _v_container.getPaddingLeft() - _v_container.getPaddingRight();
            LinearLayout.LayoutParams layoutParams  = (LinearLayout.LayoutParams) _bt_positive.getLayoutParams();
            layoutParams.width = width;
            layoutParams.gravity = Gravity.END|Gravity.CENTER_VERTICAL;
            _bt_positive.setLayoutParams(layoutParams);
            Button _bt_negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            layoutParams  = (LinearLayout.LayoutParams) _bt_negative.getLayoutParams();
            layoutParams.width = width;
            layoutParams.gravity = Gravity.START|Gravity.CENTER_VERTICAL;
            _bt_negative.setLayoutParams(layoutParams);
            if(onShowListener != null) onShowListener.onShow(dlg);
        });
        return dialog;
    }

    static String getText(Context ctx, Object text){
        if (text == null) return "";
        else if(text instanceof String) return (String) text;
        else if(text instanceof Integer) return ctx.getString((Integer) text);
        else return "";
    }

}

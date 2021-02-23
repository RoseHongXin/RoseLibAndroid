package hx.widget.dialog;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import hx.lib.R;

/**
 * Created by RoseHongXin on 2017/5/17 0017.
 */

public class DialogHelper {

    public static void erasePadding(Dialog dialog){
        config(dialog, Gravity.BOTTOM, 0, 0, 0, 0);
    }
    public static void erasePadding(Dialog dialog, int gravity){
        config(dialog, gravity, 0, 0, 0, 0);
    }

    public static void padding(Dialog dialog, int gravity){
        config(dialog, gravity, 24, 24, 24, 24);
    }
    public static void padding(Dialog dialog){
        config(dialog, Gravity.CENTER, 24, 24, 24, 24);
    }

    public static void padding(Dialog dialog, int paddingHorizontal, int paddingVertical){
        config(dialog, Gravity.CENTER, paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }
    public static void padding(Dialog dialog, int gravity, int paddingHorizontal, int paddingVertical){
        config(dialog, gravity, paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }

    private static void config(Dialog dialog, int gravity, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
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

    public static AlertDialog dlgButtonCenter(AlertDialog.Builder builder){
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button positiveBt = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            LinearLayout.LayoutParams layoutParams  = (LinearLayout.LayoutParams) positiveBt.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.CENTER;
            positiveBt.setLayoutParams(layoutParams);
            positiveBt.setTextColor(positiveBt.getResources().getColor(R.color.colorAccent));
        });

        return dialog;
    }

}
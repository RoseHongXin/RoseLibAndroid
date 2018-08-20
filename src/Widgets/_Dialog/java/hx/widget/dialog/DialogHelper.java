package hx.widget.dialog;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

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
            window.setGravity(gravity); //可设置dialog的位置
            window.getDecorView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            WindowManager.LayoutParams lp = window.getAttributes();
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            lp.width = metrics.widthPixels;   //设置宽度充满屏幕
//            lp.height = metrics.heightPixels / 3;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            window.setAttributes(lp);
        }
    }

}

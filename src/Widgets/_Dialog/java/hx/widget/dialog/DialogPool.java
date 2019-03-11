package hx.widget.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import hx.lib.R;

/**
 * Created by RoseHongXin on 2018/5/14 0014.
 */

public class DialogPool {

    public static void toast(@NonNull Activity act, @StringRes int msg) {
        toast(act, act.getString(msg));
    }
    public static void toast(@NonNull Activity act, String msg){
        toast(act, msg, null);
    }
    public static void toast(@NonNull Activity act, @StringRes int msg, DialogInterface.OnClickListener listener){
        toast(act, act.getString(msg), listener);
    }
    public static void toast(@NonNull Activity act, String msg, DialogInterface.OnClickListener listener){
        if(act.isFinishing() || act.isDestroyed()) return;
        DialogHelper.dlgButtonCenter(
                new AlertDialog.Builder(act)
                .setTitle(msg)
                .setCancelable(false)
                .setNeutralButton(R.string.HX_confirm, listener))
                .show();
    }
    public static void confirm(@NonNull Activity act, Object msg, DialogInterface.OnClickListener listener){
        confirm(act, msg, -1, listener);
    }
    public static void confirm(@NonNull Activity act, Object msg, Object positiveBt, DialogInterface.OnClickListener listener){
        if(act.isFinishing() || act.isDestroyed()) return;
        String message = "";
        if(msg instanceof String) {
            message = (String) msg;
        }else if(msg instanceof Integer) {
            int resId = (int)msg;
            if(resId != -1) message = act.getString(resId);
        }
        String btTxt = act.getString(R.string.HX_confirm);
        if(positiveBt instanceof String){
            btTxt = (String) positiveBt;
        }else if(positiveBt instanceof Integer){
            int resId = (int) positiveBt;
            if(resId != -1) btTxt = act.getString(resId);
        }
        new AlertDialog.Builder(act)
                .setTitle(message)
                .setCancelable(false)
                .setPositiveButton(btTxt, listener)
                .setNegativeButton(R.string.HX_cancel, null)
                .create()
                .show();
    }
}

package hx.widget.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

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
    public static void confirm(@NonNull Activity act, @StringRes int msg, DialogInterface.OnClickListener listener) {
        if(act.isFinishing() || act.isDestroyed()) return;
        confirm(act, act.getString(msg), listener);
    }
    public static void confirm(@NonNull Activity act, String msg, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(act)
                .setTitle(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.HX_confirm, listener)
                .setNegativeButton(R.string.HX_cancel, null)
                .create()
                .show();
    }
}
